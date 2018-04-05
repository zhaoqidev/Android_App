package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的索要笔记记录列表的javabean
 * @author Administrator
 *
 */
public class MyNoteRecordsBean {
	private String message;
	private String success;
	private Entity entity;
	
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
	
	@Override
	public String toString() {
		return "MyNoteRecordsBean [message=" + message + ", success=" + success
				+ ", entity=" + entity + "]";
	}

	public class Entity{
		private List<RecordItem> recordlist;

		public List<RecordItem> getRecordlist() {
			return recordlist;
		}

		public void setRecordlist(List<RecordItem> recordlist) {
			this.recordlist = recordlist;
		}

		@Override
		public String toString() {
			return "Entity [recordlist=" + recordlist + "]";
		}
		
	}
	public class RecordItem{
		/**
		 * 记录类型
		 * 1，我向他人索要，他人未回复 （可再次索要）
		 * 2，我向他人索要，他人已经分享给我（打赏）
		 * 3，他人向我索要，我已经分享，他人未打赏
		 * 4，他人向我索要，我已经分享，他人已打赏
		 * 5，他人向我索要，我未分享（分享笔记）
		 * 6，我分享给他人，他人未打赏
		 * 7，我分享给他人，他人已打赏
		 */
		private String type;
		/**
		 * 记录内容
		 */
		private String recordContent;
		/**
		 * 记录时间（这条记录更新的最新修改时间，精确到分）
		 */
		private String recordtime;
		/**
		 * 笔记记录的id
		 */
		private String recordId;
		/**
		 * 学友id
		 */
		private String tuid;
		/**
		 * 学友头像url
		 */
		private String avatar;
		/**
		 * 学友姓名
		 */
		private String uname;
		/**
		 * 课程id
		 */
		private String courseId;
		/**
		 * 课程名称
		 */
		private String courseName;
		/**
		 * 图片路径（课程）
		 */
		private String logo;
		public String gettype() {
			return type;
		}
		public void settype(String type) {
			this.type = type;
		}
		public String getRecordContent() {
			return recordContent;
		}
		public void setRecordContent(String recordContent) {
			this.recordContent = recordContent;
		}
		public String getRecordtime() {
			return recordtime;
		}
		public void setRecordtime(String recordtime) {
			this.recordtime = recordtime;
		}
		public String getTuid() {
			return tuid;
		}
		public void setTuid(String tuid) {
			this.tuid = tuid;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
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
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public String getRecordId() {
			return recordId;
		}
		public void setRecordId(String recordId) {
			this.recordId = recordId;
		}
		@Override
		public String toString() {
			return "RecordItem [type=" + type + ", recordContent="
					+ recordContent + ", recordtime=" + recordtime
					+ ", recordId=" + recordId + ", tuid=" + tuid + ", avatar="
					+ avatar + ", uname=" + uname + ", courseId=" + courseId
					+ ", courseName=" + courseName + ", logo=" + logo + "]";
		}
		
	};
}
