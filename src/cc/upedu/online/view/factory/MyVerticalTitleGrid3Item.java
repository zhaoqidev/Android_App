package cc.upedu.online.view.factory;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.view.GrapeGridview;

public class MyVerticalTitleGrid3Item {
	public static int showStyle = 0;
	public static final int TEXT_GRID = 1;
	public static final int TEXT_GRID_REMARKS = 2;
	//根布局左右默认的padding值
	private static int paddingLeft_RingtDefault = 8;
	//根布局距离上方控件或父控件顶部的距离
	private static int marginTopDefault = 16;
	//控件之间或控件距两端默认的距离
	private static int widgetmarginDefault = 8;
	private static float textSizeDefault = 16;
	private static int colorDefault = 0xff323232;
	private static int remarksDefault = 0xff5b5b5b;
	private static float remarksSizeDefault = 13;
	private Context context;
	private View rootView;
	//左侧图标右边的文本
	private TextView textView;
	//title下边的输入框
	private GrapeGridview gridview;
	//输入框字数角标
	private TextView remarks;
	/**
	 * 生成-title输入框图片-样式view的工厂类,
	 * 
	 */
	public MyVerticalTitleGrid3Item(int showStyle) {
		// TODO Auto-generated constructor stub
		this.showStyle = showStyle;
	}
	public void initView(Context context) {
		this.context = context;
		// TODO Auto-generated method stub
		rootView = View.inflate(context, R.layout.factory_vertical_titlegrid_3item, null);
		textView = (TextView) rootView.findViewById(R.id.text);
		gridview = (GrapeGridview) rootView.findViewById(R.id.gridview);
		remarks = (TextView) rootView.findViewById(R.id.remarks);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.setMargins(0, CommonUtil.dip2px(context, marginTopDefault), 0, 0);
		rootView.setLayoutParams(lp);
		rootView.setPadding(CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0, CommonUtil.dip2px(context, paddingLeft_RingtDefault), 0);
		if (TEXT_GRID == showStyle) {
			setIRemarksInVisibility();
		}
	}
	public static void setPaddingLeft_RingtDefault(int paddingLeft_RingtDefault) {
		MyVerticalTitleGrid3Item.paddingLeft_RingtDefault = paddingLeft_RingtDefault;
	}
	public static void setMarginTopDefault(int marginTopDefault) {
		MyVerticalTitleGrid3Item.marginTopDefault = marginTopDefault;
	}
	public View getRootView() {
		return rootView;
	}
	/**
	 * 设置隐藏右侧的图片
	 */
	public void setIRemarksInVisibility() {
		remarks.setVisibility(View.GONE);
	}
	/**
	 * 设置文本的样式index
	 * @param textSize 文本字体大小,默认16
	 * @param marginLeft 左右两侧的边距,默认8dp
	 * @param color 文本字体颜色
	 */
	public void setTextStyle(int textSize,int marginLeft_Reight,int color) {
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
			if (0 < color) {
				colorDefault = color;
				textView.setTextColor(colorDefault);
			}
		}
	}
	/**
	 * 设置备注文本的样式
	 * @param textSize 文本字体大小,默认13
	 * @param marginLeft 右侧的边距,默认8dp
	 * @param color 文本字体颜色
	 */
	public void setRemarksStyle(int textSize,int marginLeft,int color) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == remarks.getVisibility()) {
			RelativeLayout.LayoutParams layoutParams = (LayoutParams) remarks.getLayoutParams();
			if (0 <= widgetmarginDefault) {
				widgetmarginDefault = marginLeft;
				layoutParams.setMargins(CommonUtil.dip2px(context, widgetmarginDefault), 0, CommonUtil.dip2px(context, widgetmarginDefault), 0);
				remarks.setLayoutParams(layoutParams);
			}
			if (0 < textSize) {
				remarksSizeDefault = textSize;
				remarks.setTextSize(remarksSizeDefault);
			}
			if (0 < color) {
				remarksDefault = color;
				remarks.setTextColor(remarksDefault);
			}
		}
	}
	public void setText(CharSequence text) {
		// TODO Auto-generated method stub
		textView.setText(text);
	}
	private BaseAdapter gridAdapter;
	public void setAdapter(BaseAdapter adapter) {
		if (null != adapter) {
			gridAdapter = adapter;
			gridview.setAdapter(gridAdapter);
			CommonUtil.setGridViewHeightBasedOnChildren(context, gridview,5);
		}
	}
	public void setRemarks(CharSequence text) {
		// TODO Auto-generated method stub
		remarks.setText(text);
	}
	public void notifyDataGridView(){
		if (null != gridAdapter) {
			gridAdapter.notifyDataSetChanged();
			CommonUtil.setGridViewHeightBasedOnChildren(context, gridview,5);
		}
	}
}
