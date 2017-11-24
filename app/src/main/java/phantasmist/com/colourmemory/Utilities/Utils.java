package phantasmist.com.colourmemory.Utilities;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phantasmist.com.colourmemory.AppController;
import phantasmist.com.colourmemory.Models.Cards;
import phantasmist.com.colourmemory.Models.UserData;

/**
 * Created by Phantasmist on 09/07/16.
 */
public class Utils {

    private static List<Cards> mCardsList;


    public static String loadJSONFromAsset(Context mContext, String fileName) {
        String json = null;
        try {

            InputStream is = mContext.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static void addToUserList(String name, int score) {
        if (!AppController.mediapref.getString(Constants.ALLUSERS, "").isEmpty() && !AppController.mediapref.getString(Constants.ALLUSERS, "").equals(null)) {
            UserData allItems = getBackup();
            List<UserData.Datum> userList = allItems.getDatumList();
            UserData.Datum user = new UserData().new Datum(name, score);
            user.setUserName(name);
            user.setUserScore(score);
            userList.add(user);
            Collections.sort(userList,Collections.<UserData.Datum>reverseOrder());
            allItems.setDatumList(userList);
            AppController.storeString(Constants.ALLUSERS, new Gson().toJson(allItems, UserData.class));
        } else {
            List<UserData.Datum> userList = new ArrayList<>();

            UserData allItems = new UserData();
            UserData.Datum user = new UserData().new Datum(name,score);
            user.setUserName(name);
            user.setUserScore(score);
            userList.add(user);
            allItems.setDatumList(userList);

            AppController.storeString(Constants.ALLUSERS, new Gson().toJson(allItems, UserData.class));
        }
    }

    public static UserData getBackup() {
        UserData allItems = new Gson().fromJson(AppController.mediapref.getString(Constants.ALLUSERS, ""), UserData.class);
        return allItems;
    }


}
