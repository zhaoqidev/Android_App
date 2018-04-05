package cc.upedu.online.domin;

/**
 * App检查更新的javabean(不是最终版)
 * @author Administrator
 */
public class UpdataAppBean {
/*  {
    "entity": {
        "addTime": "2015-08-26 10:18:44",
        "depict": "android 1.0版本",
        "downloadUrl": "http://192.168.1.147:8080/static/edu/downapp/app.apk",
        "id": 1,
        "kType": "android",
        "versionNo": "1.0"
    },
    "message": "查询成功",
    "success": true
}
*/
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public String depict;//更新说明
		public String downloadUrl;//下载链接
		public String id;
		public String kType;//类型（ios和android）
		public String versionNo;//版本号
		public String addTime;//版本发布时间
		
	}
	
}
