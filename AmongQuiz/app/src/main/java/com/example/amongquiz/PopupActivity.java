package com.example.amongquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.amongquiz.R;

public class PopupActivity extends AppCompatActivity {

    Button btn_replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        //원래 액티비티로 돌아가기
        btn_replay = (Button)findViewById(R.id.btn_replay);

        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void restart(View v){
        Intent intent_restart = new Intent(this, DefficultyActivity.class);
        startActivity(intent_restart);
    }

    public void Go_main(View v){
        Intent intent_main = new Intent(this, MainActivity.class);
        startActivity(intent_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}
