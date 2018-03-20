package com.domain.train;

import android.app.Application;


/**
 * Created by Ilya on 08.03.2018.
 */

public class App extends Application {



    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static synchronized App getInstance() {
        return mInstance;
    }
}
