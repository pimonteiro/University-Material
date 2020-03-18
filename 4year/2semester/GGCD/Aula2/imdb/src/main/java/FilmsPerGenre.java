import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class FilmsPerGenre {

    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split("\t");
            String[] genres = words[8].split(",");

            for(String genre: genres)
                context.write(new Text(genre),new LongWritable(1));
        }
    }

    // LongSumReducer
    public static class MyReducer extends Reducer<Text, LongWritable,Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for(LongWritable value: values) {
                sum+=value.get();
            }
            context.write(key, new LongWritable(sum));
        }
    }

    public static class MyCombiner extends Reducer<Text,LongWritable,Text,LongWritable>
    {
        private LongWritable result = new LongWritable();

        public void reduce(Text key, Iterable<LongWritable> values,Context context) throws IOException, InterruptedException
        {
            int sum = 0;
            for (LongWritable val : values)
            {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance(new Configuration(), "filme-count-per-genre");

        job.setJarByClass(FilmsPerGenre.class);
        job.setMapperClass(MyMapper.class);
        job.setCombinerClass(MyCombiner.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        long start = System.currentTimeMillis();
        //TextInputFormat.setInputPaths(job, "/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz");
        TextInputFormat.setInputPaths(job, "hdfs://namenode:9000/input/title.basics.tsv.gz");

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("hdfs://namenode:9000/out"));

        job.waitForCompletion(true);
        long end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end - start));
    }
}
