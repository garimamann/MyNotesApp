package com.example.mynotes.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.*;
import android.util.Base64;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 */

public class StringFormatterUtils {

    /**
     * Variables
     */

    private static StringFormatterUtils instance;
    private String TAG = StringFormatterUtils.class.getSimpleName();

    private StringFormatterUtils() {
    }

    public static synchronized StringFormatterUtils getInstance() {
        return instance == null ? instance = new StringFormatterUtils()
                : instance;
    }

    public interface OnStylizedTextClicked {
        public void onTextClicked();
    }

    /**
     * This method is used to  underline text
     *
     * @param textToUnderLine set underline text
     * @return
     */
    public SpannableString underLineText(String originalText, String textToUnderLine) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.indexOf(textToUnderLine);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToUnderLine.length();
        spanStr.setSpan(new UnderlineSpan(), startIndex, endIndex, 0);

        return spanStr;
    }

    /**
     * This method is used to  underline text
     *
     * @param textToUnderLine set underline text
     * @return
     */
    public SpannableString underLineText(SpannableString text, String textToUnderLine) {
        String originalText = String.valueOf(text);
        int startIndex = originalText.indexOf(textToUnderLine);
        if (startIndex == -1) {
            return text;
        }
        int endIndex = startIndex + textToUnderLine.length();
        text.setSpan(new UnderlineSpan(), startIndex, endIndex, 0);

        return text;
    }

    /**
     * This method is used to make particular text bold,italic etc
     *
     * @param textToStylize set underline text
     * @return
     */
    public SpannableString makeTextStylized(SpannableString originalText, String textToStylize, int typeface) {
        String originalTextValue = String.valueOf(originalText);
        int startIndex = originalTextValue.indexOf(textToStylize);
        if (startIndex == -1) {
            return originalText;
        }
        int endIndex = startIndex + textToStylize.length();
        StyleSpan boldSpan = new StyleSpan(typeface);
        originalText.setSpan(boldSpan, startIndex, endIndex, 0);

        return originalText;
    }

    /**
     * This method is used to make particular text bold,italic etc
     *
     * @param textToStylize set underline text
     * @return
     */
    public SpannableString makeTextStylized(String originalText, String textToStylize, int typeface) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.indexOf(textToStylize);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToStylize.length();
        StyleSpan boldSpan = new StyleSpan(typeface);
        spanStr.setSpan(boldSpan, startIndex, endIndex, 0);

        return spanStr;
    }

    /**
     * This method is used to make text colorized
     *
     * @param textToColorize set color to text
     * @return
     */
    public SpannableString makeTextColorized(String originalText, String textToColorize, int color) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.indexOf(textToColorize);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToColorize.length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spanStr.setSpan(colorSpan, startIndex, endIndex, 0);

        return spanStr;
    }

    /**
     * This method is used to make text colorized
     *
     * @param textToColorize set color to text
     * @return
     */
    public SpannableString makeTextColorized(Spanned originalText, String textToColorize, int color) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.toString().indexOf(textToColorize);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToColorize.length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spanStr.setSpan(colorSpan, startIndex, endIndex, 0);

        return spanStr;
    }

    /**
     * This method is used to make text colorized
     *
     * @param textToColorize set color to text
     * @return
     */
    public SpannableString makeTextColorizedAndLarge(Context context, String originalText, String textToColorize, int color) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.indexOf(textToColorize);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToColorize.length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spanStr.setSpan(boldSpan, startIndex, endIndex, 0);
        spanStr.setSpan(new RelativeSizeSpan(1.2f), startIndex, endIndex, 0);
        spanStr.setSpan(colorSpan, startIndex, endIndex, 0);


        return spanStr;
    }

    /**
     * This method is used to make text clickable
     *
     * @param textToMadeClickable set text as clickable
     * @return
     */
    public SpannableString makeTextClickable(Context context, TextView textView, String originalText, String textToMadeClickable, int color, OnStylizedTextClicked listener) {
        SpannableString spanStr = new SpannableString(originalText);
        int startIndex = originalText.indexOf(textToMadeClickable);
        if (startIndex == -1) {
            return spanStr;
        }
        int endIndex = startIndex + textToMadeClickable.length();
        CustomSpan customClickableSpan = new CustomSpan(context, color, listener);
        spanStr.setSpan(customClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return spanStr;

    }

    /**
     * This method is used to make text clickable
     *
     * @param textToMadeClickable set text as clickable
     * @return
     */
    public SpannableString makeTextClickable(Context context, TextView textView, SpannableString originalText, String textToMadeClickable, int color, OnStylizedTextClicked listener) {
        String originalTextValue = String.valueOf(originalText);
        int startIndex = originalTextValue.indexOf(textToMadeClickable);
        if (startIndex == -1) {
            return originalText;
        }
        int endIndex = startIndex + textToMadeClickable.length();
        CustomSpan customClickableSpan = new CustomSpan(context, color, listener);
        originalText.setSpan(customClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return originalText;

    }

    /**
     * This method is used to format currency according to locale
     */
    public String formatCurrency(Locale locale, String currency) {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        String formattedCurrency = defaultFormat.format(currency);
        return formattedCurrency;
    }

    /**
     * This method is mask password wih a different character
     */
    public void maskPasswordCharacter(EditText editText, Character maskedCharacter) {
        editText.setTransformationMethod(new PasswordTransformationUtils(maskedCharacter));
    }

    /**
     * This method is used to mask a mobile number
     */
    public String maskMobileNumber(String number) {
        if (number != null && (!TextUtils.isEmpty(number))) {
            String formattedNumber = number.replaceAll("\\b(\\d{2})\\d+(\\d{3})", "$1*******$2");
            return formattedNumber;
        } else {
            return null;
        }

    }


    /**
     * Custom class to make text clickable
     */

    class CustomSpan extends ClickableSpan {
        private Context mContext;
        private int mTextColor;
        private OnStylizedTextClicked callback;

        public void onClick(View textView) {
            callback.onTextClicked();
        }

        public CustomSpan(Context context, int textColor, OnStylizedTextClicked listener) {
            mContext = context;
            callback = listener;
            mTextColor = textColor;
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            if (mTextColor != -1) {
                ds.setColor(mTextColor);
            }
        }
    }

    /**
     * Custom class to mask password with a different character
     */
    public class PasswordTransformationUtils extends PasswordTransformationMethod {
        Character mMaskedCharacter;

        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        public PasswordTransformationUtils(Character maskedCharacter) {
            mMaskedCharacter = maskedCharacter;
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return mMaskedCharacter; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }

    }

    /**
     * capitalize first letter
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        ValidationsUtils mValidationsUtils = ValidationsUtils.getInstance();
        if (mValidationsUtils.isEmptyOrNull(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        ValidationsUtils mValidationsUtils = ValidationsUtils.getInstance();
        if (!mValidationsUtils.isEmptyOrNull(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * This method is used to encodeBytearraytoBase64 String
     *
     * @param bytearray set bytearray to encode
     * @return Base64 String
     */
    public String encodeBase64(byte[] bytearray) {
        return Base64.encodeToString(bytearray, 0, bytearray.length,
                Base64.DEFAULT);
    }


    /**
     * This method is used to encodeBytearraytoBase64 String
     *
     * @param textToBeEncoded set text to encode
     * @return Base64 String
     */
    public String encodeBase64(String textToBeEncoded) {
        return Base64.encodeToString(textToBeEncoded.getBytes(), 0, textToBeEncoded.length(),
                Base64.DEFAULT);
    }

    /**
     * This method is used to return byte array from base64 String
     *
     * @param base64string Set base64String to decode
     * @return bytearray
     */
    public String decodeBase64(String base64string) {
        return Base64.decode(base64string, Base64.DEFAULT).toString();
    }

    /**
     * Get SHA 256
     *
     * @param text
     * @param length
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */

    public static String encodeToSHA256(String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();

        if (length > result.toString().length()) {
            resultStr = result.toString();
        } else {
            resultStr = result.toString().substring(0, length);
        }

        return resultStr;

    }

    /**
     * This method is used to create encrypted message packet to send
     */


    private JSONObject encodeStringToJson(String[] key, String[] value) {
        JSONObject obj = new JSONObject();
        int length = key.length;
        for (int i = 0; i < length; i++) {
            try {
                obj.put(key[i], value[i]);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return obj;
    }

    public String createXML(String[] keys, String[] values) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xmlSerializer.setOutput(writer);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag("", keys[0]);
            for (int count = 1; count < keys.length; count++) {
                xmlSerializer.startTag("", keys[count]);
                xmlSerializer.text(values[count]);
                xmlSerializer.endTag("", keys[count]);

            }
            // end DOCUMENT
            xmlSerializer.endTag("", keys[0]);
            xmlSerializer.endDocument();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();

    }


    public String formatPhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        } else {
            //Deprecated method
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

    public String returnDigitsOnly(String phoneNumber) {
        String numberOnly = phoneNumber.replaceAll("[^0-9]", "");
        return numberOnly;
    }

    public static String escapeJavaString(String st) {
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
                    String unicode = String.valueOf(c);
                    int code = (int) c;
                    if (!(code >= 0 && code <= 255)) {
                        unicode = "\\\\u" + Integer.toHexString(c);
                    }
                    builder.append(unicode);
                } else {
                    builder.append(c);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString();
    }

}
