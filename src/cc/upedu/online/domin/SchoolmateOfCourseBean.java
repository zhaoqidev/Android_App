package cc.upedu.online.domin;

import java.util.List;


/**
 * 全部学友的javabean
 * @author Administrator
 */
public class SchoolmateOfCourseBean {
	public String message;
	public String success;
	public Entity entity;
	
	public class Entity{
		public String totalPage;
		public List<SchoolmateItem> classmateList;
	}
	
	public class SchoolmateItem{
		public String avatar;//头像路径
		public String company;//公司
		public String uid;//用户ID
		public String name;//姓名
//		public String position;//职位
		public String isFriend;//0：已关注 1：互相关注2：未关注
		public String city;//所在城市
		
	}
	
}
