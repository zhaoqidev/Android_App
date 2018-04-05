package cc.upedu.online.domin;

import java.util.List;

public class CourseSectionListBean {
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
		private List<List<CatalogItem>> catalogList;
		
		public List<List<CatalogItem>> getCatalogList() {
			return catalogList;
		}

		public void setCatalogList(List<List<CatalogItem>> catalogList) {
			this.catalogList = catalogList;
		}

		@Override
		public String toString() {
			return "Entity [catalogList=" + catalogList + "]";
		}

		public class CatalogItem{
			private String courseName;
			private List<ChildListItem> childList;
			private String kpointId;
			private String videoName;
			private String videoType;
			private String videoUrl;
			private String srturl;

			public String getCourseName() {
				return courseName;
			}

			public void setCourseName(String courseName) {
				this.courseName = courseName;
			}

			public List<ChildListItem> getChildList() {
				return childList;
			}

			public void setChildList(List<ChildListItem> childList) {
				this.childList = childList;
			}

			public String getKpointId() {
				return kpointId;
			}

			public void setKpointId(String kpointId) {
				this.kpointId = kpointId;
			}

			public String getVideoName() {
				return videoName;
			}

			public void setVideoName(String videoName) {
				this.videoName = videoName;
			}

			public String getVideoType() {
				return videoType;
			}

			public void setVideoType(String videoType) {
				this.videoType = videoType;
			}

			public String getVideoUrl() {
				return videoUrl;
			}

			public void setVideoUrl(String videoUrl) {
				this.videoUrl = videoUrl;
			}

			public void setSrturl(String srturl) {
				this.srturl = srturl;
			}
			public String getSrturl() {
				return srturl;
			}

			@Override
			public String toString() {
				return "CatalogItem [courseName=" + courseName + ", childList="
						+ childList + ", kpointId=" + kpointId + ", videoName="
						+ videoName + ", videoType=" + videoType
						+ ", videoUrl=" + videoUrl +", srturl=" + srturl + "]";
			}

			public class ChildListItem{
				private String courseId;
				private String isfree;
				private String videoId;
				private String videoName;
				private String videoType;
				private String videoUrl;
				private String vidioLength;
				private String srturl;
				public String getCourseId() {
					return courseId;
				}
				public void setCourseId(String courseId) {
					this.courseId = courseId;
				}
				public String getIsfree() {
					return isfree;
				}
				public void setIsfree(String isfree) {
					this.isfree = isfree;
				}
				public String getVideoId() {
					return videoId;
				}
				public void setVideoId(String videoId) {
					this.videoId = videoId;
				}
				public String getVideoName() {
					return videoName;
				}
				public void setVideoName(String videoName) {
					this.videoName = videoName;
				}
				public String getVideoType() {
					return videoType;
				}
				public void setVideoType(String videoType) {
					this.videoType = videoType;
				}
				public String getVideoUrl() {
					return videoUrl;
				}
				public void setVideoUrl(String videoUrl) {
					this.videoUrl = videoUrl;
				}
				public String getVidioLength() {
					return vidioLength;
				}
				public void setVidioLength(String vidioLength) {
					this.vidioLength = vidioLength;
				}
				public void setSrturl(String srturl) {
					this.srturl = srturl;
				}
				public String getSrturl() {
					return srturl;
				}
				@Override
				public String toString() {
					return "ChildListItem [courseId=" + courseId + ", isfree="
							+ isfree + ", videoId=" + videoId + ", videoName="
							+ videoName + ", videoType=" + videoType
							+ ", videoUrl=" + videoUrl + ", vidioLength="
							+ vidioLength +", srturl=" + srturl + "]";
				}
				
			}
		}
	}
}
