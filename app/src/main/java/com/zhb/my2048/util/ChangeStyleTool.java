package com.zhb.my2048.util;

import android.graphics.Color;
import android.widget.TextView;

import com.zhb.my2048.R;

/**
 * @author zhb
 */
public class ChangeStyleTool {
    public void changSize(TextView text){
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
    public void ChangeStyle(TextView text){
        switch (text.getText().toString()){
            case "2":
                text.setBackgroundResource(R.drawable.text_2);
                text.setTextColor(Color.BLACK);
                break;
            case "4":
                text.setBackgroundResource(R.drawable.text_4);
                text.setTextColor(Color.BLACK);
                break;
            case "8":
                text.setBackgroundResource(R.drawable.text_8);
                text.setTextColor(Color.BLACK);
                break;
            case "16":
                text.setBackgroundResource(R.drawable.text_16);
                text.setTextColor(Color.BLACK);
                break;
            case "32":
                text.setBackgroundResource(R.drawable.text_32);
                text.setTextColor(Color.WHITE);
                break;
            case "64":
                text.setBackgroundResource(R.drawable.text_64);
                text.setTextColor(Color.WHITE);
                break;
            case "128":
                text.setBackgroundResource(R.drawable.text_128);
                text.setTextColor(Color.WHITE);
                break;
            case "256":
                text.setBackgroundResource(R.drawable.text_256);
                text.setTextColor(Color.WHITE);
                break;
            case "512":
                text.setBackgroundResource(R.drawable.text_512);
                text.setTextColor(Color.WHITE);
                break;
            case "1024":
                text.setBackgroundResource(R.drawable.text_1024);
                text.setTextColor(Color.WHITE);
                break;
            case "2048":
                text.setBackgroundResource(R.drawable.text_2048);
                text.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

}
