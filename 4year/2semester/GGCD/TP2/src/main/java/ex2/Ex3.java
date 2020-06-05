package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import java.text.DecimalFormat;

public class Ex3 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setAppName("Ratings");

        JavaSparkContext sc = new JavaSparkContext(conf);

        long start = System.currentTimeMillis();

        JavaPairRDD<String,Tuple2<Double,Long>> old_votes = sc.textFile("hdfs://namenode:9000/input/title.ratings.tsv")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0],new Tuple2<>(Double.parseDouble(t[1]),Long.parseLong(t[2]))))
                .cache();           // <ID_FILME, <Rating,Votos> >

        sc.textFile("hdfs://namenode:9000/results/results_ex1_a/*")
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0], new Tuple2<>(Long.parseLong(t[1]),1)))
                .reduceByKey((t1,t2) -> new Tuple2<>(t1._1 + t2._1, t1._2 + t2._2))         // <ID, <Soma_Novos_Votos,N_Votos>>
                .join(old_votes)        //  <ID, <<Soma_Novos_Votos,N_Votos>,<Rating,N_Votos>>>
                .mapToPair(t -> {
                    double previous_votes = t._2._2._2 * t._2._2._1;
                    double new_total = (long) t._2._1._2 + t._2._2._2;
                    double final_av = (previous_votes + t._2._1._1) / new_total;
                    DecimalFormat df = new DecimalFormat("#.#");
                    String new_final_value = "\t" + df.format(final_av) + "\t" + new_total;
                    return new Tuple2<>(t._1,new_final_value);
                })
                .saveAsTextFile("hdfs://namenode:9000/results/results_ex2_c");


        long end = System.currentTimeMillis();
        System.out.println("Time Elapsed: " + (end-start));
    }
}
