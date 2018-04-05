package cc.upedu.online.domin;

import java.util.List;

/**
 * 导师和代理商收入明细列表的javaBean
 * @author Administrator
 *
 */
public class MyWalletDaoshiDetailBean {
/*
 * {
    "entity": {
        "detailList": [
            {
                "bonus": 12.00,
                "courseName": "IOS8最快入门",
                "coursePrice": 60,
                "createTime": "2015-06-16 15:36:24",
                "detailId": 119,
                "userName": "shy"
            }
        ],
        "searchTotal": 12.00,
        "totalPage": 1
    },
    "message": "查询成功",
    "success": true
}
 */
	public String message;
	public String success;
	public Entity entity;
	public class Entity{
		public String totalPage;
		public List<detailItem> detailList;
		public String searchTotal;
	}
	public class detailItem{
		public String bonus;
		public String courseName;
		public String coursePrice;
		public String createTime;
		public String detailId;
		public String userName;
		public String userId;
	}
	
		
	}

