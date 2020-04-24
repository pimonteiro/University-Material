import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;

public class Ex3 {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("tophighestnames");
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(60));

        JavaPairRDD<String,String> names = sc.sparkContext().textFile("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz")
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],t[2]));



        sc.socketTextStream("localhost", 12345)
                .window(Durations.minutes(10),Durations.seconds(60))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],Double.parseDouble(t[1])))
                .reduce((p1,p2) -> new Tuple2<>(p1._1,p1._2 + p2._2))
                .foreachRDD(rdd -> {
                    JavaPairRDD<Double,String> tmp = rdd
                            .mapToPair(t -> new Tuple2<>(t._1,t._2))
                            .join(names)
                            .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))
                            .sortByKey(false);
                    List<Tuple2<Double,String>> lst = tmp.collect();
                    lst = lst.subList(0,Math.min(3,lst.size()));
                    System.out.println(lst);
                });


        sc.start();
        sc.awaitTermination();
    }
}
