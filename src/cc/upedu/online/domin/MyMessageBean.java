package cc.upedu.online.domin;

import java.util.List;


/**
 * 侧拉栏--我消息的javabean
 * @author Administrator
 *
 */
public class MyMessageBean {
/* 
	{
    "entity": {
        "msgList": [
            
            {
                "avatar": "/upload/eduplatform_268/default/avatar/8.jpg",
                "content": "123412341234",
                "fromUserId": 0,
                "name": "系统消息",
                "sendTime": "2015-02-08 03:25:15"
            }
        ],
        "totalPage": 4
    },
    "message": "查询成功",
    "success": true
}
*/
	public String message;
	public String success;
	public Entity entity; 
	
	public class Entity{
		public List<MsgItem> msgList;
		public String totalPage;
	}
	
	public class MsgItem{
		public String msgId;//消息ID
		public String avatar;
		public String content;
		public String fromUserId;//发送者Id
		public String name;//发送者姓名
		public String sendTime;
		public String type;//消息类型（1：网校消息 2：社区消息）
	};
	
}
