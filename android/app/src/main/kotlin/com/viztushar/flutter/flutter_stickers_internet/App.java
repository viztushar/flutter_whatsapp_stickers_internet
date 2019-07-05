package com.viztushar.flutter.flutter_stickers_internet;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.CallSuper;

import com.fxn.stash.Stash;

import io.flutter.view.FlutterMain;

public class App extends Application {

    private Activity mCurrentActivity = null;

    public App() {
    }

    @CallSuper
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        FlutterMain.startInitialization(this);
    }

    public Activity getCurrentActivity() {
        return this.mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}
