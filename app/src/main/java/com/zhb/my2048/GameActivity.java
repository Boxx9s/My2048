package com.zhb.my2048;

import android.annotation.SuppressLint;

import android.graphics.Color;


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

import com.zhb.my2048.base.MyApplication;
import com.zhb.my2048.model.*;
import com.zhb.my2048.util.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author zhb
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String CUBE_TEXT_2_FUN = "当";
    public final static String CUBE_TEXT_4_FUN = "你";
    public final static String CUBE_TEXT_8_FUN = "玩";
    public final static String CUBE_TEXT_16_FUN = "到";
    public final static String CUBE_TEXT_32_FUN = "最";
    public final static String CUBE_TEXT_64_FUN = "后";
    public final static String CUBE_TEXT_128_FUN = "就";
    public final static String CUBE_TEXT_256_FUN = "变";
    public final static String CUBE_TEXT_512_FUN = "得";
    public final static String CUBE_TEXT_1024_FUN = "很";
    public final static String CUBE_TEXT_2048_FUN = "帅";
    public final static String CUBE_TEXT_2 = "2";
    public final static String CUBE_TEXT_4 = "4";
    public final static String CUBE_TEXT_8 = "8";
    public final static String CUBE_TEXT_16 = "16";
    public final static String CUBE_TEXT_32 = "32";
    public final static String CUBE_TEXT_64 = "64";
    public final static String CUBE_TEXT_128 = "128";
    public final static String CUBE_TEXT_256 = "256";
    public final static String CUBE_TEXT_512 = "512";
    public final static String CUBE_TEXT_1024 = "1024";
    public final static String CUBE_TEXT_2048 = "2048";
    private static final String TAG = "GameActivity";
    private final float time = 200;
    private int[][] mBoxIds;
    private Boolean[][] isOver;
    private GestureDetector gestureDetector;
    private Animation mAnimation;
    private TextView mNowScore;
    private TextView mBestScore;
    private int mMyScore = 0;
    private Button reset;
    private Button mBcakHome_btn;
    private AlertDialog dialog;
    private Button malertRestart, malert_retrun;
    private MyApplication mp;
    private SaveGame mSaveGame;
    private ChangeBoxTool mChangeBoxTool;
    private Chronometer mChronometer;
    private int tempFour;
    private Button mPause;
    public boolean mGamePause;
    private long recordingTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mChangeBoxTool = new ChangeBoxTool();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SoundPlayUtils.init(this);
        mp = (MyApplication) getApplication();
        try {
            initView();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        initGesture();
        mGamePause = false;
    }


    private void initView() throws RemoteException {
        mPause = findViewById(R.id.game_time_pause);
        mChronometer = findViewById(R.id.clock);
        mNowScore = (TextView) findViewById(R.id.now_score);
        mBestScore = (TextView) findViewById(R.id.best_score);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.synt);
        initGameData();
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
                    initGameData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        mBcakHome_btn = (Button) findViewById(R.id.mBcakHome_btn);
        mBcakHome_btn.setOnClickListener(this);
        mPause.setOnClickListener(this);
    }

    private void initGameData() throws RemoteException {
        mMyScore = 0;
        tempFour = 4;
        int x, y;
        mBoxIds = new int[][]{{R.id.id_00, R.id.id_01, R.id.id_02, R.id.id_03}, {R.id.id_10, R.id.id_11, R.id.id_12, R.id.id_13},
                {R.id.id_20, R.id.id_21, R.id.id_22, R.id.id_23}, {R.id.id_30, R.id.id_31, R.id.id_32, R.id.id_33}};
        isOver = new Boolean[][]{{false, false, false, false}, {false, false, false, false},
                {false, false, false, false}, {false, false, false, false}};
        mNowScore.setText("0");
        mBestScore.setText(mp.mRankistAIDL.getBestScore());
        if (!mp.isContinue()) {
            recordingTime = 0;
            mp.mRankistAIDL.initSaveGame();
            mSaveGame = new SaveGame();
            for (x = 0; x < tempFour; x++) {
                for (y = 0; y < tempFour; y++) {
                    mSaveGame.setId(mBoxIds[x][y]);
                    mSaveGame.setText("");
                    mSaveGame.setOver(false);
                    SQLiteHelper.with(this).insert(mSaveGame);
                }
            }
            for (int[] bgs : mBoxIds) {
                for (int bg : bgs) {
                    TextView textView = findViewById(bg);
                    textView.setBackgroundResource(R.drawable.text_bg);
                    textView.setTextColor(Color.BLACK);
                    textView.setText("");
                }
            }
            recordingTime = 0;
            mp.mRankistAIDL.setTime(0);
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            setNum();
        } else if (mp.isContinue) {
            mNowScore.setText(mp.mRankistAIDL.getPauseScore());
            String sql = "select * from " + SaveGame.class.getSimpleName();
            List<Map<String, String>> mSaveResult = SQLiteHelper.with(this).query(sql);
            if (mSaveResult.size() == 0) {
                mp.isContinue = false;
                initGameData();
            } else {
                TextView t;
                for (x = 0; x < tempFour; x++) {
                    for (y = 0; y < tempFour; y++) {
                        Map<String, String> map = mSaveResult.get((y + 1) + (4 * x) - 1);
                        isOver[x][y] = "true".equals(map.get("isOver"));
                        t = findViewById(mBoxIds[x][y]);
                        t.setText("" + map.get("text"));
                        mChangeBoxTool.ChangeStyle(t);
                        mChangeBoxTool.changSize(t);
                        recordingTime = mp.mRankistAIDL.getTime();
                        mChronometer.setBase(SystemClock.elapsedRealtime() - recordingTime * 1000);
                        mChronometer.start();
                    }
                }
            }
        }
    }

    private void initGesture() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!mGamePause) {
                    if (mp.isMute) {
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
                TextView ahead = findViewById(mBoxIds[h][w - 1]);
                TextView local = findViewById(mBoxIds[h][w]);
                if (!ahead.getText().toString().equals("") && ahead.getText().toString().equals(local.getText().toString())) {
                    if (mp.isMute) {
                        SoundPlayUtils.play(1);
                    }
                    String text = ahead.getText().toString();
                    String textScore;
                    if(mp.GameMode == 0){
                        int num = parse(text);
                        ahead.setText(num + num + "");
                        local.setText("");
                        mChangeBoxTool.changSize(ahead);
                        mChangeBoxTool.changSize(local);
                        mChangeBoxTool.ChangeStyle(ahead);
                        mChangeBoxTool.ChangeStyle(local);
                        setScore(ahead.getText().toString());
                    }else if(mp.GameMode == 1) {
                        if (text.equals(CUBE_TEXT_2_FUN)) {
                            textScore = CUBE_TEXT_4;
                            setScore(textScore);
                            ahead.setText(CUBE_TEXT_4_FUN);
                            local.setText("");
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_4_FUN)) {
                            ahead.setText(CUBE_TEXT_8_FUN);
                            textScore = CUBE_TEXT_8;
                            setScore(textScore);
                            local.setText("");
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_8_FUN)) {
                            ahead.setText(CUBE_TEXT_16_FUN);
                            textScore = CUBE_TEXT_16;
                            setScore(textScore);
                            local.setText("");
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_16_FUN)) {
                            ahead.setText(CUBE_TEXT_32_FUN);
                            textScore = CUBE_TEXT_32;
                            setScore(textScore);
                            local.setText("");
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_32_FUN)) {
                            ahead.setText(CUBE_TEXT_64_FUN);
                            textScore = CUBE_TEXT_64;
                            setScore(textScore);
                            local.setText("");
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_64_FUN)) {
                            ahead.setText(CUBE_TEXT_128_FUN);
                            local.setText("");
                            textScore = CUBE_TEXT_128;
                            setScore(textScore);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_128_FUN)) {
                            ahead.setText(CUBE_TEXT_256_FUN);
                            local.setText("");
                            textScore = CUBE_TEXT_256;
                            setScore(textScore);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_256_FUN)) {
                            ahead.setText(CUBE_TEXT_512_FUN);
                            local.setText("");
                            textScore = CUBE_TEXT_512;
                            setScore(textScore);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_512_FUN)) {
                            ahead.setText(CUBE_TEXT_1024_FUN);
                            textScore = CUBE_TEXT_1024;
                            local.setText("");
                            setScore(textScore);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        if (text.equals(CUBE_TEXT_1024_FUN)) {
                            ahead.setText(CUBE_TEXT_2048_FUN);
                            local.setText("");
                            textScore = CUBE_TEXT_2048;
                            setScore(textScore);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                        }
                        ahead.setTextSize(40);
                    }
                    ahead.startAnimation(mAnimation);
                    local.setBackgroundResource(R.drawable.text_bg);
                    isOver[h][w - 1] = true;
                    isOver[h][w] = false;
                    break sign1;
                }
                if (ahead.getText().toString() == "") {
                    isOver[h][w - 1] = true;
                    isOver[h][w] = false;
                    ahead.setText(local.getText().toString() + "");
                    local.setText("");
                    local.setBackgroundResource(R.drawable.text_bg);
                    mChangeBoxTool.changSize(ahead);
                    mChangeBoxTool.changSize(local);
                    mChangeBoxTool.ChangeStyle(ahead);
                    mChangeBoxTool.ChangeStyle(local);
                }
            }
        }

    }

    private void setRight(int i, int j) throws RemoteException {
        sign2:
        for (int h = 0; h < tempFour; h++) {
            if (i == h && j != 3) {
                for (int w = j; w < tempFour - 1; w++) {
                    TextView ahead = findViewById(mBoxIds[h][w + 1]);
                    TextView local = findViewById(mBoxIds[h][w]);
                    if (ahead.getText().toString() != "" && ahead.getText().toString().equals(local.getText().toString())) {
                        if (mp.isMute) {
                            SoundPlayUtils.play(1);
                        }
                        String text = ahead.getText().toString();
                        String textScore;
                        if(mp.GameMode == 0){
                            int num = parse(text);
                            ahead.setText(num + num + "");
                            local.setText("");
                            mChangeBoxTool.changSize(ahead);
                            mChangeBoxTool.changSize(local);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                            setScore(ahead.getText().toString());
                        }else if(mp.GameMode == 1) {
                            if (text.equals(CUBE_TEXT_2_FUN)) {
                                textScore = CUBE_TEXT_4;
                                setScore(textScore);
                                ahead.setText(CUBE_TEXT_4_FUN);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_4_FUN)) {
                                ahead.setText(CUBE_TEXT_8_FUN);
                                textScore = CUBE_TEXT_8;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_8_FUN)) {
                                ahead.setText(CUBE_TEXT_16_FUN);
                                textScore = CUBE_TEXT_16;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_16_FUN)) {
                                ahead.setText(CUBE_TEXT_32_FUN);
                                textScore = CUBE_TEXT_32;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_32_FUN)) {
                                ahead.setText(CUBE_TEXT_64_FUN);
                                textScore = CUBE_TEXT_64;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_64_FUN)) {
                                ahead.setText(CUBE_TEXT_128_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_128;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_128_FUN)) {
                                ahead.setText(CUBE_TEXT_256_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_256;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_256_FUN)) {
                                ahead.setText(CUBE_TEXT_512_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_512;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_512_FUN)) {
                                ahead.setText(CUBE_TEXT_1024_FUN);
                                textScore = CUBE_TEXT_1024;
                                local.setText("");
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_1024_FUN)) {
                                ahead.setText(CUBE_TEXT_2048_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_2048;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            ahead.setTextSize(40);
                        }
                        ahead.startAnimation(mAnimation);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        break sign2;
                    }
                    if (ahead.getText().toString() == "") {
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeBoxTool.changSize(ahead);
                        mChangeBoxTool.changSize(local);
                        mChangeBoxTool.ChangeStyle(ahead);
                        mChangeBoxTool.ChangeStyle(local);
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
                    TextView ahead = findViewById(mBoxIds[i - 1][j]);
                    TextView local = findViewById(mBoxIds[i][j]);
                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        if (mp.isMute) {
                            SoundPlayUtils.play(1);
                        }
                        String text = ahead.getText().toString();
                        String textScore;
                        if(mp.GameMode == 0){
                            int num = parse(text);
                            ahead.setText(num + num + "");
                            local.setText("");
                            mChangeBoxTool.changSize(ahead);
                            mChangeBoxTool.changSize(local);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                            setScore(ahead.getText().toString());
                        }else if(mp.GameMode == 1) {
                            if (text.equals(CUBE_TEXT_2_FUN)) {
                                textScore = CUBE_TEXT_4;
                                setScore(textScore);
                                ahead.setText(CUBE_TEXT_4_FUN);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_4_FUN)) {
                                ahead.setText(CUBE_TEXT_8_FUN);
                                textScore = CUBE_TEXT_8;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_8_FUN)) {
                                ahead.setText(CUBE_TEXT_16_FUN);
                                textScore = CUBE_TEXT_16;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_16_FUN)) {
                                ahead.setText(CUBE_TEXT_32_FUN);
                                textScore = CUBE_TEXT_32;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_32_FUN)) {
                                ahead.setText(CUBE_TEXT_64_FUN);
                                textScore = CUBE_TEXT_64;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_64_FUN)) {
                                ahead.setText(CUBE_TEXT_128_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_128;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_128_FUN)) {
                                ahead.setText(CUBE_TEXT_256_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_256;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_256_FUN)) {
                                ahead.setText(CUBE_TEXT_512_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_512;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_512_FUN)) {
                                ahead.setText(CUBE_TEXT_1024_FUN);
                                textScore = CUBE_TEXT_1024;
                                local.setText("");
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_1024_FUN)) {
                                ahead.setText(CUBE_TEXT_2048_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_2048;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            ahead.setTextSize(40);
                        }
                        ahead.startAnimation(mAnimation);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        break sign3;
                    }
                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeBoxTool.changSize(ahead);
                        mChangeBoxTool.changSize(local);
                        mChangeBoxTool.ChangeStyle(ahead);
                        mChangeBoxTool.ChangeStyle(local);
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
                    TextView ahead = findViewById(mBoxIds[i + 1][j]);
                    TextView local = findViewById(mBoxIds[i][j]);
                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        if (mp.isMute) {
                            SoundPlayUtils.play(1);
                        }
                        String text = ahead.getText().toString();
                        String textScore;
                        if(mp.GameMode == 0){
                            int num = parse(text);
                            ahead.setText(num + num + "");
                            local.setText("");
                            mChangeBoxTool.changSize(ahead);
                            mChangeBoxTool.changSize(local);
                            mChangeBoxTool.ChangeStyle(ahead);
                            mChangeBoxTool.ChangeStyle(local);
                            setScore(ahead.getText().toString());
                        }else if(mp.GameMode == 1) {
                            if (text.equals(CUBE_TEXT_2_FUN)) {
                                textScore = CUBE_TEXT_4;
                                setScore(textScore);
                                ahead.setText(CUBE_TEXT_4_FUN);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_4_FUN)) {
                                ahead.setText(CUBE_TEXT_8_FUN);
                                textScore = CUBE_TEXT_8;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_8_FUN)) {
                                ahead.setText(CUBE_TEXT_16_FUN);
                                textScore = CUBE_TEXT_16;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_16_FUN)) {
                                ahead.setText(CUBE_TEXT_32_FUN);
                                textScore = CUBE_TEXT_32;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_32_FUN)) {
                                ahead.setText(CUBE_TEXT_64_FUN);
                                textScore = CUBE_TEXT_64;
                                setScore(textScore);
                                local.setText("");
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_64_FUN)) {
                                ahead.setText(CUBE_TEXT_128_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_128;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_128_FUN)) {
                                ahead.setText(CUBE_TEXT_256_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_256;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_256_FUN)) {
                                ahead.setText(CUBE_TEXT_512_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_512;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_512_FUN)) {
                                ahead.setText(CUBE_TEXT_1024_FUN);
                                textScore = CUBE_TEXT_1024;
                                local.setText("");
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            if (text.equals(CUBE_TEXT_1024_FUN)) {
                                ahead.setText(CUBE_TEXT_2048_FUN);
                                local.setText("");
                                textScore = CUBE_TEXT_2048;
                                setScore(textScore);
                                mChangeBoxTool.ChangeStyle(ahead);
                                mChangeBoxTool.ChangeStyle(local);
                            }
                            ahead.setTextSize(40);
                        }
                        local.setBackgroundResource(R.drawable.text_bg);
                        ahead.startAnimation(mAnimation);
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        break sign4;
                    }
                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        mChangeBoxTool.changSize(ahead);
                        mChangeBoxTool.changSize(local);
                        mChangeBoxTool.ChangeStyle(ahead);
                        mChangeBoxTool.ChangeStyle(local);
                    }
                    i++;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setNum() throws RemoteException {
        int index = getEmptyGridNum();
        int i, j;
        TextView pBox;
        TextView pCompareBox;
        if (index != 16) {
            int pNewNum;
            int x = new Random().nextInt(4);
            int y = new Random().nextInt(4);
            pNewNum = new Random().nextInt(4);
            TextView m;
            while (isOver[x][y]) {
                x = new Random().nextInt(4);
                y = new Random().nextInt(4);
            }
            pBox = findViewById(mBoxIds[x][y]);
            isOver[x][y] = true;
            if (pNewNum < 2) {
                if(mp.GameMode == 0) {
                    pBox.setText(2 + "");
                    mChangeBoxTool.ChangeStyle(pBox);
                    mChangeBoxTool.changSize(pBox);
                }else if(mp.GameMode == 1){
                    pBox.setText(CUBE_TEXT_2_FUN);
                    mChangeBoxTool.ChangeStyle(pBox);
                }
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                pBox.setAnimation(animation);
                pBox.startAnimation(animation);
            } else {
                if(mp.GameMode == 0) {
                    pBox.setText(4 + "");
                    mChangeBoxTool.ChangeStyle(pBox);
                    mChangeBoxTool.changSize(pBox);
                }else if(mp.GameMode == 1){
                    pBox.setText(CUBE_TEXT_4_FUN);
                    mChangeBoxTool.ChangeStyle(pBox);
                }
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                pBox.setAnimation(animation);
                pBox.startAnimation(animation);
            }
            for (i = 0; i < tempFour; i++) {
                for (j = 0; j < tempFour; j++) {
                    m = findViewById(mBoxIds[i][j]);
                    try {
                        mp.mRankistAIDL.SaveGame(mBoxIds[i][j], isOver[i][j], m.getText().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            mp.mRankistAIDL.setPauseScore(mNowScore.getText().toString());
        } else {
            boolean isOver = true;
            notOver:
            for (i = 0; i < tempFour; i++) {
                for (j = 0; j < tempFour; j++) {
                    pBox = findViewById(mBoxIds[i][j]);
                    if (i <= 2 && j <= 2) {
                        pCompareBox = findViewById(mBoxIds[i][j + 1]);
                        if (compareSame(pBox, pCompareBox)) {
                            Log.i(TAG, "setNum: i:" + i + " j:" + j);
                            isOver = false;
                            break notOver;
                        }
                        pCompareBox = findViewById(mBoxIds[i + 1][j]);
                        if (compareSame(pBox, pCompareBox)) {
                            Log.i(TAG, "setNum: i:" + i + " j:" + j);
                            isOver = false;
                            break notOver;
                        }
                    } else if (i == 3 && j != 3) {
                        pCompareBox = findViewById(mBoxIds[i][j + 1]);
                        if (compareSame(pBox, pCompareBox)) {
                            Log.i(TAG, "setNum: i:" + i + " j:" + j);
                            isOver = false;
                            break notOver;
                        }
                    } else if (j == 3 && i != 3) {
                        pCompareBox = findViewById(mBoxIds[i + 1][j]);
                        if (compareSame(pBox, pCompareBox)) {
                            Log.i(TAG, "setNum: i:" + i + " j:" + j);
                            isOver = false;
                            break notOver;
                        }
                    }
                }
            }
            if (isOver == true) {
                dialog.show();
                mChronometer.stop();
                mp.mRankistAIDL.initSaveGame();
                recordingTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
                mp.mRankistAIDL.sendScore(mNowScore.getText().toString(), mp.mPlayerName, recordingTime / 1000);
            }
        }
    }


    private boolean compareSame(TextView pTextView1, TextView pTextView2) {
        if (pTextView1.getText().toString().equals(pTextView2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private void setnum() {
        try {
            setNum();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getEmptyGridNum() {
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
            case R.id.game_time_pause:
                if (!mGamePause) {
                    mChronometer.stop();
                    recordingTime = (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
                    Log.i(TAG, "onPause: " + recordingTime);
                    try {
                        mp.mRankistAIDL.setTime(recordingTime);
                    } catch (RemoteException pE) {
                        pE.printStackTrace();
                    }
                    mPause.setText(R.string.tocontinue);
                    Toast.makeText(this, R.string.PausePrompt, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.i(TAG, "onResume: " + mp.mRankistAIDL.getTime());
                        mChronometer.setBase(SystemClock.elapsedRealtime() - mp.mRankistAIDL.getTime() * 1000);
                    } catch (RemoteException pE) {
                        pE.printStackTrace();
                    }
                    mChronometer.start();
                    mPause.setText(R.string.pause);
                    Toast.makeText(this, R.string.ContinuePrompt, Toast.LENGTH_SHORT).show();
                }
                mGamePause = !mGamePause;
                break;
            case R.id.reset:
                try {
                    initGameData();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mPause.setText(R.string.pause);
                    mGamePause = false;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mBcakHome_btn:
                returnMain();
                break;
            default:
                break;
        }
    }

    private void returnMain() {
        finish();
    }

    private void setScore(String pScore) throws RemoteException {
        switch (pScore) {
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
        if (parse(mNowScore.getText().toString()) > parse(mp.mRankistAIDL.getBestScore())) {
            mp.mRankistAIDL.setBestScore(mNowScore.getText().toString());
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
            for (int i = 0; i < tempFour; i++) {
                for (int j = 0; j < tempFour; j++) {
                }
            }
            finish();
        }
    }

    @Override
    protected void onPause() {
        mChronometer.stop();
        recordingTime = (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
        Log.i(TAG, "onPause: " + recordingTime);
        try {
            mp.mRankistAIDL.setTime(recordingTime);
        } catch (RemoteException pE) {
            pE.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            Log.i(TAG, "onResume: " + mp.mRankistAIDL.getTime());
            mChronometer.setBase(SystemClock.elapsedRealtime() - mp.mRankistAIDL.getTime() * 1000);
        } catch (RemoteException pE) {
            pE.printStackTrace();
        }
        mChronometer.start();
        super.onResume();
    }
}