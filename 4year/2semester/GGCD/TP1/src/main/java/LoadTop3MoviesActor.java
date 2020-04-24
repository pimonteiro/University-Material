import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import utils.Helpers;

import java.io.IOException;
import java.util.*;

public class LoadTop3MoviesActor {
    public static class MovieScoreMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            if(fields[0].contains("tconst"))
                return;
            String val = "_" + fields[1];
            context.write(new Text(fields[0]),new Text(val));
        }
    }

    public static class MoviesPerActorMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            if(fields[0].contains("tconst"))
                return;
            context.write(new Text(fields[0]),new Text(fields[2]));
        }
    }

    public static class Top3FirstReducer extends Reducer<Text,Text,Text,Text> {
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double score = 0;
            ArrayList<String> ids = new ArrayList<String>();
            for(Text i : values){
                String s = i.toString();
                if (s.contains("_")){
                    String[] tmp = s.split("_");
                    score = Double.parseDouble(tmp[1]);
                }
                else
                    ids.add(s);
            }
            String movie = key.toString();
            for(String i : ids) {
                context.write(new Text(i), new Text(movie + "_" + score));
            }
        }
    }

    public static class SequenceFileMapper extends Mapper<Text, Text, Text, Text> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            context.write(key,value);
        }
    }

    public static class SequenceFileReducer extends TableReducer<Text, Text, Put> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Map<String,Double> unsorted = new HashMap<String,Double>();
            for(Text i : values){
                String[] vals = i.toString().split("_");
                unsorted.put(vals[0],Double.parseDouble(vals[1]));
            }
            Map<String,Double> sorted = Helpers.sortByValue(unsorted);

            Put put = new Put(Bytes.toBytes(key.toString()));
            ArrayList<String> movies = new ArrayList<String>(sorted.keySet());
            ArrayList<String> top = new ArrayList<String>();

            for(int i = 0; i < Math.min(3,movies.size()); i++){
                top.add(movies.get(i));
            }
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("TopMovies"),Bytes.toBytes(top.toString()));
            context.write(null, put);
        }
    }

    public static long[] load_top_3_movies() throws InterruptedException, IOException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        long job1_time, job2_time;
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "zoo");

        Job job = Job.getInstance(conf,"compute-top-3-movies-per-actor-part1");
        job.setJarByClass(LoadTop3MoviesActor.class);
        job.setNumReduceTasks(1);
        MultipleInputs.addInputPath(job, new Path("hdfs://namenode:9000/input/title.principals.tsv"), TextInputFormat.class, MoviesPerActorMapper.class);
        MultipleInputs.addInputPath(job, new Path("hdfs://namenode:9000/input/title.ratings.tsv"), TextInputFormat.class, MovieScoreMapper.class);
        job.setReducerClass(Top3FirstReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        SequenceFileOutputFormat.setOutputPath(job,new Path("hdfs://namenode:9000/out/top-3-1/"));
        job.waitForCompletion(true);

        long end = System.currentTimeMillis();
        job1_time = end - start;
        System.out.println("Starting second job...");
        start = System.currentTimeMillis();

        Job job2 = Job.getInstance(conf, "compute-top-3-movies-per-actor-part2");
        job2.setJarByClass(LoadTop3MoviesActor.class);
        job2.setNumReduceTasks(1);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);
        SequenceFileInputFormat.setInputPaths(job2, "hdfs://namenode:9000/out/top-3-1/");
        job2.setMapperClass(SequenceFileMapper.class);
        TableMapReduceUtil.initTableReducerJob("actors",SequenceFileReducer.class,job2);
        job2.waitForCompletion(true);

        end = System.currentTimeMillis();
        job2_time = end - start;

        long[] times = new long[2];
        times[0] = job1_time;
        times[1] = job2_time;
        return times;
    }
}
