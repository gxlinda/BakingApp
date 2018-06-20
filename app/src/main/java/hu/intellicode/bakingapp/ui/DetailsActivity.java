package hu.intellicode.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.helper.RecipeData;
import hu.intellicode.bakingapp.models.Ingredient;
import hu.intellicode.bakingapp.models.Recipe;
import hu.intellicode.bakingapp.widget.BakingAppWidget;

public class DetailsActivity extends AppCompatActivity implements IngredientsAndStepsFragment.OnStepListItemSelected{

    private boolean mTwoPane;
    private Recipe chosenRecipe;
    public Bundle bundle = new Bundle();
    public SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        prefs = this.getSharedPreferences(
                "hu.intellicode.bakingapp", Context.MODE_PRIVATE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIngredientsToSharedPref(chosenRecipe);
                Snackbar.make(view, "Ingredients added to widget!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intentUpdate = new Intent(DetailsActivity.this, BakingAppWidget.class);
                intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
                intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intentUpdate);
            }
        });

        if (savedInstanceState != null) {
            mTwoPane = savedInstanceState.getBoolean("PANE");
            chosenRecipe = savedInstanceState.getParcelable("RECIPE");
            return;
        }

        chosenRecipe = RecipeData.recipe;
        if ( chosenRecipe != null) {
            bundle.putParcelableArrayList("INGREDIENTS", chosenRecipe.getIngredients());
            bundle.putParcelableArrayList("STEPS", chosenRecipe.getSteps());
            bundle.putString("RECIPE_NAME", chosenRecipe.getName());
        }

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

    private void saveIngredientsToSharedPref(Recipe chosenRecipe) {
        String recipeName = chosenRecipe.getName();
        ArrayList ingredientList = chosenRecipe.getIngredients();
        StringBuilder ingredientsString = new StringBuilder();
        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = (Ingredient) ingredientList.get(i);
            ingredientsString.append(String.valueOf(ingredient.getQuantity()));
            ingredientsString.append("\u0020 " + ingredient.getMeasure());
            ingredientsString.append("\u0020 " + ingredient.getIngredient() + "\n");
        }

        prefs.edit().putString("recipe_name", recipeName).apply();;
        prefs.edit().putString("ingredients", String.valueOf(ingredientsString)).apply();

    }

    // Define the behavior for onStepListItemSelected
    @Override
    public void onStepListItemSelected(int listIndex) {
        Toast.makeText(this, "Position clicked = " + listIndex, Toast.LENGTH_SHORT).show();
        RecipeData.stepIndex = listIndex;
        chosenRecipe = RecipeData.recipe;

        if(mTwoPane){
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
                    .addToBackStack("SINGLE_STEP_FRAG")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        RecipeData.stepIndex = 0;
        if (mTwoPane){
            super.onBackPressed();
        } else if (findViewById(R.id.frame_single_step) != null) {
            getSupportFragmentManager().popBackStack();
        } else if (findViewById(R.id.frame_ingredients_and_steps) != null){
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