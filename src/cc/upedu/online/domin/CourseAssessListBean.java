package cc.upedu.online.domin;

import java.util.List;

public class CourseAssessListBean {
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
		private List<CourseAssessItem> courseAssessList;
		private String totalPage;
		
		public List<CourseAssessItem> getCourseAssessList() {
			return courseAssessList;
		}

		public void setCourseAssessList(List<CourseAssessItem> courseAssessList) {
			this.courseAssessList = courseAssessList;
		}

		public String getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		@Override
		public String toString() {
			return "Entity [courseAssessList=" + courseAssessList
					+ ", totalPage=" + totalPage + "]";
		}

		public class CourseAssessItem{
			//用户图片url
			private String avatar;
			//介绍
			private String content;
			//课程id
			private String courseId;
			//创建时间
			private String createTime;
			private String id;
			//用户名
			private String realname;
			//用户id
			private String userId;
			public String getAvatar() {
				return avatar;
			}
			public void setAvatar(String avatar) {
				this.avatar = avatar;
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
			public String getCreateTime() {
				return createTime;
			}
			public void setCreateTime(String createTime) {
				this.createTime = createTime;
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getRealname() {
				return realname;
			}
			public void setRealname(String realname) {
				this.realname = realname;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			@Override
			public String toString() {
				return "CourseAssessItem [avatar=" + avatar + ", content="
						+ content + ", courseId=" + courseId + ", createTime="
						+ createTime + ", id=" + id + ", realname=" + realname
						+ ", userId=" + userId + "]";
			}
			
		}
	}
}
