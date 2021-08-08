package kr.co.company.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit.FindIdData;
import retrofit.FindIdResponse;
import retrofit.RetrofitClient;
import retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private ServiceApi service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        username = (EditText)findViewById(R.id.username);
        phone = (EditText)findViewById(R.id.phone);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Button findID = (Button) findViewById(R.id.findID);
        findID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                startActivity(intent);
                attemptFindId();
            }
        });
    }

    private void attemptFindId() {

        username.setError(null);
        phone.setError(null);

        String name = username.getText().toString();
        String tel = phone.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

        if (cancel) {
            focusView.requestFocus();
        } else {
            startFindId(new FindIdData(name, tel));
        }
    }

    private void startFindId(FindIdData data) {
        service.IdFind(data).enqueue(new Callback<FindIdResponse>() {

            @Override
            public void onResponse(Call<FindIdResponse> call, Response<FindIdResponse> response) {
                FindIdResponse result = response.body();

                if (result.getCode() == 200) {
                    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FindActivity.this);

                    //builder.setTitle("인사말").setMessage("반갑습니다");
                    builder.setMessage(result.getMessage())
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
                else {
                    Toast.makeText(FindActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<FindIdResponse> call, Throwable t) {
                Toast.makeText(FindActivity.this, "아이디 찾기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("아이디 찾기 에러 발생", t.getMessage());
            }
        });
    }


/*
    public void showDialog(View view) {
        final EditText answer = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 찾기");
        builder.setMessage("비밀번호 질문");
        builder.setMessage("비밀번호 답변");
        builder.setView(answer);
        builder.setPositiveButton("확", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

*/
}