package cc.upedu.online.domin;

import java.util.List;
/**
 * 发现--导师列表导师条目
 * @author Administrator
 */
public class DaoshiBean {
/*
 * 
 "entity": {
        "teacherList": [
             {
                "articleNum": 2,
                "courseNum": 1,
                "intro": "倬学文化创始人",
                "name": "陈明亮",
                "picPath": "/upload/eduplat/teacher/20150720/1437377837338107645.png",
                "teacherId": 108
            }
            ],
        "totalPage": 3
    },
    "message": "查询成功",
    "success": true
 */
	
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public String totalPage;
		public List<TeacherItem> teacherList;
	}
	public TeacherItem getTeacherList(){
		return new TeacherItem();
	}
	public class TeacherItem{
		public String intro;
		public String picPath;
		public String name;
	  //public String career;//导师职位
		public String teacherId;
		public String articleNum;//文章数
		public String courseNum;//课程数
	}
}
