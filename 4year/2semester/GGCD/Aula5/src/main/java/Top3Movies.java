import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Top3Movies {
    private static Connection conn;
    private static Table ht;

    public static class Top3Mapper2 extends TableMapper<ImmutableBytesWritable, Put> {
        protected void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException{
            ArrayList<Pair<String,Float>> tops = new ArrayList<Pair<String, Float>>();
            for(Cell c : value.rawCells()){
                Get get = new Get(CellUtil.cloneValue(c)); //ID FILME
                get.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Score"));
                Result res = ht.get(get);
                for(Cell t : res.rawCells()){
                    float score = Float.parseFloat(Bytes.toString(CellUtil.cloneValue(t)));
                    tops.add(new Pair<String,Float>(Bytes.toString(CellUtil.cloneValue(c)),score));
                }
            }
            Collections.sort(tops, new Comparator<Pair<String, Float>>() {
                public int compare(Pair<String, Float> t1, Pair<String, Float> t2) {
                    if(t2.getSecond() > t1.getSecond()){
                        return 1;
                    }
                    else
                        return -1;
                }
            });

            if(tops.isEmpty())
                return;
            Put put = new Put(row.get());
            for(int i = 0; i < tops.size() && i < 3; i++)
                put.addColumn(Bytes.toBytes("TopMovies"),Bytes.toBytes(i+1),Bytes.toBytes(tops.get(i).getFirst()));

            context.write(row, put);  // (ID_FILME , ID_ATOR)
        }
    }

    /*
    public static class Top3Reducer extends TableReducer<ImmutableBytesWritable, Text, Put> {
        protected void reduce(ImmutableBytesWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //Put put = new Put(key.get());
            ArrayList<Long> scores = new ArrayList<Long>();
            long score
            for(Text v : values){
                String s = v.toString();
                long score = 0;
                if(s.contains())


                String s = Bytes.toString(v.get());
                put.addColumn(Bytes.toBytes("AllMovies"),Bytes.toBytes(i++),Bytes.toBytes(s));
            }
            context.write(null,put);
        }
    }
     */

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "zoo");

        conn = ConnectionFactory.createConnection(conf);
        ht = conn.getTable(TableName.valueOf("movies"));

        Job job = Job.getInstance(conf,"load-top-movies-actors");
        job.setJarByClass(Top3Movies.class);
        job.setNumReduceTasks(0);

        Scan scan2 = new Scan();
        scan2.setCaching(500);
        scan2.setCacheBlocks(false);
        scan2.addFamily(Bytes.toBytes("AllMovies"));

        TableMapReduceUtil.initTableMapperJob("actors",scan2, Top3Mapper2.class,null,null,job);
        TableMapReduceUtil.initTableReducerJob(
                "actors",      // output table
                null,
                job);
        //job.setOutputFormatClass(NullOutputFormat.class);
        //TableMapReduceUtil.initTableReducerJob("actors", Top3Reducer.class,job);

        TextOutputFormat.setOutputPath(job, new Path("hdfs://namenode:9000/guiao4/top3Movies"));
        job.waitForCompletion(true);
    }
}
