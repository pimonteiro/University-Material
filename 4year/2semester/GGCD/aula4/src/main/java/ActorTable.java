import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ActorTable {

    public static void main(String[] args) throws Exception {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);


        // criar tabela
        Admin admin = conn.getAdmin();
        HTableDescriptor t = new HTableDescriptor(TableName.valueOf("actors"));
        t.addFamily(new HColumnDescriptor("Details"));
        t.addFamily(new HColumnDescriptor("TopMovies"));
        t.addFamily(new HColumnDescriptor("Collabs"));
        admin.createTable(t);
        admin.close();

        Table tmp = conn.getTable(TableName.valueOf("movies"));

        // encher com dados
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/home/msi-gtfo/Downloads/imdb/name.basics.tsv.gz"))));
        Table ht = conn.getTable(TableName.valueOf("actors"));

        int i=0;
        for(String l = br.readLine(); l!=null && i < 1000; l=br.readLine(), i++) {
            String[] fields = l.split("\t");
            if(i == 0)
                continue;
            Put put = new Put(Bytes.toBytes(fields[0]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("Name"), Bytes.toBytes(fields[1]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("BirthDate"), Bytes.toBytes(fields[2]));
            put.addColumn(Bytes.toBytes("Details"), Bytes.toBytes("DeathDate"), Bytes.toBytes(fields[3]));
        }
        ht.close();
        conn.close();
    }
}
