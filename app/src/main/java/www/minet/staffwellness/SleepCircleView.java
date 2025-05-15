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

    private Paint circlePaint, arcPaint, thumbPaint, textPaint, clockLabelPaint;
    private float thumbRadius = 30f;
    private RectF arcBounds;

    private double bedtimeAngle = 240; // 10:00 PM
    private double wakeupAngle = 90;   // 6:00 AM

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
        textPaint.setTextSize(30f);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        clockLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clockLabelPaint.setColor(Color.DKGRAY);
        clockLabelPaint.setTextSize(30f);
        clockLabelPaint.setTextAlign(Paint.Align.CENTER);
        clockLabelPaint.setTypeface(getResources().getFont(R.font.lineto_circular_pro_bold));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - 60;

        arcBounds = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw outer base circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw arc between bedtime and wakeup
        float startAngle = (float) bedtimeAngle;
        float sweepAngle = (float) ((wakeupAngle - bedtimeAngle + 360) % 360);
        canvas.drawArc(arcBounds, startAngle, sweepAngle, false, arcPaint);

        // Draw clock labels (1 to 12) around the circle
        float labelRadius = radius - 30;
        for (int i = 0; i < 12; i++) {
            int hour = (i == 0) ? 12 : i;
            double angle = Math.toRadians(i * 30 - 90);
            float x = (float) (centerX + labelRadius * Math.cos(angle));
            float y = (float) (centerY + labelRadius * Math.sin(angle)) + 10;
            canvas.drawText(String.valueOf(hour), x, y, clockLabelPaint);
        }

        // Draw bedtime thumb
        float[] bedXY = angleToXY(bedtimeAngle, centerX, centerY, radius);
        canvas.drawCircle(bedXY[0], bedXY[1], thumbRadius, thumbPaint);

        // Draw wakeup thumb
        float[] wakeXY = angleToXY(wakeupAngle, centerX, centerY, radius);
        canvas.drawCircle(wakeXY[0], wakeXY[1], thumbRadius, thumbPaint);

        // Draw sleep duration text
        int hours = (int) (sweepAngle / 15); // 15 degrees = 1 hour
        canvas.drawText(hours + " hr", centerX, centerY + 15, textPaint);
    }

    private float[] angleToXY(double angleDegrees, float centerX, float centerY, float radius) {
        double angleRad = Math.toRadians(angleDegrees);
        float x = (float) (centerX + radius * Math.cos(angleRad));
        float y = (float) (centerY + radius * Math.sin(angleRad));
        return new float[]{x, y};
    }

    private double xyToAngle(float x, float y, float centerX, float centerY) {
        double angle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        return (angle + 360) % 360;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x2 - x1, y2 - y1);
    }

    private String angleToTimeString(double angle) {
        int totalMinutes = (int) ((angle / 360) * 1440);
        int hours = (totalMinutes / 60) % 24;
        int minutes = totalMinutes % 60;
        String amPm = hours >= 12 ? "PM" : "AM";
        int displayHour = (hours % 12 == 0) ? 12 : hours % 12;
        return String.format("%02d:%02d %s", displayHour, minutes, amPm);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - 60;

        double angle = xyToAngle(x, y, centerX, centerY);

        float[] bedXY = angleToXY(bedtimeAngle, centerX, centerY, radius);
        float[] wakeXY = angleToXY(wakeupAngle, centerX, centerY, radius);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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

    public String getBedtime() {
        return angleToTimeString(bedtimeAngle);
    }

    public String getWakeup() {
        return angleToTimeString(wakeupAngle);
    }

    public void setStartTime(int minutes) {
        this.bedtimeAngle = (minutes / 4) % 360;
        invalidate();
    }

    public void setEndTime(int minutes) {
        this.wakeupAngle = (minutes / 4) % 360;
        invalidate();
    }
}