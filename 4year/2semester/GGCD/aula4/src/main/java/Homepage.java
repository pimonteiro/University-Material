import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Homepage{
    public static void main(String[]args)throws IOException{

        // Configurar e conetar
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);

        // Buscar a tabela
        Table ht = conn.getTable(TableName.valueOf("movies"));
        Get get = new Get(Bytes.toBytes("tt0000003"));
        Result res = ht.get(get);
        System.out.println(Bytes.toString(res.getValue(Bytes.toBytes("Details"), Bytes.toBytes("Title"))));
        System.out.println(Bytes.toString(res.getValue(Bytes.toBytes("Details"), Bytes.toBytes("Genres"))));
        System.out.println(Bytes.toString(res.getValue(Bytes.toBytes("Details"), Bytes.toBytes("Rating"))));
        System.out.println(Bytes.toString(res.getValue(Bytes.toBytes("Cast"), Bytes.toBytes(1))));
        ht.close();
        conn.close();
    }
}