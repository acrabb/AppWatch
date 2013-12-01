package crabb.andre.AppWatch;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/19/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_APPS = "seconds";

    public static final String COLUMN_ID            = "_id";
    public static final String COLUMN_TIMESTAMP     = "timestamp";
    public static final String COLUMN_NUM_SECONDS   = "num_seconds";
    public static final String COLUMN_PACKAGE_NAME  = "package_name";
    public static final String[] allColumns = {COLUMN_ID,
                                                COLUMN_PACKAGE_NAME,
                                                COLUMN_TIMESTAMP,
                                                COLUMN_NUM_SECONDS};

    private static final String DATABASE_NAME = "seconds.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_APPS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PACKAGE_NAME + " text, "
            + COLUMN_TIMESTAMP + " integer (long), "
            + COLUMN_NUM_SECONDS + " integer (long)"
            + " );";


      public MySQLiteHelper(Context context) {
          super (context, DATABASE_NAME, null, DATABASE_VERSION);
      }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(sqLiteDatabase);
    }
}
