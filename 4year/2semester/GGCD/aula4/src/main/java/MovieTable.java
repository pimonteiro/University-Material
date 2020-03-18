import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class MovieTable {

    public static void main(String[] args) throws Exception {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);


        // criar tabela
        Admin admin = conn.getAdmin();
        HTableDescriptor t = new HTableDescriptor(TableName.valueOf("movies"));
        t.addFamily(new HColumnDescriptor("Details"));
        t.addFamily(new HColumnDescriptor("Votes"));
        t.addFamily(new HColumnDescriptor("Cast"));
        admin.createTable(t);
        admin.close();

        // encher com dados
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/home/msi-gtfo/Downloads/imdb/title.basics.tsv.gz"))));
        Table ht = conn.getTable(TableName.valueOf("movies"));

        int i=0;
        for(String l = br.readLine(); l!=null && i < 1000; l=br.readLine(), i++) {
            String[] fields = l.split("\t");
            if(i == 0)
                continue;
            Put put = new Put(Bytes.toBytes(fields[0]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Title"), Bytes.toBytes(fields[2]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Genres"), Bytes.toBytes(fields[8]));
            ht.put(put);
        }


        br.close();
        br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/home/msi-gtfo/Downloads/imdb/title.principals.tsv.gz"))));
        i=0;
        int j = 1;
        for(String l = br.readLine(); l!=null && i < 1000; l=br.readLine(), i++) {
            String[] fields = l.split("\t");
            if(i == 0)
                continue;
            Put put = new Put(Bytes.toBytes(fields[0]));
            put.addColumn(Bytes.toBytes("Cast"), Bytes.toBytes(j++), Bytes.toBytes(fields[2]));
            ht.put(put);
        }


        br.close();
        br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/home/msi-gtfo/Downloads/imdb/title.ratings.tsv.gz"))));
        i=0;
        for(String l = br.readLine(); l!=null && i < 1000; l=br.readLine(), i++) {
            String[] fields = l.split("\t");
            if(i == 0)
                continue;
            Put put = new Put(Bytes.toBytes(fields[0]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Rating"), Bytes.toBytes(fields[1]));
            ht.put(put);
        }

        br.close();
        ht.close();
        conn.close();
    }
}
