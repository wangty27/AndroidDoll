package wang.tianyu.ragdoll;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Foot extends Sprite {
    private float x;
    private float y;
    private float height;
    private float width;
    private float degreeToAxis = 0;
    private DefaultState dState;
    private RectF oval;

    public Foot(float height, float width, Sprite parent, DefaultState d) {
        super(parent);
        this.x = 0;
        this.y = 0;
        this.height = height;
        this.width = width;
        this.dState = d;
        this.oval = new RectF(x, y, x + this.width, y + this.height);
    }

    @Override
    public void reset() {
        this.localTransform = new Matrix();
        this.localTransform.postRotate(this.dState.rotationDegrees, this.getPivot().getX(), this.getPivot().getY());
        this.localTransform.postTranslate(this.dState.translateX, this.dState.translateY);
        this.degreeToAxis = 0;
        for (Sprite child: this.children) {
            child.reset();
        }

    }

    @Override
    public boolean isPointInside(Point p) {
        Matrix inverse = new Matrix();
        if (this.getFullTransform().invert(inverse)) {
            float[] pArray = new float[2];
            pArray[0] = p.getX();
            pArray[1] = p.getY();
            inverse.mapPoints(pArray);
            Point cPoint = new Point(pArray);
            return this.oval.contains(cPoint.getX(), cPoint.getY());
        } else {
            Log.d("Invert Error", "Can't invert matrix");
            return false;
        }
    }

    @Override
    protected void drawSprite(Canvas canvas, Paint paint) {
        canvas.drawOval(oval, paint);
    }

    @Override
    protected Point getPivot() {
        return new Point(x + width / 2, 0);
    }

    @Override
    public void handleTouchMove(Point p) {
        Log.d("Handle Move", "Foot Move");
        float degreeChanged = -this.getDegreeChanged(p);
        if (this.degreeToAxis + degreeChanged > this.dState.maxRotationDegrees || this.degreeToAxis + degreeChanged < -this.dState.maxRotationDegrees) {
            degreeChanged = 0;
        }
        this.degreeToAxis += degreeChanged;
        this.localTransform.preRotate(degreeChanged, this.getPivot().getX(), this.getPivot().getY());
        this.lastPoint = p;
        Log.d("Degree change", degreeChanged + " degrees");
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    protected void handleScale(float distance, float change, boolean direct) {
        super.handleScale(distance, change, direct);
        this.localTransform.postTranslate(0, change);
    }
}
