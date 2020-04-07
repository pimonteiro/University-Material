import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Ex3 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Ex2");
        JavaSparkContext sc = new JavaSparkContext(conf);

        //Missing joining with actor data
        JavaPairRDD<String,List<String>> tmp = sc.textFile("hdfs://namenode:9000/input/title.principals_%1.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .mapToPair(l -> new Tuple2<>(l[0],l[2]))
                .groupByKey()
                .mapToPair(p -> new Tuple2<>(p._1,
                            StreamSupport.stream(p._2.spliterator(),false)
                                .flatMap(st1 -> StreamSupport.stream(p._2.spliterator(),false)
                                    .map(st2 -> new Tuple2<>(st1,st2)))
                                .collect(Collectors.toList())
                ))
                .map(p -> p._2)
                .flatMap(List::iterator)
                .mapToPair(p -> p)
                .groupByKey()
                .mapToPair(p -> new Tuple2<>(p._1,
                        StreamSupport.stream(p._2.spliterator(),false)
                            .collect(Collectors.toList())
                ));

        List<Tuple2<String, List<String>>> actors = tmp.collect();
        System.out.println("---------------------");
        for(Tuple2<String,List<String>> t : actors){
            System.out.println("Actor: " + t._1);
            System.out.println("Collabs:");
            t._2.forEach(s -> System.out.println(s));
            System.out.println("---------------------");
        }
    }
}
