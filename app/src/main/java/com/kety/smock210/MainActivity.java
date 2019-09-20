package com.kety.smock210.ovnsicorrectqrfull;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startScan(View view) {
        Intent intent = new Intent(MainActivity.this, QrScaner.class);
        startActivity(intent);

    }

    public void createScan(View view) {
        Intent intent = new Intent(MainActivity.this, CreateQr.class);
        intent.putExtra("qr", "Здесь вы можете написать или отредактировать свой текст для QR кода");
        startActivity(intent);

    }
}
