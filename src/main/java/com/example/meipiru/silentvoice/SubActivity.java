package com.example.meipiru.silentvoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    public void b1(View v) {
        // 画像の変更 （b1画像を設定）
        ((ImageView) findViewById(R.id.sound)).setImageResource(R.drawable.ambulance);
    }

    public void b2(View v) {
        // 画像の変更 （b1画像を設定）
        ((ImageView) findViewById(R.id.sound)).setImageResource(R.drawable.bycycle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button returnButton = (Button) findViewById(R.id.finish);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
