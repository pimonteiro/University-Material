import org.apache.spark.SparkConf;
import org.apache.spark.api.java.Optional;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.List;
import java.util.stream.StreamSupport;

public class Ex2 {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("tophighestids");

        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(60));
        sc.socketTextStream("localhost", 12345)
                .window(Durations.minutes(10),Durations.seconds(60))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],Double.parseDouble(t[1])))
                .groupByKey()
                .mapToPair(p -> new Tuple2<>(
                        StreamSupport.stream(p._2.spliterator(),false)
                        .mapToDouble(a -> a)
                        .average()
                        .getAsDouble(), p._1
                ))
                .foreachRDD(rdd -> {
                    List<Tuple2<Double, String>> lst = rdd.sortByKey(false).take(3);
                    System.out.println(lst.toString());
                });

        sc.start();
        sc.awaitTermination();
    }
}
