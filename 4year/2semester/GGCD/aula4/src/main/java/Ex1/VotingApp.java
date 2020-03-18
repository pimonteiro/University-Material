package Ex1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class VotingApp {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);

        Table ht = conn.getTable(TableName.valueOf("movies"));

        long start = System.currentTimeMillis();
        for(int i = 0; i < 5000; i++){
            int n = i % 100;
            String id = String.format("tt%06d",++n);

            Put put = new Put(Bytes.toBytes(id));
            put.addColumn(Bytes.toBytes("Votes"), Bytes.toBytes("ID"), Bytes.toBytes(i));
            ht.put(put);
        }
        long end = System.currentTimeMillis();
        System.out.println("To cast 5000 votes: " + (end - start));

        conn.close();
    }
}
