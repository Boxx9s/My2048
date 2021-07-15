package com.zhb.my2048;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhb.my2048.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author zhb
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch mSoundSwitch;
    private MyApplication mp;
    private Spinner mSpinnerGameMode;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter mAdapter;
    private Button mClearData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSoundSwitch = findViewById(R.id.sound_switch);
        mClearData = findViewById(R.id.clear_data);
        try {
            initData();
        } catch (RemoteException pE) {
            pE.printStackTrace();
        }
        final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton pCompoundButton, boolean pB) {
                try {
                    if (pB) {
                        mp.aidl.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: " + mp.aidl.isMute());
                    } else {
                        mp.aidl.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: " + mp.aidl.isMute());
                    }
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            }
        };
        list = new ArrayList<>();
        list.add("Time Win");
        list.add("Score Win");
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);

        mSoundSwitch.setOnCheckedChangeListener(listener);
        mSpinnerGameMode = findViewById(R.id.game_mode_spinner);
        //mAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_item, R.layout.support_simple_spinner_dropdown_item);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerGameMode.setAdapter(mAdapter);
        mClearData.setOnClickListener(this);

        mSpinnerGameMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                if (list.get(pI).equals("Time Win")) {
                    mp.GameMode = 1;
                } else if (list.get(pI).equals("Score Win")) {
                    mp.GameMode = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> pAdapterView) {

            }
        });
    }


    public void initData() throws RemoteException {
        mp = (MyApplication) getApplication();
        mSoundSwitch.setChecked(mp.aidl.isMute());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_data:
                try {
                    mp.aidl.initScore();
                    AppWidgetManager manager = AppWidgetManager.getInstance(this);
                    ComponentName provider = new ComponentName(this, RankWidget.class);
                    int[] ids = manager.getAppWidgetIds(provider);
                    manager.notifyAppWidgetViewDataChanged(ids, R.id.rank_list);
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            default:
                break;
        }
    }
}
