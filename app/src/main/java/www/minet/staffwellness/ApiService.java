package www.minet.staffwellness;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class ApiService {

    @Multipart
    @POST("staffapi/signup.php")
    Call<ResponseBody> registerUser(
            @Part("firstname") RequestBody firstname,
            @Part("secondname") RequestBody secondname,
            @Part("username") RequestBody username,
            @Part("gender") RequestBody gender,
            @Part("date") RequestBody date,
            @Part("staffnumber") RequestBody staffnumber,
            @Part("department") RequestBody department,
            @Part("email") RequestBody email,
            @Part("phonenumber") RequestBody phonenumber,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part profile_image
    ) {
        return null;
    }

}