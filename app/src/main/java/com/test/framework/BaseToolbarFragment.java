package com.test.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.test.R;
import com.test.ui.Utils;

/**
 * Created by lding on 31/05/2016.
 */
public abstract class BaseToolbarFragment<P extends BasePresenter> extends BaseFragment<P> {

    Toolbar mToolbar;
    View mToolbarShadow;

    public abstract View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.fragment_base_toolbar, container, false);
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbarShadow = root.findViewById(R.id.toolbar_shadow);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, mToolbar.getId());
        View content = onCreateContentView(inflater, container, savedInstanceState);
        root.addView(content, 0, params);

        setupToolbar(mToolbar);
        return root;
    }

    protected void setupToolbar(@NonNull Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back);
        int title = getTitle();
        if (title > 0) {
            toolbar.setTitle(title);
        }
        int backgroundColor = getToolbarBackground();
        if (backgroundColor > 0) {
            toolbar.setBackgroundResource(backgroundColor);
        }
    }

    protected int getTitle() {
        return 0;
    }

    protected int getToolbarBackground() {
        return 0;
    }

    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
        mToolbarShadow.setVisibility(View.GONE);
    }
}
