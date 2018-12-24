package com.example.myweather;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import java.util.ArrayList;

import okhttp3.Call;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class Weather extends AppCompatActivity {
    private EditText city1;
    MyDatabaseHelper dbmemo;
    Button bn;
    EditText textView;
    ListView mListView;
    ArrayList<String> date1=new ArrayList<>();
    int count=0;
    private String getCity() {
        String city=city1.getText().toString().trim();
        return city;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        dbmemo=new MyDatabaseHelper(this,"love.db",null,1);
        mListView=(ListView) findViewById(R.id.listview);
        SQLiteDatabase db=dbmemo.getWritableDatabase();
        Cursor cursor=db.query("love",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String author=cursor.getString(cursor.getColumnIndex("city"));
                date1.add(count++,author);
            }while (cursor.moveToNext());
            cursor.close();
            //oast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Weather.this,R.layout.support_simple_spinner_dropdown_item,date1);
        ListView listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=date1.get(position);
                Intent intent=new Intent(Weather.this,MainActivity.class);
                intent.putExtra("city",s);
                //intent.putExtra("city","上海");
                startActivity(intent);
            }
        });
        ItemOnLongClick1();
        textView=(EditText) findViewById(R.id.city) ;
        dbmemo=new MyDatabaseHelper(this,"love.db",null,1);
        dbmemo.getWritableDatabase();
        bn=(Button)findViewById(R.id.see);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=textView.getText().toString();
                Intent intent=new Intent(Weather.this,MainActivity.class);
                intent.putExtra("city",a);
                startActivity(intent);

            }
        });
        bn=(Button)findViewById(R.id.college);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=textView.getText().toString();
                String[] where={a};
                //String a="河北";
                //String[] where={"河北"};
                SQLiteDatabase db = dbmemo.getWritableDatabase();
                // Cursor cursor = db.query("WordTable",null,null,null,null,null,null);
                Cursor cursor =  db.query("love",new String[]{"city"},"city=?",where,null,null,null);
                if(cursor.moveToFirst()){
                    Toast.makeText(Weather.this,"已收藏",Toast.LENGTH_SHORT).show();
                    cursor.close();
                }else{
                    ContentValues values=new ContentValues();
                    values.put("city",a);
                    db.insert("love",null,values);
                    date1.add(count++,a);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(Weather.this,R.layout.support_simple_spinner_dropdown_item,date1);
                    ListView listView=(ListView)findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                    Toast.makeText(Weather.this,"收藏成功",Toast.LENGTH_SHORT).show();}
            }
        });

    }
    private void ItemOnLongClick1() {
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
                menu.add(0, 1, 0, "上升");
                menu.add(0, 2, 0, "置顶");

            }
        });
    }
    public boolean onContextItemSelected(MenuItem item) {
        dbmemo=new MyDatabaseHelper(this,"love.db",null,1);
        SQLiteDatabase db=dbmemo.getWritableDatabase();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int MID = (int) info.id;

        switch (item.getItemId()) {
            case 0:
                String[] where={date1.get(MID)};
                db.delete("love","city=?",where);
                date1.remove(MID);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Weather.this,R.layout.support_simple_spinner_dropdown_item,date1);
                ListView listView=(ListView)findViewById(R.id.listview);
                listView.setAdapter(adapter);
                break;
            case 1:
                String xx=date1.get(MID);
                String yy=date1.get(MID-1);
                final String[] where2={date1.get(MID-1)};
                final String[] where3={date1.get(MID)};
                final String[] where4={"*"};
                ContentValues values0=new ContentValues();
                values0.put("city","*");
                ContentValues values=new ContentValues();
                values.put("city",xx);
                db.update("love",values0,"city=?",where2);
                date1.set(MID-1,xx);
                ContentValues values1=new ContentValues();
                values1.put("city",yy);
                db.update("love",values1,"city=?",where3);
                db.update("love",values,"city=?",where4);
                date1.set(MID,yy);
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Weather.this,R.layout.support_simple_spinner_dropdown_item,date1);
                ListView listView1=(ListView)findViewById(R.id.listview);
                listView1.setAdapter(adapter1);
                break;
            case 2:
                String xxx=date1.get(MID);
                String yyy=date1.get(0);
                final String[] where22={date1.get(0)};
                final String[] where33={date1.get(MID)};
                final String[] where44={"*"};
                ContentValues values00=new ContentValues();
                values00.put("city","*");
                ContentValues values2=new ContentValues();
                values2.put("city",xxx);
                db.update("love",values00,"city=?",where22);
                date1.set(0,xxx);
                ContentValues values3=new ContentValues();
                values3.put("city",yyy);
                db.update("love",values3,"city=?",where33);
                db.update("love",values2,"city=?",where44);
                date1.set(MID,yyy);
                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(Weather.this,R.layout.support_simple_spinner_dropdown_item,date1);
                ListView listView2=(ListView)findViewById(R.id.listview);
                listView2.setAdapter(adapter2);
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
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
