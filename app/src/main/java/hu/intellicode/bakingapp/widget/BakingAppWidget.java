package hu.intellicode.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.ui.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 * Tutorial used for immediate widget update: https://code.tutsplus.com/tutorials/code-a-widget-for-your-android-app-updating-your-widget--cms-30528
 */
public class BakingAppWidget extends AppWidgetProvider {

    static SharedPreferences prefs;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        prefs = context.getSharedPreferences(
                "hu.intellicode.bakingapp", Context.MODE_PRIVATE);

        String recipeNameText = prefs.getString("recipe_name", "My Recipe");
        String ingredientsText = prefs.getString("ingredients", "Add a recipe to the widget!");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, recipeNameText);
        views.setTextViewText(R.id.appwidget_ingredients, ingredientsText);

        //opens app on widget click
        Intent intent = new Intent(context, RecipesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_ingredients, pendingIntent);

        Intent intentUpdate = new Intent(context, BakingAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

