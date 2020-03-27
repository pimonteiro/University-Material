import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Ex4 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("genres-movies");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, Double> ratings = sc.textFile("file:///home/msi-gtfo/Downloads/imdb_01%/title.ratings.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .filter(l -> !l[1].equals("\\N"))
                .filter(l -> Float.parseFloat(l[1]) >= 9.0)
                .mapToPair(l -> new Tuple2<>(l[0], Double.parseDouble(l[1])));

        JavaPairRDD<String,String> movies = sc.textFile("file:///home/msi-gtfo/Downloads/imdb_01%/title.basics.tsv.bz2")
                .map(l -> l.split("\t"))
                .filter(l -> !l[0].equals("tconst"))
                .filter(l -> !l[1].equals("\\N"))
                .mapToPair(l -> new Tuple2<>(l[0],l[2]));

        JavaPairRDD<String, Tuple2<Double,String>> joined = ratings.join(movies);
        List<Tuple2<String,Tuple2<Double,String>>> res = joined.collect();

        List<Tuple2<String,Double>> sorted = res.stream().sorted((t1,t2) -> t2._2._1.compareTo(t1._2._1))
                .map(t -> new Tuple2<String,Double>(t._2._2,t._2._1)).collect(Collectors.toList());

        for(Tuple2<String,Double> a : sorted){
            System.out.println(a._1 + " -> " + a._2);
        }


    }
}
