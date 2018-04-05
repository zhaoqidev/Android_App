package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的布局课程列表的javabean
 * @author Administrator
 *
 */
public class MyNoteCourseBean {
	private String message;
	private String success;
	private Entity entity;
	
	@Override
	public String toString() {
		return "MyNoteCourseBean [message=" + message + ", success=" + success
				+ ", entity=" + entity + "]";
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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public class Entity{
		private List<ClassItem> classList;

		public List<ClassItem> getClassList() {
			return classList;
		}

		public void setClasslist(List<ClassItem> classList) {
			this.classList = classList;
		}

		@Override
		public String toString() {
			return "Entity [classList=" + classList + "]";
		}
		
	};
	
	public class ClassItem{
		//课程ID
		private String courseId;
		//课程名
		private String courseName;
		//笔记最后修改时间(课程中任何笔记)
		private String modifyTime;
		//图片路径（课程）
		private String logo;
		//老师姓名
		private String teacherName;
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
		public String getModifyTime() {
			return modifyTime;
		}
		public void setModifyTime(String modifyTime) {
			this.modifyTime = modifyTime;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public String getTeacherName() {
			return teacherName;
		}
		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}
		@Override
		public String toString() {
			return "ClassItem [courseId=" + courseId + ", courseName="
					+ courseName + ", modifyTime=" + modifyTime + ", logo="
					+ logo + ", teacherName=" + teacherName + "]";
		}
		
	}
	
}
