package cc.upedu.online.domin;

import java.util.List;

import cc.upedu.online.domin.HobbyListBean.HobbyItem;
import cc.upedu.online.domin.IndustryListBean.IndustryItem;
import cc.upedu.online.domin.PositionListBean.PositionItem;

/**
 * 用户登陆的javabean
 * 
 * @author Administrator
 * 
 */
public class LoginBean {
	/*
	 * { "entity": { "id": 13014, "mobile": "18518673305", "name": "张平",
	 * "userType": 0 }, "message": "登录成功", "success": true }
	 */
	public String message;
	public String success;
	public Entity entity;

	public class Entity {
		public String mobile;
		public String name;
		public String userType;
		public String id;
		public String loginsid;// 用户识别码
		public String avatar;// 用户头像
		public String userInfo;// 个性签名

		public List<String> bannerList;
		public List<HobbyItem> hobbyList;
		public List<IndustryItem> industryList;
		public List<PositionItem> positionList;
	};
/*
	public class ConstellationItem {
		public String applogo;
		public String content;
		public String id;
		public String type;
	}

	public class HobbyItem {
		public String applogo;
		public String content;
		public String id;
		public String type;
	}
	public class IndustryItem {
		public String content;
		public String id;
		public String type;
	}

	public class PositionItem {
		public String content;
		public String id;
		public String type;
	}

	public class ZodiacItem {
		public String applogo;
		public String content;
		public String id;
		public String type;
	}
*/
}
