package wang.tianyu.ragdoll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DogView extends View {
    Paint blackBrush;
    Torso torso;
    Head head;
    UpperArm upperArmR;
    UpperArm upperArmL;
    LowerArm lowerArmR;
    LowerArm lowerArmL;
    Hand handR;
    Hand handL;
    UpperArm upperLegR;
    UpperArm upperLegL;
    LowerArm lowerLegR;
    LowerArm lowerLegL;
    Foot footR;
    Foot footL;
    UpperArm tailU;
    UpperArm tailD;
    UpperArm earL;
    UpperArm earR;
    Sprite hitSprite;
    boolean multitouchActive;
    float multiInitDist;

    public DogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.blackBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.blackBrush.setStyle(Paint.Style.STROKE);
        this.blackBrush.setColor(Color.BLACK);

        this.torso = new Torso(350, 130, null, new DefaultState(-90, 400, 600, 0));
        this.head = new Head(108, 180, this.torso, new DefaultState(20, 25, -108, 30));
        this.upperArmR = new UpperArm(130, 45, this.torso, new DefaultState(150, -30, 30, 50));
        this.upperArmL = new UpperArm(130, 45, this.torso, new DefaultState(120, -30, 30, 50));
        this.lowerArmR = new LowerArm(90, 45, this.upperArmR, new DefaultState(-30, 0, 135, 90));
        this.lowerArmL = new LowerArm(90, 45, this.upperArmL, new DefaultState(-30, 0, 135, 90));
        this.handR = new Hand(45, 40, this.lowerArmR, new DefaultState(50, 0, 95, 35));
        this.handL = new Hand(45, 40, this.lowerArmL, new DefaultState(50, 0, 95, 35));
        this.upperLegR = new UpperArm(140, 45, this.torso, new DefaultState(50, -30, 320, 50));
        this.upperLegL = new UpperArm(140, 45, this.torso, new DefaultState(20, -30, 320, 50));
        this.lowerLegR = new LowerArm(100, 45, this.upperLegR, new DefaultState(-50, 0, 145, 60));
        this.lowerLegL = new LowerArm(100, 45, this.upperLegL, new DefaultState(-50, 0, 145, 60));
        this.footR = new Foot(70, 40, this.lowerLegR, new DefaultState(90, 0, 105, 35));
        this.footL = new Foot(70, 40, this.lowerLegL, new DefaultState(90, 0, 105,35));
        this.tailU = new UpperArm(130, 35, this.torso, new DefaultState(-40, 105, 345, 100));
        this.tailD = new UpperArm(130, 30, this.tailU, new DefaultState(-30, 0, 135, 50));
        this.earL = new UpperArm(100, 30, this.head, new DefaultState(-80, 160, 84, 100));
        this.earR = new UpperArm(100, 30, this.head, new DefaultState(-100, 160, 24, 100));
        this.torso.reset();
    }

    public void resetView() {
        this.torso.reset();
        this.invalidate();
    }

    private float getDistance(Point a, Point b) {
        return (float)Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Point touchPoint = new Point(e.getX(), e.getY());
        int action = e.getAction() & MotionEvent.ACTION_MASK;
        int pointerCount = e.getPointerCount();
        this.multitouchActive = false;
        if (pointerCount > 1) {
            this.multitouchActive = true;
        }
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (this.multitouchActive) {
                    Log.d("Multi Touch", "Down");
                    this.multiInitDist = this.getDistance(new Point(e.getX(0), e.getY(0)),
                            new Point(e.getX(1), e.getY(1)));
                } else {
                    Log.d("Canvas Touch", "Down: x: " + e.getX() + " y: " + e.getY());
                    this.hitSprite = this.torso.getHitSprite(e);
                    if (this.hitSprite != null) {
                        Log.d("Sprite HitTest", "Hit");
                        this.hitSprite.handleTouchDown(touchPoint);
                    } else {
                        Log.d("Sprite HitTest", "Miss");
                    }
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (this.multitouchActive) {
                    float curDist = this.getDistance(new Point(e.getX(0), e.getY(0)),
                            new Point(e.getX(1), e.getY(1)));
                    Point finger0 = new Point(e.getX(0), e.getY(0));
                    Point finger1 = new Point(e.getX(1), e.getY(1));
                    if (this.hitSprite != null && this.hitSprite.isPointInside(finger0) && this.hitSprite.isPointInside(finger1)) {
                        this.hitSprite.handleScale(curDist - this.multiInitDist, 0, true);
                    } else {
                        this.hitSprite = null;
                    }
                } else {
                    Log.d("Canvas Touch", "Move: x: " + e.getX() + " y: " + e.getY());
                    if (this.hitSprite != null && this.hitSprite.isPointInside(touchPoint)) {
//                    Log.d("Canvas Touch Move", "Sprite still hit");
                        this.hitSprite.handleTouchMove(touchPoint);
                    } else {
//                    Log.d("Canvas Touch Move", "Sprite lost");
                        this.hitSprite = null;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.d("Canvas Touch", "Up: x: " + e.getX() + " y: " + e.getY());
                this.hitSprite = null;
                break;
            }
            default: {
                return false;
            }
        }
        this.invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.torso.draw(canvas, blackBrush);
//        this.head.draw(canvas, blackBrush);
    }
}
