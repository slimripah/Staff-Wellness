package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    LineChart linechart;
    LineDataSet dataset;
    LineData linedata;

    Typeface typeface;

    BarChart barchart;

    BarDataSet barDataSet;

    Button steps;
    Button calories;
    Button heart;
    Button schedule;
    SharedPreferences prefs;
    String savedBedtime;
    String savedWakeup;

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

        steps = findViewById(R.id.btn_steps);
        calories = findViewById(R.id.btn_calories);
        heart = findViewById(R.id.btn_heart);
        schedule = findViewById(R.id.btn_schedule);
        prefs = getSharedPreferences("sleep_schedule", MODE_PRIVATE);
        savedBedtime = prefs.getString("bedtime", null);
        savedWakeup = prefs.getString("wakeup", null);

        if (savedBedtime != null && savedWakeup != null) {

            TextView goBed = findViewById(R.id.go_bed);
            TextView goUp = findViewById(R.id.go_up);

            goBed.setText(savedBedtime);
            goUp.setText(savedWakeup);

        }

        steps.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Steps.class);
            startActivity(intent);
        });

        calories.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Calories.class);
            startActivity(intent);
        });

        heart.setOnClickListener(v -> {

            Intent intent = new Intent(Dashboard.this, Heart.class);

            //add transition
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(findViewById(R.id.lottie_heartbeat),"pulsating");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this, pairs);

            startActivity(intent, options.toBundle());
        });

        schedule.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Schedule.class);
            startActivityForResult(intent, 100); // 100 = requestCode
        });

        typeface = ResourcesCompat.getFont(this, R.font.lineto_circular_pro_bold);
        linechart = findViewById(R.id.lineChart);

        // Load from strings.xml
        String[] stepStrings = getResources().getStringArray(R.array.step_values);
        String[] dayLabels = getResources().getStringArray(R.array.week_days);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < stepStrings.length; i++) {
            float stepCount = Float.parseFloat(stepStrings[i]);
            entries.add(new Entry(i, stepCount));
        }

        dataset = new LineDataSet(entries, "Weekly Steps");

        dataset.setColor(Color.parseColor("#4285F4"));
        dataset.setLineWidth(1f);
        dataset.setCircleColor(Color.parseColor("#4285F4"));
        dataset.setCircleRadius(3f);
        dataset.setDrawValues(false);
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true);
        dataset.setFillColor(Color.parseColor("#D6E4FF"));

        linedata = new LineData(dataset);
        linechart.setData(linedata);

        // Animate chart
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setCubicIntensity(0.15f); // ultra smooth bends
        linechart.animateX(3000, Easing.EaseInOutSine);

        // Customize X-Axis
        XAxis xAxis = linechart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dayLabels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTypeface(typeface);

        // Customize Y-Axis
        linechart.getAxisRight().setEnabled(false);
        linechart.getAxisLeft().setGranularity(1000f);

        // Apply to Y Axis (left only, since we disabled the right one)
        YAxis yAxis = linechart.getAxisLeft();
        yAxis.setTypeface(typeface);

        // Apply to Legend
        Legend legend = linechart.getLegend();
        legend.setEnabled(false);

        // Other tweaks
        linechart.getDescription().setEnabled(false);
        linechart.invalidate(); // refresh the chart

        barchart = findViewById(R.id.barChart);

        // Sample data in hours (float)
        float[] hoursPerDay = {5.0f, 6.5f, 7.25f, 8.0f, 5.75f, 6.0f, 7.5f};
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // Convert to BarEntry list
        List<BarEntry> entry = new ArrayList<>();
        for (int i = 0; i < hoursPerDay.length; i++) {
            entry.add(new BarEntry(i, hoursPerDay[i]));
        }

        barDataSet = new BarDataSet(entry, "Daily Time");
        barDataSet.setColor(Color.parseColor("#668c5c"));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barchart.setData(data);
        barchart.setFitBars(true);
        barchart.animateY(3000, Easing.EaseInOutQuad);

        // X-Axis formatting
        XAxis xxAxis = barchart.getXAxis();
        xxAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xxAxis.setGranularity(1f);
        xxAxis.setDrawGridLines(false);
        xxAxis.setDrawLabels(false);
        xxAxis.setDrawAxisLine(false);

        // Y-Axis formatting for "hh:mm"
        YAxis yyAxis = barchart.getAxisLeft();
        yyAxis.setGranularity(0.5f); // half-hour intervals
        yyAxis.setDrawLabels(false);
        yyAxis.setDrawGridLines(false);
        yyAxis.setDrawAxisLine(false);

        yyAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int totalMinutes = Math.round(value * 60f);
                int hours = totalMinutes / 60;
                int minutes = totalMinutes % 60;
                return String.format("%dh %02dm", hours, minutes);
            }
        });

        // Disable the right Y-axis
        barchart.getAxisRight().setEnabled(false);

        // General appearance settings
        barchart.getDescription().setEnabled(false);
        barchart.getLegend().setEnabled(false);
        barchart.invalidate(); // Refresh the chart

        // Remove legend and description
        barchart.getLegend().setEnabled(false);
        barchart.getDescription().setEnabled(false);

        // Refresh the chart
        barchart.invalidate();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String bedtime = data.getStringExtra("BEDTIME");
            String wakeup = data.getStringExtra("WAKEUP");

            // Save to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("sleep_schedule", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("bedtime", bedtime);
            editor.putString("wakeup", wakeup);
            editor.apply();

            TextView goBed = findViewById(R.id.go_bed);
            TextView goUp = findViewById(R.id.go_up);

            goBed.setText(bedtime);
            goUp.setText(wakeup);
        }

    }

}