package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


/**
 * 我的收货地址页面
 * @author Administrator
 */
public class MyReceivingAddressBean {
	/*
	 * {
    "entity": {
        "addressList": [
            {
                "address": "某仓库",
                "addressId": 5,
                "area": 837,
                "areaText": "和平区",
                "city": 71,
                "cityText": "沈阳市",
                "mobile": "18518673305",
                "personName": "张平",
                "province": 7,
                "provinceText": "辽宁省"
            }
        ]
    },
    "message": "查询成功",
    "success": true
}
	 */
	public String success;
	public String message;
	public Entity entity;
	
	public class Entity{
		public List<AddressItem> addressList;
	}
	
	public static class AddressItem implements Serializable{
		public String address;
		public String addressId;
		public String area;
		public String areaText;
		public String city;
		public String cityText;
		public String mobile;
		public String personName;
		public String province;
		public String provinceText;
		public String isdefault;
	}
	
	
}
