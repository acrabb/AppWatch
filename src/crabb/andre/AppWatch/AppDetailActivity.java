package crabb.andre.AppWatch;


import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_detail);
        Bundle bundle = getIntent().getExtras();
        packageName = bundle.getString("packageName");


        pm = getPackageManager();
        try {
            info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        title = (TextView) findViewById(R.id.app_name);
        title.setText(pm.getApplicationLabel(info));

    }
}
