package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.BULK_INSERT_RECORDS_TO_INSERT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by brunosantos on 26/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestFeedProvider {

    private final Context mContext = InstrumentationRegistry.getTargetContext();


    @Before
    public void setUp() {
        deleteAllRecordsFromNewsTable();
    }

    /**
     * Esse teste verifica se o content provider esta registrado corretamente no AndroidManifest
     *
     */
    @Test
    public void testProviderRegistry() {

        String packageName = mContext.getPackageName();
        String feedProviderClassName = FeedProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, feedProviderClassName);
        try {
            PackageManager pm = mContext.getPackageManager();

            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = FeedContract.CONTENT_AUTHORITY;

            String incorrectAuthority =
                    "Error: FeedProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,actualAuthority,expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll = "Error: FeedProvider not registered at " + mContext.getPackageName();
            fail(providerNotRegisteredAtAll);
        }
    }

    /**
     * Este teste utiliza o banco de dados para inserir o registro e verifica atraves do ContentProvicer
     * se o registro foi inserido
     *
     */
    @Test
    public void testBasicFeedQuery() {
        FeedDbHelper dbHelper = new FeedDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testFeedContentValues = TestUtilities.createTestFeedContentValues();

        long newsRowId = database.insert(
                FeedContract.NewsEntry.TABLE_NAME,null,testFeedContentValues);

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, newsRowId != -1);

        database.close();


        Cursor feedCursor = mContext.getContentResolver().query(
                FeedContract.NewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        TestUtilities.validateThenCloseCursor("testBasicFeedQuery",feedCursor,testFeedContentValues);


    }

    /**
     * Test verifica o metodo bulkInsert do ContentPorvider e verifica tambem se o metodo onChange
     * foi acionado quando os dados foram inseridos
     *
     *
     */
    @Test
    public void testBulkInsert() {
        ContentValues[] bulkInsertTestContentValues = TestUtilities.createBulkInsertTestFeedValues();

        TestUtilities.TestContentObserver feedObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        contentResolver.registerContentObserver(FeedContract.NewsEntry.CONTENT_URI,
                true,feedObserver);

        int insertCount = contentResolver.bulkInsert(
                FeedContract.NewsEntry.CONTENT_URI,
                bulkInsertTestContentValues);

        feedObserver.waitForNotificationOrFail();


        contentResolver.unregisterContentObserver(feedObserver);


        String expectedAndActualInsertedRecordCountDoNotMatch =
                "Number of expected records inserted does not match actual inserted record count";
        assertEquals(expectedAndActualInsertedRecordCountDoNotMatch, insertCount,BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = mContext.getContentResolver().query(
                FeedContract.NewsEntry.CONTENT_URI,
                null,
                null,
                null,
                FeedContract.NewsEntry.COLUMN_UPDATE + " ASC");

        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord(
                    "testBulkInsert. Error validating NewsEntry " + i,
                    cursor,
                    bulkInsertTestContentValues[i]);
        }
        cursor.close();
    }

    /**
     * Este teste deleta todos os registro da tabela News utilizando o ContentProvider
     *
     */
    @Test
    public void testDeleteAllRecordsFromProvider() {
        testBulkInsert();

        TestUtilities.TestContentObserver feedObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = InstrumentationRegistry.getTargetContext().getContentResolver();

        contentResolver.registerContentObserver(
                FeedContract.NewsEntry.CONTENT_URI,
                true,
                feedObserver);

        contentResolver.delete(
                FeedContract.NewsEntry.CONTENT_URI,
                null,
                null);

        Cursor shouldBeEmptyCursor = contentResolver.query(
                FeedContract.NewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);


        feedObserver.waitForNotificationOrFail();
        contentResolver.unregisterContentObserver(feedObserver);

        String cursorWasNull = "Cursor was null.";
        assertNotNull(cursorWasNull, shouldBeEmptyCursor);
        String allRecordsWereNotDeleted = "Error: All records were not deleted from news table during delete";

        assertEquals(allRecordsWereNotDeleted,
                0,
                shouldBeEmptyCursor.getCount());

        shouldBeEmptyCursor.close();
    }

    /**
     * Este é um metodo auxiliar que deleta todos os registros da tabela direto no banco de dados
     *
     *
     */
    private void deleteAllRecordsFromNewsTable() {
        FeedDbHelper helper = new FeedDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(FeedContract.NewsEntry.TABLE_NAME, null, null);
        database.close();
    }
}
