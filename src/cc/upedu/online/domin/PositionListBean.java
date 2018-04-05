package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class PositionListBean implements Serializable{
	private List<PositionItem> positionList;

	public List<PositionItem> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<PositionItem> positionList) {
		this.positionList = positionList;
	}

	@Override
	public String toString() {
		return "PositionListBean [positionList=" + positionList + "]";
	}

	public class PositionItem implements Serializable{
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
