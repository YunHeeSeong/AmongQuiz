package com.example.amongquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.amongquiz.R;

public class DefficultyActivity extends AppCompatActivity {
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";

    private static int DATABASE_VERSION = 1;
    private DefficultyActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String GoldN;
    TextView gold;

    Button btn_lv1;
    Button btn_lv2;
    Button btn_lv3;
    Button btn_lv4;
    Button btn_lv5;
    Button btn_easy;
    Button btn_nomal;
    Button btn_hard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defficulty);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gold=(TextView)findViewById(R.id.GOLD);
        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam2();
        }

        GoldN = gold.getText().toString();

        btn_lv1 = (Button)findViewById(R.id.btn_lv1);
        btn_lv2 = (Button)findViewById(R.id.btn_lv2);
        btn_lv3 = (Button)findViewById(R.id.btn_lv3);
        btn_lv4 = (Button)findViewById(R.id.btn_lv4);
        btn_lv5 = (Button)findViewById(R.id.btn_lv5);
        btn_easy = (Button)findViewById(R.id.btn_lvEasy);
        btn_nomal = (Button)findViewById(R.id.btn_lvNomal);
        btn_hard = (Button)findViewById(R.id.btn_lvHard);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DefficultyActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }

    public void back(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


    private boolean openDatabase() {
        dbHelper = new DefficultyActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
    }

    public void btback(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


    String Level = "레벨을 선택해주세요";

    public void LVSelect(View view){

        btn_lv1.setFocusableInTouchMode(true);
        btn_lv2.setFocusableInTouchMode(true);
        btn_lv3.setFocusableInTouchMode(true);
        btn_lv4.setFocusableInTouchMode(true);
        btn_lv5.setFocusableInTouchMode(true);
        btn_easy.setFocusableInTouchMode(true);
        btn_nomal.setFocusableInTouchMode(true);
        btn_hard.setFocusableInTouchMode(true);

            switch(view.getId()){
                case R.id.btn_lv1:
                    Level = "LV1";
                    btn_lv1.requestFocus();
                    break;
                case R.id.btn_lv2:
                    Level = "LV2";
                    btn_lv2.requestFocus();
                    break;
                case R.id.btn_lv3:
                    Level = "LV3";
                    btn_lv3.requestFocus();
                    break;
                case R.id.btn_lv4:
                    Level = "LV4";
                    btn_lv4.requestFocus();
                    break;
                case R.id.btn_lv5:
                    Level = "LV5";
                    btn_lv5.requestFocus();
                    break;
                case R.id.btn_lvEasy:
                    Level = "LVEasy";
                    btn_easy.requestFocus();
                    break;
                case R.id.btn_lvNomal:
                    Level = "LVNomal";
                    btn_nomal.requestFocus();
                    break;
                case R.id.btn_lvHard:
                    Level = "LVHard";
                    btn_hard.requestFocus();
                    break;
            }

    }   //Level 변수에 문자열 저장

    public void GameStart(View view){


        switch (Level){
            case "LV1":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LV2":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LV3":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LV4":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LV5":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LVEasy":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LVNomal":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
            case "LVHard":
                startActivity(new Intent(this, Lv1Activity.class).putExtra("gold",GoldN)); break;
        }
    }   //Level 변수에 저장된 문자열에 해당하는 단계로 화면 이동

    private void executeRawQueryParam2() {

        String SQL05 = "select * "
                + " from " + TABLE_NAME3
                + " where HINT_COUNT =?";
        String[] args05 = {"6"};
        Cursor c05 = db.rawQuery(SQL05, args05);
        int recordCount05 = c05.getCount();

        for (int i = 1; i <= recordCount05; i++) {
            c05.moveToNext();
            String iid = c05.getString(0);
            String nname = c05.getString(2);
            String ggold= c05.getString(3);
            c05.close();
            gold.setText(ggold);

        }



    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        }


        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

}
