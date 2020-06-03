package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;
import java.util.stream.StreamSupport;

public class Ex1 {
    public static void main(String[] args) throws InterruptedException {        //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Top10");


        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Tuple2<String, Long>> titles_actor = sc.textFile("/home/msi-gtfo/Downloads/imdb/title.principals.tsv.gz")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .filter(t -> t[3].contains("actor") || t[3].contains("actress") || t[3].contains("self"))
                .mapToPair(t -> new Tuple2<>(t[2],t[0]))    // <ID_ACTOR, ID_FILME>
                .groupByKey()
                .mapToPair(t -> new Tuple2<>(StreamSupport.stream(t._2.spliterator(), false).count(), t._1))
                .sortByKey(false)
                .mapToPair(t -> new Tuple2<>(t._2,t._1))
                .take(10);

        System.out.println(titles_actor.toString());
    }
}
