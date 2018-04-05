package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


/**
 * 活动页面
 * @author Administrator
 */
public class SportsBean {
	/*
	 * {
    "entity": {
        "activityList": [
            {
                "address": "朝阳区罗马嘉园",
                "addtime": "2015-06-25 15:40:58",
                "browseNum": 0,
                "city": "北京",
                "id": 3,
                "joinNum": 0,
                "likeNum": 0,
                "shareNum": 0,
                "startDt": "2015-08-08 09:18:00",
                "status": 0,
                "title": "测试222",
                "uid": 2
            }
        ],
        "totalPageSize": 1
    },
    "message": "查询成功",
    "success": true
}
	 */
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public List<ActivityItem> activityList;
		public String totalPageSize;
	}
	
	public class ActivityItem implements Serializable{
		public String address; //活动地址
		public String logo; //活动封面图片
		public String addtime; 
		public String browseNum; 
		public String city; //城市
		public String id; //活动id
		public String joinNum; 
		public String likeNum; 
		public String shareNum; 
		public String startDt; //活动开始时间
		public String status; 
		public String title; //活动标题
		public String uid; 
		public String timeLimit;//距活动开始还有多长时间
		public String avatar;
		public String uname;
	}
	
}
