package www.minet.staffwellness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayActivity extends AppCompatActivity {

    private ConstraintLayout rootLayout;
    private SwipeListener swipeListener;
    private TextView timeDay;
    SharedPreferences tracking;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rootLayout = findViewById(R.id.main);
        timeDay = findViewById(R.id.timeDay);
        Date currentdate = new Date();
        String stringDate = DateFormat.getDateInstance().format(currentdate);
        timeDay.setText(stringDate);
        swipeListener = new SwipeListener(rootLayout);

    }

    private class SwipeListener implements View.OnTouchListener {

        GestureDetector gestureDetector;


        SwipeListener(View view) {
            double threhold = 200;
            double velocity_threhold = 200;


            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                    float yDiff = e2.getY() - e1.getY();
                    try {
                        if (Math.abs(yDiff) > threhold && Math.abs(velocityY) > velocity_threhold) {
                            if (yDiff < 0) {
                                //Swipe up, store date
                                SharedPreferences tracking = getSharedPreferences("tracking", MODE_PRIVATE);
                                ed = tracking.edit();
                                Date storedate = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");

                                ed.putString("counting", formatter.format(storedate));
                                ed.apply();


                                Intent intent = new Intent(DayActivity.this, NightActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                            }

                        }
                        return true;
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }
            };


            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);

        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }

    //ON BACK PRESSED
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}