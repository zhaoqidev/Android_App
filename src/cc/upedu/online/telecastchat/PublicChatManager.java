package cc.upedu.online.telecastchat;

public class PublicChatManager extends ChatManager {
	private static PublicChatManager publicChatManager = null;

	public static PublicChatManager getIns() {
		synchronized (PublicChatManager.class) {
			if (null == publicChatManager) {
				publicChatManager = new PublicChatManager();
			}
		}
		return publicChatManager;
	}
}
