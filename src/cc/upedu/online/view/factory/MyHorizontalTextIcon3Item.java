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

public class MyHorizontalTextIcon3Item {
	public static int showStyle = 0;
	public static final int TEXT = 1;
	public static final int TEXT_TEXT = 2;
	public static final int TEXT_ICON = 3;
	public static final int TEXT_TEXT_ICON = 4;
	//根布局的默认高度
	private static int rootViewHeightDefault = 44;
	//根布局左右默认的padding值
	private static int paddingLeft_RingtDefault = 8;
	//根布局距离上方控件或父控件顶部的距离
	private static float marginTopDefault = 16;
	//图片默认的大小
	private static int imgSizeDefault = 25;
	//控件之间或控件距两端默认的距离
	private static int widgetmarginDefault = 8;
	private static float textLeftSizeDefault = 16;
	private static float textReightSizeDefault = 14;
	private static int colorLeftDefault = 0xff323232;
	private static int colorReightDefault = 0xff444444;
	private Context context;
	private View rootView;
	//左侧的文本
	private TextView text_left;
	//右侧的文本
	private TextView text_reight;
	//最右边的图标
	private ImageView icon_reight;
	/**
	 * 生成-文本文本图片-样式view的工厂类,其中右侧文本和图片可以隐藏
	 * @param showStyle 固定值,表示四个样板,TEXT_EDIT-文本输入框-,ICON_EDIT-图标输入框-,ICON_EDIT_ICON-图标输入框图标-,其他-全选-
	 */
	public MyHorizontalTextIcon3Item(int showStyle) {
		// TODO Auto-generated constructor stub
		this.showStyle = showStyle;
	}
	public View initView(Context context) {
		this.context = context;
		// TODO Auto-generated method stub
		rootView = View.inflate(context, R.layout.factory_horizontal2texticon_3item, null);
		text_left = (TextView) rootView.findViewById(R.id.text_left);
		text_reight = (TextView) rootView.findViewById(R.id.text_reight);
		icon_reight = (ImageView) rootView.findViewById(R.id.icon_reight);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, rootViewHeightDefault));  
		lp.setMargins(0, CommonUtil.dip2px(context, marginTopDefault), 0, 0);
		rootView.setLayoutParams(lp);
		rootView.setPadding(CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0, CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0);
		
		switch (showStyle) {
		case TEXT:
			setTextReightInVisibility();
			setIconReightInVisibility();
			break;
		case TEXT_TEXT:
			setIconReightInVisibility();
			break;
		case TEXT_ICON:
			setTextReightInVisibility();
			break;
		case TEXT_TEXT_ICON:
			break;
		default:
			break;
		}
		return rootView;
	}
	/**
	 * 设置根布局的高度,默认高度为44dp
	 * @param rootViewHeightDefault
	 */
	public static void setRootViewHeightDefault(int rootViewHeightDefault) {
		MyHorizontalTextIcon3Item.rootViewHeightDefault = rootViewHeightDefault;
	}
	/**
	 * 设置根布局左右两端的padding值
	 * @param paddingLeft_RingtDefault
	 */
	public static void setPaddingLeft_RingtDefault(int paddingLeft_RingtDefault) {
		MyHorizontalTextIcon3Item.paddingLeft_RingtDefault = paddingLeft_RingtDefault;
	}
	/**
	 * 设置根布局距离上方控件或父控件顶部的距离,默认为16dp
	 * @param marginTopDefault
	 */
	public static void setMarginTopDefault(float marginTopDefault) {
		MyHorizontalTextIcon3Item.marginTopDefault = marginTopDefault;
	}
	
	public View getRootView() {
		return rootView;
	}
	public void setRootView(int id) {
		rootView.setId(id);
	}
	/**
	 * 设置隐藏左侧文本
	 */
	public void setTextLeftInVisibility() {
		text_left.setVisibility(View.GONE);
	}
	/**
	 * 设置隐藏右侧文本
	 */
	public void setTextReightInVisibility() {
		text_reight.setVisibility(View.GONE);
	}
	/**
	 * 设置隐藏右侧的图片
	 */
	public void setIconReightInVisibility() {
		icon_reight.setVisibility(View.GONE);
	}
	/**
	 * 设置文本的样式
	 * @param textSize 文本字体大小,默认16
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param maxWith 最大宽度,默认90dp
	 * @param color 文本字体颜色
	 */
	public void setTextLeftStyle(int textSize,int marginLeft_Reight,int maxWith, int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == text_left.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) text_left.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft_Reight;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				text_left.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textLeftSizeDefault = textSize;
				text_left.setTextSize(textLeftSizeDefault);
			}
			if (0 < maxWith) {
				text_left.setMaxWidth(CommonUtil.dip2px(context, maxWith));
			}
			if (0 < color) {
				colorLeftDefault = color;
				text_left.setTextColor(colorLeftDefault);
			}
		}
	}
	/**
	 * 设置文本的样式
	 * @param textSize 文本字体大小,默认14
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param color 文本字体颜色
	 */
	public void setTextStyle(int textSize,int marginLeft_Reight,int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == text_reight.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) text_reight.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft_Reight;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				text_reight.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textReightSizeDefault = textSize;
				text_reight.setTextSize(textReightSizeDefault);
			}
			if (0 < color) {
				colorReightDefault = color;
				text_reight.setTextColor(colorReightDefault);
			}
		}
	}
	/**
	 * 设置右侧的图标的样式
	 * @param imgSize 图片的尺寸大小,默认25dp
	 * @param marginLeft 右侧的边距,默认8dp
	 */
	public void setIconReightStyle(int imgSize,int marginLeft) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_reight.getVisibility()) {
			if (0 < imgSize) {
				imgSizeDefault = imgSize;
			}
			RelativeLayout.LayoutParams params = (LayoutParams) icon_reight.getLayoutParams();
			params.width = CommonUtil.dip2px(context, imgSizeDefault);
			params.height = CommonUtil.dip2px(context, imgSizeDefault);
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft;
			}
			params.setMargins(0, 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
			icon_reight.setLayoutParams(params);
		}
	}
	public void setTextLeft(CharSequence text) {
		// TODO Auto-generated method stub
		text_left.setText(text);
	}
	public void setTextReight(CharSequence text) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == text_reight.getVisibility()) {
			text_reight.setText(text);
		}
	}
	public String getTextReight(CharSequence text) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == text_reight.getVisibility()) {
			return text_reight.getText().toString().trim();
		}
		return "";
	}
	public void setIconReightRes(int resId) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_reight.getVisibility()) {
			icon_reight.setImageResource(resId);
		}
	}

	
}
