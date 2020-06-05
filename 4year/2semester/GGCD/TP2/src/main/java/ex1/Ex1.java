package ex1;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.time.LocalDateTime;
import java.util.Date;

public class Ex1 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Log");

        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(60));
        jsc.socketTextStream("localhost", 12345)
                .transform((rdd,time) -> rdd.map(t -> t.concat("\t" + (new Date(time.milliseconds())))))
                .window(Durations.seconds(60*10), Durations.seconds(60*10))
                .foreachRDD(rdd -> {
                    LocalDateTime time = LocalDateTime.now();
                    rdd.saveAsTextFile("results_ex1_a/"+ time.getHour() + "_" + time.getMinute());
                });

        jsc.start();
        jsc.awaitTermination();
    }
}
