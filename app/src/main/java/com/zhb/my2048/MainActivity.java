package com.zhb.my2048;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zhb.my2048.fragment.RankDialogFragment;
import com.zhb.my2048.base.MyApplication;

/**
 * @author zhb
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyApplication mp;
    private EditText mNameEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = (MyApplication) getApplication();
        initView();

    }
    private void initView() {
        Button mLeaderBoard = (Button) findViewById(R.id.main_game_rank);
        Button mStartGame = (Button) findViewById(R.id.game_start);
        Button mContinueGame = (Button) findViewById(R.id.game_continue);
        Button settingButton = findViewById(R.id.game_setting);
        mNameEdit = findViewById(R.id.playerName);
        mLeaderBoard.setOnClickListener(this);
        mStartGame.setOnClickListener(this);
        mContinueGame.setOnClickListener(this);
        settingButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        switch (v.getId()) {
            case R.id.main_game_rank:
                RankDialogFragment editNameDialog = new RankDialogFragment(this);
                editNameDialog.show(getSupportFragmentManager(), "EditNameDialog");
                break;
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