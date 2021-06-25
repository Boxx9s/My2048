package com.example.my2048;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.my2048.base.MyApplication;
import com.example.my2048.model.*;
import com.example.my2048.util.SQLiteHelper;
import com.example.my2048.util.SoundPlayUtils;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GameActivity";
    private final float time = 200;
    private int[][] name;
    private Boolean[][] isOver;
    private GestureDetector gestureDetector;
    private Animation compot;
    private TextView mNowScore;
    private TextView mBestScore;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int MySorce = 0;
    private Button reset;
    private Button zymBtn;
    private AlertDialog dialog;
    private Button alertRestart, alert_retrun;
    private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
    private MyApplication mp;
    private SaveGame mSaveGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        mNowScore = (TextView) findViewById(R.id.now_score);
        mBestScore = (TextView) findViewById(R.id.best_score);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        compot = AnimationUtils.loadAnimation(this, R.anim.synt);
        initData();
        View view1 = LayoutInflater.from(this).inflate(R.layout.game_alert, null);
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view1)
                .create();
        alert_retrun = view1.findViewById(R.id.retrun_alert);
        alert_retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrunMain();
                dialog.dismiss();
            }
        });
        alertRestart = view1.findViewById(R.id.restart_alert);
        alertRestart.setOnClickListener(new View.OnClickListener() {
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
        MySorce = 0;
        int x , y;
        name = new int[][]{{R.id.id_00, R.id.id_01, R.id.id_02, R.id.id_03}, {R.id.id_10, R.id.id_11, R.id.id_12, R.id.id_13},
                {R.id.id_20, R.id.id_21, R.id.id_22, R.id.id_23}, {R.id.id_30, R.id.id_31, R.id.id_32, R.id.id_33}};
        isOver = new Boolean[][]{{false, false, false, false}, {false, false, false, false},
                {false, false, false, false}, {false, false, false, false}};
        mNowScore.setText("0");
        mBestScore.setText(sp.getString("mBestScore", 0 + ""));
        if(!mp.isContiune()){
            SQLiteHelper.with(this).deleteTable(SaveGame.class.getSimpleName());
            mSaveGame = new SaveGame();
            for (x = 0;x < 4; x++){
                for(y = 0;y < 4;y++){
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
        }else if(mp.isContiune){
            String sql = "select * from " + SaveGame.class.getSimpleName();
            List<Map<String, String>> SaveResult = SQLiteHelper.with(this).query(sql);
            TextView t;
            for(x = 0; x < 4; x++){
                for(y = 0; y < 4; y++){
                    Map<String, String> map= SaveResult.get((y+1) + (4 * x)-1);
                    isOver[x][y]= map.get("isOver").equals("true");
                    t = findViewById(name[x][y]);
                    t.setText(""+map.get("text"));
                    switch (t.getText().toString().length()) {
                        case 1:
                            t.setTextSize(40);
                            break;
                        case 2:
                            t.setTextSize(40);
                            break;
                        case 3:
                            t.setTextSize(35);
                            break;
                        case 4:
                            t.setTextSize(30);
                            break;
                        case 5:
                            t.setTextSize(25);
                            break;
                        case 6:
                            t.setTextSize(20);
                            break;
                        case 7:
                            t.setTextSize(18);
                            break;
                        case 8:
                            t.setTextSize(16);
                            break;
                        case 9:
                            t.setTextSize(14);
                            break;
                        case 10:
                            t.setTextSize(12);
                            break;
                    }
                    switch (t.getText().toString()) {
                        case "2":
                            t.setBackgroundResource(R.drawable.text_2);
                            t.setTextColor(Color.BLACK);
                            break;
                        case "4":
                            t.setBackgroundResource(R.drawable.text_4);
                            t.setTextColor(Color.BLACK);
                            break;
                        case "8":
                            t.setBackgroundResource(R.drawable.text_8);
                            t.setTextColor(Color.BLACK);
                            break;
                        case "16":
                            t.setBackgroundResource(R.drawable.text_16);
                            t.setTextColor(Color.BLACK);
                            break;
                        case "32":
                            t.setBackgroundResource(R.drawable.text_32);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "64":
                            t.setBackgroundResource(R.drawable.text_64);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "128":
                            t.setBackgroundResource(R.drawable.text_128);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "256":
                            t.setBackgroundResource(R.drawable.text_256);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "512":
                            t.setBackgroundResource(R.drawable.text_512);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "1024":
                            t.setBackgroundResource(R.drawable.text_1024);
                            t.setTextColor(Color.WHITE);
                            break;
                        case "2048":
                            t.setBackgroundResource(R.drawable.text_2048);
                            t.setTextColor(Color.WHITE);
                            break;
                    }
                }
            }
        }
    }
    private void initGesture() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                SoundPlayUtils.play(2);
                if (e1.getX() - e2.getX() > time) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (isOver[j][i]) {
                                setLeft(j, i);
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e2.getX() - e1.getX() > time) {
                    for (int i = 3; i >= 0; i--) {
                        for (int j = 3; j >= 0; j--) {
                            if (isOver[j][i]) {
                                setRight(j, i);
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e1.getY() - e2.getY() > time) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (isOver[i][j]) {
                                setUp(i, j);
                            }
                        }
                    }
                    setnum();
                    return true;
                }
                if (e2.getY() - e1.getY() > time) {
                    for (int i = 3; i >= 0; i--) {
                        for (int j = 0; j < 4; j++) {
                            if (isOver[i][j]) {
                                setDonw(i, j);
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
    private void setLeft(int i, int j) {
        int h = i;
        sign1:
            if (j != 0) {
                for (int w = j; w > 0; w--) {
                    TextView ahead = findViewById(name[h][w - 1]);/*前面的TextView*/
                    TextView local = findViewById(name[h][w]);/*后面的TextView*/
                    if (ahead.getText().toString() != "" && ahead.getText().toString().equals(local.getText().toString())) {
                        SoundPlayUtils.play(1);
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        ahead.startAnimation(compot);
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[h][w - 1] = true;
                        isOver[h][w] = false;
                        ChangStyle(ahead, local);
                        setScore(ahead);
                        break sign1;
                    }
                    if (ahead.getText().toString() == "") {
                        isOver[h][w - 1] = true;
                        isOver[h][w] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        ChangStyle(ahead, local);
                    }
                }
            }
    }
    private void setRight(int i, int j) {
        sign2:
        for (int h = 0; h < 4; h++) {
            if (i == h && j != 3) {
                for (int w = j; w < 3; w++) {
                    TextView ahead = findViewById(name[h][w + 1]);
                    TextView local = findViewById(name[h][w]);
                    if (ahead.getText().toString() != "" && ahead.getText().toString().equals(local.getText().toString())) {
                        SoundPlayUtils.play(1);
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(compot);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        ChangStyle(ahead, local);
                        setScore(ahead);
                        break sign2;
                    }
                    if (ahead.getText().toString() == "") {
                        isOver[h][w + 1] = true;
                        isOver[h][w] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        ChangStyle(ahead, local);
                    }
                }
            }
        }
    }


    private void setUp(int i, int j) {
        sign3:
        for (int h = 0; h < 4; h++) {
            for (int w = 0; w < 4; w++) {
                if (w == j && i != 0) {
                    TextView ahead = findViewById(name[i - 1][j]);
                    TextView local = findViewById(name[i][j]);
                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        SoundPlayUtils.play(1);
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(compot);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        ChangStyle(ahead, local);
                        setScore(ahead);
                        break sign3;
                    }
                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i - 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        ChangStyle(ahead, local);
                    }
                    i--;
                }

            }

        }
    }
    private void setDonw(int i, int j) {
        sign4:
        for (int h = 0; h < 4; h++) {
            for (int w = 0; w < 4; w++) {
                if (w == j && i < 3) {
                    TextView ahead = findViewById(name[i + 1][j]);
                    TextView local = findViewById(name[i][j]);

                    if (!TextUtils.isEmpty(ahead.getText()) && ahead.getText().toString().equals(local.getText().toString())) {
                        SoundPlayUtils.play(1);
                        int num = parse(ahead.getText().toString());
                        ahead.setText(num + num + "");
                        local.setText("");
                        ahead.startAnimation(compot);
                        local.setBackgroundResource(R.drawable.text_bg);
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        ChangStyle(ahead, local);
                        setScore(ahead);
                        break sign4;
                    }

                    if (TextUtils.isEmpty(ahead.getText())) {
                        isOver[i + 1][j] = true;
                        isOver[i][j] = false;
                        ahead.setText(local.getText().toString() + "");
                        local.setText("");
                        local.setBackgroundResource(R.drawable.text_bg);
                        ChangStyle(ahead, local);
                    }


                    i++;
                }

            }

        }

    }


    private void setNum() throws RemoteException {

        int index = getTrueNum();
        int a ;
        int x = new Random().nextInt(4);
        int y = new Random().nextInt(4);
        TextView m ;
        if (index != 16) {
            int i , j;

            a = new Random().nextInt(4);
            while (isOver[x][y]) {
                x = new Random().nextInt(4);
                y = new Random().nextInt(4);
            }
            TextView textView = findViewById(name[x][y]);
            isOver[x][y] = true;
            if (a < 2) {
                textView.setText(2 + "");
                textView.setBackgroundResource(R.drawable.text_2);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                textView.setAnimation(animation);
                textView.startAnimation(animation);
            } else {
                textView.setText(4 + "");
                textView.setBackgroundResource(R.drawable.text_4);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.find);
                textView.setAnimation(animation);
                textView.startAnimation(animation);
                }
            for(i = 0; i < 4; i++){
                for(j = 0; j<4; j++){
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
            mp.aidl.sendScore(mNowScore.toString(),"player");
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
                if (B == true) {
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
                retrunMain();
                break;
        }
    }

    private void retrunMain() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void ChangStyle(TextView ahead, TextView local) {
        switch (ahead.getText().toString().length()) {
            case 1:
                ahead.setTextSize(40);
                break;
            case 2:
                ahead.setTextSize(40);
                break;
            case 3:
                ahead.setTextSize(35);
                break;
            case 4:
                ahead.setTextSize(30);
                break;
            case 5:
                ahead.setTextSize(25);
                break;
            case 6:
                ahead.setTextSize(20);
                break;
            case 7:
                ahead.setTextSize(18);
                break;
            case 8:
                ahead.setTextSize(16);
                break;
            case 9:
                ahead.setTextSize(14);
                break;
            case 10:
                ahead.setTextSize(12);
                break;
        }
        switch (ahead.getText().toString()) {
            case "2":
                ahead.setBackgroundResource(R.drawable.text_2);
                ahead.setTextColor(Color.BLACK);
                break;
            case "4":
                ahead.setBackgroundResource(R.drawable.text_4);
                ahead.setTextColor(Color.BLACK);
                break;
            case "8":
                ahead.setBackgroundResource(R.drawable.text_8);
                ahead.setTextColor(Color.BLACK);
                break;
            case "16":
                ahead.setBackgroundResource(R.drawable.text_16);
                ahead.setTextColor(Color.BLACK);
                break;
            case "32":
                ahead.setBackgroundResource(R.drawable.text_32);
                ahead.setTextColor(Color.WHITE);
                break;
            case "64":
                ahead.setBackgroundResource(R.drawable.text_64);
                ahead.setTextColor(Color.WHITE);
                break;
            case "128":
                ahead.setBackgroundResource(R.drawable.text_128);
                ahead.setTextColor(Color.WHITE);
                break;
            case "256":
                ahead.setBackgroundResource(R.drawable.text_256);
                ahead.setTextColor(Color.WHITE);
                break;
            case "512":
                ahead.setBackgroundResource(R.drawable.text_512);
                ahead.setTextColor(Color.WHITE);
                break;
            case "1024":
                ahead.setBackgroundResource(R.drawable.text_1024);
                ahead.setTextColor(Color.WHITE);
                break;
            case "2048":
                ahead.setBackgroundResource(R.drawable.text_2048);
                ahead.setTextColor(Color.WHITE);
                break;

        }

        switch (local.getText().toString()) {
            case "2":
                local.setBackgroundResource(R.drawable.text_2);
                local.setTextColor(Color.BLACK);
                break;
            case "4":
                local.setBackgroundResource(R.drawable.text_4);
                local.setTextColor(Color.BLACK);
                break;
            case "8":
                local.setBackgroundResource(R.drawable.text_8);
                local.setTextColor(Color.BLACK);
                break;
            case "16":
                local.setBackgroundResource(R.drawable.text_16);
                local.setTextColor(Color.BLACK);
                break;
            case "32":
                local.setBackgroundResource(R.drawable.text_32);
                local.setTextColor(Color.WHITE);
                break;
            case "64":
                local.setBackgroundResource(R.drawable.text_64);
                local.setTextColor(Color.WHITE);
                break;
            case "128":
                local.setBackgroundResource(R.drawable.text_128);
                local.setTextColor(Color.WHITE);
                break;
            case "256":
                local.setBackgroundResource(R.drawable.text_256);
                local.setTextColor(Color.WHITE);
                break;
            case "512":
                local.setBackgroundResource(R.drawable.text_512);
                local.setTextColor(Color.WHITE);
                break;
            case "1024":
                local.setBackgroundResource(R.drawable.text_1024);
                local.setTextColor(Color.WHITE);
                break;
            case "2048":
                local.setBackgroundResource(R.drawable.text_2048);
                local.setTextColor(Color.WHITE);
                break;
            default:
                local.setTextColor(Color.BLACK);
        }
    }


    private void setScore(TextView ahead) {
        switch (ahead.getText().toString()) {
            case "2":
                MySorce += 2;
                break;
            case "4":
                MySorce += 4;
                break;
            case "8":
                MySorce += 8;
                break;
            case "16":
                MySorce += 16;
                break;
            case "32":
                MySorce += 32;
                break;
            case "64":
                MySorce += 64;
                break;
            case "128":
                MySorce += 128;
                break;
            case "256":
                MySorce += 256;
                break;
            case "512":
                MySorce += 512;
                break;
            case "1024":
                MySorce += 1024;
                break;
            case "2048":
                MySorce += 2048;
                break;

        }
        mNowScore.setText(MySorce + "");
        if (parse(mNowScore.getText().toString()) > parse(sp.getString("mBestScore", 0 + ""))) {
            editor.putString("mBestScore", MySorce + "");
            editor.commit();
            mBestScore.setText(MySorce + "");
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
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                }
            }
            finish();
        }

    }
}