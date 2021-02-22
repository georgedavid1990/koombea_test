package com.koombea.testjorge.rest;

import com.koombea.testjorge.rest.result.DataResult;
import com.koombea.testjorge.rest.result.PostResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostsClient {

    @GET(Endpoints.POSTS)
    Call<DataResult> getPosts();
}
