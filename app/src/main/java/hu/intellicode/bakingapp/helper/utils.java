package hu.intellicode.bakingapp.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import hu.intellicode.bakingapp.adapters.RecipeAdapter;
import hu.intellicode.bakingapp.models.Recipe;
import hu.intellicode.bakingapp.networking.ApiConnection;
import hu.intellicode.bakingapp.networking.RecipeParsing;
import hu.intellicode.bakingapp.ui.RecipesActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class utils {

    private static final String TAG = "RecipesActivity";

    private static RecipeAdapter recipeAdapter;

    //Checks if device is connected to the internet or not

    public static boolean isConnectedToInternet(Context context) {
        boolean isConnectedToInternet = false;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null; //line suggested by Lint
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        isConnectedToInternet = networkInfo != null && networkInfo.isConnected();
        return isConnectedToInternet;
    }

    //downloads recipes from the url with Retrofit

    public static void loadRecipes(final Context context) {
        RecipeParsing recipeParsing = ApiConnection.getRecipe();
        recipeParsing.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ArrayList<Recipe> recipes = new ArrayList<>();
                    recipes.addAll(response.body());
                    Log.d("Utils", "Recipes loaded from API: " + recipes);

                    RecipesActivity recipesActivity = (RecipesActivity) context;
                    recipesActivity.setRecipeList(recipes);
                    recipesActivity.setProgressBarVisibility(View.GONE);

                } else {
                    Log.d(TAG, "Status code : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error loading data from API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error loading from API" + t.getMessage());
                RecipeData.success = false;
            }
        });
    }
}
