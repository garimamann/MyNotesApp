package com.example.mynotes.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;


/**
 *
 */

public class ToastUtils {

    /**
     * Variables
     */
    private static ToastUtils instance = new ToastUtils();
    private Toast mToast;
    private static View mView;

    private ToastUtils() {
    }

    public static synchronized ToastUtils getInstance() {
        return instance == null ? instance = new ToastUtils()
                : instance;
    }

    /**
     * This method is used to show toast     *
     *
     * @param message message to show in toast
     */
    public void toast(Context context, final String message) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * This method is used to show toast
     *
     * @param message message to show in toast
     */
    public void toast(Context context, final int message) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();

    }

//    /**
//     * This method is used to show Snackbar
//     *
//     * @param message message to show in Snackbar
//     */
//
//    public void showSnackBar(Context context, final String message, boolean isError) {
//
//        if (mView != null && context != null) {
//            Snackbar snackbar = Snackbar
//                    .make(mView, message, Snackbar.LENGTH_LONG);
//            View snackbarView = snackbar.getView();
//            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setMaxLines(20);
//            if (!isError) {
//                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.snack_bar_color));
//            } else {
//                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.snack_bar_error_color));
//            }
//            snackbar.show();
//        } else {
//            if (null != mToast) {
//                mToast.cancel();
//            }
//            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//            mToast.show();
//
//        }
//    }
//
//    /**
//     * This method is used to show Snackbar
//     *
//     * @param message message to show in Snackbar
//     */
//
//    public void showSnackBar(Context context, final int message, boolean isError) {
//
//        if (mView != null && context != null) {
//            Snackbar snackbar = Snackbar
//                    .make(mView, message, Snackbar.LENGTH_LONG);
//            View snackbarView = snackbar.getView();
//            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setMaxLines(20);
//            if (!isError) {
//                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.snack_bar_color));
//            } else {
//                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.snack_bar_error_color));
//            }
//            snackbar.show();
//        } else {
//            if (null != mToast) {
//                mToast.cancel();
//            }
//            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//            mToast.show();
//        }
//    }

    public void getCurrentView(View view) {
        mView = view;
    }
}
