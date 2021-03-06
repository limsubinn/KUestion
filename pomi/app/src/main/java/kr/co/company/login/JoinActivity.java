package kr.co.company.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private ArrayAdapter arrayAdapter;

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

        String[] question = getResources().getStringArray(R.array.question);

        //ArryaAdapter ?????? ??????
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.activity_spinner,question);
        adapter.setDropDownViewResource(R.layout.activity_spinner);
        spinner.setAdapter(adapter);
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
            Toast.makeText(JoinActivity.this, "???????????? ?????? ????????? ??????????????????!", Toast.LENGTH_LONG).show();
            agree.setError("");
            focusView = agree;
            cancel = true;
        }

        //???????????? ????????? ?????? ????????? ??????
        if (hint_A.isEmpty()) {
            hint.setError("???????????? ????????? ?????? ?????? ??????????????????.");
            focusView = hint;
            cancel = true;
        }


        //??????????????? ????????? ??????
        int telCheck = telIsValid(tel);
        if (tel.isEmpty()) {
            phone.setError("??????????????? ??????????????????.");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 1) {
            phone.setError("???????????? ?????? ?????? ?????? ????????????!");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 2) {
            phone.setError("???????????? ???????????? ?????????.");
            focusView = phone;
            cancel = true;
        } else if (telCheck == 3) {
            phone.setError("010?????? ???????????? ?????????.");
            focusView = phone;
            cancel = true;
        }


        // ????????? ????????? ??????
        if (name.isEmpty()) {
            username.setError("????????? ??????????????????.");
            focusView = username;
            cancel = true;
        }


        //???????????? ??????
        if (passwd2.isEmpty()) {
            checkPW.setError("??????????????? ?????? ??????????????????.");
            focusView = checkPW;
            cancel = true;
        } else if (!passwd2.equals(passwd)) {
            checkPW.setError("???????????? ??????????????? ???????????? ????????????.");
            focusView = checkPW;
            cancel = true;
        }


        // ??????????????? ????????? ??????
        int pwCheck = pwIsValid(passwd);

        if (passwd.isEmpty()) {
            joinPW.setError("??????????????? ??????????????????.");
            focusView = joinPW;
            cancel = true;
        } else if (pwCheck == 1) {
            joinPW.setError("??????????????? ????????? 8~16???????????? ?????????.");
            focusView = joinPW;
            cancel = true;
        } else if (pwCheck == 2) {
            joinPW.setError("??????????????? ??????(?????????), ????????? ???????????? ?????????.");
            focusView = joinPW;
            cancel = true;
        }

        // ???????????? ????????? ??????
        int idCheck = idIsValid(id);

        if (id.isEmpty()) {
            joinID.setError("???????????? ??????????????????.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 1) {
            joinID.setError("???????????? ????????? 6~12???????????? ?????????.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 2) {
            joinID.setError("???????????? ??????(?????????), ????????? ???????????? ?????????.");
            focusView = joinID;
            cancel = true;
        } else if (idCheck == 3) {
            joinID.setError("???????????? ??????(?????????), ???????????? ????????? ??? ????????????.");
            focusView = joinID;
            cancel = true;
        }

        service.IdCheck(new CheckIdData(id)).enqueue(new Callback<CheckIdResponse>() {
            @Override
            public void onResponse(Call<CheckIdResponse> call, Response<CheckIdResponse> response) {
                CheckIdResponse result = response.body();

                if (result.getCode() == 204) {
                    joinID.setError("????????? ???????????? ???????????????.");
                    //focusView = joinID;
                    //cancel = true;
                }
            }
            @Override
            public void onFailure(Call<CheckIdResponse> call, Throwable t) {
                Toast.makeText(JoinActivity.this, "????????? ???????????? ??????", Toast.LENGTH_SHORT).show();
                Log.e("????????? ???????????? ??????", t.getMessage());
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
                Toast.makeText(JoinActivity.this, "???????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Log.e("???????????? ?????? ??????", t.getMessage());
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