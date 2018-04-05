package cc.upedu.online.domin;

import java.util.List;

/**
 * 课程体系详情的javabean对象
 * @author Administrator
 *
 */
public class CourseShowArchitectureBean {
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
		private Subject subject;
		
		public Subject getSubject() {
			return subject;
		}

		public void setSubject(Subject subject) {
			this.subject = subject;
		}

		@Override
		public String toString() {
			return "Entity [subject=" + subject + "]";
		}

		public class Subject{
			private String appLogo;
			private List<String> childSubjectList;
			private String createTime;
			//体系介绍图片
			private String image;
			//体系简介
			private String introduct;
			private String isAppShow;
			private String level;
			//体系缘起
			private String origin;
			private String parentId;
			private String status;
			//体系结构
			private String structure;
			//体系ID
			private String subjectId;
			private String videoType;
			//体系名
			private String subjectName;
			private String updateTime;
			//介绍视频url
			private String vediourl;
			
			public String getVideoType() {
				return videoType;
			}
			public void setVideoType(String videoType) {
				this.videoType = videoType;
			}
			public String getAppLogo() {
				return appLogo;
			}
			public void setAppLogo(String appLogo) {
				this.appLogo = appLogo;
			}
			
			public List<String> getChildSubjectList() {
				return childSubjectList;
			}
			public void setChildSubjectList(List<String> childSubjectList) {
				this.childSubjectList = childSubjectList;
			}
			public String getCreateTime() {
				return createTime;
			}
			public void setCreateTime(String createTime) {
				this.createTime = createTime;
			}
			public String getImage() {
				return image;
			}
			public void setImage(String image) {
				this.image = image;
			}
			public String getIntroduct() {
				return introduct;
			}
			public void setIntroduct(String introduct) {
				this.introduct = introduct;
			}
			public String getIsAppShow() {
				return isAppShow;
			}
			public void setIsAppShow(String isAppShow) {
				this.isAppShow = isAppShow;
			}
			public String getLevel() {
				return level;
			}
			public void setLevel(String level) {
				this.level = level;
			}
			public String getOrigin() {
				return origin;
			}
			public void setOrigin(String origin) {
				this.origin = origin;
			}
			public String getParentId() {
				return parentId;
			}
			public void setParentId(String parentId) {
				this.parentId = parentId;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public String getStructure() {
				return structure;
			}
			public void setStructure(String structure) {
				this.structure = structure;
			}
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
			public String getUpdateTime() {
				return updateTime;
			}
			public void setUpdateTime(String updateTime) {
				this.updateTime = updateTime;
			}
			public String getVediourl() {
				return vediourl;
			}
			public void setVediourl(String vediourl) {
				this.vediourl = vediourl;
			}
			@Override
			public String toString() {
				return "Subject [appLogo=" + appLogo + ", childSubjectList="
						+ childSubjectList + ", createTime=" + createTime
						+ ", image=" + image + ", introduct=" + introduct
						+ ", isAppShow=" + isAppShow + ", level=" + level
						+ ", origin=" + origin + ", parentId=" + parentId
						+ ", status=" + status + ", structure=" + structure
						+ ", subjectId=" + subjectId + ", subjectName="
						+ subjectName + ", updateTime=" + updateTime
						+ ", vediourl=" + vediourl + "]";
			}
			
		}
	}
}
