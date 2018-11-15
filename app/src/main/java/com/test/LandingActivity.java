package com.test;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.framework.BaseActivity;
import com.test.framework.BasePresenter;
import com.test.microserver.DummyController;

public class LandingActivity extends BaseActivity {

    private static final String FLAG_NO_SPLASH = "flag_no_splash";
    private static DummyController.Callback microserverCallback;
    private static ActiveActivityCallback activityLifecycleCallback;
    private static Class<?> currentActivity = LandingActivity.class;

    public static void start(Context context) {
        start(context, new Intent(context, LandingActivity.class));
    }

    public static void startLastActivity(Context context) {
        start(context, new Intent(context, currentActivity));
    }

    private static void start(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        if (savedInstanceState == null) {
            startMicroserver();
            startLanding();
        }
    }

    private void startLanding() {
        MainActivity.start(this);
    }

    private void startMicroserver() {
        final Context context = getApplicationContext();
        final DummyController microServer = getMicroServerController();
        microServer.addCallback(createMicroserverCallback(context, microServer));
        addActivityLifecycleCallback();
        if (!microServer.isMicroServerRunning()) {
            // simulate connection in 5 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            microServer.start();
                        }
                    });
                }
            }, 5000);

        }
    }

    private void addActivityLifecycleCallback() {
        if (activityLifecycleCallback == null) {
            activityLifecycleCallback = new ActiveActivityCallback() {
                @Override
                public void onActivityStarted(Activity activity) {
                    if (activity.getClass() != LockActivity.class) {
                        currentActivity = activity.getClass();
                    }
                }
            };
            getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallback);
        }
    }

    @NonNull
    private DummyController.Callback createMicroserverCallback(final Context context,
                                                                     final DummyController microServer) {
        if (microserverCallback == null) {
            microserverCallback = new DummyController.Callback() {
                @Override
                public void onMSConnected() {
                    microServer.lock();
                }

                @Override
                public void onMSDisconnected() {
                    LandingActivity.startLastActivity(context);
                }

                @Override
                public void onMSError(int error, String msg) {
                }
            };
        }
        return microserverCallback;
    }

    private class ActiveActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
