package com.biplob.listviewwithimages.ui.mainactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.biplob.listviewwithimages.MyApplication;
import com.biplob.listviewwithimages.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_activity);

        MainActivityFragment mainActivityFragment = MainActivityFragment.newInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mainActivityFragment)
                    .commitNow();
        }
        ((MyApplication) getApplication()).getAppComponent().doInjection(mainActivityFragment);

    }
}
