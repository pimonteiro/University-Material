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

public class CrewTable {

    public static void main(String[] args) throws Exception {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);


        // criar tabela
        Admin admin = conn.getAdmin();
        HTableDescriptor t = new HTableDescriptor(TableName.valueOf("crew"));
        t.addFamily(new HColumnDescriptor("Directors"));
        t.addFamily(new HColumnDescriptor("Writers"));
        admin.createTable(t);
        admin.close();

        // encher com dados
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/home/msi-gtfo/Downloads/imdb/title.crew.tsv.gz"))));
        Table ht = conn.getTable(TableName.valueOf("crew"));


        int i=0;
        for(String l = br.readLine(); l!=null && i<1000; l=br.readLine(), i++) {
            String[] fields = l.split("\t");
            if(i == 0)
                continue;
            Put put = new Put(Bytes.toBytes(fields[0]));

            String[] directors = fields[1].split(",");
            String[] writers = fields[1].split(",");
            boolean flag = false;
            int j = 1;
            for(String s : directors){
                if(s.contains("\\N"))
                    break;
                put.addColumn(Bytes.toBytes("Directors"), Bytes.toBytes(j++), Bytes.toBytes(s));
                flag = true;
            }
            j = 1;
            for(String s : writers){
                if(s.contains("\\N"))
                    break;
                put.addColumn(Bytes.toBytes("Writers"), Bytes.toBytes(j++), Bytes.toBytes(s));
                flag = true;
            }
            if(flag)
                ht.put(put);
        }
        ht.close();
        conn.close();
    }
}