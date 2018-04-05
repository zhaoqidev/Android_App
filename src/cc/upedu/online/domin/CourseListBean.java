package cc.upedu.online.domin;

import java.util.List;

public class CourseListBean {
	private Entity entity;
	private String message;
	private String success;
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
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
		return "CourseBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class Entity{
		private List<CourseItem> courseList;
		private String totalPage;
		
		public List<CourseItem> getCourseList() {
			return courseList;
		}

		public void setCourseList(List<CourseItem> courseList) {
			this.courseList = courseList;
		}

		public String gettotalPage() {
			return totalPage;
		}

		public void settotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		@Override
		public String toString() {
			return "Entity [courseList=" + courseList + ", totalPage="
					+ totalPage + "]";
		}

	}
}
