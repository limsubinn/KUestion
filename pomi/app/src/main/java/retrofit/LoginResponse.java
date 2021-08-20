package retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return name;
    }

    public String getUserId() { return id; }
}