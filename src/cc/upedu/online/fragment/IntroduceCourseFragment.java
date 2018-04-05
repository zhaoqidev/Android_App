package cc.upedu.online.fragment;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelTopNSVBaseFragment;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.WebViewSettingUtils;
/**
 * 课程介绍界面中的课程简介Fragment
 * @author Administrator
 *
 */
public class IntroduceCourseFragment extends TwoPartModelTopNSVBaseFragment {
	//课程简介的导师图片
	private ImageView course_image_item;
	//课程简介的课程名称
	private TextView course_item_name;
	//课程简介的课程导师名称
	private TextView teacher_Name;
	//课程简介的课程时长
	private TextView course_time;
	//课程简介的课程描述信息
	private WebView tv_introduce_course;
	
	private Course course;

	public IntroduceCourseFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		ImageUtils.setImage(course.getCourseLogo(), course_image_item, R.drawable.img_course);
		course_item_name.setText(course.getName());
		teacher_Name.setText(course.getTeacherList().get(0).getName());
		String timeStr = "";
		if (Integer.valueOf(course.getLessontimes()) < 60) {
			timeStr = course.getLessontimes() + "分钟";
		}else {
			timeStr = Integer.valueOf(course.getLessontimes())/60 + "小时" +Integer.valueOf(course.getLessontimes())%60 + "分钟";
		}
		course_time.setText(timeStr);
		if (StringUtil.isEmpty(tv_introduce_course.getUrl())) {
			tv_introduce_course.loadDataWithBaseURL(null, course.getContext(), "text/html", "utf-8", null);
		}
	}

	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		course=(Course) PreferencesObjectUtil.readObject("IntroduceCourse", context);
		View view = View.inflate(context, R.layout.layout_introduce_course, null);
		course_image_item = (ImageView) view.findViewById(R.id.course_image_item);
		course_item_name = (TextView) view.findViewById(R.id.course_item_name);
		teacher_Name = (TextView) view.findViewById(R.id.teacher_Name);
		course_time = (TextView) view.findViewById(R.id.course_time);
		return view;
	}

	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_webview, null);
		tv_introduce_course = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, tv_introduce_course);
		return view;
	}
}
