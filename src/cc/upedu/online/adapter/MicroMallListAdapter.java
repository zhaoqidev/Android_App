package cc.upedu.online.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.MicroMallGoodsDetailActivity;
import cc.upedu.online.activity.MicroMallOrderActivity;
import cc.upedu.online.domin.MicroMallListFromVdianBean.Item;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.StringUtil;

/**
 * 微商城商品列表页面的adapter
 * 
 * @author Administrator
 * 
 */
public class MicroMallListAdapter extends BaseAdapter {
	String token;
	List<Item> listLeft=new ArrayList<Item>();
	List<Item> listRight=new ArrayList<Item>();
	 
	private Context context;
	
	private String coin;//用户成长币个数
	
	public MicroMallListAdapter(Context context, List<Item> list,String coin,String token) {
		this.context = context;
		this.coin = coin;
		this.token=token;
		
		
		for (int i = 0; i < list.size(); i++) {
			if (i%2==0) {
				listLeft.add(list.get(i));
			}else {
				listRight.add(list.get(i));
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		// 复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.activity_micromall_item, null);
			holder = new ViewHolder();
			holder.iv_goods_left =  (ImageView) view.findViewById(R.id.iv_goods);//商品图片
			holder.tv_goods_name_left = (TextView) view.findViewById(R.id.tv_goods_name);
//			holder.tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
			holder.tv_goods_coin_left = (TextView) view.findViewById(R.id.tv_goods_coin);//兑换商品所需的成长币
			holder.rl_goods_left=(LinearLayout) view.findViewById(R.id.rl_goods1);//商品一布局
			holder.tv_add_shopcar_left=(TextView) view.findViewById(R.id.tv_add_shopcar);//添加购物车
			
			holder.iv_goods_right =  (ImageView) view.findViewById(R.id.iv_goods2);//商品图片
			holder.tv_goods_name_right = (TextView) view.findViewById(R.id.tv_goods_name2);
			holder.tv_goods_coin_right = (TextView) view.findViewById(R.id.tv_goods_coin2);//兑换商品所需的成长币
			holder.rl_goods_right=(LinearLayout) view.findViewById(R.id.rl_goods2);//商品二布局
			holder.tv_add_shopcar_right=(TextView) view.findViewById(R.id.tv_add_shopcar2);//添加购物车

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		if (position<listLeft.size()) {

			holder.rl_goods_right.setVisibility(View.GONE);
			/*ImageUtils.setBackGroundImage(listLeft.get(position).imgs.get(0),
					holder.iv_goods_left, R.drawable.img_course);*/

			ImageUtils.setImageFullPath(listLeft.get(position).imgs.get(0),
					holder.iv_goods_left, R.drawable.img_course);
			holder.tv_goods_name_left.setText(listLeft.get(position).item_name);
			holder.tv_goods_coin_left.setText(listLeft.get(position).price+"成长币");
			
		}
		
		if (position<listRight.size()) {
			holder.rl_goods_right.setVisibility(View.VISIBLE);
			
			/*ImageUtils.setBackGroundImage(listRight.get(position).imgs.get(0),
					holder.iv_goods_right, R.drawable.img_course);*/

			ImageUtils.setImageFullPath(listRight.get(position).imgs.get(0),
					holder.iv_goods_right, R.drawable.img_course);

			holder.tv_goods_name_right.setText(listRight.get(position).item_name);
			holder.tv_goods_coin_right.setText(listRight.get(position).price+"成长币兑换");
			
		}
		//进入商品详情，左侧
		holder.rl_goods_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpInGoodsDetails(listLeft.get(position));
			}
		});
		//进入商品详情，右侧
		holder.rl_goods_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpInGoodsDetails(listRight.get(position));
			}
		});
		//进入确认订单，左侧
		holder.tv_add_shopcar_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (coin !=null) {
					jumpInstruction(MicroMallOrderActivity.class,listLeft.get(position),coin);
				}
			}
		});
		//进入确认订单，右侧
		holder.tv_add_shopcar_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (coin !=null) {
					jumpInstruction(MicroMallOrderActivity.class,listRight.get(position),coin);
				}
				
			}
		});
		
		return view;
	}
	
	private class ViewHolder {
		ImageView iv_goods_left;//商品图片
		TextView tv_goods_name_left;// 商品名称
//		TextView tv_goods_price;// 商品价格
		TextView tv_goods_coin_left;// 兑换所需要的成长币
		
		ImageView iv_goods_right;//商品图片
		TextView tv_goods_name_right;// 商品名称
		TextView tv_goods_coin_right;// 兑换所需要的成长币
		
		LinearLayout rl_goods_left;//左侧商品的布局
		LinearLayout rl_goods_right;//右侧商品的布局
		
		TextView tv_add_shopcar_left;//左侧商品加入购物车
		TextView tv_add_shopcar_right;//右侧商品加入购物车
	}
	/**
	 * 跳转到确认订单页面
	 */
	private void jumpInstruction( Class<?> cls,Item currentCommodityItem,String coin) {
		Intent intent = new Intent(context, cls);
//		intent.putExtra("commodityItem", (Serializable) currentCommodityItem);
		PreferencesObjectUtil.saveObject(currentCommodityItem, "commodityItem", context);
		if (!StringUtil.isEmpty(coin)) {
			intent.putExtra("coin", coin);
		}
		context.startActivity(intent);
	}
	/**
	 * 跳转到商品详情页面
	 * @param item
	 */
	private void jumpInGoodsDetails(Item item){
		Intent intent = new Intent(context, MicroMallGoodsDetailActivity.class);
		PreferencesObjectUtil.saveObject(item, "commodityItem", context);
		intent.putExtra("itemid", item.itemid);
		intent.putExtra("token", token);
		intent.putExtra("coin", coin);
		context.startActivity(intent);
	}

	@Override
	public int getCount() {
		return listLeft.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
