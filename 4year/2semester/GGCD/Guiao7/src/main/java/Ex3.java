import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public class Ex3 {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("tophighestnames");
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(60));

        JavaPairRDD<String,String> names = sc.sparkContext().textFile("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz")
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],t[2]))
                .cache();



        sc.socketTextStream("localhost", 12345)
                .window(Durations.minutes(10),Durations.seconds(60))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],Double.parseDouble(t[1])))
                .groupByKey()
                .mapToPair(p -> new Tuple2<>(p._1,
                        StreamSupport.stream(p._2.spliterator(),false)
                                .mapToDouble(a -> a)
                                .average()
                                .getAsDouble()
                ))
                .foreachRDD(rdd -> {
                    JavaPairRDD<Double,String> tmp = rdd
                            .mapToPair(t -> new Tuple2<>(t._1,t._2))
                            .join(names)
                            .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))
                            .sortByKey(false);
                    List<Tuple2<Double,String>> lst = tmp.take(3);
                    System.out.println(lst.toString());
                });

        sc.start();
        sc.awaitTermination();
    }
}
