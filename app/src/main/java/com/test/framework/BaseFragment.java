package com.test.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.test.ui.LoadingView;

import butterknife.Unbinder;

/**
 * Created by lding on 12/04/2016.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    private P mPresenter;

    protected abstract P initPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onPostCreate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
        }
        super.onStop();
    }

    public void stop() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void unbind() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).unbind();
        }
    }

    public Unbinder getUnbinder() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getUnbinder();
        }
        return null;
    }

    public void setUnbinder(Unbinder unbinder) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setUnbinder(unbinder);
        }
    }

    protected P getPresenter() {
        if (mPresenter == null) {
            throw new NullPointerException("No presenter available for this fragment.");
        }
        return mPresenter;
    }

    protected void showError(TextInputLayout inputLayout, String error) {
        if (inputLayout != null) {
            boolean hasError = !TextUtils.isEmpty(error);
            inputLayout.setErrorEnabled(hasError);
            inputLayout.setError(error);
            if (hasError) {
                inputLayout.requestFocus();
            }
        }
    }

    protected void showLoading(String message) {
        if (getLoadingView() != null) {
            getLoadingView().message(message).show();
        }
    }

    protected void hideLoading() {
        if (getLoadingView() != null) {
            getLoadingView().hide();
        }
    }

    private LoadingView getLoadingView() {
        if (getContext() instanceof LoadingSpinnerProvider) {
            return ((LoadingSpinnerProvider) getContext()).getLoadingSpinner();
        }
        return null;
    }
}
