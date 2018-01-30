package com.uol.poc.brunosantos.pocuol.feed.main.view;

import android.content.ContentValues;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.detail.FeedDetailActivity;
import com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by brunosantos on 29/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class FeedMainActivityTest {
    private static final int COUNT_RECORDS_TO_INSERT = 30;

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public ActivityTestRule<FeedMainActivity> mActivityRule = new ActivityTestRule<>(
            FeedMainActivity.class);

    @Before
    public void setup(){
        mContext.getContentResolver().delete(FeedContract.NewsEntry.CONTENT_URI,null,null);
        insertValues();
    }

    @After
    public void tearDown() throws Exception {
        mContext.getContentResolver().delete(FeedContract.NewsEntry.CONTENT_URI,null,null);
    }

    @Test
    public void testScroolToItem(){
        onView(withId(R.id.rv_feed))
                .perform(RecyclerViewActions.scrollToPosition(COUNT_RECORDS_TO_INSERT -1));

    }

    @Test
    public void testScroolToInit(){
        onView(withId(R.id.rv_feed))
                .perform(RecyclerViewActions.scrollToPosition(0));

    }

    @Test
    public void testLaunchDetailActivity(){
        Intents.init();
        onView(withId(R.id.appbar_main)).perform(swipeUp());

        onView(withId(R.id.rv_feed))
                .perform(RecyclerViewActions.actionOnItemAtPosition(COUNT_RECORDS_TO_INSERT -1,
                        click()));

        intended(hasComponent(FeedDetailActivity.class.getName()));
        Intents.release();
    }

    private void insertValues(){
        ContentValues[] values = new ContentValues[COUNT_RECORDS_TO_INSERT];
        for (int i = 0; i < COUNT_RECORDS_TO_INSERT; i++) {
            values[i] = TestUtilities.getFeedValues("News-" + i);
        }
        mContext.getContentResolver().bulkInsert(FeedContract.NewsEntry.CONTENT_URI,values);
    }

}