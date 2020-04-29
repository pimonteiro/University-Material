import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class Ex1 {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("simplestream");

        /*
        JavaStreamingContext jsc = JavaStreamingContext.getOrCreate("/tmp/stream", () -> {
            JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(60));
            sc.socketTextStream("localhost", 12345)
                    .map(l->l.toUpperCase())
                    .print();
            sc.checkpoint("/tmp/stream");
            return sc;
        });
        */
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(60));
        jsc.socketTextStream("localhost", 12345)
                .foreachRDD(rdd -> {
                    rdd.saveAsTextFile("/Users/JoaoPimentel/desktop/4ANO/GGCD/Resolucoes/G7");
                });

        jsc.start();
        jsc.awaitTermination();
    }
}
