package com.zhb.my2048;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zhb.my2048.service.RankWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RankWidget extends AppWidgetProvider {


    private RemoteViews mRv;
    private Intent mIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            mIntent =  new Intent(context, RankWidgetService.class);
            mRv = new RemoteViews(context.getPackageName(), R.layout.rank_widget);
            mRv.setRemoteAdapter(R.id.rank_list, mIntent);
            mRv.setEmptyView(R.id.rank_list,R.id.widget_empty);
            Intent viewIntent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, viewIntent, 0);
            mRv.setOnClickPendingIntent(R.id.list_row, pi);
            mRv.setPendingIntentTemplate(R.id.rank_list, pi);
            manager.updateAppWidget(appWidgetId, mRv);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}