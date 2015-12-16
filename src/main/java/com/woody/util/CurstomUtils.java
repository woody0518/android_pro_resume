package com.woody.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public final class CurstomUtils {
	private static CurstomUtils util = new CurstomUtils();

	private CurstomUtils() {
	}

	public static CurstomUtils getInstance() {
		return util;
	}

	public String getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		return year + "-" + month + "-" + day;
	}

	public String getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);

		return year + "年";
	}

	public int getViewHeight(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		int height = v.getMeasuredHeight();
		return height;
	}

	public int getViewWidth(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		int width = v.getMeasuredWidth();
		return width;
	}

	public String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minite = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		return year + "年" + month + "月" + day + "日" + "\t" + hours + ":" + minite + ":" + second + ":";
	}

	public int getDeviceHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager service = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		service.getDefaultDisplay().getMetrics(dm);
		int deviceHeight = dm.heightPixels;
		return deviceHeight;
	}

	public int getDeviceWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager service = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		service.getDefaultDisplay().getMetrics(dm);
		int deviceWidth = dm.widthPixels;
		return deviceWidth;
	}

	public void showToast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public void showToastLong(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public boolean stringFilter(String str) {
		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(str);
		boolean isMatches = matcher.matches();
		return isMatches;
	}

	public boolean isMobileNO(String mobiles) {
		// Pattern p = Pattern
		// .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("1[3|5|7|8|][0-9]{9}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public boolean isNetWorking(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		}
		return info.isAvailable();
	}

	public boolean isWifi(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (isNetWorking(context)) {
			return info.getType() == ConnectivityManager.TYPE_WIFI;
		}
		return false;
	}

	public ArrayList<String> formate(String from) {
		ArrayList<String> to = new ArrayList<String>();
		String[] split = from.split(",");
		for (int i = 0; i < split.length; i++) {
			to.add(split[i]);
		}
		return to;
	}

	public String formate(ArrayList<String> from) {
		StringBuffer to = new StringBuffer();
		if (from == null) {
			return "";
		}
		if (from.size() == 0) {
			return "";
		}
		for (int i = 0; i < from.size(); i++) {
			to.append(from.get(i) + ",");
		}
		String subTo = to.substring(0, to.length() - 1);
		return subTo;
	}

	public int getIndex(String[] array, String element) {
		if (array == null)
			return -1;
		int length = array.length;
		for (int i = 0; i < length; i++) {
			if (array[i].equals(element))
				return i;
		}
		return -1;
	}

	public int getIndex(ArrayList<String> strings, String string) {
		if (strings == null)
			return -1;
		for (int i = 0; i < strings.size(); i++) {
			if (strings.get(i).equals(string))
				return i;
		}
		return -1;
	}

	public void closeDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
			dialog.dismiss();
		} catch (Exception e) {
		}
	}

	public void closeDialog(Dialog dialog) {
		try {
			Field field = dialog.getClass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
			dialog.dismiss();
		} catch (Exception e) {
		}
	}

	public void donnotCloseDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
			dialog.dismiss();
		} catch (Exception e) {
		}
	}

	public void donnotCloseDialog(Dialog dialog) {
		try {
			Field field = dialog.getClass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
			dialog.dismiss();
		} catch (Exception e) {
		}
	}

	public String formateTime(long serverTime) {
		Date date = new Date(serverTime);
		Date nowData = new Date();
		long nowTime = nowData.getTime();
		long l = nowTime - serverTime;
		if (l >= 0 && l < 60 * 1000) {

			return "刚刚";

		} else if (l >= 60 * 1000 && l < 60 * 60 * 1000) {

			int minutes = (int) (l / (60 * 1000));

			return minutes + "分钟前";

		} else if (l >= 60 * 60 * 1000 && l < 24 * 60 * 60 * 1000) {

			int hour = (int) (l / (60 * 60 * 1000));

			return hour + "小时前";

		} else if (l >= 24 * 60 * 60 * 1000 && l < 2 * 24 * 60 * 60 * 1000) {

			return "昨天";

		} else {

			return DateFormat.format("yyyy-MM-dd", date).toString();
		}
	}

	public String formateHoldTime(long l) {

		if (l >= 0 && l < 60 * 1000) {

			return "一分钟";

		} else if (l >= 60 * 1000 && l < 60 * 60 * 1000) {

			int minutes = (int) (l / (60 * 1000));

			return "0时" + minutes + "分";

		} else if (l >= 60 * 60 * 1000 && l < 24 * 60 * 60 * 1000) {

			int hour = (int) (l / (60 * 60 * 1000));
			int minutes = (int) ((l / (60 * 1000)) % 60);

			return hour + "时" + minutes + "分";

		} else if (l >= 24 * 60 * 60 * 1000 && l < 2 * 24 * 60 * 60 * 1000) {

			return "一天";

		} else {

			return "0时0分";
		}
	}

	public boolean isRunning(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName)
					|| info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}

	@SuppressWarnings("static-access")
	public void hideKeyBoard(Activity context) {
		if (context == null)
			return;
		boolean imeShow = isImeShow(context);
		if (imeShow) {
			View view = context.getWindow().peekDecorView();
			if (view != null && view.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
			}
		}
	}

	@SuppressWarnings("static-access")
	public boolean isImeShow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	@SuppressWarnings("static-access")
	public void showKeyBoard(Activity context) {
		if (context == null)
			return;
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void setLimitDecimal(EditText mEditText, Editable s, int digit, int length) {
		int index = s.toString().indexOf(".");
		if (index < 0) {
			// editText.setFilters(new InputFilter[] { new
			// InputFilter.LengthFilter(length + 1) });
			if (s.length() > length) {
				s.delete(length, s.length());
			}
			return;
		}
		if (s.toString().length() - index - 1 > digit) {
			// s.delete(index + digit + 1, index + digit + 2);
			s.replace(mEditText.getSelectionStart() - 1, mEditText.getSelectionStart(), "");
		} else if (index == 0) {
			s.delete(index, index + 1);
		} else if (s.length() - digit - 1 > length) {
			s.replace(mEditText.getSelectionStart() - 1, mEditText.getSelectionStart(), "");
		}
	}

	public SpannableStringBuilder getHightLightString(String oldStr, String condition, int hightLightColor) {
		String old = oldStr.toLowerCase(Locale.getDefault());
		String con = condition.toLowerCase(Locale.getDefault());
		int start = old.indexOf(con);
		if (start == -1)
			return new SpannableStringBuilder(oldStr);
		int end = start + condition.length();
		SpannableStringBuilder style = new SpannableStringBuilder(oldStr);
		style.setSpan(new ForegroundColorSpan(hightLightColor), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public boolean Sixto16WordAndDig(String mPassword) {
		Pattern pattern = Pattern.compile("[\\da-zA-Z]{6,16}");
		Matcher isNum = pattern.matcher(mPassword);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取sd卡图片的旋转角度 目前Android SDK定义的Tag有: TAG_DATETIME 时间日期 TAG_FLASH 闪光灯
	 * TAG_GPS_LATITUDE 纬度 TAG_GPS_LATITUDE_REF 纬度参考 TAG_GPS_LONGITUDE 经度
	 * TAG_GPS_LONGITUDE_REF 经度参考 TAG_IMAGE_LENGTH 图片长 TAG_IMAGE_WIDTH 图片宽
	 * TAG_MAKE 设备制造商 TAG_MODEL 设备型号 TAG_ORIENTATION 方向 TAG_WHITE_BALANCE 白平衡
	 */
	public int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("ExifOrientation", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}

			}
		}
		return degree;
	}

	// 格式化服务器时间
	public String formateData(String time) {
		long serverTime = getServerTime(time);
		Date date = new Date(serverTime);
		return DateFormat.format("yyyy-MM-dd", date).toString();
	}

	public long getServerTime(String str) {
		String time = str.substring(6, str.length() - 2);
		return Long.valueOf(time);
	}

	public void callPhoneNum(Context context, String PhoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PhoneNum));
		context.startActivity(intent);
	}

	public String forNumber(double d,int offset){
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.setGroupingUsed(false);
		df.setMaximumIntegerDigits(30);
		df.setMaximumFractionDigits(offset);
		return df.format(d);
	}
	public String forNumber(String d,int offset){
		double dd = Double.valueOf(d);
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.setGroupingUsed(false);
		df.setMaximumIntegerDigits(30);
		df.setMaximumFractionDigits(offset);
		return df.format(dd);
	}
}
