package phantasmist.com.colourmemory;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Phantasmist on 13/01/17.
 */

public class AppController extends Application {
    public static SharedPreferences mediapref = null;
    private String PREFERENCE_NAME = "colour_memory";
    private static AppController mInstance;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mediapref = this.getSharedPreferences(PREFERENCE_NAME, 0);
        context=getApplicationContext();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static void storeString(String key, String value) {
        mediapref.edit().putString(key, value).commit();
    }

    public static void storeBoolean(String key, boolean value) {
        mediapref.edit().putBoolean(key, value).commit();
    }

    public static void storeInt(String key, int value) {
        mediapref.edit().putInt(key, value).commit();
    }
}
