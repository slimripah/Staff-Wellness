package www.minet.staffwellness;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    LineChart linechart;
    LineDataSet dataset;
    LineData linedata;

    Typeface typeface;

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

    }

}