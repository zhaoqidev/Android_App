package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class CourseItem implements Serializable{
	private String courseId;
	private String logo;
	private String name;
	private List<String> subjectList;
	private List<String> teacherList;
	private String title;
	private String viewCount;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<String> subjectList) {
		this.subjectList = subjectList;
	}
	public List<String> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<String> teacherList) {
		this.teacherList = teacherList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getViewCount() {
		return viewCount;
	}
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	@Override
	public String toString() {
		return "CourseItem [courseId=" + courseId + ", logo=" + logo
				+ ", name=" + name + ", subjectList=" + subjectList
				+ ", teacherList=" + teacherList + ", title=" + title
				+ ", viewCount=" + viewCount + "]";
	}
}
