package cc.upedu.online.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelTopBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.LunarUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.factory.MyHorizontalIconTextItem;
import cc.upedu.online.view.wheelview.JudgeDate;
import cc.upedu.online.view.wheelview.MyAlertDialog;
import cc.upedu.online.view.wheelview.ScreenInfo;
import cc.upedu.online.view.wheelview.WheelMain;
/**
 * 设置我的生日
 * @author Administrator
 *
 */
public class SetBirthdayActivity extends TwoPartModelTopBaseActivity {
	public static final int RESULT_SETBIRTHDAY = 9;
	private String oldBirthday,newBirthday = "",oldBirthdayZodiac,newBirthdayZodiac = "",oldConstellationId,
				newConstellationId = "",oldZodiacId,newZodiacId = "",oldIsbirthdayopen,newIsbirthdayopen = "0";
	private TextView tv_birthday,tv_birthdayzodiac;
	private ImageView iv_constellation,iv_zodiac;
	private TextView tv_constellation,tv_zodiac;
//	private RadioGroup rg_jurisdiction;
	private RadioButton rb_friend,rb_all;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.remarks_birthday);
		return view;
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		oldBirthday = getIntent().getStringExtra("birthday");
		oldBirthdayZodiac = getIntent().getStringExtra("birthdayZodiac");
		oldConstellationId = getIntent().getStringExtra("constellation");
		oldZodiacId = getIntent().getStringExtra("zodiac");
		oldIsbirthdayopen = getIntent().getStringExtra("isbirthdayopen");
		
		View view = View.inflate(context, R.layout.activity_setbirthday, null);
		ll_birthday = (RelativeLayout) view.findViewById(R.id.ll_birthday);
		tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);
		if (!StringUtil.isEmpty(oldBirthday)) {
			newBirthday = oldBirthday;
			String[] split = oldBirthday.split("-");
			tv_birthday.setText(split[0]+"年"+split[1]+"月"+split[2]+"日");
		}else {
			oldBirthday = "";
			tv_birthday.setText("点击设置");
		}
		tv_birthdayzodiac = (TextView) view.findViewById(R.id.tv_birthdayzodiac);
		if (!StringUtil.isEmpty(oldBirthdayZodiac)) {
			newBirthdayZodiac = oldBirthdayZodiac;
			tv_birthdayzodiac.setText(oldBirthdayZodiac);
		}else {
			tv_birthdayzodiac.setText("未设置");
		}
		iv_constellation = (ImageView) view.findViewById(R.id.iv_constellation);
		tv_constellation = (TextView) view.findViewById(R.id.tv_constellation);
		if (!StringUtil.isEmpty(oldConstellationId)) {
			newConstellationId = oldConstellationId;
			List<String> constellationids = Arrays.asList(LunarUtils.constellationId);
			if (constellationids.contains(oldConstellationId)) {
				int indexOf = constellationids.indexOf(oldConstellationId);
				iv_constellation.setImageResource(LunarUtils.constellationLogo[indexOf]);
				tv_constellation.setText(LunarUtils.constellationContent[indexOf]);
			}else {
				iv_constellation.setImageResource(R.drawable.img_constellation);
				tv_constellation.setText("未知");
			}
		}else {
			iv_constellation.setImageResource(R.drawable.img_constellation);
			tv_constellation.setText("未设置");
		}
		iv_zodiac = (ImageView) view.findViewById(R.id.iv_zodiac);
		tv_zodiac = (TextView) view.findViewById(R.id.tv_zodiac);
		if (!StringUtil.isEmpty(oldZodiacId)) {
			newZodiacId = oldZodiacId;
			List<String> zodiacids = Arrays.asList(LunarUtils.zodiacId);
			if (zodiacids.contains(oldZodiacId)) {
				int indexOf = zodiacids.indexOf(oldZodiacId);
				iv_zodiac.setImageResource(LunarUtils.zodiacLogo[indexOf]);
				tv_zodiac.setText(LunarUtils.zodiacContent[indexOf]);
			}else {
				iv_zodiac.setImageResource(R.drawable.img_zodiac);
				tv_zodiac.setText("未知");
			}
		}else {
			iv_zodiac.setImageResource(R.drawable.img_zodiac);
			tv_zodiac.setText("未设置");
		}
		
//		rg_jurisdiction = (RadioGroup) view.findViewById(R.id.rg_jurisdiction);
		rb_friend = (RadioButton) view.findViewById(R.id.rb_friend);
		rb_all = (RadioButton) view.findViewById(R.id.rb_all);
		if (!StringUtil.isEmpty(oldIsbirthdayopen)) {
			if ("1".equals(oldIsbirthdayopen)) {
				rb_friend.setChecked(true);
				rb_all.setChecked(false);
			}else if ("0".equals(oldIsbirthdayopen)) {
				rb_friend.setChecked(false);
				rb_all.setChecked(true);
			}
		}else {
			oldIsbirthdayopen = "0";
			rb_friend.setChecked(false);
			rb_all.setChecked(true);
		}
		rb_friend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsbirthdayopen = "1";
					rb_all.setChecked(false);
				}
			}
		});
		rb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsbirthdayopen = "0";
					rb_friend.setChecked(false);
				}
			}
		});
//		rg_jurisdiction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
//            @Override  
//            public void onCheckedChanged(RadioGroup group, int checkedId)  
//            {
//            	if (checkedId == rb_friend.getId()) {
//            		newIsbirthdayopen = "1";
//				}else if (checkedId == rb_all.getId()) {
//					newIsbirthdayopen = "0";
//				}
//            }  
//        });
		return view;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的生日");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("birthday", newBirthday);
				intent.putExtra("birthdayZodiac", newBirthdayZodiac);
				intent.putExtra("constellation", newConstellationId);
				intent.putExtra("zodiac", newZodiacId);
				intent.putExtra("isbirthdayopen", newIsbirthdayopen);
				setResult(RESULT_SETBIRTHDAY, intent);
				finish();
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldBirthday.equals(newBirthday) || !oldIsbirthdayopen.equals(newIsbirthdayopen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetBirthdayActivity.this.finish();
						}
					});
				else
					SetBirthdayActivity.this.finish();
			}
		});
	}
	private RelativeLayout ll_birthday;
	@Override
	protected void initListener() {
		super.initListener();
		ll_birthday.setOnClickListener(this);
	}
	private WheelMain wheelMain;
	private boolean isShowWheel = false;
	private MyAlertDialog dialog;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		String time;
		switch (v.getId()) {
		case R.id.ll_birthday:
			LayoutInflater inflater = LayoutInflater.from(SetBirthdayActivity.this);
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(SetBirthdayActivity.this);
			wheelMain = new WheelMain(timepickerview,true);
			wheelMain.screenheight = screenInfo.getHeight();
			if (StringUtil.isEmpty(newBirthday)) {
				time = StringUtil.getStringDateShort();
				String[] strs = time.split("-");
				strs[0] = String.valueOf(Integer.valueOf(strs[0])-40);
				time = strs[0]+"-"+strs[1]+"-"+strs[2];
			}else {
				time = newBirthday.replace("年", "-").replace("月", "-").replace("日", "-");
			}
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
				try {
					calendar.setTime(StringUtil.mDateFormat.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year, month, day);
			dialog = new MyAlertDialog(SetBirthdayActivity.this)
					.builder()
//					.setTitle(tv_birthday.getText().toString().trim())
					// .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
					// .setEditText("1111111111111")
					.setView(timepickerview)
					.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							isShowWheel = !isShowWheel;
						}
					});
			dialog.setPositiveButton("保存", new OnClickListener() {
				@Override
				public void onClick(View v) {
					String birthday = wheelMain.getTime();
					newBirthday = birthday;
					String[] strs = newBirthday.split("-");
					tv_birthday.setText(strs[0]+"年"+strs[1]+"月"+strs[2]+"日");
					setBirthday(birthday);
					isShowWheel = !isShowWheel;
					dialog.dismiss();
				}
			});
			Window window = dialog.getWindow();  
		    window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置   
		    window.setWindowAnimations(R.style.mydatadialogstyle);
			isShowWheel = !isShowWheel;
			dialog.show();
			break;
		}
	}
	private void setBirthday(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);
			Date birthday = sdf.parse(date);
			Calendar vlue = Calendar.getInstance();
			vlue.setTime(birthday);
			LunarUtils lunar = new LunarUtils(vlue);
			String lunarYear = lunar.cyclical() + "年";
			String lunarBirthday = lunar.toString();
			newBirthdayZodiac = lunarYear + lunarBirthday;
			tv_birthdayzodiac.setText(newBirthdayZodiac);
			String zodiac = lunar.animalsYear();
			List<String> zodiaccontents = Arrays.asList(LunarUtils.zodiacContent);
			int indexOfzodiac = zodiaccontents.indexOf(zodiac);
			if (indexOfzodiac != -1) {
				int newZodiacLogo = LunarUtils.zodiacLogo[indexOfzodiac];
				newZodiacId = LunarUtils.zodiacId[indexOfzodiac];
				iv_zodiac.setImageResource(newZodiacLogo);
				tv_zodiac.setText(zodiac);
			}else {
				newZodiacId = "";
				tv_zodiac.setText("未知");
				iv_zodiac.setImageResource(R.drawable.img_zodiac);
			}
			int month = vlue.get(Calendar.MONTH);
			int day = vlue.get(Calendar.DATE);
			String constellation = LunarUtils.getConstellation(month, day);

			List<String> constellationcontents = Arrays.asList(LunarUtils.constellationContent);
			int indexOfconstellation = constellationcontents.indexOf(constellation);
			if (indexOfconstellation != -1) {
				int newconstellationLogo = LunarUtils.constellationLogo[indexOfconstellation];
				newConstellationId = LunarUtils.constellationId[indexOfconstellation];
				iv_constellation.setImageResource(newconstellationLogo);
				tv_constellation.setText(constellation);
			}else {
				newConstellationId = "";
				tv_constellation.setText("未知");
				iv_constellation.setImageResource(R.drawable.img_constellation);
			}
		} catch (ParseException e) {
			ShowUtils.showMsg(context, "日期解析失败,请重新设置正确的生日!");
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if (isShowWheel) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}else {
				if (!oldBirthday.equals(newBirthday) || !oldIsbirthdayopen.equals(newIsbirthdayopen)) {
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetBirthdayActivity.this.finish();
						}
					});
				}else {
					return super.onKeyDown(keyCode, event);
				}
			}
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
