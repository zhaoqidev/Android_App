package cc.upedu.online.domin;

import java.util.List;

public class NoticeBean {
	private List<NoticeItem> entity;
	private String message;
	private String success;
	
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

	public List<NoticeItem> getEntity() {
		return entity;
	}

	public void setEntity(List<NoticeItem> entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "NoticeBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

}
