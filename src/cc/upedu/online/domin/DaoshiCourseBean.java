package cc.upedu.online.domin;

import java.util.List;



/**
 * 导师名片页面的课程条目
 * @author Administrator
 */
public class DaoshiCourseBean {
	/*{
    "entity": {
        "courseList": [
            {
                "courseId": 1,
                "lessonNum": "01:30",
                "logo": "/upload/eduplatform_268/default/course/1.jpg",
                "name": "英语大全",
                "viewcount": 2775
            }
        ],
        "totalPage": 1
    },
    "message": "查询成功",
    "success": true
}*/
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public List<CourseItem> courseList;
		public String totalPage;
	}
	
	public class CourseItem{
		public String courseId; 
		//public String intro; 
		public String lessonNum; 
		public String logo; 
		public String name; 
		public String courseType; 
		public String viewcount; 
	}
}
