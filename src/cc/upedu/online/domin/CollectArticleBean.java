package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

/**
 * 导师名片页面的导师文章条目
 * @author Administrator
 */
public class CollectArticleBean {
	public String message;
	public String success;
	public Entity entity;
	
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

	@Override
	public String toString() {
		return "CollectArticleBean [message=" + message + ", success="
				+ success + ", entity=" + entity + "]";
	}

	public class Entity{
		public List<ArticleItem> articleList;
		public String totalPageSize;
		public List<ArticleItem> getArticleList() {
			return articleList;
		}
		public void setArticleList(List<ArticleItem> articleList) {
			this.articleList = articleList;
		}
		public String getTotalPageSize() {
			return totalPageSize;
		}
		public void setTotalPageSize(String totalPageSize) {
			this.totalPageSize = totalPageSize;
		}
		@Override
		public String toString() {
			return "Entity [articleList=" + articleList + ", totalPageSize="
					+ totalPageSize + "]";
		}
		
	}
	
	public class ArticleItem implements Serializable{
		public String articleId; 
		public String createTime; 
		public String favouriteId; 
		public String intro; 
		public String picPath; 
		public String title;
		public String clickTimes;//浏览次数
		
		public String getClickTimes() {
			return clickTimes;
		}
		public void setClickTimes(String clickTimes) {
			this.clickTimes = clickTimes;
		}
		public String getArticleId() {
			return articleId;
		}
		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getFavouriteId() {
			return favouriteId;
		}
		public void setFavouriteId(String favouriteId) {
			this.favouriteId = favouriteId;
		}
		public String getIntro() {
			return intro;
		}
		public void setIntro(String intro) {
			this.intro = intro;
		}
		public String getPicPath() {
			return picPath;
		}
		public void setPicPath(String picPath) {
			this.picPath = picPath;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		@Override
		public String toString() {
			return "ArticleItem [articleId=" + articleId + ", createTime="
					+ createTime + ", favouriteId=" + favouriteId + ", intro="
					+ intro + ", picPath=" + picPath + ", title=" + title + "]";
		} 
		
	}
	
}
