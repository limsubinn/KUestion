package retrofit;

import com.google.gson.annotations.SerializedName;

public class DeleteData {

    @SerializedName("passwd")
    private String passwd;

    @SerializedName("id")
    private String id;

    public DeleteData(String passwd, String id) {
        this.passwd = passwd;
        this.id= id;
    }

}