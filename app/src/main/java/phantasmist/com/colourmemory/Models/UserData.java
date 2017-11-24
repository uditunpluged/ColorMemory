package phantasmist.com.colourmemory.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import phantasmist.com.colourmemory.BR;


/**
 * Created by Phantasmist on 13/01/17.
 */

public class UserData {
    @SerializedName("data")
    @Expose
    private List<Datum> datumList = new ArrayList<>();

    public List<Datum> getDatumList() {
        return datumList;
    }

    public void setDatumList(List<Datum> datumList) {
        this.datumList = datumList;
    }

    public class Datum extends BaseObservable implements Comparable<Datum> {
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userScore")
        @Expose
        private int userScore;

        public Datum(String userName, int userScore) {
            this.userName = userName;
            this.userScore = userScore;
        }

        @Bindable
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
            notifyPropertyChanged(BR.userName);
        }

        @Bindable
        public int getUserScore() {
            return userScore;
        }

        public void setUserScore(int userScore) {
            this.userScore = userScore;
            notifyPropertyChanged(BR.userScore);
        }


        @Override
        public int compareTo(Datum datum) {
            Float val = Float.parseFloat(String.valueOf(getUserScore()));

            return val.compareTo(Float.parseFloat(String.valueOf(datum.getUserScore())));
        }
    }
}
