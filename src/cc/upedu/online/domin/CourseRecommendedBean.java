package cc.upedu.online.domin;

import java.util.List;
/**
 * 推荐课程的实体类
 * @author Administrator
 *
 */
public class CourseRecommendedBean {
	private List<CourseItem> entity;
	private String message;
	private String success;
	
	public List<CourseItem> getEntity() {
		return entity;
	}

	public void setEntity(List<CourseItem> entity) {
		this.entity = entity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "RecmdCourseBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class CourseItem {
		//课程ID
		private String courseId;
		//课程名
		private String courseName;
		private String id;
		//简介
		private String intro;
		//课程图片路径
		private String logo;
		private String orderNum;
		//推荐类型id  1:推荐课程 2：免费课程
		private String recommendId;
		public String getCourseId() {
			return courseId;
		}
		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getintro() {
			return intro;
		}
		public void setintro(String intro) {
			this.intro = intro;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public String getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}
		public String getRecommendId() {
			return recommendId;
		}
		public void setRecommendId(String recommendId) {
			this.recommendId = recommendId;
		}
		@Override
		public String toString() {
			return "CourseItem [courseId=" + courseId + ", courseName="
					+ courseName + ", id=" + id + ", intro="
					+ intro + ", logo=" + logo + ", orderNum=" + orderNum
					+ ", recommendId=" + recommendId + "]";
		}
		
	}
}
