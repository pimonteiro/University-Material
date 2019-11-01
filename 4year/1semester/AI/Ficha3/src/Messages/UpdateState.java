package Messages;

import java.io.Serializable;

public class UpdateState implements Serializable {
    private float final_x;
    private float final_y;
    private boolean state;

    public UpdateState(float x, float y, boolean state) {
        this.final_x = x;
        this.final_y = y;
        this.state = state;
    }

    public float getFinal_x() {
        return final_x;
    }

    public void setFinal_x(float final_x) {
        this.final_x = final_x;
    }

    public float getFinal_y() {
        return final_y;
    }

    public void setFinal_y(float final_y) {
        this.final_y = final_y;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
