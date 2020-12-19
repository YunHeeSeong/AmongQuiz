package com.example.amongquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amongquiz.R;

public class PopupExitActivity extends AppCompatActivity {

    Button btn_OK,btn_cancle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_exit);

        //어플 종료시키기
        btn_OK = (Button)findViewById(R.id.btn_Ok);

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        //원래 액티비티로 돌아가기
        btn_cancle2 = (Button)findViewById(R.id.btn_cancel2);

        btn_cancle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
