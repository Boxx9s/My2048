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

import androidx.appcompat.app.AppCompatActivity;

import com.zhb.my2048.fragment.MakeSureDialog;
import com.zhb.my2048.base.MyApplication;

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
                        mp.mRankistAIDL.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: " + mp.mRankistAIDL.isMute());
                    } else {
                        mp.mRankistAIDL.setMute(pB);
                        mp.isMute = pB;
                        Log.i("TAG", "onCheckedChanged: " + mp.mRankistAIDL.isMute());
                    }
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            }
        };

        mSoundSwitch.setOnCheckedChangeListener(listener);
        mSpinnerGameMode = findViewById(R.id.game_mode_spinner);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_item, R.layout.support_simple_spinner_dropdown_item);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerGameMode.setAdapter(mAdapter);
        mClearData.setOnClickListener(this);
        try {
            mSpinnerGameMode.setSelection(mp.mRankistAIDL.getGameMode());
        } catch (RemoteException pE) {
            pE.printStackTrace();
        }
        mSpinnerGameMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                int flag = mp.GameMode;
                if (pI == 1) {
                    mp.GameMode = 1;
                    try {
                        mp.mRankistAIDL.setGameMode(1);
                    } catch (RemoteException pE) {
                        pE.printStackTrace();
                    }
                } else if (pI == 0) {
                    mp.GameMode = 0;
                    try {
                        mp.mRankistAIDL.setGameMode(0);
                    } catch (RemoteException pE) {
                        pE.printStackTrace();
                    }
                }
                if(flag != mp.GameMode){
                    try {
                        mp.mRankistAIDL.initSaveGame();
                    } catch (RemoteException pE) {
                        pE.printStackTrace();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> pAdapterView) {
            }
        });
    }


    public void initData() throws RemoteException {
        mp = (MyApplication) getApplication();
        mSoundSwitch.setChecked(mp.mRankistAIDL.isMute());
    }


    private void showMakeSureDialog() {
        MakeSureDialog dialog = new MakeSureDialog();
        dialog.setContent("确认删除吗？");
        dialog.setDialogClickListener(new MakeSureDialog.onDialogClickListener() {
            @Override
            public void onSureClick() {
                try {
                    mp.mRankistAIDL.initScore();
                    AppWidgetManager manager = AppWidgetManager.getInstance(SettingActivity.this);
                    ComponentName provider = new ComponentName(SettingActivity.this, RankWidget.class);
                    int[] ids = manager.getAppWidgetIds(provider);
                    manager.notifyAppWidgetViewDataChanged(ids, R.id.rank_list);
                } catch (RemoteException pE) {
                    pE.printStackTrace();
                }
            }

            @Override
            public void onCancelClick() {
//这里是取消操作
            }
        });
        dialog.show(getSupportFragmentManager(), "sureDialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_data:
                showMakeSureDialog();
            default:
                break;
        }
    }
}
