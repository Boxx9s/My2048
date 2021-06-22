package com.example.my2048;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my2048.base.MyApplication;
import com.example.my2048.model.*;
import com.example.my2048.util.SQLiteHelper;

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame;
    private Button continueGame;
    private boolean mBound = false;
    private SaveGame mSaveGame;
    private MyApplication mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mp = (MyApplication)getApplication();
    }

    private void initView() {
        startGame = (Button) findViewById(R.id.game_start);
        continueGame = (Button) findViewById(R.id.game_continue);
        startGame.setOnClickListener(this);
        continueGame.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent =new Intent(MainActivity.this,GameActivity.class);
        switch (v.getId()) {
            case R.id.game_start:
                mp.setContiune(false);
                startActivity(intent);
                break;
            case R.id.game_continue:
//                String sql = "select * from " + SaveGame.class.getSimpleName();
//                List<Map<String, String>> SaveResult = SQLiteHelper.with(this).query(sql);
//                int a = 1+2;
                mp.setContiune(true);
                startActivity(intent);
                break;
        }
    }
}
