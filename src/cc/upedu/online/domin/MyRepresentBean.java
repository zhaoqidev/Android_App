package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的代言的javabean
 * @author Administrator
 *
 */
public class MyRepresentBean {
/*  {
    "entity": {
        "courseList": [
            {
                "courseId": 117,
                "currentPrice": 5,
                "joinNum": 0,
                "lessionnum": 2,
                "logo": "/upload/eduplat/course/20150720/1437379648072348240.png",
                "name": "构建企业商业模式",
                "shareId": 27,
                "teacherName": "陈明亮",
                "title": "文章"
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
		public List<CourseItem> courseList;
		public String totalPageSize;
	};
	
	public class CourseItem{
		public String courseId;
		public String joinNum;//参加人数
		public String name;
		public String logo;
		public String title;
		public String shareId;
		public String currentPrice;
		public String lessionnum;
		public String teacherName;
	}
	
}
