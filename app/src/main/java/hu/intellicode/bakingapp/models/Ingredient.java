package hu.intellicode.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private Double quantity;

    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    //This empty constructor is needed by the Parceler library
    public Ingredient() {
    }

    public Ingredient(Double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeValue(quantity);
        parcel.writeValue(measure);
        parcel.writeValue(ingredient);
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel pc) {
            return new Ingredient(pc);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * Ctor from Parcel, reads back fields IN THE ORDER they were written
     */

    private Ingredient(Parcel pc) {
        quantity = pc.readDouble();
        measure = pc.readString();
        ingredient = pc.readString();
    }


}
