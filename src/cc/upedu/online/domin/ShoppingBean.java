package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class ShoppingBean {
	private Entity entity;
	private String message;
	private String success;
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
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

	@Override
	public String toString() {
		return "CourseBean [entity=" + entity + ", message=" + message
				+ ", success=" + success + "]";
	}

	public class Entity implements Serializable{
		private List<ShoppingItem> shopcartList;
		//成长币总额
		private String totalGcoin;

		public List<ShoppingItem> getShopcartList() {
			return shopcartList;
		}


		public void setShopcartList(List<ShoppingItem> shopcartList) {
			this.shopcartList = shopcartList;
		}


		public String getTotalGcoin() {
			return totalGcoin;
		}


		public void setTotalGcoin(String totalGcoin) {
			this.totalGcoin = totalGcoin;
		}

		@Override
		public String toString() {
			return "Entity [shopcartList=" + shopcartList + ", totalGcoin="
					+ totalGcoin + "]";
		}

		public class ShoppingItem implements Serializable{
			//课程id
			private String goodsid;
			//购物车商品id
			private String itemId;
			//商品图片
			private String pic;
			//商品价格
			private String price;
			//商品名称
			private String title;
			private String userId;
			private String courseType;
			public String getGoodsid() {
				return goodsid;
			}
			public void setGoodsid(String goodsid) {
				this.goodsid = goodsid;
			}
			public String getItemId() {
				return itemId;
			}
			public void setItemId(String itemId) {
				this.itemId = itemId;
			}
			public String getPic() {
				return pic;
			}
			public void setPic(String pic) {
				this.pic = pic;
			}
			public String getPrice() {
				return price;
			}
			public void setPrice(String price) {
				this.price = price;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
			public String getCourseType() {
				return courseType;
			}
			public void setCourseType(String courseType) {
				this.courseType = courseType;
			}
			@Override
			public String toString() {
				return "ShoppingItem [goodsid=" + goodsid + ", itemId="
						+ itemId + ", pic=" + pic + ", price=" + price
						+ ", title=" + title + ", userId=" + userId
						+ ", courseType=" + courseType + "]";
			}
			
		}
	}
}
