package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的课程的javabean
 * @author Administrator
 *
 */
public class UserLearningRecordsBean {
	private String message;
	private String success;
	private Entity entity;
	
	@Override
	public String toString() {
		return "UserLearningRecordsBean [message=" + message + ", success="
				+ success + ", entity=" + entity + "]";
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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public class Entity{
		/**
		 * 学习记录集合
		 */
		private List<RecordsItem> studylist;
		/**
		 * 总页数
		 */
		private String totalPageSize;
		
		@Override
		public String toString() {
			return "Entity [studylist=" + studylist + ", totalPageSize="
					+ totalPageSize + "]";
		}

		public List<RecordsItem> getStudylist() {
			return studylist;
		}

		public void setStudylist(List<RecordsItem> studylist) {
			this.studylist = studylist;
		}

		public String getTotalPageSize() {
			return totalPageSize;
		}

		public void setTotalPageSize(String totalPageSize) {
			this.totalPageSize = totalPageSize;
		}
		/**
		 * 学习记录对象
		 * @author Administrator
		 *
		 */
		public class RecordsItem{
			/**
			 * 课程id
			 */
			private String courseId;
			/**
			 * 课程名称
			 */
			private String courseName;
			private String databak;
			/**
			 * 记录id
			 */
			private String id;
			/**
			 * 视频节点id
			 */
			private String kpointId;
			/**
			 * 视频节点名
			 */
			private String kpointName;
			/**
			 * 图片路径
			 */
			private String logo;
			/**
			 * 播放量
			 */
			private String playercount;
			/**
			 * 导师名
			 */
			private String teacherName;
			private String title;
			private String updateTime;
			/**
			 * 用户id
			 */
			private String userId;
			/**
			 * 是否已经收藏，true是，false否
			 */
			private String iscollected;
			private String videotype;
			private String videourl;
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
			public String getDatabak() {
				return databak;
			}
			public void setDatabak(String databak) {
				this.databak = databak;
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
			public String getKpointName() {
				return kpointName;
			}
			public void setKpointName(String kpointName) {
				this.kpointName = kpointName;
			}
			public String getLogo() {
				return logo;
			}
			public void setLogo(String logo) {
				this.logo = logo;
			}
			public String getPlayercount() {
				return playercount;
			}
			public void setPlayercount(String playercount) {
				this.playercount = playercount;
			}
			public String getTeacherName() {
				return teacherName;
			}
			public void setTeacherName(String teacherName) {
				this.teacherName = teacherName;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
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
			public String getVideoType() {
				return videotype;
			}
			public void setVideoType(String videoType) {
				this.videotype = videoType;
			}
			public String getVideourl() {
				return videourl;
			}
			public void setVideourl(String videourl) {
				this.videourl = videourl;
			}
			public String getIscollected() {
				return iscollected;
			}
			public void setIscollected(String iscollected) {
				this.iscollected = iscollected;
			}
			@Override
			public String toString() {
				return "RecordsItem [courseId=" + courseId + ", courseName="
						+ courseName + ", databak=" + databak + ", id=" + id
						+ ", kpointId=" + kpointId + ", kpointName="
						+ kpointName + ", logo=" + logo + ", playercount="
						+ playercount + ", teacherName=" + teacherName
						+ ", title=" + title + ", updateTime=" + updateTime
						+ ", userId=" + userId + ", iscollected=" + iscollected
						+ ", videoType=" + videotype + ", videourl=" + videourl
						+ "]";
			}
			
		}
	}
}
