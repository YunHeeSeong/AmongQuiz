package com.example.amongquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.WindowManager;

import java.util.Random;
import android.widget.Toast;

import com.example.amongquiz.R;

public class Lv1Activity extends AppCompatActivity {

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    private TextView count;
    private int numcnt=1;
    private int qnum=2;
    private int jungnum;
    private int AllQstNo = 15; // DB에 들어간 전체 문제 수
    private int QstNo = 5; // 제출할 문제 수
    private int check = 0;
    public static final String TAG = "MainActivity";

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "QUIZ";
    private static String TABLE_NAME2 = "ANSWER";
    private static String TABLE_NAME3 = "USER";
    private static String TABLE_NAME4 = "AVATAR";
    private static int DATABASE_VERSION = 1;
    private Lv1Activity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static String LEVEL;
    private static String ANSWER;
    private static String HINTmg="";
    private static int ansCase=0;
    private static int naN=0;
    private String Hnum;
    private static int HCount=1;
    private static String GoldN;
    private static String Question;

    /*static String num;*/
    Random rnd = new Random();
    String num = String.valueOf(rnd.nextInt(10)+1);

    TextView txtResult, txtAns, txtCorrect,gold,txthint;
    ImageView pause, imgO, imgX, hint;
    RadioGroup rgAns;
    Animation fade;
    int[] arrQuest = new int[AllQstNo];
    String[] arrWorng = new String[QstNo];

    int solved = 0;
    int correct = 0;
    int hincount = 3;
    int worngFlag = 0; // 틀린 문제 답변 형식(주관식=0, 객관식=1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv1);
        Intent intent=getIntent();
        GoldN=intent.getStringExtra("gold");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gold=(TextView)findViewById(R.id.GOLD);
        rgAns = (RadioGroup) findViewById(R.id.rgAnswer);
        txtAns = (TextView) findViewById(R.id.txtAnswer);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        txtCorrect = (TextView) findViewById(R.id.textView5);
        txthint = (TextView)findViewById(R.id.txthint);
        count = (TextView) findViewById(R.id.textView3);
        txtResult = (TextView) findViewById(R.id.status);
        pause = (ImageView) findViewById(R.id.img_pause);
        imgO = (ImageView) findViewById(R.id.iv_O);
        imgX = (ImageView) findViewById(R.id.iv_X);
        hint=(ImageView) findViewById(R.id.img_hint);
        imgO.setVisibility(View.INVISIBLE);
        imgX.setVisibility(View.INVISIBLE);
        gold.setText(GoldN);

        for(int i=0; i<AllQstNo; i++)
        {
            arrQuest[i] = i+1;
        }

        for(int i=0; i<AllQstNo; i++)
        {
            Random ran = new Random();
            int n = ran.nextInt(AllQstNo);
            int temp;

            // 마지막 index와 나온 index swap하기
            temp = arrQuest[n];
            arrQuest[n] = arrQuest[AllQstNo-1];
            arrQuest[AllQstNo-1] = temp;
        }
        // 문제 코드를 랜덤으로 arrQuest 배열에 넣기


        ImageView.OnClickListener btnListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_pause:
                        Intent intent = new Intent(Lv1Activity.this, PopupActivity.class);
                        startActivityForResult(intent, 1);
//                        Toast.makeText(Lv1Activity.this,  GoldN, Toast.LENGTH_LONG).show();

                        break;

                    case R.id.img_hint:

                        if (hincount != 0){
                            executeRawQueryParam2();
                        }else{
                            Toast.makeText(Lv1Activity.this, "힌트를 다 사용 하셨습니다.", Toast.LENGTH_LONG).show();
                        }

                        break;
                }
            }
        };

        pause.setOnClickListener(btnListner);
        //일시 정지 버튼 기능
        hint.setOnClickListener(btnListner);

        showAnswerPanel();

        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Lv1Activity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }

    //랜덤으로 정답 패널을 출력하는 함수
    public void showAnswerPanel(){
        Random ran0 = new Random();
        ansCase = ran0.nextInt(2);

        if (ansCase == 0) { // 주관식일 때
            txtAns.setVisibility(View.VISIBLE);
            rgAns.setVisibility(View.INVISIBLE);
            radioButton1.setVisibility(View.INVISIBLE);
            radioButton2.setVisibility(View.INVISIBLE);
            radioButton3.setVisibility(View.INVISIBLE);
            radioButton4.setVisibility(View.INVISIBLE);
            worngFlag = 0;
        } else { // 객관식일 때
            txtAns.setVisibility(View.INVISIBLE);
            rgAns.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.VISIBLE);
            radioButton4.setVisibility(View.VISIBLE);
            worngFlag = 1;
        }
    }

    private boolean openDatabase() {
        dbHelper = new Lv1Activity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME1, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam() {
        Random rnd = new Random();
        String num = String.valueOf(rnd.nextInt(5)+1);
        int rndQst = rnd.nextInt(2);

        //퀴즈 문제 화면부분
        String SQL = "select * "
                + " from " + TABLE_NAME1
                + " where QUIZ_ID =?";
        String[] args= {String.valueOf(arrQuest[check])};
        Cursor c1 = db.rawQuery(SQL, args);
        int recordCount = c1.getCount();
        check++;

        for (int i = 1; i <= recordCount; i++) {
            c1.moveToNext();
            qnum = c1.getInt(0);

            if(rndQst==0)
                Question = c1.getString(3); //한글설명
            else
                Question = c1.getString(4); //영어설명

            /* int age = c1.getInt(1);*/
            LEVEL = c1.getString(2);
            ANSWER = c1.getString(1);
            println(Question);
            c1.close();
        }

        String SQL01 = "select * "
                + " from " + TABLE_NAME2
                + " where QUIZ_ID =?"
                + " AND CODE =?";
        String[] args01= {String.valueOf(qnum),"1"};
        Cursor c01 = db.rawQuery(SQL01, args01);
        int recordCount01 = c01.getCount();

        for (int a = 1; a <= recordCount01; a++) {
            c01.moveToNext();
            String  EXAMPLE = c01.getString(1);
            radioButton1.setText("1. "+EXAMPLE);
            c01.close();
        }

        String SQL02 = "select * "
                + " from " + TABLE_NAME2
                + " where QUIZ_ID =?"
                + " AND CODE =?";
        String[] args02= {String.valueOf(qnum),"2"};
        Cursor c02 = db.rawQuery(SQL02, args02);
        int recordCount02 = c02.getCount();

        for (int a = 1; a <= recordCount02; a++) {
            c02.moveToNext();
            String  EXAMPLE = c02.getString(1);
            radioButton2.setText("2. "+EXAMPLE);
            c02.close();
        }

        String SQL03 = "select * "
                + " from " + TABLE_NAME2
                + " where QUIZ_ID =?"
                + " AND CODE =?";
        String[] args03= {String.valueOf(qnum),"3"};
        Cursor c03 = db.rawQuery(SQL03, args03);
        int recordCount03 = c03.getCount();

        for (int a = 1; a <= recordCount03; a++) {
            c03.moveToNext();
            String  EXAMPLE = c03.getString(1);
            radioButton3.setText("3. "+EXAMPLE);
            c03.close();
        }

        String SQL04 = "select * "
                + " from " + TABLE_NAME2
                + " where QUIZ_ID =?"
                + " AND CODE =?";
        String[] args04= {String.valueOf(qnum),"4"};
        Cursor c04= db.rawQuery(SQL04, args04);
        int recordCount04 = c04.getCount();

        for (int a = 1; a <= recordCount04; a++) {
            c04.moveToNext();
            String  EXAMPLE = c04.getString(1);
            radioButton4.setText("4. "+EXAMPLE);
            c04.close();
        }
    }

    private void radioButton(){


        if( Integer.parseInt(Hnum)==1)
        {
            radioButton1.setVisibility(View.INVISIBLE);
        }
        else if (Integer.parseInt(Hnum)==2)
        {
            radioButton2.setVisibility(View.INVISIBLE);
        }
        else if (Integer.parseInt(Hnum)==3)
        {
            radioButton3.setVisibility(View.INVISIBLE);
        }
        else
        {
            radioButton4.setVisibility(View.INVISIBLE);
        }
    }


    private void executeRawQueryParam2() {
        if(HCount==1) {
            Random Hrnd = new Random();
            Hnum = String.valueOf(Hrnd.nextInt(3)+1);


            String SQL05 = "select HINT_COUNT "
                    + " from " + TABLE_NAME3
                    + " where USER_ID =?";

            String[] args05= {"root"};
            Cursor c05 = db.rawQuery(SQL05, args05);
            int recordCount05 = c05.getCount();


            for (int i = 1; i <= recordCount05; i++) {

                c05.moveToNext();
                String HINT = c05.getString(0);
                c05.close();

                if(ansCase==0){

                    //HINTmg=ANSWER.length();
                    HINTmg=String.valueOf(ANSWER.length());
                    //힌트 한개씩 줄어드는부분
                    // db.execSQL("UPDATE" + TABLE_NAME3 + "SET HINT_COUNT = "+String.valueOf(Integer.parseInt(HINT)-1)+" WHERE USER_ID= 1 ;");
                    // db.execSQL("UPDATE" + TABLE_NAME3 + "SET HINT_COUNT = "+String.valueOf(Integer.parseInt(HINT)-1) ;");
                    Toast.makeText(Lv1Activity.this,  "힌트: 정답의 글자수는"+HINTmg+"개 입니다!", Toast.LENGTH_LONG).show();
                    hincount--;
                    txthint.setText("남은 힌트 : "+ hincount);
                }
                else {


                    String SQL = "select * "
                            + " from " + TABLE_NAME2
                            + " where EXAMPLE =?";
                    String[] args= {ANSWER};
                    Cursor c1 = db.rawQuery(SQL, args);
                    int recordCount = c1.getCount();

                    for (int a = 1; a <= recordCount; a++) {
                        c1.moveToNext();
                        jungnum = c1.getInt(0);

                        switch (jungnum%4) {


                            case 0:
                                if (4 == Integer.parseInt(Hnum)) {
                                    radioButton3.setVisibility(View.INVISIBLE);
                                } else
                                    radioButton();
                                break;
                            case 1:
                                if (1 == Integer.parseInt(Hnum)) {
                                    radioButton2.setVisibility(View.INVISIBLE);
                                } else
                                    radioButton();
                                break;
                            case 2:
                                if (2 == Integer.parseInt(Hnum)) {
                                    radioButton3.setVisibility(View.INVISIBLE);
                                } else
                                    radioButton();
                                break;
                            case 3:
                                if (3 == Integer.parseInt(Hnum)) {
                                    radioButton4.setVisibility(View.INVISIBLE);
                                } else
                                    radioButton();
                                break;
                        }

                        c1.close();
                    }

                    Toast.makeText(Lv1Activity.this, "힌트사용으로 보기한개 제거완료!", Toast.LENGTH_LONG).show();
                    hincount--;
                    txthint.setText("남은 흰트 : "+ hincount);
                }

            }
            HCount=0;
        }

    }

    // 정답 확인 함수
    private void chCorrect(){
        int code = 0; // 선택한 radioButton
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // 선택한 답에 따른 변수 지정
        switch (rgAns.getCheckedRadioButtonId())
        {
            case R.id.radioButton1:
                code = 1; break;
            case R.id.radioButton2:
                code = 2; break;
            case R.id.radioButton3:
                code = 3; break;
            case R.id.radioButton4:
                code = 4; break;
            case -1:
                code = 0; break;
        }

        // 주관식 문제일 때
        if(code==0){
            String checkCor = "select EXAMPLE"
                    + " from " + TABLE_NAME2
                    + " where QUIZ_ID =?"
                    + " and CORRECT = 1";
            String[] argsCor= {String.valueOf(qnum)};
            Cursor cCor= db.rawQuery(checkCor, argsCor);
            cCor.moveToFirst();
            String Example = cCor.getString(0);

            if(Example.equals(txtAns.getText().toString().trim())) {
                correct++;
                imgO.setAnimation(fade);
            }else
            {
                arrWorng[solved] = Question+"/"+Example; // [문제/답] 형식으로 배열에 저장
                Log.i(TAG,arrWorng[solved]);
                imgX.setAnimation(fade);
            }
        }
        else
        { // 객관식 문제일 때
            String checkCor = "select EXAMPLE, CORRECT"
                    + " from " + TABLE_NAME2
                    + " where QUIZ_ID =?"
                    + " and CODE =?";
            String[] argsCor= {String.valueOf(qnum), String.valueOf(code)};
            Cursor cCor= db.rawQuery(checkCor, argsCor);
            cCor.moveToFirst();
            String Example = cCor.getString(0);
            int Correct = cCor.getInt(1);

            if(Correct == 1) {
                correct++;
                imgO.setAnimation(fade);
            }else
            {
                arrWorng[solved] = Question+"/"+Example; // [문제/답] 형식으로 배열에 저장
                Log.i(TAG,arrWorng[solved]);
                imgX.setAnimation(fade);
            }
        }
    }

    private void println(String msg) {
        Log.d(TAG, msg);
        txtResult.setText( msg);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            try {

            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }


            try {

            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            try {


            } catch (Exception ex) {
                Log.e(TAG, "Exception in insert SQL", ex);
            }
        }

        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

    public void setQuestion()
    {
        executeRawQuery();
        executeRawQueryParam();
        showAnswerPanel();
    }

    public void btclear(View view){

        HCount=1;
        chCorrect();
        if (solved < 4)
        {
            setQuestion();
            solved++;
            rgAns.clearCheck();
            txtAns.setText("");
            txtCorrect.setText("맞은 개수 : "+correct);

        }
        else
        {
            Intent intent = new Intent(this, ResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("score", correct);
            bundle.putStringArray("arrWorng", arrWorng);
            int worng = 0;
            for(int i = 0; i<5; i++)
            {
                if(arrWorng[i]!=null)
                    ++worng;
            }
            bundle.putInt("worng",worng);
            intent.putExtras(bundle);
            setResult(1,intent);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }
}
