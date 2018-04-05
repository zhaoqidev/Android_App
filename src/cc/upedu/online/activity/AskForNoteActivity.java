package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.NoteBean;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 索要笔记
 * 
 * @author Administrator
 * 
 */
public class AskForNoteActivity extends TitleBaseActivity {
	private EditText et_ask;
	private ImageView course_image_item;// 课程图片
	private TextView course_name;// 课程名
	private TextView tv_name;// 索要笔记对象的姓名
	private TextView tv_askorshare;// 分享或者索要
	private RelativeLayout rl_username;

	/**
	 * 区分是页面是分享笔记还是索要笔记 ask：索要笔记 share：分享笔记
	 */
	private String type;
	private RequestVo requestVo;
	private DataCallBack<NoteBean> dataCallBack;
	private Intent intent;

	@Override
	protected void initTitle() {
		setTitleText("");
		intent = getIntent();
		type = intent.getStringExtra("type");
		String title = null;
		if ("ask".equals(type)) {
			title = "索要笔记";
		} else if ("share".equals(type)) {
			title = "分享笔记";
		}
		setRightText(title, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!StringUtil.isEmpty(et_ask.getText().toString().trim())) {
					getRequestVo();
					getDataCallBack();
					getDataServer(requestVo, dataCallBack);
				}else {
					if ("ask".equals(type)) {
						ShowUtils.showMsg(context, "请填写向好友索要笔记的请求");
					} else if ("share".equals(type)) {
						ShowUtils.showMsg(context, "请填写向好友分享笔记的赠言");
					}
				}
			}
		});
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_askfor_note, null);
		et_ask = (EditText) view.findViewById(R.id.et_ask);// 输入的索要请求或者输入的分享感言
		course_image_item = (ImageView) view.findViewById(R.id.course_image_item);
		course_name = (TextView) view.findViewById(R.id.course_name);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_askorshare = (TextView) view.findViewById(R.id.tv_askorshare);
		rl_username=(RelativeLayout) view.findViewById(R.id.rl_username);
		return view;
	}

	@Override
	protected void initListener() {
		super.initListener();
		rl_username.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		if ("ask".equals(type)) {
			tv_askorshare.setText("索要");
			et_ask.setHint("请输入索要笔记请求");
		} else if ("share".equals(type)) {
			tv_askorshare.setText("分享");
			et_ask.setHint("请输入分享笔记赠言");
		} else {
			ShowUtils.showMsg(context, "数据传递有误，请返回上一界面重试");
		}
		tv_name.setText(intent.getStringExtra("tname"));
			if (!StringUtil.isEmpty(intent.getStringExtra("course_image"))) {

				OnlineApp.myApp.imageLoader.displayImage(
						ConstantsOnline.SERVER_IMAGEURL+intent.getStringExtra("course_image"), 
						course_image_item, 
						OnlineApp.myApp.builder.build());
				
			}
			course_name.setText(intent.getStringExtra("course_name"));// 课程名
		}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_username:
			Intent intent = new Intent(context, UserShowActivity.class);
			intent.putExtra("userId", intent.getStringExtra("tuid"));
			context.startActivity(intent);
			break;
		}
	}

	private void getRequestVo() {
		// 获取我的答疑相关的课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.askForNote(context,
				UserStateUtil.getUserId(), intent.getStringExtra("tuid"),
				intent.getStringExtra("course_id"), et_ask.getText().toString().trim());

		if ("ask".equals(type)) {
			requestVo = new RequestVo(ConstantsOnline.ASKFOR_NOTE, context,
					requestDataMap, new MyBaseParser<>(NoteBean.class));
		} else if ("share".equals(type)) {
			requestVo = new RequestVo(ConstantsOnline.SHARE_NOTE, context,
					requestDataMap, new MyBaseParser<>(NoteBean.class));
		} else {

		}
	}
	
	private void getDataCallBack(){
		dataCallBack=new DataCallBack<NoteBean>() {

			@Override
			public void processData(NoteBean object) {
				if(object!=null){
					if ("true".equals(object.success)) {
						if ("ask".equals(type)) {
							ShowUtils.showMsg(context, "索要笔记请求成功");
							finish();//请求成功，关闭页面
						} else if ("share".equals(type)) {
							ShowUtils.showMsg(context, "笔记分享成功");
							finish();//请求成功，关闭页面
						} else {

						}
					}else {
						ShowUtils.showMsg(context, "请求失败，请稍后重试");
					}
				}else {
					ShowUtils.showMsg(context, "数据获取失败，请稍后重试");
				}
					
				
			}
		};
	}
}