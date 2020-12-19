package com.example.amongquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amongquiz.R;

public class MyPageActivity extends AppCompatActivity {
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static int DATABASE_VERSION = 1;
    private MyPageActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String U_ID;

    EditText IDT,NMT, PWT;

    ImageView imgcat= null, imgdef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent intent = getIntent();

        imgcat = (ImageView)findViewById(R.id.img_avarta);
        imgdef = (ImageView)findViewById(R.id.img_avarta_def);

        String num = intent.getExtras().getString("num");
//        Log.d("로그","값 : "+num);
        if( num.equals("0"))
        {
            imgcat.setVisibility(View.INVISIBLE);
            imgdef.setVisibility(View.VISIBLE);
        }
        else
        {
            imgcat.setVisibility(View.VISIBLE);
            imgdef.setVisibility(View.INVISIBLE);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    //MainActivity 풀 스크린 기능 (상단바 제거)
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IDT = (EditText) findViewById(R.id.edt_id) ;
        NMT = (EditText) findViewById(R.id.edit_name);
        PWT = (EditText) findViewById(R.id.edit_password);


        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
        }

    }
    private boolean openDatabase() {
        dbHelper = new MyPageActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }
    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
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

    public void update()
    {

    }

    public void btupdate(View view) {
      // db.execSQL("UPDATE "+TABLE_NAME3+" SET USER_GOLD = "+600+" WHERE HINT_COUNT  = 6");
        //U_ID=(String)SaveSharedPreference.getUserName(this);

        //db.execSQL("UPDATE "+TABLE_NAME3+" SET USER_GOLD = "+600+" WHERE USER_ID = '"+ U_ID+"'");


        //Toast.makeText(MyPageActivity.this, "600골드로 초기화" , Toast.LENGTH_LONG).show();

        U_ID=(String)SaveSharedPreference.getUserName(this);
        String sql = "SELECT USER_NAME FROM "+TABLE_NAME3+" WHERE USER_ID = '"+ U_ID+"'";
        Cursor cursor = db.rawQuery(sql,null);

        Log.d("로그", "ID : " + U_ID);
        while(cursor.moveToNext()) {
            String name = cursor.getString(0);
            String pwd = cursor.getString(0);

            String NAME = NMT.getText().toString();
            String PWD = PWT.getText().toString();

            //db.execSQL("SELECT USER_GOLD FROM "+TABLE_NAME3+" WHERE USER_ID = '"+ U_ID+"'");
            db.execSQL("UPDATE " + TABLE_NAME3 + " SET USER_NAME = " + NAME + " WHERE USER_ID = '" + U_ID + "'");
            db.execSQL("UPDATE " + TABLE_NAME3 + " SET USER_PWD = " + PWD + " WHERE USER_ID = '" + U_ID + "'");

        }

        Log.d("로그","닉네임 : " + NMT.getText());
        Log.d("로그","닉네임 : " + PWT.getText());

        // db.execSQL("UPDATE "+TABLE_NAME3+" SET USER_NAME = "+NMT.getText().toString()+" WHERE USER_ID  = "+SaveSharedPreference.getUserName(this));
        Toast.makeText(this, "회원정보변경완료.", Toast.LENGTH_LONG).show();
    }


//    public void test(View view) {
//        //Toast.makeText(this, SaveSharedPreference.getUserName(this), Toast.LENGTH_LONG).show();
//
//
//
//        String SQL05 = "select * "
//                + " from " + TABLE_NAME3
//                + " where USER_ID =?";
//        String[] args05 = {"root"};
//        Cursor c05 = db.rawQuery(SQL05, args05);
//        int recordCount05 = c05.getCount();
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
//
//
//    }

    public void btback(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void btPoket(View view){
        Intent intent = new Intent(getApplicationContext(), PoketActivity.class);
        intent.putExtra("num", "1");
        startActivityForResult(intent,1);//액티비티 띄우기
    }

    public void  btLogout(View view){
        SaveSharedPreference.clearUserName(MyPageActivity.this);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyPageActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }
}
