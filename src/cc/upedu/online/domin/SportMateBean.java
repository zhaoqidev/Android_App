package cc.upedu.online.domin;

import java.util.List;


/**
 * 活动详情页面，已加入学友的javabean
 * @author Administrator
 */
public class SportMateBean {
	
/*
 * {
    "entity": {
        "isjoin": 1,
        "joinUserList": [
            {
                "activityId": 1,
                "addtime": "2015-06-25 16:36:36",
                "avatar": "/upload/eduplatform_268/default/avatar/8.jpg",
                "id": 1,
                "isFriend": 0,
                "uid": 11,
                "uname": "测试用户8"
            },
            {
                "activityId": 1,
                "addtime": "2015-06-29 18:40:57",
                "avatar": "/upload/eduplat/customer/20141115/1416018073798179778.jpg",
                "id": 2,
                "isFriend": 0,
                "ucompany": "倬脉",
                "uid": 2,
                "uname": "山岭巨人",
                "uposition": "董事长"
            }
        ]
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
		public List<JoinUserItem> joinUserList;
	}
	
	public class JoinUserItem{
		public String avatar;//头像路径
		public String ucompany;//公司
		public String uid;//用户ID
		public String uname;//姓名
		public String uposition;//职位
		public String isFriend;//0：未关注 1：已关注 2：互相关注
		public String activityId;//活动ID
		public String addtime;
		
	}
	
}
