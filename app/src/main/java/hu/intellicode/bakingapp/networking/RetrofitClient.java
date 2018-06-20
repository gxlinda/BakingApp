package hu.intellicode.bakingapp.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitCall = null;

    static Retrofit getClient(String baseUrl) {
        if (retrofitCall == null) {
            retrofitCall = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitCall;
    }
}
