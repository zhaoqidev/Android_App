package cc.upedu.online.domin;

import java.io.Serializable;

public class NoteItem implements Serializable{
	//笔记ID
	private String noteId;
	//节点名称（可为空 ，直播课程）
	private String kpointName;
	//节点id，预留，用于课程跳转（可为空 ，直播课程）
	private String kpointId;
	//笔记内容
	private String courseContent;
	//笔记最后修改时间
	private String modifyTime;
	//学友id
	private String uid;
	//学友头像url
	private String avatar;
	//学友姓名
	private String uname;
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getKpointName() {
		return kpointName;
	}
	public void setKpointName(String kpointName) {
		this.kpointName = kpointName;
	}
	public String getKpointId() {
		return kpointId;
	}
	public void setKpointId(String kpointId) {
		this.kpointId = kpointId;
	}
	public String getCourseContent() {
		return courseContent;
	}
	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
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
	@Override
	public String toString() {
		return "NoteItem [noteId=" + noteId + ", kpointName=" + kpointName
				+ ", kpointId=" + kpointId + ", courseContent="
				+ courseContent + ", modifyTime=" + modifyTime + ", uid="
				+ uid + ", avatar=" + avatar + ", uname=" + uname + "]";
	}
	
}
