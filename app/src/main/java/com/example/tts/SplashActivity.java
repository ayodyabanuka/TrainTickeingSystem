package com.example.tts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import com.example.tts.Database.MyDatabaseHelper;

public class SplashActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;

    String nic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        storeDataInArrays();
        if(nic == null ){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }

    }
    void storeDataInArrays(){
        myDB = new MyDatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){

        }else{

            if(cursor.moveToFirst()){
                do{
                    nic = cursor.getString(1);
                }while(cursor.moveToNext());
            }

        }
    }
}