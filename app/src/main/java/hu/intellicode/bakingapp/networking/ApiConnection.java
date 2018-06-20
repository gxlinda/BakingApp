package hu.intellicode.bakingapp.networking;

public class ApiConnection {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static RecipeParsing getRecipe() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeParsing.class);
    }
}
