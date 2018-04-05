package cc.upedu.online.domin;

import java.io.Serializable;


/**
 * 根据用户Id获取用户信息的bean
 * @author Administrator
 */
public class UserInfoBean {
	/*{
	    "entity": {
	        "avatar": "/upload/eduplatform_268/default/avatar/4.jpg",
	        "uname": "王一飞"
	    },
	    "message": "查询成功",
	    "success": true
	}*/
	
	public String message;
	public String success;
	public UserInfo entity;
	
	public class UserInfo implements Serializable{
		public String avatar;
		public String uname;
		public String position;
	}
}
