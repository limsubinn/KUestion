package retrofit;

import com.google.gson.annotations.SerializedName;

public class FindPwResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("pw")
    private String pw;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}