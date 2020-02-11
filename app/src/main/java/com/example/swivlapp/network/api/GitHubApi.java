package com.example.swivlapp.network.api;

import com.example.swivlapp.entriesmodel.User;
import com.example.swivlapp.entriesmodel.UserDetail;

import java.util.List;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApi {

    @GET("users")
    Single<List<User>> getUsers(@Query("since") long userId, @Query("per_page") int perPage);

    @GET("users/{name}")
    Single<UserDetail> getUserInfo(@Path("name") String name);

}
