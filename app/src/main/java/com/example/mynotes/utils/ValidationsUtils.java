package com.example.mynotes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * ************************************************************************************************************************************************
 *
 * This is a reusable Utility class for Validations. The code has been done here and can be used multiple times in the project
 *
 *
 *
 *******************************************************************************************************************************************
 */

public final class ValidationsUtils {

    /**
     * Variables
     */
    private String TAG = ValidationsUtils.class.getSimpleName();
    private static ValidationsUtils instance;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String IPADDRESS_STRING_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    public static final String ALPHA_NUMERIC_PATTERN = "\\S*(\\S*([a-zA-Z]\\S*[0-9])|([0-9]\\S*[a-zA-Z]))\\S*";
    // public static final String ALPHA_NUMERIC_PATTERN = "^.*(?=.{6,})(?=.*\\d)(?=.*[a-zA-Z]).*$";
    public static final String ALPHA_NUMERIC_AND_ONE_SPECIAL_CHARACTER_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]$";
    public static final String ALPHA_NUMERIC_AND_ONE_UPPER_CASE_AND_ONE_LOWER_CASE_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]$";
    public static final String ALPHA_NUMERIC_AND_ONE_UPPER_CASE_AND_ONE_LOWER_CASE_AND_ONE_SPECIAL_CHARACTER_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]$";

    public final String SUPPORTED_GOOGLE_DOC_FORMATS[] = {".doc",
            ".docx", ".docm", ".dot", ".dotx", ".dotm", ".html", ".txt",
            ".rtf", ".odt", ".xls", ".xlsx", ".xlsm", ".xlt", ".xltx", ".xltm",
            ".ods", ".csv", ".tsv", ".tab", ".ppt", ".pptx", ".pptm", ".pps",
            ".ppsx", ".ppsm", ".pot", ".potx", ".potm", ".wmf", ".jpg", ".gif",
            ".png", ".pdf"};

    /**
     * Runtime variable
     */
    private Runtime mGarbageCollector = Runtime.getRuntime();


    public ValidationsUtils() {
    }

    public static synchronized ValidationsUtils getInstance() {
        return instance == null ? instance = new ValidationsUtils()
                : instance;
    }

    /**
     * This method is used to detect internet connection.
     * <p/>
     * Returns true is internet is working and else returns false
     *
     * @return connected(boolean)
     */
    public boolean isOnline(Context context) {
        boolean mConnected;
        try {
            LogUtils.e(TAG, "Detect Connection");
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            mConnected = networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected();

        } catch (Exception e) {

            mConnected = false;
            e.printStackTrace();
        }
        LogUtils.e(TAG, "mConnected = " + mConnected);
        return mConnected;

    }


    /**
     * This method is used to detect the screen type in order to apply correct
     * orientation
     *
     * @return boolean true or false
     */
    public boolean isScreenLarge(Activity context) {
        int screenSize = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_UNDEFINED;
    }

    /**
     * This method is used to detect run time memory usuage
     *
     * @return usage memory
     */
    public long maxMemory() {
        mGarbageCollector.gc();
        return (mGarbageCollector.totalMemory() - mGarbageCollector
                .freeMemory()) / (1024 * 1024);

    }


    /**
     * Checks whether the given email address is valid.
     *
     * @param email represents the email address.
     * @return true if the email is valid, false otherwise.
     * @since 09-Feb-2009
     */
    public boolean isEmail(String email) {
        if (email == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isNorwegianEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailPattern = "^([A-Za-z0-9_\\-\\.\\�\\�\\�\\�\\�\\�])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})";
        return email.matches(emailPattern);
    }

    /**
     * Checks whether the given URL (website address) is valid.
     *
     * @param url represents the website address.
     * @return true if the email is valid, false otherwise.
     * @since 09-Feb-2009
     */
    public boolean isURL(String url) {
        if (url == null) {
            return false;
        }

        return Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * Uses androids android.telephony.PhoneNumberUtils to check if an phone
     * number is valid.
     *
     * @param number Phone number to check
     * @return true if the <code>number</code> is a valid phone number.
     */
    public boolean isValidPhoneNumber(String number) {
        if (number == null) {
            return false;
        } else {
            return PhoneNumberUtils.isGlobalPhoneNumber(number);
        }
    }


    /**
     * This method is used to check if the entered string is null, blank, or
     * "null"
     *
     * @param str set String to check
     * @return true if null else false
     */
    public boolean isEmptyOrNull(String str) {
        return !(!TextUtils.isEmpty(str) && !str.toLowerCase().equalsIgnoreCase("null"));
    }


    /**
     * Check application build API.If equal to GINGERBREAD or above return true
     * else false.
     *
     * @return
     */
    public boolean isSdkVersionAboveGingerBread() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

    public boolean isSdkVersionAboveJellyBean() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }


    /**
     * Check if password length is valid
     */
    public boolean isPasswordLenghtValid(String password, int minLength) {
        boolean isValid = false;
        if (isEmptyOrNull(password)) {
            isValid = false;

        } else {
            if (password.trim().length() >= minLength) {
                isValid = true;
            } else {
                isValid = false;
            }
        }
        return isValid;

    }

    /**
     * Check if password length is valid
     */
    public boolean isMinMaxLengthValid(String check, int minLength, int maxLength) {
        boolean isValid = false;
        if (isEmptyOrNull(check)) {
            isValid = false;

        } else {
            int length = check.trim().length();
            if (length >= minLength && length <= maxLength) {
                isValid = true;
            } else {
                isValid = false;
            }
        }
        return isValid;

    }


    /**
     * **************************************************************************
     * <p/>
     * This method is used to check if url is supported by Google Docs
     * <p/>
     * **************************************************************************
     */

    public boolean isGoogleDocsSupported(String url) {
        List<String> match = Arrays
                .asList(SUPPORTED_GOOGLE_DOC_FORMATS);
        boolean isGoogleDocsSupported = false;
        for (int i = 0; i < match.size(); i++) {
            if (!url.endsWith(match.get(i))) {
                isGoogleDocsSupported = false;
            } else {
                isGoogleDocsSupported = true;
                break;
            }
        }
        return isGoogleDocsSupported;
    }


    /**
     * *************************************************************
     * <p/>
     * This method is cused to set the format of the currency according to the
     * locale
     * <p/>
     * *************************************************************
     */

    public String setCurrencyFormat(double price) {
        Locale locale = Locale.getDefault();
        String languageCode;
        DecimalFormat decimalFormat;
        String formattedNumber = null;
        languageCode = locale.getCountry();
        if (TextUtils.equals(languageCode, "NO")) {

            decimalFormat = new DecimalFormat("#,###,##0.00");
            formattedNumber = decimalFormat.format(price);
            // formattedNumber = formattedNumber.replaceAll("\\,", " ");
            // log("value", formattedNumber);
            // formattedNumber = formattedNumber.replace(".", ",");
        } else {
            decimalFormat = new DecimalFormat("0.00");
            formattedNumber = decimalFormat.format(price);

        }

        return formattedNumber;

    }

    public boolean isOnlyZeroes(String checkForZeroes) {
        if (checkForZeroes.matches("[0]+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param password
     * @param confirmPassword that need to match
     * @return true if email address is matched with defined pattern.
     * Here pattern is used from api level 22 android
     */
    public boolean isPasswordAndConfirmPasswordMatching(String password, String confirmPassword) {
        return checkForMatchingStrings(password, confirmPassword);
    }

    public boolean checkForMatchingStrings(String str1, String str2) {
        return TextUtils.equals(str1, str2);
    }

    private boolean isAppInstalledOrNot(Context context, String uri) {

        PackageManager pm = context.getPackageManager();
        boolean isAppInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isAppInstalled = false;
        }
        return isAppInstalled;
    }

    private String setStringValueForJSONObject(JSONObject jsonObject, String value) {

        if (jsonObject != null) {
            if (jsonObject.has(value)) {
                try {
                    return jsonObject.getString(value);
                } catch (JSONException e) {
                    return "";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private Integer setIntValueForJSONObject(JSONObject jsonObject, String value) {

        if (jsonObject != null) {
            if (jsonObject.has(value)) {
                try {
                    return jsonObject.getInt(value);
                } catch (JSONException e) {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private Boolean setBooleanValueForJSONObject(JSONObject jsonObject, String value) {

        if (jsonObject != null) {
            if (jsonObject.has(value)) {
                try {
                    return jsonObject.getBoolean(value);
                } catch (JSONException e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

//    /**
//     * Method to verify google play services on the device
//     */
//    public boolean checkPlayServices(Context context) {
//        ToastUtils mAlertDialogUtils = ToastUtils.getInstance();
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(context);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                mAlertDialogUtils.toast(context, "This device is not supported.");
//
//            }
//            return false;
//        }
//        return true;
//    }

    /**
     * Checks is value is between given range
     *
     * @return true if between range; false if outside of range
     */
    public boolean isValueBetweenTheRange(String value, String startRange, String endRange) {
        if (!isEmptyOrNull(startRange) && (!isEmptyOrNull(endRange)) && (!isEmptyOrNull(value))) {
            double inputedSize = Double.parseDouble(value);
            return inputedSize >= Double.parseDouble(startRange)
                    && inputedSize <= Double.parseDouble(endRange);
        } else
            return false;
    }

    /**
     * Checks if string has anything that isn't numbers.
     *
     * @param str
     * @return false only if null or not numeric
     */
    private boolean containsOnlyNumbers(String str) {
        boolean isOnlyNumbers = true;
        if (isEmptyOrNull(str)) {
            isOnlyNumbers = false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    isOnlyNumbers = false;
                    break;
                }
            }
        }

        return isOnlyNumbers;
    }

    public boolean isIPAddressValid(String ipAddress) {
        if (isEmptyOrNull(ipAddress)) {
            return false;
        } else {
            Pattern IP_ADDRESS_PATTERN;
            IP_ADDRESS_PATTERN = Pattern
                    .compile(IPADDRESS_STRING_PATTERN);
            return IP_ADDRESS_PATTERN.matcher(ipAddress).matches();
        }

    }

    public boolean isAlphaNumericPassword(String password) {
        if (isEmptyOrNull(password)) {
            return false;
        } else {
            Pattern ALPHA_NUMERIC_PATTERN_VALUE;
            ALPHA_NUMERIC_PATTERN_VALUE = Pattern
                    .compile(ALPHA_NUMERIC_PATTERN);
            Matcher matcher = ALPHA_NUMERIC_PATTERN_VALUE.matcher(password);
            if (matcher.matches()) {
                return true;
            } else {
                return false;
            }
        }

    }


}
