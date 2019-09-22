package wang.tianyu.ragdoll;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

public class Torso extends Sprite {
    private float x;
    private float y;
    private float height;
    private float width;
    private DefaultState dState;
    private RectF rect;

    public Torso(float height, float width, Sprite parent, DefaultState d) {
        super(parent);
        this.x = 0;
        this.y = 0;
        this.height = height;
        this.width = width;
        this.dState = d;
        this.rect = new RectF(x, y, x + this.width, y + this.height);
    }

    @Override
    public void reset() {
        this.localTransform = new Matrix();
//        this.localTransform.postScale((float)0.9, (float)0.9);
        this.localTransform.postRotate(this.dState.rotationDegrees, this.getPivot().getX(), this.getPivot().getY());
        this.localTransform.postTranslate(this.dState.translateX, this.dState.translateY);
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
            return this.rect.contains(cPoint.getX(), cPoint.getY());
        } else {
            Log.d("Invert Error", "Can't invert matrix");
            return false;
        }
    }

    @Override
    protected void drawSprite(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(rect, 40, 40, paint);
    }

    @Override
    protected Point getPivot() {
        return new Point(this.x, this.y);
    }

    @Override
    public void handleTouchMove(Point p) {
        Log.d("Handle Move", "Torso Move");
        float dx = p.getX() - this.lastPoint.getX();
        float dy = p.getY() - this.lastPoint.getY();
        this.lastPoint = p;
        this.localTransform.postTranslate(dx, dy);
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }
}
