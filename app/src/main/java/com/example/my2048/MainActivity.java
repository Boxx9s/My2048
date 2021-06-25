package com.example.my2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.my2048.base.MyApplication;
import com.example.my2048.model.*;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame;
    private Button continueGame;
    private boolean mBound = false;
    private SaveGame mSaveGame;
    private MyApplication mp;
    private AlertDialog mPlayerDialog;
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
                mp.setContiune(true);
                startActivity(intent);
                break;
        }
    }
}
