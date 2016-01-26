package xuanfu;

import java.util.LinkedList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * 跟包(应用程序)有关的工具
 * 实现功能包括查看当前界面Activity属于哪个包，打开指定应用等
 * @author 思落羽
 * 2014年8月27日 下午2:57:35
 *
 */
public class PackageUtil {
	
	private Context mContext;
	
	public PackageUtil(Context context) {
		mContext = context;
	}
	
	/**
	 * 当前前台为游戏
	 * @return
	 */
	public boolean currentIsGame() {
		// TODO
		return true;
	}
	
	/**
	 * 当前界面是否为主界面
	 * @return 为Home界面则返回true，否则为false
	 */
	public boolean currentIsLauncher() {
			return getHomes().contains(getFrontPackage());
	}
	
	/**
	 * 当前为rkHelper应用在前台
	 * @return
	 */
	public boolean currentIsRKHelper() {
		return getFrontPackage().equals(mContext.getPackageName());
	}
	
	/**
	 * 当前界面主界面的包名
	 * @return 为Home界面则返回true，否则为false
	 */
	public String getFrontPackage() {
		/* 获取 ActivityManager ，检查是否存在主界面 */ 
		ActivityManager activityManager = 
				(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		/* 获取当前正在运行的任务 */
		List<RunningTaskInfo> infos = activityManager.getRunningTasks(1);
		/* RunningTaskInfo 是任务栈信息，得到栈顶的Activity再去获取期包名 */
		return infos.get(0).topActivity.getPackageName();
	}
	
	/**
	 * 通过包名打开app
	 * @param context
	 * @param packageName
	 */
	public void startPackege(String packageName) throws NameNotFoundException {
		// 通过 PackageManager 获取到包的启动Intent，之后直接启动即可
		Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
		mContext.startActivity(launchIntent);
	}
	
	/**
	 * 通过包名获取app名称
	 * @param context
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException 
	 */
	public String getPackageLabel(String packageName) throws NameNotFoundException {
		PackageManager packageManager = mContext.getPackageManager();
		return packageManager.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES).name;
	}
	
	/**
	 * 通过包名获取app Logo
	 * @param context
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException
	 */
	public Drawable getPackageLogo(String packageName) throws NameNotFoundException {
		return mContext.getPackageManager().getApplicationIcon(packageName);
	}
	
	/**
	 * 获取到桌面应用的应用名称
	 * @return
	 */
	public List<String> getHomes() {
		
		List<String> names = new LinkedList<String>();
		PackageManager packageManager = mContext.getPackageManager();
		/* 找出action为 Intent.ACTION_MAIN，
		 * Category为Intent.CATEGORY_HOME的包的包名，这个包就是主界面的包 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		/* 查找intent可以唤醒的Activity的信息 */
		List<ResolveInfo> resolveInfos = packageManager
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resolveInfos) {
			names.add(resolveInfo.activityInfo.packageName);
		}
		return names;
	}
	
	/**
	 * @Title: isPackageExists
	 * @Description: 
	 *      判断应用是否安装，传递的是应用的包名
	 * 
	 * @param targetPackage
	 * @return      
	 * @throws
	 */ 
	public boolean isPackageExists(String targetPackage) { 
	    List<ApplicationInfo> packages; 
	    PackageManager pm; 
	    pm = mContext.getPackageManager(); 
	    packages = pm.getInstalledApplications(0); 
	    for (ApplicationInfo packageInfo : packages) { 
	        if (packageInfo.packageName.equals(targetPackage)) { 
	            return true; 
	        } 
	    } 
	    return false; 
	}  
	
}
