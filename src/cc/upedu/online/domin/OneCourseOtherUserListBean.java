package cc.upedu.online.domin;

import java.util.List;

/**
 * 我的布局课程列表的javabean
 * @author Administrator
 *
 */
public class OneCourseOtherUserListBean {
	private String message;
	private String success;
	private Entity entity;
	
	@Override
	public String toString() {
		return "MyNoteCourseBean [message=" + message + ", success=" + success
				+ ", entity=" + entity + "]";
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
		private List<StudentItem> studentList;

		public List<StudentItem> getStudentList() {
			return studentList;
		}

		public void setStudentlist(List<StudentItem> studentList) {
			this.studentList = studentList;
		}

		@Override
		public String toString() {
			return "Entity [studentList=" + studentList + "]";
		}
		
	};
	public class StudentItem{
		//学友id
		private String uid;
		//学友头像url
		private String avatar;
		//学友姓名
		private String uname;
		//笔记列表notelist
		private List<NoteItem> noteList;
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
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
		public List<NoteItem> getNoteList() {
			return noteList;
		}
		public void setNotelist(List<NoteItem> noteList) {
			this.noteList = noteList;
		}
		@Override
		public String toString() {
			return "StudentItem [uid=" + uid + ", avatar=" + avatar
					+ ", uname=" + uname + ", noteList=" + noteList + "]";
		}
		
	};
}
