import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Ex1 {

    public static void main(String[] args){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("genres-movies");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String,Integer> mr = sc.textFile("file:///home/msi-gtfo/Downloads/title.basics.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .map(l -> l[8])
                .filter(l -> !l.equals("\\N"))
                .flatMap(l -> Arrays.asList(l.split(",")).iterator())
                .mapToPair(l -> new Tuple2<>(l,1))
                .foldByKey(0,(v1,v2) -> v1 + v2);

        double start = System.currentTimeMillis();
        List<Tuple2<String,Integer>> genres = mr.collect();
        double end = System.currentTimeMillis();

        for(Tuple2<String,Integer> g : genres)
            System.out.println(g._1 + " -> " + g._2);
        System.out.println("Time to process: " + (end-start) + "ms");
    }
}

/*
    0.1% of the File           -> 800ms
    1% of the File             -> 900ms
    100% of the File (bzip2)   -> 38003ms

    100% of the File (gzip)    -> 10633ms
 */