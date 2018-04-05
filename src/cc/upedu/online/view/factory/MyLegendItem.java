package cc.upedu.online.view.factory;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cc.upedu.online.R;

/**
 * 图例条目
 * @author Administrator
 *
 */
public class MyLegendItem {
	
	private View rootView;
	//左侧色块
	private ImageView icon_lift;
	//金额种类
	private TextView kinds;
	//金额
	private TextView money;
	
	
	public View getRootView() {
		return rootView;
	}
	/**
	 * 初始化图例条目
	 * @param context 上下文对象
	 * @param color 图例颜色
	 * @param kindsText 充值或智慧分享或代言。。。
	 * @param Money 金额
	 */
	public void initView(Context context,int color,String kindsText,String moneyString) {
		rootView = View.inflate(context, R.layout.factory_legend_item, null);
		icon_lift = (ImageView) rootView.findViewById(R.id.icon_lift);
		kinds = (TextView) rootView.findViewById(R.id.kinds);
		money = (TextView) rootView.findViewById(R.id.money);
		
		icon_lift.setBackgroundColor(color);
		kinds.setText(kindsText);
		money.setText(moneyString);
	}
	
	/**
	 * 设置左侧小图标颜色
	 * @param color 色值
	 */
	public void setIconColor(int color) {
		icon_lift.setBackgroundColor(color);
	}
	
	/**
	 * 设置图表显示或者隐藏
	 * @param visible 显示或隐藏
	 */
	public void setIconVisible(int visible) {
		icon_lift.setVisibility(visible);
	}
	
	
	/**
	 * 设置是哪种金额
	 * @param kindsText 充值或智慧分享或代言。。。
	 */
	public void setKindsText(String kindsText) {
		kinds.setText(kindsText);
	}
	/**
	 * 设置金额数
	 * @param money
	 */
	public void setMoney(String moneyString) {
		money.setText(moneyString);
	}
}
