package ex1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

public class Complete_EX1 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Ex1");

        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(60));
        jsc.checkpoint("checkpoint_ex1");

        JavaReceiverInputDStream<String> stream = jsc.socketTextStream("localhost", 12345);

        // A
        stream.transform((rdd,time) -> rdd.map(t -> t.concat("\t" + time)))
                .window(Durations.seconds(60*10), Durations.seconds(60*10))
                .foreachRDD(rdd -> {
                    LocalDateTime time = LocalDateTime.now();
                    rdd.saveAsTextFile("results_ex1_a/"+ time.getHour() + "_" + time.getMinute());
                });

        // B
        JavaPairRDD<String,String> names = jsc.sparkContext().textFile("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0],t[2]))
                .cache();


        stream.window(Durations.minutes(10), Durations.seconds(60))
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
                    JavaPairRDD<String, Double> tmp = rdd
                            .mapToPair(t -> new Tuple2<>(t._1,t._2))
                            .join(names)
                            .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))
                            .sortByKey(false)
                            .mapToPair(t -> new Tuple2<>(t._2,t._1));
                    List<Tuple2<String, Double>> lst = tmp.take(3);
                    System.out.println(lst.toString());
                });

        // C
        Function3<String, Optional<Double>, State<Double>, String> mappingFunc =
                (k, v, s) -> {
                    double old_votes = s.exists() ? s.get() : 0;
                    double new_votes = v.get();
                    if(new_votes > old_votes) {
                        s.update(new_votes);
                        return k + "\t" + new_votes;
                    } else
                        s.remove();
                    return "";
                };

        JavaMapWithStateDStream<String, Double, Double, String> stateDstream = stream
                .window(Durations.minutes(15), Durations.minutes(15))
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],Double.parseDouble(t[1])))
                .mapWithState(StateSpec.function(mappingFunc));
        stateDstream.print();

        jsc.start();
        jsc.awaitTermination();
    }

}
