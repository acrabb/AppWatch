package crabb.andre.AppWatch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends Activity {

    final String            TAG = "ACACACAC";
    public static PackageManager  pm;

    private Intent          intent;
    private DataController  mDataController;
//    private LinearLayout    mListOfApps;
    private GridView        mAppGrid;
    private AppDataAdapter  mAppAdapter;

    // ------------------------------------------------------------------------
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDataController = DataController.getInstance();
        pm = getPackageManager();

        fireUpService();
        setUpView();
    }
    // ------------------------------------------------------------------------
    private void fireUpService() {
        intent = new Intent(this, WatcherService.class);
        startService(intent);
    }
    // ------------------------------------------------------------------------
    private void setUpView() {
//        mListOfApps = (LinearLayout) findViewById(R.id.listLayout);
        mAppGrid = (GridView) findViewById(R.id.app_grid);
        mAppAdapter = new AppDataAdapter(getApplicationContext(),
                                                R.layout.app_cell,
                                                mDataController.getAppDatas());
        mAppGrid.setAdapter(mAppAdapter);
        // Set View to update every second.
    }
    // ------------------------------------------------------------------------
    /*private void updateView() {
//        mListOfApps.removeAllViews();
        mAppGrid.removeAllViews();

        ArrayList<AppData> datas = mDataController.getAppDatas();
        Collections.sort(datas);
        Collections.reverse(datas);
        Log.d(TAG, String.format(">> APP UPDATING VIEW WITH: %s", datas));

        TextView    tTextView;
        String      tString, display;
        ApplicationInfo info;

        for (AppData ad : datas) {
            tTextView = new TextView(getApplicationContext());
            tTextView.setTextSize(30);
            display = "Some name";
            info = null;
            try {
                info = pm.getApplicationInfo(ad.getPackageName(), PackageManager.GET_META_DATA);
                display = pm.getApplicationLabel(info).toString();
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, "!> NameNotFound :(");
            }
            tString = String.format("%s;\t\n\t\t\tTimeSpent: %d;\t\tTimesOpened: %d.",
                                    display,
//                                    ad.getPackageName(),
                                    ad.getSecondsSpent(),
                                    ad.getTimesOpened());
            tTextView.setText(tString);
            mListOfApps.addView(tTextView);
        }
    }*/
    // ------------------------------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        Log.d(TAG, ">> APP ON PAUSE!");
    }
    // ------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        Log.d(TAG, ">> APP ON RESUME!");
        mAppAdapter.notifyDataSetChanged();
//        updateView();
    }
}
