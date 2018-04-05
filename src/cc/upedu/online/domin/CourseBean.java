package cc.upedu.online.domin;

import java.util.List;

public class CourseBean {
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
		private List<Course> courseList;
		private String totalPageSize;
		
		public List<Course> getCourseList() {
			return courseList;
		}

		public void setCourseList(List<Course> courseList) {
			this.courseList = courseList;
		}

		public String getTotalPageSize() {
			return totalPageSize;
		}

		public void setTotalPageSize(String totalPageSize) {
			this.totalPageSize = totalPageSize;
		}

		@Override
		public String toString() {
			return "Entity [courseList=" + courseList + ", totalPageSize="
					+ totalPageSize + "]";
		}

		public class Course{
			private String courseId;
			private String logo;
			private String name;
			private List<String> subjectList;
			private List<String> teacherList;
			private String title;
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
			public List<String> getSubjectList() {
				return subjectList;
			}
			public void setSubjectList(List<String> subjectList) {
				this.subjectList = subjectList;
			}
			public List<String> getTeacherList() {
				return teacherList;
			}
			public void setTeacherList(List<String> teacherList) {
				this.teacherList = teacherList;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			@Override
			public String toString() {
				return "Course [courseId=" + courseId + ", logo=" + logo
						+ ", name=" + name + ", subjectList=" + subjectList
						+ ", teacherList=" + teacherList + ", title=" + title
						+ "]";
			}
			
		}
	}
}
