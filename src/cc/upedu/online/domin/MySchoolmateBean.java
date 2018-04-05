package cc.upedu.online.domin;

import java.util.List;


/**
 * 侧拉栏，我关注的好友的javabean
 * @author Administrator
 */
public class MySchoolmateBean {
	
/*
 * "entity": {
        "followList": [
            {
                "avatar": "/upload/eduplatform_268/default/avatar/3.jpg",
                "city": "北京市",
                "mutual": 1,
                "name": "测试用户3",
                "userId": 3
            }
            {
                "avatar": "/upload/eduplat/customer/20150703/1435910323811221618.jpg",
                "company": "中和黄埔",
                "mutual": 0,
                "name": "测试用户13005",
                "position": "董事长",
                "userId": 13005
            }
        ],
        "totalPage": 1
    },
    "message": "查询成功",
    "success": true
}
 *
 */
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public String totalPage;
		public List<SchoolmateItem> followList;
	}
	
	public class SchoolmateItem{
		public String avatar;//头像路径
		public String company;//公司
		public String userId;//用户ID
		public String name;//姓名
		public String position;//职位
		public String mutual;//0:单向关注 1:已互相关注
		public String city;//所在城市
		
	}
	
}
