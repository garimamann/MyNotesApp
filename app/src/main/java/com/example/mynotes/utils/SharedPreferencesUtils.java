package com.example.mynotes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.*;

/***************************************************************************
 * @author <p>
 *         This is SharedPreferences SingleTon Class used to store small amount
 *         of data in private mode
 ***************************************************************************/
public class SharedPreferencesUtils {
    private static SharedPreferencesUtils instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static final String PREF_NAME = "TEST_PROJECT";

    /****************************************************************************
     * @param context Parameterized Constructor is called
     ***************************************************************************/
    public static SharedPreferencesUtils getInstance(Context context) {

        return instance == null ? instance = new SharedPreferencesUtils(context)
                : instance;
    }

    /****************************************************************************
     * Parameterized Constructor is called
     ***************************************************************************/
    private SharedPreferencesUtils(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    /****************************************************************************
     * Default Constructor that make this class Singleton by taking this
     * Constructor as private
     ***************************************************************************/
    private SharedPreferencesUtils() {
        // TODO Auto-generated constructor stub
    }

    /****************************************************************************
     * Here a boolean value is inserted into the preference
     ***************************************************************************/
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /****************************************************************************
     * Here a string value is inserted into the preference
     ***************************************************************************/
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /****************************************************************************
     * Here an integer value is inserted into the preference
     ***************************************************************************/
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /****************************************************************************
     * Here a long value is inserted into the preference
     ***************************************************************************/

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /****************************************************************************
     * Here a float value is inserted into the preference
     ***************************************************************************/
    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    // public void putObject(String key, Object value) {
    // Gson gson = new Gson();
    // String values = gson.toJson(value);
    // putString(key, values);
    // }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();

    }

    /*****************************************************************************
     * Here the preference is cleared
     ****************************************************************************/

    public void clearPref() {
        editor.clear();
        editor.commit();
    }

    /****************************************************************************
     * Here all values is to get String value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return string
     ***************************************************************************/
    public String getString(String key, String defaultvalue) {
        return pref.getString(key, defaultvalue);
    }

    /****************************************************************************
     * Here all values is to get int value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defValue give here defaultValue if not found defalutValue is assigned
     * @return int
     ***************************************************************************/
    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    /****************************************************************************
     * Here all values is to get boolean value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defValue give here defaultValue if not found defalutValue is assigned
     * @return boolean
     ***************************************************************************/
    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    /****************************************************************************
     * Here all values is to get long valued from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defValue give here defaultValue if not found defalutValue is assigned
     * @return long
     ***************************************************************************/
    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    /****************************************************************************
     * Here all values is to get float value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defValue give here defaultValue if not found defalutValue is assigned
     * @return float
     ***************************************************************************/
    public float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

//    /****************************************************************************
//     * This method is used to get the saved value from the shared preferences
//     *
//     * @param key         retrieve by unique key
//     * @param objectClass pass class of object
//     * @return object
//     ***************************************************************************/
    // public Object getObject(String key, Class<?> objectClass) {
    // String re = getString(key, null);
    // if (re == null)
    // throw new RuntimeException("Invalid value");
    // return new Gson().fromJson(re, objectClass);
    // }
    public void writeObjectOnSharedPreference(String prefKey, Object object) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        ObjectOutputStream objectOutput;
        try {
            objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(object);
            byte[] data = arrayOutputStream.toByteArray();
            objectOutput.close();
            arrayOutputStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
            b64.write(data);
            b64.close();
            out.close();

            putString(prefKey, new String(out.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObjectFromSharedPreference(String prefkey) {

        Object object = null;
        byte[] bytes = getString(prefkey, "{}").getBytes();
        if (bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        Base64InputStream base64InputStream = new Base64InputStream(byteArray,
                Base64.DEFAULT);
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(base64InputStream);

            object = in.readObject();
            in.close();
        } catch (StreamCorruptedException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return object;
    }

    public void putObject(String key, Object object) {
        writeObjectOnSharedPreference(key, object);
    }

    public Object getObject(String key, Object object) throws Exception {
        if (pref.contains(key)) {
            return readObjectFromSharedPreference(key);
        } else {
            return object;
        }

    }

    public boolean containsKey(String key) {
        boolean isContain = false;
        if (pref.contains(key)) {
            isContain = true;
        }

        return isContain;
    }

}
