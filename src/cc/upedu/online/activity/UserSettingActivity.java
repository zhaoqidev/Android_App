package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelBottomBaseActivity;
import cc.upedu.online.domin.UserCordBean;
import cc.upedu.online.domin.UserCordBean.Entity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.CreateBmpFactory;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.HttpMultipartPost;
import cc.upedu.online.utils.HttpMultipartPost.UploadCallBack;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ScreenUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.view.CircleImageView;
import cc.upedu.online.view.ImageSelectorDialog;

public class UserSettingActivity extends TwoPartModelBottomBaseActivity {
	// 我的照片条目中预览的图片
	private ImageView user_card_bg,iv_set_picture,iv_user_stpe;
	// 背景上的头像,设置头像的布局中头像的显示
	private CircleImageView iv_user_head,user_set_head;
	// 设置头像的布局,编辑 姓名 的条目布局,编辑 我的照片 的条目布局,编辑 我的企业 的条目布局,编辑 我的位置 的条目布局,编辑 我的爱好
	// 的条目布局,编辑 我的梦想 的条目布局,编辑 我的生日 的条目布局,编辑 价值观与信念 的条目布局,编辑 我的手机 的条目布局
	// 编辑 我的邮箱的条目布局,编辑 我的微信号 的条目布局,编辑 我的qq号 的条目布局,设置个性背景,标题栏,标题栏左边布局
	private RelativeLayout ll_set_head, ll_set_name, ll_set_picture,
			ll_set_company, ll_set_adress, ll_set_hobby, ll_set_dream,
			ll_set_birthday, ll_set_thought, ll_set_phone, ll_set_email,
			ll_set_weixin, ll_set_qq;
	private LinearLayout user_setting_bg;
	// 姓名,我的企业名称,我的位置,我的爱好 ,我的梦想,我的生日
	// 价值观与信念,我的手机,我的微信号, 我的qq号
	private TextView tv_user_name,tv_user_position,tv_user_contactway,tv_set_name,
			tv_set_company, tv_set_adress, tv_set_hobby, tv_set_dream,
			tv_set_birthday, tv_set_thought, tv_set_phone, tv_set_email,
			tv_set_weixin, tv_set_qq;
	// 输入签名的文本编辑框
	private EditText et_signature;
	private UserCordBean mUserCordBean;
	private UserCordBean mUserCordBeanBack;
	private boolean isChangeUserName = false;
	//旧企业修改的列表,新添加的企业的列表
	private List<CompanyItem> alterCompanyList,addCompanyList;
	private List<String> delCompanyIdList;
	String companyListString = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> filePathList;
			HttpMultipartPost post;
			switch (msg.what) {
			case 0:
				if ("true".equals(mUserCordBean.getSuccess())) {
					setData2View();
				} else {
					ShowUtils.showMsg(context, mUserCordBean.getMessage());
				}
				break;
			case 1:
				if (isChangeUserName) {
					SharedPreferencesUtil.getInstance().editPutString("name", entity.getUserInfo().getName());
					isChangeUserName = false;
				}
				if (userChangedDataMap.containsKey("avatar")) {
					String avatar = userChangedDataMap.get("avatar");
					SharedPreferencesUtil.getInstance().editPutString("avatar", avatar.substring(1, avatar.length()-1));
				}
				if (userChangedDataMap.containsKey("intro")) {
					String intro = userChangedDataMap.get("intro");
					if (intro.contains("\"")) {
						SharedPreferencesUtil.getInstance().editPutString("userInfo", "");
					}else {
						SharedPreferencesUtil.getInstance().editPutString("userInfo", intro);
					}
				}
				if (userChangedDataMap.containsKey("picAddArray")) {
					picAddArray = null;
				}
				if (userChangedDataMap.containsKey("picDelArray")) {
					picDelArray = null;
				}
				if (userChangedDataMap.containsKey("companyList")) {
					alterCompanyList = null;
					addCompanyList = null;
				}
				if (userChangedDataMap.containsKey("delCompArray")) {
					delCompanyIdList = null;
				}
				userChangedDataMap.clear();
				mUserCordBean = mUserCordBeanBack;
				setData2View();
				ShowUtils.showMsg(context, "保存个人信息成功");
				break;
			case 2:
				if (mUserCordBeanBack == null) {
					ShowUtils.showMsg(context, "保存个人信息失败,请检查您的网络连接是否正常!");
				}else {
					ShowUtils.showMsg(context, mUserCordBeanBack.getMessage());
				}
				break;
			case 3:
				String picPath = Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis() + ".png";
				CommonUtil.saveMyBitmap(headBitmap, picPath);
				filePathList = new ArrayList<String>();
        		filePathList.add(picPath);
    			post = new HttpMultipartPost(UserSettingActivity.this, filePathList,new UploadCallBack() {

					@Override
					public void onSuccessListener(String result) {
						// TODO Auto-generated method stub
						Drawable drawable = new BitmapDrawable(headBitmap);
//						user_set_head.setImageDrawable(drawable);
//						iv_user_head.setImageDrawable(drawable);
						disposeCollBackData("avatar", result.substring(result.indexOf("/")));
						saveData();
					}
				});
    			post.execute();
				break;
			}
		}

	};

	// 设置数据到界面
	private void setData2View() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
		if (saveDialog != null && saveDialog.isShowing()) {
			saveDialog.dismiss();
		}
		entity = mUserCordBean.getEntity();
		// 个性背景
		if (!StringUtil.isEmpty(entity.getUserInfo().getBannerUrl())) {
			
			ImageUtils.setImage(entity.getUserInfo().getBannerUrl(), user_card_bg, R.drawable.usercardbg);
		}else {
			user_card_bg.setImageResource(R.drawable.usercardbg);
		}
		// 点赞
		// if (mUserCordBean.getEntity().) {
		// praise_count.setText("");
		// }
		// 签名
		if (!StringUtil.isEmpty(entity.getUserInfo().getName())) {
			et_signature.setText(entity.getUserInfo().getIntro());
		} else {
			et_signature.setText("");
		}
		// 头像
		if (!StringUtil.isEmpty(entity.getUserInfo().getAvatar())) {
			ImageUtils.setImage(entity.getUserInfo().getAvatar(), user_set_head, R.drawable.left_menu_head);
			ImageUtils.setImage(entity.getUserInfo().getAvatar(), iv_user_head, R.drawable.left_menu_head);
		}else {
			user_set_head.setImageResource(R.drawable.left_menu_head);
			iv_user_head.setImageResource(R.drawable.left_menu_head);
		}
		String userType = UserStateUtil.getUserType();
		if (StringUtil.isEmpty(userType)) {
			iv_user_stpe.setImageResource(R.drawable.user_stpe_student_write);
		}else {
			Integer valueOf = Integer.valueOf(userType);
			if (valueOf == 1) {
				iv_user_stpe.setImageResource(R.drawable.user_stpe_teacher_write);
			}else if (valueOf == 2) {
				iv_user_stpe.setImageResource(R.drawable.user_stpe_counselor_write);
			}else {
				iv_user_stpe.setImageResource(R.drawable.user_stpe_student_write);
			}
		}
		// 名称
		if (StringUtil.isEmpty(entity.getUserInfo().getName())) {
			tv_set_name.setText("未设置");
			tv_user_name.setText("未设置用户名");
		} else {
			tv_set_name.setText(entity.getUserInfo().getName());
			tv_user_name.setText(entity.getUserInfo().getName());
		}
		// 企业
		if (entity.getUserInfo().getCompanyList().size() > 0) {
			if (StringUtil.isEmpty(entity.getUserInfo().getCompanyList().get(0).getName())) {
				tv_set_company.setText("未设置");
			} else {
				tv_set_company.setText(entity.getUserInfo().getCompanyList().get(0).getName());
			}
			oldIdPosition = new HashMap<String, Integer>();
			for (int i = 0; i < entity.getUserInfo().getCompanyList().size(); i++) {
				oldIdPosition.put(entity.getUserInfo().getCompanyList().get(i).getId(), i);
			}
			if (StringUtil.isEmpty(entity.getUserInfo().getCompanyList().get(0).getPositionName())) {
				tv_user_position.setText("未设置职位");
			}else {
				tv_user_position.setText(entity.getUserInfo().getCompanyList().get(0).getPositionName());
			}
		}
		// 我的照片
		if (entity.getUserInfo().getPicList().size() > 0) {
			if (!StringUtil.isEmpty(entity.getUserInfo().getPicList().get(0)
					.getPicPath())) {
				ImageUtils.setImage(entity.getUserInfo().getPicList().get(0).getPicPath(),
						iv_set_picture, R.drawable.wodeimg_default);
			}
		}else {
			iv_set_picture.setImageResource(R.drawable.wodeimg_default);
		}
		// 我的位置
		if (StringUtil.isEmpty(entity.getUserInfo().getCity())) {
			tv_set_adress.setText("未设置");
		} else {
			tv_set_adress.setText(entity.getUserInfo().getCity());
		}
		// 我的爱好
		if (StringUtil.isEmpty(entity.getUserInfo().getHobbyText())) {
			tv_set_hobby.setText("未设置");
		} else {
			tv_set_hobby.setText(entity.getUserInfo().getHobbyText());
		}
		// 我的梦想
		if (entity.getUserInfo().getDreamList().size() > 0) {
			if (StringUtil.isEmpty(entity.getUserInfo().getDreamList().get(0))) {
				tv_set_dream.setText("未设置");
			} else {
				tv_set_dream.setText(entity.getUserInfo().getDreamList().get(0));
			}
		} else {
			tv_set_dream.setText("未设置");
		}
		// 我的生日
		if (StringUtil.isEmpty(entity.getUserInfo().getBirthday())) {
			tv_set_birthday.setText("未设置");
		} else {
			String[] strs = entity.getUserInfo().getBirthday().split("-");
			tv_set_birthday.setText(strs[0]+"年"+strs[1]+"月"+strs[2]+"日");
		}
		// 价值观和信念
		if (entity.getUserInfo().getValueList().size() > 0) {
			if (StringUtil.isEmpty(entity.getUserInfo().getValueList().get(0))) {
				tv_set_thought.setText("未设置");
			} else {
				tv_set_thought.setText(entity.getUserInfo().getValueList().get(0));
			}
		} else {
			tv_set_thought.setText("未设置");
		}
		// 我的手机
		if (StringUtil.isEmpty(entity.getUserInfo().getMobile())) {
			tv_set_phone.setText("未设置");
			tv_user_contactway.setText("未设置联系方式");
		} else {
			tv_set_phone.setText(entity.getUserInfo().getMobile());
			tv_user_contactway.setText(entity.getUserInfo().getMobile());
		}
		// 我的邮箱
		if (StringUtil.isEmpty(entity.getUserInfo().getEmail())) {
			tv_set_email.setText("未设置");
		} else {
			tv_set_email.setText(entity.getUserInfo().getEmail());
		}
		// 我的微信
		if (StringUtil.isEmpty(entity.getUserInfo().getWeixin())) {
			tv_set_weixin.setText("未设置");
		} else {
			tv_set_weixin.setText(entity.getUserInfo().getWeixin());
		}
		// 我的qq
		if (StringUtil.isEmpty(entity.getUserInfo().getQq())) {
			tv_set_qq.setText("未设置");
		} else {
			tv_set_qq.setText(entity.getUserInfo().getQq());
		}
	}

	@Override
	protected void initData() {
		// 根据userId获取用户名片信息
		Map<String, String> requestDataMap;
		RequestVo requestVo;
		// 获取用户名片的数据
		requestDataMap = ParamsMapUtil.getUserCard(context, userId,null);
		requestVo = new RequestVo(ConstantsOnline.USER_CARD, context,
				requestDataMap, new MyBaseParser<>(UserCordBean.class));
		DataCallBack<UserCordBean> rollDataCallBack = new DataCallBack<UserCordBean>() {

			@Override
			public void processData(UserCordBean object) {
				if (object == null) {
					ShowUtils.showMsg(context, "获取数据失败请联系客服");
				} else {
					mUserCordBean = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, rollDataCallBack);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("成长名片");
		setRightText("保存", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String signature = et_signature.getText().toString().trim();
				if (!StringUtil.isEmpty(signature)) {
					if (!signature.equals(entity.getUserInfo().getIntro())) {
						disposeCollBackData("intro", signature);
					}
				}else {
					if (!StringUtil.isEmpty(entity.getUserInfo().getIntro())) {
						disposeCollBackData("intro", "");
					}
				}
				saveData();
			}
		});
		userId = getIntent().getStringExtra("userId");
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_user_setting, null);
		user_card_bg = (ImageView) view.findViewById(R.id.user_card_bg);
		int screenWidth = ScreenUtil.getInstance(context).getScreenWidth();
		LayoutParams para = user_card_bg.getLayoutParams();
        para.width = screenWidth;  
        para.height = screenWidth*542/960;
        user_card_bg.setLayoutParams(para);
		iv_user_head = (CircleImageView) view.findViewById(R.id.iv_user_head);
		iv_user_stpe = (ImageView) view.findViewById(R.id.iv_user_stpe);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		tv_user_position = (TextView) view.findViewById(R.id.tv_user_position);
		tv_user_contactway = (TextView) view.findViewById(R.id.tv_user_contactway);
		
		ll_set_head = (RelativeLayout) view.findViewById(R.id.ll_set_head);
		user_set_head = (CircleImageView) view.findViewById(R.id.user_set_head);
		et_signature = (EditText) view.findViewById(R.id.et_signature);

		ll_set_name = (RelativeLayout) view.findViewById(R.id.ll_set_name);
		tv_set_name = (TextView) view.findViewById(R.id.tv_set_name);
		ll_set_picture = (RelativeLayout) view.findViewById(R.id.ll_set_picture);
		iv_set_picture = (ImageView) view.findViewById(R.id.iv_set_picture);

		ll_set_company = (RelativeLayout) view.findViewById(R.id.ll_set_company);
		tv_set_company = (TextView) view.findViewById(R.id.tv_set_company);
		ll_set_adress = (RelativeLayout) view.findViewById(R.id.ll_set_adress);
		tv_set_adress = (TextView) view.findViewById(R.id.tv_set_adress);
		ll_set_hobby = (RelativeLayout) view.findViewById(R.id.ll_set_hobby);
		tv_set_hobby = (TextView) view.findViewById(R.id.tv_set_hobby);
		ll_set_dream = (RelativeLayout) view.findViewById(R.id.ll_set_dream);
		tv_set_dream = (TextView) view.findViewById(R.id.tv_set_dream);
		ll_set_birthday = (RelativeLayout) view.findViewById(R.id.ll_set_birthday);
		tv_set_birthday = (TextView) view.findViewById(R.id.tv_set_birthday);
		ll_set_thought = (RelativeLayout) view.findViewById(R.id.ll_set_thought);
		tv_set_thought = (TextView) view.findViewById(R.id.tv_set_thought);
		ll_set_email = (RelativeLayout) view.findViewById(R.id.ll_set_email);
		tv_set_email = (TextView) view.findViewById(R.id.tv_set_email);
		ll_set_phone = (RelativeLayout) view.findViewById(R.id.ll_set_phone);
		tv_set_phone = (TextView) view.findViewById(R.id.tv_set_phone);
		ll_set_weixin = (RelativeLayout) view.findViewById(R.id.ll_set_weixin);
		tv_set_weixin = (TextView) view.findViewById(R.id.tv_set_weixin);
		ll_set_qq = (RelativeLayout) view.findViewById(R.id.ll_set_qq);
		tv_set_qq = (TextView) view.findViewById(R.id.tv_set_qq);
		return view;
	}

	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_usersetting_bottom, null);
		user_setting_bg = (LinearLayout) view.findViewById(R.id.user_setting_bg);
		return view;
	}

	public final static int REQUEST_SETHEAD = 0;
	public static final int REQUEST_SETNAME = 1;
	public static final int REQUEST_SETPICTURE = 2;
	public static final int REQUEST_SETBG = 3;
	public static final int REQUEST_SETQQ = 4;
	public static final int REQUEST_SETWEIXIN = 5;
	public static final int REQUEST_SETEMAIL = 6;
	public static final int REQUEST_SETPHONE = 7;
	public static final int REQUEST_SETTHOUGHT = 8;
	public static final int REQUEST_SETBIRTHDAY = 9;
	public static final int REQUEST_SETADRESS = 10;
	public static final int REQUEST_SETDREAM = 11;
	public static final int REQUEST_SETHOBBY = 12;
	private static final int REQUEST_SETCOMPANY = 13;
	private Entity entity;
	private String userId;
	private List<String> picAddArray;
	private List<String> picDelArray;
	private Bitmap headBitmap;
	private Map<String, Integer> oldIdPosition;
	private Map<String, Integer> oldIdPositionProduct;
	private Dialog loadingDialog;
	private Dialog saveDialog;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			boolean isBannerUrl = false;
			if (requestCode == CreateBmpFactory.PHOTO_REQUEST_GALLERY) {
				// 如果是直接从相册获取
				startPhotoZoom(data.getData());
				isBannerUrl = true;
			}
			if (requestCode == CreateBmpFactory.PHOTO_REQUEST_CAREMA) {
				// 如果是调用相机拍照时
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/xiaoma.jpg");
				startPhotoZoom(Uri.fromFile(temp));
				isBannerUrl = true;
			}
			if (requestCode == CreateBmpFactory.PHOTO_REQUEST_NULL) {
				/**
				 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
				 * 当前功能时，会报NullException
				 * 
				 */
				if (data != null) {
					headBitmap = setPicToView(requestCode,resultCode,data);
					if (headBitmap != null) {
						handler.obtainMessage(3).sendToTarget();  
					}else{
						ShowUtils.showMsg(context, "上传的文件路径出错");
					} 
				}
				isBannerUrl = true;
			}
			if (requestCode == REQUEST_SETNAME) {// 设置名称的数据回传
				if (resultCode == SetNameActivity.RESULT_SETNAME) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String name = bundle.getString("name");
						if (!name.equals(entity.getUserInfo().getName())) {
							entity.getUserInfo().setName(name);
							tv_set_name.setText(name);
							disposeCollBackData("name", name);
							isChangeUserName = true;
						}
						String gender = bundle.getString("sex");
						if (!gender.equals(entity.getUserInfo().getGender())) {
							if (!StringUtil.isEmpty(gender)) {
								entity.getUserInfo().setGender(gender);
								disposeCollBackData("gender", gender);
							}
						}
						String married = bundle.getString("married");
						if (!married.equals(entity.getUserInfo().getMarried())) {
							if (!StringUtil.isEmpty(married)) {
								entity.getUserInfo().setMarried(married);
								disposeCollBackData("married", married);
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETPICTURE) {// 设置图片的数据回传
				if (resultCode == SetPictureActivity.RESULT_SETPICTURE) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						picAddArray = (List<String>) bundle.getSerializable("picAddArray");
						picDelArray = (List<String>) bundle.getSerializable("picDelArray");
						if (picAddArray.size() > 0) {
							ImageUtils.setImage(picAddArray.get(0).substring(1, picAddArray.get(0).length()-1),
									iv_set_picture, 0);
							
							disposeCollBackData("picAddArray", picAddArray.toString());
							if (picDelArray.size() > 0) {
								disposeCollBackData("picDelArray", picDelArray.toString());
							}
						}else {
							if (picDelArray.size() > 0) {
								disposeCollBackData("picDelArray", picDelArray.toString());
								if (entity.getUserInfo().getPicList().size() > picDelArray.size()) {
									List<String> picDelIds = new ArrayList<String>();
									for (String picIdVos : picDelArray) {
										picDelIds.add(picIdVos.split("_")[0]);
									}
									for (PicItem picItem : entity.getUserInfo().getPicList()) {
										if (!picDelIds.contains(picItem.getId())) {
											
											ImageUtils.setImage(picItem.getPicPath(), iv_set_picture, 0);
											break;
										}
									}
								}else {
									iv_set_picture.setImageResource(R.drawable.wodeimg_default);
								}
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETCOMPANY) {// 设置企业的数据回传
				if (resultCode == SetCompanyActivity.RESULT_SETCOMPANY) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						alterCompanyList = (List<CompanyItem>) bundle.getSerializable("alterCompanyList");
						addCompanyList = (List<CompanyItem>) bundle.getSerializable("addCompanyList");
						delCompanyIdList = (List<String>) bundle.getSerializable("delCompanyIdList");
						List<CompanyItem> backCompanyList = new ArrayList<UserCordBean.Entity.UserInfo.CompanyItem>();
						if (alterCompanyList != null && alterCompanyList.size() > 0) {
							backCompanyList.addAll(alterCompanyList);
							tv_set_company.setText(alterCompanyList.get(0).getName());
						}
						if (addCompanyList != null && addCompanyList.size() > 0) {
							tv_set_company.setText(addCompanyList.get(0).getName());
							backCompanyList.addAll(addCompanyList);
						}
						if (backCompanyList.size() > 0) {
//							if (userChangedDataMap.containsKey("companyList:")) {
//								if (!StringUtil.isEmpty(companyListString)) {
//									userInfo.replace("companyList:", "").replace(companyListString, "");
//									companyListString = null;
//								}
//							}
							companyListString = disposeCompanyCollBackData(backCompanyList);
							if (!StringUtil.isEmpty(companyListString) && companyListString.length() != 2) {
								disposeCollBackData("companyList", companyListString);
							}
						}
						if (delCompanyIdList != null && delCompanyIdList.size() > 0) {
							disposeCollBackData("delCompArray", delCompanyIdList.toString().substring(1, delCompanyIdList.toString().length()-1));
						}
					}
				}
			}
			if (requestCode == REQUEST_SETADRESS) {// 设置我的位置的数据回传
				if (resultCode == SetAdressActivity.RESULT_SETADRESS) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String city = bundle.getString("city");
						String cityId = bundle.getString("cityId");
						if (!city.equals(entity.getUserInfo().getCity())) {
							if (!StringUtil.isEmpty(city)) {
								entity.getUserInfo().setCity(city);
								tv_set_adress.setText(city);
								if (!"0".equals(cityId)) {
									if (!StringUtil.isEmpty(cityId)) {
										disposeCollBackData("city", cityId);
									}
								}
							}else {
								tv_set_adress.setText("未设置");
							}
						}
						String hometown = bundle.getString("hometown");
						String hometownId = bundle.getString("hometownId");
						if (!hometown.equals(entity.getUserInfo().getHometown())) {
							entity.getUserInfo().setHometown(hometown);
							if (!StringUtil.isEmpty(hometown)) {
								if (!"0".equals(hometownId)) {
									if (!StringUtil.isEmpty(hometownId)) {
										disposeCollBackData("hometown", hometownId);
									}
								}
							}
						}
						String travelcity = bundle.getString("travelcity");
						String travelcityId = bundle.getString("travelcityId");
						if (!travelcity.equals(entity.getUserInfo().getTravelCity())) {
							entity.getUserInfo().setTravelCity(travelcity);
							if (!StringUtil.isEmpty(travelcity)) {
								if (!"0".equals(travelcityId)) {
									if (!StringUtil.isEmpty(travelcityId)) {
										disposeCollBackData("travelcity", travelcityId);
									}
								}
							}else {
								disposeCollBackData("travelcity", "");
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETHOBBY) {// 设置爱好的数据回传
				if (resultCode == SetHobbyActivity.RESULT_SETHOBBY) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String hobbyId = bundle.getString("hobbyId");
						String hobby = bundle.getString("hobby");
						if (!hobby.equals(entity.getUserInfo().getHobbyText())) {
							if (!StringUtil.isEmpty(hobby)) {
								tv_set_hobby.setText(hobby);
								entity.getUserInfo().setHobbyText(hobby);
							}else {
								tv_set_hobby.setText("未设置");
								entity.getUserInfo().setHobbyText("\"\"");
							}
						}
						if (!hobbyId.equals(entity.getUserInfo().getHobby())) {
							if (!StringUtil.isEmpty(hobbyId)) {
								entity.getUserInfo().setHobby(hobbyId);
								disposeCollBackData("hobby", hobbyId);
							}else {
								entity.getUserInfo().setHobby("");
								disposeCollBackData("hobby", "");
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETDREAM) {// 设置梦想的数据回传
				if (resultCode == SetDreamActivity.RESULT_SETDREAM) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						List<String> dreamList = (List<String>) bundle.getSerializable("dreamList");
						
						if (dreamList.size() > 0) {
							for (int i = 0; i < dreamList.size(); i++) {
								if (StringUtil.isEmpty(dreamList.get(i).trim())) {
									dreamList.remove(i);
									i--;
								}
							}
							if (dreamList.size() > 0) {
								tv_set_dream.setText(dreamList.get(0));
								List<String> newDreamList = new ArrayList<String>();
								for (String dreamStr : dreamList) {
									newDreamList.add("\'"+dreamStr +"\'");
								}
								disposeCollBackData("dreamList", newDreamList.toString());
								entity.getUserInfo().setDreamList(dreamList);
							}else {
								if (entity.getUserInfo().getDreamList() != null && entity.getUserInfo().getDreamList().size() >0) {
									disposeCollBackData("dreamList", dreamList.toString());
									entity.getUserInfo().setDreamList(dreamList);
								}
								tv_set_dream.setText("未设置");
							}
							
						}else {
							if (entity.getUserInfo().getDreamList() != null && entity.getUserInfo().getDreamList().size() >0) {
								disposeCollBackData("dreamList", dreamList.toString());
								entity.getUserInfo().setDreamList(dreamList);
							}
							tv_set_dream.setText("未设置");
						}
					}
				}
			}
			if (requestCode == REQUEST_SETBIRTHDAY) {// 设置生日的数据回传
				if (resultCode == SetBirthdayActivity.RESULT_SETBIRTHDAY) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String birthday = bundle.getString("birthday");
						String birthdayZodiac = bundle.getString("birthdayZodiac");
						String constellation = bundle.getString("constellation");
						String zodiac = bundle.getString("zodiac");
						if (!birthday.equals(entity.getUserInfo().getBirthday())) {
							if (!StringUtil.isEmpty(birthday)) {
								entity.getUserInfo().setBirthday(birthday);
								String[] strs = birthday.split("-");
								tv_set_birthday.setText(strs[0]+"年"+strs[1]+"月"+strs[2]+"日");
								disposeCollBackData("birthday", birthday);
								entity.getUserInfo().setBirthdayZodiac(birthdayZodiac);
								disposeCollBackData("birthdayZodiac", birthdayZodiac);
								entity.getUserInfo().setConstellation(constellation);
								disposeCollBackData("constellation", constellation);
								entity.getUserInfo().setZodiac(zodiac);
								disposeCollBackData("zodiac", zodiac);
								
							}
						}
						String isbirthdayopen = bundle.getString("isbirthdayopen");
						if (!isbirthdayopen.equals(entity.getUserInfo().getIsbirthdayopen())) {
							if (!StringUtil.isEmpty(isbirthdayopen)) {
								entity.getUserInfo().setIsbirthdayopen(isbirthdayopen);
								disposeCollBackData("isbirthdayopen", isbirthdayopen);
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETTHOUGHT) {// 设置价值观的数据回传
				if (resultCode == SetThoughtActivity.RESULT_SETTHOUGHT) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						List<String> thoughtList = (List<String>) bundle
								.getSerializable("thoughtList");
						if (thoughtList.size() > 0) {
							for (int i = 0; i < thoughtList.size(); i++) {
								if (StringUtil.isEmpty(thoughtList.get(i).trim())) {
									thoughtList.remove(i);
									i--;
								}
							}
							if (thoughtList.size() > 0) {
								tv_set_thought.setText(thoughtList.get(0));
								List<String> newThoughtList = new ArrayList<String>();
								for (String thoughtStr : thoughtList) {
									newThoughtList.add("\'"+thoughtStr +"\'");
								}
								disposeCollBackData("valueList", newThoughtList.toString());
								entity.getUserInfo().setValueList(thoughtList);
							}else {
								if (entity.getUserInfo().getValueList() != null && entity.getUserInfo().getValueList().size() >0) {
									disposeCollBackData("valueList", thoughtList.toString());
									entity.getUserInfo().setValueList(thoughtList);
								}else {
									tv_set_thought.setText("未设置");
								}
							}
						} else {
							if (entity.getUserInfo().getValueList() != null && entity.getUserInfo().getValueList().size() >0) {
								disposeCollBackData("valueList", thoughtList.toString());
								entity.getUserInfo().setValueList(thoughtList);
							}else {
								tv_set_thought.setText("未设置");
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETPHONE) {// 设置手机的数据回传
				if (resultCode == SetPhoneActivity.RESULT_SETPHONE) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String ismobileopen = bundle.getString("ismobileopen");
						if (!ismobileopen.equals(entity.getUserInfo()
								.getIsmobileopen())) {
							if (!StringUtil.isEmpty(ismobileopen)) {
								entity.getUserInfo().setIsmobileopen(ismobileopen);
								disposeCollBackData("ismobileopen", ismobileopen);
							}
						}
					}
				}
			}
			if (requestCode == REQUEST_SETEMAIL) {// 设置邮箱的数据回传
				if (resultCode == SetEmailActivity.RESULT_SETEMAIL) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String email = bundle.getString("email");
						String isemailopen = bundle.getString("isemailopen");
						if (!StringUtil.isEmpty(email)) {
							if (!email.equals(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin(email);
								tv_set_email.setText(email);
								disposeCollBackData("email", email);
							}
						}else {
							if (!StringUtil.isEmpty(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin("");
								tv_set_email.setText("未设置");
								disposeCollBackData("email", "");
							}
						}
						if (!StringUtil.isEmpty(isemailopen)) {
							if (!isemailopen.equals(entity.getUserInfo().getIsemailopen())) {
								entity.getUserInfo().setIsemailopen(isemailopen);
								disposeCollBackData("isemailopen", isemailopen);
							}
						}else {
							entity.getUserInfo().setIsweixinopen("1");
							disposeCollBackData("isemailopen", "1");
						}
					}
				}
			}
			if (requestCode == REQUEST_SETWEIXIN) {// 设置微信的数据回传
				if (resultCode == SetWeixinActivity.RESULT_SETWEIXIN) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String weixin = bundle.getString("weixin");
						String isweixinopen = bundle.getString("isweixinopen");
						if (!StringUtil.isEmpty(weixin)) {
							if (!weixin.equals(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin(weixin);
								tv_set_weixin.setText(weixin);
								disposeCollBackData("weixin", weixin);
							}
						}else {
							if (!StringUtil.isEmpty(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin("");
								tv_set_weixin.setText("未设置");
								disposeCollBackData("weixin", "");
							}
						}
						if (!StringUtil.isEmpty(isweixinopen)) {
							if (!isweixinopen.equals(entity.getUserInfo().getIsweixinopen())) {
								entity.getUserInfo().setIsweixinopen(isweixinopen);
								disposeCollBackData("isweixinopen", isweixinopen);
							}
						}else {
							entity.getUserInfo().setIsweixinopen("1");
							disposeCollBackData("isweixinopen", "1");
						}
					}
				}
			}
			if (requestCode == REQUEST_SETQQ) {// 设置QQ的数据回传
				if (resultCode == SetQqActivity.RESULT_SETQQ) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						String qq = bundle.getString("qq");
						String isqqopen = bundle.getString("isqqopen");
						if (!StringUtil.isEmpty(qq)) {
							if (!qq.equals(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin(qq);
								tv_set_qq.setText(qq);
								disposeCollBackData("qq", qq);
							}
						}else {
							if (!StringUtil.isEmpty(entity.getUserInfo().getWeixin())) {
								entity.getUserInfo().setWeixin("");
								tv_set_qq.setText("未设置");
								disposeCollBackData("qq", "");
							}
						}
						if (!StringUtil.isEmpty(isqqopen)) {
							if (!isqqopen.equals(entity.getUserInfo().getIsqqopen())) {
								entity.getUserInfo().setIsqqopen(isqqopen);
								disposeCollBackData("isqqopen", isqqopen);
							}
						}else {
							entity.getUserInfo().setIsqqopen("1");
							disposeCollBackData("isqqopen", "1");
						}
					}
				}
			}
			if (requestCode == REQUEST_SETBG) {// 设置个性背景的数据回传
				isBannerUrl = true;
				if (resultCode == SetBgActivity.RESULT_SETBG) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						final String bannerUrl = bundle.getString("bannerUrl");
						if (!StringUtil.isEmpty(bannerUrl)) {
							if (!bannerUrl.equals(entity.getUserInfo().getBannerUrl())) {
								entity.getUserInfo().setBannerUrl(bannerUrl);
								Map<String, String> map = new HashMap<String, String>();
								map.put("bannerUrl", bannerUrl);
								map.put("userId", userId);
								UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.UPDATE_USERBANNER,map , new UploadDataCallBack() {

									@Override
									public void onUploadDataSuccess() {
										// TODO Auto-generated method stub
										ShowUtils.showMsg(context, "保存个性背景成功");
										ImageUtils.setImage(bannerUrl, user_card_bg, R.drawable.usercardbg);
									}

									@Override
									public void onUploadDataFailure() {
										// TODO Auto-generated method stub
										
									}
								});
//								disposeCollBackData("bannerUrl", bannerUrl);
							}
						}else {
							ShowUtils.showMsg(context, "设置个性背景失败异常,请联系客服!");
						}
					}
				}
			}
			if (!isBannerUrl) {
				saveData();
			}else {
				isBannerUrl = false;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	private HashMap<String, String> userChangedDataMap;
	private void disposeCollBackData(String key, String value) {
		if (userChangedDataMap == null) {
			userChangedDataMap = new HashMap<>();
		}
		userChangedDataMap.put(key, value);
	}
	private String getUserInfo(){
		String userInfo = "";
		if (userChangedDataMap != null && userChangedDataMap.size() > 0) {
			JSONObject jsonObject = new JSONObject(userChangedDataMap);
			userInfo = jsonObject.toString();
			userInfo = userInfo.substring(1, userInfo.length()-1);
		}
		return userInfo;
	}
	private String disposeCompanyCollBackData(List<CompanyItem> companyList) {
		String companyString = null;
		for (int i = 0; i < companyList.size(); i++) {
			if (i == 0) {
				companyString = "[";
			}
			String companyItemString = "{";
			if (!companyList.get(i).getId().contains("#")) {//旧企业
				companyItemString += ("cid:"+companyList.get(i).getId()+",");
				if (!StringUtil.isEmpty(companyList.get(i).getName()) && !companyList.get(i).getName().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getName())) {
					companyItemString += ("name:"+companyList.get(i).getName()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIsnameopen()) && !companyList.get(i).getIsnameopen().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getIsnameopen())) {
					companyItemString += ("isnameopen:"+companyList.get(i).getIsnameopen()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIndustry()) && !companyList.get(i).getIndustry().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getIndustry())) {
					companyItemString += ("industry:"+companyList.get(i).getIndustry()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getCity()) && !companyList.get(i).getCity().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getCity())) {
					companyItemString += ("city:"+companyList.get(i).getCity()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getPosition()) && !companyList.get(i).getPosition().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getPosition())) {
					companyItemString += ("position:"+companyList.get(i).getPosition()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getWebsite()) && !companyList.get(i).getWebsite().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getWebsite())) {
					companyItemString += ("website:"+companyList.get(i).getWebsite()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIswebsiteopen()) && !companyList.get(i).getIswebsiteopen().equals(entity.getUserInfo().getCompanyList().get(oldIdPosition.get(companyList.get(i).getId())).getIswebsiteopen())) {
					companyItemString += ("issiteopen:"+companyList.get(i).getIswebsiteopen()+",");
				}
			}else {//新企业
				if (!StringUtil.isEmpty(companyList.get(i).getName())) {
					companyItemString += ("name:"+companyList.get(i).getName()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIsnameopen())) {
					companyItemString += ("isnameopen:"+companyList.get(i).getIsnameopen()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIndustry())) {
					companyItemString += ("industry:"+companyList.get(i).getIndustry()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getCity())) {
					companyItemString += ("city:"+companyList.get(i).getCity()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getPosition())) {
					companyItemString += ("position:"+companyList.get(i).getPosition()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getWebsite())) {
					companyItemString += ("website:"+companyList.get(i).getWebsite()+",");
				}
				if (!StringUtil.isEmpty(companyList.get(i).getIswebsiteopen())) {
					companyItemString += ("issiteopen:"+companyList.get(i).getIswebsiteopen()+",");
				}
			}
			List<String> delProductIdList = companyList.get(i).getDelProductIdList();
			if (delProductIdList != null && delProductIdList.size() > 0) {
				companyItemString += ("delProdArray:"+"\""+delProductIdList.toString().substring(1, delProductIdList.toString().length()-1)+"\""+",");
			}
			List<ProductItem> productList = new ArrayList<UserCordBean.Entity.UserInfo.CompanyItem.ProductItem>();
			List<ProductItem> alterProductList = companyList.get(i).getAlterProductList();
			List<ProductItem> addProductList = companyList.get(i).getAddProductList();
			if (alterProductList != null && alterProductList.size() > 0) {
				productList.addAll(alterProductList);
			}
			if (addProductList != null && addProductList.size() > 0) {
				productList.addAll(addProductList);
			}
			if (productList.size() > 0) {
				if (oldIdPositionProduct == null) {
					oldIdPositionProduct = new HashMap<String, Integer>();
				}else {
					oldIdPositionProduct.clear();
				}
				if (companyList.get(i).getProductList() != null && companyList.get(i).getProductList().size() > 0) {
					for (int j = 0; j < companyList.get(i).getProductList().size(); j++) {
						oldIdPositionProduct.put(companyList.get(i).getProductList().get(j).getId(), j);
					}
				}
				String prodListString = disposeProdCollBackData(productList,companyList.get(i).getProductList());
				if (!StringUtil.isEmpty(prodListString) && prodListString.length() != 2) {
					companyItemString += ("prodList:"+prodListString);
				}
			}
			if (companyItemString.lastIndexOf(",") == companyItemString.length() - 1) {
				companyItemString = companyItemString.substring(0, companyItemString.length() - 1);
			}
			companyItemString += "}";
			companyString += companyItemString;
			if (i == companyList.size() - 1) {
				companyString += "]";
			}else {
				companyString += ",";
			}
		}
		return companyString;
	}
	private String disposeProdCollBackData(List<ProductItem> productList,List<ProductItem> oldProductList) {
		String productString = null;
		for (int i = 0; i < productList.size(); i++) {
			if (i == 0) {
				productString = "[";
			}
			String productItemString = "{";
			if (productList.get(i).getId().contains("#")) {//新产品
				if (!StringUtil.isEmpty(productList.get(i).getTitle())) {
					productItemString += ("title:"+productList.get(i).getTitle()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getDesc())) {
					productItemString += ("desc:"+productList.get(i).getDesc()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getValue())) {
					productItemString += ("value:"+productList.get(i).getValue()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getCustomer())) {
					productItemString += ("customer:"+productList.get(i).getCustomer()+",");
				}
			}else {//旧产品
				productItemString += ("pid:"+productList.get(i).getId()+",");
				if (!StringUtil.isEmpty(productList.get(i).getTitle()) && !productList.get(i).getTitle().equals(oldProductList.get(oldIdPositionProduct.get(productList.get(i).getId())).getTitle())) {
					productItemString += ("title:"+productList.get(i).getTitle()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getDesc()) && !productList.get(i).getDesc().equals(oldProductList.get(oldIdPositionProduct.get(productList.get(i).getId())).getDesc())) {
					productItemString += ("desc:"+productList.get(i).getDesc()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getValue()) && !productList.get(i).getValue().equals(oldProductList.get(oldIdPositionProduct.get(productList.get(i).getId())).getValue())) {
					productItemString += ("value:"+productList.get(i).getValue()+",");
				}
				if (!StringUtil.isEmpty(productList.get(i).getCustomer()) && !productList.get(i).getCustomer().equals(oldProductList.get(oldIdPositionProduct.get(productList.get(i).getId())).getCustomer())) {
					productItemString += ("customer:"+productList.get(i).getCustomer()+",");
				}
				if (productList.get(i).getPicDelArray() != null && productList.get(i).getPicDelArray().size() > 0) {
					productItemString += ("picDelArray:"+productList.get(i).getPicDelArray().toString()+",");
				}
			}
			if (productList.get(i).getPicAddArray() != null && productList.get(i).getPicAddArray().size() > 0) {
				productItemString += ("picAddArray:"+productList.get(i).getPicAddArray().toString()+",");
			}
			if (productItemString.lastIndexOf(",") == productItemString.length() - 1) {
				productItemString = productItemString.substring(0, productItemString.length() - 1);
			}
			productItemString += "}";
			productString += productItemString;
			if (i == productList.size() - 1) {
				productString += "]";
			}else {
				productString += ",";
			}
		}
		return productString;
	}
//	private void addString(String key, String value) {
//		if (StringUtil.isEmpty(userInfo)) {
//			userInfo = key+value;
//		}else {
//			userInfo += (","+key+value);
//		}
//	}
//	private void replaceString(String key, String value) {
//		String[] strings = userInfo.split(key);
//		if (2==strings.length) {
//			if (-1==strings[1].indexOf(",")) {
//				userInfo = strings[0] +key+value;
//			}else {
//				if ("\"".equals(strings[1].substring(0, 1))) {
//					userInfo = strings[0] +key+value+ strings[1].substring(strings[1].indexOf("\"",1)+1);
//				}else {
//					userInfo = strings[0] +key+value+ strings[1].substring(strings[1].indexOf(","));
//				}
//			}
//		}else {
//			ShowUtils.showMsg(context, "数据上传准备工作发生异常,请联系客服,谢谢!");
//		}
//	}
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CreateBmpFactory.PHOTO_REQUEST_NULL);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return
	 */
	private Bitmap setPicToView(int requestCode, int resultCode, Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			
			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // 将图片流以字符串形式存储下来
			 * 
			 * tp = new String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
			 * 
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
			 * Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable
			 * = new BitmapDrawable(dBitmap);
			 */
			
			return photo;
			
		}else {
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.praise_count:// 点赞

			break;
		case R.id.ll_set_head:// 设置头像
			if (entity != null) {
				ImageSelectorDialog dialog = new ImageSelectorDialog(context);
				dialog.setDialogCallBack(dialog.new DialogCallBack() {
					@Override
					public void sendPic() {
						/**
						 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
						 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
						 */
						Intent intent = new Intent(Intent.ACTION_PICK, null);

						/**
						 * 下面这句话，与其它方式写是一样的效果，如果：
						 * intent.setData(MediaStore.Images
						 * .Media.EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");设置数据类型
						 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
						 * ："image/jpeg 、 image/png等的类型"
						 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
						 */
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent,
								CreateBmpFactory.PHOTO_REQUEST_GALLERY);
					}

					@Override
					public void sendCamera() {
						/**
						 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
						 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
						 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
						 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
						 */
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										"xiaoma.jpg")));
						startActivityForResult(intent,
								CreateBmpFactory.PHOTO_REQUEST_CAREMA);
					}
				});
				Window window = dialog.getWindow();
				window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
				window.setWindowAnimations(R.style.style_imageselectordialog); // 添加动画
				window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				dialog.show();
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_name:// 设置名称
			if (entity != null) {
				intent = new Intent(context, SetNameActivity.class);
				intent.putExtra("name", entity.getUserInfo().getName());
				intent.putExtra("sex", entity.getUserInfo().getGender());
				intent.putExtra("married", entity.getUserInfo().getMarried());
				startActivityForResult(intent, REQUEST_SETNAME);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_picture:// 设置我的照片
			if (entity != null) {
				intent = new Intent(context, SetPictureActivity.class);
				intent.putExtra("picList", (Serializable) entity.getUserInfo().getPicList());
				if (picAddArray != null) {
					intent.putExtra("picAddArray", (Serializable) picAddArray);
				}
				if (picDelArray != null) {
					intent.putExtra("picDelArray", (Serializable) picDelArray);
				}
				startActivityForResult(intent, REQUEST_SETPICTURE);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_company:// 设置我的企业
			if (entity != null) {
				intent = new Intent(context, SetCompanyActivity.class);
				intent.putExtra("companyList", (Serializable) entity
						.getUserInfo().getCompanyList());
				intent.putExtra("alterCompanyList",
						(Serializable) alterCompanyList);
				intent.putExtra("addCompanyList",(Serializable) addCompanyList);
				intent.putExtra("delCompanyIdList",(Serializable) delCompanyIdList);
				
				startActivityForResult(intent, REQUEST_SETCOMPANY);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_adress:// 设置我的位置
			if (entity != null) {
				intent = new Intent(context, SetAdressActivity.class);
				intent.putExtra("city", entity.getUserInfo().getCity());
				intent.putExtra("hometown", entity.getUserInfo().getHometown());
				intent.putExtra("travelcity", entity.getUserInfo().getTravelCity());
				intent.putExtra("travelcityText", entity.getUserInfo().getTravelCityText());
				startActivityForResult(intent, REQUEST_SETADRESS);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_hobby:// 设置我的爱好
			if (entity != null) {
				intent = new Intent(context, SetHobbyActivity.class);
				intent.putExtra("hobbyId", entity.getUserInfo().getHobby());
				startActivityForResult(intent, REQUEST_SETHOBBY);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_dream:// 设置我的梦想
			if (entity != null) {
				intent = new Intent(context, SetDreamActivity.class);
				intent.putExtra("isSeting", true);
				intent.putExtra("dreamList", (Serializable) entity.getUserInfo().getDreamList());
				startActivityForResult(intent, REQUEST_SETDREAM);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_birthday:// 设置我的生日
			if (entity != null) {
				intent = new Intent(context, SetBirthdayActivity.class);
				intent.putExtra("birthday", entity.getUserInfo().getBirthday());
				intent.putExtra("birthdayZodiac", entity.getUserInfo().getBirthdayZodiac());
				intent.putExtra("constellation", entity.getUserInfo().getConstellation());
				intent.putExtra("zodiac", entity.getUserInfo().getZodiac());
				intent.putExtra("isbirthdayopen", entity.getUserInfo().getIsbirthdayopen());
				startActivityForResult(intent, REQUEST_SETBIRTHDAY);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_thought:// 设置价值观与信念
			if (entity != null) {
				intent = new Intent(context, SetThoughtActivity.class);
				intent.putExtra("isSeting", true);
				intent.putExtra("thoughtList", (Serializable) entity.getUserInfo().getValueList());
				startActivityForResult(intent, REQUEST_SETTHOUGHT);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_phone:// 设置我的手机contactway
			if (entity != null) {
				intent = new Intent(context, SetPhoneActivity.class);
				intent.putExtra("mobile", entity.getUserInfo().getMobile());
				intent.putExtra("ismobileopen", entity.getUserInfo()
						.getIsmobileopen());
				startActivityForResult(intent, REQUEST_SETPHONE);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_email:// 设置我的邮箱
			if (entity != null) {
				intent = new Intent(context, SetEmailActivity.class);
				intent.putExtra("email", entity.getUserInfo().getEmail());
				intent.putExtra("isemailopen", entity.getUserInfo()
						.getIsemailopen());
				startActivityForResult(intent, REQUEST_SETEMAIL);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_weixin:// 设置我的微信号
			if (entity != null) {
				intent = new Intent(context, SetWeixinActivity.class);
				intent.putExtra("weixin", entity.getUserInfo().getWeixin());
				intent.putExtra("isweixinopen", entity.getUserInfo()
						.getIsweixinopen());
				startActivityForResult(intent, REQUEST_SETWEIXIN);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_set_qq:// 设置我的qq号
			if (entity != null) {
				intent = new Intent(context, SetQqActivity.class);
				intent.putExtra("qq", entity.getUserInfo().getQq());
				intent.putExtra("isqqopen", entity.getUserInfo().getIsqqopen());
				startActivityForResult(intent, REQUEST_SETQQ);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.user_setting_bg:// 设置个性背景
			if (entity != null) {
				intent = new Intent(context, SetBgActivity.class);
				intent.putExtra("bannerUrl", entity.getUserInfo().getBannerUrl());
				startActivityForResult(intent, REQUEST_SETBG);
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		}
	}

	/**
	 * 
	 */
	private void saveData() {
		if (userChangedDataMap != null && userChangedDataMap.size() > 0) {
			UserStateUtil.loginInFailuer(context,new FailureCallBack() {
				
				@Override
				public void onFailureCallBack() {
					ShowUtils.showDiaLog(context, "温馨提示", "用户登录已经过期,需要您先重新登录,再保存设置!", new ConfirmBackCall() {
						
						@Override
						public void confirmOperation() {
							//跳转到登录界面
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
						}
					});
				}
			}, new SuccessCallBack() {
				
				@Override
				public void onSuccessCallBack() {
					saveDialog = ShowUtils.createLoadingDialog(context, true);
					saveDialog.show();
					setHttpRequestData(userId, getUserInfo());
				}
			});
		}else {
			ShowUtils.showMsg(context, "您没有修改的数据可以保存!");
		}
	}

	@Override
	protected void initListener() {
		super.initListener();
		ll_set_head.setOnClickListener(this);
		ll_set_name.setOnClickListener(this);
		ll_set_picture.setOnClickListener(this);
		ll_set_company.setOnClickListener(this);
		ll_set_adress.setOnClickListener(this);
		ll_set_hobby.setOnClickListener(this);
		ll_set_dream.setOnClickListener(this);
		ll_set_birthday.setOnClickListener(this);
		ll_set_thought.setOnClickListener(this);
		ll_set_phone.setOnClickListener(this);
		ll_set_email.setOnClickListener(this);
		ll_set_weixin.setOnClickListener(this);
		ll_set_qq.setOnClickListener(this);
		user_setting_bg.setOnClickListener(this);
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (userChangedDataMap != null && userChangedDataMap.size() > 0) {
        		ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
        			@Override
        			public void confirmOperation() {
        				UserSettingActivity.this.finish();
        			}
        		});
			}else {
				UserSettingActivity.this.finish();
			}
        	if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
				UserSettingActivity.this.finish();
			}
        	if (saveDialog != null && saveDialog.isShowing()) {
        		saveDialog.dismiss();
        		UserSettingActivity.this.finish();
        	}
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
	/**
	 * 发送http请求网络数据
	 */
	private void setHttpRequestData(String userId,String userInfo) {
		RequestParams params = new RequestParams();
		String url = null;
//		if (String.valueOf(SET_BGIMAGE).equals(type)) {
//			url = ConstantsOnline.UPDATE_USERBANNER;
//			params.addQueryStringParameter("userId", userId);
//			params.addQueryStringParameter("bannerUrl", bannerUrl);
//		}else if (String.valueOf(SET_USERCORD).equals(type)) {
//		}
		url = ConstantsOnline.UPDATE_USERINFO;
		params.addQueryStringParameter("userInfo", "{userId:"+userId+","+userInfo+"}");
		System.out.println("{userId:"+userId+","+userInfo+"}");
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (ProcessedData(responseInfo.result)) {
							handler.obtainMessage(1).sendToTarget();
						}else {
							handler.obtainMessage(2).sendToTarget();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
					}
				});
	}
	
	/**
	 * 利用xutils进行http请求数据
	 * @param httpMethod 请求类型
	 * @param url	请求地址
	 * @param params 参数
	 * @param callBack 请求回调
	 */
	public void getData(HttpMethod httpMethod, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils= new HttpUtils();
		httpUtils.send(httpMethod, url,params, callBack);
	}
	
	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	private boolean ProcessedData(String result) {
//		if (String.valueOf(SET_BGIMAGE).equals(type)) {
//			mSetBgMessageBean = GsonUtil.jsonToBean(result, CollectionMessageBean.class);
//			if (mSetBgMessageBean != null) {
//				return Boolean.valueOf(mSetBgMessageBean.success);
//			}else {
//				return false;
//			}
//		}else if (String.valueOf(SET_USERCORD).equals(type)) {
//		}else {
//			return false;
//		}
		mUserCordBeanBack = GsonUtil.jsonToBean(result, UserCordBean.class);
		if (mUserCordBeanBack != null) {
			return Boolean.valueOf(mUserCordBeanBack.getSuccess());
		}else {
			return false;
		}
	}
}
