package www.minet.staffwellness;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Leaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        animateBarHeight(R.id.bar_first, 180);
        animateBarHeight(R.id.bar_second, 150);
        animateBarHeight(R.id.bar_third, 130);

    }

    private void animateBarHeight(int viewId, int targetDp) {
        final LinearLayout bar = findViewById(viewId);
        final int targetPx = (int) (targetDp * getResources().getDisplayMetrics().density);
        ValueAnimator animator = ValueAnimator.ofInt(0, targetPx);
        animator.setDuration(3000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = bar.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            bar.setLayoutParams(params);
        });
        animator.start();
    }

}