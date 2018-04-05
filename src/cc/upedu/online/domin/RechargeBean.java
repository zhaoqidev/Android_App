package cc.upedu.online.domin;



/**
 * 充值页面的javaBean
 * @author Administrator
 */


public class RechargeBean {
	
	public String message;
	public String success;
	public Entity entity;

	public class Entity{
		public String balance;//用户帐号余额
		public String bankAmount;
		public String orderId;
		public String orderNo;
		public String payType;
		public String outTradeNo;
		
	}
	
}
