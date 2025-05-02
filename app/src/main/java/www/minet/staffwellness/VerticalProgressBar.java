package www.minet.staffwellness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class VerticalProgressBar extends View {

    private float progress = 0f;
    private float max = 1000f;
    private Paint paint;
    private LinearGradient gradient;

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Set up the vertical gradient from bottom to top
        gradient = new LinearGradient(
                0, h, 0, 0, // from bottom to top
                new int[]{
                        0xFFa10100,
                        0xFFda1f05,
                        0xFFf33c04,
                        0xFFfe650d,
                        0xFFffc11f,
                        0xFFfff75d
                },
                null,
                Shader.TileMode.CLAMP
        );

        paint.setShader(gradient);
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