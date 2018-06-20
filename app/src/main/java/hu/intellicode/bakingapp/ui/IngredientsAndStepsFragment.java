package hu.intellicode.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.adapters.StepListAdapter;
import hu.intellicode.bakingapp.models.Ingredient;
import hu.intellicode.bakingapp.models.Step;

public class IngredientsAndStepsFragment extends Fragment {

    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;
    private OnStepListItemSelected mCallback;
    private TextView recipeNameView;
    private TextView ingredientsView;
    private String recipeName;
    RecyclerView stepsView;

    // Mandatory empty constructor
    public IngredientsAndStepsFragment() {
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnStepListItemSelected {
        /**
         * Called by IngredientsAndStepsFragment when a list item is selected
         */
        void onStepListItemSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepListItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    // Inflates the View of ingredients and steps
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        recipeNameView = (TextView) rootView.findViewById(R.id.tv_chosen_recipe_name);
        ingredientsView = (TextView) rootView.findViewById(R.id.tv_ingredients);
        stepsView = (RecyclerView) rootView.findViewById(R.id.rv_step_descriptions);

        if (savedInstanceState != null) {
            ingredientList = savedInstanceState.getParcelableArrayList("INGREDIENTS_SAVED");
            stepList = savedInstanceState.getParcelableArrayList("STEPS_SAVED");
            recipeName = savedInstanceState.getString("RECIPE_NAME_SAVED");

        } else {
            if (getArguments() != null) {
                ingredientList = getArguments().getParcelableArrayList("INGREDIENTS");
                stepList = getArguments().getParcelableArrayList("STEPS");
                recipeName = getArguments().getString("RECIPE_NAME");
            }
        }

        recipeNameView.setText(recipeName);

        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = (Ingredient) ingredientList.get(i);
            ingredientsView.append(String.valueOf(ingredient.getQuantity()));
            ingredientsView.append("\u0020 " + ingredient.getMeasure());
            ingredientsView.append("\u0020 " + ingredient.getIngredient() + "\n");
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        stepsView.setLayoutManager(layoutManager);

        // Create the adapter
        StepListAdapter stepAdapter = new StepListAdapter(stepList, new StepListAdapter.OnStepListItemClickListener() {
            @Override
            public void onStepListItemClick(Step item) {
                //Toast.makeText(getContext(), "Item Clicked: " + item.getId(), Toast.LENGTH_LONG).show();
                mCallback.onStepListItemSelected(item.getId());
            }
        });

        // Set the adapter on the view
        stepsView.setAdapter(stepAdapter);

        return rootView;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList("INGREDIENTS_SAVED", ingredientList);
        currentState.putParcelableArrayList("STEPS_SAVED", stepList);
        currentState.putString("RECIPE_NAME_SAVED", recipeName);
    }
}