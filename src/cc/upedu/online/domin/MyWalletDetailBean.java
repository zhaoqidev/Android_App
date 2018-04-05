package cc.upedu.online.domin;

import java.util.List;

/**
 * 收支--明细列表的javaBean，代理商，代言人，导师
 * @author Administrator
 *
 */
public class MyWalletDetailBean {
/*
 * {
    "entity": {
        "recordList": [
            {
                "addTime": "2015-09-22 16:17:55",
                "amount": 1,
                "description": "送给朋友<tomato>1成长币",
                "type": "支出",
                "typeInt": 4
            }
        ],
        "totalPageSize": 1
    },
    "message": "查询成功",
    "success": true
}
}
 */
	public String message;
	public String success;
	public Entity entity;
	public class Entity{
		public String totalPageSize;
		public List<recordItem> recordList;
	}
	public class recordItem{
		public String addTime;
		public String amount;
		public String description;
		public String type;//类型，收入或支出
		public String userName;
		public String userId;
		public String courseId;//课程ID
		public String courseName;//课程名
	}
	
		
	}

