package retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/user/findID")
    Call<FindIdResponse> IdFind(@Body FindIdData data);

    @POST("/user/findQ")
    Call<FindQResponse> QFind(@Body FindQData data);

    @POST("/user/findPW")
    Call<FindPwResponse> PwFind(@Body FindPwData data);

    @POST("/user/checkID")
    Call<CheckIdResponse> IdCheck (@Body CheckIdData data);

    @POST("/user/delete")
    Call<DeleteResponse> userDelete (@Body DeleteData data);
}
