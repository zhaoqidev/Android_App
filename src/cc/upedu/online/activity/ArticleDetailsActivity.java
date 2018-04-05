package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.WebViewSettingUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * 文章详情界面
 * @author Administrator
 *
 */
public class ArticleDetailsActivity extends TitleBaseActivity {
	private String articleId;//文章ID
	private String articleTitle;//文章标题
	private String articleImage,clickTimes,addtime;//文章图片
	private WebView webview;
	private String iscollected = "false";
	private OnekeyShare oks;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("文章详情");
		setRightButton(R.drawable.iconfont_shoucang, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//收藏
				//判断是否已经登录
				if (!UserStateUtil.isLogined()) {//未登录
					ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							//跳转到登录界面
							Intent intent = new Intent(context, LoginActivity.class);
							startActivity(intent);
						}
					});
				}else {//已登录
					//判断是否已经收藏
					if ("true".equals(iscollected)) {
						ShowUtils.showMsg(context, "您已经收藏过此课程!");
					}else {
						String userId = UserStateUtil.getUserId();
						Map<String, String> map = new HashMap<String, String>();
						map.put("articleId", articleId);
						map.put("userId", userId);
						UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.COLLECTION_ARTICLE,map , new UploadDataCallBack() {

							@Override
							public void onUploadDataSuccess() {
								// TODO Auto-generated method stub
								ShowUtils.showMsg(context, "收藏文章成功");
								iscollected = "true";
								setRightButton(R.drawable.iconfont_shoucang,null);
							}

							@Override
							public void onUploadDataFailure() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
			}
		});
		setRightButton2(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//分享
				//进入分享页面
				if (StringUtil.isEmpty(articleId) || StringUtil.isEmpty(articleTitle) || StringUtil.isEmpty(articleImage)) {
					ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
				}else {
					if (oks == null) {
						oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_ARTICLE,articleId,articleTitle,articleImage,false,null,"文章: "+articleTitle+
								"\n浏览: "+clickTimes+"\n时间: "+addtime);
					}
					oks.show(context);
				}
			}
		});
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		articleId = intent.getStringExtra("articleId");
		articleTitle = intent.getStringExtra("articleTitle");
		articleImage=intent.getStringExtra("articleImage");
		clickTimes=intent.getStringExtra("clickTimes");
		addtime=intent.getStringExtra("addtime");
		iscollected = intent.getStringExtra("iscollected");
		
		View view = View.inflate(context, R.layout.layout_webview, null);
		if ("true".equals(iscollected)) {
			setRightButton(R.drawable.iconfont_shoucang,null);
		}else {
			setRightButton(R.drawable.left_collect,null);
		}
		
		webview = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, webview);
		return view;
	}

	@Override
	protected void initData() {
		if (StringUtil.isEmpty(articleId)) {
			String data = "<h1>该课程未找到</h1><p><img src=\"file:///android_asset/logobaiq.png\" /></p>";
			webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
		}else {
			webview.loadUrl(ConstantsOnline.ARTICLE_DETAILS + articleId);
		}
	}
}
