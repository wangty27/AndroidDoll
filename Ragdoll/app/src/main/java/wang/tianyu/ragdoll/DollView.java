package wang.tianyu.ragdoll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DollView extends View {
    Paint blackBrush;
    Torso torso;
    Head head;
    UpperArm upperArmR;
    UpperArm upperArmL;
    LowerArm lowerArmR;
    LowerArm lowerArmL;
    Hand handR;
    Hand handL;
    UpperLeg upperLegR;
    UpperLeg upperLegL;
    LowerLeg lowerLegR;
    LowerLeg lowerLegL;
    Foot footR;
    Foot footL;
    Sprite hitSprite;
    boolean multitouchActive;
    float multiInitDist;

    public DollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.blackBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.blackBrush.setStyle(Paint.Style.STROKE);
        this.blackBrush.setColor(Color.BLACK);

        this.torso = new Torso(400, 190, null, new DefaultState(0, 200, 300, 0));
        this.head = new Head(200, 120, this.torso, new DefaultState(0, 35, -220, 50));
        this.upperArmR = new UpperArm(230, 55, this.torso, new DefaultState(-20, 170, 10, -1));
        this.upperArmL = new UpperArm(230, 55, this.torso, new DefaultState(20, -35, 10, -1));
        this.lowerArmR = new LowerArm(190, 55, this.upperArmR, new DefaultState(20, 0, 235, 135));
        this.lowerArmL = new LowerArm(190, 55, this.upperArmL, new DefaultState(-20, 0, 235, 135));
        this.handR = new Hand(75, 60, this.lowerArmR, new DefaultState(-20, 0, 195, 35));
        this.handL = new Hand(75, 60, this.lowerArmL, new DefaultState(20, 0, 195, 35));
        this.upperLegR = new UpperLeg(250, 55, this.torso, new DefaultState(0, 145, 405, 90));
        this.upperLegL = new UpperLeg(250, 55, this.torso, new DefaultState(0, 10, 405, 90));
        this.lowerLegR = new LowerLeg(210, 55, this.upperLegR, new DefaultState(0, 0, 255, 90));
        this.lowerLegL = new LowerLeg(210, 55, this.upperLegL, new DefaultState(0, 0, 255, 90));
        this.footR = new Foot(110, 50, this.lowerLegR, new DefaultState(-90, 0, 225, 35));
        this.footL = new Foot(110, 50, this.lowerLegL, new DefaultState(90, 0, 225,35));
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
