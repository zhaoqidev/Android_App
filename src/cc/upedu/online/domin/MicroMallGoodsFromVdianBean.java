package cc.upedu.online.domin;

import java.util.List;
/**
 * 从微店获取单个商品信息的javabean
 * @author Administrator
 *
 */
public class MicroMallGoodsFromVdianBean {
	public Result result;
	public Status status;

	public class Status {
		public String status_code;
		public String status_reason;

	}
	
	
	public class Result {
		
		public List<Cate> cates;
		public String fx_fee_rate;
		public List<String> imgs;
		public String istop;
		public String item_desc;
		public String item_name;
		public String itemid;
		public String merchant_code;
		public String price;
		public String seller_id;
		public List<Sku> skus;
		public String sold;
		public String stock;
		public String status;
		public List<String> thumb_imgs;
		
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
