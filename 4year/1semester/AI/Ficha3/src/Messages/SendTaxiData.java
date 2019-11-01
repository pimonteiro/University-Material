package Messages;

import java.io.Serializable;

public class SendTaxiData implements Serializable {
    private float x_pos;
    private float y_pos;

    public SendTaxiData(float x_pos, float y_pos) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
    }

    public float getX_pos() {
        return x_pos;
    }

    public void setX_pos(float x_pos) {
        this.x_pos = x_pos;
    }

    public float getY_pos() {
        return y_pos;
    }

    public void setY_pos(float y_pos) {
        this.y_pos = y_pos;
    }
}
