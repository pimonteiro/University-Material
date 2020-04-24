import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;

public class LoadMoviesActorData {

    public static class TotalMoviesMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            context.write(new Text(fields[2]),new Text(fields[0]));
        }
    }

    public static class TotalMoviesReducer extends TableReducer<Text, Text, Put> {
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Put put = new Put(Bytes.toBytes((key.toString())));
            int i = 0;
            for(Text v : values)
                i++;
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("N_Movies"), Bytes.toBytes(i));
            context.write(null,put);
        }
    }

    public static void load_actors_movies_data() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "zoo");

        Job job = Job.getInstance(conf,"load-n-movies-per-actor");
        job.setJarByClass(LoadMoviesActorData.class);
        job.setNumReduceTasks(1);
        job.setMapperClass(TotalMoviesMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.setInputPaths(job, "hdfs://namenode:9000/input/title.principals.tsv");

        TableMapReduceUtil.initTableReducerJob("actors", TotalMoviesReducer.class, job);

        job.waitForCompletion(true);
    }
}
