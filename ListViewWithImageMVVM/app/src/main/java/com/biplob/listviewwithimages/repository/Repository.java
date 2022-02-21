package com.biplob.listviewwithimages.repository;

import com.biplob.listviewwithimages.api.ApiCallInterface;
import com.biplob.listviewwithimages.models.CharacterList;

import io.reactivex.Observable;

public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call getResponse api
     * */
    public Observable<CharacterList> getResponse() {
        return apiCallInterface.getResponse();
    }

    /*
     * method to call getResponse api for more response
     * */
    public Observable<CharacterList> getMoreResponse(String pageNumber) {
        return apiCallInterface.getMoreResponse(pageNumber);
    }
}
