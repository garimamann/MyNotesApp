package com.example.mynotes.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.Serializable;


public class PermissionUtils {
    /**
     * Clasess
     */
    private static PermissionUtils instance = new PermissionUtils();
    private PermissionHandlerListener mListener;


    /**
     * Variables
     */

    private String mPermission;
    private String mMessage;
    private int REQUEST_CODE;
    private static Context mContext;
    private static boolean mFirstTime;


    /**
     * Interface to handle the success when a permission is granted
     */
    public interface PermissionHandlerListener extends Serializable {
        void onPermissionGranted(int requestCode, String permission);
    }

    /**
     * Defauklt Constructor
     */
    private PermissionUtils() {
    }

    /**
     * Constructor to initialize the singleton class
     *
     * @param context
     * @return
     */
    public static synchronized PermissionUtils getInstance(Context context) {
        mContext = context;
        mFirstTime = true;
        return instance == null ? instance = new PermissionUtils()
                : instance;
    }

    /**
     * Method to set the listener and handle dynamic permissions
     *
     * @param message
     * @param permission
     * @param requestCode
     * @param listener
     */
    public void setListener(String message, String permission, int requestCode, PermissionHandlerListener listener) {
        // Setting the values for the instance variables
        mPermission = permission;
        mListener = listener;
        REQUEST_CODE = requestCode;
        mMessage = message;
        // Checking the current API version of the device
        int currentAPIVersion = Build.VERSION.SDK_INT;
        // Check if Android version of the device is equal to or greater than Marshmallow then  prompt user to allow access to read and write external storage.
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, mPermission) != PackageManager.PERMISSION_GRANTED) {
                // Check if the rationale should be shown or not
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mPermission)) {
                    // The rationale always comes as false for the first time so handling that condition
                    if (mFirstTime) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermission}, REQUEST_CODE);
                    }
                    // If the show rationale comes as false, show an alert to the user that the permissions are necessary
                    else {
                        mFirstTime = false;
                        showPermissionDeniedAlertDialog(mContext, mMessage);
                    }
                }
                // Else if the show rationale is true, just show the request permission dialog to the user
                else {
                    mFirstTime = false;
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermission}, REQUEST_CODE);
                }
            }
            // If the permission has already been granted just call the success listener
            else {
                mFirstTime = false;
                mListener.onPermissionGranted(REQUEST_CODE, mPermission);

            }
        }
        // If the version is less than Marshmallow, just call the success listener
        else {
            mFirstTime = false;
            mListener.onPermissionGranted(REQUEST_CODE, mPermission);
        }


    }


    /**
     * Method called as a callback when a permission is granted or denied
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // Check if the result is greater than 0 and the permission is granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mListener.onPermissionGranted(REQUEST_CODE, mPermission);
        } else {
            //Show the alert if the user has denied the request
            showPermissionDeniedAlertDialog(mContext, mMessage);

        }

    }

    /**
     * Alert user with the importance of allowing permission.
     *
     * @param context
     */
    private void showPermissionDeniedAlertDialog(final Context context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(message + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // If the rationale can be shown directly open the request permission dialog
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mPermission)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{mPermission}, REQUEST_CODE);
                }
                // else open the settings screen of the app
                else {
                    ShareUtils mShareUtils = ShareUtils.getInstance();
                    mShareUtils.openAppSettingsScreen(context);
                }
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


}
