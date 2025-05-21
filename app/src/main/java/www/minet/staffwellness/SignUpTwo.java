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

public class SignUpTwo extends AppCompatActivity {

    Button next;

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

        next.setOnClickListener(v -> {

            Intent intent = new Intent(SignUpTwo.this, SignUpThree.class);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpTwo.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

}