package kr.co.company.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit.DeleteData;
import retrofit.DeleteResponse;
import retrofit.RetrofitClient;
import retrofit.ServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberPageActivity extends AppCompatActivity {

    private TextView tv_id, tv_name;
    private ServiceApi service;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Intent intent = getIntent(); // 실려온 인텐트 정보를 담음
        String name = intent.getStringExtra("userName");
        String id = intent.getStringExtra("userId");

        tv_id = findViewById(R.id.user_id);
        tv_id.setText(id);

        tv_name = findViewById(R.id.user_name);
        tv_name.setText(name);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Button deleteMemberButton = (Button) findViewById(R.id.deleteMemberButton);
        deleteMemberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String input_id = id;
                EditText editText = new EditText(MemberPageActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberPageActivity.this);
                builder.setTitle("정말로 탈퇴하시겠습니까? \n" +
                        "비밀번호를 입력해주세요.")
                        .setView(editText);



                builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String input_pw = editText.getText().toString();
                        System.out.println(input_pw);//잘 들어옴

                        //데이터베이스의 passwd 비교
                        service.userDelete(new DeleteData(input_pw, input_id )).enqueue(new Callback<DeleteResponse>() {

                            @Override
                            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                                DeleteResponse result = response.body();
                                System.out.println(result.getCode()); // 잘 들어옴

                                if (result.getCode() == 200) {
                                    Toast.makeText(MemberPageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MemberPageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                                Toast.makeText(MemberPageActivity.this, "탈퇴 에러", Toast.LENGTH_SHORT).show();
                                Log.e("탈퇴 에러", t.getMessage());
                                //탈퇴 에러시 그대로..
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                        });



                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "회원탈퇴를 취소하셨습니다.", Toast.LENGTH_LONG).show();

                    }
                });
                builder.show();

//
//                AlertDialog alertDialog = builder.create();
//
//                alertDialog.show();
            }

        });
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MemberPageActivity.this, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }


        });



    }


}
