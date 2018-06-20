package hu.intellicode.bakingapp.networking;

import java.util.ArrayList;

import hu.intellicode.bakingapp.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeParsing {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
