package com.example.mynotes.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.*;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/*
 * ************************************************************************************************************************************************
 *
 * This is a reusable Utility class for Share functions. The code has been done here and can be used multiple times in the project
 *
 * @author
 *
 *******************************************************************************************************************************************
 */

public final class ShareUtils {

    /**
     * Variables
     */

    private static ShareUtils instance;
    private String GOOGLE_DOCS_LINK = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
    private Context mContext;

    private ShareUtils()

    {

    }

    public interface OnPageLoadedListener {

        public void onPageLoaded();

    }


    public static synchronized ShareUtils getInstance() {

        return instance == null ? instance = new ShareUtils()
                : instance;
    }

    public boolean checkSMSSendAvailablity(Context context) {
        mContext = context;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return true;
        } else {
            return false;
        }
    }

    public void sendMessage(Context context, String message, String number) {
        mContext = context;
        ToastUtils mAlertDialogUtils = ToastUtils.getInstance();
        if ((((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()
                == TelephonyManager.PHONE_TYPE_NONE) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                == null) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()
                == TelephonyManager.NETWORK_TYPE_UNKNOWN)) {
            mAlertDialogUtils.toast(context, "No Sim Available");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
            {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);

                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                context.startActivity(sendIntent);

            } else //For early versions, do what worked for you before.
            {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", message);
                sendIntent.putExtra("address", number);
                context.startActivity(sendIntent);
            }
        }

    }


    public void callNumber(Context context, String number) {
        mContext = context;

        ToastUtils mAlertDialogUtils = ToastUtils.getInstance();
        if ((((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()
                == TelephonyManager.PHONE_TYPE_NONE) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                == null) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()
                == TelephonyManager.NETWORK_TYPE_UNKNOWN)) {
            mAlertDialogUtils.toast(context, "No Sim Available");
        } else {

            if (!ValidationsUtils.getInstance().isEmptyOrNull(number)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + number));
                context.startActivity(intent);
            } else {
                mAlertDialogUtils.toast(context, "No Number Available");
            }
        }
    }

    /**
     * Used to send email from the application.
     *
     * @param subject of the email
     * @param content to send
     * @param sendTo  recipient of the email
     */
    public void sendEmail(String subject, String content, String sendTo,
                          Context context) {
        mContext = context;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");

        if (!ValidationsUtils.getInstance().isEmptyOrNull(sendTo)) {
            intent.putExtra(Intent.EXTRA_EMAIL, sendTo);
            intent.setData(Uri.parse("mailto:" + sendTo));

        } else {
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.setData(Uri.parse("mailto:" + ""));
        }
        if (!ValidationsUtils.getInstance().isEmptyOrNull(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!ValidationsUtils.getInstance().isEmptyOrNull(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        try {

            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            LogUtils.e("Email error:", e.toString());
        }
    }

    /**
     * Used to send email from the application.
     *
     * @param subject of the email
     * @param content to send
     * @param sendTo  recipient of the email
     */
    public void sendEmailWithAttachment(String subject, String content, String sendTo,
                                        Context context, File file) {
        Uri u = null;
        u = Uri.fromFile(file);
        mContext = context;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");

        if (!ValidationsUtils.getInstance().isEmptyOrNull(sendTo)) {
            intent.putExtra(Intent.EXTRA_EMAIL, sendTo);
            intent.setData(Uri.parse("mailto:" + sendTo));

        } else {
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.setData(Uri.parse("mailto:" + ""));
        }
        if (!ValidationsUtils.getInstance().isEmptyOrNull(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!ValidationsUtils.getInstance().isEmptyOrNull(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
        }
        if (u != null) {
            intent.putExtra(Intent.EXTRA_STREAM, u);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        try {

            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            LogUtils.e("Email error:", e.toString());
        }
    }

    /**
     * Used to send SMS from the application. User can also send long text
     * message using this method
     *
     * @param phoneNumber of the used to whom the message to be sent
     * @param message     to send
     */
    public void sendLongSMS(Context context, String phoneNumber, String message) {
        mContext = context;
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
                null);
    }

    public void shareOnWhatsapp(Context context, String message) {
        mContext = context;
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            ToastUtils.getInstance().toast(context, "Whatsapp not installed on device!");
        }
    }

    /**
     * ***********************************************************************************************
     * <p/>
     * <p/>
     * This is the function used to open the webview
     * <p/>
     * <p/>
     * ************************************************************************************************
     */
    public void openWebView(final Context context, WebView mWebview, String Url, final Dialog mCustomProgressWheel) {
        boolean openInGoogleDocs = false;
        String urlToLoad;
        SharedPreferencesUtils mSharedPreferencesUtils = SharedPreferencesUtils.getInstance(context);
        if (!ValidationsUtils.getInstance().isEmptyOrNull(Url)) {
            openInGoogleDocs = ValidationsUtils.getInstance().isGoogleDocsSupported(Url);
            if (openInGoogleDocs) {
                urlToLoad = GOOGLE_DOCS_LINK + Url;
            } else {
                urlToLoad = Url;
            }
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.getSettings().setDisplayZoomControls(false);
            mWebview.getSettings().setBuiltInZoomControls(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.loadUrl(urlToLoad);
            mWebview.setWebViewClient(new WebViewClient() {
                                          public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                              if (url.toLowerCase().startsWith("mailto:")) {
                                                  //Handle mail Urls
                                                  context.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                                              } else if (url.toLowerCase().startsWith("tel:")) {
                                                  //Handle telephony Urls
                                                  context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                                              } else {
                                                  boolean openInGoogleDocs = false;
                                                  String urlToLoad;
                                                  openInGoogleDocs = ValidationsUtils.getInstance().isGoogleDocsSupported(url);
                                                  if (openInGoogleDocs) {
                                                      urlToLoad = GOOGLE_DOCS_LINK + url;
                                                  } else {
                                                      urlToLoad = url;
                                                  }
                                                  view.loadUrl(urlToLoad);
                                              }
                                              return true;
                                          }

                                          @Override
                                          public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                              handler.proceed(); // Ignore SSL certificate errors
                                              super.onReceivedSslError(view, handler, error);
                                          }


                                          @Override
                                          public void onPageFinished(WebView view, String url) {
                                              mCustomProgressWheel.dismiss();

                                          }

                                          public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                              mCustomProgressWheel.show();

                                          }

                                      }


            );

        }
    }


    public void openIntentChooser(Context context, String shareSubject, String shareContent, String shareURL) {

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        emailIntent.setType("message/rfc822");

        PackageManager pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");


        Intent openInChooser = Intent.createChooser(emailIntent, shareSubject);

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {

                emailIntent.setPackage(packageName);
            } else if (packageName.contains("com.twitter.android") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("whatsapp")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                intent.setType("text/plain");
                if (packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                } else if (packageName.contains("whatsapp")) {
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                } else if (packageName.contains("facebook")) {
                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                    // will show the <meta content ="..."> text from that page with our link in Facebook.
                    intent.putExtra(Intent.EXTRA_TEXT, shareURL);
//                    intent= new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("http://www.facebook.com/dialog/feed?app_id=YOUR_APP_ID&redirect_uri=REDIRECT_URI"));
                } else if (packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                }
//                else if (packageName.contains("android.gm")) { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
//                    intent.putExtra(Intent.EXTRA_TEXT, content);
//                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                    intent.setType("message/rfc822");
//                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        context.startActivity(openInChooser);
    }


    /**
     * This method is used to open the Settings screen of the app to handle the runtime permissions in case they are denied
     *
     * @param context
     */
    public void openAppSettingsScreen(final Context context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public Bitmap takeAppScreenshot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public interface MessageTrackingListener {
        public void onMessageSent();

        public void onMessageNotSent(String error);

        public void onMessageDelivered();

        public void onMessageNotDelivered();

    }

    /**
     * Method to send SMS programmatically
     *
     * @param context
     * @param phoneNumber
     * @param message
     * @param listener
     */
    public void sendSMSProgammatically(final Context context, String phoneNumber, String message,
                                       final MessageTrackingListener listener) {
        ToastUtils mAlertDialogUtils = ToastUtils.getInstance();
        if ((((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()
                == TelephonyManager.PHONE_TYPE_NONE) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                == null) || (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()
                == TelephonyManager.NETWORK_TYPE_UNKNOWN)) {
            // mAlertDialogUtils.toast(context, R.string.validation_no_sim_available);
            listener.onMessageNotSent("No Sim Available");
        } else {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);

            // ---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            listener.onMessageSent();

                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                            listener.onMessageNotSent("Generic failure");
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            listener.onMessageNotSent("No service");

                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            listener.onMessageNotSent("Null PDU");
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            listener.onMessageNotSent("Radio off");
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            // ---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            listener.onMessageDelivered();
                            break;
                        case Activity.RESULT_CANCELED:
                            listener.onMessageNotDelivered();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
    }

    /**
     * ***********************************************************************************************
     * <p/>
     * <p/>
     * This is the function used to open Google Maps app in Play Store
     * <p/>
     * <p/>
     * ************************************************************************************************
     */
    public void openAppInPlayStore(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}
