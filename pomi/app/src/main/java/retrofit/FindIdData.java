package retrofit;

import com.google.gson.annotations.SerializedName;

public class FindIdData {

    @SerializedName("name")
    private String name;

    @SerializedName("tel")
    private String tel;

    public FindIdData(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }
}
