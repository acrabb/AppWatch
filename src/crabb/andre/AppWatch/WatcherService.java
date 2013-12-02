package crabb.andre.AppWatch;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
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
    private String          mLastActivePackageName = null;
    private List<ActivityManager.RunningTaskInfo> taskInfos = null;
    private ActivityManager mActivityManager = null;
    private int             sessionSeconds = 0;
    private KeyguardManager mKeyguardManager;

    private final int       INTERVAL_SECONDS = 1;
    private final int       INTERVAL_MILLI_SECONDS = INTERVAL_SECONDS * 1000;
    private final String    TAG = "ACAC-SERVICE";
    private boolean isSetUp = false;


    // ------------------------------------------------------------------------
    public WatcherService() {
        super("WatcherService");
        mDataController = DataController.getInstance(this);
        isSetUp = false;
    }
    // ------------------------------------------------------------------------
    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isSetUp) {
            mActivityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            mKeyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
            isSetUp = true;
        }
        while (true) {
            // If screen is locked...don't do anything.
            if(mKeyguardManager.inKeyguardRestrictedInputMode()) {
                continue;
            }
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
        if (!packageName.equals(mLastActivePackageName) && mLastActivePackageName != null) {
            if (sessionSeconds > 0) {
                mDataController.addAppData(mLastActivePackageName, sessionSeconds);
            }
            sessionSeconds = 0;
            Log.d(TAG, String.format(">> Last package: %s, this package: %s", mLastActivePackageName, packageName));
        } else {
            sessionSeconds++;
        }
        mLastActivePackageName = packageName;
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
