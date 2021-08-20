package kr.co.company.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String name = intent.getStringExtra("userName");
        String id = intent.getStringExtra("userId");

        Button mypage = (Button)findViewById(R.id.mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mv_mypage = new Intent(getApplicationContext(), MemberPageActivity.class);

                mv_mypage.putExtra("userName", name);
                mv_mypage.putExtra("userId", id);

                startActivity(mv_mypage);
            }
        });

        TextView mv_website = findViewById(R.id.mv_website);
        mv_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
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
    }
}