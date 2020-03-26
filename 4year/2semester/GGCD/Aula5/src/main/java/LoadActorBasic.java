import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class LoadActorBasic {
    public static class ActorMapper extends Mapper<LongWritable, Text, NullWritable, Put> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            Put put = new Put(Bytes.toBytes(fields[0]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Name"), Bytes.toBytes(fields[1]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Birth"), Bytes.toBytes(fields[2]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Death"), Bytes.toBytes(fields[3]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Professions"), Bytes.toBytes(fields[4]));
            context.write(null, put);
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "zoo"); //distribuited zoo
        Connection conn = ConnectionFactory.createConnection(conf);

        Admin admin = conn.getAdmin();
        HTableDescriptor t = new HTableDescriptor(TableName.valueOf("actors"));
        t.addFamily(new HColumnDescriptor("Details"));
        t.addFamily(new HColumnDescriptor("AllMovies"));
        t.addFamily(new HColumnDescriptor("TopMovies"));
        t.addFamily(new HColumnDescriptor("Collabs"));
        admin.createTable(t);
        admin.close();


        Job job = Job.getInstance(conf, "load-actors");
        job.setJarByClass(LoadActorBasic.class);
        job.setMapperClass(ActorMapper.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Put.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.setInputPaths(job, "hdfs://namenode:9000/input/name.basics.tsv");

        job.setOutputFormatClass(TableOutputFormat.class);

        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, "actors");
        TextOutputFormat.setOutputPath(job, new Path("hdfs://namenode:9000/guiao4/loadActorBasic"));

        job.waitForCompletion(true);
    }
}