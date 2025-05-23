package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpThree extends AppCompatActivity {

    Button next;
    TextInputLayout staffnumber;
    Spinner departmentspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_three);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.btn_next);
        staffnumber = findViewById(R.id.signup_staff);
        departmentspinner = findViewById(R.id.department_spinner);

        next.setOnClickListener(v -> {

            if(!validateStaffNumber() | !validateDepartment()) {
                return;
            }

            String _staffnumber = staffnumber.getEditText().getText().toString();
            String _department = departmentspinner.getSelectedItem().toString();


            //get all values passed from previous screen using Intent
            String _firstname = getIntent().getStringExtra("firstname");
            String _secondname = getIntent().getStringExtra("secondname");
            String _username = getIntent().getStringExtra("username");
            String _gender = getIntent().getStringExtra("gender");
            String _date = getIntent().getStringExtra("date");

            Intent intent = new Intent(SignUpThree.this, SignUpFour.class);

            //pass all fields to the next activity
            intent.putExtra("firstname", _firstname);
            intent.putExtra("secondname", _secondname);
            intent.putExtra("username", _username);
            intent.putExtra("gender", _gender);
            intent.putExtra("date", _date);
            intent.putExtra("staffnumber", _staffnumber);
            intent.putExtra("department", _department);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpThree.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

    private boolean validateStaffNumber() {
        String checkspaces = "\\A\\w{1,20}\\z";

        String val = staffnumber.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            staffnumber.setError("please enter your staff number");
            return false;
        } else if (val.length() > 5) {
            staffnumber.setError("Staff Number is too long");
            return false;
        } else if (val.length() < 5) {
            staffnumber.setError("Staff Number is too short");
            return false;
        } else if (!val.matches(checkspaces)) {
            staffnumber.setError("space is not allowed");
            return false;
        } else {
            staffnumber.setError(null);
            staffnumber.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateDepartment() {
        if (departmentspinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a department", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}