package cc.upedu.online.domin;


public class UserInfoDetailBean {
	private String message;
	private String success;
	private Entity entity;
	
	
	@Override
	public String toString() {
		return "UserInfoDetailBean [message=" + message + ", success="
				+ success + ", entity=" + entity + "]";
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


	public class Entity{
		//用户名
		private String uname;
		//图片
		private String avatar;
		//职位
		private String position;
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		@Override
		public String toString() {
			return "Entity [uname=" + uname + ", avatar=" + avatar
					+ ", position=" + position + "]";
		}
		
		
	}
}
