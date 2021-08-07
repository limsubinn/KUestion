package kr.co.company.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit.JoinData;
import retrofit.JoinResponse;
import retrofit.RetrofitClient;
import retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    private EditText joinID;
    private EditText joinPW;
    private EditText checkPW;
    private EditText username;
    private EditText phone;
    private Spinner spinner;
    private EditText hint;
    private CheckBox agree;
    private Button join;
    private ServiceApi service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinID = (EditText) findViewById(R.id.joinID);
        joinPW = (EditText) findViewById(R.id.joinPW);
        checkPW = (EditText) findViewById(R.id.checkPW);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        spinner = (Spinner) findViewById(R.id.spinner);
        hint = (EditText) findViewById(R.id.hint);
        agree = (CheckBox) findViewById(R.id.agree);
        join = (Button) findViewById(R.id.join);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptJoin();
            }
        });
    }

    private void attemptJoin() {
        username.setError(null);
        joinID.setError(null);
        joinPW.setError(null);
        checkPW.setError(null);
        phone.setError(null);
        hint.setError(null);
        join.setError(null);
        agree.setError(null);

        String name = username.getText().toString();
        String id = joinID.getText().toString();
        String passwd = joinPW.getText().toString();
        String passwd2 = checkPW.getText().toString();
        String tel = phone.getText().toString();
        String hint_Q = spinner.getSelectedItem().toString();
        String hint_A = hint.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (passwd.isEmpty()) {
            joinPW.setError("비밀번호를 입력해주세요.");
            focusView = joinPW;
            cancel = true;
        }

        if (passwd2.isEmpty()) {
            checkPW.setError("비밀번호를 다시 입력해주세요.");
            focusView = checkPW;
            cancel = true;
        } else if (!passwd2.equals(passwd)) {
            checkPW.setError("입력하신 비밀번호가 일치하지 않습니다.");
            focusView = checkPW;
            cancel = true;
        }

        // 이메일의 유효성 검사
        if (id.isEmpty()) {
            joinID.setError("아이디를 입력해주세요.");
            focusView = joinID;
            cancel = true;
        }

        // 이름의 유효성 검사
        if (name.isEmpty()) {
            username.setError("이름을 입력해주세요.");
            focusView = username;
            cancel = true;
        }

        if (tel.isEmpty()) {
            phone.setError("전화번호를 입력해주세요.");
            focusView = phone;
            cancel = true;
        }

        if (hint_A.isEmpty()) {
            hint.setError("비밀번호 힌트에 대한 답을 입력해주세요.");
            focusView = hint;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startJoin(new JoinData(id, passwd, name, tel, hint_A, hint_Q));
        }
    }

    private void startJoin(JoinData data) {
        service.userJoin(data).enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                JoinResponse result = response.body();
                Toast.makeText(JoinActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Toast.makeText(JoinActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }
}
