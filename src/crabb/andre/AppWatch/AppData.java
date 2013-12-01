package crabb.andre.AppWatch;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/13/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppData implements Comparable {
    /*
        This represents one row in the database table.
        PackageName | timestamp | numSeconds | id
     */

    private long        mId;
    private long        mTimestamp;
    private String      mPackageName;
    private long         mSeconds;


    final String    TAG = "ACAC-AppData";

    // -- Contructors ---------------------------------------------------------
    // ------------------------------------------------------------------------
    public AppData (String packageName, long timestamp, long seconds) {
        this.mPackageName   = packageName;
        this.mTimestamp     = timestamp;
        this.mSeconds       = seconds;
    }

    // -- GETTERS & SETTERS ---------------------------------------------------
    // ------------------------------------------------------------------------
    public String getPackageName() {
        return mPackageName;
    }
    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }
    public long getSeconds() {
        return mSeconds;
    }
    public long getId() {
        return mId;
    }
    public void setId(long mId) {
        this.mId = mId;
    }
    public long getTimestamp() {
        return mTimestamp;
    }
    public void setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }


    public String toString() {
        return mPackageName + " " + mSeconds;
    }

    @Override
    public int compareTo(Object o) {
        AppData other = (AppData) o;
        return (this.mTimestamp < other.getTimestamp()) ? -1 :
                (this.mTimestamp == other.getTimestamp() ? 0 : 1);
    }
}
