package crabb.andre.AppWatch;

import java.util.ArrayList;
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

    private Hashtable<String, AppData> mDataTable;
    private static DataController instance;

    // -- METHODS -------------------------------------------------------------
    private DataController() {
        mDataTable = new Hashtable<String, AppData>();
    }
    // ------------------------------------------------------------------------
    public static DataController getInstance() {
        if (instance == null) {
            instance = new DataController();
        }
        return instance;
    }
    // ------------------------------------------------------------------------
    public boolean addSecondsToApp(String packageName, int seconds) {
        if (packageName == null) {
            return false;
        }
        return getOrCreateData(packageName).addSeconds(seconds);
    }
    // ------------------------------------------------------------------------
    public boolean incrementTimesOpenedToApp(String packageName) {
        if (packageName == null) {
            return false;
        }
        return getOrCreateData(packageName).incrementTimesOpened();
    }
    // ------------------------------------------------------------------------
    public ArrayList<AppData> getAppDatas() {
        return new ArrayList<AppData>(mDataTable.values());
    }
    // ------------------------------------------------------------------------
    /*
     * Returns the AppData obect for the given 'packageName'.
     *  Creates it of necessary.
     */
    public AppData getOrCreateData(String packageName) {
        AppData retVal;
        if (!mDataTable.containsKey(packageName)) {
            retVal = new AppData(packageName);
            mDataTable.put(packageName, retVal);
        }
        return mDataTable.get(packageName);
    }
}
