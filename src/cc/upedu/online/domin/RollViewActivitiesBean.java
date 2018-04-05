package cc.upedu.online.domin;

import java.util.List;
/**
 * 活动页面轮播图的实体类
 * @author Administrator
 *
 */
public class RollViewActivitiesBean {
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
		return "RecmdCourseBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class Entity{
		private List<CourseItem> activityCenterBanner;
		
		public List<CourseItem> getactivityCenterBanner() {
			return activityCenterBanner;
		}

		public void setactivityCenterBanner(List<CourseItem> activityCenterBanner) {
			this.activityCenterBanner = activityCenterBanner;
		}

		@Override
		public String toString() {
			return "Entity [activityCenterBanner=" + activityCenterBanner + "]";
		}

		public class CourseItem{
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
	}
}
