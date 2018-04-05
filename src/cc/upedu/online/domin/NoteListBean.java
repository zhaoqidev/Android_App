package cc.upedu.online.domin;

import java.util.List;

public class NoteListBean {
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
		private List<NoticeItem> noteList;
		private String totalPage;
		
		public List<NoticeItem> getNoteList() {
			return noteList;
		}

		public void setNoteList(List<NoticeItem> noteList) {
			this.noteList = noteList;
		}

		public String getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		@Override
		public String toString() {
			return "Entity [noteList=" + noteList + ", totalPage=" + totalPage
					+ "]";
		}

		public class NoticeItem{
			//同学的头像
			private String avatar;
			//公司名称
			private String company;
			//笔记内容
			private String content;
			private String courseId;
			//用户id
			private String id;
			//目录id
			private String kpointId;
			//职位
			private String position;
			//同学的姓名
			private String realName;
			private String status;
			//发布时间
			private String updateTime;
			//用户的id
			private String userId;
			public String getAvatar() {
				return avatar;
			}
			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}
			public String getCompany() {
				return company;
			}
			public void setCompany(String company) {
				this.company = company;
			}
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
			public String getPosition() {
				return position;
			}
			public void setPosition(String position) {
				this.position = position;
			}
			public String getRealName() {
				return realName;
			}
			public void setRealName(String realName) {
				this.realName = realName;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public String getUpdateTime() {
				return updateTime;
			}
			public void setUpdateTime(String updateTime) {
				this.updateTime = updateTime;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			@Override
			public String toString() {
				return "NoticeItem [avatar=" + avatar + ", company=" + company
						+ ", content=" + content + ", courseId=" + courseId
						+ ", id=" + id + ", kpointId=" + kpointId
						+ ", position=" + position + ", realName=" + realName
						+ ", status=" + status + ", updateTime=" + updateTime
						+ ", userId=" + userId + "]";
			}
			
		}
	}
}
