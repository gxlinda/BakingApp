package hu.intellicode.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;
    @SerializedName("steps")
    private ArrayList<Step> steps;
    @SerializedName("servings")
    private Integer servings;
    @SerializedName("image")
    private String image;

    //This empty constructor is needed by the Parceler library
    public Recipe() {
    }

    public Recipe(Integer id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel pc) {
            return new Recipe(pc);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Ctor from Parcel, reads back fields IN THE ORDER they were written
     */
    private Recipe(Parcel pc) {
        this.id = pc.readInt();
        this.name = pc.readString();
        this.ingredients = new ArrayList<>();
        pc.readArrayList(Ingredient.class.getClassLoader());
        this.steps = new ArrayList<>();
        pc.readArrayList(Step.class.getClassLoader());
        this.servings = pc.readInt();
        this.image = pc.readString();
    }
}
