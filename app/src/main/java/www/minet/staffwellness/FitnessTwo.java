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

public class FitnessTwo extends AppCompatActivity {

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

        signIn.setOnClickListener(v -> {

            Intent intent = new Intent(FitnessTwo.this, Dashboard.class);

            //add transition
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(findViewById(R.id.btn_sign_in), "fit");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FitnessTwo.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

}