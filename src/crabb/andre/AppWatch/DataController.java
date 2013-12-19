package crabb.andre.AppWatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/13/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

/*
 * This class is the middleman between the MainActivity and the model data.
 */
public class DataController {

    private static DataController instance;
    private static String TAG = "ACAC-DC";

    private SQLiteDatabase mDatabase;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = MySQLiteHelper.allColumns;
    private Hashtable<String, AppData> mCombinedTable;


    // -- METHODS -------------------------------------------------------------
    private DataController(Context context) {
        dbHelper = new MySQLiteHelper(context);
        mCombinedTable = new Hashtable<String, AppData>();
    }
    // ------------------------------------------------------------------------
    public static DataController getInstance(Context context) {
        if (instance == null) {
            instance = new DataController(context);
        }
        return instance;
    }
    // ------------------------------------------------------------------------
    public void open() {
        mDatabase = dbHelper.getWritableDatabase();
    }
    // ------------------------------------------------------------------------
    public void close() {
        dbHelper.close();
    }
    // ------------------------------------------------------------------------
    public AppData addAppData(String packageName, int seconds) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PACKAGE_NAME, packageName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());
        values.put(MySQLiteHelper.COLUMN_NUM_SECONDS, seconds);
        open();
        long insertId = mDatabase.insert(MySQLiteHelper.TABLE_APPS, null, values);
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS,
                                        allColumns,
                                        MySQLiteHelper.COLUMN_ID + " = " + insertId,
                                        null, null, null, null);
        cursor.moveToFirst();
        AppData appData = cursorToAppData(cursor);
        cursor.close();
        close();
        return appData;
    }

    // ------------------------------------------------------------------------
    private AppData cursorToAppData(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_PACKAGE_NAME));
        if (name == null) {
            return null;
        }
        AppData appData =
            new AppData(name,
                    cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TIMESTAMP)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_NUM_SECONDS))
                    );
        appData.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ID)));
        return appData;
    }




    /*
     * Get all rows and all columns
     */
    public ArrayList<AppData> getAppDatasAll() {
        return makeQuery(null, null);
    }
    // ------------------------------------------------------------------------
    public ArrayList<AppData> getAppDatasWithName(String packageName) {
        return makeQuery("package_name = ?", new String[]{packageName});
    }
    // ------------------------------------------------------------------------
    public ArrayList<AppData> getAppDatasWithNameFromDate(String packageName, long dateInMs) {
        String dateString = String.format("%d", dateInMs);
        return makeQuery("package_name = ? and (timestamp > ?)", new String[] {packageName, dateString});
    }
    // ------------------------------------------------------------------------
    private ArrayList<AppData> makeQuery(String query, String[] params) {
        ArrayList<AppData> datas = new ArrayList<AppData>();
        open();
        Log.i(TAG, String.format("DC> Making query '%s' with params: %s", query, params));
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS, allColumns,
                                        query, params, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            datas.add(cursorToAppData(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        Log.i(TAG, ">> Returning app datas: " + datas);
        Collections.reverse(datas);
        return datas;
    }
    /*
        This aggregates all the rows in the database for each app
        into one object.
     */
    public Collection<AppData> getCombinedAppDatas() {
        mCombinedTable.clear();
        open();
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        AppData ad = null;
        String adName;

        int totalUsageSeconds = 0;
        int tempSec = 0;
        while(!cursor.isAfterLast()) {
            adName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_PACKAGE_NAME));
            if (adName == null) {
                Log.e(TAG, "!> Null name from cursor.");
                break;
            }
            ad = getOrCreateData(adName, mCombinedTable);
            tempSec = (int) cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_NUM_SECONDS));
            ad.addSeconds(tempSec);
            totalUsageSeconds += tempSec;
            ad.incrementTimesOpened();
            cursor.moveToNext();
        }
        cursor.close();
        close();
        ad = new AppData(Utils.OVERALL);
        ad.addSeconds(totalUsageSeconds);
        Collection<AppData> vals = mCombinedTable.values();
        vals.add(ad);
        return vals;
    }


    // Returns the AppData obect for the given 'packageName'.
    //  Creates it of necessary.
    public AppData getOrCreateData(String packageName, Hashtable<String, AppData> ht) {
        if (packageName == null) {
            return null;
        }
        AppData retVal;
        if (!ht.containsKey(packageName)) {
            retVal = new AppData(packageName);
            ht.put(packageName, retVal);
        }
        return ht.get(packageName);
    }

}
