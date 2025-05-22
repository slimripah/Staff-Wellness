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

public class ForgetPasswordTwo extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password_two);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.btn_next);

        next.setOnClickListener(v -> {

            Intent intent = new Intent(ForgetPasswordTwo.this, ForgetPasswordThree.class);

            //add transition
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_dos");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPasswordTwo.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

}