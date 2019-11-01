package Messages;

import java.io.Serializable;

public class FinishTrip implements Serializable {
    private String msg;

    public void setMsg(String s){
        this.msg = s;
    }

    public String getMsg(){
        return this.msg;
    }
}
