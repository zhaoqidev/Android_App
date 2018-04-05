package cc.upedu.online.domin;

import java.util.List;

public class TelecastBean {
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
		private List<Live> liveList;
		private String totalPage;

		public String getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(String totalPage) {
			this.totalPage = totalPage;
		}

		public List<Live> getLiveList() {
			return liveList;
		}

		public void setLiveList(List<Live> liveList) {
			this.liveList = liveList;
		}

		@Override
		public String toString() {
			return "Entity [liveList=" + liveList + ", totalPage=" + totalPage
					+ "]";
		}

		public class Live{
			//课程id
			private String courseId;
			//报名次数
			private String joinNum;
			//观看次数
			private String viewNum;
			//直播图片路径
			private String logo;
			//开始时间
			private String startTime;
			//结束时间
			private String endTime;
			private String intro;
			private String picPath;
			//导师id
			private String teacherId;
			//导师名称
			private String teacherName;
			//直播标题
			private String title;
			//赞 次数
			private String likeNum;
			public String getCourseId() {
				return courseId;
			}
			public void setCourseId(String courseId) {
				this.courseId = courseId;
			}
			public String getJoinNum() {
				return joinNum;
			}
			public void setJoinNum(String joinNum) {
				this.joinNum = joinNum;
			}
			public String getLogo() {
				return logo;
			}
			public void setLogo(String logo) {
				this.logo = logo;
			}
			public String getStartTime() {
				return startTime;
			}
			public void setStartTime(String startTime) {
				this.startTime = startTime;
			}
			public String getTeacherId() {
				return teacherId;
			}
			public void setTeacherId(String teacherId) {
				this.teacherId = teacherId;
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
			
			public String getLikeNum() {
				return likeNum;
			}
			public void setLikeNum(String likeNum) {
				this.likeNum = likeNum;
			}
			public String getViewNum() {
				return viewNum;
			}
			public void setViewNum(String viewNum) {
				this.viewNum = viewNum;
			}
			public String getEndTime() {
				return endTime;
			}
			public void setEndTime(String endTime) {
				this.endTime = endTime;
			}
			public String getIntro() {
				return intro;
			}
			public void setIntro(String intro) {
				this.intro = intro;
			}
			public String getPicPath() {
				return picPath;
			}
			public void setPicPath(String picPath) {
				this.picPath = picPath;
			}
			@Override
			public String toString() {
				return "Live [courseId=" + courseId + ", joinNum=" + joinNum
						+ ", viewNum=" + viewNum + ", logo=" + logo
						+ ", startTime=" + startTime + ", endTime=" + endTime
						+ ", intro=" + intro + ", picPath=" + picPath
						+ ", teacherId=" + teacherId + ", teacherName="
						+ teacherName + ", title=" + title + ", likeNum="
						+ likeNum + "]";
			}
			
		}
	}
}
