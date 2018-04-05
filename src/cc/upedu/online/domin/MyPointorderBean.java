package cc.upedu.online.domin;

import java.util.ArrayList;

/**
 * 我的兑换记录的Javabean
 * @author Administrator
 *
 */
public class MyPointorderBean {
	private String message;
	private String success;
	private Entity entity;
	
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
		return "MyPointorderBean [message=" + message + ", success=" + success
				+ ", entity=" + entity + "]";
	}

	public class Entity{
		private ArrayList<OrderItem> orderList;
		//成长币余额
		private String pointbalance;
		//总页数
		private String totalPageSize;
		
		public ArrayList<OrderItem> getOrderList() {
			return orderList;
		}

		public void setOrderList(ArrayList<OrderItem> orderList) {
			this.orderList = orderList;
		}

		public String getPointbalance() {
			return pointbalance;
		}

		public void setPointbalance(String pointbalance) {
			this.pointbalance = pointbalance;
		}

		public String getTotalPageSize() {
			return totalPageSize;
		}

		public void setTotalPageSize(String totalPageSize) {
			this.totalPageSize = totalPageSize;
		}

		@Override
		public String toString() {
			return "Entity [orderList=" + orderList + ", pointbalance="
					+ pointbalance + ", totalPageSize=" + totalPageSize + "]";
		}

		public class OrderItem{
			//收货地址
			private String address;
			//订单创建时间
			private String createTime;
			//商品名称
			private String commodityName;
			//商品图片
			private String imageUrl;
			//订单id
			private String pointOrderId;
			//实付金额
			private String pointamount;
			//备注
			private String remarks;
			//订单状态
			private String status;
			public String getAddress() {
				return address;
			}
			public void setAddress(String address) {
				this.address = address;
			}
			public String getCreateTime() {
				return createTime;
			}
			public void setCreateTime(String createTime) {
				this.createTime = createTime;
			}
			public String getCommodityName() {
				return commodityName;
			}
			public void setCommodityName(String commodityName) {
				this.commodityName = commodityName;
			}
			public String getImageUrl() {
				return imageUrl;
			}
			public void setImageUrl(String imageUrl) {
				this.imageUrl = imageUrl;
			}
			public String getPointOrderId() {
				return pointOrderId;
			}
			public void setPointOrderId(String pointOrderId) {
				this.pointOrderId = pointOrderId;
			}
			public String getPointamount() {
				return pointamount;
			}
			public void setPointamount(String pointamount) {
				this.pointamount = pointamount;
			}
			public String getRemarks() {
				return remarks;
			}
			public void setRemarks(String remarks) {
				this.remarks = remarks;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			@Override
			public String toString() {
				return "OrderItem [address=" + address + ", createTime="
						+ createTime + ", commodityName=" + commodityName
						+ ", imageUrl=" + imageUrl + ", pointOrderId="
						+ pointOrderId + ", pointamount=" + pointamount
						+ ", remarks=" + remarks + ", status=" + status + "]";
			}
			
		};
	}
}
