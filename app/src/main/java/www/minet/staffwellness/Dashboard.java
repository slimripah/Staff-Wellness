package www.minet.staffwellness;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void animateRings() {
        ImageView ringBlue = findViewById(R.id.ringBlue);
        ImageView ringGreen = findViewById(R.id.ringGreen);
        ImageView ringRed = findViewById(R.id.ringRed);

        ObjectAnimator blueAnim = ObjectAnimator.ofInt(ringBlue, "level", 0, 10000);
        blueAnim.setDuration(1500);

        ObjectAnimator greenAnim = ObjectAnimator.ofInt(ringGreen, "level", 0, 8000);
        greenAnim.setDuration(1500);

        ObjectAnimator redAnim = ObjectAnimator.ofInt(ringRed, "level", 0, 6000);
        redAnim.setDuration(1500);

        blueAnim.start();
        greenAnim.start();
        redAnim.start();
    }

}