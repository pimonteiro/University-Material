import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        ArrayList<Pair<String,Float>> tops = new ArrayList<Pair<String, Float>>();
        tops.add(new Pair<String,Float>("Filme1",(float)8.9));
        tops.add(new Pair<String,Float>("Filme2",(float)7.9));
        tops.add(new Pair<String,Float>("Filme3",(float)9.2));
        tops.add(new Pair<String,Float>("Filme4",(float)3.0));

        Collections.sort(tops, new Comparator<Pair<String, Float>>() {
            public int compare(Pair<String, Float> t1, Pair<String, Float> t2) {
                if(t2.getSecond() > t1.getSecond()){
                    return 1;
                }
                else
                    return -1;
            }
        });
        System.out.println(tops.toString());
    }
}
