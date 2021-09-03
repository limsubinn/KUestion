package kr.co.company.login;

import android.app.AlertDialog;
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

import retrofit.CheckIdData;
import retrofit.CheckIdResponse;
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

        joinID = (EditText) findViewById(R.id.joinID);;
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

       // CheckBox agree = (CheckBox) findViewById(R.id.agree) ;
        agree.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
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

        if (!agree.isChecked()) {
            Toast.makeText(JoinActivity.this, "개인정보 활용 동의를 체크해주세요!", Toast.LENGTH_LONG).show();
            agree.setError("");
            focusView = agree;
            cancel = true;
        }

        //비밀번호 힌트에 대한 유효성 검사
        if (hint_A.isEmpty()) {
            hint.setError("비밀번호 힌트에 대한 답을 입력해주세요.");
            focusView = hint;
            cancel = true;
        }


        //전화번호의 유효성 검사
        int telCheck = telIsValid(tel);
        if (tel.isEmpty()) {
            phone.setError("전화번호를 입력해주세요.");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 1) {
            phone.setError("전화번호 입력 수가 맞지 않습니다!");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 2) {
            phone.setError("숫자만을 입력해야 합니다.");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 3) {
            phone.setError("010부터 입력해야 합니다.");
            focusView = phone;
            cancel = true;
        }


        // 이름의 유효성 검사
        if (name.isEmpty()) {
            username.setError("이름을 입력해주세요.");
            focusView = username;
            cancel = true;
        }


        //비밀번호 확인
        if (passwd2.isEmpty()) {
            checkPW.setError("비밀번호를 다시 입력해주세요.");
            focusView = checkPW;
            cancel = true;
        } else if (!passwd2.equals(passwd)) {
            checkPW.setError("입력하신 비밀번호가 일치하지 않습니다.");
            focusView = checkPW;
            cancel = true;
        }


        // 패스워드의 유효성 검사
        int pwCheck = pwIsValid(passwd);

        if (passwd.isEmpty()) {
            joinPW.setError("비밀번호를 입력해주세요.");
            focusView = joinPW;
            cancel = true;
        } else if (pwCheck == 1) {
            joinPW.setError("비밀번호의 길이는 8~16자이어야 합니다.");
            focusView = joinPW;
            cancel = true;
        } else if (pwCheck == 2) {
            joinPW.setError("비밀번호는 영문(소문자), 숫자를 포함해야 합니다.");
            focusView = joinPW;
            cancel = true;
        }

        // 아이디의 유효성 검사
        int idCheck = idIsValid(id);

        if (id.isEmpty()) {
            joinID.setError("아이디를 입력해주세요.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 1) {
            joinID.setError("아이디의 길이는 6~12자이어야 합니다.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 2) {
            joinID.setError("아이디는 영문(소문자), 숫자를 포함해야 합니다.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 3) {
            joinID.setError("아이디는 영문(소문자), 숫자만을 사용할 수 있습니다.");
            focusView = joinID;
            cancel = true;
        }

        service.IdCheck(new CheckIdData(id)).enqueue(new Callback<CheckIdResponse>() {
            @Override
            public void onResponse(Call<CheckIdResponse> call, Response<CheckIdResponse> response) {
                CheckIdResponse result = response.body();

                if (result.getCode() == 204) {
                    joinID.setError("중복된 아이디가 존재합니다.");
                    //focusView = joinID;
                    //cancel = true;
                }
            }
            @Override
            public void onFailure(Call<CheckIdResponse> call, Throwable t) {
                Toast.makeText(JoinActivity.this, "아이디 중복확인 에러", Toast.LENGTH_SHORT).show();
                Log.e("아이디 중복확인 에러", t.getMessage());
                return;
            }
        });


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

    private int idIsValid(String ID) {
        int smallLetter = 0;
        int number = 0;

        for (int i = 0; i < ID.length(); i++) {
            char ch = ID.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                smallLetter++;
            } else if (ch >= '0' && ch <= '9') {
                number++;
            }
        }

        int check = smallLetter + number;


        if ((ID.length() < 6) || (ID.length() > 12)) {
            return 1;
        } else if (smallLetter == 0 || number == 0) {
            return 2;
        } else if (check < ID.length()) {
            return 3;
        } else {
            return 0;
        }

    }

    private int pwIsValid(String PW) {
        int smallLetter = 0;
        int number = 0;

        for (int i = 0; i < PW.length(); i++) {
            char ch = PW.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                smallLetter++;
            } else if (ch >= '0' && ch <= '9') {
                number++;
            }
        }

        if ((PW.length() < 8) || (PW.length() > 16)) {
            return 1;
        } else if (smallLetter == 0 || number == 0) {
            return 2;
        } else {
            return 0;
        }
    }

    private int telIsValid(String tel) {
        int number = 0;

        for (int i = 0; i < tel.length(); i++) {
            char ch = tel.charAt(i);
            if (ch >= '0' && ch <= '9') {
                number++;
            }
        }

        if (tel.length() != 11) {
            return 1;
        } else if (number != 11) {
            return 2;
        } else if ((tel.charAt(0)!='0') && (tel.charAt(1)!='1') && (tel.charAt(2)!='0')) {
            return 3;
        } else {
            return 0;
        }
    }
}
