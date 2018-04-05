package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

/**
 * 微商城的javabean
 * @author Administrator
 *
 */
public class MicroMallBean{
	/*
	 * 
    "entity": {
        "commodityList": [
            {
                "commodityId": 66,
                "commodityName": "康宁家用高精度电子体温计",
                "imageUrl": "/upload/eduplat/gift/20151026/1445824236681710807.png",
                "pointprice": 34
            }
        ],
        "totalPageSize": 2
    },
    "message": "查询成功",
    "success": true
}
	 */
	
	public String success;
	public String message;
	public Entity entity;
	
	public class Entity{
		public List<CommodityItem> commodityList;
		public String totalPageSize;
	
	}
	
	public class CommodityItem implements Serializable{
		/**
		 * 商品ID
		 */
		public String commodityId;
		/**
		 * 商品名称
		 */
		public String commodityName;
		/**
		 * 图片url
		 */
		public String imageUrl;
		/**
		 * 成长币价格
		 */
		public String pointprice;
		
	}
	
	public static class DoubleCommodityItem{
		public List<CommodityItem> commodityList;
		public List<CommodityItem> commodityList2;
	}
	
}
