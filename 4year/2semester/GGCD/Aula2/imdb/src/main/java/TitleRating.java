import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class TitleRating {

    public static class MyMapper1 extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if(key.get() == 0){
                return;
            }
            String[] words = value.toString().split("\t");

            context.write(new Text(words[0]), new Text("NUNUN:"+words[1]));
        }
    }

    public static class MyMapper2 extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split("\t");
            if(key.get() == 0){
                return;
            }

            context.write(new Text(words[0]), new Text(words[3]));
        }
    }

    public static class MyReducer1 extends Reducer<Text, Text,Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String title = "";
            String rate = "";
            for(Text v : values){
                if(v.toString().contains("NUNUN")){
                    String[] w = v.toString().split(":");
                    rate = w[1];
                }
                else
                    title = v.toString();
            }

            context.write(new Text(title), new Text(rate));
        }
    }


    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance(new Configuration(), "title-ratings");

        job.setJarByClass(TitleRating.class);
        //job.setCombinerClass(MyCombiner.class);
        //job.setCombinerClass(MyReducer.class);
        job.setReducerClass(MyReducer1.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //job.setInputFormatClass(TextInputFormat.class);
        long start = System.currentTimeMillis();
        MultipleInputs.addInputPath(job, new Path("/home/msi-gtfo/Downloads/imdb/title.ratings.tsv.gz"),TextInputFormat.class,MyMapper1.class);
        MultipleInputs.addInputPath(job, new Path("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz"),TextInputFormat.class,MyMapper2.class);
        job.setMapOutputValueClass(Text.class);

        //TextInputFormat.setInputPaths(job, "/home/msi-gtfo/Downloads/imdb/titles.ratings.tsv.gz,/home/msi-gtfo/Downloads/imdb/titles.basics.tsv.gz");

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("out"));

        job.waitForCompletion(true);
        long end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end - start));
    }
}
