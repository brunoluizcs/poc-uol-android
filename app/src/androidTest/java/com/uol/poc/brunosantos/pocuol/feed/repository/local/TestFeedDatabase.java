package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.getConstantNameByStringValue;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.getStaticIntegerField;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.getStaticStringField;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.readableClassNotFound;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.TestUtilities.readableNoSuchField;
import static org.junit.Assert.*;

/**
 * Created by brunosantos on 25/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestFeedDatabase {
    private final Context context = InstrumentationRegistry.getTargetContext();


    private static final String packageName = "com.uol.poc.brunosantos.pocuol";
    private static final String dataPackageName = packageName + ".feed.repository.local";

    private Class newsEntryClass;
    private Class feedDbHelperClass;
    private static final String feedContractName = FeedContract.class.getName();
    private static final String newsEntryName = FeedContract.NewsEntry.class.getName();
    private static final String feedDbHelperName = FeedDbHelper.class.getName();

    private static final String databaseNameVariableName = "DATABASE_NAME";
    private static String REFLECTED_DATABASE_NAME;

    private static final String databaseVersionVariableName = "DATABASE_VERSION";
    private static int REFLECTED_DATABASE_VERSION;

    private static final String tableNameVariableName = "TABLE_NAME";
    private static String REFLECTED_TABLE_NAME;

    private static final String columnNewsUpdateVariableName = "COLUMN_UPDATE";
    static String REFLECTED_COLUMN_UPDATE;

    private static final String columnNewsTypeVariableName = "COLUMN_TYPE";
    static String REFLECTED_COLUMN_TYPE;

    private static final String columnNewsTitleVariableName = "COLUMN_TITLE";
    static String REFLECTED_COLUMN_TITLE;

    private static final String columnNewsThumbVariableName = "COLUMN_THUMB";
    static String REFLECTED_COLUMN_THUMB;

    private static final String columnNewsShareUrlVariableName = "COLUMN_SHARE_URL";
    static String REFLECTED_COLUMN_SHARE_URL;

    private static final String columnNewsWebViewUrlVariableName = "COLUMN_WEBVIEW_URL";
    static String REFLECTED_COLUMN_WEB_VIEW;

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {
            newsEntryClass = Class.forName(newsEntryName);




            if (!BaseColumns.class.isAssignableFrom(newsEntryClass)) {
                String newsEntryDoesNotImplementBaseColumns = "NewsEntry class needs to " +
                        "implement the interface BaseColumns, but does not.";
                fail(newsEntryDoesNotImplementBaseColumns);
            }

            REFLECTED_TABLE_NAME = getStaticStringField(newsEntryClass, tableNameVariableName);
            REFLECTED_COLUMN_UPDATE = getStaticStringField(newsEntryClass, columnNewsUpdateVariableName);
            REFLECTED_COLUMN_TYPE = getStaticStringField(newsEntryClass, columnNewsTypeVariableName);
            REFLECTED_COLUMN_TITLE = getStaticStringField(newsEntryClass, columnNewsTitleVariableName);
            REFLECTED_COLUMN_THUMB = getStaticStringField(newsEntryClass, columnNewsThumbVariableName);
            REFLECTED_COLUMN_SHARE_URL = getStaticStringField(newsEntryClass, columnNewsShareUrlVariableName);
            REFLECTED_COLUMN_WEB_VIEW = getStaticStringField(newsEntryClass, columnNewsWebViewUrlVariableName);

            feedDbHelperClass = Class.forName(feedDbHelperName);

            REFLECTED_DATABASE_NAME = getStaticStringField(feedDbHelperClass, databaseNameVariableName);
            REFLECTED_DATABASE_VERSION = getStaticIntegerField(feedDbHelperClass, databaseVersionVariableName);

            Constructor feedDbHelperCtor = feedDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) feedDbHelperCtor.newInstance(context);

            context.deleteDatabase(REFLECTED_DATABASE_NAME);

            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);



        } catch (ClassNotFoundException e) {
            fail(readableClassNotFound(e));
        } catch (NoSuchFieldException e) {
            fail(readableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }



    @Test
    public void testDatabaseVersionWasIncremented() {
        int expectedDatabaseVersion = 1;
        String databaseVersionShouldBe1 = "Database version should be "
                + expectedDatabaseVersion + " but isn't."
                + "\n Database version: ";
        assertEquals(databaseVersionShouldBe1,expectedDatabaseVersion,REFLECTED_DATABASE_VERSION);
    }


    /**
     * Colunas com valores nulos não podem ser inseridas
     */
    @Test
    public void testNullColumnConstraints() {
        Cursor FeedTableCursor = database.query(REFLECTED_TABLE_NAME,null, null, null, null, null, null);

        String[] feedTableColumnNames = FeedTableCursor.getColumnNames();
        FeedTableCursor.close();

        ContentValues testValues = TestUtilities.createTestFeedContentValues();
        ContentValues testValuesReferenceCopy = new ContentValues(testValues);

        for (String columnName : feedTableColumnNames) {
            if (columnName.equals(FeedContract.NewsEntry._ID)) continue;

            testValues.putNull(columnName);

            long shouldFailRowId = database.insert(
                    REFLECTED_TABLE_NAME,
                    null,
                    testValues);

            String variableName = getConstantNameByStringValue(FeedContract.NewsEntry.class,columnName);

            String nullRowInsertShouldFail =
                    "Insert should have failed due to a null value for column: '" + columnName + "'"
                            + ", but didn't."
                            + "\n Check that you've added NOT NULL to " + variableName
                            + " in your create table statement in the FeedEntry class."
                            + "\n Row ID: ";
            assertEquals(nullRowInsertShouldFail,-1,shouldFailRowId);

            testValues.put(columnName, testValuesReferenceCopy.getAsString(columnName));
        }
        dbHelper.close();
    }

    /**
     * Testa se o valor da coluna _ID esta incrementando
     */
    @Test
    public void testIntegerAutoincrement() {
        testInsertSingleRecordIntoNewsTable();

        ContentValues testFeedValues = TestUtilities.createTestFeedContentValues();

        long originalDate = testFeedValues.getAsLong(REFLECTED_COLUMN_UPDATE);

        long firstRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testFeedValues);

        database.delete(
                REFLECTED_TABLE_NAME,
                "_ID == " + firstRowId,
                null);


        long dayAfterOriginalDate = originalDate + TimeUnit.DAYS.toMillis(1);
        testFeedValues.put(REFLECTED_COLUMN_UPDATE, dayAfterOriginalDate);

        long secondRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testFeedValues);

        String sequentialInsertsDoNotAutoIncrementId = "IDs were reused and shouldn't be if autoincrement is setup properly.";
        assertNotSame(sequentialInsertsDoNotAutoIncrementId,
                firstRowId, secondRowId);
    }

    /**
     * Testa o comportamento quando o banco de dados é atualizado como o banco faz apenas um cache das informações
     * as tabelas devem ser recriadas.
     */
    @Test
    public void testOnUpgradeBehavesCorrectly() {

        testInsertSingleRecordIntoNewsTable();

        dbHelper.onUpgrade(database, 13, 14);


        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + REFLECTED_TABLE_NAME + "'",
                null);


        int expectedTableCount = 1;
        String shouldHaveSingleTable = "There should only be one table returned from this query.";
        assertEquals(shouldHaveSingleTable,
                expectedTableCount,
                tableNameCursor.getCount());

        tableNameCursor.close();

        Cursor shouldBeEmptyFeedCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int expectedRecordCountAfterUpgrade = 0;
        String newsTableShouldBeEmpty =
                "News table should be empty after upgrade, but wasn't."
                        + "\nNumber of records: ";
        assertEquals(newsTableShouldBeEmpty,
                expectedRecordCountAfterUpgrade,
                shouldBeEmptyFeedCursor.getCount());

        database.close();
    }

    /**
     * Este teste verifica se o banco foi criado com sucesso e possui todos as tabelas que deve ter
     * <p>
     * {@link com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry#TABLE_NAME}.
     * <p>
     *
     */
    @Test
    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(REFLECTED_TABLE_NAME);
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,true,database.isOpen());
        Cursor tableNameCursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
        String errorInCreatingDatabase = "Error: This means that the database has not been created correctly";

        assertTrue(errorInCreatingDatabase, tableNameCursor.moveToFirst());
        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());

        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());

        tableNameCursor.close();
    }

    /**
     * Testa a inclusao de um registro na tabela news
     *
     */
    @Test
    public void testInsertSingleRecordIntoNewsTable() {
        ContentValues testFeedValues = TestUtilities.createTestFeedContentValues();

        long newsRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testFeedValues);

        int valueOfIdIfInsertFails = -1;
        String insertFailed = "Unable to insert into the database";
        assertNotSame(insertFailed,valueOfIdIfInsertFails,newsRowId);

        Cursor newsCursor = database.query(REFLECTED_TABLE_NAME,null,null,null,null,null,null);

        String emptyQueryError = "Error: No Records returned from news query";
        assertTrue(emptyQueryError,newsCursor.moveToFirst());

        String expectedNewsDidntMatchActual = "Expected news values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedNewsDidntMatchActual,newsCursor,testFeedValues);

        assertFalse("Error: More than one record returned from news query", newsCursor.moveToNext());
        newsCursor.close();
    }
}