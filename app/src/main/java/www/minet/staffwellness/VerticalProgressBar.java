package www.minet.staffwellness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class VerticalProgressBar extends View {

    private float progress = 0f;
    private float max = 1000f;
    private Paint paint;

    public VerticalProgressBar(Context context) {
        super(context);
        init();
    }

    public VerticalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF4CAF50); // green
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float heightRatio = progress / max;
        float fillHeight = heightRatio * getHeight();

        float top = getHeight() - fillHeight;
        float bottom = getHeight();

        canvas.drawRoundRect(0, top, getWidth(), bottom, 20, 20, paint);
    }

    // ADD THIS METHOD HERE
    public void setProgress(float value) {
        if (value < 0f) value = 0f;
        if (value > max) value = max;
        progress = value;
        invalidate();
    }

    public void setMax(float maxValue) {
        this.max = maxValue;
        invalidate();
    }

}