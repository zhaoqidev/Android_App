package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

/**
 * 导师名片页面的导师文章条目
 * @author Administrator
 */
public class DaoshiArticleBean {
	/*{
	    "entity": {
	        "articleList": [
	            {
		            "addtime": "2015-07-17 12:19:17",
	                "articleId": 62,
	                "iscollected": false,
	                "picPath": "/upload/eduplat/article/20150921/1442822332801395708.png",
	                "title": "修炼好口才",
	                "viewNum": 7
	            }
	        ],
	        "totalPage": 2
	    },
	    "message": "查询成功",
	    "success": true
	}*/
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public List<ArticleItem> articleList;
		public String totalPage;
	}
	
	public class ArticleItem implements Serializable{
		public String addtime; 
		public String iscollected; //用户是否已经收藏
		public String articleId; 
		public String picPath; 
		public String title; 
		public String viewNum; 
		
	}
	
}
