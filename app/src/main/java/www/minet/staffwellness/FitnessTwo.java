package www.minet.staffwellness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class FitnessTwo extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "GoogleFitLogin";
    private GoogleSignInClient googleSignInClient;
    private static final String PREF_NAME = "wearable_prefs";
    private static final String KEY_SIGNED_IN = "signed_in";

    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fitness_two);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signIn = findViewById(R.id.btn_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                        new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/fitness.activity.read"),
                        new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/fitness.heart_rate.read"),
                        new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/fitness.nutrition.read")
                )
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(v -> signIn());

        // Check if the user has already signed in
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (preferences.getBoolean(KEY_SIGNED_IN, false)) {
            // If signed in, skip the login and go directly to Wearable activity
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
            return;
        }

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Signed in as " + account.getEmail(), Toast.LENGTH_SHORT).show();

            // Save sign-in status in SharedPreferences
            SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_SIGNED_IN, true);
            editor.apply();

            // Open the dashboard activity
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();

        } catch (ApiException e) {
            Log.e(TAG, "Sign-in failed: " + e.getStatusCode());
            Toast.makeText(this, "Sign-in failed!", Toast.LENGTH_SHORT).show();
        }

    }

}