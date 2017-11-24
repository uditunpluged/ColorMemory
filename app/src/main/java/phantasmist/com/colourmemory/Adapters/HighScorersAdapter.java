package phantasmist.com.colourmemory.Adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import phantasmist.com.colourmemory.Models.UserData;
import phantasmist.com.colourmemory.R;
import phantasmist.com.colourmemory.Utilities.TypefaceTextView;
import phantasmist.com.colourmemory.databinding.ListItemBinding;

/**
 * Created by Phantasmist on 14/01/17.
 */

public class HighScorersAdapter extends RecyclerView.Adapter<HighScorersAdapter.HighScorerHolder> {

    List<UserData.Datum> mDatumList;


    public HighScorersAdapter(List<UserData.Datum> mDatumList) {
        this.mDatumList = mDatumList;
    }

    @Override
    public HighScorerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item, parent, false);
        return new HighScorerHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(HighScorerHolder holder, int position) {
        holder.bindUserData(mDatumList.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return mDatumList.size();
    }

    public class HighScorerHolder extends RecyclerView.ViewHolder {
        ListItemBinding mListItemBinding;
        private TypefaceTextView mRankText;

        public HighScorerHolder(ListItemBinding mListItemBinding) {
            super(mListItemBinding.getRoot());
            this.mListItemBinding = mListItemBinding;
            mRankText = (TypefaceTextView) mListItemBinding.getRoot().findViewById(R.id.rankText);
        }

        public void bindUserData(UserData.Datum datum, int rank) {
            mListItemBinding.setUser(datum);
            mRankText.setText("" + rank);
        }
    }
}
