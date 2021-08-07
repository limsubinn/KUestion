package retrofit;

import com.google.gson.annotations.SerializedName;

public class JoinData {

    @SerializedName("id")
    private String id;

    @SerializedName("passwd")
    private String passwd;


    @SerializedName("name")
    private String name;

    @SerializedName("tel")
    private String tel;

    @SerializedName("hint_A")
    private String hint_A;

    @SerializedName("hint_Q")
    private String hint_Q;

    public JoinData(String id, String passwd, String name, String tel, String hint_A, String hint_Q) {
        this.id = id;
        this.passwd = passwd;
        this.name = name;
        this.tel = tel;
        this.hint_A = hint_A;
        this.hint_Q = hint_Q;
    }
}
