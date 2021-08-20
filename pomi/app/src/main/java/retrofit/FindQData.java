package retrofit;

import com.google.gson.annotations.SerializedName;

public class FindQData {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public FindQData (String id, String name) {
        this.id = id;
        this.name = name;
    }
}