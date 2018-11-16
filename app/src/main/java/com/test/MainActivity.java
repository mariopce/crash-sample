package com.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.fragments.DestinationDownloadFragment;
import com.test.fragments.HelpCenterFragment;
import com.test.fragments.SettingsFragment;
import com.test.framework.BackPressHandler;
import com.test.framework.BaseActivity;
import com.test.framework.BasePresenter;
import com.test.framework.LoadingSpinnerProvider;
import com.test.ui.LoadingView;
import com.test.ui.TabShadow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoadingSpinnerProvider, TabLayout.OnTabSelectedListener {

    private static final String KEY_SELECTED_TAB = "selected_tab";

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.tabs_shadow)
    TabShadow mTabsShadow;

    @BindView(R.id.view_loading)
    LoadingView mLoadingSpinner;

    private int mSelectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));

        setSupportActionBar(mToolbar);

        setupTabs();
        tryRestoreTab(savedInstanceState);

//        MicroServerController msController = getMicroServerController();
//        if (msController != null && msController.isMicroServerRunning()) {
//            msController.checkLockState();
//        }
    }

    private void setupTabs() {
        mTabs.addOnTabSelectedListener(this);
        // Destination download
        mTabs.addTab(createTab(
                R.drawable.ic_destination_download,
                R.string.tab_destination_download,
                DestinationDownloadFragment.class), false);
        // Help centre
        mTabs.addTab(createTab(
                R.drawable.ic_help,
                R.string.tab_help,
                HelpCenterFragment.class), false);
        // Settings
        mTabs.addTab(createTab(
                R.drawable.ic_settings,
                R.string.tab_settings,
                SettingsFragment.class), false);
        // setup shadow
        mTabsShadow.setTabCount(mTabs.getTabCount());
    }

    private TabLayout.Tab createTab(int icon, int text, Object tag) {
        TabLayout.Tab tab = mTabs.newTab()
                .setCustomView(R.layout.view_tab)
                .setIcon(icon)
                .setText(text)
                .setTag(tag);
        View tabView = tab.getCustomView();
        if (tabView == null) {
            throw new IllegalStateException("Tab view cannot be created");
        }
        View textView = tabView.findViewById(android.R.id.text1);
        if (textView instanceof TextView) {
            ((TextView) textView).setTextColor(mTabs.getTabTextColors());
        }

        //Setting the mID for drawable images for appium testing
        View imgView = tabView.findViewById(android.R.id.icon);
        imgView.setId(icon);

        // custom tab view in the TabLayout will somehow always be set as selected, so deselect it
        tabView.setSelected(false);
        return tab;
    }

    private void tryRestoreTab(Bundle savedState) {
        if (savedState != null) {
            mSelectedTab = savedState.getInt(KEY_SELECTED_TAB);
        }
        TabLayout.Tab selected = mTabs.getTabAt(mSelectedTab);
        if (selected != null) {
            selected.select();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // update the title
        setTitle(tab.getText());
        /*
         * we don't want to create a new one every time. Instead, the activity will try to find
         * the previous fragment and simply attach it on the screen.
         */
        Fragment f = getFragmentFromTab(tab);
        if (f == null) {
            addFragment(R.id.fragment_container, (Class<?>) tab.getTag(), null);
        } else if (!isFragmentShowing(f, R.id.fragment_container)) {
            attachFragment(f);
        }
        // update shadow
        mTabsShadow.onTabSelect(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab == null) {
            return;
        }
        // when unselected the tab, the fragment needs to be detached.
        Fragment f = getFragmentFromTab(tab);
        if (f != null) {
            detachFragment(f);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Fragment f = getFragmentFromTab(tab);
    }

    private Fragment getFragmentFromTab(TabLayout.Tab tab) {
        Object tag = tab.getTag();
        if (tag instanceof Class<?>) {
            return getFragment(((Class<?>) tag).getName());
        }
        return null;
    }

    @Nullable
    @Override
    protected BackPressHandler getBackPressHandler() {
        Fragment current = getFragment(R.id.fragment_container);
        if (current instanceof BackPressHandler) {
            return (BackPressHandler) current;
        }
        return super.getBackPressHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSelectedTab = mTabs.getSelectedTabPosition();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_TAB, mSelectedTab);
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    @Override
    public LoadingView getLoadingSpinner() {
        return mLoadingSpinner;
    }

    @Override
    protected void setTransactionAnimations(FragmentTransaction transaction) {
        super.setTransactionAnimations(transaction);
        transaction.setCustomAnimations(android.R.anim.fade_in, 0);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

}
