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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amongquiz.R;


public class ResultActivity extends AppCompatActivity {

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME3 = "USER";
    private static String USER_GOLD = "USER_GOLD";
    private static int DATABASE_VERSION = 1;
    private static int LEVEL = 1;
    public static final String TAG = "MainActivity";
    private ResultActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static String U_ID;

    int score, myScore = 0;
    int worng, myWorng;
    Button check,note;
    String[] arrWorng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if( bundle != null )
        {
            myScore = bundle.getInt("score", score);
            arrWorng = bundle.getStringArray("arrWorng");
            myWorng = bundle.getInt("worng", worng);
        }

        TextView tvScore = (TextView) findViewById(R.id.textView4);
        tvScore.setText(myScore*20+"점");

        ImageView ivScore = (ImageView) findViewById(R.id.imageView5);

        if(myScore>3)
            ivScore.setImageResource(R.drawable.star_icon3);
        else if(myScore>1)
            ivScore.setImageResource(R.drawable.star_icon2);
        else if(myScore>=0)
            ivScore.setImageResource(R.drawable.star_icon1);

        TextView tvGainGold = (TextView) findViewById(R.id.tvGainGold);
        tvGainGold.setText(myScore*10+" Gold 획득!");


        check = (Button)findViewById(R.id.btn_ok);
        note = (Button)findViewById(R.id.btn_note);

        Button.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btn_ok:

                        Intent intent = new Intent(ResultActivity.this, PopupNoteActivity.class);
                        startActivityForResult(intent, 1);
                        break;

                    case  R.id.btn_note:
                        if(myWorng==0)
                            Toast.makeText(ResultActivity.this, "틀린 문제가 없습니다.", Toast.LENGTH_LONG).show();
                        else{
                            Intent intent1 = new Intent(ResultActivity.this, NoteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("arrWorng", arrWorng);
                            bundle.putInt("myWorng", myWorng);
                            intent1.putExtras(bundle);
                            startActivityForResult(intent1, 1);
                        }
                        break;
                }
            }
        };

        check.setOnClickListener(btnListener);
        note.setOnClickListener(btnListener);



        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            update();
        }
    }

    private boolean openDatabase() {
        dbHelper = new ResultActivity.DatabaseHelper(this);
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
        U_ID=(String)SaveSharedPreference.getUserName(this);
        String sql = "SELECT USER_GOLD FROM "+TABLE_NAME3+" WHERE USER_ID = '"+ U_ID+"'";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()) {
            int oldgold = cursor.getInt(0);

            int sum = oldgold + myScore * 10;

            //db.execSQL("SELECT USER_GOLD FROM "+TABLE_NAME3+" WHERE USER_ID = '"+ U_ID+"'");
            db.execSQL("UPDATE " + TABLE_NAME3 + " SET USER_GOLD = " + sum + " WHERE USER_ID = '" + U_ID + "'");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }
}