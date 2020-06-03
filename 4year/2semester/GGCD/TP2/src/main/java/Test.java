public class Test {
    public static void main(String []args){
        String s = "(ola,25)";
        s = s.replace("(","").replace(")","");
        System.out.println(s);
    }

}
