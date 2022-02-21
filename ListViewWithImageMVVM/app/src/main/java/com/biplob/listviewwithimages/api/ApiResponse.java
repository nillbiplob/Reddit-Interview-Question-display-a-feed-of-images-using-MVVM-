package com.biplob.listviewwithimages.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.biplob.listviewwithimages.enums.Status;
import com.biplob.listviewwithimages.models.CharacterList;

import java.util.Observable;


public class ApiResponse extends Observable {

    public final Status status;

    @Nullable
    public final CharacterList data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable CharacterList data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(Status.LOADING, null, null);
    }

    public static ApiResponse success(@NonNull CharacterList data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(Status.ERROR, null, error);
    }

}
