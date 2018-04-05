package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.NoteBean;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 打赏好友成长币
 * 
 * @author Administrator
 * 
 */
public class AwardGCoinActivity extends TitleBaseActivity {
	private EditText et_ask;// 打赏留言
	private EditText et_coin;// 打赏成长币个数

	private ImageView course_image_item;// 课程图片
	private TextView course_name;// 课程名

	private RequestVo requestVo;
	private DataCallBack<NoteBean> dataCallBack;
	
	private TextView tv_name;// 索要笔记对象的姓名
	private RelativeLayout rl_username;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("打赏");
		setRightText("发送", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!StringUtil.isEmpty(et_ask.getText().toString().trim())) {
					if (!StringUtil.isEmpty(et_coin.getText().toString().trim())&& Integer.valueOf(et_coin.getText().toString().trim()) != 0) {
						getRequestVo();
						getDataCallBack();
						getDataServer(requestVo, dataCallBack);
					} else {
						ShowUtils.showMsg(context, "成长币数量要大于0哦~");
					}
				} else {
					ShowUtils.showMsg(context, "请填写打赏留言");
				}
			}
		});
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_awardgcoin, null);
		et_ask = (EditText) view.findViewById(R.id.et_ask);// 输入的打赏感言
		et_coin = (EditText) view.findViewById(R.id.et_coin);// 输入的打赏的成长币个数
		tv_name =  (TextView) view.findViewById(R.id.tv_name);
		rl_username = (RelativeLayout) view.findViewById(R.id.rl_username);
		course_image_item = (ImageView) view.findViewById(R.id.course_image_item);
		course_name = (TextView) view.findViewById(R.id.course_name);
		tv_name.setText(getIntent().getStringExtra("uname"));
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		rl_username.setOnClickListener(this);
	}

	@Override
	protected void initData() {

		String course_image=getIntent().getStringExtra("course_image");
		if (!StringUtil.isEmpty(course_image)) {
		ImageUtils.setImage(course_image, course_image_item, 0);
		course_name.setText(getIntent().getStringExtra("course_name"));// 课程名
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_username:
			Intent intent = new Intent(context, UserShowActivity.class);
			intent.putExtra("userId", getIntent().getStringExtra("tuid"));
			context.startActivity(intent);
			break;
		}
	}

	private void getRequestVo() {
		// 获取我的答疑相关的课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.awardGCoin(context,
				UserStateUtil.getUserId(), getIntent().getStringExtra("tuid"),
				et_coin.getText().toString().trim(), et_ask.getText().toString().trim(),getIntent().getStringExtra("recordId"));

		requestVo = new RequestVo(ConstantsOnline.AWARD_GCOIN, context,
				requestDataMap, new MyBaseParser<>(NoteBean.class));
	}

	private void getDataCallBack() {
		dataCallBack = new DataCallBack<NoteBean>() {

			@Override
			public void processData(NoteBean object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, "打赏成功");
						finish();
					} else {
						ShowUtils.showMsg(context, "请求失败，请稍后重试");
					}
				} else {
					ShowUtils.showMsg(context, "打赏失败，请稍后重试");
				}

			}
		};
	}
}