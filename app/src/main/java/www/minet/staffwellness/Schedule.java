package www.minet.staffwellness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Schedule extends AppCompatActivity {

    SleepCircleView sleepView;
    Button save;
    SharedPreferences prefs;
    String savedBedtime;
    String savedWakeup;
    private SleepCircleView circularSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sleepView = findViewById(R.id.sleep_circle);
        save = findViewById(R.id.save_button);

        save.setOnClickListener(v -> {

            String bedtime = sleepView.getBedtime();
            String wakeup = sleepView.getWakeup();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("BEDTIME", bedtime);
            resultIntent.putExtra("WAKEUP", wakeup);
            setResult(RESULT_OK, resultIntent);
            finish();

        });

        prefs = getSharedPreferences("sleep_schedule", MODE_PRIVATE);
        savedBedtime = prefs.getString("bedtime", null);
        savedWakeup = prefs.getString("wakeup", null);

        if (savedBedtime != null && savedWakeup != null) {

            int bedtimeMinutes = convertToMinutes(savedBedtime);
            int wakeupMinutes = convertToMinutes(savedWakeup);

            sleepView.setStartTime(bedtimeMinutes);
            sleepView.setEndTime(wakeupMinutes);

        }

    }

    private int convertToMinutes(String timeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf.parse(timeStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // fallback
        }
    }

}