package cc.upedu.online.domin;

public class MyWalletBean {
/*
 * {
    "entity": {
        "coin": 450,
        "money": 412.00
    },
    "message": "查询成功",
    "success": true
}
 */
	
	public String  message;
	public String  success;
	public Entity entity;
	
	public class Entity{
		public String coin;//成长币余额
		public String money;
	}
}
