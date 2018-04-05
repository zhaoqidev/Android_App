package cc.upedu.online.domin;

import java.util.List;

public class TextAnswerListBean {
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
		private List<TextAnswerItem> qaWordsList;
		private String totalPage;
		
		public List<TextAnswerItem> getQaWordsList() {
			return qaWordsList;
		}

		public void setQaWordsList(List<TextAnswerItem> qaWordsList) {
			this.qaWordsList = qaWordsList;
		}

		public String getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		@Override
		public String toString() {
			return "Entity [qaWordsList=" + qaWordsList + ", totalPage="
					+ totalPage + "]";
		}

		public class TextAnswerItem{
			//答案
			private String content;
			//发布时间
			private String createTime;
			//答疑id
			private String id;
			//导师id
			private String teacherId;
			//简介
			private String intro;
			//问题
			private String title;
			public String getContent() {
				return content;
			}
			public void setContent(String content) {
				this.content = content;
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
			public String getTeacherId() {
				return teacherId;
			}
			public void setTeacherId(String teacherId) {
				this.teacherId = teacherId;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			@Override
			public String toString() {
				return "TextAnswerItem [content=" + content + ", createTime="
						+ createTime + ", id=" + id + ", teacherId="
						+ teacherId + ", title=" + title + "]";
			}
			
		}
	}
}
