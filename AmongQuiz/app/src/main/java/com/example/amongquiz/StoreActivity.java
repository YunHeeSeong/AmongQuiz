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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amongquiz.R;

public class StoreActivity extends AppCompatActivity {
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static int DATABASE_VERSION = 1;
    private StoreActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String GoldN;
    private static int itemG;
    private static String U_ID;
    TextView gold, Mygold, explain;
    int i = 0;
    ImageView imgHint = null, imgCat = null, imgGift=null;
    String iid, nname, ggold, nowgold;
    String itemPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    //MainActivity 풀 스크린 기능 (상단바 제거)
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Mygold=(TextView)findViewById(R.id.tvMyGold);
        gold=(TextView)findViewById(R.id.tvGold);
        explain = (TextView)findViewById(R.id.txt_exp);

        ImageView imgLeft = (ImageView)findViewById(R.id.imgLeft);
        ImageView imgRight = (ImageView)findViewById(R.id.imgRight);

        imgGift = (ImageView)findViewById(R.id.imgGift);
        imgCat = (ImageView)findViewById(R.id.imgCat);
        imgHint = (ImageView)findViewById(R.id.imgHint);
        imgCat.setImageResource(R.drawable.cat);
        imgHint.setImageResource(R.drawable.hint);
        imgGift.setImageResource(R.drawable.giftbox);

        imgHint.setVisibility(View.INVISIBLE);
        imgCat.setVisibility(View.INVISIBLE);
        imgGift.setVisibility(View.VISIBLE);
        explain.setText("다양한 아이템이 들어있는 랜덤 박스! \n Contains a variety of items!");


        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0){
                    imgGift.setVisibility(View.INVISIBLE);
                    imgHint.setVisibility(View.VISIBLE);
                    imgCat.setVisibility(View.INVISIBLE);
                    explain.setText("문제가 어려울때 도움이 되어주는 아이템! \n I'll give you a hint!");
                    i = 1;
                }
                else if(i==1){
                    imgGift.setVisibility(View.INVISIBLE);
                    imgHint.setVisibility(View.INVISIBLE);
                    imgCat.setVisibility(View.VISIBLE);
                    explain.setText("귀여운 아바타를 모아보세요! \n Gather up the cute characters!");
                    i = 2;
                }else if(i==2){
                    imgGift.setVisibility(View.VISIBLE);
                    imgHint.setVisibility(View.INVISIBLE);
                    imgCat.setVisibility(View.INVISIBLE);
                    explain.setText("다양한 아이템이 들어있는 랜덤 박스! \n Contains a variety of items!");
                    i = 0;
                }

            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==1){
                    imgGift.setVisibility(View.VISIBLE);
                    imgHint.setVisibility(View.INVISIBLE);
                    imgCat.setVisibility(View.INVISIBLE);
                    explain.setText("다양한 아이템이 들어있는 랜덤 박스! \n Contains a variety of items!");
                    i = 0;
                }else if (i==2){
                    imgGift.setVisibility(View.INVISIBLE);
                    imgHint.setVisibility(View.VISIBLE);
                    imgCat.setVisibility(View.INVISIBLE);
                    explain.setText("문제가 어려울때 도움이 되어주는 아이템! \n I'll give you a hint!");
                    i = 1;
                }else if (i==0){
                    imgGift.setVisibility(View.INVISIBLE);
                    imgHint.setVisibility(View.INVISIBLE);
                    imgCat.setVisibility(View.VISIBLE);
                    explain.setText("귀여운 아바타를 모아보세요! \n Gather up the cute characters!");
                    i = 2;
                }
            }
        });

        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam2();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StoreActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }

    private boolean openDatabase() {
        dbHelper = new StoreActivity.DatabaseHelper(this);
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

    public void buyItem(View view){
        int myG;
        U_ID=(String)SaveSharedPreference.getUserName(this);
        Log.d("로그","gold : "+gold.getText());
        // itemPay = (String) gold.getText();
        itemG = Integer.parseInt(gold.getText().toString());
        myG = Integer.parseInt(ggold);
        //     Log.d("로그","itemG : "+itemG+" myG : "+myG+" 결과 : "+Gold);

        if(myG >= itemG){
            myG = myG- itemG;
            db.execSQL("UPDATE "+TABLE_NAME3+" SET USER_GOLD = "+myG+" WHERE USER_ID  = '" + U_ID + "'");
            executeRawQueryParam2();
            Mygold.setText(String.valueOf(myG));
        }
        else{
            executeRawQueryParam2();
            Mygold.setText(String.valueOf(myG));
            Toast.makeText(this, "골드가 부족합니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void test(View view) {
        executeRawQueryParam0();

    }

    private void executeRawQueryParam0() {
        String SQL05 = "select * "
                + " from " + TABLE_NAME3
                + " where USER_ID =?";
        String[] args05 = {"root"};
        Cursor c05 = db.rawQuery(SQL05, args05);
        int recordCount05 = c05.getCount();
//
//        for (int i = 1; i <= recordCount05; i++) {
//            c05.moveToNext();
//            String iid = c05.getString(0);
//            String ppwd = c05.getString(1);
//            String nname = c05.getString(2);
//            String ggold= c05.getString(3);
//            c05.close();
//            Toast.makeText(this, "아이디는"+iid+"패스워드는"+ppwd+"네임은"+nname+"골드는"+ggold, Toast.LENGTH_LONG).show();
//        }


/*
        db.execSQL("UPDATE "+TABLE_NAME3+" SET USER_GOLD = "+600+" WHERE HINT_COUNT  = 6");

        Toast.makeText(StoreActivity.this, "600골드로 초기화" , Toast.LENGTH_LONG).show();
        executeRawQueryParam2();
        Mygold.setText(String.valueOf(600));*/
    }
    private void executeRawQueryParam2() {
        U_ID=(String)SaveSharedPreference.getUserName(this);
        String SQL05 = "select * "
                + " from " + TABLE_NAME3
                + " where USER_ID =?";
        String[] args05 = { U_ID };
        Cursor c05 = db.rawQuery(SQL05, args05);
        int recordCount05 = c05.getCount();

        for (int i = 1; i <= recordCount05; i++) {
            c05.moveToNext();
            iid = c05.getString(0);
            nname = c05.getString(2);
            ggold = c05.getString(3);
            //Toast.makeText(DefficultyActivity.this, "입력한아이디:" + iid + "\n 입력한 이름:" + nname, Toast.LENGTH_LONG).show();
            c05.close();
            Mygold.setText(ggold);

        }
        GoldN = Mygold.getText().toString();



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
