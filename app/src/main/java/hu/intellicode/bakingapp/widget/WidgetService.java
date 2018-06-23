package hu.intellicode.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.helper.RecipeData;
import hu.intellicode.bakingapp.models.Ingredient;

public class WidgetService extends RemoteViewsService {

    private int appWidgetId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }

    public class WidgetRemoteViewsFactory implements RemoteViewsFactory {

        Context context;
        private ArrayList<Ingredient> ingredients;
        private Ingredient ingredient;

        WidgetRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
        }

        //load data for the list
        @Override
        public void onDataSetChanged() {
            ingredients = RecipeData.recipe.getIngredients(); //does it have the data? how to check?
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            ingredient = ingredients.get(position);
            RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.widget_list_item);
            remoteView.setTextViewText(R.id.widget_qty, String.valueOf(ingredient.getQuantity()));
            remoteView.setTextViewText(R.id.widget_measure, ingredient.getMeasure());
            remoteView.setTextViewText(R.id.widget_ingredient, ingredient.getIngredient());
            return remoteView;
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
