import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Ex2 {
    SparkConf conf = new SparkConf().setAppName("Ex2");
    JavaSparkContext sc = new JavaSparkContext(conf);

    
}
