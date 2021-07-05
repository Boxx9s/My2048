package com.example.my2048;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    private boolean mBound = false;
    private SaveGame mSaveGame;
    private MyApplication mp;
    private AlertDialog mPlayerDialog;
    private Button mSettingButton;
    private EditText mNameEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mp = (MyApplication) getApplication();
    }

    private void initView() {
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
                SQLiteHelper.with(this).deleteTable(Score.class.getSimpleName());
                //               startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            default:
                break;
        }
    }
}