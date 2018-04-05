package cc.upedu.online.utils;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * 分享功能的工具类
 */
public class ShareUtil {
	private static ShareUtil mShareUtil;
	public static ShareUtil getInstance(){
		if (mShareUtil == null) {
			mShareUtil = new ShareUtil();
		}
		return mShareUtil;
	}
	/**
	 * 分享体系
	 */
	public static final int STYE_SUBJECT = 0;
	/**
	 * 分享导师
	 */
	public static final int STYE_TEACHER = 1;
	/**
	 * 分享课程
	 */
	public static final int STYE_COURSE = 2;
	/**
	 * 分享直播
	 */
	public static final int STYE_TELECAST = 3;
	/**
	 * 分享活动
	 */
	public static final int STYE_SPORT = 4;
	/**
	 * 分享文章
	 */
	public static final int STYE_ARTICLE = 5;
	/**
	 * 分享名片
	 */
	public static final int STYE_USERCARD = 6;
	/**
	 * 分享一张带有二维码的图片
	 */
	public static final int STYE_COURSE_QR = 7;
	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 *
	 *
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 *
	 */
	public OnekeyShare showShare(int stye,String id,String name,String logo,boolean isok,String qrCodePath,String text) {
		ShareSDK.initSDK(OnlineApp.myApp.context);
		OnekeyShare oks = new OnekeyShare();
		//当stye==7的时候表示只分享一张图片
		if (stye == 7) {
			oks.setImagePath(logo);
		} else{
		String urlHead = "";
		switch (stye) {
		case 0://分享体系
			urlHead = ConstantsOnline.SHAR_SUBJECT;
			break;
		case 1://分享导师
			urlHead = ConstantsOnline.SHAR_TEACHER;
			break;
		case 2://分享课程
			urlHead = ConstantsOnline.SHAR_COURSE;
			break;
		case 3://分享直播
			urlHead = ConstantsOnline.SHAR_COURSE;
			break;
		case 4://分享活动
			urlHead = ConstantsOnline.SHAR_SPORT;
			break;
		case 5://分享文章
			urlHead = ConstantsOnline.ARTICLE_DETAILS;
			break;
		case 6://分享名片
			urlHead = ConstantsOnline.SHAR_USERCARD;
			break;
		default:
			urlHead = ConstantsOnline.SHAR_COURSE;
			break;
		}
		oks.setTitle(name);
		String secretUid=SharedPreferencesUtil.getInstance().spGetString("secretUid");
		if (stye == 2 && isok) {
			oks.setTitleUrl(urlHead+id+"?shareBy="+secretUid);
			oks.setUrl(urlHead+id+"?shareBy="+secretUid);
			oks.setSiteUrl(urlHead+id+"?shareBy="+secretUid);
//			oks.setText(name+" "+urlHead+id+"?shareBy="+UserStateUtil.getUserId());
		}else if (stye == 3 && isok) {
			oks.setTitleUrl(urlHead+id+"?shareBy="+secretUid+"&type=live");
			oks.setUrl(urlHead+id+"?shareBy="+secretUid+"&type=live");
			oks.setSiteUrl(urlHead+id+"?shareBy="+secretUid+"&type=live");
//			oks.setText(name+" "+urlHead+id+"?shareBy="+UserStateUtil.getUserId()+"&type=live");
		}else {
			oks.setTitleUrl(urlHead+id);
			oks.setUrl(urlHead+id);
			oks.setSiteUrl(urlHead+id);
//			oks.setText(name+" "+urlHead+id);
		}
		if (StringUtil.isEmpty(qrCodePath)) {
			oks.setImageUrl(ConstantsOnline.SERVER_IMAGEURL+logo);
		}else {
			oks.setImagePath(qrCodePath);
		}
		final String appName = OnlineApp.myApp.context.getString(R.string.app_name);
		oks.setText(text);
		oks.setComment(text);
		oks.setSite(appName);
//		oks.setSiteUrl("http://www.upedu.cc/");
		oks.setVenueName(appName);
		oks.setVenueDescription(text);
//			oks.setLatitude(23.056081f);
//			oks.setLongitude(113.385708f);
		oks.disableSSOWhenAuthorize();
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform, ShareParams paramsToShare) {
				// 改写twitter分享内容中的text字段，否则会超长，
				// 因为twitter会将图片地址当作文本的一部分去计算长度
				if ("Twitter".equals(platform.getName())) {
					paramsToShare.setText(appName);
				}
			}
		});
		}
//			oks.setShareFromQQAuthSupport(true);
//			String theme = "SKYBLUE";
//			if(OnekeyShareTheme.SKYBLUE.toString().toLowerCase().equals(theme)){
//				oks.setTheme(OnekeyShareTheme.SKYBLUE);
//			}else{
//				oks.setTheme(OnekeyShareTheme.CLASSIC);
//			}
//			// 令编辑页面显示为Dialog模式
//			oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		//if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		//oks.setCallback(new OneKeyShareCallback());

		// 去自定义不同平台的字段内容
//			oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		// 为EditPage设置一个背景的View
//					oks.setEditPageBackground(getPage());
		return oks;
	}
}
