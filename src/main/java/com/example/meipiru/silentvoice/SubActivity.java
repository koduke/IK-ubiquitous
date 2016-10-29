package com.example.meipiru.silentvoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    

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

int num = 2;//ここの値によって表示する画像変更

        switch (num){
            case 1:
                ((ImageView) findViewById(R.id.sound)).setImageResource(R.drawable.ambulance);
                break;
            case 2:
                ((ImageView) findViewById(R.id.sound)).setImageResource(R.drawable.bycycle);
                break;
        }

    }
}
