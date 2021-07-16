package com.zhb.my2048.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhb.my2048.R;
import com.zhb.my2048.model.Score;

import java.util.ArrayList;

/**
 * @author zhb
 */
public class RankDialogFragmentListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Score> mScores;

    @Override
    public int getCount() {
        return mScores.size();
    }

    @Override
    public Object getItem(int pI) {
        return mScores.get(pI);
    }

    @Override
    public long getItemId(int pI) {
        return pI;
    }

    @Override
    public View getView(int pI, View pView, ViewGroup pViewGroup) {
        Score score = mScores.get(pI);
        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder();
        pView = mInflater.inflate(R.layout.list_item, pViewGroup, false);
        viewHolder.mItem_player = (TextView) pView.findViewById(R.id.item_player);
        viewHolder.mItem_time = (TextView) pView.findViewById(R.id.item_time);
        viewHolder.mItem_score = (TextView) pView.findViewById(R.id.item_score);
        viewHolder.mItem_player.setText(score.getPlayer());
        viewHolder.mItem_time.setText(String.valueOf(score.getTime()));
        viewHolder.mItem_score.setText(score.getScore());
        return pView;
    }


    public RankDialogFragmentListAdapter(Context pContext, ArrayList<Score> pScores) {
        mScores = pScores;
        mContext = pContext;
        mInflater = LayoutInflater.from(pContext);
    }

    static class ViewHolder {
        TextView mItem_player;
        TextView mItem_score;
        TextView mItem_time;
    }
}
