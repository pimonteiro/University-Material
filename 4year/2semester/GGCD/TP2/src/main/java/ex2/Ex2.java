package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Ex2 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("Friends");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, String> names = sc.textFile("/home/msi-gtfo/Downloads/imdb/title.principals.tsv.gz")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0], t[2]))
                .cache();                   //<ID_FILME, ID_ACTOR>

        JavaPairRDD<String, List<String>> names_pair = names.join(names)
                .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))
                .groupByKey()       // <ID_ACTOR1, [ID_ACTOR2,ID_ACTOR3,...]>
                .mapToPair(t -> new Tuple2<>(t._1, StreamSupport.stream(t._2.spliterator(),false).collect(Collectors.toList())));

        names_pair.coalesce(1).saveAsTextFile("results_ex2_b");
    }
}
