package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpFour extends AppCompatActivity {

    Button next;

    TextInputLayout email, phonenumber, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_four);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.btn_next);
        email = findViewById(R.id.signup_email);
        phonenumber = findViewById(R.id.signup_phone);
        password = findViewById(R.id.signup_password);

        next.setOnClickListener(v -> {

            if (!validateEmail() | !validatePhoneNumber() | !validatePassword()) {
                return;
            }

            String _email = email.getEditText().getText().toString();
            String _phonenumber = phonenumber.getEditText().getText().toString();
            String _password = password.getEditText().getText().toString();

            //get all values passed from previous screens
            String _firstname = getIntent().getStringExtra("firstname");
            String _secondname = getIntent().getStringExtra("secondname");
            String _username = getIntent().getStringExtra("username");
            String _gender = getIntent().getStringExtra("gender");
            String _date = getIntent().getStringExtra("date");
            String _staffnumber = getIntent().getStringExtra("staffnumber");
            String _department = getIntent().getStringExtra("department");

            Intent intent = new Intent(SignUpFour.this, SignUpFive.class);

            //pass all fields to the next activity
            intent.putExtra("firstname", _firstname);
            intent.putExtra("secondname", _secondname);
            intent.putExtra("username", _username);
            intent.putExtra("gender", _gender);
            intent.putExtra("date", _date);
            intent.putExtra("staffnumber", _staffnumber);
            intent.putExtra("department", _department);
            intent.putExtra("email", _email);
            intent.putExtra("phonenumber", _phonenumber);
            intent.putExtra("password", _password);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpFour.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

    private boolean validateEmail() {

        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("please enter email");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("invalid email");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {

        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                "(?=\\S+$)" +       //no white spaces
                ".{8,}" +           //at least 8 characters
                "$";

        if (val.isEmpty()) {
            password.setError("please enter password");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain at-least 4 characters. White spaces are not allowed");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePhoneNumber() {

        String val = phonenumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            phonenumber.setError("please enter phone number");
            return false;
        } else if (val.length() > 10) {
            phonenumber.setError("phone number is too long");
            return false;
        } else if (val.length() < 10) {
            phonenumber.setError("phone number is too short");
            return false;
        } else if (!val.matches(checkspaces)) {
            phonenumber.setError("No white spaces allowed");
            return false;
        } else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }

    }

}