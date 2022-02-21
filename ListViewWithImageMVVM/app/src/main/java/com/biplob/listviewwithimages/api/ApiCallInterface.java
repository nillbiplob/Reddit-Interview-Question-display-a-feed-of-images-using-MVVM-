package com.biplob.listviewwithimages.api;

import com.biplob.listviewwithimages.models.CharacterList;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiCallInterface {

    @GET("character/")
    Observable<CharacterList> getResponse();

    @GET("character/")
    Observable<CharacterList>  getMoreResponse(@Query("page") String pageNo);
}
