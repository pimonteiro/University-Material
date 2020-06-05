package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;
import java.util.stream.StreamSupport;

public class Ex1 {
    public static void main(String[] args) throws InterruptedException {        //DONE
        SparkConf conf = new SparkConf()
                .setAppName("Top10");


        JavaSparkContext sc = new JavaSparkContext(conf);

        long start = System.currentTimeMillis();

        List<Tuple2<String, Integer>> titles_actor = sc.textFile("hdfs://namenode:9000/input/title.principals.tsv")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .filter(t -> t[3].contains("actor") || t[3].contains("actress") || t[3].contains("self"))
                .mapToPair(t -> new Tuple2<>(t[2],1))    // <ID_ACTOR, Contador>
                .reduceByKey((t1,t2) -> t1+t2)
                .mapToPair(t -> new Tuple2<>(t._2,t._1))
                .sortByKey(false)
                .mapToPair(t -> new Tuple2<>(t._2,t._1))
                .take(10);
        long end = System.currentTimeMillis();
        System.out.println(titles_actor.toString());
        System.out.println("Time Elapsed: " + (end-start));
    }
}
