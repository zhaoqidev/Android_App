package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.UserCordBean;
import cc.upedu.online.domin.UserCordBean.Entity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.LunarUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ScreenUtil;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;
/**
 * 显示成长名片
 * @author Administrator
 *
 */
public class UserShowActivity extends TitleBaseActivity {
	private TextView tv_attention;
	// 名片背景图片,我的照片条目中预览的图片
	private ImageView user_card_bg,iv_attention,iv_show_constellation,iv_show_zodiac,
//			iv_dream_arrows,iv_thought_arrows,
			iv_dream_arrow,iv_thought_arrow,iv_user_stpe,
			iv_com_arrow,iv_pic_arrow;
	// 背景上的头像
	private CircleImageView iv_user_head;
	private RelativeLayout rl_show_dream,rl_show_thought;
	// 展示 我的照片 的条目布局,展示 我的企业 的条目布局
	private LinearLayout ll_show_birthday,ll_attention,ll_show_company;
	private RelativeLayout ll_show_picture;
	// 返回,标题,背景上的点赞,背景上的名称,背景上的职位,背景上的联系方式
	// 显示签名的文本,我的企业名称,我的位置,我的爱好 ,我的梦想,我的生日,价值观与信念,我的手机,我的微信号, 我的qq号
	private TextView praise_count, tv_user_name, tv_user_position,tv_show_picture,tv_userstpe_name,
			tv_user_contactway,tv_user_write,tv_show_signature,tv_show_company,tv_show_mycity,
			tv_show_hometown,tv_show_adress3, tv_show_hobby,tv_show_birthday,tv_show_birthdayzodiac,tv_show_constellation,tv_show_zodiac,
			tv_show_email,tv_show_weixin, tv_show_qq,tv_show_dream,tv_show_thought,tv_show_gender,tv_show_married;
	private UserCordBean mUserCordBean;
	private Entity entity;
	private String userId;
	//0：已关注 1：互相关注2：未关注
	private String attention;
	private GridView gv_show_picture;
	private MyGridViewAdapter adapter;
	private Dialog loadingDialog;
	private Dialog saveDialog;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mUserCordBean.getSuccess())) {
					setData2View();
				} else {
					ShowUtils.showMsg(context, mUserCordBean.getMessage());
				}
				break;
			}
		}
	};
	// 展示数据到界面
	private void setData2View() {
		entity = mUserCordBean.getEntity();
//		if (!StringUtil.isEmpty(entity.getUserInfo().getIsFriend())) {
//		}
		attention = entity.getIsFriend();
		loadingDialog.dismiss();
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
		// 头像
		if (!StringUtil.isEmpty(entity.getUserInfo().getAvatar())) {
			
			ImageUtils.setImage(entity.getUserInfo().getAvatar(), iv_user_head, R.drawable.left_menu_head);
		}else {
			iv_user_head.setImageResource(R.drawable.left_menu_head);
		}
		
		// 名称
		if (StringUtil.isEmpty(entity.getUserInfo().getName())) {
//			tv_user_name.setText("未设置用户名");
			tv_user_name.setText("");
		} else {
			tv_user_name.setText(entity.getUserInfo().getName());
		}
		// 职位
		String userPosition = "";
		if (entity.getUserInfo().getCompanyList().size() > 0) {
			userPosition = entity.getUserInfo().getCompanyList().get(0)
					.getPositionName();
			if (StringUtil.isEmpty(userPosition)) {
//				tv_user_position.setText("未设置职位");
				userPosition = "";
			}
		} else {
//			tv_user_position.setText("未设置职位");
			userPosition = "";
		}
		tv_user_position.setText(userPosition);
		// 联系方式
		if (StringUtil.isEmpty(entity.getUserInfo().getMobile())) {
//			tv_user_contactway.setText("未设置联系方式");
			tv_user_contactway.setText("");
			tv_user_contactway.setFocusable(false);
			tv_user_contactway.setClickable(false);
		} else {
			if ("0".equals(entity.getUserInfo().getIsmobileopen())) {
				tv_user_contactway.setText(entity.getUserInfo().getMobile());
			}else {
				if ("1".equals(entity.getUserInfo().getIsmobileopen()) && "1".equals(attention)) {
					tv_user_contactway.setText(entity.getUserInfo().getMobile());
				}else {
					tv_user_contactway.setText("未公开手机号");
					tv_user_contactway.setFocusable(false);
					tv_user_contactway.setClickable(false);
				}
			}
		}
		//用户类型
		if (!StringUtil.isEmpty(entity.getUserInfo().getUserType())) {
			switch (Integer.valueOf(entity.getUserInfo().getUserType())) {
			case 0:
				iv_user_stpe.setImageResource(R.drawable.user_stpe_student_write);
				tv_userstpe_name.setText("学生");
				break;
			case 1:
				iv_user_stpe.setImageResource(R.drawable.user_stpe_teacher_write);
				tv_userstpe_name.setText("导师");
				break;
			case 2:
				iv_user_stpe.setImageResource(R.drawable.user_stpe_counselor_write);
				tv_userstpe_name.setText("顾问");
				break;
			default:
				iv_user_stpe.setImageResource(R.drawable.user_stpe_student_write);
				tv_userstpe_name.setText("学生");
				break;
			}
		}else {
			iv_user_stpe.setImageResource(R.drawable.user_stpe_student_write);
			tv_userstpe_name.setText("学生");
		}
		//关注的状态
		if (StringUtil.isEmpty(attention)) {
			ll_attention.setVisibility(View.GONE);
		}else {
			ll_attention.setVisibility(View.VISIBLE);
			if ("2".equals(attention)) {
				iv_attention.setImageResource(R.drawable.usershow_noattention);
				tv_attention.setText(getResources().getString(R.string.name_noattention));
			}else {
				iv_attention.setImageResource(R.drawable.usershow_attention);
				tv_attention.setText(getResources().getString(R.string.name_attention));
			}
		}
		// 个性签名
		if (StringUtil.isEmpty(entity.getUserInfo().getIntro())) {
//			tv_show_signature.setText("未设置");
			tv_show_signature.setText("");
		} else {
			tv_show_signature.setText(entity.getUserInfo().getIntro());
		}
		// 企业
		if (entity.getUserInfo().getCompanyList().size() > 0) {
			if (entity.getUserInfo().getCompanyList().size() > 0 && StringUtil.isEmpty(entity.getUserInfo().getCompanyList().get(0)
					.getName())) {
//				tv_user_company.setText("未设置");
				tv_show_company.setText("");
				iv_com_arrow.setVisibility(View.INVISIBLE);
				ll_show_company.setFocusable(false);
				ll_show_company.setClickable(false);
			} else {
				iv_com_arrow.setVisibility(View.VISIBLE);
				if ("0".equals(entity.getUserInfo().getCompanyList().get(0).getIsnameopen())) {
//					tv_user_company.setText(entity.getUserInfo().getCompanyList().get(0).getName());
					tv_show_company.setText(entity.getUserInfo().getCompanyList()
							.get(0).getName());
				}else {
					tv_show_company.setText("未公开");
					ll_show_company.setFocusable(false);
					ll_show_company.setClickable(false);
				}
			}
		}else {
//			tv_user_company.setText("未设置");
			tv_show_company.setText("");
			iv_com_arrow.setVisibility(View.INVISIBLE);
			ll_show_company.setFocusable(false);
			ll_show_company.setClickable(false);
		}
		// 所在城市
		if (StringUtil.isEmpty(entity.getUserInfo().getCity())) {
//			tv_show_mycity.setText("未设置");
			tv_show_mycity.setText("");
		} else {
			tv_show_mycity.setText(entity.getUserInfo().getCity());
		}
		// 家乡位置
		if (StringUtil.isEmpty(entity.getUserInfo().getHometown())) {
//			tv_show_hometown.setText("未设置");
			tv_show_hometown.setText("");
		} else {
			tv_show_hometown.setText(entity.getUserInfo().getHometown());
		}
		// 往来城市
		if (StringUtil.isEmpty(entity.getUserInfo().getTravelCityText())) {
//			tv_show_adress3.setText("未设置");
			tv_show_adress3.setText("");
		} else {
			tv_show_adress3.setText(entity.getUserInfo().getTravelCityText());
		}
		// 性别
		String gender = "";
		switch (entity.getUserInfo().getGender()) {
		case "1":
			gender = "男";
			break;
		case "2":
			gender = "女";
			break;
		default:
			gender = "";
			break;
		}
		tv_show_gender.setText(gender);
		// 我的爱好
		String married = "";
		switch (entity.getUserInfo().getMarried()) {
		case "0":
			married = "未婚";
			break;
		case "1":
			married = "已婚";
			break;
		case "2":
			married = "就不告诉你";
			break;
		default:
			married = "";
			break;
		}
		tv_show_married.setText(married);
		// 我的爱好
		if (StringUtil.isEmpty(entity.getUserInfo().getHobbyText())) {
//			tv_show_hobby.setText("未设置");
			tv_show_hobby.setText("");
		} else {
			tv_show_hobby.setText(entity.getUserInfo().getHobbyText());
		}
		// 我的梦想
		if (entity.getUserInfo().getDreamList() != null && entity.getUserInfo().getDreamList().size() > 0 && !StringUtil.isEmpty(entity.getUserInfo().getDreamList().get(0))) {
			tv_show_dream.setText(entity.getUserInfo().getDreamList().get(0));
			iv_dream_arrow.setVisibility(View.VISIBLE);
		} else {
			tv_show_dream.setText("");
			iv_dream_arrow.setVisibility(View.INVISIBLE);
			rl_show_dream.setFocusable(false);
			rl_show_dream.setClickable(false);
		}
		
		
		// 我的生日
		if (StringUtil.isEmpty(entity.getUserInfo().getBirthday())) {
//			tv_show_birthday.setText("未设置");
			tv_show_birthday.setText("");
			ll_show_birthday.setVisibility(View.GONE);
		} else {
			if ("0".equals(entity.getUserInfo().getIsbirthdayopen())) {
				showBirthday();
			}else {
				if ("1".equals(entity.getUserInfo().getIsbirthdayopen()) && "1".equals(attention)) {
					showBirthday();
				}else {
					tv_show_birthday.setText("未公开");
					ll_show_birthday.setVisibility(View.GONE);
				}
			}
		}
		// 价值观和信念
		if (entity.getUserInfo().getValueList() != null && entity.getUserInfo().getValueList().size() > 0 && !StringUtil.isEmpty(entity.getUserInfo().getValueList().get(0))) {
			tv_show_thought.setText(entity.getUserInfo().getValueList().get(0));
			iv_thought_arrow.setVisibility(View.VISIBLE);
		} else {
			tv_show_thought.setText("");
			iv_thought_arrow.setVisibility(View.INVISIBLE);
			rl_show_thought.setFocusable(false);
			rl_show_thought.setClickable(false);
		}
		
		// 我的邮箱
		if (StringUtil.isEmpty(entity.getUserInfo().getEmail())) {
//			tv_show_email.setText("未设置");
			tv_show_email.setText("");
		} else {
			if ("0".equals(entity.getUserInfo().getIsemailopen())) {
				tv_show_email.setText(entity.getUserInfo().getEmail());
			}else {
				if ("1".equals(entity.getUserInfo().getIsemailopen()) && "1".equals(attention)) {
					tv_show_email.setText(entity.getUserInfo().getEmail());
				}else {
					tv_show_email.setText("未公开");
				}
			}
		}
		// 我的微信
		if (StringUtil.isEmpty(entity.getUserInfo().getWeixin())) {
//			tv_show_weixin.setText("未设置");
			tv_show_weixin.setText("");
		} else {
			if ("0".equals(entity.getUserInfo().getIsweixinopen())) {
				tv_show_weixin.setText(entity.getUserInfo().getWeixin());
			}else {
				if ("1".equals(entity.getUserInfo().getIsweixinopen()) && "1".equals(attention)) {
					tv_show_weixin.setText(entity.getUserInfo().getWeixin());
				}else {
					tv_show_weixin.setText("未公开");
				}
			}
		}
		// 我的qq
		if (StringUtil.isEmpty(entity.getUserInfo().getQq())) {
//			tv_show_qq.setText("未设置");
			tv_show_qq.setText("");
		} else {
			if ("0".equals(entity.getUserInfo().getIsqqopen())) {
				tv_show_qq.setText(entity.getUserInfo().getQq());
			}else {
				if ("1".equals(entity.getUserInfo().getIsqqopen()) && "1".equals(attention)) {
					tv_show_qq.setText(entity.getUserInfo().getQq());
				}else {
					tv_show_qq.setText("未公开");
				}
			}
		}
		// 我的照片
		if (entity.getUserInfo().getPicList().size() > 0) {
			gv_show_picture.setVisibility(View.VISIBLE);
			iv_pic_arrow.setVisibility(View.VISIBLE);
			tv_show_picture.setVisibility(View.INVISIBLE);
			List<PicItem> picList = new ArrayList<UserCordBean.Entity.UserInfo.PicItem>();
			if (entity.getUserInfo().getPicList().size() > 4) {
				for (int i = 0; i < 4; i++) {
					picList.add(entity.getUserInfo().getPicList().get(i));
				}
			}else {
				picList.addAll(entity.getUserInfo().getPicList());
			}
			if (adapter == null) {
				adapter = new MyGridViewAdapter(context, picList);
				gv_show_picture.setAdapter(adapter);
			}else {
				adapter.notifyDataSetChanged();
			}
			setGridViewHeightBasedOnChildren(context,gv_show_picture);
		}else {
			gv_show_picture.setVisibility(View.INVISIBLE);
			iv_pic_arrow.setVisibility(View.INVISIBLE);
			tv_show_picture.setVisibility(View.VISIBLE);
		}

		oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_USERCARD,userId,entity.getUserInfo().getName(),entity.getUserInfo().getAvatar(),false,null,
				entity.getUserInfo().getName()+"的成长名片\n个人职务"+userPosition);
	}
	/**
	 * 
	 */
	private void showBirthday() {
		String[] strings = entity.getUserInfo().getBirthday().split("-");
		tv_show_birthday.setText(strings[0]+"年"+strings[1]+"月"+strings[2]+"日");
		ll_show_birthday.setVisibility(View.VISIBLE);
		tv_show_birthdayzodiac.setText(entity.getUserInfo().getBirthdayZodiac());
		if (!StringUtil.isEmpty(entity.getUserInfo().getConstellation())) {
			String constellation = entity.getUserInfo().getConstellation();
			List<String> constellationids = Arrays.asList(LunarUtils.constellationId);
			int indexOfconstellation = constellationids.indexOf(constellation);
			if (indexOfconstellation != -1) {
				iv_show_constellation.setImageResource(LunarUtils.constellationLogo[indexOfconstellation]);
				tv_show_constellation.setText(LunarUtils.constellationContent[indexOfconstellation]);
			}else {
				tv_show_constellation.setText("未知");
				iv_show_constellation.setImageResource(R.drawable.img_constellation);
			}
		}
		if (!StringUtil.isEmpty(entity.getUserInfo().getZodiac())) {
			String zodiac = entity.getUserInfo().getZodiac();
			List<String> zodiacids = Arrays.asList(LunarUtils.zodiacId);
			int indexOfzodiac = zodiacids.indexOf(zodiac);
			if (indexOfzodiac != -1) {
				iv_show_zodiac.setImageResource(LunarUtils.zodiacLogo[indexOfzodiac]);
				tv_show_zodiac.setText(LunarUtils.zodiacContent[indexOfzodiac]);
			}else {
				tv_show_zodiac.setText("未知");
				iv_show_zodiac.setImageResource(R.drawable.img_zodiac);
			}
		}
	}
	public class MyGridViewAdapter extends BaseMyAdapter<PicItem> {
		public MyGridViewAdapter(Context context, List<PicItem> list) {
			super(context, list);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View picView = View.inflate(context, R.layout.gv_pic_item, null);
			final ImageButton picIBtn = (ImageButton) picView
					.findViewById(R.id.pic);
			
			ImageUtils.setImageToImageButton(list.get(position).getPicPath(), picIBtn, 0);
			
			return picView;
		}
	}
	private void setGridViewHeightBasedOnChildren(Context context,GridView gridView) { 

        ListAdapter gridAdapter = gridView.getAdapter();  

        if (gridAdapter == null) { 
            // pre-condition 
            return; 
        } 

        int totalHeight = 0; 

        View gridItem = gridAdapter.getView(0, null, gridView);
        gridItem.measure(0, 0); // 计算子项View 的宽高
        totalHeight += gridItem.getMeasuredHeight();
        
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        
        params.height = totalHeight; 

        gridView.setLayoutParams(params); 

    } 
	@Override
	protected void initData() {
		// 获取用户名片的数据
		String uid;
		if (userId.equals(UserStateUtil.getUserId())) {
			uid = null;
		}else {
			uid = UserStateUtil.getUserId();
		}
		Map<String, String> requestDataMap = ParamsMapUtil.getUserCard(context, userId,uid);
		RequestVo requestVo = new RequestVo(ConstantsOnline.USER_CARD, context,
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
		setRightButton(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				//分享个人名片
				showPopWin();
			}
		});
		
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_user_show, null);
		userId = getIntent().getStringExtra("userId");
		if (StringUtil.isEmpty(userId)){
			userId=getIntent().getData().getQueryParameter("userId");
		}
//		attention = getIntent().getStringExtra("attention");

		user_card_bg = (ImageView) view.findViewById(R.id.user_card_bg);
		ll_attention = (LinearLayout) view.findViewById(R.id.ll_attention);
		iv_attention = (ImageView) view.findViewById(R.id.iv_attention);
		tv_attention = (TextView) view.findViewById(R.id.tv_attention);
		
		int screenWidth = ScreenUtil.getInstance(context).getScreenWidth();
		LayoutParams para = user_card_bg.getLayoutParams();
        para.width = screenWidth;  
        para.height = screenWidth*542/960;
        user_card_bg.setLayoutParams(para);
		praise_count = (TextView) view.findViewById(R.id.praise_count);
		iv_user_head = (CircleImageView) view.findViewById(R.id.iv_user_head);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		tv_user_position = (TextView) view.findViewById(R.id.tv_user_position);
		tv_user_contactway = (TextView) view.findViewById(R.id.tv_user_contactway);
		tv_user_write = (TextView) view.findViewById(R.id.tv_user_write);
		iv_user_stpe = (ImageView) view.findViewById(R.id.iv_user_stpe);
		tv_userstpe_name = (TextView) view.findViewById(R.id.tv_userstpe_name);
//		tv_user_company = (TextView) view.findViewById(R.id.tv_user_company);

		tv_show_signature = (TextView) view.findViewById(R.id.tv_show_signature);
		ll_show_picture = (RelativeLayout) view.findViewById(R.id.ll_show_picture);
		tv_show_picture = (TextView) view.findViewById(R.id.tv_show_picture);
		gv_show_picture = (GridView) view.findViewById(R.id.gv_show_picture);
		iv_pic_arrow = (ImageView) view.findViewById(R.id.iv_pic_arrow);
		ll_show_company = (LinearLayout) view.findViewById(R.id.ll_show_company);
		tv_show_company = (TextView) view.findViewById(R.id.tv_show_company);
		iv_com_arrow = (ImageView) view.findViewById(R.id.iv_com_arrow);
		tv_show_mycity = (TextView) view.findViewById(R.id.tv_show_mycity);
		tv_show_hometown = (TextView) view.findViewById(R.id.tv_show_hometown);
		tv_show_adress3 = (TextView) view.findViewById(R.id.tv_show_adress3);
		tv_show_hobby = (TextView) view.findViewById(R.id.tv_show_hobby);
		tv_show_gender = (TextView) view.findViewById(R.id.tv_show_gender);
		tv_show_married = (TextView) view.findViewById(R.id.tv_show_married);
		rl_show_dream = (RelativeLayout) view.findViewById(R.id.rl_show_dream);
		tv_show_dream = (TextView) view.findViewById(R.id.tv_show_dream);
		iv_dream_arrow = (ImageView) view.findViewById(R.id.iv_dream_arrow);
		tv_show_birthday = (TextView) view.findViewById(R.id.tv_show_birthday);
		ll_show_birthday = (LinearLayout) view.findViewById(R.id.ll_show_birthday);
		tv_show_birthdayzodiac = (TextView) view.findViewById(R.id.tv_show_birthdayzodiac);
		iv_show_constellation = (ImageView) view.findViewById(R.id.iv_show_constellation);
		tv_show_constellation = (TextView) view.findViewById(R.id.tv_show_constellation);
		iv_show_zodiac = (ImageView) view.findViewById(R.id.iv_show_zodiac);
		tv_show_zodiac = (TextView) view.findViewById(R.id.tv_show_zodiac);
		rl_show_thought = (RelativeLayout) view.findViewById(R.id.rl_show_thought);
		tv_show_thought = (TextView) view.findViewById(R.id.tv_show_thought);
		iv_thought_arrow = (ImageView) view.findViewById(R.id.iv_thought_arrow);
		tv_show_email = (TextView) view.findViewById(R.id.tv_show_email);
		tv_show_weixin = (TextView) view.findViewById(R.id.tv_show_weixin);
		tv_show_qq = (TextView) view.findViewById(R.id.tv_show_qq);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		tv_user_contactway.setOnClickListener(this);
		if (View.VISIBLE == ll_attention.getVisibility()) {
			ll_attention.setOnClickListener(this);
		}
		tv_user_write.setOnClickListener(this);
		ll_show_picture.setOnClickListener(this);
		ll_show_company.setOnClickListener(this);
		rl_show_dream.setOnClickListener(this);
		rl_show_thought.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.praise_count:// 点赞

			break;
		case R.id.ll_show_picture:// 展示我的照片
			if (entity != null) {
				if (entity.getUserInfo().getPicList().size() > 0) {
					intent = new Intent(context, ShowPictureActivity.class);
					intent.putExtra("picList", (Serializable) entity.getUserInfo().getPicList());
					startActivity(intent);
				}
			} else {
				ShowUtils.showMsg(context, "用户数据获取失败,请检查网络重新加载!");
			}
			break;
		case R.id.ll_show_company:// 展示我的企业
			intent = new Intent(context, ShowCompanyDetailsActivity.class);
			intent.putExtra("companyList", (Serializable) entity.getUserInfo().getCompanyList());
			startActivity(intent);
			break;
		case R.id.rl_show_dream:
			intent = new Intent(context, SetDreamActivity.class);
			intent.putExtra("isSeting", false);
			intent.putExtra("dreamList", (Serializable) entity.getUserInfo().getDreamList());
			startActivity(intent);
			break;
		case R.id.rl_show_thought:
			intent = new Intent(context, SetThoughtActivity.class);
			intent.putExtra("isSeting", false);
			intent.putExtra("thoughtList", (Serializable) entity.getUserInfo().getValueList());
			startActivity(intent);
			break;
		case R.id.tv_user_contactway:
			if ("0".equals(entity.getUserInfo().getIsmobileopen()) || ("1".equals(entity.getUserInfo().getIsmobileopen()) && "1".equals(attention))) {
				callPhone(entity.getUserInfo().getMobile());
			}
			break;
		case R.id.ll_attention:
			if (!isDelaying) {
	        	isDelaying = !isDelaying;
	        	if (saveDialog == null) {
	        		saveDialog = ShowUtils.createLoadingDialog(context, true);
				}
	        	saveDialog.show();
				MyCount mc = new MyCount(3000, 1000);  
				mc.start();
				if ("2".equals(attention)) {//加关注
					getAttentionData();
				}else {//取消关注
					getDeleteData();
				}
			}else {
	        	ShowUtils.showMsg(context, "不能频繁操作!");
			}
			break;
		case R.id.tv_user_write://写信
			//先检测是否登录
			/*站内信
			if (UserStateUtil.isLogined()) {
				intent = new Intent(context, PrivateLetterActivity.class);
				intent.putExtra("uid", UserStateUtil.getUserId());
				intent.putExtra("fid", userId);
				intent.putExtra("name", entity.getUserInfo().getName());
				startActivity(intent);
			}else{
				UserStateUtil.NotLoginDialog(context);
			}*/
			
			//即时通讯
			if (UserStateUtil.isLogined()) {
				/*intent = new Intent(context, ConversationActivity.class);
				intent.putExtra("targetId", userId);
				intent.putExtra("userName", entity.getUserInfo().getName());
				intent.putExtra("userAvatar", entity.getUserInfo().getAvatar());
				startActivity(intent);*/
				
				RongIM.getInstance().startPrivateChat(context, userId, entity.getUserInfo().getName()); 
			}else{
				UserStateUtil.NotLoginDialog(context);
			}
			break;
		}
	}
	private boolean isDelaying = false;
	private OnekeyShare oks;
	/*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {
        	isDelaying = !isDelaying;
        }     
        @Override     
        public void onTick(long millisUntilFinished) {
        }    
    }

	/**
	 * 跳转到系统拨号界面
	 * @param phoneNum 要拨打的电话号
	 */
	private void callPhone(String phoneNum) {
		// TODO Auto-generated method stub
		 if(!StringUtil.isEmpty(phoneNum)) {
			 Intent intent = new  Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNum));
			 startActivity(intent);
		 }


	}

	/**
	 * 发送http请求网络数据
	 */
	private void getAttentionData() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", UserStateUtil.getUserId());
		params.addQueryStringParameter("fid", userId);
		
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.ATTENTION_OTHERS, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
					
						ProcessedData(responseInfo.result);
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
	private void ProcessedData(String result) {
		Attention attentionBean = GsonUtil.jsonToBean(result, Attention.class);
		if (attentionBean!=null) {
			ShowUtils.showMsg(context, attentionBean.message);
			if ("true".equals(attentionBean.success)) {
				//holder.tv_attention.setText("已关注");
				iv_attention.setImageResource(R.drawable.usershow_attention);
				tv_attention.setText(getResources().getString(R.string.name_attention));
				attention = attentionBean.entity.isFriend;
			}
		}else {
			ShowUtils.showMsg(context, "关注学友失败，请稍后重试");
		}
		saveDialog.dismiss();
	}
	/**
	 * 发送http请求网络数据
	 */
	private void getDeleteData() {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", UserStateUtil.getUserId());
		params.addQueryStringParameter("fid", userId);
		
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.DELETE_MY_ATTENTION, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//请求成功解析数据
						Attention bean = GsonUtil.jsonToBean(responseInfo.result, Attention.class);
						if (bean!=null) {
							if (Boolean.valueOf(bean.success)) {
								attention = "2";
								iv_attention.setImageResource(R.drawable.usershow_noattention);
								tv_attention.setText(getResources().getString(R.string.name_noattention));
							}
							ShowUtils.showMsg(context, bean.message);
						}else {
							ShowUtils.showMsg(context, "取消关注失败，请稍后重试");
						}
						saveDialog.dismiss();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
					}
				});
	}
	private class Attention{
		public String message;
		public String success;
		public Entity entity;
		public class Entity{
			public String isFriend;
		}
	}

	private PopupWindow pop;
	private void showPopWin() {
		if (pop == null) {
			View view = View.inflate(context, R.layout.layout_represent_popwindow, null);
			TextView tv_share_qr= (TextView) view.findViewById(R.id.tv_share_qr);
			tv_share_qr.setText("分享用户二维码名片");
			TextView tv_share= (TextView) view.findViewById(R.id.tv_share);
			tv_share.setText("直接分享用户名片");
			pop = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			pop.setFocusable(true); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
			pop.setTouchable(true);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0x1e000000);
			pop.setBackgroundDrawable(dw);
			// 设置popWindow的显示和消失动画
			pop.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			pop.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					// 这里如果返回true的话，touch事件将被拦截
					// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});

			// 点击事件监听
			view.findViewById(R.id.ll_qrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到分享二维码界面
					Intent intent = new Intent(context, ShareQRCodeActivity.class);
					intent.putExtra("userId", userId);
					intent.putExtra("type", 3);
					startActivity(intent);
					pop.dismiss();
				}
			});
			view.findViewById(R.id.ll_noqrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到直接分享用户名片
					if (oks != null) {
						oks.show(context);
					}else {
						ShowUtils.showMsg(context, "数据错误,请反馈信息,谢谢!");
					}
					pop.dismiss();
				}
			});
		}
		// 设置好参数之后再show
		pop.showAtLocation(this.findViewById(R.id.iv_pic_arrow), Gravity.BOTTOM, 0, 0);
	}
}
