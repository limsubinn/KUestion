package retrofit;

import com.google.gson.annotations.SerializedName;

public class CheckIdData {

    @SerializedName("id")
    private String id;

    public CheckIdData (String id) {
        this.id = id;
    }
}