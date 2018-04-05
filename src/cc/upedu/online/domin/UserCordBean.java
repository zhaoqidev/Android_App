package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

/**
 * 用户名片的javabean对象
 * @author Administrator
 *
 */
public class UserCordBean implements Serializable{
	private String success;
	private String message;
	private Entity entity;
	
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

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "UserCordBean [success=" + success + ", message=" + message
				+ ", entity=" + entity + "]";
	}
	
	public class Entity implements Serializable{
		private UserInfo userInfo;
		//0：已关注 1：互相关注2：未关注
		private String isFriend;
		public String getIsFriend() {
			return isFriend;
		}
		public void setIsFriend(String isFriend) {
			this.isFriend = isFriend;
		}
		public UserInfo getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(UserInfo userInfo) {
			this.userInfo = userInfo;
		}

		@Override
		public String toString() {
			return "Entity [userInfo=" + userInfo + ", isFriend=" + isFriend + "]";
		}

		public class UserInfo implements Serializable{
			//头像
			private String avatar;
			//阳历生日
			private String birthday;
			//阴历生日
			private String birthdayZodiac;
			//所在城市
			private String city;
			//企业列表
			private List<CompanyItem> companyList;
			//星座
			private String constellation;
			//梦想列表
			private List<String> dreamList;
			//邮箱
			private String email;
			//性别（1男 2女）
			private String gender;
			//爱好编码（多个以，分隔）
			private String hobby;
			//爱好文本
			private String hobbyText;
			//家乡
			private String hometown;
			//名片签名
			private String intro;
			private String isbirthdayopen;
			//0:全公开 1:只对好友公开
			private String isemailopen;
			//0:全公开 1:只对好友公开
			private String ismobileopen;
			private String isqqopen;
			private String isweixinopen;
			//婚否（0：未婚 1：已婚）
			private String married;
			//手机号
			private String mobile;
			//姓名
			private String name;
			//照片列表
			private List<PicItem> picList;
			private String qq;
			//经常往来城市（多个以，分隔）
			private String travelCity;
			private String travelCityText;
			//价值观与信念列表
			private List<String> valueList;
			private String weixin;
			//属相
			private String zodiac;
			//背景图片路径
			private String bannerUrl;
			//0:普通用户 1:导师 2:代理商
			private String userType;
			
			@Override
			public String toString() {
				return "UserInfo [avatar=" + avatar + ", birthday=" + birthday
						+ ", birthdayZodiac=" + birthdayZodiac + ", city="
						+ city + ", companyList=" + companyList
						+ ", constellation=" + constellation + ", dreamList="
						+ dreamList + ", email=" + email + ", gender=" + gender
						+ ", hobby=" + hobby + ", hobbyText=" + hobbyText
						+ ", hometown=" + hometown + ", intro=" + intro
						+ ", isbirthdayopen=" + isbirthdayopen
						+ ", isemailopen=" + isemailopen + ", ismobileopen="
						+ ismobileopen + ", isqqopen=" + isqqopen
						+ ", isweixinopen=" + isweixinopen + ", married="
						+ married + ", mobile=" + mobile + ", name=" + name
						+ ", picList=" + picList + ", qq=" + qq
						+ ", travelCity=" + travelCity + ", travelCityText="
						+ travelCityText + ", valueList=" + valueList
						+ ", weixin=" + weixin + ", zodiac=" + zodiac
						+ ", bannerUrl=" + bannerUrl
						+ ", userType=" + userType + "]";
			}
			
			public String getUserType() {
				return userType;
			}
			public void setUserType(String userType) {
				this.userType = userType;
			}
			public String getTravelCityText() {
				return travelCityText;
			}
			public void setTravelCityText(String travelCityText) {
				this.travelCityText = travelCityText;
			}
			public String getHobbyText() {
				return hobbyText;
			}
			public void setHobbyText(String hobbyText) {
				this.hobbyText = hobbyText;
			}
			public String getBannerUrl() {
				return bannerUrl;
			}
			public void setBannerUrl(String bannerUrl) {
				this.bannerUrl = bannerUrl;
			}
			public String getAvatar() {
				return avatar;
			}
			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}
			public String getBirthday() {
				return birthday;
			}
			public void setBirthday(String birthday) {
				this.birthday = birthday;
			}
			public String getBirthdayZodiac() {
				return birthdayZodiac;
			}
			public void setBirthdayZodiac(String birthdayZodiac) {
				this.birthdayZodiac = birthdayZodiac;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public List<CompanyItem> getCompanyList() {
				return companyList;
			}
			public void setCompanyList(List<CompanyItem> companyList) {
				this.companyList = companyList;
			}
			public String getConstellation() {
				return constellation;
			}
			public void setConstellation(String constellation) {
				this.constellation = constellation;
			}
			public List<String> getDreamList() {
				return dreamList;
			}
			public void setDreamList(List<String> dreamList) {
				this.dreamList = dreamList;
			}
			public String getEmail() {
				return email;
			}
			public void setEmail(String email) {
				this.email = email;
			}
			public String getGender() {
				return gender;
			}
			public void setGender(String gender) {
				this.gender = gender;
			}
			public String getHobby() {
				return hobby;
			}
			public void setHobby(String hobby) {
				this.hobby = hobby;
			}
			public String getHometown() {
				return hometown;
			}
			public void setHometown(String hometown) {
				this.hometown = hometown;
			}
			public String getIntro() {
				return intro;
			}
			public void setIntro(String intro) {
				this.intro = intro;
			}
			public String getIsbirthdayopen() {
				return isbirthdayopen;
			}
			public void setIsbirthdayopen(String isbirthdayopen) {
				this.isbirthdayopen = isbirthdayopen;
			}
			public String getIsemailopen() {
				return isemailopen;
			}
			public void setIsemailopen(String isemailopen) {
				this.isemailopen = isemailopen;
			}
			public String getIsmobileopen() {
				return ismobileopen;
			}
			public void setIsmobileopen(String ismobileopen) {
				this.ismobileopen = ismobileopen;
			}
			public String getIsqqopen() {
				return isqqopen;
			}
			public void setIsqqopen(String isqqopen) {
				this.isqqopen = isqqopen;
			}
			public String getIsweixinopen() {
				return isweixinopen;
			}
			public void setIsweixinopen(String isweixinopen) {
				this.isweixinopen = isweixinopen;
			}
			public String getMarried() {
				return married;
			}
			public void setMarried(String married) {
				this.married = married;
			}
			public String getMobile() {
				return mobile;
			}
			public void setMobile(String mobile) {
				this.mobile = mobile;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public List<PicItem> getPicList() {
				return picList;
			}
			public void setPicList(List<PicItem> picList) {
				this.picList = picList;
			}
			public String getQq() {
				return qq;
			}
			public void setQq(String qq) {
				this.qq = qq;
			}
			public String getTravelCity() {
				return travelCity;
			}
			public void setTravelCity(String travelCity) {
				this.travelCity = travelCity;
			}
			public List<String> getValueList() {
				return valueList;
			}
			public void setValueList(List<String> valueList) {
				this.valueList = valueList;
			}
			public String getWeixin() {
				return weixin;
			}
			public void setWeixin(String weixin) {
				this.weixin = weixin;
			}
			public String getZodiac() {
				return zodiac;
			}
			public void setZodiac(String zodiac) {
				this.zodiac = zodiac;
			}
			public class CompanyItem implements Serializable{
				//添加时间
				private String addtime;
				//所在地
				private String city;
				private String cityName;
				//公司id
				private String id;
				//行业
				private String industry;
				private String industryName;
				private String isnameopen;
				//企业网站是否公开
				private String iswebsiteopen;
				//企业名称
				private String name;
				//职务
				private String position;
				private String positionName;
				//产品列表
				private List<ProductItem> productList;
				private String showProductName;
				private List<ProductItem> alterProductList;
				private List<ProductItem> addProductList;
				private List<String> delProductIdList;
				private String userId;
				//企业网站
				private String website;
				
				public String getShowProductName() {
					return showProductName;
				}

				public void setShowProductName(String showProductName) {
					this.showProductName = showProductName;
				}

				@Override
				public String toString() {
					return "CompanyItem [addtime=" + addtime + ", city=" + city
							+ ", cityName=" + cityName + ", id=" + id
							+ ", industry=" + industry + ", industryName="
							+ industryName + ", isnameopen=" + isnameopen
							+ ", iswebsiteopen=" + iswebsiteopen + ", name="
							+ name + ", position=" + position
							+ ", positionName=" + positionName
							+ ", productList=" + productList
							+ ", alterProductList=" + alterProductList
							+ ", addProductList=" + addProductList
							+ ", delProductIdList=" + delProductIdList
							+ ", userId=" + userId + ", website=" + website
							+ "]";
				}

				public List<String> getDelProductIdList() {
					return delProductIdList;
				}

				public void setDelProductIdList(List<String> delProductIdList) {
					this.delProductIdList = delProductIdList;
				}

				public List<ProductItem> getAlterProductList() {
					return alterProductList;
				}

				public void setAlterProductList(List<ProductItem> alterProductList) {
					this.alterProductList = alterProductList;
				}

				public List<ProductItem> getAddProductList() {
					return addProductList;
				}

				public void setAddProductList(List<ProductItem> addProductList) {
					this.addProductList = addProductList;
				}

				public String getAddtime() {
					return addtime;
				}

				public void setAddtime(String addtime) {
					this.addtime = addtime;
				}

				public String getCity() {
					return city;
				}

				public void setCity(String city) {
					this.city = city;
				}

				public String getCityName() {
					return cityName;
				}

				public void setCityName(String cityName) {
					this.cityName = cityName;
				}

				public String getId() {
					return id;
				}

				public void setId(String id) {
					this.id = id;
				}

				public String getIndustry() {
					return industry;
				}

				public void setIndustry(String industry) {
					this.industry = industry;
				}

				public String getIndustryName() {
					return industryName;
				}

				public void setIndustryName(String industryName) {
					this.industryName = industryName;
				}

				public String getIsnameopen() {
					return isnameopen;
				}

				public void setIsnameopen(String isnameopen) {
					this.isnameopen = isnameopen;
				}

				public String getIswebsiteopen() {
					return iswebsiteopen;
				}

				public void setIswebsiteopen(String iswebsiteopen) {
					this.iswebsiteopen = iswebsiteopen;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getPosition() {
					return position;
				}

				public void setPosition(String position) {
					this.position = position;
				}

				public String getPositionName() {
					return positionName;
				}

				public void setPositionName(String positionName) {
					this.positionName = positionName;
				}

				public List<ProductItem> getProductList() {
					return productList;
				}

				public void setProductList(List<ProductItem> productList) {
					this.productList = productList;
				}

				public String getUserId() {
					return userId;
				}

				public void setUserId(String userId) {
					this.userId = userId;
				}

				public String getWebsite() {
					return website;
				}

				public void setWebsite(String website) {
					this.website = website;
				}

				public class ProductItem implements Serializable{
					private String addtime;
					//公司id
					private String companyId;
					//适合客户
					private String customer;
					//产品描述
					private String desc;
					//产品id
					private String id;
					//产品照片
					private List<PicItem> picList;
					//产品名称
					private String title;
					private String userId;
					//产品价值
					private String value;
					//用于回传数据的集合
					private List<String> picAddArray;
					private List<String> picDelArray;
					
					
					public List<String> getPicAddArray() {
						return picAddArray;
					}
					public void setPicAddArray(List<String> picAddArray) {
						this.picAddArray = picAddArray;
					}
					public List<String> getPicDelArray() {
						return picDelArray;
					}
					public void setPicDelArray(List<String> picDelArray) {
						this.picDelArray = picDelArray;
					}
					public String getAddtime() {
						return addtime;
					}
					public void setAddtime(String addtime) {
						this.addtime = addtime;
					}
					public String getCompanyId() {
						return companyId;
					}
					public void setCompanyId(String companyId) {
						this.companyId = companyId;
					}
					public String getCustomer() {
						return customer;
					}
					public void setCustomer(String customer) {
						this.customer = customer;
					}
					public String getDesc() {
						return desc;
					}
					public void setDesc(String desc) {
						this.desc = desc;
					}
					public String getId() {
						return id;
					}
					public void setId(String id) {
						this.id = id;
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
					public String getValue() {
						return value;
					}
					public void setValue(String value) {
						this.value = value;
					}
					public List<PicItem> getPicList() {
						return picList;
					}
					public void setPicList(List<PicItem> picList) {
						this.picList = picList;
					}
					@Override
					public String toString() {
						return "ProductItem [addtime=" + addtime
								+ ", companyId=" + companyId + ", customer="
								+ customer + ", desc=" + desc + ", id=" + id
								+ ", picList=" + picList + ", title=" + title
								+ ", userId=" + userId + ", value=" + value
								+ ", picAddArray=" + picAddArray
								+ ", picDelArray=" + picDelArray + "]";
					}
					
				}
			}
			public class PicItem implements Serializable{
				//图片id
				private String id;
				//图片路径
				private String picPath;
				//产品id
				private String pid;
				//版本号
				private String version;
				public String getId() {
					return id;
				}
				public void setId(String id) {
					this.id = id;
				}
				public String getPicPath() {
					return picPath;
				}
				public void setPicPath(String picPath) {
					this.picPath = picPath;
				}
				public String getPid() {
					return pid;
				}
				public void setPid(String pid) {
					this.pid = pid;
				}
				public String getVersion() {
					return version;
				}
				public void setVersion(String version) {
					this.version = version;
				}
				@Override
				public String toString() {
					return "PicItem [id=" + id + ", picPath=" + picPath
							+ ", pid=" + pid + ", version=" + version + "]";
				}
			}
		}
	}
}
