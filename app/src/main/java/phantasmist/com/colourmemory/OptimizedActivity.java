package phantasmist.com.colourmemory;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phantasmist.com.colourmemory.Models.Cards;
import phantasmist.com.colourmemory.Models.UserData;
import phantasmist.com.colourmemory.Utilities.Constants;
import phantasmist.com.colourmemory.Utilities.TypefaceTextView;
import phantasmist.com.colourmemory.Utilities.Utils;

public class OptimizedActivity extends AppCompatActivity {


    /***
     *
     * THIS ACTIVITY IS NOT BASED ON MVP PATTERN AND CONTAINS ALL THE BUSINESS LOGIC
     * WITHIN THIS CLASS ITSELF
     *
     * */


    private FrameLayout mParentContainer;
    private View vertigo1;
    private View vertigo2;
    private View vertigo3;
    private View hori1;
    private View hori2;
    private View hori3;


    private AnimatorSet mSetRightOut1;
    private AnimatorSet mSetLeftIn1;
    private AnimatorSet mSetRightOut2;
    private AnimatorSet mSetLeftIn2;
    private boolean mIsBackVisible = false;
    private List<Cards.Datum> mCardsList;
    private TypefaceTextView mScoreTextView;

    int cardTap = 0;
    private FrameLayout mCover;
    private FrameLayout mBackgroundColor;
    private ImageView mHighScoreButton;

    private void assignViews() {
        mParentContainer = (FrameLayout) findViewById(R.id.parentContainer);
        mScoreTextView = (TypefaceTextView) findViewById(R.id.scoreTextView);
        mCover = (FrameLayout) findViewById(R.id.cover);
        mBackgroundColor = (FrameLayout) findViewById(R.id.backgroundColor);
        mHighScoreButton = (ImageView) findViewById(R.id.highScoreButton);
    }

    private void loadAnimations() {
        mSetRightOut1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        mSetRightOut2 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn2 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_layout);
        assignViews();

        loadAnimations();
        generateGridAndPlaceCards();
        generateCardsList();

        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptimizedActivity.this, HighScoresActivity.class));
            }
        });

    }

    List<View> cardsViewList;
    List<View> backCardsViewList;

    List<ImageView> mCardImageViews;
    List<ImageView> mParentCardImageViews;

    private void generateGridAndPlaceCards() {
        mParentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mParentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mParentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                /**
                 * INFLATE VERTICAL AND HORIZONTAL LINES TO MAKE A GRIF INSIDE THE PARENT CONTAINER
                 * */
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vertigo1 = layoutInflater.inflate(R.layout.vertical_line, null);
                vertigo2 = layoutInflater.inflate(R.layout.vertical_line, null);
                vertigo3 = layoutInflater.inflate(R.layout.vertical_line, null);

                hori1 = layoutInflater.inflate(R.layout.horizontal_line, null);
                hori2 = layoutInflater.inflate(R.layout.horizontal_line, null);
                hori3 = layoutInflater.inflate(R.layout.horizontal_line, null);


                cardsViewList = new ArrayList<>();
                backCardsViewList = new ArrayList<>();
                /**
                 * INITIALIZE VIEWS DYNAMICALLY AND ADD IT TO cardsViewList
                 * */
                for (int i = 0; i < 16; i++) {
                    cardsViewList.add(layoutInflater.inflate(R.layout.my_card, null));
                }
                /**
                 * INITIALIZE VIEWS DYNAMICALLY AND ADD IT TO backCardsViewList
                 * */
                for (int i = 0; i < 16; i++) {
                    backCardsViewList.add(layoutInflater.inflate(R.layout.my_card, null));
                }

                mCardImageViews = new ArrayList<>();
                mParentCardImageViews = new ArrayList<>();

                /**
                 * INITIALIZE IMAGEVIEWS DYNAMICALLY AND ADD IT TO mCardsImageViews  LIST (front face of card which has the actual image)
                 * */
                for (int i = 0; i < 16; i++) {
                    mCardImageViews.add((ImageView) cardsViewList.get(i).findViewById(R.id.img));
                }
                /**
                 * INITIALIZE IMAGEVIEWS DYNAMICALLY AND ADD IT TO mParentCardImageViews  LIST (back face of card which has the colour memory logo)
                 * */
                for (int i = 0; i < 16; i++) {
                    mParentCardImageViews.add((ImageView) backCardsViewList.get(i).findViewById(R.id.img));
                }


                ViewGroup parent = (ViewGroup) findViewById(R.id.parentContainer);

                /**
                 *  CALCULATE TOTAL WIDTH OF PARENT CONTAINER*/
                float totalViewWidth = mParentContainer.getWidth();
                float intervalWidth = totalViewWidth / 4;

                /**
                 *  CALCULATE TOTAL WIDTH OF PARENT CONTAINER*/
                float totalViewHeight = mParentContainer.getHeight();
                float intervalVerticalWidth = totalViewHeight / 4;

                /**
                 *  CALCULATE SCALE FOR FLIP ANIMATIONS*/
                int distance = 8000;
                float scale = getResources().getDisplayMetrics().density * distance;

                /**
                 * ASSIGN POSITION TO VERTICAL LINES ON X AXIS
                 * */
                vertigo1.setX(intervalWidth * 1 - vertigo1.getWidth());
                vertigo2.setX(intervalWidth * 2 - vertigo2.getWidth());
                vertigo3.setX(intervalWidth * 3 - vertigo3.getWidth());

                /**
                 * ASSIGN POSITION TO HORIZONTAL LINES ON Y AXIS
                 * */
                hori1.setY(intervalVerticalWidth * 1 - hori1.getHeight());
                hori2.setY(intervalVerticalWidth * 2 - hori2.getHeight());
                hori3.setY(intervalVerticalWidth * 3 - hori3.getHeight());

                /**
                 * Inflating FIRST ROW*/
                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        cardsViewList.get(i).setX(vertigo1.getWidth());
                        backCardsViewList.get(i).setX(vertigo1.getWidth());
                    } else {
                        cardsViewList.get(i).setX(intervalWidth * i - vertigo1.getWidth());
                        backCardsViewList.get(i).setX(intervalWidth * i - vertigo2.getWidth());

                    }
                    cardsViewList.get(i).setY(0);
                    backCardsViewList.get(i).setY(0);


                }

                /**
                 * Inflating SECONDE ROW*/
                for (int i = 4; i < 8; i++) {
                    if (i == 4) {
                        cardsViewList.get(i).setX(vertigo1.getWidth());
                        backCardsViewList.get(i).setX(vertigo1.getWidth());
                    } else {
                        cardsViewList.get(i).setX(intervalWidth * (i - 4) - vertigo2.getWidth());
                        backCardsViewList.get(i).setX(intervalWidth * (i - 4) - vertigo2.getWidth());
                    }
                    cardsViewList.get(i).setY(intervalVerticalWidth * 1 - hori1.getHeight());
                    backCardsViewList.get(i).setY(intervalVerticalWidth * 1 - hori1.getHeight());

                }

                /**
                 * Inflating THIRD ROW*/
                for (int i = 8; i < 12; i++) {
                    if (i == 8) {
                        cardsViewList.get(i).setX(vertigo1.getWidth());
                        backCardsViewList.get(i).setX(vertigo1.getWidth());
                    } else {
                        cardsViewList.get(i).setX(intervalWidth * (i - 8) - vertigo2.getWidth());
                        backCardsViewList.get(i).setX(intervalWidth * (i - 8) - vertigo2.getWidth());
                    }
                    cardsViewList.get(i).setY(intervalVerticalWidth * 2 - hori1.getHeight());
                    backCardsViewList.get(i).setY(intervalVerticalWidth * 2 - hori1.getHeight());

                }

                /**
                 * Inflating FOURTH ROW*/
                for (int i = 12; i < 16; i++) {
                    if (i == 12) {
                        cardsViewList.get(i).setX(vertigo1.getWidth());
                        backCardsViewList.get(i).setX(vertigo1.getWidth());
                    } else {
                        cardsViewList.get(i).setX(intervalWidth * (i - 12) - vertigo2.getWidth());
                        backCardsViewList.get(i).setX(intervalWidth * (i - 12) - vertigo2.getWidth());
                    }
                    cardsViewList.get(i).setY(intervalVerticalWidth * 3 - hori1.getHeight());
                    backCardsViewList.get(i).setY(intervalVerticalWidth * 3 - hori1.getHeight());

                }

                /**
                 * Add horizonatal and vertical lines**/
                parent.addView(vertigo1);
                parent.addView(vertigo2);
                parent.addView(vertigo3);
                parent.addView(hori1);
                parent.addView(hori2);
                parent.addView(hori3);

                /**
                 * Set camera distance to all cards & add to the parent view*/
                for (int i = 0; i < 16; i++) {
                    cardsViewList.get(i).setCameraDistance(scale);
                    backCardsViewList.get(i).setCameraDistance(scale);
                    parent.addView(cardsViewList.get(i));
                    parent.addView(backCardsViewList.get(i));
                }

                /**
                 * Setup on click listener for all the views*/
                for (int i = 0; i < mCardImageViews.size(); i++) {
                    final int finalI = i;
                    mCardImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cardTap += 1;
                            flipCard(mParentCardImageViews.get(finalI), mCardImageViews.get(finalI), finalI);
                        }
                    });
                }

                /**
                 *  SHUFFLE THE CARDS */
                shuffleCards();
            }
        });


    }


    private void generateCardsList() {
        /**
         * THIS METHOD READS A JSON FILE, ADDS THE DATA TO mCardsList
         * {"card_id": "7",        "match_id": "4",         "card_name": "colour4"}
         * 1. card_id : is the unique id assiged to each card
         * 1. match_id : is id assiged to cards which have same face
         * 1. card_name : is the name of image associated with that card
         * */
        mCardsList = new ArrayList<>();
        Cards mCards = new Gson().fromJson(Utils.loadJSONFromAsset(getApplication(), "card_list"), Cards.class);
        for (int i = 0; i < mCards.getData().size(); i++) {
            mCardsList.add(mCards.getData().get(i));
        }

    }

    private void shuffleCards() {
        /**
         * SHUFFLE THE mCardsList PREPARED EARLIER IN generateCardsList() METHOD
         * */
        Collections.shuffle(mCardsList);

        for (int i = 0; i < mCardsList.size(); i++) {
            int imageResource = getApplication().getResources().getIdentifier(mCardsList.get(i).getCardName(), "string", getApplication().getPackageName());
            String value = imageResource == 0 ? "" : (String) getResources().getText(imageResource);
            int id = getResources().getIdentifier(value, null, getPackageName());
            Drawable res = getResources().getDrawable(id);
            /**
             * ASSIGN RESPECTIVE DRAWABLES TO IMAGEVIEWS
             * */
            mCardImageViews.get(i).setImageDrawable(res);
            mCardImageViews.get(i).setTag(mCardsList.get(i).getMatchId());
        }

    }

    /**
     * VARIABLES FOR STORING DATA OF SELECTED CARDS
     */
    Cards.Datum selectedCard1;
    Cards.Datum selectedCard2;
    int score = 0;
    int donePairs = 0;

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

    public void flipCard(ImageView front, ImageView back, int position) {

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
                cardTap = 0;
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

    String userName;

    public void checkIfGameIsFinished(int completedPairCount, final int score) {
        if (completedPairCount == 8) {
            if (!AppController.mediapref.getString(Constants.ALLUSERS, "").isEmpty()) {
                UserData mUserData = new Gson().fromJson(AppController.mediapref.getString(Constants.ALLUSERS, ""), UserData.class);
                if (score > mUserData.getDatumList().get(0).getUserScore()) {
                    String content = "Congragulations!\nYou made it to the top. Your score is " + score + " which is highest till now.\nPlease enter your name.";
                    new MaterialDialog.Builder(this).iconRes(R.drawable.ic_launcher).limitIconToDefaultSize().content(content).title("HIGHEST SCORE").inputType(InputType.TYPE_CLASS_TEXT).input("John Doe", null, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            if (input.toString().equals("")) {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            } else {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                userName = input.toString();
                            }
                        }
                    }).positiveText("SUBMIT").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Utils.addToUserList(userName, score);
                        }
                    }).alwaysCallInputCallback().cancelable(false).show();
                } else {
                    String content = " Your score is " + score + " .\n Please enter your name.";
                    new MaterialDialog.Builder(this).iconRes(R.drawable.ic_launcher).limitIconToDefaultSize().content(content).title("GAME OVER").inputType(InputType.TYPE_CLASS_TEXT).input("John Doe", null, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            if (input.toString().equals("")) {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            } else {
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                userName = input.toString();
                            }
                        }
                    }).positiveText("SUBMIT").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Utils.addToUserList(userName, score);
                        }
                    }).alwaysCallInputCallback().cancelable(false).show();
                }
            } else {
                String content = " Your score is " + score + " .\n Please enter your name.";
                new MaterialDialog.Builder(this).iconRes(R.drawable.ic_launcher).limitIconToDefaultSize().content(content).title("GAME OVER").inputType(InputType.TYPE_CLASS_TEXT).input("John Doe", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equals("")) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            userName = input.toString();
                        }
                    }
                }).positiveText("SUBMIT").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.addToUserList(userName, score);
                    }
                }).alwaysCallInputCallback().cancelable(false).show();
            }
        }
    }


}
