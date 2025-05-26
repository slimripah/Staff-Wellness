package www.minet.staffwellness;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFive extends AppCompatActivity {

    Button next;
    Button profile;
    ImageView profileImage;
    Uri selectedImageUri;
    private static final int GALLERY_REQUEST_CODE = 101;

    // Declare user data
    String _firstname, _secondname, _username, _gender, _date, _staffnumber, _department, _email, _phonenumber, _password;

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

        profile.setOnClickListener(v -> openGallery());

        // Retrieve data from previous screens
        Intent intent = getIntent();
        _firstname = intent.getStringExtra("firstname");
        _secondname = intent.getStringExtra("secondname");
        _username = intent.getStringExtra("username");
        _gender = intent.getStringExtra("gender");
        _date = intent.getStringExtra("date");
        _staffnumber = intent.getStringExtra("staffnumber");
        _department = intent.getStringExtra("department");
        _email = intent.getStringExtra("email");
        _phonenumber = intent.getStringExtra("phonenumber");
        _password = intent.getStringExtra("_password");

        next.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            } else {
                uploadDataToApi();
            }
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
            profileImage.setImageURI(selectedImageUri);
        }
    }

    private void uploadDataToApi() {
        File imageFile = new File(getRealPathFromURI(selectedImageUri));

        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"), _firstname);
        RequestBody secondname = RequestBody.create(MediaType.parse("text/plain"), _secondname);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), _username);
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), _gender);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), _date);
        RequestBody staffnumber = RequestBody.create(MediaType.parse("text/plain"), _staffnumber);
        RequestBody department = RequestBody.create(MediaType.parse("text/plain"), _department);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), _email);
        RequestBody phonenumber = RequestBody.create(MediaType.parse("text/plain"), _phonenumber);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), _password);

        RequestBody requestFile = RequestBody.create(MediaType.parse(getMimeType(selectedImageUri)), imageFile);
        MultipartBody.Part profile_image = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), requestFile);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.registerUser(
                firstname, secondname, username, gender, date, staffnumber,
                department, email, phonenumber, password, profile_image
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpFive.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    openNextPage();
                } else {
                    Toast.makeText(SignUpFive.this, "Failed to sign up. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignUpFive.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openNextPage() {
        Intent intent = new Intent(SignUpFive.this, FitnessThree.class);
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<>(findViewById(R.id.sign_title), "transition_text");
        pairs[1] = new Pair<>(findViewById(R.id.sign_desc), "transition_desc");
        pairs[2] = new Pair<>(findViewById(R.id.btn_next), "transition_next");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpFive.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

    private String getMimeType(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
