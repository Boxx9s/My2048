package com.example.my2048;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;


import android.media.AudioManager;
import android.media.SoundPool;

import android.os.Bundle;
import android.os.RemoteException;

import android.os.SystemClock;

import android.text.TextUtils;


import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my2048.base.MyApplication;
import com.example.my2048.model.*;
import com.example.my2048.util.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author zhb
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GameActivity";
    private final float time = 200;
    private int[][] name;
    private Boolean[][] isOver;
    private GestureDetector gestureDetector;
    private Animation mAnimation;
    private TextView mNowScore;
    private TextView mBestScore;
    private int mMyScore = 0;
    private Button reset;
    private Button zymBtn;
    private AlertDialog dialog;
    private Button malertRestart, malert_retrun;
    private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
    private MyApplication mp;
    private SaveGame mSaveGame;
    private ChangeStyleTool mChangeStyleTool;
    private Chronometer mChronometer;
    private int tempFour;
    private Button start;
    private Button pause;
    private long recordingTime;
    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recordingTime = 0;
        mChangeStyleTool = new ChangeStyleTool();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SoundPlayUtils.init(this);
        mp =(MyApplication)getApplication();
        try {
            initView();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        initGesture();
    }

    private void initView() throws RemoteException {
        start = findViewById(R.id.kkk);
        pause = findViewById(R.id.lll);
        mChronometer = findViewById(R.id.clock);
        mNowScore = (TextView) findViewById(R.id.now_score);
        mBestScore = (TextView) findViewById(R.id.best_score);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.synt);
        initData();
        View view1 = LayoutInflater.from(this).inflate(R.layout.game_alert, null);
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view1)
                .create();
        malert_retrun = view1.findViewById(R.id.retrun_alert);
        malert_retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMain();
                dialog.dismiss();
            }
        });
        malertRestart = view1.findViewById(R.id.restart_alert);
        malertRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        zymBtn = (Button) findViewById(R.id.zym_btn);
        zymBtn.setOnClickListener(this);
    }
    private void initData() throws RemoteException {
        mMyScore = 0;
        tempFour = 4;
        int x , y;
        name = new int[][]{{R.id.id_00, R.id.id_01, R.id.id_02, R.id.id_03}, {R.id.id_10, R.id.id_11, R.id.id_12, R.id.id_13},
                {R.id.id_20, R.id.id_21, R.id.id_22, R.id.id_23}, {R.id.id_30, R.id.id_31, R.id.id_32, R.id.id_33}};
        isOver = new Boolean[][]{{false, false, false, false}, {false, false, false, false},
                {false, false, false, false}, {false, false, false, false}};
        mNowScore.setText("0");
        mBestScore.setText(mp.aidl.getBestScore());
        if(!mp.isContinue()){
            mp.aidl.initSaveGame();
            mSaveGame = new SaveGame();
            for (x = 0;x < tempFour; x++){
                for(y = 0;y < tempFour;y++){
                    mSaveGame.setId(name[x][y]);
                    mSaveGame.setText("");
                    mSaveGame.setOver(false);
                    SQLiteHelper.with(this).insert(mSaveGame);
                }
            }
            for (int[] bgs : name) {
                for (int bg : bgs) {
                    TextView textView = findViewById(bg);
                    textView.setBackgroundResource(R.drawable.text_bg);
                    textView.setTextColor(Color.BLACK);
                    textView.setText("");
                }
            }
            setNum();
        }else if(mp.isContinue){
            String sql = "select * from " + SaveGame.class.getSimpleName();
            List<Map<String, String>> mSaveResult = SQLiteHelper.with(this).query(sql);
            TextView t;
            for(x = 0; x < tempFour; x++){
                for(y = 0; y < tempFour; y++){
                    Map<String, String> map= mSaveResult.get((y+1) + (4 * x)-1);
                    isOver[x][y]= "true".equals(map.get("isOver"));
                    t = findViewById(name[x][y]);
                    t.setText(""+map.get("text"));
                    mChangeStyleTool.ChangeStyle(t);
                    mChangeStyleTool.changSize(t);
                }
            }
        }
    }
    private void initGesture() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(mp.isMute){
                    SoundPlayUtils.play(2);
                }
                if (e1.getX() - e2.getX() > time) {
                    for (int i = 0; i < tempFour; i++) {
                        for (int j = 0; j < tempFour; j++) {
                            if (isOver[j][i]) {
                                try {
                                    setLeft(j, i);
                                } catch (RemoteException pE) {
                                    pE.printStackTrace();
                                }
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e2.getX() - e1.getX() > time) {
                    for (int i = tempFour - 1; i >= 0; i--) {
                        for (int j = tempFour - 1; j >= 0; j--) {
                            if (isOver[j][i]) {
                                try {
                                    setRight(j, i);
                                } catch (RemoteException pE) {
                                    pE.printStackTrace();
                                }
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e1.getY() - e2.getY() > time) {
                    for (int i = 0; i < tempFour; i++) {
                        for (int j = 0; j < tempFour; j++) {
                            if (isOver[i][j]) {
                                try {
                                    setUp(i, j);
                                } catch (RemoteException pE) {
                                    pE.printStackTrace();
                                }
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e2.getY() - e1.getY() > time) {
                    for (int i = tempFour - 1; i >= 0; i--) {
                        for (int j = 0; j < tempFour; j++) {
                            if (isOver[i][j]) {
                                try {
                                    setDown(i, j);
                                } catch (RemoteException pE) {
                                    pE.printStackTrace();
                                }
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                return false;
            }
        });
    }
    private void setLeft(int i, int j) throws RemoteException {
        int h = i;
        sign1:
            if (j != 0) {
                for (int w = j; w > 0; w--) {
                    TextView ahead = findViewById(name[h][w - 1]);
                    TextView local = findViewById(name[h][w]);
                    if (!ahead.getText().toString().equals("") && ahead.getText().toString().equals(local.getText().toString())) {
                        if(mp.isMute){
                            SoundPlayUtils.play(1);
                        }
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        ahead.startAnimation(mAnimation);
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[h][w - 1] = true;
                        isOver[h][w] = false;
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                        setScore(ahead);
                        break sign1;
                    }
                    if (ahead.getText().toString() == "") {
                        isOver[h][w - 1] = true;
                        isOver[h][w] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                    }
                }
            }
        }
    private void setRight(int i, int j) throws RemoteException {
        sign2:
        for (int h = 0; h < tempFour; h++) {
            if (i == h && j != 3) {
                for (int w = j; w < tempFour - 1; w++) {
                    TextView ahead = findViewById(name[h][w + 1]);
                    TextView local = findViewById(name[h][w]);
                    if (ahead.getText().toString() != "" && ahead.getText().toString().equals(local.getText().toString())) {
                        if(mp.isMute){
                            SoundPlayUtils.play(1);
                        }
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(mAnimation);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                        setScore(ahead);
                        break sign2;
                    }
                    if (ahead.getText().toString() == "") {
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                    }
                }
            }
        }
    }

    private void setUp(int i, int j) throws RemoteException {
        sign3:
        for (int h = 0; h < tempFour; h++) {
            for (int w = 0; w < tempFour; w++) {
                if (w == j && i != 0) {
                    TextView ahead = findViewById(name[i - 1][j]);
                    TextView local = findViewById(name[i][j]);
                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        if(mp.isMute){
                            SoundPlayUtils.play(1);
                        }
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(mAnimation);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                        setScore(ahead);
                        break sign3;
                    }
                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                    }
                    i--;
                }
            }
        }
    }
    private void setDown(int i, int j) throws RemoteException {
        sign4:
        for (int h = 0; h < tempFour; h++) {
            for (int w = 0; w < tempFour; w++) {
                if (w == j && i < 3) {
                    TextView ahead = findViewById(name[i + 1][j]);
                    TextView local = findViewById(name[i][j]);
                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        if(mp.isMute){
                            SoundPlayUtils.play(1);
                        }
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(mAnimation);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                        setScore(ahead);
                        break sign4;
                    }
                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeStyleTool.changSize(ahead);
                        mChangeStyleTool.changSize(local);
                        mChangeStyleTool.ChangeStyle(ahead);
                        mChangeStyleTool.ChangeStyle(local);
                    }
                    i++;
                }
            }
        }
    }
    private void setNum() throws RemoteException {
        int index = getTrueNum();
        if (index != 16) {
            int a ;
            int x = new Random().nextInt(4);
            int y = new Random().nextInt(4);
            a = new Random().nextInt(4);
            TextView m ;
            int i , j;
            while (isOver[x][y]) {
                x = new Random().nextInt(4);
                y = new Random().nextInt(4);
            }
            TextView textView = findViewById(name[x][y]);
            isOver[x][y] = true;
            if (a < 2) {
                textView.setText(2 + "");
                mChangeStyleTool.ChangeStyle(textView);
                mChangeStyleTool.changSize(textView);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                textView.setAnimation(animation);
                textView.startAnimation(animation);
            } else {
                textView.setText(4 + "");
                mChangeStyleTool.ChangeStyle(textView);
                mChangeStyleTool.changSize(textView);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                textView.setAnimation(animation);
                textView.startAnimation(animation);
                }
            for(i = 0; i < tempFour; i++){
                for(j = 0; j<tempFour; j++){
                    m = findViewById(name[i][j]);
                    try {
                        mp.aidl.Savegame(name[i][j],isOver[i][j],m.getText().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            dialog.show();
            mp.aidl.sendScore(mNowScore.getText().toString(),mp.mPlayerName);
        }
    }
    private void setnum(){
        try {
            setNum();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getTrueNum() {
        int index = 0;
        for (Boolean[] Bs : isOver) {
            for (Boolean B : Bs) {
                if (B) {
                    index++;
                }
            }
        }
        return index;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    private int parse(String data) {
        return Integer.parseInt(data);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                try {
                    initData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.zym_btn:
                returnMain();
                break;
            case R.id.kkk:
                mChronometer.setBase(SystemClock.elapsedRealtime() - recordingTime);
                mChronometer.start();
                break;
            case R.id.lll:
                mChronometer.stop();
                recordingTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
                break;
            default:
                break;
        }
    }

    private void returnMain() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setScore(TextView ahead) throws RemoteException {
        switch (ahead.getText().toString()) {
            case "2":
                mMyScore += 2;
                break;
            case "4":
                mMyScore += 4;
                break;
            case "8":
                mMyScore += 8;
                break;
            case "16":
                mMyScore += 16;
                break;
            case "32":
                mMyScore += 32;
                break;
            case "64":
                mMyScore += 64;
                break;
            case "128":
                mMyScore += 128;
                break;
            case "256":
                mMyScore += 256;
                break;
            case "512":
                mMyScore += 512;
                break;
            case "1024":
                mMyScore += 1024;
                break;
            case "2048":
                mMyScore += 2048;
                break;
            default:
                break;
        }
        mNowScore.setText(mMyScore + "");
            if (parse(mNowScore.getText().toString()) > parse(mp.aidl.getBestScore())) {
                mp.aidl.setBestScore(mNowScore.getText().toString());
                mBestScore.setText(mMyScore + "");
            }
    }
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出游戏",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            for(int i = 0; i < tempFour; i++){
                for(int j = 0; j < tempFour; j++){
                }
            }
            finish();
        }

    }
}