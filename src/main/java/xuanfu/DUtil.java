package xuanfu;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 创建日期： 2015/11/12.
 */
public class DUtil {

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    private boolean isAppOnForeground(Context mContext) {
        ActivityManager activityManager =(ActivityManager) mContext.getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = mContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
    public static  boolean isGetPermission(Context mContext){
        PackageManager pm = mContext.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", "packageName"));
        if (permission) {
            return true;
        }else {
            return false;
        }
    }
}
