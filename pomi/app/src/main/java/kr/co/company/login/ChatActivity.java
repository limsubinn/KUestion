package kr.co.company.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import kr.co.company.login.adapters.ChatAdapter;
import kr.co.company.login.helpers.SendMessageInBg;
import kr.co.company.login.interfaces.BotReply;
import retrofit.DeleteData;
import retrofit.DeleteResponse;
import retrofit.MessageData;
import retrofit.RetrofitClient;
import retrofit.ServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements BotReply {
    Toolbar toolbar;
    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<MessageData> messageList = new ArrayList<>();
    EditText editMessage;
    ImageButton btnSend;
    Button backButton;
    ImageButton helpbtn;
    TextView tv_name;
    ServiceApi service;

    DrawerLayout drawerLayout;
    View drawerView;

    //dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "chatactivity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chatView);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);
        backButton = findViewById(R.id.backButton);
        helpbtn = findViewById(R.id.helpButton);


        chatAdapter = new ChatAdapter(messageList, this);
        chatView.setAdapter(chatAdapter);

        messageList.add(new MessageData("???????????????. ?????? ???????????? ???????????????.\n ????????? ?????? ????????? ?????? ??????????????????.\n( ex. ??????, ?????? )", true));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                System.out.println(message);
                if (!message.isEmpty()) {
                    messageList.add(new MessageData(message, false));
                    editMessage.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.addDrawerListener(listener);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        helpbtn = findViewById(R.id.helpButton);
        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("?????? ?????? Tip! \n")
                        .setMessage("1. ?????? \n" +
                                "  -?????? \n" +
                                "  -?????? \n" +
                                "ex) ?????? ?????????, ?????? ????????? \n" +
                                "2. ???????????? ??????(FAQ) \n" +
                                "ex) ???????????? ?????????, ???????????? ?????? ??? \n")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {  }
                        });

                builder.show();
            }
        });

        TextView call_portmis =findViewById(R.id.call_portmis);
        call_portmis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001172"));
                startActivity(intent);
            }
        });

        TextView mv_website = findViewById(R.id.mv_website);
        mv_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });;


        Intent intent = getIntent(); // ????????? ????????? ????????? ??????
        String name = intent.getStringExtra("userName");
        String id = intent.getStringExtra("userId");

        tv_name = findViewById(R.id.user_name);
        tv_name.setText(name);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Button deleteMemberButton = (Button) findViewById(R.id.deleteMember);
        deleteMemberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String input_id = id;
                EditText editText = new EditText(ChatActivity.this);
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("????????? ????????????????????????? \n" +
                        "??????????????? ??????????????????.")
                        .setView(editText);



                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String input_pw = editText.getText().toString();

                        //????????????????????? passwd ??????
                        service.userDelete(new DeleteData(input_pw, input_id )).enqueue(new Callback<DeleteResponse>() {

                            @Override
                            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                                DeleteResponse result = response.body();

                                if (result.getCode() == 200) {
                                    Toast.makeText(ChatActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ChatActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    //startActivity(intent);
                                } else {
                                    Toast.makeText(ChatActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                                Toast.makeText(ChatActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                                Log.e("?????? ??????", t.getMessage());

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                        });



                    }
                });

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "??????????????? ?????????????????????", Toast.LENGTH_LONG).show();

                    }
                });
                builder.show();

            }

        });
        Button logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "???????????? ???????????????", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ChatActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }

        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("ko-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                messageList.add(new MessageData(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }

}