package cc.upedu.online.view.factory;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.utils.CommonUtil;

/**
 * 生成-图片文本-样式view的工厂类,其中左侧图片可以隐藏
 * @author Administrator
 *
 */
public class MyHorizontalIconTextItem {
	//默认图片文字都显示
	public static int showStyle = 0;
	public static final int ICONTEXT = 0;
	public static final int TEXT = 1;
	private Context context;
	//根布局左右默认的padding值
	private static int paddingLeft_RingtDefault = 8;
	//图片默认的大小
	private static int imgSizeDefault = 20;
	//控件距上下两端两端默认的距离
	private static int widgetmarginDefault = 8;
	private static int iconRightDefault = 8;
	private static float textSizeDefault = 14;
	private static int colorDefault = 0xff323232;
	private View rootView;
	private ImageView icon_left;
	//左侧的文本
	private TextView text_reight;
	
	public MyHorizontalIconTextItem(int showStyle) {
		// TODO Auto-generated constructor stub
		this.showStyle = showStyle;
	}
	public View initView(Context context) {
		this.context = context;
		// TODO Auto-generated method stub
		rootView = View.inflate(context, R.layout.factory_horizontalicontext_2item, null);
		text_reight = (TextView) rootView.findViewById(R.id.text);
		icon_left = (ImageView) rootView.findViewById(R.id.icon_left);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		rootView.setLayoutParams(lp);
		rootView.setPadding(CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0, CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0);
		
		switch (showStyle) {
		case TEXT:
			setIconReightInVisibility();
			break;
		}
		return rootView;
	}
	/**
	 * 设置隐藏倬侧的图片
	 */
	private void setIconReightInVisibility() {
		icon_left.setVisibility(View.GONE);
	}
	public void setBackgroundResource(int resid){
		if (rootView != null) {
			rootView.setBackgroundResource(resid);
		}
	}
	public void setBackgroundColor(int color){
		if (rootView != null) {
			rootView.setBackgroundColor(color);
		}
	}
	/**
	 * 设置文本的样式
	 * @param textSize 文本字体大小,默认14
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param color 文本字体颜色
	 */
	public void setTextStyle(int textSize,int marginTop_Bottom,int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == text_reight.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) text_reight.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginTop_Bottom;
				layoutParams.setMargins(0,CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault));
				text_reight.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textSizeDefault = textSize;
				text_reight.setTextSize(textSizeDefault);
			}
			if (0 < color) {
				colorDefault = color;
				text_reight.setTextColor(colorDefault);
			}
		}
	}
	/**
	 * 设置右侧的图标的样式
	 * @param imgSize 图片的尺寸大小,默认20dp
	 * @param marginRight 右侧的边距,默认5dp
	 */
	public void setIconReightStyle(int imgSize,int marginRight) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_left.getVisibility()) {
			if (0 < imgSize) {
				imgSizeDefault = imgSize;
			}
			RelativeLayout.LayoutParams params = (LayoutParams) icon_left.getLayoutParams();
			params.width = CommonUtil.dip2px(context, imgSizeDefault);
			params.height = CommonUtil.dip2px(context, imgSizeDefault);
			if (0 <= widgetmarginDefault) {
				iconRightDefault = marginRight;
			}
			params.setMargins(0, 0, CommonUtil.dip2px(context, iconRightDefault), 0);
			icon_left.setLayoutParams(params);
		}
	}
	public String getText(CharSequence text) {
		// TODO Auto-generated method stub
		return text_reight.getText().toString().trim();
	}

	public void setText(CharSequence text) {
		// TODO Auto-generated method stub
		text_reight.setText(text);
	}
	public void setText(int id) {
		// TODO Auto-generated method stub
		text_reight.setText(id);
	}
	public void setIconReightRes(int resId) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_left.getVisibility()) {
			icon_left.setImageResource(resId);
		}
	}
}
