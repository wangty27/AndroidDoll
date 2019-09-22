package wang.tianyu.ragdoll;

public class DefaultState {
    public float rotationDegrees;
    public float translateX;
    public float translateY;
    public float maxRotationDegrees;

    public DefaultState(float r, float x, float y, float m) {
        this.rotationDegrees = r;
        this.translateX = x;
        this.translateY = y;
        this.maxRotationDegrees = m;
    }
}
