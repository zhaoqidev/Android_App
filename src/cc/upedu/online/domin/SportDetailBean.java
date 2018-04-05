package cc.upedu.online.domin;

import java.util.List;

/**
 * 活动详情的javaBean
 * @author Administrator
 *
 */
public class SportDetailBean {
/*
 * 
 * {
    "entity": {
        "activityInfo": {
            "address": "朝阳区罗马嘉园",
            "addtime": "2015-06-25 15:32:38",
            "avatar": "/upload/eduplat/customer/20141115/1416018073798179778.jpg",
            "browseNum": 0,
            "city": "北京",
            "contactList": [
                {
                    "activityId": 1,
                    "addtime": "2015-06-25 15:51:49",
                    "contact": "张女士",
                    "id": 1,
                    "mobile": "15600001111"
                }
            ],
            "id": 1,
            "joinNum": 0,
            "joinUserList": [
                {
                    "activityId": 1,
                    "addtime": "2015-06-25 16:36:36",
                    "avatar": "/upload/eduplatform_268/default/avatar/8.jpg",
                    "id": 1,
                    "isFriend": 0,
                    "uid": 11,
                    "uname": "测试用户8"
                }
            ],
            "likeNum": 0,
            "picList": [
                {
                    "activityId": 1,
                    "addtime": "2015-06-25 16:04:05",
                    "id": 7,
                    "picPath": "/upload/yyyyyy/111.png",
                    "version": 1
                }
            ],
            "shareNum": 0,
            "startDt": "2015-07-01 09:18:00",
            "status": 0,
            "title": "测试",
            "ucompany": "倬脉",
            "uid": 2,
            "uname": "山岭巨人",
            "uposition": "董事长"
        },
        "isjoin": 0
    },
    "message": "查询成功",
    "success": true
}
 * 
 */
	
	public Entity entity;
	public String message;
	public String success;
	
	public class Entity{
		public ActivityInfo activityInfo;
		public String isjoin;//是否参加
		public String iscollected;//是否收藏
		
	}
	public class ActivityInfo{
		public String address;
		public String addtime;
		public String avatar;
		public String content;//活动内容
		public String browseNum;
		public String city;
		public List<ContactItem> contactList;
		public String id;
		public String joinNum;
		public List<JoinUserItem> joinUserList;
		public String likeNum;
		public List<PicList> picList;
		public String shareNum;
		public String startDt;
		public String status;
		public String title;
		public String ucompany;
		public String uid;
		public String uname;
		public String uposition;
		public String endDt;//活动结束时间

		public String logo;
		
	}
	public class ContactItem{
		public String activityId;
		public String addtime;
		public String contact;//联系人姓名
		public String id;
		public String mobile;
		public String email;
		
	}
	public class JoinUserItem{
		public String activityId;
		public String addtime;
		public String avatar;
		public String id;
		public String isFriend;
		public String uid;
		public String uname;
	}
	public class PicList{
		public String activityId;
		public String addtime;
		public String id;
		public String picPath;
		public String version;
	}
	
}
