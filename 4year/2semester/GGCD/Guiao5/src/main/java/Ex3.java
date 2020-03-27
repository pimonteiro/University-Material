import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Ex3 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("genres-movies");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, Float> ratings = sc.textFile("file:///home/msi-gtfo/Downloads/imdb_01%/title.ratings.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .filter(l -> !l[1].equals("\\N"))
                .mapToPair(l -> new Tuple2<>(l[0], Float.parseFloat(l[1])));

        JavaPairRDD<String,String> movies = sc.textFile("file:///home/msi-gtfo/Downloads/imdb_01%/title.basics.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .filter(l -> !l[1].equals("\\N"))
                .mapToPair(l -> new Tuple2<>(l[0],l[2]));

        JavaPairRDD<String, Tuple2<Float,String>> joined = ratings.join(movies);
        List<Tuple2<String,Tuple2<Float,String>>> res = joined.collect();

        for(Tuple2<String,Tuple2<Float,String>> a : res){
            System.out.println(a._2._2 + " -> " + a._2._1);
        }

    }

}
