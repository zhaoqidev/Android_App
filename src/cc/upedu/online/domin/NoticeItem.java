package cc.upedu.online.domin;

import java.io.Serializable;

public class NoticeItem implements Serializable{
	//公告id
	private String id;
	private String picture;
	//公告详情的url
	private String url;
	//公告的标题
	private String title;
	private String type;
	//公告发布的时间
	private String updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "NoticeItem [id=" + id + ", picture=" + picture + ", url="
				+ url + ", title=" + title + ", type=" + type
				+ ", updateTime=" + updateTime + "]";
	}
	
}
