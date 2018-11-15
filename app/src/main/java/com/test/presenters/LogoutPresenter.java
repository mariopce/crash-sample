package com.test.presenters;

import android.content.Context;

import com.test.framework.BasePresenter;

public class LogoutPresenter extends BasePresenter {

    private final ILogoutOperation mOperations;

    public LogoutPresenter(Context context, ILogoutOperation operation) {
        super(context);
        mOperations = operation;
    }
}
