package com.example.my2048;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my2048.base.MyApplication;
import com.example.my2048.model.SaveGame;
import com.example.my2048.model.Score;
import com.example.my2048.util.SQLiteHelper;

/**
 * @author zhb
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mstartGame;
    private Button mcontinueGame;
    private MyApplication mp;
    private Button mSettingButton;
    private EditText mNameEdit;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = (MyApplication) getApplication();
        initView();
    }

    private void initView() {
        Log.i("test", "onServiceConnected:1111 ");

        mstartGame = (Button) findViewById(R.id.game_start);
        mcontinueGame = (Button) findViewById(R.id.game_continue);
        mSettingButton = findViewById(R.id.game_setting);
        mNameEdit = findViewById(R.id.playerName);
        mstartGame.setOnClickListener(this);
        mcontinueGame.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        switch (v.getId()) {
            case R.id.game_start:
                mp.mPlayerName = mNameEdit.getText().toString();
                mp.setContinue(false);
                startActivity(intent);
                break;
            case R.id.game_continue:
                mp.mPlayerName = mNameEdit.getText().toString();
                mp.setContinue(true);
                startActivity(intent);
                break;
            case R.id.game_setting:
                Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}