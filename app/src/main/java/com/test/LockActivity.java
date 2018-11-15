package com.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.test.framework.BaseActivity;
import com.test.framework.BasePresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockActivity extends BaseActivity {

    public static final String EXTRA_FORCE_SHOW = "force_show";
    private boolean mForceShow;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        setUnbinder(ButterKnife.bind(this));
        mForceShow = getIntent().getBooleanExtra(EXTRA_FORCE_SHOW, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mForceShow = getIntent().getBooleanExtra(EXTRA_FORCE_SHOW, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMicroServerController().setLock(true);
        getMicroServerController().addCallback(this);

        if (!getMicroServerController().isMicroServerConnected() && !mForceShow) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_FORCE_SHOW, mForceShow);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mForceShow = savedInstanceState.getBoolean(EXTRA_FORCE_SHOW);
    }

    @OnClick(R.id.btn_disconnect)
    public void disconnect() {
        getMicroServerController().disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMicroServerController().setLock(false);
        getMicroServerController().removeCallback(this);
    }

    @Override
    public void onMSDisconnected() {
        finish();
    }

    @Override
    public void onMSError(int error, String msg) {
        finish();
    }

    @Override
    public void onBackPressed() {
        // not allow user hit back
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }
}
