import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class Populate {
    public static void main(String[] args) throws Exception {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);

        /*
        // criar tabela
        Admin admin = conn.getAdmin();
        HTableDescriptor t = new HTableDescriptor(TableName.valueOf("tabela"));
        t.addFamily(new HColumnDescriptor("detail"));
        t.addFamily(new HColumnDescriptor("orders"));
        admin.createTable(t);
        admin.close();
*/
        // encher com dados
        Table ht = conn.getTable(TableName.valueOf("tabela"));
        for(int i=0; i<1000; i++) {
            Put put = new Put(Bytes.toBytes(i));
            put.addColumn(Bytes.toBytes("detail"), Bytes.toBytes("phoneNo"),
                    Bytes.toBytes("+3513423"+i));

            for(int j=0;j<10;j++) {
                put.addColumn(Bytes.toBytes("orders"), Bytes.toBytes(j),
                        Bytes.toBytes("detalhe da enc #"+j));
            }

            ht.put(put);
        }
        ht.close();

        //Aceder a dados
        ht = conn.getTable(TableName.valueOf("tabela"));
        Get get = new Get(Bytes.toBytes(2));
        get.addColumn(Bytes.toBytes("orders"),Bytes.toBytes(2));
        Result res = ht.get(get);
        ht.close();
        System.out.println(Bytes.toString(res.value()));

        conn.close();
    }
}
