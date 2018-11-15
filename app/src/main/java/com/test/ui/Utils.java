package com.test.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mohammad Haseeb (mohammad.haseeb@intelematics.com.au
 * on 21/10/2015.
 */
public class Utils {

    public static void hideKeyboard(Context context) {
        showKeyboard(context, false);
    }

    public static void showKeyboard(Context context) {
        showKeyboard(context, true);
    }

    private static void showKeyboard(Context context, boolean show) {
        if (!(context instanceof Activity)) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        View currentFocus = ((Activity) context).getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        if (show) {
            inputManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT);
        } else {
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void call(Context context, String number) {
        if (!TextUtils.isEmpty(number)) {
            try {
                String deformatNumber = deformatNumber(number);
                String encodedNumber =
                        URLEncoder.encode(deformatNumber, Charset.defaultCharset().name());
                launchExternalApp(context,
                        new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + encodedNumber)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private static String deformatNumber(String number) {
        if (number == null) {
            return null;
        }
        return number.replaceAll("\\s|\\(|\\)", "").trim();
    }

    public static void openLink(Context context, String link) {
        String httpLink = link;
        if (!TextUtils.isEmpty(httpLink)) {
            if (!httpLink.startsWith("http://") && !httpLink.startsWith("https://")) {
                httpLink = "http://".concat(httpLink);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpLink));
            launchExternalApp(context, intent);
        }
    }

    /**
     * Launches the external app
     *
     * @param context
     * @param intent
     */
    private static void launchExternalApp(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // do nothing
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int EOF = -1;

    public static byte[] readBytesFromStream(InputStream input)
            throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[DEFAULT_BUFFER_SIZE];
        int i;
        while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != EOF) {
            buffer.write(arrayOfByte, 0, i);
        }
        input.close();
        buffer.flush();
        byte[] result = buffer.toByteArray();
        buffer.close();
        return result;
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        return formatter.format(new Date());
    }

    public static boolean isLollipopCompatible() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}