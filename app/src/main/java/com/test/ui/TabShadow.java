package com.test.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.test.R;

/**
 * Created by lding on 15/07/2016.
 */
public class TabShadow extends LinearLayout {

    private int mSelectedIndex;

    private int mSelectColour;

    public TabShadow(Context context) {
        super(context);
        init(null);
    }

    public TabShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabShadow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        mSelectedIndex = -1;
        if (attrs == null) {
            return;
        }
        TypedArray attrList =
                getContext().obtainStyledAttributes(attrs, R.styleable.TabShadow);
        mSelectColour = attrList.getColor(R.styleable.TabShadow_selectColor, 0);
        attrList.recycle();
    }

    public void setTabCount(int count) {
        removeAllViews();
        setWeightSum(count);
        for (int i = 0; i < count; i++) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
            addView(view);
        }
    }

    public void onTabSelect(int index) {
        if (mSelectColour == 0) {
            // if the select colour is not set, do nothing
            return;
        }
        if (index >= getChildCount() || index < 0) {
            // if the index is invalid, do nothing
            return;
        }
        if (index == mSelectedIndex) {
            // if the new one has already been selected, do nothing
            return;
        }
        View oldSelected = getChildAt(mSelectedIndex);
        if (oldSelected != null) {
            oldSelected.setBackgroundColor(UIUtils.getColor(getContext(), android.R.color.transparent));
        }
        View newSelected = getChildAt(index);
        newSelected.setBackgroundColor(mSelectColour);
        mSelectedIndex = index;
    }
}
