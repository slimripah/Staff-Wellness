package www.minet.staffwellness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatActivity extends AppCompatActivity {

    SharedPreferences tracking;
    SharedPreferences.Editor ed;
    CircularProgressBar progressBar;
    TextView progressText;
    TextView datestat, wenttosleep, wakeup;
    Button backtohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressWithAnimation(75F, 1500L);

        initUI();
        statset();

        backtohome = findViewById(R.id.backtohome);
        backtohome.setOnClickListener(view -> {

            Intent intent = new Intent(StatActivity.this, Dashboard.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.slideup, R.anim.slidedown);

        });

    }

    private void statset() {

        SharedPreferences tracking = getSharedPreferences("tracking", MODE_PRIVATE);
        ed = tracking.edit();
        Date curdate = new Date();
        datestat.setText(DateFormat.getDateInstance().format(curdate));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        SimpleDateFormat displaytime = new SimpleDateFormat("HH:mm:ss");

        try {
            Date storedate = formatter.parse(tracking.getString("counting", formatter.format(curdate)));
            double different = curdate.getTime() - storedate.getTime();
            double hour = different / 3600000;

            int progress = (int) Math.round(hour * 45); // Convert hours to progress

            // Make sure progress is within valid range
            progress = Math.max(0, Math.min(progress, 360));

            // Convert to percentage
            int percentage = Math.round(progress / 3.6f);

            progressBar.setProgress(progress);
            progressText.setText(percentage + "%");

            wenttosleep.setText(displaytime.format(storedate));
            wakeup.setText(displaytime.format(curdate));

            ed.remove("counting");
            ed.apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        datestat = findViewById(R.id.datestat);
        wenttosleep = findViewById(R.id.wenttosleep);
        wakeup = findViewById(R.id.wakeup);

    }

}