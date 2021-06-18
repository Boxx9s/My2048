package com.example.my2048;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my2048.model.*;
import com.example.my2048.util.SQLiteHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame;
    private Button continueGame;
    private RankistAIDL aidl;
    private boolean mBound = false;
    private SaveGame mSaveGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent = new Intent("com.example.my2048.service");
        intent.setPackage("com.example.my2048");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                if (aidl == null){
                    aidl = RankistAIDL.Stub.asInterface(service);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        },BIND_AUTO_CREATE);
    }

    private void initView() {
        startGame = (Button) findViewById(R.id.game_start);
        continueGame = (Button) findViewById(R.id.game_continue);
        startGame.setOnClickListener(this);
        continueGame.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_start:
                Intent intent =new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
                break;
            case R.id.game_continue:
                break;
        }
    }
}
