package Messages;

import jade.core.AID;

import java.io.Serializable;

public class SendTaxi implements Serializable {
    private AID taxi;
    private AID client;
    private float x_dest;
    private float y_dest;

    public SendTaxi(AID taxi, AID client, float x_dest, float y_dest) {
        this.taxi = taxi;
        this.client = client;
        this.x_dest = x_dest;
        this.y_dest = y_dest;
    }

    public AID getTaxi() {
        return taxi;
    }

    public void setTaxi(AID taxi) {
        this.taxi = taxi;
    }

    public float getX_dest() {
        return x_dest;
    }

    public void setX_dest(float x_dest) {
        this.x_dest = x_dest;
    }

    public float getY_dest() {
        return y_dest;
    }

    public void setY_dest(float y_dest) {
        this.y_dest = y_dest;
    }

    public AID getClient(){
        return client;
    }

    public void setClient(AID client){
        this.client = client;
    }
}
