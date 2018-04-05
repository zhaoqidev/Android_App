package cc.upedu.online.domin;

import java.util.List;

/**
 * 导师名片页面的JavaBean
 * @author Administrator
 */
public class DaoshiCardBean {
/*
 * 
 
    "entity": {
        "honors": "",
        "introduct": "郭翔，北京师范大学法学院副教授、清华大学法学博士。自2004年至今已有9年的司法考试培训经验。长期从事司法考试辅导，深知命题规律，了解解题技巧。内容把握准确，授课重点明确，层次分明，调理清晰，将法条法理与案例有机融合，强调综合，深入浅出。",
        "name": "杨老师",
        "picArray": [
            "/static/edu/images/myPic/image/20150608/20150608111227_701.jpg",
            "/static/edu/images/myPic/image/20150608/20150608111227_463.jpg"
        ],
        "picPath": "/upload/eduplat/teacher/20150608/1433733205893520700.jpg",
        "teacherId": 68,
        "vidiourl": "http://yuntv.letv.com/bcloud.html?uu=3e58f2d148&vu=6838c2177c&pu=6b998c2e3b&auto_play=1&gpcflag=1&width=640&height=360"
    },
    "message": "查询成功",
    "success": true
}
 */
	
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public String honors;//导师荣誉
		public String education;//导师职称
		public String introduct;//导师简介
		public String name;
		public String picPath;//导师图片
		public String teacherId;
		public String teacherUid;//导师的用户Id
		public String vidiourl;//介绍视频url
		public String videotype;
		public List<String> picArray;//导师风采图片列表
	}
	
}
