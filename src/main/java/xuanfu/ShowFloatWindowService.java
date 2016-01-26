package xuanfu;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.capricorn.ArcLayout;
import com.capricorn.ArcMenu;

/**
 * 通过Service控制悬浮窗的显示，
 * 用Activity，会因为生命周期而不能在其他界面显示
 * @author 思落羽
 * 2014年8月27日 上午11:39:08
 *
 */
public class ShowFloatWindowService extends Service {
	
	public static final String ACTION = "action";
	public static final int ACTION_SHOW = 0;
	public static final int ACTION_HIDE = 1;
	
	/**
	 * 检查浮窗是否需要显示的周期
	 */
	private static final int PERIOD = 500;
	
	
	private WindowManager.LayoutParams params;
	private LinearLayout suspendLayout;
	
	/**点击以后才把主要内容显示出来
	 * 悬浮的小按钮，
	 */
	private ImageButton suspendBtn;
	
	/**
	 * 做为内容PopupWindow的支撑，不然PopupWindow不能显示出来
	 */
	private View suspendContent;
	
	/**
	 * 当前的悬浮窗的显示状态，true为显示，false为未显示
	 */
	private boolean isShow;
	
	/**
	 * true表示为应该显示浮窗
	 */
	private boolean isShouldShow;
	
	/**
	 * 悬浮的PopupWindow，PopupWindow的显示需要一个同样大小的View悬浮窗在背后
	 */
	private FloatContentWindow popupWindow;

	/**
	 * 计时器任务调用显示
	 */
	private Runnable showFloatWindowRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (checkShow()) {
				show();
			} else {
				hide();
			}
			if (isShouldShow)
				handler.postDelayed(showFloatWindowRunnable, PERIOD);
			else
				handler = null;
		}
	};
	
	private Runnable halfRunnable = new Runnable() {
		@Override
		public void run() {
			if(params.y ==  -PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).y/2){
				suspendBtn.setScaleType(ImageView.ScaleType.FIT_START);
				suspendBtn.setImageResource(ResouceUtil.getDrawableId(ShowFloatWindowService.this, "rktop"));
			}else if(params.y ==  PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).y/2){
				suspendBtn.setScaleType(ImageView.ScaleType.FIT_END);
//				suspendBtn.setImageResource(R.drawable.rkbottom);
				suspendBtn.setImageResource(ResouceUtil.getDrawableId(ShowFloatWindowService.this, "rkbottom"));
			}else if (params.x == 0) {
				suspendBtn.setScaleType(ImageView.ScaleType.FIT_START);
//				suspendBtn.setImageResource(R.drawable.rk_ban);
				suspendBtn.setImageResource(ResouceUtil.getDrawableId(ShowFloatWindowService.this, "rk_ban"));
			} else if(params.x == PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).x){
				suspendBtn.setScaleType(ImageView.ScaleType.FIT_END);
//				suspendBtn.setImageResource(R.drawable.rk_ban_right);
				suspendBtn.setImageResource(ResouceUtil.getDrawableId(ShowFloatWindowService.this, "rk_ban_right"));
			}
		}
		
	};
	
	private Handler handler;

	/**
	 * 显示悬浮窗
	 * @param context
	 */
	public static void showSuspend(Context context) {	Intent showIntent = new Intent(context, ShowFloatWindowService.class);
		showIntent.putExtra(ACTION, ACTION_SHOW);
		context.startService(showIntent);
	}
	/**
	 * 隐藏悬浮窗
	 * @param context
	 */
	public static void hideSuspend(Context context) {
		Intent hideIntent = new Intent(context, ShowFloatWindowService.class);
		hideIntent.putExtra(ACTION, ACTION_HIDE);
		context.startService(hideIntent);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// 系统重启的时候intent为null，抛弃
		if (intent == null) {
			return;
		}
		switch (intent.getIntExtra(ACTION, 0)) {
		case ACTION_SHOW:
			isShouldShow = true;
			startTimer();
			show();
			break;
		case ACTION_HIDE:
			isShouldShow = false;
			stopTimer();
			hide();
			break;
		}
	}
	
	private void startTimer() {
		if (handler == null) {
			handler = new Handler(getMainLooper());
		}
		handler.postDelayed(showFloatWindowRunnable, 0);
	}
	
	private void stopTimer() {
		if (handler != null) {
			handler.removeCallbacks(showFloatWindowRunnable);
		}
	}
	
	/**
	 * 悬浮窗当前状态为显示
	 * @return
	 */
	public boolean isFloatWindowShow() {
		return isShow;
	}
	
	/**
	 * 显示悬浮窗
	 */
	protected void show() {
//		log.info("调用show()方法");
		if (isShow) {
			return;
		} else {
			isShow = true;
			if (suspendLayout == null) {
				initSuspendLayout();
			}
			getWindowManager().addView(suspendLayout, params);
			handler.removeCallbacks(halfRunnable);
			handler.postDelayed(halfRunnable, 2000);
		}
	}
	
	/**
	 * 隐藏悬浮窗
	 */
	protected void hide() {
//		log.info("调用hide()方法");
		if (!isShow) {
			return;
		} else {
			isShow = false;
			handler.removeCallbacks(halfRunnable);
			if (popupWindow != null){
				popupWindow.setHide(true);
				popupWindow.dismiss();
			}
			if (suspendLayout != null) {
				getWindowManager().removeView(suspendLayout);
				if (!isShouldShow) {
					// 隐藏后这个Service也没用了，停止自身
					stopSelf();
				}
			}
		}
	}


	
	/**
	 * 初始化悬浮窗
	 */
	private void initSuspendLayout() {
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, 
				PixelFormat.TRANSLUCENT);
		// 设置布局为向左
		params.gravity = Gravity.LEFT;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			params.type =WindowManager.LayoutParams.TYPE_TOAST;
		}else{
			if(!DUtil.isGetPermission(this)){
//				CommonUtil.showToast("请到权限管理中开启悬浮框权限，否则不能正常开启悬浮框");
			}
		}
//		suspendLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.floatwindow, null);
		suspendLayout = (LinearLayout) LayoutInflater.from(this).inflate(ResouceUtil.getLayoutId(this, "floatwindow"), null);
		// 简单的设置个动画
		suspendLayout.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, android.R.anim.fade_in)));
		// 初始化小按钮
//		suspendBtn = (ImageButton) suspendLayout.findViewById(R.id.btn_open_content);
		suspendBtn = (ImageButton) suspendLayout.findViewById(ResouceUtil.getId(this, "btn_open_content"));
		// 初始化PopupWindow背景台
//		suspendContent = suspendLayout.findViewById(R.id.content);
		suspendContent = suspendLayout.findViewById(ResouceUtil.getId(this, "content"));
		suspendLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!inSuspend(event.getX(), event.getY())) {
					hideContent();
				}
				return false;
			}
		});
//		suspendContent.measure(View.MeasureSpec.makeMeasureSpec(0,
//				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
//				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		suspendBtn.setOnClickListener(new ButtonClick());
		suspendBtn.setOnTouchListener(new ButtonOnTouch());
	}

	/**
	 * 触摸点是否在控件内，x 和 y是相对于控件左上角的位置
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean inSuspend(float x, float y) {
		return x >= suspendLayout.getLeft() && x <= suspendLayout.getRight() 
				&& y >= suspendLayout.getTop()&& y <= suspendLayout.getBottom();
	}
	
	protected WindowManager getWindowManager() {
		return (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	}
	
	protected void initPopupWindow() {

		popupWindow = new FloatContentWindow(this);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				hideContent();
			}
		});
		popupWindow.setDissMissListener(new ArcLayout.DismissListener() {
			@Override
			public void onDisimiss() {
				popupWindow.dismiss();
			}
		});
	}
	
	private void toggleContent(int from ,int to) {
		if (popupWindow != null && popupWindow.isShowing()) {
			hideContent();
		} else {
			showContent(from,to);
		}
	}
	
	private void showContent(int from,int to) {
		//规避按钮显示的视觉差
		suspendBtn.postDelayed(new Runnable() {
			@Override
			public void run() {
				suspendBtn.setVisibility(View.INVISIBLE);
			}
		},50);
		if (popupWindow == null) {
			initPopupWindow();
		}
		popupWindow.setArc(from,to);
		if(from==270){
			popupWindow.showAtLocation(suspendBtn, Gravity.CENTER,0 , 0,ArcMenu.GRIVATY_LEFT);
		}else if(from==90){
			popupWindow.showAtLocation(suspendBtn, Gravity.CENTER,0 , 0,ArcMenu.GRIVATY_RIGHT);
		}else if(from == 0){
			popupWindow.showAtLocation(suspendBtn, Gravity.CENTER,0 , 0,ArcMenu.GRIVATY_TOP);
		}else if(from == 180){
			popupWindow.showAtLocation(suspendBtn, Gravity.CENTER,0 , 0,ArcMenu.GRIVATY_BOTTOM);
		}
		suspendContent.setVisibility(View.VISIBLE);
	}
	
	public void hideContent() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		suspendBtn.setVisibility(View.VISIBLE);
		suspendContent.setVisibility(View.GONE);
		handler.postDelayed(halfRunnable,2000);
	}
	
	/**
	 * 检查当前应该显示还是隐藏浮窗
	 */
	private boolean checkShow() {
		PackageUtil pu = new PackageUtil(this);
		// 为RK语音应用、桌面或不是游戏的应用时不显示
		if (pu.currentIsRKHelper()) {
			return true;
		}
		return false;
	}
	
	private class ButtonClick implements OnClickListener {
		
		@Override
		public void onClick(View v) {
				if(params.y<-PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).y/2+100){
					toggleContent(0,180);
				}else if(params.y>PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).y/2-100){
					toggleContent(180,360);
				}else if(params.x<100){
					toggleContent(270,450);
				}else if(params.x >= PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).x-100){
					toggleContent(90,270);
				}
		}
		
	}
	
	protected class ButtonOnTouch implements OnTouchListener {
		
		float x;
		float y;
		// 按下时，窗口的x
		int downX;
		// 按下时，窗口的y
		int downY;
		
		int checkX;
		int checkY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				down(v, event);
				break;
			case MotionEvent.ACTION_UP:
				up(v, event);
				
				break;
			case MotionEvent.ACTION_MOVE:
				move(v, event);
				break;
			}
			return true;
		}
		
		void down(View v, MotionEvent event) {
			/* 按下时记录按下的坐标 */
			x = event.getX();
			y = event.getY();
			downX = params.x;
			downY = params.y;
			suspendBtn.setImageResource(ResouceUtil.getDrawableId(ShowFloatWindowService.this, "rk"));
		}
		
		void up(View v, MotionEvent event) {
			// 浮窗位置未改变视为点击，点击事件允许一些偏差
			if (Math.abs(downX - params.x) < 100 && Math.abs(downY - params.y) < 100) {
//			if(Math.abs(x - event.getX()) < 100 && Math.abs(y - event.getY())<50){
				/* 松开与点下的x y不变，视为点击 */
				v.performClick();
				handler.removeCallbacks(halfRunnable);

			} else {
				handler.removeCallbacks(halfRunnable);
				handler.postDelayed(halfRunnable, 2000);
			}
			WindowManager wm = getWindowManager();
			Display display =wm.getDefaultDisplay();
			if(params.y<-display.getHeight()/2+300||params.y>display.getHeight()/2-300){
				if(params.y<-display.getHeight()/2+300){
					params.y =-display.getHeight()/2; 
				}else if(params.y>display.getHeight()/2-300){
					params.y = display.getHeight()/2;
				}
			}else{
				int right = display.getWidth() / 2 > params.x ? 0 : display.getWidth();
				/* 设置新的x y的值 */
				params.x = right;
			}
				/* 更新整个悬浮窗的位置 */
			wm.updateViewLayout(suspendLayout, params);
		}

		void move(View v, MotionEvent event) {
			/* 获取 WindowManager */
			WindowManager wm = getWindowManager();
			/* 设置新的x y的值 */
			params.x += event.getX() - x;
			params.y += event.getY() - y;
//			//getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
			params.x = (int) event.getRawX()-suspendLayout.getMeasuredWidth()/2;
			//减25为状态栏的高度
			params.y = (int) event.getRawY()-PhoneUtil.getScreenDisplay(ShowFloatWindowService.this).y/2;
			/* 更新整个悬浮窗的位置 */

			wm.updateViewLayout(suspendLayout, params);
		}
		
	}
	
	
}
