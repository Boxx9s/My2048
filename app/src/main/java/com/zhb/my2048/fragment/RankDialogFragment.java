package com.zhb.my2048.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;


import androidx.fragment.app.DialogFragment;

import com.zhb.my2048.R;
import com.zhb.my2048.adapter.RankDialogFragmentListAdapter;
import com.zhb.my2048.model.Score;
import com.zhb.my2048.util.SQLiteHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author zhb
 */
public class RankDialogFragment extends DialogFragment {
    Context mContext;
    ListView mFragmentRankListview;
    private List<Map<String, String>> mMapList;
    private ArrayList<Score> mScores = new ArrayList<>();
    public RankDialogFragment(Context pContext){
        mContext = pContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.rankdialogfragment, container);
        mFragmentRankListview = view.findViewById(R.id.fragment_rank_list);
        getData();
        RankDialogFragmentListAdapter mRankDialogFragmentListAdapter =
                new RankDialogFragmentListAdapter(mContext,mScores);
        mFragmentRankListview.setAdapter(mRankDialogFragmentListAdapter);
        return view;
    }
    private void getData(){
        String sql = "select * from " + Score.class.getSimpleName();
        mMapList = SQLiteHelper.with(mContext).query(sql);
        for (Map<String, String> map : mMapList) {
            Score a = new Score();
            a.setScore(map.get("mScore"));
            a.setPlayer(map.get("player"));
            a.setTime(Integer.parseInt(map.get("time")));
            mScores.add(a);
        }
        mScores.sort(new Comparator<Score>() {
            @Override
            public int compare(Score x, Score y) {
                return Integer.parseInt(y.getScore()) - Integer.parseInt(x.getScore());
            }
        });
    }
}
