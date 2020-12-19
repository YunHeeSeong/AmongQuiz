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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amongquiz.R;

public class PoketActivity extends AppCompatActivity {
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static int DATABASE_VERSION = 1;
    private PoketActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String U_ID;
    TextView Mygold, None, item, explan;
    int i = 0;
    ImageView imgpoket1 = null, imgpoket2 = null, imgpoket3 = null , Right = null, Left = null;
    Button check;
    String iid, nname, ggold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poket);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    //MainActivity 풀 스크린 기능 (상단바 제거)
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Mygold=(TextView)findViewById(R.id.txtgold);
        None = (TextView)findViewById(R.id.txtNone);
        Left = (ImageView)findViewById(R.id.imgleft);
        Right = (ImageView)findViewById(R.id.imgright);
        item = (TextView)findViewById(R.id.txtpoket1);
        explan = (TextView)findViewById(R.id.txtexplan1);

        imgpoket1 = (ImageView)findViewById(R.id.imgpoket1);
        imgpoket2 = (ImageView)findViewById(R.id.imgpoket2);
        imgpoket3 = (ImageView)findViewById(R.id.imgpoket3);
        check = (Button)findViewById(R.id.btnCheck);

        imgpoket1.setImageResource(R.drawable.cat);
        imgpoket2.setImageResource(R.drawable.hint);
        imgpoket3.setImageResource(R.drawable.giftbox);

        None.setVisibility(View.INVISIBLE);
        imgpoket1.setVisibility(View.INVISIBLE);
        imgpoket2.setVisibility(View.INVISIBLE);
        imgpoket3.setVisibility(View.INVISIBLE);
        Left.setVisibility(View.INVISIBLE);
        Right.setVisibility(View.INVISIBLE);
        item.setVisibility(View.INVISIBLE);
        explan.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);

        if (false)  //보유한 아이템이 없을 경우 true, 있으면 false
        {
            None.setVisibility(View.VISIBLE);
            imgpoket1.setVisibility(View.INVISIBLE);
            imgpoket2.setVisibility(View.INVISIBLE);
            imgpoket3.setVisibility(View.INVISIBLE);
            Left.setVisibility(View.INVISIBLE);
            Right.setVisibility(View.INVISIBLE);
            item.setVisibility(View.INVISIBLE);
            explan.setVisibility(View.INVISIBLE);
            check.setVisibility(View.INVISIBLE);
        }
        else    // 보유한 아이템이 있을 경우
            {
                Left.setVisibility(View.VISIBLE);
                Right.setVisibility(View.VISIBLE);
                imgpoket2.setVisibility(View.VISIBLE);
                item.setVisibility(View.VISIBLE);
                explan.setVisibility(View.VISIBLE);
                item.setText("힌트");
                explan.setText("문제가 어려울때 도움이 되어주는 아이템입니다! \n " +
                        "If you have a difficult quiz! \n I'll give you a hint.");

            None.setVisibility(View.INVISIBLE);
            check.setVisibility(View.INVISIBLE);

            Right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i==0){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.INVISIBLE);
                        imgpoket2.setVisibility(View.VISIBLE);
                        imgpoket1.setVisibility(View.INVISIBLE);

                        check.setVisibility(View.INVISIBLE);
                        item.setText("힌트");
                        explan.setText("문제가 어려울때 도움이 되어주는 아이템입니다! \n " +
                                "If you have a difficult quiz! \n I'll give you a hint.");

                        i = 1;
                    }
                    else if(i==1){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.INVISIBLE);
                        imgpoket2.setVisibility(View.INVISIBLE);
                        imgpoket1.setVisibility(View.VISIBLE);

                        check.setVisibility(View.VISIBLE);
                        check.setText("적용하기");
                        item.setText("사용자 아이콘");
                        explan.setText("Player icon");

                        check.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                                intent.putExtra("num", "1");
                                startActivityForResult(intent,1);//액티비티 띄우기
                            }
                        });

                        i = 2;
                    }else if(i==2){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.VISIBLE);
                        imgpoket2.setVisibility(View.INVISIBLE);
                        imgpoket1.setVisibility(View.INVISIBLE);

                        check.setVisibility(View.VISIBLE);
                        check.setText("열   기");
                        item.setText("랜덤 박스");
                        explan.setText("랜덤으로 아이템 또는 골드를 획득합니다. \n" +
                                "Randomly earn items or gold.");

                        i = 0;
                    }

                }
            });

            Left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i==1){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.VISIBLE);
                        imgpoket2.setVisibility(View.INVISIBLE);
                        imgpoket1.setVisibility(View.INVISIBLE);

                        check.setVisibility(View.VISIBLE);
                        check.setText("열   기");
                        item.setText("랜덤 박스");
                        explan.setText("랜덤으로 아이템 또는 골드를 획득합니다. \n" +
                                "Randomly earn items or gold.");

                        i = 0;
                    }else if (i==2){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.INVISIBLE);
                        imgpoket2.setVisibility(View.VISIBLE);
                        imgpoket1.setVisibility(View.INVISIBLE);

                        check.setVisibility(View.INVISIBLE);
                        item.setText("힌트");
                        explan.setText("문제가 어려울때 도움이 되어주는 아이템입니다! \n " +
                                "If you have a difficult quiz! \n I'll give you a hint.");

                        i = 1;
                    }else if (i==0){
                        Left.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                        imgpoket3.setVisibility(View.INVISIBLE);
                        imgpoket2.setVisibility(View.INVISIBLE);
                        imgpoket1.setVisibility(View.VISIBLE);

                        check.setVisibility(View.VISIBLE);
                        check.setText("적용하기");
                        item.setText("사용자 아이콘");
                        explan.setText("Player icon");

                        check.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                                intent.putExtra("num", "1");
                                startActivityForResult(intent,1);//액티비티 띄우기
                            }
                        });
                        i = 2;
                    }
                }
            });

        }

        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam2();
        }
    }

    public void btback(View view){
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        intent.putExtra("num", "0");
        startActivityForResult(intent,1);//액티비티 띄우기
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PoketActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }

    private boolean openDatabase() {
        dbHelper = new PoketActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam2() {
        U_ID=(String)SaveSharedPreference.getUserName(this);
        String SQL05 = "select * "
                + " from " + TABLE_NAME3
                + " where USER_ID =?";
        String[] args05 = {U_ID};
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
