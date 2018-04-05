package cc.upedu.online.domin;


public class UploadDataMessageBean {
	public String success;
	public String message;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "UploadDataMessageBean [success=" + success + ", message="
				+ message + "]";
	}
	
}
