package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Ex3 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Ratings");

        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String,Tuple2<Double,Long>> old_votes = sc.textFile("/home/msi-gtfo/Downloads/imdb/title.ratings.tsv.gz")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0],new Tuple2<>(Double.parseDouble(t[1]),Long.parseLong(t[2]))))
                .cache();           // <ID_FILME, <Rating,Votos> >

        sc.textFile("results_ex1_a/*")
                .map(t -> t.split("\t"))
                .mapToPair(t -> new Tuple2<>(t[0],Long.parseLong(t[1])))
                .groupByKey()
                .join(old_votes)        //  <ID, <[votos],<Rating,N_Votos>>>
                .mapToPair(t -> {
                    List<Long> votos = StreamSupport.stream(t._2._1.spliterator(), false).collect(Collectors.toList());
                    double new_votes = 0;
                    double n_new_votes = 0;
                    for(Long v : votos){
                        new_votes += v;
                        n_new_votes++;
                    }
                    double previous_votes = t._2._2._1 * t._2._2._2;
                    long new_total =  (long) n_new_votes + t._2._2._2;
                    double final_av = (previous_votes + new_votes) / new_total;
                    DecimalFormat df = new DecimalFormat("#.#");
                    String new_final_value = "\t" + df.format(final_av) + "\t" + new_total;
                    return new Tuple2<>(t._1,new_final_value);
                })
                .sortByKey(true)
                .coalesce(1).saveAsTextFile("results_ex2_c");

    }
}
