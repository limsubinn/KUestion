package retrofit;

import com.google.gson.annotations.SerializedName;

// 로그인 요청시 보낼 데이터
public class LoginData {

    @SerializedName("id")
    String id;

    @SerializedName("passwd")
    String passwd;

    public LoginData(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
    }
}
