package com.example.my2048;

import android.app.Application;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my2048.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhb
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch mSoundSwitch;
    private MyApplication mp;
    private Spinner mSpinnerGameMode;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<CharSequence> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSoundSwitch = findViewById(R.id.sound_switch);
        try {
            initData();
        } catch (RemoteException pE) {
            pE.printStackTrace();
        }
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton pCompoundButton, boolean pB) {
                try {
                    if(pB){
                        mp.aidl.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: "+ mp.aidl.isMute());
                    }else {
                        mp.aidl.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: "+ mp.aidl.isMute());
                    }
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            }
        };
        mSoundSwitch.setOnCheckedChangeListener(listener);
        mSpinnerGameMode = findViewById(R.id.game_mode_spinner);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_item,R.layout.support_simple_spinner_dropdown_item);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerGameMode.setAdapter(mAdapter);
    }

    public void initData() throws RemoteException {
        mp = (MyApplication)getApplication();
        mSoundSwitch.setChecked(mp.aidl.isMute());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }
}
