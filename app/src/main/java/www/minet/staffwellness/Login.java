package www.minet.staffwellness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    Button login;
    Button forgot;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.btn_login);
        forgot = findViewById(R.id.btn_forget);
        create = findViewById(R.id.btn_create);

        login.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);

        });

        forgot.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, ForgetPasswordOne.class);
            startActivity(intent);

        });

        create.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, SignUpOne.class);
            startActivity(intent);

        });

    }

}