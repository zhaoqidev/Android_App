package cc.upedu.online.base;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.StringUtil;
/**
 * titlebar确定,页面主题内容不确定的activity
 * titlebar分为左中右三部分,左边为图片和文本的组合(不能做两个独立按钮使用,可以自定义显示单个或多个)
 * 		中间为title文本,右侧为ImageButton-ImageButton-TextView样式,可自定义显示单个或多个,每个都是独立的
 * @author Administrator
 *
 */
public abstract class TitleBaseActivity extends BaseActivity{
	private LinearLayout ll_left,ll_root,basetitlebar;
	private ImageView ib_left;
	private TextView tv_left,tv_title,tv_right;
	private ImageButton ibtn_right,ibtn_right2;
	private OnClickMyListener myLeftButtomListener;
	private OnClickMyListener myRightButtomListener;
	private OnClickMyListener myRightButtom2Listener;
	private OnClickMyListener myRightTextListener;
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_title_base);
		getTitleView();
		ll_root = (LinearLayout) findViewById(R.id.ll_root);
		View contentView = initContentView();
		if (contentView != null) {
			ll_root.addView(contentView);
		}
	}
	/**
	 * 
	 */
	protected void getTitleView() {
		basetitlebar = (LinearLayout) findViewById(R.id.basetitlebar);
		ll_left = (LinearLayout) findViewById(R.id.ll_back);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		tv_left = (TextView) findViewById(R.id.tv_back_2);
		tv_title = (TextView) findViewById(R.id.tv_title_2);
		ibtn_right = (ImageButton) findViewById(R.id.ibtn_right);
		ibtn_right2 = (ImageButton) findViewById(R.id.ibtn_right2);
		tv_right = (TextView) findViewById(R.id.tv_next);
		
		initTitle();
	}
	/**
	 * 设置title布局的显示和隐藏
	 * @param visibility
	 */
	public void setTitleViewVisibility(int visibility){
		if (basetitlebar != null) {
			basetitlebar.setVisibility(visibility);
		}
	}
	/**
	 * 设置下方主体内容布局的显示和隐藏
	 * @param visibility
	 */
	protected void setContentViewVisibility(int visibility){
		if (ll_root != null) {
			ll_root.setVisibility(visibility);
		}
	}
	/**
	 * 设置左侧只显示图片,并设置点击事件
	 * @param imgResId 图片的资源id
	 * @param listener 默认为关闭此activity
	 */
	public void setLeftButton(int imgResId ,OnClickMyListener listener){
		ib_left.setImageResource(imgResId);
		tv_left.setVisibility(View.GONE);
		myLeftButtomListener = listener;
	}
	/**
	 * 设置左侧只显示文本,并设置点击事件
	 * @param lefttext 文本
	 * @param listener 默认为关闭此activity
	 */
	public void setLeftButton(String lefttext ,OnClickMyListener listener){
		if (!StringUtil.isEmpty(lefttext)) {
			tv_left.setText(lefttext);
		}
		ib_left.setVisibility(View.GONE);
		myLeftButtomListener = listener;
	}
	/**
	 * 设置左侧为图片-文本,并设置点击事件
	 * @param listener
	 */
	public void setLeftButton(OnClickMyListener listener){
		myLeftButtomListener = listener;
	}
	/**
	 * 设置左侧显示图片和文本,并设置点击事件
	 * @param imgResId 图片的资源id
	 * @param lefttext 文本
	 * @param listener 默认为关闭此activity
	 */
	public void setLeftButton(int imgResId,String lefttext ,OnClickMyListener listener){
		ib_left.setImageResource(imgResId);
		tv_left.setText(lefttext);
		myLeftButtomListener = listener;
	}
	public void setTitleText(String titletext){
		tv_title.setText(titletext);
	}
	/**
	 * 设置右侧第一个图片button,并设置点击事件
	 * @param imgResId 图片的资源id
	 * @param listener 默认为关闭此activity
	 */
	public void setRightButton(int imgResId ,OnClickMyListener listener){
		ibtn_right.setVisibility(View.VISIBLE);
		ibtn_right.setImageResource(imgResId);
		if (listener != null) {
			myRightButtomListener = listener;
		}
	}
	/**
	 * 设置右侧第一个图片button,并设置点击事件
	 * @param imgResId 图片的资源id
	 * @param listener 默认为关闭此activity
	 */
	public void setRightButton2(int imgResId ,OnClickMyListener listener){
		ibtn_right2.setVisibility(View.VISIBLE);
		ibtn_right2.setImageResource(imgResId);
		if (listener != null) {
			myRightButtom2Listener = listener;
		}
	}
	/**
	 * 设置右侧第一个图片button,并设置点击事件
	 * @param imgResId 图片的资源id
	 * @param listener 默认为关闭此activity
	 */
	public void setRightText(String righttext ,OnClickMyListener listener){
		tv_right.setVisibility(View.VISIBLE);
		tv_right.setText(righttext);
		if (listener != null) {
			myRightTextListener = listener;
		}
	}
	public void setContentBackgroundResource(int resid) {
		// TODO Auto-generated method stub
		ll_root.setBackgroundResource(resid);
	}
	public void setContentBackgroundColor(int color) {
		// TODO Auto-generated method stub
		ll_root.setBackgroundColor(color);
	}
	@SuppressLint("NewApi")
	public void setContentBackground(Drawable background) {
		// TODO Auto-generated method stub
		ll_root.setBackground(background);
	}
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		ll_left.setOnClickListener(this);
		if (View.VISIBLE == ibtn_right.getVisibility()) {
			ibtn_right.setOnClickListener(this);
		}
		if (View.VISIBLE == ibtn_right2.getVisibility()) {
			ibtn_right2.setOnClickListener(this);
		}
		if (View.VISIBLE == tv_right.getVisibility()) {
			tv_right.setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			if (myLeftButtomListener == null) {
				this.finish();
			}else {
				myLeftButtomListener.onClick(ll_left);
			}
			break;
		case R.id.ibtn_right:
			if (myRightButtomListener != null) {
				myRightButtomListener.onClick(ibtn_right);
			}
			break;
		case R.id.ibtn_right2:
			if (myRightButtom2Listener != null) {
				myRightButtom2Listener.onClick(ibtn_right2);
			}
			break;
		case R.id.tv_next:
			if (myRightTextListener != null) {
				myRightTextListener.onClick(tv_right);
			}
			break;
		}
	}
	/**
	 * 初始title
	 * 默认左侧是返回(包括图片/文本返回/点击事件关闭activity),中间是文本,右侧不显示
	 * 设置方法
	 * 		setLeftButton
	 * 		setTitleText
	 * 		setRightButton
	 * 		setRightButton2
	 * 		setRightText
	 */
	protected abstract void initTitle();
	/**
	 * 初始title下方的内容view
	 */
	protected abstract View initContentView();
}
