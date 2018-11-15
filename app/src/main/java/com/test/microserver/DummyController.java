package com.test.microserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.test.LockActivity;

import java.util.ArrayList;
import java.util.List;

public class DummyController {

    private final Context mContext;
    private final List<Callback> mCallbacks;
    private final Class<? extends Activity> mLockActivity;
    private boolean mLocked = false;
    private boolean mIsConnected;
    private boolean misRunning;

    private volatile static DummyController sInstance;

    public DummyController(Context context) {
        mCallbacks = new ArrayList<>();
        mLockActivity = LockActivity.class;
        mContext = context;
    }

    public static DummyController getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DummyController.class) {
                if (sInstance == null) {
                    sInstance = new DummyController(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void lock() {
        if (!mLocked) {
            Intent intent = new Intent(mContext, mLockActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public void setLock(boolean locked) {
        mLocked = locked;
        if(!locked) {
            mIsConnected = false;
            misRunning = false;
        }
    }

    public boolean isMicroServerConnected() {
        return mIsConnected;
    }

    public void removeCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    public boolean isMicroServerRunning() {
        return misRunning;
    }

    public void start() {
        for (Callback cb : new ArrayList<>(mCallbacks)) {
            mIsConnected = true;
            misRunning = true;
            cb.onMSConnected();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disconnect();
            }
        }, 5000);
    }

    public void disconnect() {
        for (Callback cb : new ArrayList<>(mCallbacks)) {
            cb.onMSDisconnected();
        }
    }

    public interface Callback {

        void onMSConnected();

        void onMSDisconnected();

        void onMSError(int error, String msg);

    }

    public void addCallback(Callback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }
}
