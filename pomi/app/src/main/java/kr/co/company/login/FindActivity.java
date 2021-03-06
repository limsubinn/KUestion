package kr.co.company.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit.FindIdData;
import retrofit.FindIdResponse;
import retrofit.FindPwData;
import retrofit.FindPwResponse;
import retrofit.FindQData;
import retrofit.FindQResponse;
import retrofit.RetrofitClient;
import retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.DialogInterface.*;


public class FindActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;

    private EditText userID;
    private EditText username2;

    private ServiceApi service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        username = (EditText)findViewById(R.id.username);
        phone = (EditText)findViewById(R.id.phone);

        userID = (EditText)findViewById(R.id.userID);
        username2 = (EditText)findViewById(R.id.username2);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Button findID = (Button) findViewById(R.id.findID);
        findID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptFindId();
            }
        });

        Button findPW = (Button) findViewById(R.id.findPW);
        findPW.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptFindPw();
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

        if (tel.isEmpty()) {
            phone.setError("??????????????? ??????????????????.");
            focusView = phone;
            cancel = true;
        }

        if (name.isEmpty()) {
            username.setError("????????? ??????????????????.");
            focusView = username;
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
                    builder.setMessage(result.getMessage())
                            .setPositiveButton("??????", new OnClickListener() {
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
                Toast.makeText(FindActivity.this, "????????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Log.e("????????? ?????? ?????? ??????", t.getMessage());
            }
        });
    }

    private void attemptFindPw() {

        userID.setError(null);
        username2.setError(null);

        String name = username2.getText().toString();
        String id = userID.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (name.isEmpty()) {
            username2.setError("????????? ??????????????????.");
            focusView = username2;
            cancel = true;
        }

        if (id.isEmpty()) {
            userID.setError("???????????? ??????????????????.");
            focusView = userID;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startFindQ(new FindQData(id, name));
        }
    }

    private void startFindQ(FindQData data) {
        service.QFind(data).enqueue(new Callback<FindQResponse>() {

            @Override
            public void onResponse(Call<FindQResponse> call, Response<FindQResponse> response) {
                FindQResponse result = response.body();

                if (result.getCode() == 200) {
                    LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    RelativeLayout dialogLayout = (RelativeLayout) li.inflate(R.layout.activity_dialog, null);

                    final TextView hint = (TextView)dialogLayout.findViewById(R.id.hint);
                    final EditText answer = (EditText)dialogLayout.findViewById(R.id.answer);

                    hint.setText(result.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
                    builder.setTitle("???????????? ??????");
                    builder.setPositiveButton("??????", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String hint_A = answer.getText().toString();
                            startFindPw(new FindPwData(hint_A));
                        }
                    });
                    builder.setView(dialogLayout);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(FindActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<FindQResponse> call, Throwable t) {
                Toast.makeText(FindActivity.this, "???????????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Log.e("???????????? ?????? ?????? ??????", t.getMessage());
            }
        });
    }

    private void startFindPw(FindPwData data) {
        service.PwFind(data).enqueue(new Callback<FindPwResponse>() {

            @Override
            public void onResponse(Call<FindPwResponse> call, Response<FindPwResponse> response) {
                FindPwResponse result = response.body();

                if (result.getCode() == 200) {
                    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FindActivity.this);

                    builder.setMessage(result.getMessage())
                            .setPositiveButton("??????", new OnClickListener() {
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
            public void onFailure(Call<FindPwResponse> call, Throwable t) {
                Toast.makeText(FindActivity.this, "????????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Log.e("????????? ?????? ?????? ??????", t.getMessage());
            }
        });
    }
}