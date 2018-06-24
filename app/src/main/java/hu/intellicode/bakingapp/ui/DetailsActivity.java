package hu.intellicode.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.helper.RecipeData;
import hu.intellicode.bakingapp.models.Recipe;
import hu.intellicode.bakingapp.widget.BakingAppWidgetProvider;

public class DetailsActivity extends AppCompatActivity implements IngredientsAndStepsFragment.OnStepListItemSelected {

    private boolean mTwoPane;
    private Recipe chosenRecipe;
    public Bundle bundle = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.recipe_toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpdate = new Intent(DetailsActivity.this, BakingAppWidgetProvider.class);
                intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidgetProvider.class));
                intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intentUpdate);
                Snackbar.make(view, "Ingredients added to widget!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState != null) {
            mTwoPane = savedInstanceState.getBoolean("PANE");
            chosenRecipe = savedInstanceState.getParcelable("RECIPE");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(chosenRecipe.getName());
            }
            return;
        }

        chosenRecipe = RecipeData.recipe;
        if (chosenRecipe != null) {
            bundle.putParcelableArrayList("INGREDIENTS", chosenRecipe.getIngredients());
            bundle.putParcelableArrayList("STEPS", chosenRecipe.getSteps());
            bundle.putString("RECIPE_NAME", chosenRecipe.getName());
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(chosenRecipe.getName());
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane == true){
                    onBackPressed();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    if (mTwoPane == false && findViewById(R.id.frame_single_step) != null) {
                        fm.popBackStack(); //goes back to ingredients and steps fragment
                    } else {
                        //goes back to recipe list screen
                        finish();
                        Intent intent = new Intent(DetailsActivity.this, RecipesActivity.class);
                        RecipeData.recipe = null;
                        startActivity(intent);
                    }
                }
            }
        });

        if (findViewById((R.id.two_pane_layout)) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                SingleStepFragment singleStepFragment = new SingleStepFragment();
                singleStepFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.single_step_fragment, singleStepFragment)
                        .commit();

                IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
                ingredientsAndStepsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.ingredients_and_steps_fragment, ingredientsAndStepsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            mTwoPane = false;

            FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
            ingredientsAndStepsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_and_steps_fragment, ingredientsAndStepsFragment)
                    .commit();
        }
    }

    // Define the behavior for onStepListItemSelected
    @Override
    public void onStepListItemSelected(int listIndex) {
        Toast.makeText(this, "Position clicked = " + listIndex, Toast.LENGTH_SHORT).show();
        RecipeData.stepIndex = listIndex;
        chosenRecipe = RecipeData.recipe;

        if (mTwoPane) {
            SingleStepFragment newSingleStepFragment = new SingleStepFragment();
            newSingleStepFragment.setListIndex(listIndex);
            bundle.putParcelableArrayList("STEPS", chosenRecipe.getSteps());
            newSingleStepFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.single_step_fragment, newSingleStepFragment)
                    .commit();
        } else {
            SingleStepFragment newSingleStepFragment = new SingleStepFragment();
            newSingleStepFragment.setListIndex(listIndex);
            bundle.putParcelableArrayList("STEPS", chosenRecipe.getSteps());
            newSingleStepFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_ingredients_and_steps, newSingleStepFragment)
                    .addToBackStack("STEP_STACK")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        RecipeData.stepIndex = 0;
        if (mTwoPane) {
            finish();
            super.onBackPressed();
        } else if (findViewById(R.id.frame_single_step) != null) {
            getSupportFragmentManager().popBackStack();
        } else if (findViewById(R.id.frame_ingredients_and_steps) != null) {
            finish();
            Intent intent = new Intent(this, RecipesActivity.class);
            RecipeData.recipe = null;
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putBoolean("PANE", mTwoPane);
        currentState.putParcelable("RECIPE", chosenRecipe);
    }
}