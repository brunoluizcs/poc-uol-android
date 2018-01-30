package com.uol.poc.brunosantos.pocuol.feed.detail.view;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.detail.FeedDetailActivity;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static com.uol.poc.brunosantos.pocuol.feed.detail.FeedDetailFragment.EXTRA_NEWS;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by brunosantos on 29/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class FeedDetailActivityTest {

    @Rule
    public ActivityTestRule<FeedDetailActivity> mActivityRule = new ActivityTestRule<FeedDetailActivity>(
            FeedDetailActivity.class) {

            @Override
            protected Intent getActivityIntent() {
                Context targetContext = InstrumentationRegistry.getInstrumentation()
                        .getTargetContext();
                Intent intent = new Intent(targetContext, FeedDetailActivity.class);
                intent.putExtra(EXTRA_NEWS, getNews());
                return intent;

            }

    };

    @Test
    public void testBackButtonClick(){
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    /**
     * Testa se o WebView esta fazendo o carregamento das paginas carrega a pagina do google e digita
     * o valor android no campo de pesquisa
     */

    @Test
    public void testWebViewLoadPage(){
        onWebView()
                .withElement(findElement(Locator.ID, "lst-ib"))
                .perform(clearElement())
                .perform(DriverAtoms.webKeys("Android"));

    }

    @Test
    public void testShareButtonClick(){
        Intents.init();
        onView(withId(R.id.action_share)).perform(click());
        intended(hasAction(equalTo(Intent.ACTION_CHOOSER)));
        Intents.release();

        mActivityRule.finishActivity();
    }

    private News getNews(){
        return new News("News",
                "News-0",
                "http://lorempixel.com/142/100",
                 new Date(),"http://google.com",
                "http://google.com");
    }

}