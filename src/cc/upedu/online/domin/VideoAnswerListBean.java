package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


public class VideoAnswerListBean {
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
		private Notice notice;
		private List<VideoAnswerItem> qaVideoList;
		private String totalPage;
		
		@Override
		public String toString() {
			return "Entity [notice=" + notice + ", qaVideoList=" + qaVideoList
					+ ", totalPage=" + totalPage + "]";
		}
		public List<VideoAnswerItem> getQaVideoList() {
			return qaVideoList;
		}
		public void setQaVideoList(List<VideoAnswerItem> qaVideoList) {
			this.qaVideoList = qaVideoList;
		}
		public Notice getNotice() {
			return notice;
		}
		public void setNotice(Notice notice) {
			this.notice = notice;
		}
		public String getTotalPage() {
			return totalPage;
		}
		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}
		public class Notice{
			//公告id
			private String noticeId;
			//标题
			private String title;
			//直播id
			private String liveId;
			
			public String getNoticeId() {
				return noticeId;
			}
			
			public void setNoticeId(String noticeId) {
				this.noticeId = noticeId;
			}
			
			public String getTitle() {
				return title;
			}
			
			public void setTitle(String title) {
				this.title = title;
			}
			
			public String getLiveId() {
				return liveId;
			}
			
			public void setLiveId(String liveId) {
				this.liveId = liveId;
			}
			
			@Override
			public String toString() {
				return "Notice [noticeId=" + noticeId + ", title=" + title
						+ ", liveId=" + liveId + "]";
			}
		}
		public class VideoAnswerItem implements Serializable{
			//答疑内容
			private String content;
			//发布时间
			private String createTime;
			//答疑id
			private String id;
			//标题
			private String title;
			//导师id
			private String teacherId;
			//答疑视频url
			private String videourl;
			private String videotype;
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
			public String getVideourl() {
				return videourl;
			}
			public void setVideourl(String videourl) {
				this.videourl = videourl;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getVideoType() {
				return videotype;
			}
			public void setVideoType(String videoType) {
				this.videotype = videoType;
			}
			@Override
			public String toString() {
				return "VideoAnswerItem [content=" + content + ", createTime="
						+ createTime + ", id=" + id + ", title=" + title
						+ ", teacherId=" + teacherId + ", videourl=" + videourl
						+ ", videoType=" + videotype + "]";
			}
			
		}
	}
}
