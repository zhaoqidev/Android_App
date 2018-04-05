package cc.upedu.online.domin;

/**
 * 检查订单的页面
 * @author Administrator
 */
public class CheckOrderBean {
	
	/*{
	    "entity": {
	        "bankAmount": "1.00",
	        "orderId": 25929,
	        "orderNo": "1303320150811102917784",
	        "outTradeNo": "PAY224",
	        "payType": "WEIXIN"
	    },
	    "message": "用户账户余额不足！",
	    "success": false
	}*/
	
	public String message;
	public String success;
	public Entity entity;

	public class Entity{
		
		public String orderNo;//订单号
		public String bankAmount;//实际支付金额
		public String payType;//支付类型
		public String orderId;//订单ID
		public String outTradeNo;//

		
	}
	
}
