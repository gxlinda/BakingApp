package hu.intellicode.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hu.intellicode.bakingapp.R;
import hu.intellicode.bakingapp.models.Recipe;

//Adapter used to accomodate Recyclerview of Recipes

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, Recipe recipe, ImageView imageView);
    }

    public List<Recipe> recipeList;
    private OnItemClickListener listener;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.cardview_recipe, viewGroup, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapterViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Recipe recipe = recipeList.get(position);

        String imageUrl = recipe.getImage();
        if (imageUrl!="") {
            Picasso.with(context)
                    .load(recipe.getImage())
                    .placeholder(context.getResources().getDrawable(R.drawable.cupcake))
                    .error(context.getResources().getDrawable(R.drawable.cupcake))
                    .into(holder.recipeImageView);
        } else holder.recipeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cupcake));

        holder.recipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (null == recipeList) return 0;
        return recipeList.size();
    }

    // Define viewholder
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView recipeImageView;
        TextView recipeName;

        public RecipeAdapterViewHolder(final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview_recipe);
            recipeImageView = itemView.findViewById(R.id.iv_recipe_image);
            recipeName = itemView.findViewById(R.id.tv_recipe_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int pos = getAdapterPosition();
                Recipe recipe = recipeList.get(pos);
                listener.onItemClick(pos, recipe, recipeImageView);
            }
        }

    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        this.listener = listener;
    }

    // Helper method to set the actual recipes list into the recyclerview on the activity
    public void setRecipeList(ArrayList<Recipe> recipes) {
        recipeList = recipes;
        notifyDataSetChanged();
    }

}
