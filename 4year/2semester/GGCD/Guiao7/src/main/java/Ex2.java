import org.apache.spark.SparkConf;
import org.apache.spark.api.java.Optional;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.List;

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
                .reduce((p1,p2) -> new Tuple2<>(p1._1,p1._2 + p2._2))
                .foreachRDD(rdd -> {
                    List<Tuple2<String,Double>> lst = rdd.sortBy(t -> t._2,false,1).collect();
                    lst = lst.subList(0,Math.min(3,lst.size()));
                    System.out.println(lst);
                });


        sc.start();
        sc.awaitTermination();
    }
}
