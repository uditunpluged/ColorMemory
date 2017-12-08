package phantasmist.com.colourmemory;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import phantasmist.com.colourmemory.Presenter.GameActivityPresenter;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Phantasmist on 15/01/17.
 */
@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public final ActivityRule<GameActivity> main = new ActivityRule<>(GameActivity.class);
    private GameActivityPresenter mPresenter;
    @Before
    public void setUp() throws Exception{
        mPresenter=new GameActivityPresenter(main.get());
        mPresenter.loadAnimations(InstrumentationRegistry.getTargetContext());
        mPresenter.generateCardsList(InstrumentationRegistry.getTargetContext());
    }
    @Test
    public void shouldBeAbleToLoadDataFromJSON(){
        assertTrue(!mPresenter.generateCardsList(InstrumentationRegistry.getTargetContext()).isEmpty());
    }
//    @Test
//    public void flipRandomCards(){
//
//        /**
//         * Need to do certain modifications to presenter for making this tests run
//         * */
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(0),main.get().getCardImageViews().get(0) , 0, 1);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(4),main.get().getCardImageViews().get(4) , 4, 2);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(5),main.get().getCardImageViews().get(5) , 5, 1);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(2),main.get().getCardImageViews().get(2) , 2, 2);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(6),main.get().getCardImageViews().get(6) , 6, 1);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(12),main.get().getCardImageViews().get(12) , 12, 2);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(14),main.get().getCardImageViews().get(14) , 14, 1);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(15),main.get().getCardImageViews().get(15) , 15, 2);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(8),main.get().getCardImageViews().get(8) , 8, 1);
//        mPresenter.flipCard(main.get().getBackCardImageViews().get(13),main.get().getCardImageViews().get(13) , 13, 2);
//
//    }


}