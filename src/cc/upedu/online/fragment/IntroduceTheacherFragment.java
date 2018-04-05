package cc.upedu.online.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.activity.TeacherVisitCard;
import cc.upedu.online.base.TwoPartModelTopNSVBaseFragment;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course.TeacherItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.view.CircleImageView;

/**
 * 课程介绍界面中的课程导师Fragment
 * 
 * @author Administrator
 * 
 */
public class IntroduceTheacherFragment extends TwoPartModelTopNSVBaseFragment {
	// 课程简介的导师图片
	private CircleImageView teacher_img;
	// 课程简介的导师名称
	private TextView teacher_name;
	// 课程简介的导师职称
	private TextView teacher_work;
	// 课程简介的导师描述信息
	private TextView teacher_doc;
	// 课程信息对象
	// private TeacherItem course;
	// 导师对象
	private TeacherItem teacherItem;

	@Override
	public View initTopLayout() {
		teacherItem = ((Course) PreferencesObjectUtil.readObject("IntroduceCourse", context)).getTeacherList().get(0);
		View view = View.inflate(context, R.layout.layout_introduce_teacher,null);
		teacher_img = (CircleImageView) view.findViewById(R.id.teacher_img);
		teacher_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (teacherItem != null) {
					Intent intent = new Intent(context, TeacherVisitCard.class);
					intent.putExtra("teacherId", teacherItem.getId());
					intent.putExtra("teacherPosition", teacherItem.getEducation());
					intent.putExtra("teacherName", teacherItem.getName());
					intent.putExtra("teacherLogo", teacherItem.getPicPath());
					context.startActivity(intent);
				} else {
					ShowUtils.showMsg(context, "请求的数据异常!");
				}
			}
		});
		teacher_name = (TextView) view.findViewById(R.id.teacher_name);
		teacher_work = (TextView) view.findViewById(R.id.teacher_work);
		return view;
	}

	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_teacherdoc_textview,null);
		teacher_doc = (TextView) view.findViewById(R.id.teacher_doc);
		return view;
	}
	@Override
	public void initData() {
		
		ImageUtils.setImage(teacherItem.getPicPath(), teacher_img, R.drawable.teacher_images);
		teacher_name.setText(teacherItem.getName());
		teacher_work.setText(teacherItem.getEducation());
		teacher_doc.setText(teacherItem.getCareer());
		// tv_introduce_teacher.loadDataWithBaseURL(null, extraData.getCareer(),
		// "text/html", "utf-8", null);
	}
}
