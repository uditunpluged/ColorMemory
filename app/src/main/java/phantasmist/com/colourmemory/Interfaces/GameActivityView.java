package phantasmist.com.colourmemory.Interfaces;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import phantasmist.com.colourmemory.Utilities.TypefaceTextView;

/**
 * Created by Phantasmist on 15/01/17.
 */
public interface GameActivityView {
    TypefaceTextView getScoreTextView();
    FrameLayout getCover();
    ImageView getHighScoreImageButton();
    List<ImageView> getCardImageViews();
    List<ImageView> getBackCardImageViews();
    Context getGameApplicationContext();
//    void setLockOnRevealedCards(ImageView imageView,boolean lockCards);
//    void setScoreText(String scoreText);
    void setCardTapZero(int cardTapZero);

    void showDialog(String content, int score);



//    void checkIfGameIsFinished(int completedPairCount, int score);
}
