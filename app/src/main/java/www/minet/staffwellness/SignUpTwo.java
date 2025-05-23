package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class SignUpTwo extends AppCompatActivity {

    Button next;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_two);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.btn_next);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);

        next.setOnClickListener(v -> {

            if (!validateGender() | !validateAge()) {
                return;
            }

            selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
            String _gender = selectedGender.getText().toString();

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String _date = day + "/" + month + "/" + year;

            //get all values passed from previous screen using Intent
            String _firstname = getIntent().getStringExtra("firstname");
            String _secondname = getIntent().getStringExtra("secondname");
            String _username = getIntent().getStringExtra("username");

            Intent intent = new Intent(SignUpTwo.this, SignUpThree.class);

            //pass all fields to the next activity
            intent.putExtra("firstname", _firstname);
            intent.putExtra("secondname", _secondname);
            intent.putExtra("username", _username);
            intent.putExtra("gender", _gender);
            intent.putExtra("date", _date);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpTwo.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 18) {
            Toast.makeText(this, "You are underage", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

}