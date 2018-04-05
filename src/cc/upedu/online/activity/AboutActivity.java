package cc.upedu.online.activity;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;

/**
 * 关于成长吧
 * 
 * @author Administrator
 * 
 */
public class AboutActivity extends TitleBaseActivity {

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("关于成长吧");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.activity_about, null);
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}


}