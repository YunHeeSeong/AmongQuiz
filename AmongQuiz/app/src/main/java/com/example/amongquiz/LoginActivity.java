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
import android.widget.EditText;
import android.widget.Toast;

import com.example.amongquiz.R;

public class LoginActivity extends AppCompatActivity {

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME1 = "QUIZ";
    private static String TABLE_NAME2 = "ANSWER";
    private static String TABLE_NAME3 = "USER";
    private static String TABLE_NAME4 = "AVATAR";
    private static int DATABASE_VERSION = 1;
    private LoginActivity.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";

    EditText IDT, PWT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    //MainActivity 풀 스크린 기능 (상단바 제거)
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button join = (Button) findViewById(R.id.btn_join);
        IDT = (EditText) findViewById(R.id.edt_id);
        PWT = (EditText) findViewById(R.id.edt_password);

        Button.OnClickListener joinListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_join:
                        Intent join = new Intent(LoginActivity.this, joinActivity.class);
                        startActivityForResult(join, 1);
                        break;
                }
            }
        };

        join.setOnClickListener(joinListner);

        DATABASE_NAME = "quiz";
        boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, PopupExitActivity.class);
        startActivityForResult(intent, 1);
    }

    private boolean openDatabase() {
        dbHelper = new LoginActivity.DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private void executeRawQuery() {
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME3, null);
        c1.moveToNext();
        c1.close();
    }


    public void Login(View v) {


        String SQL05 = "select USER_ID "
                + " from " + TABLE_NAME3
                + " where USER_ID =?";

        String[] args05 = {IDT.getText().toString()};
        Cursor c05 = db.rawQuery(SQL05, args05);

        if (c05.getCount() != 1) {
            //아이디가 틀렸습니다.
            Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
        } else {
            /// Intent main = new Intent(this, MainActivity.class);
            ///  startActivity(main);

            c05.close();
            String SQL06 = "select USER_PWD "
                    + " from " + TABLE_NAME3
                    + " where USER_ID =?";

            String[] args06 = {IDT.getText().toString()};
            Cursor c06 = db.rawQuery(SQL06, args06);
            c06.moveToNext();



            if (PWT.getText().toString().equals(c06.getString(0))) {


                Toast.makeText(LoginActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                //인텐트 생성 및 호출

                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);

                //비밀번호가 틀렸습니다.

                // 자동 로그인 유지
                SaveSharedPreference.setUserName(LoginActivity.this, IDT.getText().toString());
                SaveSharedPreference.setUserPwd(LoginActivity.this, PWT.getText().toString());
                //Toast.makeText(LoginActivity.this, SaveSharedPreference.getUserName(this), Toast.LENGTH_SHORT).show();

            } else {
                //로그인성공
                Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginActivity.this,)

            }
            c06.close();
        }

    }




    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                String DROP_SQL = "drop table if exists " + TABLE_NAME3;
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }



            String CREATE_SQL = "create table " + TABLE_NAME2 + "("
                    + " ANSWER_ID integer PRIMARY KEY, "
                    + " EXAMPLE     text, "
                    + " CODE       integer,"
                    + " QUIZ_ID       integer,"
                    + " CORRECT    integer)";


            String CREATE_SQL2 ="create table " + TABLE_NAME1 + "("
                    + " QUIZ_ID integer PRIMARY KEY, "
                    + " ANSWER      text, "
                    + " LEVEL       text, "
                    + " KOREAN      text, "
                    + " ENGLISH     text, "
                    + "constraint fk_quiz_id foreign key(quiz_id) references answer(quiz_id))";

            String CREATE_SQL3 ="create table " + TABLE_NAME3 + "("
                    + " USER_ID         text PRIMARY KEY, "
                    + " USER_PWD        text, "
                    + " USER_NAME       text, "
                    + " USER_GOLD       integer, "
                    + " USER_RANK       text, "
                    + " AVATAR_ITEM_ID  text,"
                    + " HINT_COUNT      integer)";

            String CREATE_SQL4 ="create table " + TABLE_NAME4 + "("
                    + " AVATAR_ITEM_ID  text PRIMARY KEY, "
                    + " AVATAR_PRICE  integer, "
                    + " AVATAR_BUYCHECK  integer,"
                    + " AVATAR_IMAGE  integer, "
                    + " USER_ID  text,"
                    + "constraint fk_user_id foreign key(user_id) references answer(user_id))";

            try {

                db.execSQL(CREATE_SQL);
                db.execSQL(CREATE_SQL2);
                db.execSQL(CREATE_SQL3);
                db.execSQL(CREATE_SQL4);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            try {


                db.execSQL("insert into " + TABLE_NAME3 + "(USER_ID,USER_PWD,USER_NAME,USER_GOLD,USER_RANK,AVATAR_ITEM_ID,HINT_COUNT) values('root','1234','관리자',3,'GOLD','0',4);");

                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH )values(1, '거울'   ,'1','자기 자신을 비추는 도구',            'He looked at himself in the mirror');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH )values(2, '지도'   ,'1','길을 찾기 위해 사용하는 도구',       'Let me show you a map.');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(3 ,'영화'  ,'1','극장에서 상영되는 영상',             'When do you show the movie? ');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(4 ,'연필'  ,'1','가장 보편적으로 사용하는 나무로 만든 필기도구','She tried to write in pencil.');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(5 ,'입'    ,'1','음식을 먹거나 말을 하는 신체부위',     'He has a really bad breath.');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(6 ,'불'    ,'1','음식을 조리하거나 익힐때 필요한 것',   'make a fire');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(7 ,'사진'  ,'1','아빠가 찍어준 어릴적 OO',              'take a picture');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(8 ,'나무'  ,'1','집을 짓거나 가구를 만들때 쓰이는 식물','plant a tree');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(9 ,'상점'  ,'1','돈으로 물건을 사는곳',                 'a shoe shop');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(10,'종이'  ,'1','글을 쓰거나 그림을 그릴 수 있는 얇은 OO ','This is paper.');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(11,'지팡이','1','할아버지가 OOO 집고 뛰어가신다.',         'an alpenstock');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(12,'장난감','1','아이들이 가지고 노는 도구',               'play with toys');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(13,'여행'  ,'1','휴식을 목적으로 다른 지역, 외국에 가는일','take a trip');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(14,'형제'  ,'1','같은 부모에게서 난 아이',                 'We’re brothers.');");
                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(15,'주스'  ,'1','과실을 짜 낸 즙','orange juice');");

//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(16,'맹물','2','아무것도 첨가하지 않은 맛이 밍밍한 물', 'This isn t wine, it s dishwater!');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(17,'맹수','2','성질이 사나운 짐승으로 주로 사자나 호랑이를 말한다''He was saved from those savage beasts by a narrow margin.');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(18,'개명','2 ','이름을 변경하는 일 ','change one´s name from A to B ');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(19,'화음','2','두개 이상의 소리가 동시에 울려 어울리는 합성음 ',' to sing in harmony');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(20,'기사','2','신문이나 잡지 등에 실린 글','write an article');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(21,'부케','2','결혼식 때 신부가 손에 드는 작은 꽃다발','The little girl presented the princess with a large bouquet of flowers');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(22,'전사','2','전쟁에서 싸우다 죽은 것','The soldier fell in battle');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(23,'욕망','2','어떠한 혜택을 누리고자 간절하게 바라는 감정','suppress one s desire');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(24,'부전승','2','운동경기에서 추첨이나 상대편의 기권 따위로 경기를 치르지 않고 이기는 승','Our team drew a bye.');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(25,'무인도','2','사람이 살지 않는 섬','The island is uninhabited');");
//
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(26,'화법','3','글이나 말을 펼처 가는 말하는 방법 ','He is direct in his speech');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(27,'분양가','3','토지나 건물 따위를 나누어 파는 가격', 'calculate the apartment price');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(28,'가로수','3','거리의 미관을 위하여 길가에 줄지어 심어 놓은 나무를 말한다','There are trees planted along the road.');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(29,'대식가','3','함꺼번에 음식을 많이 먹는 사람',' I m not a glutton but a gourmet.');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(30,'인건비','3','사람을 쓰는 데 드는 비용',' cut down personnel expenses');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(31,'인지','3','어떠한 사실을 분명하게 인식하여 아는것','I perceived him going out with her.'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(32,'학자','3','학문에 능통한 또는 학문을 연국하는 사람', 'a distinguished  scholar');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(33,'면세품','3','세금이나 관세가 면제된 상품','Is this tax free?');  ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(34,'망신','3','말이나 행동을 잘못하여 자기의 지위,명예,체면 따위를 손상함', 'You are a disgrace[discredit] to our family.');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(35,'청와대','3','우리나라 대통령 관저','the Presidential residence'); ");
//
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(36,'김밥','4','김위에 밥을 펴 놓고 여러 가지 반찬을 올려 말아 싸서 먹는 음식','Can you tell me the recipe for gimbap?');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(37,'가석방','4','형기 만료 전에 수용자를 조건부로 석방하는 제도','He was released on parole.'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(38,'무도','4','무술과 무예 따위를 통틀어 이르는 말','chivalry, knighthood'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(39,'홍익인간','4','널리 인간을 이롭게 함 단군의 건국 이념','Humanitarianism is our nation's founding principle.'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(40,'신호등','4','도로에 설치하여 적색,녹색,황색 등으로 신호를 표현하는 점멸로','Don t run the red light.'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(41,'내빈석','4','모임에 공식적으로 초대받은 사람이 앉도록 마련해 놓은 자리','The ceremony was held with guests from all walks of life.'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(42,'현지','4','현재 사람이나 사물이 있는 그 곳','in-the-field'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(43,'일거양득','4','한 가지 일을 하여 두가지 이익을 얻음', 'killing two birds with one stone');");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(44,'급소','4','신체에 치명적인 약점이 될 수 있는 부위','attack a vital point'); ");
//                db.execSQL("insert into " + TABLE_NAME1 + "(QUIZ_ID, ANSWER, LEVEL,KOREAN ,ENGLISH ) values(45,'크래커','4','밀가루르 주재료로 하여, 설탕,버터,우유 들을 섞어 구운 비스킷 류의 과자','have crackers for snacks'); ");





                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(1, '가옥'     ,1    ,1,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(2, '고옥'     ,2    ,1,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(3, '기운'     ,3    ,1,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(4, '거울'     ,4    ,1,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(5, '지구'     ,1    ,2,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(6, '지략'     ,2    ,2,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(7, '지도'     ,3    ,2,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(8, '진도'     ,4    ,2,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(9, '연호'     ,1    ,3,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(10,'연장'     ,2    ,3,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(11,'영화'     ,3    ,3,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(12,'영희'     ,4    ,3,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(13,'연필'     ,1    ,4,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(14,'필통'     ,2    ,4,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(15,'면봉'     ,3    ,4,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(16,'지우개'   ,4    ,4,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(17,'집'       ,1    ,5,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(18,'입'       ,2    ,5,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(19,'맛술'     ,4    ,5,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(21,'파이어'   ,1    ,6,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(22,'불'       ,2    ,6,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(23,'전기'     ,3    ,6,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(24,'열'       ,4    ,6,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(25,'포즈'     ,1    ,7,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(26,'신사'     ,2    ,7,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(27,'상식'     ,3    ,7,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(28,'사진'     ,4    ,7,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(29,'나무'     ,1    ,8,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(30,'장작'     ,2    ,8,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(31,'젓가락'   ,3    ,8,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(32,'연못'     ,4    ,8,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(33,'식당'     ,1    ,9,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(34,'휴게소'   ,2    ,9,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(35,'사무소'   ,3    ,9,   0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(36,'상점'     ,4    ,9,   1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(37,'잡지'     ,1    ,10,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(38,'도서'     ,2    ,10,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(39,'종이'     ,3    ,10,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(40,'참고서'   ,4    ,10,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(41,'지팡이'   ,1    ,11,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(42,'집게'     ,2    ,11,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(43,'몽둥이'   ,3    ,11,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(44,'망치'     ,4    ,11,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(45,'상자'     ,1    ,12,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(46,'장난감'   ,2    ,12,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(47,'건담'     ,3    ,12,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(48,'피규어'   ,4    ,12,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(49,'일상'     ,1    ,13,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(50,'여행'     ,2    ,13,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(51,'일생'     ,3    ,13,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(52,'외출'     ,4    ,13,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(53,'향수'     ,1    ,14,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(54,'친척'     ,2    ,14,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(55,'형제'     ,3    ,14,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(56,'빅브라더' ,4    ,14,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(57,'주스'     ,1    ,15,  1  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(58,'죠스'     ,2    ,15,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(59,'장식'     ,3    ,15,  0  );");
                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(60,'장수'     ,4    ,15,  0  );");


//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(61, '맹물'     ,1    ,16,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(62, '냉무'     ,2    ,16,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(63, '치도리'    ,3    ,16,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(64, '팅커벨'    ,4    ,16,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(65, '뚜러뻥'    ,1    ,17,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(66, '로고'     ,2    ,17,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(67, '맹수'     ,3    ,17,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(68, '한조'     ,4    ,17,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(69, '해수면'     ,1    ,18,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(70,'개명'    ,2    ,18,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(71,'게릴라'    ,3    ,18,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(72,'마포대교'    ,4    ,18,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(73,'묻는다'    ,1    ,19,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(74,'더블'    ,2    ,19,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(75,'화음'    ,3    ,19,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(76,'간다'    ,4    ,19,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(77,'명품차'    ,1    ,20,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(78,'리치킹'    ,2    ,20,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(79,'버커킹'    ,3    ,20,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(80,'기사'    ,4    ,20,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(81,'부품'    ,1    ,21,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(82,'부분파괴'    ,2    ,21,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(83,'해설'    ,3    ,21,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(84,'부케'    ,4    ,21,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(85,'조국'    ,1    ,22,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(86,'마쩌둥'    ,2    ,22,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(87,'전사'    ,3    ,22,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(88,'핵피엔딩'    ,4    ,22,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(89,'드로우'    ,1    ,23,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(90,'함정카드'    ,2    ,23,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(91,'욕망'    ,3    ,23,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(92,'항아리'    ,4    ,23,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(93,'반칙'    ,1    ,24,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(94,'알파고'    ,2    ,24,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(95,'눈물'    ,3    ,24,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(96,'부전승'    ,4    ,24,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(97,'이인승'    ,1    ,25,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(98,'탑승'    ,2    ,25,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(99,'무인도'    ,3    ,25,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(100,'하얀섬'    ,4    ,25,  0  );");
//
//
//
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(101, '나선환'    ,1    ,26,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(102, '노오력'    ,2    ,26,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(103, '화법'    ,3    ,26,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(104, '체술'    ,4    ,26,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(105, '바람속성'    ,1    ,27,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(106, '흙먼지'    ,2    ,27,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(107, '미세먼지'    ,3    ,27,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(108, '분양가'    ,4    ,27,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(109, '해신'    ,1    ,28,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(110,'인어공주'    ,2    ,28,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(111,'가로수'    ,3    ,28,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(112,'낙성대'    ,4    ,28,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(113,'활활'    ,1    ,29,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(114,'커플지옥'    ,2    ,29,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(115,'음식점'    ,3    ,29,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(116,'대식가'    ,4    ,29,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(117,'인건비'    ,1    ,30,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(118,'극장'    ,2    ,30,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(119,'헬스장'    ,3    ,30,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(120,'모래사장'    ,4    ,30,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(121,'인지'    ,1    ,31,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(122,'능지'    ,2    ,31,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(123,'추리'    ,3    ,31,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(124,'명책'    ,4    ,31,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(125,'논문'    ,1    ,32,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(126,'학자'    ,2    ,32,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(127,'게임'    ,3    ,32,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(128,'토론'    ,4    ,32,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(129,'비행기'    ,1    ,33,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(130,'면세품'    ,2    ,33,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(131,'공항버스'    ,3    ,33,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(132,'여행카드'    ,4    ,33,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(133,'전기세'    ,1    ,34,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(134,'핸드북'    ,2    ,34,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(135,'칠판'    ,3    ,34,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(136,'망신'    ,4    ,34,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(137,'청화대'    ,1    ,35,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(138,'버스'    ,2    ,35,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(139,'수류탄'    ,3    ,35,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(140,'인질'    ,4    ,35,  0  );");
//
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(151, '김밥'    ,1    ,36,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(152, '천국'    ,2    ,36,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(153, '라면볶이'    ,3    ,36,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(154, '샌드위치'    ,4    ,36,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(155, '죠죠'    ,1    ,37,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(156, '가석방'    ,2    ,37,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(157, '권총'    ,3    ,37,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(158, '귀신'    ,4    ,37,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(159, '영춘권'    ,1    ,38,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(160,'엽문'    ,2    ,38,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(161,'무도'    ,3    ,38,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(162,'이소령'    ,4    ,38,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(163,'홍익인간'    ,1    ,39,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(164,'귀귀'    ,2    ,39,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(165,'열파참'    ,3    ,39,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(166,'검법'    ,4    ,39,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(167,'유로트럭'    ,1    ,40,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(168,'교통체중'    ,2    ,40,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(169,'신호등'    ,3    ,40,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(170,'택시'    ,4    ,40,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(171,'배트맨'    ,1    ,41,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(172,'조커'    ,2    ,41,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(173,'삐에로'    ,3    ,41,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(174,'내빈석'    ,4    ,41,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(175,'먹힌다'    ,1    ,42,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(176,'현지'    ,2    ,42,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(177,'푸드트럭'    ,3    ,42,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(178,'짬뽕'    ,4    ,42,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(179,'일거양득'    ,1    ,43,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(180,'가위'    ,2    ,43,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(181,'도박'    ,3    ,43,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(182,'파워볼'    ,4    ,43,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(183,'급소'    ,1    ,44,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(184,'인중'    ,2    ,44,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(185,'대머리'    ,3    ,44,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(186,'화살'    ,4    ,44,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(187,'말먹이'    ,1    ,45,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(188,'과자상자'    ,2    ,45,  0  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(189,'크래커'    ,3    ,45,  1  );");
//                db.execSQL("insert into " + TABLE_NAME2 + "(ANSWER_ID, EXAMPLE, CODE, QUIZ_ID, CORRECT) values(190,'탄수화물'    ,4    ,45,  0  );");









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

}
