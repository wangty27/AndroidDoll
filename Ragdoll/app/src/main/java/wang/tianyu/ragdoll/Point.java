package wang.tianyu.ragdoll;

public class Point {
    float x;
    float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point(float[] p) {
        this.x = p[0];
        this.y = p[1];
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
