package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;
/**
 * 从微店获取商品列表的javabean
 * @author Administrator
 *
 */
public class MicroMallListFromVdianBean {
	public Result result;
	public Status status;

	public class Result {
		public String item_num;
		public String total_num;
		public List<Item> items;

	}

	public class Status {
		public String status_code;
		public String status_reason;

	}
	
	@SuppressWarnings("serial")
	public class Item implements Serializable{
		
		public List<Cate> cates;
		public String fx_fee_rate;
		public List<String> imgs;
		public String istop;
		public String item_name;
		public String itemid;
		public String price;
		public String seller_id;
		public List<Sku> skus;
		public String sold;
		public String stock;
		public List<String> thumb_imgs;
		public String update_time;
		
	}
	
	public class Cate{
		public String cate_id;
		public String  cate_name;
	}
	
	public  class Sku{
		
		public String id;
		public String price;
		public String stock;
		public String title;

	}
}
