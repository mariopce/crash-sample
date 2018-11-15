package com.test.presenters;

import android.content.Context;

import com.test.framework.BasePresenter;

/**
 * Created by lding on 16/05/2016.
 */
public class HelpCenterPresenter extends BasePresenter {

    private final IHelpCenterOperation mOperation;


    public HelpCenterPresenter(Context context, IHelpCenterOperation operation) {
        super(context);
        mOperation = operation;
    }
}
