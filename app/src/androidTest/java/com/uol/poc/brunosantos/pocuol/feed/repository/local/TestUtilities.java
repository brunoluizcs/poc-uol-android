/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.uol.poc.brunosantos.pocuol.PollingCheck;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_SHARE_URL;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_THUMB;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_TITLE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_TYPE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_UPDATE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_WEBVIEW_URL;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


class TestUtilities {
    static final long DATE_NORMALIZED = 1475280000000L;
    static final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    /**
     * Valido que o cursor não esta nulo e comparo ele com os valores esperados este metodo fecha o cursor
     *
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateThenCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertNotNull(
                "This cursor is null. Did you make sure to register your ContentProvider in the manifest?",
                valueCursor);

        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    /**
     * Este metodo corre o cursor e faz varios asserts para verificar se os valores foram preenchidos apropriadamente
     *
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);

            String columnNotFoundError = "Column '" + columnName + "' not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(index);

            String valuesDontMatchError = "Actual value '" + actualValue
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + error;

            assertEquals(valuesDontMatchError,
                    expectedValue,
                    actualValue);
        }
    }

    /**
     * Metodo auxiliar para popular um objeto  ContentValues para inserir no banco ou inserir pelo ContentProvider
     *
     * @return ContentValues that can be inserted into our ContentProvider or pocUol.db
     */
    static ContentValues createTestFeedContentValues() {
        ContentValues testFeedEntryValues = new ContentValues();

        testFeedEntryValues.put(COLUMN_UPDATE, DATE_NORMALIZED);
        testFeedEntryValues.put(COLUMN_TYPE, "news");
        testFeedEntryValues.put(COLUMN_THUMB, "http://lorempixel.com/400/200");
        testFeedEntryValues.put(COLUMN_TITLE, "News " + Calendar.getInstance().getTimeInMillis());
        testFeedEntryValues.put(COLUMN_SHARE_URL, "http://share.com");
        testFeedEntryValues.put(COLUMN_WEBVIEW_URL, "http://news.com/detail");

        return testFeedEntryValues;
    }


    static ContentValues[] createBulkInsertTestFeedValues() {

        ContentValues[] bulkTestFeedValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        long testDate = TestUtilities.DATE_NORMALIZED;
        long normalizedTestDate = normalizeDate(testDate);

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {

            normalizedTestDate += DAY_IN_MILLIS;

            ContentValues feedValues = new ContentValues();

            feedValues.put(COLUMN_UPDATE, normalizedTestDate);
            feedValues.put(COLUMN_TYPE, "news");
            feedValues.put(COLUMN_THUMB, "http://lorempixel.com/400/200");
            feedValues.put(COLUMN_TITLE, "News " + Calendar.getInstance().getTimeInMillis());
            feedValues.put(COLUMN_SHARE_URL, "http://share.com");
            feedValues.put(COLUMN_WEBVIEW_URL, "http://news.com/detail");

            bulkTestFeedValues[i] = feedValues;
        }

        return bulkTestFeedValues;
    }


    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }


    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("feedValues");
            ht.start();
            return new TestContentObserver(ht);
        }


        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }


        void waitForNotificationOrFail() {

            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static String getConstantNameByStringValue(Class klass, String value)  {
        for (Field f : klass.getDeclaredFields()) {
            int modifiers = f.getModifiers();
            Class<?> type = f.getType();
            boolean isPublicStaticFinalString = Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)
                    && type.isAssignableFrom(String.class);

            if (isPublicStaticFinalString) {
                String fieldName = f.getName();
                try {
                    String fieldValue = (String) klass.getDeclaredField(fieldName).get(null);
                    if (fieldValue.equals(value)) return fieldName;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }
        }

        return null;
    }

    static String getStaticStringField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field stringField = clazz.getDeclaredField(variableName);
        stringField.setAccessible(true);
        return (String) stringField.get(null);
    }

    static Integer getStaticIntegerField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field intField = clazz.getDeclaredField(variableName);
        intField.setAccessible(true);
        return (Integer) intField.get(null);
    }

    static String readableClassNotFound(ClassNotFoundException e) {
        String message = e.getMessage();
        int indexBeforeSimpleClassName = message.lastIndexOf('.');
        String simpleClassNameThatIsMissing = message.substring(indexBeforeSimpleClassName + 1);
        simpleClassNameThatIsMissing = simpleClassNameThatIsMissing.replaceAll("\\$", ".");
        String fullClassNotFoundReadableMessage = "Couldn't find the class "
                + simpleClassNameThatIsMissing
                + ".\nPlease make sure you've created that class.";
        return fullClassNotFoundReadableMessage;
    }

    static String readableNoSuchField(NoSuchFieldException e) {
        String message = e.getMessage();

        Pattern p = Pattern.compile("No field (\\w*) in class L.*/(\\w*\\$?\\w*);");

        Matcher m = p.matcher(message);

        if (m.find()) {
            String missingFieldName = m.group(1);
            String classForField = m.group(2).replaceAll("\\$", ".");
            String fieldNotFoundReadableMessage = "Couldn't find "
                    + missingFieldName + " in class " + classForField + "."
                    + "\nPlease make sure you've declared that field.";
            return fieldNotFoundReadableMessage;
        } else {
            return e.getMessage();
        }
    }

    static long normalizeDate(long date) {
        long daysSinceEpoch = TimeUnit.MILLISECONDS.toDays(date);
        return daysSinceEpoch * DAY_IN_MILLIS;
    }
}