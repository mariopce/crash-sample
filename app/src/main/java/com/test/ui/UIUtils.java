package com.test.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lding on 28/04/2016.
 */
public class UIUtils {

    public static void setupSpinnerContent(Spinner spinner, String[] content) {
        setupSpinnerContent(spinner, Arrays.asList(content));
    }

    public static void setupSpinnerContent(Spinner spinner, List<String> content) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(), android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static int getItemPosition(Spinner spinner, String item) {
        Adapter adapter = spinner.getAdapter();
        if (adapter == null) {
            throw new NullPointerException("Spinner doesn't have adapter, please set adapter first");
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            String str = (String) adapter.getItem(i);
            if (str.equals(item)) {
                return i;
            }
        }
        return 0;
    }

    public static String getText(@NonNull EditText editText) {
        return editText.getText().toString();
    }

    public static String getText(@NonNull Spinner spinner) {
        Object selectedItem = spinner.getSelectedItem();
        return selectedItem == null ? null : selectedItem.toString();
    }

    public static AlertDialog.Builder dialogBuilder(Context context, String title,
                                                    DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, listener);
    }

    /**
     * Get the color from the resId. The type of resId can be either "attr" or "color"
     *
     * @param context
     * @param resId
     *
     * @return
     */
    public static int getColor(Context context, int resId) {
        Resources resources = context.getResources();
        String type = resources.getResourceTypeName(resId);
        if ("attr".equalsIgnoreCase(type)) {
            int[] attrs = new int[] {resId};
            TypedArray a = context.obtainStyledAttributes(resId, attrs);
            int color = a.getColor(0, 0);
            a.recycle();
            return color;
        }
        else if ("color".equalsIgnoreCase(type)) {
            return ContextCompat.getColor(context, resId);
        }
        throw new IllegalStateException("Make sure the resId is from either R.attr or R.color");
    }

    public static Bitmap getBitmap(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            if (bitmap != null) {
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
