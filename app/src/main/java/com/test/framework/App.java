package com.test.framework;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.test.ui.Utils;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (!Utils.isLollipopCompatible()) {
            MultiDex.install(this);
        }
    }
}
