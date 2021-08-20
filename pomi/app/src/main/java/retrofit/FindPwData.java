package retrofit;

import com.google.gson.annotations.SerializedName;

public class FindPwData {

    @SerializedName("hint_A")
    private String hint_A;

    public FindPwData(String hint_A) {
        this.hint_A = hint_A;
    }
}
