package www.minet.staffwellness;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Calories extends AppCompatActivity {

    VerticalProgressBar progressBar;
    TextView caloryText;
    RelativeLayout container;
    String countStr;
    int targetCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calories);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.vertical_progress);
        caloryText = findViewById(R.id.calory_count);
        container = findViewById(R.id.calory_progress_container);
        countStr = caloryText.getText().toString().replaceAll("[^0-9]", "");
        targetCalories = Integer.parseInt(countStr);

        // Parse kcal value
        String kcalText = caloryText.getText().toString().replaceAll("[^0-9]", "");
        int kcal = 0;
        try {
            kcal = Integer.parseInt(kcalText);
        } catch (NumberFormatException ignored) {
        }

        progressBar.setProgress(kcal);

        progressBar.post(() -> {
            int barHeight = progressBar.getHeight(); // get actual height
            int labelCount = 21; // 0 to 1000 kcal, every 50 kcal

            for (int i = 0; i < labelCount; i++) {
                int value = 50 * (i + 1); // scale to start from 50 up to 1050
                TextView label = new TextView(this);
                label.setText(value + " kcal");
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                label.setTypeface(getResources().getFont(R.font.lineto_circular_pro_bold));

                int margin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                // Calculate Y offset from bottom to top
                float ratio = i / (float) (labelCount - 1);
                int yOffset = (int) (barHeight - (ratio * barHeight)); // bottom = 0 kcal

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                if (i % 2 == 0) {
                    params.addRule(RelativeLayout.LEFT_OF, progressBar.getId());
                    params.setMarginEnd(margin);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, progressBar.getId());
                    params.setMarginStart(margin);
                }

                label.setLayoutParams(params);
                label.setTranslationY(yOffset - label.getHeight() / 2f); // Center align

                container.addView(label);

            }

        });

        // Animate progress from 0 to targetCalories
        ValueAnimator animator = ValueAnimator.ofFloat(0, targetCalories);
        animator.setDuration(3500); // 1.5 seconds
        animator.addUpdateListener(animation -> {
            float currentValue = (float) animation.getAnimatedValue();
            progressBar.setProgress(currentValue);
        });
        animator.start();

    }

}