package com.example.myweather;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {
    private TextView wendu,tishi,fengxiang,fengli,high,type,low,date;
    MyDatabaseHelper dbmemo;
    private String tianqi[];
    Button bn;
    TextView textView;
    TextView city1;

    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==10)
            {
                String content=(String) msg.obj;
                try {
                    tianqi=today(content);
                    wendu.setText("当前温度： \t"+tianqi[0]);
                    tishi.setText("温馨提示： \t"+tianqi[1]);
                    fengxiang.setText("风向： \t"+tianqi[2]);
                    fengli.setText("风力： \t"+tianqi[3].substring(10,12));
                    high.setText("最"+tianqi[4]);
                    type.setText("天气类型： \t"+tianqi[5]);
                    low.setText("最"+tianqi[6]);
                    date.setText("日期： \t"+tianqi[7]);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    };
    private Handler handler1=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==10)
            {
                String content=(String) msg.obj;
                try {
                    tianqi=today1(content);
                    wendu.setText("当前温度： \t"+tianqi[0]);
                    tishi.setText("温馨提示： \t"+tianqi[1]);
                    fengxiang.setText("风向： \t"+tianqi[2]);
                    fengli.setText("风力： \t"+tianqi[3].substring(10,12));
                    high.setText("最"+tianqi[4]);
                    type.setText("天气类型： \t"+tianqi[5]);
                    low.setText("最"+tianqi[6]);
                    date.setText("日期： \t"+tianqi[7]);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    };



    protected String[] today(String content) throws Exception {
        // TODO Auto-generated method stub
        JSONObject obj=new JSONObject(content);
        JSONObject data = obj.getJSONObject("data");
        String wendu = data.getString("wendu");
        String tishi=data.getString("ganmao");
        JSONArray forecast = data.getJSONArray("forecast");
        JSONObject obj2=(JSONObject) forecast.get(0);
        String fengxiang=obj2.getString("fengxiang");
        String fengli=obj2.getString("fengli");
        String high=obj2.getString("high");
        String type=obj2.getString("type");
        String low=obj2.getString("low");
        String date=obj2.getString("date");
        String tianqi[]={wendu,tishi,fengxiang,fengli,high,type,low,date};
        return tianqi;
    }
    protected String[] today1(String content) throws Exception {
        // TODO Auto-generated method stub
        JSONObject obj=new JSONObject(content);
        JSONObject data = obj.getJSONObject("data");
        String wendu = data.getString("wendu");
        String tishi=data.getString("ganmao");
        JSONArray forecast = data.getJSONArray("forecast");
        JSONObject obj2=(JSONObject) forecast.get(1);
        String fengxiang=obj2.getString("fengxiang");
        String fengli=obj2.getString("fengli");
        String high=obj2.getString("high");
        String type=obj2.getString("type");
        String low=obj2.getString("low");
        String date=obj2.getString("date");
        String tianqi[]={wendu,tishi,fengxiang,fengli,high,type,low,date};
        return tianqi;
    }
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city1=(TextView)findViewById(R.id.chengshi) ;
        Intent intent = getIntent();
        final String city2 = intent.getStringExtra("city");
        city1.setText(city2);
        dbmemo=new MyDatabaseHelper(this,"love.db",null,1);
        initViews();
        bn=(Button)findViewById(R.id.first);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    myclick(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bn=(Button)findViewById(R.id.second);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    myclick2(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bn=(Button)findViewById(R.id.cllege);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=city2;
                String[] where={city2};

                SQLiteDatabase db = dbmemo.getWritableDatabase();
                // Cursor cursor = db.query("WordTable",null,null,null,null,null,null);
                Cursor cursor =  db.query("love",new String[]{"city"},"city=?",where,null,null,null);

                if(cursor.moveToFirst()){
                    Toast.makeText(MainActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    cursor.close();

                }else{

                    ContentValues values=new ContentValues();
                    values.put("city",a);
                    db.insert("love",null,values);
                    Toast.makeText(MainActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();}

            }
        });
        bn=(Button)findViewById(R.id.back);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Weather.class);
                startActivity(intent);
            }
        });


    }
    private String getCity() {
        // TODO Auto-generated method stub
        String city=city1.getText().toString().trim();
        return city;
    }
    private void initViews() {
        // TODO Auto-generated method stub

        wendu =(TextView) findViewById(R.id.wendu);
        tishi =(TextView) findViewById(R.id.tishi);
        fengxiang =(TextView) findViewById(R.id.fengxiang);
        fengli =(TextView) findViewById(R.id.fengli);
        high =(TextView) findViewById(R.id.high);
        type =(TextView) findViewById(R.id.type);
        low=(TextView) findViewById(R.id.low);
        date =(TextView) findViewById(R.id.date);

    }

    public void myclick(View v) throws Exception
    {;
        Request request;
        OkHttpClient client= new OkHttpClient();
        Request.Builder builder=new Request.Builder();
        builder.get();
        builder.url("http://wthrcdn.etouch.cn/weather_mini?city="+ URLEncoder.encode(getCity(), "UTF-8"));
        request = builder.build();
        Call call=client.newCall(request);

        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                Message msg=Message.obtain();
                msg.obj=content;
                msg.what=10;
                handler.sendMessage(msg);
            }
        });
    }
    public void myclick2(View v) throws Exception
    {;
        Request request;
        OkHttpClient client= new OkHttpClient();
        Request.Builder builder=new Request.Builder();
        builder.get();
        builder.url("http://wthrcdn.etouch.cn/weather_mini?city="+ URLEncoder.encode(getCity(), "UTF-8"));
        request = builder.build();
        Call call=client.newCall(request);

        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                Message msg=Message.obtain();
                msg.obj=content;
                msg.what=10;
                handler1.sendMessage(msg);
            }
        });
    }
    class MyDatabaseHelper extends SQLiteOpenHelper {
        public static final String CREATE_LOVE = "create table love ("
                + "city text" + ")";

        private Context mContext;

        public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);

            mContext = context;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LOVE);
            Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
