package www.minet.staffwellness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SleepCircleView extends View {

    private Paint circlePaint, arcPaint, thumbPaint, textPaint;
    private float radius;
    private float centerX, centerY;
    private RectF arcBounds;

    private double bedtimeAngle = 240;  // 10:00 PM
    private double wakeupAngle = 90;    // 6:00 AM

    private float thumbRadius = 30f;
    private boolean movingStart = false, movingEnd = false;

    public SleepCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.LTGRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(20);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setColor(Color.BLUE);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(Color.DKGRAY);
        thumbPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        radius = Math.min(centerX, centerY) - 60;

        arcBounds = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Base circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Sleep arc
        float startAngle = (float) bedtimeAngle;
        float sweepAngle = (float) ((wakeupAngle - bedtimeAngle + 360) % 360);
        canvas.drawArc(arcBounds, startAngle, sweepAngle, false, arcPaint);

        // Bedtime thumb
        float[] bedXY = angleToXY(bedtimeAngle);
        canvas.drawCircle(bedXY[0], bedXY[1], thumbRadius, thumbPaint);

        // Wakeup thumb
        float[] wakeXY = angleToXY(wakeupAngle);
        canvas.drawCircle(wakeXY[0], wakeXY[1], thumbRadius, thumbPaint);

        // Draw text: sleep duration
        int hours = (int) (sweepAngle / 15); // each 15 degrees = 1 hour
        canvas.drawText(hours + " hr", centerX, centerY + 15, textPaint);
    }

    private float[] angleToXY(double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);
        float x = (float) (centerX + radius * Math.cos(angleRad));
        float y = (float) (centerY + radius * Math.sin(angleRad));
        return new float[]{x, y};
    }

    private double xyToAngle(float x, float y) {
        double angle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        angle = (angle + 360) % 360;
        return angle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        double angle = xyToAngle(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float[] bedXY = angleToXY(bedtimeAngle);
                float[] wakeXY = angleToXY(wakeupAngle);
                if (distance(x, y, bedXY[0], bedXY[1]) < thumbRadius * 1.5) {
                    movingStart = true;
                } else if (distance(x, y, wakeXY[0], wakeXY[1]) < thumbRadius * 1.5) {
                    movingEnd = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (movingStart) {
                    bedtimeAngle = angle;
                    invalidate();
                } else if (movingEnd) {
                    wakeupAngle = angle;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                movingStart = false;
                movingEnd = false;
                break;
        }

        return true;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}