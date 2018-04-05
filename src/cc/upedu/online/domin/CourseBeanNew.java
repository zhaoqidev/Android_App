package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class CourseBeanNew implements Serializable{
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
		private List<CenterBannerItem> courseCenterBanner;
		private List<NoticeItem> noticeList;
		private List<SubjectItem> subjectList;
		private String courseTotalPage;
		
		@Override
		public String toString() {
			return "Entity [courseList=" + courseList + ", courseCenterBanner="
					+ courseCenterBanner + ", noticeList=" + noticeList
					+ ", subjectList=" + subjectList + ", courseTotalPage="
					+ courseTotalPage + "]";
		}
		public String getCourseTotalPage() {
			return courseTotalPage;
		}
		public void setCourseTotalPage(String courseTotalPage) {
			this.courseTotalPage = courseTotalPage;
		}
		public List<CourseItem> getCourseList() {
			return courseList;
		}
		public void setCourseList(List<CourseItem> courseList) {
			this.courseList = courseList;
		}
		public List<CenterBannerItem> getCourseCenterBanner() {
			return courseCenterBanner;
		}
		public void setCourseCenterBanner(List<CenterBannerItem> courseCenterBanner) {
			this.courseCenterBanner = courseCenterBanner;
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
