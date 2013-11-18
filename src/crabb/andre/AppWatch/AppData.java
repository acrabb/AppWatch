package crabb.andre.AppWatch;

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
        How long did I spend on Facebook today? This week?
        How many times did I open Facebook today?
     */

    private String  mPackageName;
    private int     mTimesOpened;
    private int     mSecondsTotal;
    private int     mSeconds;
    private int     mMinutes;
    private int     mHours;
    private int     mDays;

    final String    TAG = "ACAC-AppData";

    // -- Contructors ---------------------------------------------------------
    // ------------------------------------------------------------------------
    public AppData (String packageName) {
        this.mPackageName = packageName;
        this.mTimesOpened = 0;
        this.mSeconds = 0;
    }

    // -- METHODS -------------------------------------------------------------
    // ------------------------------------------------------------------------
    public boolean incrementTimesOpened() {
        return incrementTimesOpened(1);
    }
    // ------------------------------------------------------------------------
    private boolean incrementTimesOpened(int num) {
        if (num < 0) {
            Log.d(TAG, "!> Trying to add negative times opened.");
            return false;
        }
        this.mTimesOpened += num;
        return true;
    }
    // ------------------------------------------------------------------------
    public boolean addSeconds(int seconds) {
        if (seconds < 0) {
            Log.d(TAG, "!> Trying to add negative seconds.");
            return false;
        }
        this.mSeconds += seconds;
        return true;
    }

    // -- GETTERS & SETTERS ---------------------------------------------------
    // ------------------------------------------------------------------------
    public String getPackageName() {
        return mPackageName;
    }
    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }
    public int getTimesOpened() {
        return mTimesOpened;
    }
    public int getSecondsSpent() {
        return mSeconds;
    }

    // -- Comparable ----------------------------------------------------------
    @Override
    public int compareTo(Object o) {
        AppData ad = (AppData) o;
        return (this.mSeconds < ad.getSecondsSpent()) ? -1 :
                (this.mSeconds == ad.getSecondsSpent() ? 0 : 1);
    }
}
