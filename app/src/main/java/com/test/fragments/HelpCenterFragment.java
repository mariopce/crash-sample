package com.test.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.R;
import com.test.framework.BaseFragment;
import com.test.presenters.HelpCenterPresenter;
import com.test.presenters.IHelpCenterOperation;

import butterknife.ButterKnife;

/**
 * Fragment for Help Center. Note RSA stands for Roadside Assist.
 * <p/>
 * Created by lding on 16/05/2016.
 */
public class HelpCenterFragment extends BaseFragment<HelpCenterPresenter> implements IHelpCenterOperation {

    @Override
    protected HelpCenterPresenter initPresenter() {
        return new HelpCenterPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind();
    }
}
