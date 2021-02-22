package com.koombea.testjorge.rest;

import com.koombea.testjorge.rest.result.DataResult;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private final PostsClient postsClient;

    private static RestClient restClient;

    public  static RestClient getRestClient() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private RestClient(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Endpoints.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.
                client(okHttpClient)
                .build();

        postsClient = retrofit.create(PostsClient.class);
    }

    // Fetch posts.
    public Call<DataResult> getPosts(){
        return postsClient.getPosts();
    }

}
