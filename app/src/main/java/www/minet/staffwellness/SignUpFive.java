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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpFive extends AppCompatActivity {

    Button next, profile;
    ImageView profileImage;
    Uri selectedImageUri;
    private static final int GALLERY_REQUEST_CODE = 101;

    // User data passed from previous activities
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

        // Get passed data
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
                uploadDataToApi(
                        selectedImageUri,
                        _firstname,
                        _secondname,
                        _username,
                        _gender,
                        _date,
                        _staffnumber,
                        _department,
                        _email,
                        _phonenumber,
                        _password
                );
            }
        });
    }

    private void uploadDataToApi(
            Uri imageUri,
            String firstname,
            String secondname,
            String username,
            String gender,
            String date,
            String staffnumber,
            String department,
            String email,
            String phonenumber,
            String password
    ) {
        try {
            String imagePath = getRealPathFromURI(imageUri);
            if (imagePath == null) {
                Toast.makeText(this, "Unable to get file path", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(imagePath);
            if (!file.exists()) {
                Toast.makeText(this, "Image file does not exist", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("profile_image", file.getName(), imageBody);

            // Prepare other form data
            RequestBody rbFirstname = RequestBody.create(MediaType.parse("text/plain"), firstname);
            RequestBody rbSecondname = RequestBody.create(MediaType.parse("text/plain"), secondname);
            RequestBody rbUsername = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody rbGender = RequestBody.create(MediaType.parse("text/plain"), gender);
            RequestBody rbDate = RequestBody.create(MediaType.parse("text/plain"), date);
            RequestBody rbStaffNumber = RequestBody.create(MediaType.parse("text/plain"), staffnumber);
            RequestBody rbDepartment = RequestBody.create(MediaType.parse("text/plain"), department);
            RequestBody rbEmail = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody rbPhone = RequestBody.create(MediaType.parse("text/plain"), phonenumber);
            RequestBody rbPassword = RequestBody.create(MediaType.parse("text/plain"), password);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://YOUR_IP_ADDRESS/staffwellness/api/") // Update with your actual IP and endpoint
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            Call<ResponseBody> call = apiService.uploadUser(
                    rbFirstname,
                    rbSecondname,
                    rbUsername,
                    rbGender,
                    rbDate,
                    rbStaffNumber,
                    rbDepartment,
                    rbEmail,
                    rbPhone,
                    rbPassword,
                    imagePart
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUpFive.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        openNextPage();
                    } else {
                        Toast.makeText(SignUpFive.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(SignUpFive.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
