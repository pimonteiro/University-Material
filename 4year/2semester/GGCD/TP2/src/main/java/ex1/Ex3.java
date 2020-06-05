package ex1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.stream.StreamSupport;


public class Ex3 {
    public static void main(String[] args) throws InterruptedException {        //DONE

        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Trending");


        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(60));
        jsc.checkpoint("checkpoint_ex1_c");

        Function3<String, Optional<Integer>, State<Integer>, String> mappingFunc =
                (k, v, s) -> {
                    int old_votes = s.exists() ? s.get() : 0;
                    int new_votes = v.get();
                    if(new_votes > old_votes) {
                        s.update(new_votes);
                        return k + "\t" + new_votes;
                    } else
                        s.remove();
                    return "";
                };

        JavaMapWithStateDStream<String, Integer, Integer, String> stateDstream = jsc
                .socketTextStream("localhost", 12345)
                .window(Durations.minutes(15), Durations.minutes(15))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],1))
                .reduceByKey((t1,t2) -> t1+t2)
                .mapWithState(StateSpec.function(mappingFunc));

        stateDstream.print();
        jsc.start();
        jsc.awaitTermination();
    }
}