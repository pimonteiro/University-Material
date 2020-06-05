import java.time.LocalDateTime;
import java.util.Date;

public class Test {
    public static void main(String []args){
        String s = "(ola,25)";
        s = s.replace("(","").replace(")","");

        Long t = 1591209120000L;
        String ss = "ID\t";
        ss = ss.concat("\t" + (new Date(t)));
        System.out.println(ss);
    }

}
