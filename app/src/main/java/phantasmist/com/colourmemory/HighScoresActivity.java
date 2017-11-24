package phantasmist.com.colourmemory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import phantasmist.com.colourmemory.Adapters.HighScorersAdapter;
import phantasmist.com.colourmemory.Models.UserData;
import phantasmist.com.colourmemory.Utilities.Constants;
import phantasmist.com.colourmemory.Utilities.TypefaceTextView;

/**
 * Created by Phantasmist on 14/01/17.
 */

public class HighScoresActivity extends AppCompatActivity {

    private RecyclerView mHighScoresRecyclerView;
    private Toolbar mToolbar;
    List<UserData.Datum> mUsersList;
    private TypefaceTextView mNoRecsTextView;

    private void assignViews() {
        mHighScoresRecyclerView = (RecyclerView) findViewById(R.id.highScoresRecyclerView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNoRecsTextView = (TypefaceTextView) findViewById(R.id.noRecsText);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to show activity icon or logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);
        assignViews();
        mHighScoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHighScoresRecyclerView.setAdapter(new HighScorersAdapter(getUsersList()));

        /**
         * SHows No records text**/
        if (AppController.mediapref.getString(Constants.ALLUSERS, "").isEmpty()) {
            mNoRecsTextView.setVisibility(View.VISIBLE);
        } else {
            mNoRecsTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public List<UserData.Datum> getUsersList() {
        mUsersList = new ArrayList<>();
        if (!(AppController.mediapref.getString(Constants.ALLUSERS, "").isEmpty())) {
            UserData mUserData = new Gson().fromJson(AppController.mediapref.getString(Constants.ALLUSERS, ""), UserData.class);
            mUsersList = mUserData.getDatumList();
        }
        return mUsersList;
    }


}
