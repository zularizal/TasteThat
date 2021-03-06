package com.stoyanivanov.tastethat.network;

import com.stoyanivanov.tastethat.network.models.NextImagesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public interface QwantAPI {

    @GET("images?count=30")
    Call<NextImagesResponse> getNextImages(@Query("q") String searchedString,
                                           @Query("offset") int offset);
}
