package federate;

/**
 * Created by Andrew on 11/4/2016.
 */
public class PositionVector {

    public double x, y, z;

    public PositionVector() { x = y = z = 0.0f; }

    public PositionVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(PositionVector delta) {
        this.x += delta.x;
        this.y += delta.y;
        this.z += delta.z;
    }

    @Override
    public String toString() {
        return "";
    }
}
