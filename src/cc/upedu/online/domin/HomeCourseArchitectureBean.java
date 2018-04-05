package cc.upedu.online.domin;

import java.util.List;

public class HomeCourseArchitectureBean {
	private List<Entity> entity;
	private String message;
	private String success;
	
	public List<Entity> getEntity() {
		return entity;
	}

	public void setEntity(List<Entity> entity) {
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
		return "HomeCourseArchitectureBean [entity=" + entity + ", message="
				+ message + ", success=" + success + "]";
	}

	public class Entity{
		private String subjectId;
		private String subjectLogo;
		private String subjectName;
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		public String getSubjectLogo() {
			return subjectLogo;
		}
		public void setSubjectLogo(String subjectLogo) {
			this.subjectLogo = subjectLogo;
		}
		public String getSubjectName() {
			return subjectName;
		}
		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}
		@Override
		public String toString() {
			return "Entity [subjectId=" + subjectId + ", subjectLogo="
					+ subjectLogo + ", subjectName=" + subjectName + "]";
		}
		
	}
}
