package cc.upedu.online.domin;

import java.util.List;

public class DawnMenuBean {
	List<Entity> entity;
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
		return "DawnMenuBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class Entity{
//		private List<String> childSubjectList;
//		private String createTime;
//		private String level;
//		private String parentId;
//		private String status;
		//分类id
		private String subjectId;
		//分类名称
		private String subjectName;
		//分类图片的URL
		private String subjectLogo;
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		public String getSubjectName() {
			return subjectName;
		}
		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}
		public String getSubjectLogo() {
			return subjectLogo;
		}
		public void setSubjectLogo(String subjectLogo) {
			this.subjectLogo = subjectLogo;
		}
		@Override
		public String toString() {
			return "Entity [subjectId=" + subjectId + ", subjectName="
					+ subjectName + ", subjectLogo=" + subjectLogo + "]";
		}
		
	}
}
