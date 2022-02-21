package com.biplob.listviewwithimages.ui.mainactivity;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.biplob.listviewwithimages.api.ApiResponse;
import com.biplob.listviewwithimages.models.CharacterList;
import com.biplob.listviewwithimages.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {


    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private Repository repository;

    public MainActivityViewModel() {
    }

    public MainActivityViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> response() {
        return responseLiveData;
    }

    /*
     * method to call normal getResponse api
     * */
    public void getResponse() {

        disposables.add(repository.getResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable d) throws Exception {
                        responseLiveData.setValue(ApiResponse.loading());
                    }
                })
                .subscribe(
                        new Consumer<CharacterList>() {
                            @Override
                            public void accept(CharacterList result) throws Exception {
                                responseLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                responseLiveData.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }



    /*
     * method to call normal getResponse api
     * */
    public void getMoreResponse(String pageNumber) {

        Log.e("getMoreResponse","is calling");

        new CompositeDisposable().add(repository.getMoreResponse(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable d) throws Exception {
                        responseLiveData.setValue(ApiResponse.loading());
                    }
                })
                .subscribe(
                        new Consumer<CharacterList>() {
                            @Override
                            public void accept(CharacterList result) throws Exception {
                                responseLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                responseLiveData.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }



    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
