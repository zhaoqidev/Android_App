package cc.upedu.online.domin;

import java.util.List;

public class CollectCoureListBean {
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
		private List<CollectCoureItem> courseList;
		private String totalPageSize;
		
		public List<CollectCoureItem> getCourseList() {
			return courseList;
		}

		public void setCourseList(List<CollectCoureItem> courseList) {
			this.courseList = courseList;
		}

		public String gettotalPageSize() {
			return totalPageSize;
		}

		public void settotalPageSize(String totalPageSize) {
			this.totalPageSize = totalPageSize;
		}

		@Override
		public String toString() {
			return "Entity [courseList=" + courseList + ", totalPageSize="
					+ totalPageSize + "]";
		}

		public class CollectCoureItem{
			private String courseId;
			//收藏记录ID
			private String favouriteId;
			private String logo;
			private String name;
			private String title;
			private String courseType;
			private String joinNum;//参加人数
			private String teacherName;//老师名称
			public String getTeacherName() {
				return teacherName;
			}
			public void setTeacherName(String teacherName) {
				this.teacherName = teacherName;
			}
			public String getJoinNum() {
				return joinNum;
			}
			public void setJoinNum(String joinNum) {
				this.joinNum = joinNum;
			}
			public String getCourseId() {
				return courseId;
			}
			public void setCourseId(String courseId) {
				this.courseId = courseId;
			}
			public String getLogo() {
				return logo;
			}
			public void setLogo(String logo) {
				this.logo = logo;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getFavouriteId() {
				return favouriteId;
			}
			public void setFavouriteId(String favouriteId) {
				this.favouriteId = favouriteId;
			}
			public String getCourseType() {
				return courseType;
			}
			public void setCourseType(String courseType) {
				this.courseType = courseType;
			}
			@Override
			public String toString() {
				return "CollectCoureItem [courseId=" + courseId
						+ ", favouriteId=" + favouriteId + ", logo=" + logo
						+ ", name=" + name + ", title=" + title
						+ ", courseType=" + courseType + ", joinNum=" + joinNum
						+ ", teacherName=" + teacherName + "]";
			}
			
		}
	}
}
