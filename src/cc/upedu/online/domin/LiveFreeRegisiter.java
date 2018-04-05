package cc.upedu.online.domin;

import java.io.Serializable;


public class LiveFreeRegisiter implements Serializable{
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

	@Override
	public String toString() {
		return  "message=" + message
				+ ", success=" + success + "]";
	}
}
