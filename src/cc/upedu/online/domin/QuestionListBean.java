package cc.upedu.online.domin;

import java.util.List;

public class QuestionListBean {
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
		private List<QuestionItem> questionList;
		private String totalPage;
		
		public List<QuestionItem> getQuestionList() {
			return questionList;
		}

		public void setQuestionList(List<QuestionItem> questionList) {
			this.questionList = questionList;
		}

		public String getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		@Override
		public String toString() {
			return "Entity [questionList=" + questionList + ", totalPage="
					+ totalPage + "]";
		}

		public class QuestionItem{
			//公司名称
			private String company;
			//同学头像
			private String avatar;
			//职位
			private String position;
			//问题内容
			private String content;
			//发布时间
			private String createTime;
			//问题id
			private String id;
			//同学姓名
			private String name;
			//同学的用户id
			private String userId;
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
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			
			public String getAvatar() {
				return avatar;
			}
			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}
			public String getPosition() {
				return position;
			}
			public void setPosition(String position) {
				this.position = position;
			}
			@Override
			public String toString() {
				return "QuestionItem [company=" + company + ", avatar="
						+ avatar + ", position=" + position + ", content="
						+ content + ", createTime=" + createTime + ", id=" + id
						+ ", name=" + name + ", userId=" + userId + "]";
			}
		}
	}
}
