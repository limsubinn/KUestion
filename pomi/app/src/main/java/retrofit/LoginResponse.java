package retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    @SerializedName("flag")
    private int flag;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getFlag() { return flag; }

    public String getUserName() {
        return name;
    }
}