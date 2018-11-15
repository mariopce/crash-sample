package com.test.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lding on 27/05/2016.
 */
public class LoadingView extends RelativeLayout {

    @BindView(R.id.txt_message_loading)
    TextView mMessage;

    private Unbinder mUnbinder;

    public LoadingView(Context context) {
        super(context);
        init(null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_loading, this, true);
        mUnbinder = ButterKnife.bind(this);
        setClickable(true);

        setupMessage(attrs);
    }

    private void setupMessage(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray attrList =
                getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        String message = attrList.getString(R.styleable.LoadingView_android_text);
        message(message);
        int defaultColor = UIUtils.getColor(getContext(), android.R.attr.textColorPrimary);
        int color = attrList.getColor(R.styleable.LoadingView_android_textColor, defaultColor);
        mMessage.setTextColor(color);
        attrList.recycle();
    }

    public LoadingView message(String message) {
        mMessage.setText(message);
        return this;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
