package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpFive extends AppCompatActivity {

    Button next;
    Button profile;
    ImageView profileImage;
    Uri selectedImageUri;
    private static final int GALLERY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_five);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.btn_next);
        profile = findViewById(R.id.btn_add_profile);
        profileImage = findViewById(R.id.signup_profile);

        // Open gallery when profile button is clicked
        profile.setOnClickListener(v -> openGallery());

        next.setOnClickListener(v -> {

            if (selectedImageUri == null) {
                Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
                return;
            }

            //get all values passed from previous screens
            String _firstname = getIntent().getStringExtra("firstname");
            String _secondname = getIntent().getStringExtra("secondname");
            String _username = getIntent().getStringExtra("username");
            String _gender = getIntent().getStringExtra("gender");
            String _date = getIntent().getStringExtra("date");
            String _staffnumber = getIntent().getStringExtra("staffnumber");
            String _department = getIntent().getStringExtra("department");
            String _email = getIntent().getStringExtra("email");
            String _phonenumber = getIntent().getStringExtra("phonenumber");
            String _password = getIntent().getStringExtra("_password");

            Intent intent = new Intent(SignUpFive.this, FitnessThree.class);

            //add transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(findViewById(R.id.sign_title), "transition_text");
            pairs[1] = new Pair<View, String>(findViewById(R.id.sign_desc), "transition_desc");
            pairs[2] = new Pair<View, String>(findViewById(R.id.btn_next), "transition_next");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpFive.this, pairs);
            startActivity(intent, options.toBundle());

        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri); // Show selected image
        }
    }

}