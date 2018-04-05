package cc.upedu.online.domin;

import java.io.Serializable;
import java.util.List;

public class BannerListBean implements Serializable{
	public List<String> bannerList;

	public List<String> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<String> bannerList) {
		this.bannerList = bannerList;
	}

	@Override
	public String toString() {
		return "BannerListBean [bannerList=" + bannerList + "]";
	}
	
}
