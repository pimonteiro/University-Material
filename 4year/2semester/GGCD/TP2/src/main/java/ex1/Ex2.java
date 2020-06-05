package ex1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.List;
import java.util.stream.StreamSupport;

public class Ex2 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Top3");


        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(60));

        JavaPairRDD<String,String> names = jsc.sparkContext().textFile("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0],t[2]))
                .cache();


        jsc.socketTextStream("localhost", 12345)
                .window(Durations.minutes(10), Durations.seconds(60))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],new Tuple2<>(Double.parseDouble(t[1]),1)))
                .reduceByKey((t1,t2) -> new Tuple2<>(t1._1 + t2._1, t1._2 + t2._2))
                .mapToPair(t -> new Tuple2<>(t._1,t._2._1 / t._2._2))
                .foreachRDD(rdd -> {
                    JavaPairRDD<String, Double> tmp = rdd
                            .mapToPair(t -> new Tuple2<>(t._1,t._2))
                            .join(names)
                            .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))
                            .sortByKey(false)
                            .mapToPair(t -> new Tuple2<>(t._2,t._1));
                    List<Tuple2<String, Double>> lst = tmp.take(3);
                    System.out.println(lst.toString());
                });

        jsc.start();
        jsc.awaitTermination();
    }
}
