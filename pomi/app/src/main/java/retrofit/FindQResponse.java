package retrofit;

import com.google.gson.annotations.SerializedName;

public class FindQResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private String id;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}