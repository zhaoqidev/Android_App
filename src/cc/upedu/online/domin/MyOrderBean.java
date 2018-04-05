package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


/**
 * 侧拉栏--我订单的javabean
 * @author Administrator
 *
 */
public class MyOrderBean {
/* 
	 "entity": [
        {
            "amount": 10.00,
            "couponAmount": 0.00,
            "couponCodeId": 0,
            "createTime": "2015-08-03 15:51:36",
            "orderAmount": 10.00,
            "payTime": "2015-08-03 15:51:38",
            "payType": "零钱支付",
            "requestId": "220150803155136154",
            "trxStatus": "已支付",
            "trxorderDetailList": [
                {
                    "courseImgUrl": "/upload/eduplatform_268/default/course/9.jpg",
                    "courseName": "托福英语",
                    "currentPirce": "10.00",
                    "lessionNum": "100"
                }
            ],
            "trxorderId": 25800,
            "version": 0
        }
    ],
    "message": "查询成功",
    "success": true
}
}
*/
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public List<OrderItem> orderList;
		public String totalPageSize;
		public String balance;
		
	}
	public class OrderItem{
		public List<TrxorderDetailList> trxorderDetailList;
		public String amount ;
		public String couponAmount ;
		public String couponCodeId ;
		public String createTime ;//下单时间
		public String orderAmount ;//订单总额
		public String payTime ;
		public String payType ;
		public String requestId ;
		public String trxStatus ;
		public String trxorderId ;
		public String version ;

	}
	
	public class TrxorderDetailList implements Serializable{
		public String courseImgUrl ;
		public String courseName;
		public String currentPirce;
		public String lessionNum;
		public String courseId;
		public String type;
		
	};
	
}
