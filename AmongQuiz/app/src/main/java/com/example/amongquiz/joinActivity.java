package com.example.amongquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2

import android.widget.ImageView;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.amongquiz.R;

public class joinActivity extends AppCompatActivity {
    private static String PW;
    private static String PW2;
    public static final String TAG = "MainActivity";
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static int DATABASE_VERSION = 1;
    private joinActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private EditText join_name;
    private EditText join_id;
    private EditText join_pw;
    EditText pw, pw2;

    private static String UID, UPW, UNAME;
    private static int UGOLD = 600;
    private static int HINT_C = 6;
    private static String URANK = "1";
    private static String AITEM = "1";
    private static int GOLD;
    private static int btn;


    ImageView hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        pw = (EditText) findViewById(R.id.edit_pwcheck);
        pw2 = (EditText) findViewById(R.id.edit_pw);
        join_name = (EditText) findViewById(R.id.edit_name);
        join_id = (EditText) findViewById(R.id.edit_id);
        join_pw = (EditText) findViewById(R.id.edit_pw);
        hint = (ImageView) findViewById(R.id.img_hint);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DATABASE_NAME = "quiz";

        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();


        }

    }

    private boolean openDatabase() {
        dbHelper = new joinActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam3() {

        try {
            db.execSQL("insert into " + TABLE_NAME3 + "(USER_ID,USER_PWD,USER_NAME,USER_GOLD,USER_RANK,AVATAR_ITEM_ID,HINT_COUNT) VALUES" + "(" + "'" + UID + "'" + "," + "'" + UPW + "'" + "," + "'" + UNAME + "'" + "," + UGOLD + "," + "'" + URANK + "'" + "," + "'" + AITEM + "'" + "," + HINT_C + ");");

        } catch (Exception ex) {
            Log.e(TAG, "Exception in insert SQL", ex);
        }
    }


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
            GOLD = c05.getInt(3);
            //확인구문 Toast.makeText(joinActivity.this, "입력한아이디:" + iid + "\n 입력한 이름:" + nname, Toast.LENGTH_LONG).show();
            c05.close();

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


    public void overlap(View v) {
        String SQL05 = "select USER_ID "
                + " from " + TABLE_NAME3
                + " where USER_ID =?";

        String[] args05 = {join_id.getText().toString()};
        Cursor c05 = db.rawQuery(SQL05, args05);

        if (c05.getCount() == 1) {
            //아이디가 틀렸습니다.
            Toast.makeText(joinActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(joinActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show();
            btn = 1;
        }
    }


    public void Go_login(View v) {
        if (btn == 1) {
            if (pw.getText().toString().length() == 0) {
                Toast.makeText(joinActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (!pw.getText().toString().equals(pw2.getText().toString())) {
                Toast.makeText(joinActivity.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                pw.setText("");
                pw2.setText("");
                pw.requestFocus();
            } else {
                UNAME = join_name.getText().toString();
                UID = join_id.getText().toString();
                UPW = join_pw.getText().toString();
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



                    executeRawQueryParam3();
                    executeRawQueryParam2();


                    Intent intent_join = new Intent(this, LoginActivity.class);
                    startActivity(intent_join);

            }
        }
        else{
            Toast.makeText(joinActivity.this, "아이디 중복확인을 눌러주세요", Toast.LENGTH_LONG).show();
        }
    }
}
