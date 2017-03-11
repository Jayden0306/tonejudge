package group6.tcss450.uw.edu.tonejudge.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import group6.tcss450.uw.edu.tonejudge.R;

/**
 * Created by Jayden on 2/28/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "ToneJudge.db";

    /**
     * table name
     */
    private static final String TABLE_RESULT = "Result";

    /**
     * create result table string
     */
    private final String CREATE_RESULT_SQL;

    /**
     * drop result table string
     */
    private final String DROP_RESULT_SQL;

    /**
     * column names
     */
    private final String[] COLUMN_NAMES;

    /**
     * the sqlite database
     */
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * initialize the database
     * @param context the current activity's context
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        CREATE_RESULT_SQL = context.getString(R.string.CREATE_RESULT_SQL);
        DROP_RESULT_SQL = context.getString(R.string.DROP_RESULT_SQL);
        COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES);
        mSQLiteDatabase = this.getWritableDatabase();
    }

    /**
     * create the result table in the database
     * @param sqLiteDatabase the sqlite database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RESULT_SQL);
    }

    /**
     * update the table with newer version
     * @param sqLiteDatabase the sqlite database
     * @param oldVersion older version of the table
     * @param newVersion newer version of the table
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop the older table if existed
        sqLiteDatabase.execSQL(DROP_RESULT_SQL);

        //create tables again
        onCreate(sqLiteDatabase);
    }

    /**
     * add the result data and store into the sqlite database
     * @param tone
     */
    public void addResult(ToneModel tone) {

        //create a new map of values, where colun name are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMES[0], tone.getID());
        values.put(COLUMN_NAMES[1], tone.getmMessage());
        values.put(COLUMN_NAMES[2], tone.getAnger());
        values.put(COLUMN_NAMES[3], tone.getDisgust());
        values.put(COLUMN_NAMES[4], tone.getFear());
        values.put(COLUMN_NAMES[5], tone.getJoy());
        values.put(COLUMN_NAMES[6], tone.getSadness());
        values.put(COLUMN_NAMES[7], tone.getAnalytical());
        values.put(COLUMN_NAMES[8], tone.getConfident());
        values.put(COLUMN_NAMES[9], tone.getTentative());
        values.put(COLUMN_NAMES[10], tone.getOpenness());
        values.put(COLUMN_NAMES[11], tone.getConscientiousness());
        values.put(COLUMN_NAMES[12], tone.getExtraversion());
        values.put(COLUMN_NAMES[13], tone.getAgreeableness());
        values.put(COLUMN_NAMES[14], tone.getEmotionalRange());

        //insert the new row, returning the primary value of the new row
        mSQLiteDatabase.insert(TABLE_RESULT, null, values);
    }

    /**
     * close database connection
     */
    public void closeDataBase() {
        mSQLiteDatabase.close();
    }

    /**
     * the result of the database query stores into tone model objects
     * @return a list of tone model
     */
    public List<ToneModel> getAllScores() {
        List<ToneModel> toneList = new ArrayList<>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RESULT;
        Cursor cursor = mSQLiteDatabase.query(
                TABLE_RESULT,  // The table to query
                COLUMN_NAMES,                               // The COLUMN_NAMES to return
                null,                                // The COLUMN_NAMES for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        //move the cursor into the first row
        cursor.moveToFirst();

        //loop through rows of results
        //each row's data store into the tone model object
        //and each tone model object store into arraylist
        for(int i = 0; i < cursor.getCount(); i++) {
            ToneModel tone = new ToneModel();
            tone.setID(cursor.getLong(0));
            tone.setmMessage(cursor.getString(1));
            tone.setAnger(cursor.getString(2));
            tone.setDisgust(cursor.getString(3));
            tone.setFear(cursor.getString(4));
            tone.setJoy(cursor.getString(5));
            tone.setSadness(cursor.getString(6));
            tone.setAnalytical(cursor.getString(7));
            tone.setConfident(cursor.getString(8));
            tone.setTentative(cursor.getString(9));
            tone.setOpenness(cursor.getString(10));
            tone.setConscientiousness(cursor.getString(11));
            tone.setExtraversion(cursor.getString(12));
            tone.setAgreeableness(cursor.getString(13));
            tone.setEmotionalRange(cursor.getString(14));
            cursor.moveToNext();
            toneList.add(tone);
        }

        cursor.close();
        return toneList;
    }
}
