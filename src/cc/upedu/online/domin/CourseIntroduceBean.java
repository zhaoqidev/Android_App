package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;


public class CourseIntroduceBean implements Serializable{
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
		//是否已收藏
		private String iscollected;
		//是否可以直接观看
		private String isok;
		//是否已经分享
		private String isshared;
		//是否免费，true是，false否
		private String isfree;
		private Course course;
		//分享用户id
		private String secretUid;

		public void setSecretUid(String secretUid) {
			this.secretUid = secretUid;
		}

		public String getSecretUid() {
			return secretUid;
		}

		public String getIscollected() {
			return iscollected;
		}

		public void setIscollected(String iscollected) {
			this.iscollected = iscollected;
		}

		public String getIsok() {
			return isok;
		}

		public void setIsok(String isok) {
			this.isok = isok;
		}

		public String getIsshared() {
			return isshared;
		}

		public void setIsshared(String isshared) {
			this.isshared = isshared;
		}

		public Course getCourse() {
			return course;
		}

		public void setCourse(Course course) {
			this.course = course;
		}

		@Override
		public String toString() {
			return "Entity [iscollected=" + iscollected + ", isok=" + isok
					+ ", isshared=" + isshared + ", course=" + course + "]";
		}

		public String getIsfree() {
			return isfree;
		}

		public void setIsfree(String isfree) {
			this.isfree = isfree;
		}

		/**
		 * 课程对象
		 * @author Administrator
		 *
		 */
		public class Course implements Serializable{
			//课程详情介绍
			private String context;
			//课程ID
			private String courseId;
			//课程图片
			private String courseLogo;
			//课程名
			private String name;
			//直播课程时课程的url
			private String freeurl;
			private String videotype;
			//时长
			private String lessontimes;
			//课程老师List
			private List<TeacherItem> teacherList;
			//课程价值
			private String value;
			private String liveurl;
			//直播开始时间
			private String liveBeginTime;
			//直播结束时间
			private String liveEndTime;
			
			public String getFreeurl() {
				return freeurl;
			}

			public void setFreeurl(String freeurl) {
				this.freeurl = freeurl;
			}

			public String getLessontimes() {
				return lessontimes;
			}

			public void setLessontimes(String lessontimes) {
				this.lessontimes = lessontimes;
			}

			public String getContext() {
				return context;
			}

			public void setContext(String context) {
				this.context = context;
			}

			public String getCourseId() {
				return courseId;
			}

			public void setCourseId(String courseId) {
				this.courseId = courseId;
			}

			public String getCourseLogo() {
				return courseLogo;
			}

			public void setCourseLogo(String courseLogo) {
				this.courseLogo = courseLogo;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public List<TeacherItem> getTeacherList() {
				return teacherList;
			}

			public void setTeacherList(List<TeacherItem> teacherList) {
				this.teacherList = teacherList;
			}

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}

			public String getLiveurl() {
				return liveurl;
			}

			public void setLiveurl(String liveurl) {
				this.liveurl = liveurl;
			}
			
			public String getLiveBeginTime() {
				return liveBeginTime;
			}
			public void setLiveBeginTime(String liveBeginTime) {
				this.liveBeginTime = liveBeginTime;
			}
			
			public String getLiveEndTime() {
				return liveEndTime;
			}
			public void setLiveEndTime(String liveEndTime) {
				this.liveEndTime = liveEndTime;
			}

			public String getVideotype() {
				return videotype;
			}

			public void setVideotype(String videotype) {
				this.videotype = videotype;
			}

			@Override
			public String toString() {
				return "Course [context=" + context + ", courseId=" + courseId
						+ ", courseLogo=" + courseLogo + ", name=" + name
						+ ", freeurl=" + freeurl + ", videoType=" + videotype
						+ ", lessontimes=" + lessontimes + ", teacherList="
						+ teacherList + ", value=" + value + ", liveurl="
						+ liveurl + ", liveBeginTime=" + liveBeginTime
						+ ", liveEndTime=" + liveEndTime + "]";
			}

			public class TeacherItem implements Serializable{
				//导师介绍
				private String career;
				private String createTime;
				//教育介绍
				private String education;
				private String id;
				private String isStar;
				//导师名称
				private String name;
				private String picArray;
				//导师头像
				private String picPath;
				private String status;
				//介绍视频url
				private String vidioUrl;
				public String getCareer() {
					return career;
				}
				public void setCareer(String career) {
					this.career = career;
				}
				public String getCreateTime() {
					return createTime;
				}
				public void setCreateTime(String createTime) {
					this.createTime = createTime;
				}
				public String getEducation() {
					return education;
				}
				public void setEducation(String education) {
					this.education = education;
				}
				public String getId() {
					return id;
				}
				public void setId(String id) {
					this.id = id;
				}
				public String getIsStar() {
					return isStar;
				}
				public void setIsStar(String isStar) {
					this.isStar = isStar;
				}
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
				public String getPicArray() {
					return picArray;
				}
				public void setPicArray(String picArray) {
					this.picArray = picArray;
				}
				public String getPicPath() {
					return picPath;
				}
				public void setPicPath(String picPath) {
					this.picPath = picPath;
				}
				public String getStatus() {
					return status;
				}
				public void setStatus(String status) {
					this.status = status;
				}
				public String getVidioUrl() {
					return vidioUrl;
				}
				public void setVidioUrl(String vidioUrl) {
					this.vidioUrl = vidioUrl;
				}
				@Override
				public String toString() {
					return "TeacherItem [career=" + career + ", createTime="
							+ createTime + ", education=" + education + ", id="
							+ id + ", isStar=" + isStar + ", name=" + name
							+ ", picArray=" + picArray + ", picPath=" + picPath
							+ ", status=" + status + ", vidioUrl=" + vidioUrl
							+ "]";
				}
			}
		}
	}
}
