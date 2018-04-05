package cc.upedu.online.domin;

import java.util.List;


/**
 * 全部学友的javabean
 * @author Administrator
 */
public class SchoolmateAllBean {
	
/*
 * {
    "entity": {
        "totalPage": 1,
        "userList": [
            {
                "avatar": "/upload/eduplatform_268/default/avatar/1.jpg",
                "company": "未公开",
                "userId": 1,
                "userName": "测试用户1"
            },
            {
                "avatar": "/upload/eduplatform_268/default/avatar/3.jpg",
                "company": "未公开",
                "userId": 3,
                "userName": "测试用户3"
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
		public List<SchoolmateItem> userList;
	}
	
	public class SchoolmateItem{
		public String avatar;//头像路径
		public String company;//公司
		public String userId;//用户ID
		public String userName;//姓名
		public String position;//职位
		public String isFriend;//0：已关注 1：互相关注2：未关注
		public String city;//所在城市
		
	}
	
}
