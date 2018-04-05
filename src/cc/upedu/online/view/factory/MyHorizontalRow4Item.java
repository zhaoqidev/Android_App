package cc.upedu.online.view.factory;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.StringUtil;

public class MyHorizontalRow4Item {
	public static int showStyle = 0;
	public static final int TEXT_EDIT = 1;
	public static final int ICON_EDIT = 2;
	public static final int ICON_EDIT_ICON = 3;
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
	private static float textSizeDefault = 16;
	private static int colorDefault = 0xff323232;
	private static int colorEditDefault = 0xff444444;
	private static int colorHintDefault = 0xff5b5b5b;
	private Context context;
	private View rootView;
	//左侧的图标
	private ImageView icon_lift;
	//左侧图标右边的文本
	private TextView textView;
	//文本右边的输入框
	private EditText editText;
	//最右边的图标
	private ImageView icon_reight;
	/**
	 * 生成-图片文本输入框图片-样式view的工厂类,其中每个子控件都可以隐藏
	 * @param showStyle 固定值,表示四个样板,TEXT_EDIT-文本输入框-,ICON_EDIT-图标输入框-,ICON_EDIT_ICON-图标输入框图标-,其他-全选-
	 */
	public MyHorizontalRow4Item(int showStyle) {
		// TODO Auto-generated constructor stub
		this.showStyle = showStyle;
	}
	public void initView(Context context) {
		this.context = context;
		// TODO Auto-generated method stub
		rootView = View.inflate(context, R.layout.factory_horizontalrow_4item, null);
		icon_lift = (ImageView) rootView.findViewById(R.id.icon_lift);
		textView = (TextView) rootView.findViewById(R.id.text);
		editText = (EditText) rootView.findViewById(R.id.edit);
		icon_reight = (ImageView) rootView.findViewById(R.id.icon_reight);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, rootViewHeightDefault));  
		lp.setMargins(0, CommonUtil.dip2px(context, marginTopDefault), 0, 0);
		rootView.setLayoutParams(lp);
		rootView.setPadding(CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0, CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0);
		
		switch (showStyle) {
		case TEXT_EDIT:
			setIconLiftInVisibility();
			setIconReightInVisibility();
			break;
		case ICON_EDIT:
			setTextInVisibility();
			setIconReightInVisibility();
			break;
		case ICON_EDIT_ICON:
			setTextInVisibility();
			break;
		default:
			break;
		}
	}
	/**
	 * 设置根布局的高度,默认高度为44dp
	 * @param rootViewHeightDefault
	 */
	public static void setRootViewHeightDefault(int rootViewHeightDefault) {
		MyHorizontalRow4Item.rootViewHeightDefault = rootViewHeightDefault;
	}
	/**
	 * 设置根布局左右两端的padding值
	 * @param paddingLeft_RingtDefault
	 */
	public static void setPaddingLeft_RingtDefault(int paddingLeft_RingtDefault) {
		MyHorizontalRow4Item.paddingLeft_RingtDefault = paddingLeft_RingtDefault;
	}
	/**
	 * 设置根布局距离上方控件或父控件顶部的距离,默认为16dp
	 * @param marginTopDefault
	 */
	public static void setMarginTopDefault(float marginTopDefault) {
		MyHorizontalRow4Item.marginTopDefault = marginTopDefault;
	}
	
	public View getRootView() {
		return rootView;
	}
	/**
	 * 设置隐藏左侧的图片
	 */
	public void setIconLiftInVisibility() {
		icon_lift.setVisibility(View.GONE);
	}
	/**
	 * 设置隐藏文本
	 */
	public void setTextInVisibility() {
		textView.setVisibility(View.GONE);
	}
	/**
	 * 设置隐藏输入框
	 */
	public void setEditInVisibility() {
		editText.setVisibility(View.GONE);
	}
	/**
	 * 设置隐藏右侧的图片
	 */
	public void setIconReightInVisibility() {
		icon_reight.setVisibility(View.GONE);
	}
	/**
	 * 设置左侧的图标的样式
	 * @param imgSize 图片的尺寸大小,默认25dp
	 * @param marginLeft 左侧的边距,默认8dp
	 */
	public void setIconLiftStyle(int imgSize,int marginLeft) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_lift.getVisibility()) {
			if (0 < imgSize) {
				imgSizeDefault = imgSize;
			}
			RelativeLayout.LayoutParams params = (LayoutParams) icon_lift.getLayoutParams();
			params.width = CommonUtil.dip2px(context, imgSizeDefault);
			params.height = CommonUtil.dip2px(context, imgSizeDefault);
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft;
			}
			params.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, 0, 0);
			icon_lift.setLayoutParams(params);
		}
	}
	/**
	 * 设置文本的样式
	 * @param textSize 文本字体大小,默认16
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param maxWith 最大宽度,默认90dp
	 * @param color 文本字体颜色
	 */
	public void setTextStyle(int textSize,int marginLeft_Reight,int maxWith, int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == textView.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft_Reight;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				textView.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				textSizeDefault = textSize;
				textView.setTextSize(textSizeDefault);
			}
			if (0 < maxWith) {
				textView.setMaxWidth(CommonUtil.dip2px(context, maxWith));
			}
			if (0 < color) {
				colorDefault = color;
				textView.setTextColor(colorDefault);
			}
		}
	}
	/**
	 * 设置输入框的样式
	 * @param textSize 文字大小,默认16
	 * @param marginLeft_Reight 左右两侧的边距,默认8dp
	 * @param color 输入文字字体颜色
	 * @param colorHint 提示文字字体颜色
	 */
	public void setEditStyle(int textSize,int marginLeft_Reight,int color,int colorHint) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft_Reight;
			}
			layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, 0, 0);
			editText.setLayoutParams(layoutParams);
			if (0 < textSize) {
				textSizeDefault = textSize;
				editText.setTextSize(textSizeDefault);
			}
			if (0 < color) {
				colorEditDefault = color;
				editText.setTextColor(colorEditDefault);
			}
			if (0 < colorHint) {
				colorHintDefault = colorHint;
				editText.setHintTextColor(colorHintDefault);
			}
		}
	}
	/**
	 * 设置右侧的图标的样式
	 * @param imgSize 图片的尺寸大小,默认25dp
	 * @param marginLeft padding值,默认8dp
	 */
	public void setIconReightStyle(int imgSize,int marginLeft) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_reight.getVisibility()) {
			if (0 < imgSize) {
				imgSizeDefault = imgSize;
			}
			RelativeLayout.LayoutParams params = (LayoutParams) icon_reight.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft;
			}
			params.width = CommonUtil.dip2px(context, imgSizeDefault+widgetmarginDefault);
			params.height = CommonUtil.dip2px(context, imgSizeDefault+widgetmarginDefault);
//			params.setMargins(0, 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
			icon_reight.setLayoutParams(params);
			icon_reight.setPadding(CommonUtil.dip2px(context, widgetmarginDefault), CommonUtil.dip2px(context, widgetmarginDefault), CommonUtil.dip2px(context, widgetmarginDefault), CommonUtil.dip2px(context, widgetmarginDefault));
		}
	}
	public void setIconLiftRes(int resId) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_lift.getVisibility()) {
			icon_lift.setImageResource(resId);
		}
	}
	public void setText(CharSequence text) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == textView.getVisibility()) {
			textView.setText(text);
		}
	}
	public void setEditHintText(CharSequence text) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			editText.setHint(text);
		}
	}
	public void setEditText(CharSequence text) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			if (StringUtil.isEmpty(text)) {
				editText.setText("");
			}else {
				editText.setText(text);
			}
		}
	}
	public String getEditText() {
		// TODO Auto-generated method stub
		if (View.VISIBLE == editText.getVisibility()) {
			return editText.getText().toString().trim();
		}else {
			return "";
		}
	}
	public void setIconReightRes(int resId) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == icon_reight.getVisibility()) {
			icon_reight.setImageResource(resId);
		}
	}
	public void setIconReightOnClickListener(final IconReightOnClickListener mIconReightOnClickListener) {
		// TODO Auto-generated method stub
		icon_reight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mIconReightOnClickListener.onClick(v);
			}
		});
	}
	public interface IconReightOnClickListener{
		public void onClick(View v);
	}
}
