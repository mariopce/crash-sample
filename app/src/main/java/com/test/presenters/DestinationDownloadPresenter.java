package com.test.presenters;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.test.framework.BasePresenter;


public class DestinationDownloadPresenter extends BasePresenter {
    private final Fragment fragment;
    private final IDestinationDownloadOperation mOperation;

    public DestinationDownloadPresenter(Fragment fragment, IDestinationDownloadOperation operation) {
        super(fragment.getActivity());
        this.fragment = fragment;
        mOperation = operation;
    }
}
