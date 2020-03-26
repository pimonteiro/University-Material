import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableMap;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class LoadAllMoviesActor {
    public static class TotalMoviesMapper extends TableMapper<ImmutableBytesWritable, ImmutableBytesWritable> {
        protected void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException{
            for(Cell c : value.rawCells()){
                context.write(new ImmutableBytesWritable(CellUtil.cloneValue(c)),row);
            }
        }
    }

    public static class TotalMoviesReducer extends TableReducer<ImmutableBytesWritable, ImmutableBytesWritable, Put> {
        protected void reduce(ImmutableBytesWritable key, Iterable<ImmutableBytesWritable> values, Context context) throws IOException, InterruptedException {
            Put put = new Put(key.get());
            int i = 0;
            for(ImmutableBytesWritable v : values){
                String s = Bytes.toString(v.get());
                put.addColumn(Bytes.toBytes("AllMovies"),Bytes.toBytes(i++),Bytes.toBytes(s));
            }
            context.write(null,put);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "zoo"); //distribuited zoo

        Job job = Job.getInstance(conf,"load-actors-allmovies");
        job.setJarByClass(LoadAllMoviesActor.class);
        job.setNumReduceTasks(1);

        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        scan.addFamily(Bytes.toBytes("Cast"));
        TableMapReduceUtil.initTableMapperJob("movies",scan,TotalMoviesMapper.class,ImmutableBytesWritable.class,ImmutableBytesWritable.class,job);
        TableMapReduceUtil.initTableReducerJob("actors",TotalMoviesReducer.class,job);
        /*
        job.setInputFormatClass(TableInputFormat.class);

        job.setOutputFormatClass(TableOutputFormat.class);
        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE,"actors");
        */
        TextOutputFormat.setOutputPath(job, new Path("hdfs://namenode:9000/guiao4/loadAllMoviesActor"));

        job.waitForCompletion(true);
    }
}
