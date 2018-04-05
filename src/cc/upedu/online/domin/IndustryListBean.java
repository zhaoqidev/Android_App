package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class IndustryListBean implements Serializable{
	private List<IndustryItem> industryList;
	
	public List<IndustryItem> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<IndustryItem> industryList) {
		this.industryList = industryList;
	}

	@Override
	public String toString() {
		return "IndustryListBean [industryList=" + industryList + "]";
	}

	public class IndustryItem implements Serializable{
		private String content;
		private String id;
		private String type;
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
			return "IndustryItem [content=" + content + ", id=" + id
					+ ", type=" + type + "]";
		}
		
	}
}
