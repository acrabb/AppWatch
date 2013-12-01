package crabb.andre.AppWatch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    final String            TAG = "ACACACAC";
    public static PackageManager  pm;

    private Intent          mServiceIntent;
    private Intent          mActivityIntent;
    private DataController  mDataController;
    private GridView        mAppGrid;
    private AppDataAdapter  mAppAdapter;

    // ------------------------------------------------------------------------
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.main);

        mDataController = DataController.getInstance(getApplicationContext());
        pm = getPackageManager();

        fireUpService();
        setUpView();
    }
    // ------------------------------------------------------------------------
    private void fireUpService() {
        // TODO Check if its already running?
        // Schedule for it to autostart itself again later?
        mServiceIntent = new Intent(this, WatcherService.class);
        startService(mServiceIntent);
    }
    // ------------------------------------------------------------------------
    private void setUpView() {
//        mListOfApps = (LinearLayout) findViewById(R.id.listLayout);
        mAppGrid = (GridView) findViewById(R.id.app_grid);
        mAppGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Log.d(TAG, String.format(">> CLICKED ITEM %d", position));
                AppData ad = (AppData) adapterView.getItemAtPosition(position);
                mActivityIntent = new Intent(view.getContext(), AppDetailActivity.class);
                mActivityIntent.putExtra("packageName", ad.getPackageName());
                startActivity(mActivityIntent);
            }
        });
        mAppAdapter = new AppDataAdapter(getApplicationContext(),
                                                R.layout.app_cell,
                                                mDataController.getAppDatas());
        mAppGrid.setAdapter(mAppAdapter);
        // Set View to update every second.
    }
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
