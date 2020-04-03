import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Ex1 {
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf().setAppName("Ex1");
        JavaSparkContext sc = new JavaSparkContext(conf);

        //Compute number of Movies by actor ---> <actor,[movies]>
        JavaPairRDD<String, Iterable<String>> m_actor = sc.textFile("hdfs://namenode:9000/input/title.principals_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[2], l[0]))
                .groupByKey();

        JavaPairRDD<String,String> actor_data = sc.textFile("hdfs://namenode:9000/input/name.basics_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0],l[1]));

        // Guy name, [Movies id]
        JavaPairRDD<String,Iterable<String>> joined_actor_data = m_actor.join(actor_data)
                .mapToPair(l -> new Tuple2<>(l._2._2,l._2._1)).persist(StorageLevel.DISK_ONLY());

        List<Tuple2<String, Iterable<String>>> res1 = joined_actor_data.collect();
        List<Tuple2<Integer, String>> top_10_actors = joined_actor_data.mapToPair(l -> new Tuple2<>(StreamSupport.stream(l._2.spliterator(),false).collect(Collectors.toList()).size(),l._1))
                .sortByKey(false).take(10);


        System.out.println("Finished collecting everything");
        File f = new File("results1.txt");
        f.createNewFile();
        FileWriter myWriter = new FileWriter("results1.txt");
        for(Tuple2<String, Iterable<String>> t : res1){
            myWriter.write(t._1+" -> "+ StreamSupport.stream(t._2.spliterator(), false).collect(Collectors.toList()).size());
        }
        myWriter.close();

        for(Tuple2<Integer, String> t : top_10_actors){
            System.out.println(t._2 + " -> " + t._1);
        }
    }
}
