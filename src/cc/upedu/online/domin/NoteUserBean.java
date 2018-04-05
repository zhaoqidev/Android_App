package cc.upedu.online.domin;

import java.io.Serializable;

public class NoteUserBean {
	private NoteUserItem entity;
	private String message;
	private String success;
	
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

	public NoteUserItem getEntity() {
		return entity;
	}

	public void setEntity(NoteUserItem entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "NoteUserBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class NoteUserItem implements Serializable{
		//笔记内容
		private String content;
		private String courseId;
		//id
		private String id;
		private String kpointId;
		private String status;
		private String userId;
		//发布的时间
		private String updateTime;
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getCourseId() {
			return courseId;
		}
		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getKpointId() {
			return kpointId;
		}
		public void setKpointId(String kpointId) {
			this.kpointId = kpointId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
		@Override
		public String toString() {
			return "NoteUserItem [content=" + content + ", courseId="
					+ courseId + ", id=" + id + ", kpointId=" + kpointId
					+ ", status=" + status + ", userId=" + userId
					+ ", updateTime=" + updateTime + "]";
		}
		
	}
}
