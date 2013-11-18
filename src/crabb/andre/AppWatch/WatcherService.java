package crabb.andre.AppWatch;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: André
 * Date: 11/12/13
 * Time: 2:29 PM
 */
public class WatcherService extends IntentService {

    private DataController  mDataController = null;
    private String mLastActivePackageName = null;
    private List<ActivityManager.RunningTaskInfo> taskInfos = null;
    private ActivityManager mActivityManager = null;

    private final int       INTERVAL_SECONDS = 1;
    private final int       INTERVAL_MILLI_SECONDS = INTERVAL_SECONDS * 1000;
    private final String    TAG = "ACAC-SERVICE";


    // ------------------------------------------------------------------------
    public WatcherService() {
        super("WatcherService");
        mDataController = DataController.getInstance();
    }
    // ------------------------------------------------------------------------
    @Override
    protected void onHandleIntent(Intent intent) {
        mActivityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        while (true) {
            // Get current class name
            taskInfos = mActivityManager.getRunningTasks(1);
            String packageName = taskInfos.get(0).topActivity.getPackageName();
            doStuff(packageName);
            synchronized (this) {
                try {
                    wait(INTERVAL_MILLI_SECONDS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Exception in onHandleIntent");
                }
            }
        }
    }
    // ------------------------------------------------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "S> ON START COMMAND");
        return START_STICKY;
    }

    // -- ADD TO DATA ---------------------------------------------------------
    // ------------------------------------------------------------------------
    private void doStuff(String packageName) {
        if (packageName == null) {
            Log.d(TAG, "!> Null packageName in doStuff :(");
            return;
        }
        if (!packageName.equals(mLastActivePackageName)) {
            mLastActivePackageName = packageName;
            mDataController.incrementTimesOpenedToApp(packageName);
        }
        mDataController.addSecondsToApp(mLastActivePackageName, INTERVAL_SECONDS);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "S> ON DESTROY");
        super.onDestroy();
    }

//    @Override
//    public void onCreate() {
//        Log.d(TAG, "S> ON CREATE");
//        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    @Override
//    public ComponentName startService(Intent service) {
//        Log.d(TAG, "S> START SERVICE");
//        return super.startService(service);    //To change body of overridden methods use File | Settings | File Templates.
//    }

}
