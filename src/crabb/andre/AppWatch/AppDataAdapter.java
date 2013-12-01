package crabb.andre.AppWatch;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/13/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppDataAdapter extends ArrayAdapter<AppData> {

    private int                 resourceId;
    private Context             context;
    private AppData             app;
    private LayoutInflater      inflater;
    private ArrayList<AppData>  datas;

    private final String TAG    = "ACAC-Adapter";
    public PackageManager pm = MainActivity.pm;


    public AppDataAdapter(Context context, int resource, ArrayList<AppData> objects) {
        super(context, R.layout.app_cell, objects);
        this.context = context;
        this.datas = objects;
        Collections.sort(datas);
        Collections.reverse(datas);
        this.resourceId = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View            gridCell;
        ImageView       image;
        TextView        text;
        TextView        time;
        ApplicationInfo info;

        // See if you can recycle a view
        if (convertView == null) {
            gridCell = inflater.inflate(resourceId, parent, false);
        } else {
            gridCell = convertView;
        }

        app = datas.get(position);

        text = (TextView) gridCell.findViewById(R.id.title);
        time = (TextView) gridCell.findViewById(R.id.time);
        image = (ImageView) gridCell.findViewById(R.id.image);
        try {
            info = pm.getApplicationInfo(app.getPackageName(), PackageManager.GET_META_DATA);
            text.setText(pm.getApplicationLabel(info).toString());
            time.setText(Utils.secondsToClockTimeString(app.getSeconds()));
//            time.setText(String.format("%d", app.getSeconds()));
            image.setImageDrawable(pm.getApplicationIcon(info));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "!> NameNotFound :(");
        }
        return gridCell;
    }
}
