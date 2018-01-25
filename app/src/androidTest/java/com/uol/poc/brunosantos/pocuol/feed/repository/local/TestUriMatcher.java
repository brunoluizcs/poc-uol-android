package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.getStaticIntegerField;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.readableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by brunosantos on 25/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {

    private static final Uri TEST_WEATHER_DIR = FeedContract.NewsEntry.CONTENT_URI;

    private static final String feedCodeVariableName = "CODE_FEED";
    private static int REFLECTED_FEED_CODE;

    private UriMatcher testMatcher;

    @Before
    public void before() {
        try {
            Method buildUriMatcher = FeedProvider.class.getDeclaredMethod("buildUriMatcher");
            testMatcher = (UriMatcher) buildUriMatcher.invoke(FeedProvider.class);

            REFLECTED_FEED_CODE = getStaticIntegerField(
                    FeedProvider.class,
                    feedCodeVariableName);

        } catch (NoSuchFieldException e) {
            fail(readableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            String noBuildUriMatcherMethodFound =
                    "It doesn't appear that you have created a method called buildUriMatcher in " +
                            "the FeedProvider class.";
            fail(noBuildUriMatcherMethodFound);
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUriMatcher() {
        String weatherUriDoesNotMatch = "Error: The CODE_FEED URI was matched incorrectly.";
        int actualWeatherCode = testMatcher.match(TEST_WEATHER_DIR);
        int expectedWeatherCode = REFLECTED_FEED_CODE;
        assertEquals(weatherUriDoesNotMatch,expectedWeatherCode,actualWeatherCode);
    }
}
