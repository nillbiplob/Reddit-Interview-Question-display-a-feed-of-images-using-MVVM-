package com.biplob.listviewwithimages;

import com.biplob.listviewwithimages.ui.mainactivity.MainActivityFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivityFragment mainActivityFragment);

}
