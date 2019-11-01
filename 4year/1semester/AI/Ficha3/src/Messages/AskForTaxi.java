package Messages;

import jade.core.AID;

public class AskForTaxi implements java.io.Serializable {
    private AID client;
    private float x_pos;
    private float y_pos;
    private float x_dest;
    private float y_dest;

    public AskForTaxi(AID client, float x_pos, float y_pos, float x_dest, float y_dest) {
        this.client = client;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.x_dest = x_dest;
        this.y_dest = y_dest;
    }

    public AID getClient() {
        return client;
    }

    public float getX_pos() {
        return x_pos;
    }

    public float getY_pos() {
        return y_pos;
    }

    public float getX_dest() {
        return x_dest;
    }

    public float getY_dest() {
        return y_dest;
    }

    public void setClient(AID client) {
        this.client = client;
    }

    public void setX_pos(float x_pos) {
        this.x_pos = x_pos;
    }

    public void setY_pos(float y_pos) {
        this.y_pos = y_pos;
    }

    public void setX_dest(float x_dest) {
        this.x_dest = x_dest;
    }

    public void setY_dest(float y_dest) {
        this.y_dest = y_dest;
    }
}
