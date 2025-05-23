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

public class SignUpOne extends AppCompatActivity {

    Button next;
    TextInputLayout firstname, secondname, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_one);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstname = findViewById(R.id.signup_firstName);
        secondname = findViewById(R.id.signup_secondName);
        username = findViewById(R.id.signup_username);

        next = findViewById(R.id.btn_next);

        next.setOnClickListener(v -> {

            if (!validateFirstname() | !validateSecondname() | !validateUsername()) {
                return;
            }

            String _firstname = firstname.getEditText().getText().toString();
            String _secondname = secondname.getEditText().getText().toString();
            String _username = username.getEditText().getText().toString();

            Intent intent = new Intent(SignUpOne.this, SignUpTwo.class);

            //pass all fields to the next activity
            intent.putExtra("firstname", _firstname);
            intent.putExtra("secondname", _secondname);
            intent.putExtra("username", _username);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpOne.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

    private boolean validateFirstname() {

        String val = firstname.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            firstname.setError("please enter your first name");
            return false;
        } else if (val.length() > 20) {
            firstname.setError("Name is too long");
            return false;
        } else if (val.length() < 3) {
            firstname.setError("Name is too short");
            return false;
        } else {
            firstname.setError(null);
            firstname.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateSecondname() {

        String val = secondname.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            secondname.setError("please enter your second name");
            return false;
        } else if (val.length() > 20) {
            secondname.setError("Name is too long");
            return false;
        } else if (val.length() < 3) {
            secondname.setError("Name is too short");
            return false;
        } else {
            secondname.setError(null);
            secondname.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateUsername() {

        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("please enter username");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too long");
            return false;
        } else if (val.length() < 3) {
            username.setError("Username is too long");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("space is not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

}