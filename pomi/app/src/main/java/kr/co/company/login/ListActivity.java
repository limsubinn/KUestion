package kr.co.company.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    private TextView mv_PORTMIS_site;
    private TextView busan1,busan2,ulsan, yeosu1, yeosu2, incheon1, incheon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        mv_PORTMIS_site = findViewById(R.id.mv_PORTMIS_site);
        mv_PORTMIS_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_portmis.class);
                startActivity(intent);
            }
        });

        busan1 = findViewById(R.id.busan1);
        busan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_busan1.class);
                startActivity(intent);
            }
        });
        busan2 = findViewById(R.id.busan2);
        busan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_busan2.class);
                startActivity(intent);
            }
        });
        ulsan = findViewById(R.id.ulsan);
        ulsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_ulsan.class);
                startActivity(intent);
            }
        });
        yeosu1 = findViewById(R.id.yeosu1);
        yeosu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_yeosu1.class);
                startActivity(intent);
            }
        });
        yeosu2 = findViewById(R.id.yeosu2);
        yeosu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_yeosu2.class);
                startActivity(intent);
            }
        });
        incheon1 = findViewById(R.id.incheon1);
        incheon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_incheon1.class);
                startActivity(intent);
            }
        });
        incheon2 = findViewById(R.id.incheon2);
        incheon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebView_incheon2.class);
                startActivity(intent);
            }
        });
    }


}