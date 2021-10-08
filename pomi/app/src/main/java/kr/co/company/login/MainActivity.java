package kr.co.company.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.*;
import retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText inputID;
    private EditText inputPW;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputID = (EditText)findViewById(R.id.inputID);
        inputPW = (EditText)findViewById(R.id.inputPW);

        service = RetrofitClient.getClient().create(ServiceApi.class);


        Button joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }

        });

        Button findButton = (Button) findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });





        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                startActivity(intent);
                attemptLogin();
            }
        });



    }



    private void attemptLogin() {

        inputID.setError(null);
        inputPW.setError(null);

        String id = inputID.getText().toString();
        String passwd = inputPW.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (passwd.isEmpty()) {
            inputPW.setError("비밀번호를 입력해주세요.");
            focusView = inputPW;
            cancel = true;
        }

        // 아이디의 유효성 검사
        if (id.isEmpty()) {
            inputID.setError("아이디를 입력해주세요.");
            focusView = inputID;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin(new LoginData(id, passwd));
        }
    }

    private void startLogin(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();

                if(result.getCode() == 200) {
                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                    String name = result.getUserName();
                    String id = result.getUserId();

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("userName",name);
                    intent.putExtra("userId",id);

                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }


}