package cc.upedu.online.domin;

import java.util.List;


/**
 * 导师名片页面的同学寄语条目
 * @author Administrator
 */
public class DaoshiWishesBean {
	/*
	 * {
    "entity": {
        "totalPage": 1,
        "wordsList": [
            {
                "addtime": "2015-06-15 16:33:57",
                "avatar": "/upload/eduplat/customer/20141115/1416018073798179778.jpg",
                "realname": "山岭巨人",
                "userId": 2,
                "words": "老师，讲的不错，希望您继续为我们解答疑问！",
                "wordsId": 3
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
		public List<WordsItem> wordsList;
		public String totalPage;
	}
	
	public class WordsItem{
		public String addtime; 
		public String avatar; 
		public String realname; 
		public String userId; 
		public String words; 
		public String wordsId; 
	}
	
}
