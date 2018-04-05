package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class HomeBean implements Serializable{
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
		return "HomeBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class Entity implements Serializable{
		private List<CourseItem> courseList;
		private List<CenterBannerItem> indexCenterBanner;
		private List<NoticeItem> noticeList;
		private List<SubjectItem> subjectList;
		
		@Override
		public String toString() {
			return "Entity [courseList=" + courseList + ", indexCenterBanner="
					+ indexCenterBanner + ", noticeList=" + noticeList
					+ ", subjectList=" + subjectList + "]";
		}
		public List<CourseItem> getCourseList() {
			return courseList;
		}
		public void setCourseList(List<CourseItem> courseList) {
			this.courseList = courseList;
		}
		public List<CenterBannerItem> getIndexCenterBanner() {
			return indexCenterBanner;
		}
		public void setIndexCenterBanner(List<CenterBannerItem> indexCenterBanner) {
			this.indexCenterBanner = indexCenterBanner;
		}
		public List<NoticeItem> getNoticeList() {
			return noticeList;
		}
		public void setNoticeList(List<NoticeItem> noticeList) {
			this.noticeList = noticeList;
		}
		public List<SubjectItem> getSubjectList() {
			return subjectList;
		}
		public void setSubjectList(List<SubjectItem> subjectList) {
			this.subjectList = subjectList;
		}
		public class CourseItem implements Serializable{
			//课程ID
			private String courseId;
			//课程名
			private String courseName;
			private String id;
			//简介
			private String intro;
			//课程图片路径
			private String logo;
			private String orderNum;
			//推荐类型id  1:推荐课程 2：免费课程
			private String recommendId;
			private List<String> teacherList;
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
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getintro() {
				return intro;
			}
			public void setintro(String intro) {
				this.intro = intro;
			}
			public String getLogo() {
				return logo;
			}
			public void setLogo(String logo) {
				this.logo = logo;
			}
			public String getOrderNum() {
				return orderNum;
			}
			public void setOrderNum(String orderNum) {
				this.orderNum = orderNum;
			}
			public String getRecommendId() {
				return recommendId;
			}
			public void setRecommendId(String recommendId) {
				this.recommendId = recommendId;
			}
			public String getIntro() {
				return intro;
			}
			public void setIntro(String intro) {
				this.intro = intro;
			}
			public List<String> getTeacherList() {
				return teacherList;
			}
			public void setTeacherList(List<String> teacherList) {
				this.teacherList = teacherList;
			}
			@Override
			public String toString() {
				return "CourseItem [courseId=" + courseId + ", courseName="
						+ courseName + ", id=" + id + ", intro=" + intro
						+ ", logo=" + logo + ", orderNum=" + orderNum
						+ ", recommendId=" + recommendId + ", teacherList="
						+ teacherList + "]";
			}
			
		}
		public class CenterBannerItem implements Serializable{
			private String color;
			private String courseId;
			private String id;
			private String imagesUrl;
			private String keyWord;
			private String previewUrl;
			private String seriesNumber;
			private String title;
			public String getColor() {
				return color;
			}
			public void setColor(String color) {
				this.color = color;
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
			public String getImagesUrl() {
				return imagesUrl;
			}
			public void setImagesUrl(String imagesUrl) {
				this.imagesUrl = imagesUrl;
			}
			public String getKeyWord() {
				return keyWord;
			}
			public void setKeyWord(String keyWord) {
				this.keyWord = keyWord;
			}
			public String getPreviewUrl() {
				return previewUrl;
			}
			public void setPreviewUrl(String previewUrl) {
				this.previewUrl = previewUrl;
			}
			public String getSeriesNumber() {
				return seriesNumber;
			}
			public void setSeriesNumber(String seriesNumber) {
				this.seriesNumber = seriesNumber;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			@Override
			public String toString() {
				return "CourseItem [color=" + color + ", courseId=" + courseId
						+ ", id=" + id + ", imagesUrl=" + imagesUrl
						+ ", keyWord=" + keyWord + ", previewUrl=" + previewUrl
						+ ", seriesNumber=" + seriesNumber + ", title=" + title
						+ "]";
			}
		}
		
		public class SubjectItem implements Serializable{
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
				return "SubjectItem [subjectId=" + subjectId + ", subjectLogo="
						+ subjectLogo + ", subjectName=" + subjectName + "]";
			}
			
		}
	}
}
