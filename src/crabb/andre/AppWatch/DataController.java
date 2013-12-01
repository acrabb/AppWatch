package crabb.andre.AppWatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

    private SQLiteDatabase mDatabase;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = MySQLiteHelper.allColumns;
//    private Hashtable<String, CombinedAppData> mDataTable;


    // -- METHODS -------------------------------------------------------------
    private DataController(Context context) {
//        mDataTable = new Hashtable<String, AppData>();
        dbHelper = new MySQLiteHelper(context);
    }
    // ------------------------------------------------------------------------
    public static DataController getInstance(Context context) {
        if (instance == null) {
            instance = new DataController(context);
        }
        return instance;
    }
    // ------------------------------------------------------------------------
    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }
    public AppData addAppData(String packageName, int seconds) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PACKAGE_NAME, packageName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());
        values.put(MySQLiteHelper.COLUMN_NUM_SECONDS, seconds);
        long insertId = mDatabase.insert(MySQLiteHelper.TABLE_APPS, null, values);
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS,
                                        allColumns,
                                        MySQLiteHelper.COLUMN_ID + " = " + insertId,
                                        null, null, null, null);
        cursor.moveToFirst();
        AppData appData = cursorToAppData(cursor);
        cursor.close();
        return appData;
    }

    private AppData cursorToAppData(Cursor cursor) {
        AppData appData =
            new AppData(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_PACKAGE_NAME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TIMESTAMP)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_NUM_SECONDS))
                    );
        appData.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ID)));
        return appData;
    }

    /*
        This aggregates all the rows in the database for a certain app
        into one object.
     */
    /*
    public ArrayList<CombinedAppData> getCombinedAppDatas() {
        // TODO
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS,
            allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        CombinedAppData cad = null;
        while(!cursor.isAfterLast()) {
            cad = getOrCreateData(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_PACKAGE_NAME)));
            cad.addSeconds(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_NUM_SECONDS));
            cad.incrementTimesOpened();
        }
    }

    // Returns the AppData obect for the given 'packageName'.
    //  Creates it of necessary.
    public AppData getOrCreateData(String packageName) {
        AppData retVal;
        if (!mDataTable.containsKey(packageName)) {
            retVal = new AppData(packageName);
            mDataTable.put(packageName, retVal);
        }
        return mDataTable.get(packageName);
    }


    // -- ADD TO CAD class ----------------------------------------------------------------------
    public boolean addSecondsToApp(String packageName, int seconds) {
        if (packageName == null) {
            return false;
        }
        return getOrCreateData(packageName).addSeconds(seconds);
    }
*/

    public ArrayList<AppData> getAppDatas() {
        ArrayList<AppData> datas = new ArrayList<AppData>();
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_APPS,
                    allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            datas.add(this.cursorToAppData(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return datas;
    }
    /*
        // ------------------------------------------------------------------------
        public boolean incrementTimesOpenedToApp(String packageName) {
            if (packageName == null) {
                return false;
            }
            return getOrCreateData(packageName).incrementTimesOpened();
        }
        // ------------------------------------------------------------------------
        // ------------------------------------------------------------------------
    */
}
