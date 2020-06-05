package ex2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;


public class Ex2 {
    public static void main(String[] args) throws InterruptedException {            //DONE
        SparkConf conf = new SparkConf()
                .setAppName("Friends");
        JavaSparkContext sc = new JavaSparkContext(conf);

        long start = System.currentTimeMillis();

        JavaPairRDD<String, String> names = sc.textFile("hdfs://namenode:9000/input/title.principals.tsv")
                .map(t -> t.split("\t"))
                .filter(t -> !t[0].equals("tconst"))
                .mapToPair(t -> new Tuple2<>(t[0], t[2]))
                .cache();                   //<ID_FILME, ID_ACTOR>

        names.join(names)    // <_ID_FILME, <ID_ACTOR, ID_ACTOR>>
                .mapToPair(t -> new Tuple2<>(t._2._1,t._2._2))      // <ID_ACTOR1, ID_ACTOR2>
                .groupByKey()       // <ID_ACTOR1, [ID_ACTOR2,ID_ACTOR3,...]>
                .saveAsTextFile("hdfs://namenode:9000/results/results_ex2_b");

        long end = System.currentTimeMillis();
        System.out.println("Time Elapsed: " + (end-start));
    }
}
