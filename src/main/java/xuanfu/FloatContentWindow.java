package xuanfu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.capricorn.ArcLayout;
import com.capricorn.ArcMenu;

/**
 * 鎮诞绐楄杈撳叆闇�鏀惧埌鎮诞绐楅噷锛屽惁鍒欓渶瑕佸幓鎺塅lag閲岀殑FLAG_NOT_FOCAUSABLE锛�
 * 浣嗘槸杩欐牱鍋氫細灞忚斀鎺塰ome閿瓑
 * 
 * @author 鎬濊惤缇� 2014骞�鏈�9鏃�涓婂崍10:08:58
 * 
 */
public class FloatContentWindow extends PopupWindow {

	private Context mContext;
	/**
	 * 判断悬浮窗是否隐藏了
	 */
	private boolean isHide;
	/**
	 * 鎮诞绐楀唴瀹归〉
	 */
	protected ViewFlipper vfContent;
	private ArcMenu arcMenu2;

	public FloatContentWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	long oldDismissTime=0;
	long dismissTime=0;
	boolean isItemClick;
	public void setIsItemClick(boolean isItemClick){
		this.isItemClick=isItemClick;
	}
	public void setHide(boolean isHide){
		this.isHide = isHide;
	}
	@Override
	public void dismiss() {
		dismissTime =System.currentTimeMillis();
		//避免用户快速点击产生的bug
		if(dismissTime-showTime<700||dismissTime-oldDismissTime<700){
			return;
		}
		oldDismissTime=dismissTime;
		//规避悬浮窗按钮隐藏时的视觉差
		arcMenu2.postDelayed(new Runnable() {
			@Override
			public void run() {
				ShowFloatWindowService floatWindowService =(ShowFloatWindowService) mContext;
				floatWindowService.hideContent();
			}
		}, 480);
		arcMenu2.postDelayed(new Runnable() {
			@Override
			public void run() {
				FloatContentWindow.super.dismiss();
			}
		}, 500);
		//判断菜单项点击时不显示菜单显示隐藏的动画和悬浮窗隐藏时
		if(!isItemClick&&!isHide){
			arcMenu2.getArcLayout().switchState(true);
		}
		isHide=false;
		isItemClick=false;
	}

	public FloatContentWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public FloatContentWindow(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public FloatContentWindow(int width, int height) {
		super(width, height);
		init();
	}

	public FloatContentWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		mContext = contentView.getContext();
		init();
	}

	public FloatContentWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		mContext = contentView.getContext();
		init();
	}

	public FloatContentWindow(View contentView) {
		super(contentView);
		mContext = contentView.getContext();
		init();
	}

	/**
	 * 鍒濆鍖朠opupWindow
	 */
	private void init() {
//		int width = mContext.getResources().getDimensionPixelSize(
//				R.dimen.suspend_content_wight);
//		int height = mContext.getResources().getDimensionPixelSize(
//				R.dimen.suspend_content_height);
		int width = mContext.getResources().getDimensionPixelSize(
				ResouceUtil.getDimenId(mContext, "suspend_content_wight"));
		int height = mContext.getResources().getDimensionPixelSize(ResouceUtil.getDimenId(mContext, "suspend_content_height"));
		RelativeLayout content = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(ResouceUtil.getLayoutId(mContext, "float_popup_view"), null);
		content.layout(0, 0, width, height);
		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		setFocusable(true);
		// 璁剧疆杩欎釜鎵嶈兘鍦ㄥ闈㈢偣鍑绘秷澶盤opupWindow
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(content);
		arcMenu2 = (ArcMenu) content.findViewById(ResouceUtil.getId(mContext, "arc_menu_2"));
		arcMenu2.setWindow(this);
		int[] ITEM_DRAWABLES = {ResouceUtil.getDrawableId(mContext, "composer_camera"),
				ResouceUtil.getDrawableId(mContext, "composer_music"),ResouceUtil.getDrawableId(mContext, "composer_place"),
				ResouceUtil.getDrawableId(mContext, "composer_sleep"), ResouceUtil.getDrawableId(mContext, "composer_thought"),
				ResouceUtil.getDrawableId(mContext, "composer_with")};
		initArcMenu(arcMenu2, ITEM_DRAWABLES);
	}

//	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera,
//			R.drawable.composer_music, R.drawable.composer_place,
//			R.drawable.composer_sleep, R.drawable.composer_thought,
//			R.drawable.composer_with };
	
	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		
		String[] item_title = { "礼包","活动","开心","个人中心"};
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < 4; i++) {
			Button item = new Button(mContext);
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					35, 35);
			item.setBackgroundResource(ResouceUtil.getDrawableId(mContext, "m4399_ope_pop_logo_two_hide"));
			item.setText(item_title[i]);
			item.setLayoutParams(layoutParams);
			item.setTextSize(8);
			item.setTextColor(Color.GRAY);
			final int position = i;
			menu.addItem(item, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
		}
	}

	public void setDissMissListener(ArcLayout.DismissListener dissMissListener) {
		arcMenu2.getArcLayout().setDismissListener(dissMissListener);
	}

	public void setArc(int fromDegrees, int toDegrees) {
		arcMenu2.getArcLayout().setArc(fromDegrees, toDegrees);
	}

	public void setChildSize(int newChildSize) {
		arcMenu2.getArcLayout().setChildSize(newChildSize);
	}

	long showTime=0;
	public void showAtLocation(View parent, int gravity, int x, int y,int controllGrivaty) {
		super.showAtLocation(parent, gravity, x, y);
		//显示之前进行一些初始化，把菜单项的动画清空，扩展设为false
		arcMenu2.getArcLayout().clearAllAnimation();
			arcMenu2.getArcLayout().setExpanded(false);
			arcMenu2.setCtrollLayoutGrivaty(controllGrivaty);
			showTime = System.currentTimeMillis();
			arcMenu2.postDelayed(new Runnable() {
			@Override
			public void run() {
				arcMenu2.showItem(true);
			}
		}, 200);
	}

}
