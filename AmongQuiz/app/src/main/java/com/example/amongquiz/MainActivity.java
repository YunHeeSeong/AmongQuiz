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
import android.widget.TextView;
import android.widget.Toast;

import com.example.amongquiz.R;

public class MainActivity extends AppCompatActivity {
    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static int DATABASE_VERSION = 1;
    private MainActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";
    private static String U_ID;
    TextView mygold;
    String ggold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    //MainActivity 풀 스크린 기능 (상단바 제거)
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mygold=(TextView)findViewById(R.id.tvMyGold2);

        DATABASE_NAME = "quiz";

        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            executeRawQueryParam2();
            // executeRawQueryParam();

        }

        Toast.makeText(MainActivity.this, SaveSharedPreference.getUserName(this) , Toast.LENGTH_LONG ).show();
    }

    private boolean openDatabase() {
        dbHelper = new MainActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }


    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
    }


    public void btstart(View view) {
        startActivity(new Intent(getApplicationContext(), DefficultyActivity.class));
    }

    public void btStore(View view) {
        startActivity(new Intent(getApplicationContext(), StoreActivity.class));
    }

    public void btS(View view) {
        executeRawQueryParam2();

    }

    public void btPage(View view) {
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        intent.putExtra("num", "0");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
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
            String iid = c05.getString(0);
            String nname = c05.getString(2);
            ggold = c05.getString(3);

            c05.close();
            mygold.setText(ggold);
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
