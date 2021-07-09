package com.example.my2048;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.my2048.service.RankWidgetService;
import com.example.my2048.service.WidgetUpdateService;

/**
 * Implementation of App Widget functionality.
 */
public class RankWidget extends AppWidgetProvider {


    private RemoteViews mRv;
    private Intent mIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        context.startService(new Intent(context, WidgetUpdateService.class));
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            mIntent =  new Intent(context, RankWidgetService.class);
            mRv = new RemoteViews(context.getPackageName(), R.layout.rank_widget);
            mRv.setRemoteAdapter(R.id.rank_list, mIntent);
            mRv.setEmptyView(R.id.rank_list,R.id.widget_empty);
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