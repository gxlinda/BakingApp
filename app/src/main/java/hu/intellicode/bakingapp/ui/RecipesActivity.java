package hu.intellicode.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.adapters.RecipeAdapter;
import hu.intellicode.bakingapp.helper.RecipeData;
import hu.intellicode.bakingapp.helper.SimpleIdlingResource;
import hu.intellicode.bakingapp.helper.utils;
import hu.intellicode.bakingapp.models.Recipe;

//In the making of this project I used Udacity materials provided for Android Developer Nanodegree


public class RecipesActivity extends AppCompatActivity implements
        RecipeAdapter.OnItemClickListener {

    private static final String TAG = "RecipesActivity";
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private RecyclerView recipeRecyclerView;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private GridLayoutManager layoutManager;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Toolbar toolbar = findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        //Assign the views
        recipeRecyclerView = findViewById(R.id.rv_recipes);
        progressBar = findViewById(R.id.progress_bar);
        emptyStateTextView = findViewById(R.id.tv_empty_state);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        int spanCount;

        //At portrait mode there will be 1 column in the grid, in landscape mode there will be 2.
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 1;
        } else {
            spanCount = 2;
        }

        // If there is no internet, it updates empty state with no connection error message
        if (!utils.isConnectedToInternet(this)) {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(R.string.no_internet_connection);

            // If there is internet connection, it loads data from json
        } else {
            Context context = this;
            emptyStateTextView.setVisibility(View.GONE);
            recipeRecyclerView.setVisibility(View.VISIBLE);
            layoutManager = new GridLayoutManager(this, spanCount);
            recipeRecyclerView.setLayoutManager(layoutManager);
            utils.loadRecipes(this);
        }
    }

    //sets recipelist to adapter after loading from json is done
    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        recipeAdapter = new RecipeAdapter(recipeList);
        recipeAdapter.setRecipeList(recipeList);
        recipeAdapter.setOnItemClickListener(this);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onItemClick(int position, Recipe recipe, ImageView imageView) {
        Intent intent = new Intent(RecipesActivity.this, DetailsActivity.class);
        RecipeData.recipe = recipe;
        startActivity(intent);
    }

    public void setProgressBarVisibility(int gone) {
        progressBar.setVisibility(View.GONE);
    }
}
