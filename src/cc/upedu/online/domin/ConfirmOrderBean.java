package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


/**
 * 确认订单的页面
 * @author Administrator
 */

/*{
    "entity": {
        "balance": "0.00",
        "orderInfo": {
            "amount": 2283.00,
            "couponAmount": 0.00,
            "couponCodeId": 0,
            "createTime": "2015-08-11 19:26:32",
            "orderAmount": 2283.00,
            "payType": "支付宝支付",
            "requestId": "1303320150811192632035",
            "trxStatus": "未支付",
            "trxorderDetailList": [
                {
                    "courseImgUrl": "/upload/eduplatform_268/default/course/0.jpg",
                    "courseName": "CAD基础入门到精通",
                    "currentPirce": "2283.00",
                    "lessionNum": "45"
                }
            ],
            "trxorderId": 25940,
            "version": 0
        }
    },
    "message": "下单成功",
    "success": true
}*/
public class ConfirmOrderBean {
	
	public String message;
	public String success;
	public Entity entity;

	public class Entity{
		public String balance;//用户帐号余额
		public OrderInfo orderInfo;
		public String isPayed;//成长币是否能够全部抵扣，能返回“true”，不能返回null；
	}
	public class OrderInfo {
		public List<ConfrimOrderList> trxorderDetailList;
		public String amount;
		public String couponAmount;
		public String couponCodeId;
		public String createTime;
		public String orderAmount;
		public String payType;
		public String requestId;
		public String trxStatus;
		public String trxorderId;
		public String version;
		
	}
	public class ConfrimOrderList implements Serializable{
		
		public String courseImgUrl;
		public String courseName;
		public String currentPirce;
		public String lessionNum;
		
	}
	
}
