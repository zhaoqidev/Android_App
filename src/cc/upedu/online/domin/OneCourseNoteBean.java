package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

/**
 * 我的布局课程列表的javabean
 * @author Administrator
 *
 */
public class OneCourseNoteBean {
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

	public class Entity implements Serializable{
		private List<NoteItem> noteList;

		public List<NoteItem> getNoteList() {
			return noteList;
		}

		public void setNotelist(List<NoteItem> notelist) {
			this.noteList = notelist;
		}

		@Override
		public String toString() {
			return "Entity [noteList=" + noteList + "]";
		}
		
	};
	
}
