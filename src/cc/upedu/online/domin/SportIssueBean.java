package cc.upedu.online.domin;

import java.util.List;

/**
 * 发布活动的javaBean
 * @author Administrator
 *
 */
public class SportIssueBean {
	/*
	 * {
    "entity": {
        "activityInfo": {
            "address": "罗马嘉园",
            "addtime": "2015-08-21 10:50:36",
            "aid": 22,
            "avatar": "/upload/eduplat/fromApp/20150813/1439455016587037928.png",
            "browseNum": 1,
            "city": "石家庄市",
            "contactList": [],
            "id": 22,
            "joinNum": 0,
            "likeNum": 0,
            "logo": "/upload/eduplat/activity/hd_005.png",
            "picList": [],
            "shareNum": 0,
            "startDt": "2015-10-01 09:18:00",
            "status": 0,
            "timeLimit": 3536844,
            "title": "同乡聚会",
            "uid": 13033,
            "uname": "许多的"
        },
        "isjoin": 0,
        "totalPage": 0
    },
    "message": "更新成功",
    "success": true
}
	 */
	public String message;
	public String success;
	public Entity entity ;
	
	public class Entity{
		public String isjoin;
		public String totalPage;
		public ActivityInfo activityInfo ;
	}
	
	public class ActivityInfo{
		public String address;
		public String addtime;
		public String aid;
		public String avatar;
		public String browseNum;
		public String city;
		public String id;
		public String joinNum;
		public String likeNum;
		public String logo;
		public String shareNum;
		public String startDt;
		public String status;
		public String timeLimit;
		public String title;
		public String uid;
		public String uname;
		public List<ContactItem> contactList;
		public List<picItem> picList;
	}
	
	public class ContactItem{
		public String activityId;
		public String addtime;
		public String contact;//联系人姓名
		public String id;
		public String mobile;
		public String email;
	}
	public class picItem{
		public String activityId;
		public String addtime;
		public String id;
		public String picPath;
		public String version;
	}
	
	
}
