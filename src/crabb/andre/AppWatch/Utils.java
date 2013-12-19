package crabb.andre.AppWatch;

/**
 * Created with IntelliJ IDEA.
 * User: Andre
 * Date: 11/19/13
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public final static String LAUNCHER = "com.android.launcher";
    public final static String SYSTEM_UI = "com.android.systemui";


    private final static int SECOND    = 1;
    private final static int MINUTE    = 60 * SECOND;
    private final static int HOUR      = 60 * MINUTE;
    private final static int DAY       = 24 * HOUR;
    private final static int WEEK      = 7 * DAY;
    private final static int MONTH     = 31 * DAY;
    private final static int YEAR      = 12 * MONTH;
    public static final String OVERALL = "Overall Usage";

    public static String secondsToClockTimeString(long seconds) {
        int hours = (int) (seconds / HOUR);
        seconds = seconds % HOUR;
        int minutes = (int) (seconds / MINUTE);
        seconds = seconds % MINUTE;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String secondsToTotalTimeString(int seconds) {
        StringBuilder sb = new StringBuilder();
        int value;
        if (seconds > YEAR) {
            value = seconds / YEAR;
            sb.append(String.format("%d Years", value));
            seconds = seconds % YEAR;
        }
        if (seconds > MONTH) {
            value = seconds / MONTH;
            sb.append(String.format("%d Months", value));
            seconds = seconds % MONTH;
        }
        if (seconds > WEEK) {
            value = seconds / WEEK;
            sb.append(String.format("%d Weeks", value));
            seconds = seconds % WEEK;
        }
        if (seconds > DAY) {
            value = seconds / DAY;
            sb.append(String.format("%d Days", value));
            seconds = seconds % DAY;
        }
        if (seconds > HOUR) {
            value = seconds / HOUR;
            sb.append(String.format("%d Years", value));
            seconds = seconds % HOUR;
        }
        if (seconds > MINUTE) {
            value = seconds / MINUTE;
            sb.append(String.format("%d Minute", value));
            seconds = seconds % MINUTE;
        }
        if (seconds > SECOND) {
            value = seconds / SECOND;
            sb.append(String.format("%d Seconds", value));
            seconds = seconds % SECOND;
        }


        return sb.toString();
    }
}
