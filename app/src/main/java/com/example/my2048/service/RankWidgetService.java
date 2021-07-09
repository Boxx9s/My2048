package com.example.my2048.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.my2048.R;
import com.example.my2048.base.MyApplication;
import com.example.my2048.model.Score;
import com.example.my2048.util.SQLiteHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RankWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent pIntent) {
        return new RankRemoteViewsFactory(this, pIntent);
    }


    private class RankRemoteViewsFactory implements RemoteViewsFactory{
        private Context mContext;
        private RemoteViews mRemoteViews;
        private ArrayList<Score> mScores = new ArrayList<>();
        public MyApplication mp;
        private List<Map<String, String>> mMapList;

        public RankRemoteViewsFactory(Context pContext, Intent pIntent){
            mContext = pContext;
        }

        @Override
        public void onCreate() {
            String sql = "select * from " + Score.class.getSimpleName();
            mMapList = SQLiteHelper.with(RankWidgetService.this).query(sql);
            for (Map<String, String> map : mMapList) {
                Score a = new Score();
                a.setMscore(map.get("mScore"));
                a.setPlayer(map.get("player"));
                mScores.add(a);
            }
            mScores.sort(new Comparator<Score>() {
                @Override
                public int compare(Score x, Score y) {
                    return Integer.parseInt(y.getMscore()) - Integer.parseInt(x.getMscore());
                }
            });
        }

        @Override
        public RemoteViews getViewAt(int pI) {
            mRemoteViews = new RemoteViews(getPackageName(), R.layout.list_item);
            mRemoteViews.setTextViewText(R.id.item_text, mScores.get(pI).getMscore());
            mRemoteViews.setTextViewText(R.id.item_player, mScores.get(pI).getPlayer());
            return mRemoteViews;
        }
        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mMapList.size();
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int pI) {
            return pI;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
