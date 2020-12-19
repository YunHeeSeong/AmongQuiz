package com.example.amongquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amongquiz.R;

public class NoteActivity extends AppCompatActivity {

    ImageView pause2;
    TextView txtResult, txtAnswer;

    String[] arrWorng, note, PrintNote;
    int myWorng, worngNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        pause2 = (ImageView) findViewById(R.id.img_pause2);

        txtResult = (TextView) findViewById(R.id.quiz);
        txtAnswer = (TextView) findViewById(R.id.tvAns);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if( bundle != null )
        {
            arrWorng = bundle.getStringArray("arrWorng");
            myWorng = bundle.getInt("myWorng");
            note = new String[myWorng];
        }
        for(int i = 0; i<5; i++)
        {
            if(arrWorng[i]!=null)
            {
                note[worngNo] = arrWorng[i];
                ++worngNo;
            }
        }

        if(worngNo>0)
        {
            PrintNote = note[worngNo-1].split("/");
            txtResult.setText(PrintNote[0]);
            txtAnswer.setText("답 : "+PrintNote[1]);
            --worngNo;
        }

        ImageView.OnClickListener btnListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_pause2:
                        Intent intent = new Intent(NoteActivity.this, PopupActivity.class);
                        startActivityForResult(intent, 1);

                        break;
                }
            }
        };

        pause2.setOnClickListener(btnListner);

    } //onCreate end

    public void btclear(View view){

        if(worngNo>0)
        {
            PrintNote = note[worngNo-1].split("/");
            txtResult.setText(PrintNote[0]);
            txtAnswer.setText("답 : "+PrintNote[1]);
            --worngNo;
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }

}
