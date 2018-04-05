package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的课程的javabean
 * @author Administrator
 *
 */
public class MyCourseBean {
/*  "entity": [
        {
            "courseId": 94,
            "courseType": "COURSE",
            "iscollected": true,
            "joinNum": 17,
            "logo": "/upload/eduplat/course/20151116/1447641844489779881.png",
            "name": "冠军之路",
            "teacherName": {
                "education": "互联网专家",
                "fansNum": 0,
                "id": 70,
                "likeNum": 0,
                "name": "leo Tan",
                "picPath": "/upload/eduplat/teacher/20150820/1440063441068511281.png",
                "seriesNum": 0
            },
            "title": "《冠军之路》-行业冠军育成私房课是由中国著名管理大师杨思卓老师亲自研发而成，课程时间是三天三夜，在课程学员定位上是由企业核心人物带领两位核心团队成员共同参加学习，属于“企业价值突变课程系统”针对企业核心团队的“商道融达”阶段的第一堂课程。"
        }
    ],
    "message": "查询成功",
    "success": true
}
*/
	public String message;
	public String success;
	public List<CourseItem> entity;
	
	public class CourseItem{
		public String courseId;
		public String joinNum;
		public String name;
		public TeacherName teacherName;
		public String logo;
		public String title;
		public String courseType;
	};
	public class TeacherName{
		public String id;
		public String name;
	}
}
