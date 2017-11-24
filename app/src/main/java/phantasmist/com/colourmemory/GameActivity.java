package phantasmist.com.colourmemory;

/**
 * Created by Phantasmist on 15/01/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import phantasmist.com.colourmemory.Interfaces.GameActivityView;
import phantasmist.com.colourmemory.Presenter.GameActivityPresenter;
import phantasmist.com.colourmemory.Utilities.TypefaceTextView;
import phantasmist.com.colourmemory.Utilities.Utils;

public class GameActivity extends AppCompatActivity implements GameActivityView {

    /***
     *
     * THIS ACTIVITY IS BASED ON MVP PATTERN
     * BUSINESS LOGIC IS WRITTEN IN THE GameActivityPresenter class
     *
     * */
    private FrameLayout mParentContainer;
    private View vertigo1;
    private View vertigo2;
    private View vertigo3;
    private View hori1;
    private View hori2;
    private View hori3;

    private TypefaceTextView mScoreTextView;

    int cardTap = 0;
    private FrameLayout mCover;
    private FrameLayout mBackgroundColor;
    private ImageView mHighScoreButton;
    private GameActivityPresenter presenter;
    String userName;

    List<View> cardsViewList;
    List<View> backCardsViewList;

    List<ImageView> mCardImageViews;
    List<ImageView> mParentCardImageViews;


    private void assignViews() {
        mParentContainer = (FrameLayout) findViewById(R.id.parentContainer);
        mScoreTextView = (TypefaceTextView) findViewById(R.id.scoreTextView);
        mCover = (FrameLayout) findViewById(R.id.cover);
        mBackgroundColor = (FrameLayout) findViewById(R.id.backgroundColor);
        mHighScoreButton = (ImageView) findViewById(R.id.highScoreButton);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_layout);
        assignViews();
        generateGridAndPlaceCards();
        presenter = new GameActivityPresenter(this);


        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameActivity.this, HighScoresActivity.class));
            }
        });


    }


    /**
     * THIS MEHOD DYNAMICALLY GENERATES THE VIEW AND PLACES THE CARDS ON THE GRID
     * */
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
                    mParentCardImageViews.add(((ImageView) backCardsViewList.get(i).findViewById(R.id.img)));
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
                            presenter.flipCard(mParentCardImageViews.get(finalI), mCardImageViews.get(finalI), finalI, cardTap);
                        }
                    });
                }

                /**
                 *  SHUFFLE THE CARDS */
                presenter.shuffleCards();
            }
        });


    }



    /**
     * INTERFACE METHODS, THROUGH WHICH THE PRESENTER COMMUNICATES WITH THIS ACTIVITY
     * */
    @Override
    public TypefaceTextView getScoreTextView() {
        return mScoreTextView;
    }

    @Override
    public FrameLayout getCover() {
        return mCover;
    }

    @Override
    public ImageView getHighScoreImageButton() {
        return mHighScoreButton;
    }

    @Override
    public List<ImageView> getCardImageViews() {
        return mCardImageViews;
    }

    @Override
    public Context getGameApplicationContext() {
        return getApplicationContext();
    }

    @Override
    public void setCardTapZero(int cardTapZero) {
        cardTap = cardTapZero;
    }

    @Override
    public List<ImageView> getBackCardImageViews() {
        return mParentCardImageViews;
    }


    /**
     * SHOWS DIALOG BOX AND PROMPTS USER TO ENTER HIS NAME WITH BASIC VALIDATION i.e. "" is unacceptable
     * */
    @Override
    public void showDialog(String content, final int score) {
        new MaterialDialog.Builder(GameActivity.this).iconRes(R.drawable.ic_launcher).limitIconToDefaultSize().content(content).title("GAME OVER").inputType(InputType.TYPE_CLASS_TEXT).input("John Doe", null, new MaterialDialog.InputCallback() {
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
                startActivity(new Intent(GameActivity.this, HighScoresActivity.class));

            }
        }).alwaysCallInputCallback().cancelable(false).show();
    }
}
