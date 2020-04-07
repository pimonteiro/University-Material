import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Ex2 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Ex2");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // <Movie_ID,String_Rating>
        JavaPairRDD<String,Float> movies_rating = sc.textFile("hdfs://namenode:9000/input/title.ratings_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0],Float.parseFloat(l[1])));

        // <movie_id,movie_name>
        JavaPairRDD<String,String> movies_data = sc.textFile("hdfs://namenode:9000/input/title.basics_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0],l[2]));

        // <movie_id, <movie_name,rating>
        JavaPairRDD<String,Tuple2<String,Float>> movie_rating_data = movies_data.join(movies_rating);


        //Compute number of Movies by actor ---> <actor_id,[<movie_name,rating>]>
        JavaPairRDD<String, ArrayList<Tuple2<String,Float>>> m_actor = sc.textFile("hdfs://namenode:9000/input/title.principals_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0], l[1]))           // <movie_id,[actor_id]>
                .join(movie_rating_data)                            // <movie_id,<actor_id,<movie_name,rating>>>
                .mapToPair(l -> new Tuple2<>(l._2._1,l._2._2))         // <actor_id,<movie_name,rating>>
                .groupByKey()
                .mapToPair(l -> new Tuple2<>(l._1,
                        StreamSupport.stream(l._2.spliterator(),false)
                            .sorted((a, b) -> b._2.compareTo(a._2))
                            .collect(Collectors.toList())
                ))
                .mapToPair(p -> new Tuple2<>(p._1, new ArrayList<>(p._2.subList(0, Math.min(p._2.size(), 3)))));

        // <actor_id,actor_name>
        JavaPairRDD<String,String> actor_data = sc.textFile("hdfs://namenode:9000/input/name.basics_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0],l[1]));

        // <actor_name,[<movie_name,rating>]>
        JavaPairRDD<String, ArrayList<Tuple2<String,Float>>> joined_actor_data = m_actor.join(actor_data)
                .mapToPair(p -> new Tuple2<>(p._2._2,p._2._1));         // <actor_name, [<movie_name,rating>]>

        List<Tuple2<String, ArrayList<Tuple2<String, Float>>>> res = joined_actor_data.collect();
        System.out.println("Finished Collecting EX2");
        for(Tuple2<String, ArrayList<Tuple2<String, Float>>> t : res){
            System.out.println("Actor: " + t._1);
            for(Tuple2<String, Float> tt : t._2){
                System.out.println(tt._1 + " -> " + tt._2);
            }
        }
    }
}
