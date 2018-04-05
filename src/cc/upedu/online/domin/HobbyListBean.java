package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class HobbyListBean implements Serializable{
	private List<HobbyItem> hobbyList;
	
	public List<HobbyItem> getHobbyList() {
		return hobbyList;
	}

	public void setHobbyList(List<HobbyItem> hobbyList) {
		this.hobbyList = hobbyList;
	}

	@Override
	public String toString() {
		return "HobbyListBean [hobbyList=" + hobbyList + "]";
	}

	public class HobbyItem implements Serializable{
		private String applogo;
		private String content;
		private String id;
		private String type;
		public String getApplogo() {
			return applogo;
		}
		public void setApplogo(String applogo) {
			this.applogo = applogo;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		@Override
		public String toString() {
			return "HobbyItem [applogo=" + applogo + ", content=" + content
					+ ", id=" + id + ", type=" + type + "]";
		}
		
	}
}
