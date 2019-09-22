package wang.tianyu.ragdoll;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

// Inspired by sample code Sprite.js
public abstract class Sprite {
    Point touchPoint = null;
    Point lastPoint = null;
    Sprite parent = null;
    List<Sprite> children = new ArrayList<>();
    Matrix localTransform = new Matrix();
    float degreeToAxis = 0;

    public Sprite(Sprite parent) {
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Sprite child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void setParent(Sprite parent) {
        this.parent = parent;
    }

    public Sprite getParent() {
        return this.parent;
    }

    public List<Sprite> getChildren() {
        return this.children;
    }

    public abstract boolean isPointInside(Point p);

    public void handleTouchDown(Point p) {
        this.touchPoint = new Point(p);
        this.lastPoint = new Point(p);
    }

    public abstract void reset();

    public abstract void handleTouchMove(Point p);

    public Sprite getHitSprite(MotionEvent e) {
        Point hitPoint = new Point(e.getX(), e.getY());
        for (Sprite s: this.children) {
            if (s.getHitSprite(e) != null) {
                return s.getHitSprite(e);
            }
        }
        if (this.isPointInside(hitPoint)) {
            return this;
        }
        return null;
    }

    public Matrix getLocalTransform() {
        return new Matrix(this.localTransform);
    }

    public Matrix getFullTransform() {
        Matrix returnMatrix = new Matrix();
        Sprite curSprite = this;
        while (curSprite != null) {
            returnMatrix.postConcat(curSprite.getLocalTransform());
            curSprite = curSprite.getParent();
        }
        return returnMatrix;
    }

    public void transform(Matrix m) {
        this.localTransform.postConcat(m);
    }

    public void draw(Canvas canvas, Paint paint) {
        Matrix oldMatrix = canvas.getMatrix();
        Matrix curMatrix = this.getFullTransform();
        canvas.setMatrix(curMatrix);
        this.drawSprite(canvas, paint);
        canvas.setMatrix(oldMatrix);
        for (Sprite child: this.children) {
            child.draw(canvas, paint);
        }
    }

    protected abstract void drawSprite(Canvas canvas, Paint paint);

    protected abstract Point getPivot();

    protected float getDegreeChanged(Point p) {
        Matrix inverse = new Matrix();
        this.getFullTransform().invert(inverse);
        float[] pArray = new float[2];
        pArray[0] = p.getX();
        pArray[1] = p.getY();
        inverse.mapPoints(pArray);
        Point pPoint = new Point(pArray);
        float[] lArray = new float[2];
        lArray[0] = this.lastPoint.getX();
        lArray[1] = this.lastPoint.getY();
        inverse.mapPoints(lArray);
        Point lPoint = new Point(lArray);
        double pDegree = this.getDegreeToAxis(pPoint);
        double lDegree = this.getDegreeToAxis(lPoint);
        float degreeChanged = (float)(pDegree - lDegree);
        return degreeChanged;
    }

    private double getDegreeToAxis(Point p) {
        Point pivot = this.getPivot();
        double theta = 0;
        if (p.getX() >= pivot.getX() && p.getY() >= pivot.getY()) {
            float dx = p.getX() - pivot.getX();
            float dy = p.getY() - pivot.getY();
            if (dy == 0 && dx != 0) {
                theta = 90;
            } else {
                double radTheta = Math.atan((double) dx / dy);
                theta = Math.toDegrees(radTheta);
            }
        } else if (p.getX() >= pivot.getX() && p.getY() < pivot.getY()) {
            float dx = p.getX() - pivot.getX();
            float dy = pivot.getY() - p.getY();
            double radTheta = Math.atan((double) dx / dy);
            theta = Math.toDegrees(radTheta);
            theta = 180 - theta;
        } else if (p.getX() < pivot.getX() && p.getY() >= pivot.getY()) {
            float dx = pivot.getX() - p.getX();
            float dy = p.getY() - pivot.getY();
            if (dy == 0) {
                theta = 270;
            } else {
                double radTheta = Math.atan((double) dx / dy);
                theta = Math.toDegrees(radTheta);
                theta = 360 - theta;
            }
        } else if (p.getX() < pivot.getX() && p.getY() < pivot.getY()) {
            float dx = pivot.getX() - p.getX();
            float dy = pivot.getY() - p.getY();
            double radTheta = Math.atan((double) dx / dy);
            theta = Math.toDegrees(radTheta);
            theta = 180 + theta;
        }
        return theta;
    }

    protected abstract float getWidth();

    protected abstract float getHeight();

    protected void handleScale(float distance, float change, boolean direct) {}
}
