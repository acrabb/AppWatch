package crabb.andre.AppWatch;


import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/18/13
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppDetailActivity extends Activity {

    private PackageManager  pm;
    private TextView        title;
    private String          packageName;
    private ApplicationInfo info;
    private DataController  mDataController;
    private LinearLayout    mDataList;
    private LinearLayout    mDetailLayout;
    private LineGraph       mLineGraph;

    private static final String TAG     = "ACAC-Detail";

    private static final String HOUR    = "Hour";
    private static final String DAY     = "Day";
    private static final String WEEK    = "Week";
    private static final String MONTH   = "Month";
    private final String[] units = {HOUR, DAY, WEEK, MONTH};

    private static final HashMap<String, Long> mUnitMap;
    static {
        long SEC = 1000;
        long MIN = SEC * 60;
        long HR  = MIN * 60;
        long DY  = HR * 24;
        long WK  = DY * 7;
        long MTH = WK * 4;
        long YR  = MTH * 12;

        mUnitMap = new HashMap<String, Long>();
        mUnitMap.put(HOUR, HR);
        mUnitMap.put(DAY, DY);
        mUnitMap.put(WEEK, WK);
        mUnitMap.put(MONTH, MTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.app_detail);
        Bundle bundle = getIntent().getExtras();
        packageName = bundle.getString("packageName");
        mDataController = DataController.getInstance(this);


        pm = getPackageManager();
        try {
            info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setUpView();
    }

    public void setUpView() {
        // Get layout
        mDetailLayout = (LinearLayout) findViewById(R.id.detail_layout);

        // Set Title
        title = (TextView) findViewById(R.id.app_name);
        title.setText(pm.getApplicationLabel(info));

        // Get graph
        mLineGraph  = (LineGraph) findViewById(R.id.line_graph);
        //mDetailLayout.addView(mLineGraph);

        // Set Linear Layout
        mDataList = (LinearLayout) findViewById(R.id.data_list);
        ArrayList<AppData> al = mDataController.getAppDatasWithName(packageName);
        refreshViewWithData(al);

        // Set Buttons
        Button btn;
        for (String s : units) {
            btn = new Button(this);
            btn.setTag(s);
            btn.setText(String.format("Last %s", s));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked((String) v.getTag());
                }
            });
            mDetailLayout.addView(btn);
        }
    }

    private void refreshViewWithData(ArrayList<AppData> list) {
        mDataList.removeAllViews();
        TextView tv = new TextView(this);
        Date d;
        for (AppData ad : list) {
            tv = new TextView(this);
            d = new Date(ad.getTimestamp());
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            tv.setText(String.format("%s, \t %d", df.format(d), ad.getSeconds()));
            mDataList.addView(tv);
        }

        mLineGraph.removeAllLines();
        Line line = new Line();
        line.setColor(Color.parseColor("#55DDDD"));
        LinePoint lp;
        long maxSec = -1;
        for (AppData ad : list) {
            maxSec = Math.max(maxSec, ad.getSeconds());
            lp = new LinePoint((double) ad.getTimestamp(), (double) ad.getSeconds());
            line.addPoint(lp);
        }
        mLineGraph.addLine(line);
        mLineGraph.setRangeY(0, maxSec);
        mLineGraph.setLineToFill(0);
    }

    public void buttonClicked(String tag) {
        Log.i(TAG, String.format("Clicked button: %s", tag));
        // redo query with adjusted time
        ArrayList<AppData> list = mDataController.getAppDatasWithNameFromDate(packageName, System.currentTimeMillis() - mUnitMap.get(tag));
        refreshViewWithData(list);
    }
}
