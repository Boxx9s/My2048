package com.zhb.my2048.util;

import android.graphics.Color;
import android.os.RemoteException;
import android.widget.TextView;

import com.zhb.my2048.MainActivity;
import com.zhb.my2048.R;
import com.zhb.my2048.base.MyApplication;

/**
 * @author zhb
 */
public class ChangeBoxTool {
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

    public void changSize(TextView text) {

        switch (text.getText().toString().length()) {
            case 1:

            case 2:
                text.setTextSize(40);
                break;
            case 3:
                text.setTextSize(35);
                break;
            case 4:
                text.setTextSize(30);
                break;
            case 5:
                text.setTextSize(25);
                break;
            case 6:
                text.setTextSize(20);
                break;
            case 7:
                text.setTextSize(18);
                break;
            case 8:
                text.setTextSize(16);
                break;
            case 9:
                text.setTextSize(14);
                break;
            case 10:
                text.setTextSize(12);
                break;
            default:
                break;
        }
    }

    public void ChangeStyle(TextView text) {
        switch (text.getText().toString()) {
            case "2":
            case CUBE_TEXT_2_FUN:
                text.setBackgroundResource(R.drawable.text_2);
                text.setTextColor(Color.BLACK);
                break;
            case "4":
            case CUBE_TEXT_4_FUN:
                text.setBackgroundResource(R.drawable.text_4);
                text.setTextColor(Color.BLACK);
                break;
            case "8":
            case CUBE_TEXT_8_FUN:
                text.setBackgroundResource(R.drawable.text_8);
                text.setTextColor(Color.BLACK);
                break;
            case "16":
            case CUBE_TEXT_16_FUN:
                text.setBackgroundResource(R.drawable.text_16);
                text.setTextColor(Color.BLACK);
                break;
            case "32":
            case CUBE_TEXT_32_FUN:
                text.setBackgroundResource(R.drawable.text_32);
                text.setTextColor(Color.WHITE);
                break;
            case "64":
            case CUBE_TEXT_64_FUN:
                text.setBackgroundResource(R.drawable.text_64);
                text.setTextColor(Color.WHITE);
                break;
            case "128":
            case CUBE_TEXT_128_FUN:
                text.setBackgroundResource(R.drawable.text_128);
                text.setTextColor(Color.WHITE);
                break;
            case "256":
            case CUBE_TEXT_256_FUN:
                text.setBackgroundResource(R.drawable.text_256);
                text.setTextColor(Color.WHITE);
                break;
            case "512":
            case CUBE_TEXT_512_FUN:
                text.setBackgroundResource(R.drawable.text_512);
                text.setTextColor(Color.WHITE);
                break;
            case "1024":
            case CUBE_TEXT_1024_FUN:
                text.setBackgroundResource(R.drawable.text_1024);
                text.setTextColor(Color.WHITE);
                break;
            case "2048":
            case CUBE_TEXT_2048_FUN:
                text.setBackgroundResource(R.drawable.text_2048);
                text.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }


}
