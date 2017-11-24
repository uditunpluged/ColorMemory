package phantasmist.com.colourmemory.Presenter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phantasmist.com.colourmemory.AppController;
import phantasmist.com.colourmemory.Interfaces.GameActivityView;
import phantasmist.com.colourmemory.Models.Cards;
import phantasmist.com.colourmemory.Models.UserData;
import phantasmist.com.colourmemory.R;
import phantasmist.com.colourmemory.Utilities.Constants;
import phantasmist.com.colourmemory.Utilities.TypefaceTextView;
import phantasmist.com.colourmemory.Utilities.Utils;

/**
 * Created by Phantasmist on 15/01/17.
 */
public class GameActivityPresenter {

    /**
     * Presenter class controls the logic and communicates with GameActivity
     * */
    Cards.Datum selectedCard1;
    Cards.Datum selectedCard2;
    int score = 0;
    int donePairs = 0;

    private AnimatorSet mSetRightOut1;
    private AnimatorSet mSetLeftIn1;
    private AnimatorSet mSetRightOut2;
    private AnimatorSet mSetLeftIn2;
    private boolean mIsBackVisible = false;
    private List<Cards.Datum> mCardsList;
    private TypefaceTextView mScoreTextView;

    /**
     * VARIABLES FOR STORING REFERENCE TO FIRST SELECTED CARD FRONT/BACK
     */
    ImageView selectedCardImage1Front;
    ImageView selectedCardImage1Back;
    /**
     * VARIABLES FOR STORING REFERENCE TO SECONDE SELECTED CARD FRONT/BACK
     */
    ImageView selectedCardImage2Front;
    ImageView selectedCardImage2Back;

    int cardTap = 0;
    private FrameLayout mCover;
    private FrameLayout mBackgroundColor;
    private ImageView mHighScoreButton;
    String userName;
    Context mContext;

    /**
     * Interacts with the view
     */
    private GameActivityView mGameActivityView;


    public GameActivityPresenter(GameActivityView mGameActivityView) {
        this.mGameActivityView = mGameActivityView;
        this.mScoreTextView = mGameActivityView.getScoreTextView();
        this.mContext = mGameActivityView.getGameApplicationContext();
        loadAnimations(mContext);
        this.mCover = mGameActivityView.getCover();
        this.mHighScoreButton = mGameActivityView.getHighScoreImageButton();
//        this.mCardsList = generateCardsList(mContext);
    }

    public boolean loadAnimations(Context mContext) {
        mSetRightOut1 = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.out_animation);
        mSetLeftIn1 = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.in_animation);
        mSetRightOut2 = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.out_animation);
        mSetLeftIn2 = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.in_animation);

        mSetLeftIn2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCover.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mSetLeftIn1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCover.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mSetRightOut1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCover.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return true;
    }

    public List<Cards.Datum> generateCardsList(Context mContext) {
        /**
         * THIS METHOD READS A JSON FILE, ADDS THE DATA TO mCardsList
         * {"card_id": "7",        "match_id": "4",         "card_name": "colour4"}
         * 1. card_id : is the unique id assiged to each card
         * 1. match_id : is id assiged to cards which have same face
         * 1. card_name : is the name of image associated with that card
         * */
        mCardsList = new ArrayList<>();
        Cards mCards = new Gson().fromJson(Utils.loadJSONFromAsset(mContext, "card_list"), Cards.class);
        for (int i = 0; i < mCards.getData().size(); i++) {
            mCardsList.add(mCards.getData().get(i));
        }

        return mCardsList;
    }

    List<View> cardsViewList;
    List<View> backCardsViewList;

    List<ImageView> mCardImageViews;
    List<ImageView> mParentCardImageViews;


    public void shuffleCards() {
        this.mCardsList = generateCardsList(mContext);
        /**
         * SHUFFLE THE mCardsList PREPARED EARLIER IN generateCardsList() METHOD
         * */
        Collections.shuffle(mCardsList);

        for (int i = 0; i < mCardsList.size(); i++) {
            int imageResource = mContext.getResources().getIdentifier(mCardsList.get(i).getCardName(), "string", mContext.getPackageName());
            String value = imageResource == 0 ? "" : (String) mContext.getResources().getText(imageResource);
            int id = mContext.getResources().getIdentifier(value, null, mContext.getPackageName());
            Drawable res = mContext.getResources().getDrawable(id);
            /**
             * ASSIGN RESPECTIVE DRAWABLES TO IMAGEVIEWS
             * */
            mGameActivityView.getCardImageViews().get(i).setImageDrawable(res);
            mGameActivityView.getCardImageViews().get(i).setTag(mCardsList.get(i).getMatchId());
            mGameActivityView.getBackCardImageViews().get(i).setImageDrawable(mContext.getDrawable(R.drawable.card_bg));
        }

    }


    public void flipCard(ImageView front, ImageView back, int position, int cardTap) {

        /***
         * ALL GAME LOGIC TAKES PLACE INSIDE THIS,METHOD
         * */
        if (cardTap <= 2) {

            /***
             * CARD REVEAL ANIMATION
             * */
            if (!mIsBackVisible) {
                mSetRightOut1.setTarget(front);
                mSetLeftIn1.setTarget(back);
                mSetRightOut1.start();
                mSetLeftIn1.start();
                mIsBackVisible = false;
            }

            /**
             * FIRST CARD IS TAPPED
             * */
            if (cardTap == 1) {
                selectedCard1 = mCardsList.get(position);
                selectedCardImage1Back = back;
                selectedCardImage1Front = front;
            }

            /**
             * SECONDE CARD IS TAPPED | | VALUES OF FIRST AND SECONDE CARD ARE COMPARED HERE
             * */
            if (cardTap == 2) {
                mCover.setVisibility(View.VISIBLE);
                mGameActivityView.setCardTapZero(0);
                selectedCardImage2Back = back;
                selectedCardImage2Front = front;
                selectedCard2 = mCardsList.get(position);

                if (selectedCard1.getMatchId().equals(selectedCard2.getMatchId())) {
                    /**
                     * VALUES MATCHED
                     * */
                    score += 2;
                    donePairs += 1;
                    mScoreTextView.setText("" + score);
                    selectedCard1 = null;
                    selectedCard2 = null;
                    /**
                     * DISABLE TOUCH ON REVEALED CARDS
                     * */
                    selectedCardImage1Back.setEnabled(false);
                    selectedCardImage2Back.setEnabled(false);
                    mCover.setVisibility(View.GONE);
                    checkIfGameIsFinished(donePairs, score);
                } else {
                    /**
                     * VALUES NOT MATCHED
                     * */
                    score -= 1;
                    mIsBackVisible = false;
                    mScoreTextView.setText("" + score);
                    selectedCard1 = null;
                    selectedCard2 = null;
                    /**
                     * EXECUTE CARD FLIP (CLOSE) ANIMATION AFTER 1.5 SECONDES
                     * */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSetRightOut1.setTarget(selectedCardImage2Back);
                            mSetLeftIn1.setTarget(selectedCardImage2Front);
                            mSetRightOut1.start();
                            mSetLeftIn1.start();
                            mSetRightOut2.setTarget(selectedCardImage1Back);
                            mSetLeftIn2.setTarget(selectedCardImage1Front);
                            mSetRightOut2.start();
                            mSetLeftIn2.start();
                        }
                    }, 1500);

                }

            }

        }
    }


    public void checkIfGameIsFinished(int completedPairCount, final int score) {
        if (completedPairCount == 8) {
            if (!AppController.mediapref.getString(Constants.ALLUSERS, "").isEmpty()) {
                UserData mUserData = new Gson().fromJson(AppController.mediapref.getString(Constants.ALLUSERS, ""), UserData.class);
                if (score >= mUserData.getDatumList().get(0).getUserScore()) {
                    String content = "Congragulations!\nYou made it to the top. Your score is " + score + " which is highest till now.\nPlease enter your name.";
                    mGameActivityView.showDialog(content, score);
                } else {
                    String content = "Your score is " + score +
                    ".\nPlease enter your name.";
                    mGameActivityView.showDialog(content, score);
                }
            } else {
                String content = " Your score is " + score + " .\nPlease enter your name.";
                mGameActivityView.showDialog(content, score);
            }
        }
    }


}
