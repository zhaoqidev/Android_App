


package cc.upedu.online.utils;

import android.content.Context;

import java.util.Map;

/**
 * 请求参数工具类
 * 
 * @author 
 * 
 */
public class ParamsMapUtil extends BaseParamsMapUtil {
	/**
	 * 收藏夹 请求接口参数
	 * @param context 上下文
	 * @param page 页
	 * @param pageNum 每页多少个
	 * @return
	 */
	public static Map<String, String> getFavorite(Context context,String page,String pageNum) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("page", page);
		paramsMap.put("pageNum",pageNum);
		return paramsMap;
	}
	/**
	 * 发送短信验证码的请求
	 * @param context
	 * @param mobile
	 * @return
	 */
	public static Map<String, String> sendSms(Context context,String mobile,String forgetpwd) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("mobile", mobile);
		paramsMap.put("forgetpwd", forgetpwd);
		return paramsMap;
	}
	
	/**
	 * 用户注册请求接口参数
	 * @param context	上下文
	 * @param mobile	手机号
	 * @param username	用户名
	 * @param password	密码
	 * @return
	 */
	public static Map<String, String> setUserInfo(Context context,String mobile,String username,String password,String brand,String type,String size,String system,String courseId,String shareUid,String randomCode) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("mobile", mobile);
		paramsMap.put("name", username);
		paramsMap.put("password",password);
		paramsMap.put("brand",brand);//品牌
		paramsMap.put("type",type);//型号
		paramsMap.put("size",size);//屏幕尺寸
		paramsMap.put("courseId",courseId);//分享的课程的Id
		paramsMap.put("shareUid",shareUid);//分享人的ID
		paramsMap.put("system", system);
		paramsMap.put("randomCode", randomCode);
		return paramsMap;
	}
	
	/**
	 * 用户登录请求接口参数
	 * @param context	上下文
	 * @param username	用户名
	 * @param password	密码
	 * @return
	 */
	public static Map<String, String> getLoginInfo(Context context,String username,String password,String brand,String type,String size,String system,String courseId,String shareUid) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("account", username);
		paramsMap.put("password",password);
		paramsMap.put("brand",brand);//品牌
		paramsMap.put("type",type);//型号
		paramsMap.put("size",size);//屏幕尺寸
		paramsMap.put("courseId",courseId);//分享的课程的Id
		paramsMap.put("shareUid",shareUid);//分享人的ID
		paramsMap.put("system", system);
		
		
		return paramsMap;
	}
	/**
	 * 搜索
	 * @param context
	 * @param keyword
	 * @param page
	 * @param pageNum
	 * @return
	 */
	public static Map<String, String> getSearch(Context context,String keyword,String page,String pageNum) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("keyword", keyword);
		paramsMap.put("page", page);
		paramsMap.put("pageNum",pageNum );
		return paramsMap;
	}
	public static Map<String, String> getSearch(Context context,String page,String pageNum) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("page", page);
		paramsMap.put("pageNum",pageNum );
		return paramsMap;
	}
	/**
	 * 轮播图
	 * @param context
	 * @param keyword 
	 * @return
	 */
	public static Map<String, String> getRollView(Context context,String keyword) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("keyword", keyword);
		return paramsMap;
	}
	/**
	 * 体系
	 * @param context
	 * @return
	 */
	public static Map<String, String> getARCHITECTURE(Context context,String level,String isAllShow) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("level", level);
		paramsMap.put("isAllShow", isAllShow);
		return paramsMap;
	}
	/**
	 * 公告
	 * @param context
	 * @return
	 */
	public static Map<String, String> getIndexNotice(Context context,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 公告
	 * @param context
	 * @return
	 */
	public static Map<String, String> getRecommendCourse(Context context,String recommendId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("recommendId", recommendId);
		return paramsMap;
	}
	/**
	 * 获取所有课程
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getAllCourse(Context context,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 获取所有课程
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getHomeCourse(Context context) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 获取同学感悟
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getCourseAssess(Context context,String courseId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取单个体系/分类的课程
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getSubjectCourse(Context context, String subjectId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("subjectId", subjectId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 获取课程学习中的笔记列表界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getCourseNotice(Context context, String currentPage, String courseId,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 获取课程中某个章节的个人笔记
	 * @param context
	 * @param courseId
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getShowCourseNotice(Context context, String courseId, String kpointId,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		if (!StringUtil.isEmpty(kpointId)) {
			paramsMap.put("kpointId", kpointId);
		}
		paramsMap.put("courseId", courseId);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 获取课程学习中的课程目录界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getCourseCatalog(Context context, String courseId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		return paramsMap;
	}
	/**
	 * 获取课程学习中的视频答疑界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getVideoAnswer(Context context, String currentPage, String courseId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取课程学习中的文字答疑界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getTextAnswer(Context context, String courseId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取我的收藏界面中的收藏课程界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getCollectCoure(Context context, String userId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取我的收藏界面中的收藏文章界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getCollectArticle(Context context, String userId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取课程学习中的问题界面
	 * @param context
	 * @param currentPage
	 * @param kpointId
	 * @return
	 */
	public static Map<String, String> getCourseQuestion(Context context, String currentPage, String courseId, String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取单个体系/分类的导师
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getArchitectureTeacher(Context context, String subjectId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("subjectId", subjectId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 获取下拉列表的数据
	 * @param context
	 * @param level
	 * @param isAllShow
	 * @return
	 */
	public static Map<String, String> getDownMenu(Context context,String level,String isAllShow) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("level", level);
		paramsMap.put("isAllShow", isAllShow);
		return paramsMap;
	}
	
	/**
	 * 用户忘记密码请求接口参数
	 * @param context	上下文
	 * @param mobile	手机号
	 * @return
	 */
	public static Map<String, String> getFindPwd(Context context,String mobile,String randomCode) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("mobile", mobile);
		paramsMap.put("randomCode", randomCode);
		return paramsMap;
	}
	
	/**
	 * 重置密码请求接口参数
	 * @param context	上下文
	 * @param mobile	电话
	 * @param newPwd	密码
	 * @return
	 */
	public static Map<String, String> resetPassword(Context context,String mobile,String newPwd) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("mobile", mobile);
		paramsMap.put("newPwd",newPwd);
		return paramsMap;
	}
	/**
	 * 修改密码的接口
	 * @param context
	 * @param userId
	 * @param newPwd
	 * @return
	 */
	public static Map<String, String> alterPassword(Context context,String userId,String newPwd) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("newPwd",newPwd);
		return paramsMap;
	}
	/**
	 * 用户名片
	 * @param context
	 * @param keyword 
	 * @return
	 */
	public static Map<String, String> getUserCard(Context context,String userId,String uid) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		if (!StringUtil.isEmpty(uid)) {
			paramsMap.put("uid", uid);
		}
		return paramsMap;
	}
	/**
	 * 获取课程体系界面的首页信息
	 * @param context
	 * @param subjectId
	 * @return
	 */
	public static Map<String, String> getCourseArchitecture(Context context,
			String subjectId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("subjectId", subjectId);
		return paramsMap;
	}
	/**
	 * 获取课程体系界面的首页信息
	 * @param context
	 * @param subjectId
	 * @return
	 */
	public static Map<String, String> getCourseIntroduce(Context context,String courseId,String userId,String system) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("courseId", courseId);
		paramsMap.put("userId", userId);
		paramsMap.put("system", system);
		return paramsMap;
	}
	
	/**
	 * 获取课程体系界面的首页信息
	 * @param context
	 * @param subjectId
	 * @return
	 */
	public static Map<String, String> getLiveFree(Context context,String courseId,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("liveId", courseId);
		return paramsMap;
	}
	public static Map<String, String> getCourseUserLearningRecords(Context context,
			String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的课程
	 * @param context 上下文
	 * @param userId 用户ID
	 * @return
	 */
	public static Map<String, String> getMyCourse(Context context,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 我的笔记中我的笔记课程列表/他人笔记课程列表/分享的笔记课程列表
	 * @param context
	 * @param userId
	 * @param type
	 * @return
	 */
	public static Map<String, String> getMyNoteCourse(Context context,String userId,String type,String currentPage,String pageSize) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", userId);
		paramsMap.put("type", type);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", pageSize);
		return paramsMap;
	}
	/**
	 * 我的笔记中某个课程的布局列表
	 * @param context
	 * @param userId
	 * @param type
	 * @return
	 */
	public static Map<String, String> getMyNoteOneCourse(Context context,String userId,String courseId,String type,String currentPage,String pageSize) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", userId);
		paramsMap.put("courseId", courseId);
		paramsMap.put("type", type);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", pageSize);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的笔记中索要笔记的记录
	 * @param context 上下文
	 * @param userId 用户ID
	 * @return
	 */
	public static Map<String, String> getMyNoteRecord(Context context,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", userId);
		return paramsMap;
	}
	/**
	 * 智库，GradeView
	 * @param context
	 * @param level	分类级别
	 * @param isAllShow	1:取得所有分类 0:取得部分分类
	 * @return
	 */
	public static Map<String, String> getZhiKu(Context context,String level,String isAllShow) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("level", level);
		paramsMap.put("isAllShow", isAllShow);
		return paramsMap;
	}
	/**
	 * 发现，导师列表
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getDaoshi(Context context, String currentPage,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("content", content);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 发现，导师列表
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getCourseDaoshi(Context context, String subjectId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("subjectId", subjectId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 学友
	 * @param context
	 * @param type
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> schoolMate(Context context,String type, String userId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 行业学友
	 * @param context
	 * @param type
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> peerSchoolMate(Context context,String type, String userId, String currentPage,String otherCode) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("otherCode", otherCode);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 行业学友
	 * @param context
	 * @param type
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> searchSchoolMate(Context context,String type, String userId, String currentPage,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("content", content);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 同城
	 * @param context
	 * @param type
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> otherCitySchoolMate(Context context,String type, String userId, String currentPage,String otherCity) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("otherCity", otherCity);
		return paramsMap;
	}
	/**
	 * 侧拉栏--我的代言
	 * @param context
	 * @param level	分类级别
	 * @param isAllShow	1:取得所有分类 0:取得部分分类
	 * @return
	 */
	public static Map<String, String> getMyRepresent(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 导师名片页面
	 * @param context
	 * @param teacherId 导师ID
	 * @return
	 */
	public static Map<String, String> DaoshiCard(Context context, String teacherId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("teacherId", teacherId);
		return paramsMap;
	}
	/**
	 * 导师课程列表页面
	 * @param context
	 * @param teacherId 导师ID
	 * @param currentPage 
	 * @return
	 */
	public static Map<String, String> DaoshiCouse(Context context, String teacherId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("teacherId", teacherId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	
	/**
	 * 导师寄语列表页面
	 * @param context
	 * @param teacherId 导师ID
	 * @param currentPage 
	 * @return
	 */
	public static Map<String, String> DaoshiWishes(Context context, String teacherId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("teacherId", teacherId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	
	/**
	 * 导师寄语界面，发送新的寄语
	 * @param context 
	 * @param teacherId 导师ID
	 * @param userId 用户ID
	 * @param content 寄语内容
	 * @return
	 */
	public static Map<String, String> newWishes(Context context, String teacherId,String userId,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("teacherId", teacherId);
		paramsMap.put("userId", userId);
		paramsMap.put("content", content);
		return paramsMap;
	}
	
	/**
	 * 导师文章列表页面
	 * @param context
	 * @param teacherId 导师ID
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> DaoshiArticle(Context context, String teacherId,String currentPage,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("teacherId", teacherId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 智库文章列表界面
	 * @param context
	 * @param subjectId 课程ID
	 * @param currentPage 
	 * @return
	 */
	public static Map<String, String> ZhikuArticle(Context context, String subjectId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("subjectId", subjectId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	

	/**
	 * 活动页面全部活动
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> AllSport(Context context,String currentPage,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("content", content);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 活动页面同城，其他城市活动
	 * @param context
	 * @param city
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> otherCitySport(Context context,String currentPage,String city) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("city", city);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 活动页面，同城活动
	 * @param context
	 * @param city
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> oneCitySport(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "50");
		return paramsMap;
	}
	/**
	 * 活动详情
	 * @param context
	 * @param aid 活动ID
	 * @param uid 用户ID
	 * @return
	 */
	public static Map<String, String> SportDetail(Context context,String aid,String uid) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("aid", aid);
		paramsMap.put("uid", uid);
		return paramsMap;
	}
	/**
	 * 活动详情页面，学友列表
	 * @param context
	 * @param aid
	 * @param uid
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> SportMate(Context context,String aid,String uid,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("aid", aid);
		paramsMap.put("uid", uid);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 发布更新活动
	 * @param context
	 * @param activity
	 * @return
	 */
	public static Map<String, String> SportIssue(Context context,String activity) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("activity", activity);
		return paramsMap;
	}
	
	/**
	 * 侧拉栏，我的活动的接口
	 * @param context
	 * @param userId
	 * @param type
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> MySport(Context context,String userId,String type,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("type", type);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的消息接口
	 * @param context
	 * @param userId 用户ID
	 * @param currentPage 
	 * @return
	 */
	public static Map<String, String> myMessage(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", "30");
		
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的钱包接口
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Map<String, String> myWallet(Context context,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		return paramsMap;
	}

	/**
	 * 侧拉栏，我的钱包收支明细接口--全部		
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myWalletUserAll(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		//paramsMap.put("ym", ym);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的钱包收支明细接口--按月查询	
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @param ym
	 * @return
	 */
	public static Map<String, String> myWalletUserTime(Context context,String userId,String ym,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("ym", ym);
		return paramsMap;
	}
	
	/**
	 * 侧拉栏，我的钱包, 导师和代理商收入明细按时间查询
	 * @param context
	 * @param userId
	 * @param ym
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myWalletDaoshiAndAgentTime(Context context,String userId,String ym,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("ym", ym);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	
	
	/**
	 * 侧拉栏，我的钱包, 导师收入明细按月份
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @param ym
	 * @return
	 */
	public static Map<String, String> myWalletDaoshiTime(Context context,String userId,String ym,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("ym", ym);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的钱包, 导师收入明细 ,全部
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myWalletDaoshiAll(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 侧拉栏，我的钱包, 代理商收入明细 ,按月份
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @param ym
	 * @return
	 */
	public static Map<String, String> myWalletAgentTime(Context context,String userId,String ym,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("ym", ym);
		return paramsMap;
	}
	/**
	 *
	 * 侧拉栏，我的钱包, 代理商收入明细 ,按月份
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @param ym
	 * @return
	 */
	public static Map<String, String> myWalletAgentAll(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 全部成长币
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myWalletCoinAll(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		//paramsMap.put("ym", ym);
		return paramsMap;
	}
	/**
	 * 按月份查询成长币
	 * @param context
	 * @param userId
	 * @param ym
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myWalletCoinTime(Context context,String userId,String ym,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("ym", ym);
		return paramsMap;
	}
	/**
	 * 查询收入统计信息
	 * @param context
	 * @param userId 用户ID
	 * @param ym 查询区间
	 * @param querytype 方案查询类型
	 * @param ymtype 区间查询类型
	 * @return
	 */
	public static Map<String, String> getIncomeChart(Context context,String userId,String ym,String querytype,String ymtype) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("ym", ym);
		paramsMap.put("querytype", querytype);
		paramsMap.put("ymtype", ymtype);
		return paramsMap;
	}
	/**
	 * 侧拉栏，已关注的学友
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> mySchoolMate(Context context, String userId, String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 侧拉栏，删除我的消息的接口
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> deleteMySchoolMate(Context context, String msgId, String type) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);//消息类型（1：网校 2：社区)
		paramsMap.put("msgId", msgId);
		return paramsMap;
	}
	/**
	 * 发送站内信
	 * @param context
	 * @param uid 自己的ID
	 * @param fid 好友的ID
	 * @param content 站内信内容
	 * @return
	 */
	public static Map<String, String> sendMessage(Context context, String uid, String fid,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", uid);
		paramsMap.put("fid", fid);
		paramsMap.put("content", content);
		return paramsMap;
	}
	/**
	 * 赠送成长币
	 * @param context
	 * @param userId 自己的Id
	 * @param amount 赠送数量
	 * @param friendId 朋友的Id
	 * @return
	 */
	public static Map<String, String> sendCoin(Context context, String userId, String amount,String friendId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("amount", amount);
		paramsMap.put("friendId", friendId);
		return paramsMap;
	}
	public static Map<String, String> getMyShopping(Context context,String userId,String goodsid) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("goodsid", goodsid);
		return paramsMap;
	}
	public static Map<String, String> getMyShopping(Context context,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 选择行业好友的筛选条件
	 * @param context
	 * @param type
	 * 			基础数据类型
	 *			1：兴趣爱好
 	 *			2：职务
	 *			3：企业所属行业
	 *			4：属相 
	 *			5：星座
	 * 
	 * @return
	 */
	public static Map<String, String> basicData(Context context,String type) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("type", type);
		return paramsMap;
	}
	
	/**
	 * 确认订单
	 * @param context
	 * @param userId 用户Id
	 * @param courseId 课程Id
	 * @param payType 支付类型
	 * @param gcoin 成长币个数 ,没有不传
	 * @return
	 */
	public static Map<String, String> confrimOrder(Context context,String userId,String courseId,String payType,String gcoin) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("courseId", courseId);
		paramsMap.put("payType", payType);
		paramsMap.put("gcoin", gcoin);
		return paramsMap;
	}
	
	/**
	 * 支付前检测
	 * @param context
	 * @param userId
	 * @param orderId
	 * @param payType
	 * @return
	 */
	public static Map<String, String> checkOrder(Context context,String userId,String orderId,String payType) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("orderId", orderId);
		paramsMap.put("payType", payType);
		return paramsMap;
	}
	
	/**
	 * 我的订单列表接口
	 * @param context
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> myOrder(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 支付成功回调接口
	 * @param context
	 * @param userId 用户Id
	 * @param totalFee 支付的总金额
	 * @param orderNo 订单号
	 * @param outTradeNo 传给支付宝的外部订单号
	 * @return
	 */
	public static Map<String, String> paySuccess(Context context,String userId,String totalFee,String orderNo,String outTradeNo) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("totalFee", totalFee);
		paramsMap.put("orderNo", orderNo);
		paramsMap.put("outTradeNo", outTradeNo);
		return paramsMap;
	}
	/**
	 * 取消订单
	 * @param context
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public static Map<String, String> cancelOrder(Context context,String userId,String orderId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("orderId", orderId);
		return paramsMap;
	}
	
	/**
	 * 账户充值
	 * @param context
	 * @param userId 用户Id
	 * @param amount 充值金额
	 * @param payType 支付类型
	 * @return
	 */
	public static Map<String, String> recharge(Context context,String userId,String amount,String payType) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("amount", amount);
		paramsMap.put("payType", payType);
		return paramsMap;
	}
	
	/**
	 * App检查更新
	 * @param context
	 * @param kTtype
	 * @return
	 */
	public static Map<String, String> updataApp(Context context,String kType) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("kType", kType);
		return paramsMap;
	}
	/**
	 * 获取商品列表
	 * @param context
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> getMicroMallGoods(Context context,String currentPage,String pageSize) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", pageSize);
		return paramsMap;
	}
	
	/**
	 * 获取收货地址列表
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Map<String, String> getMyReceivingAddress(Context context,String userId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		return paramsMap;
	}
	/**
	 * 获取收货地址列表
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Map<String, String> getExchangeRecord(Context context,String userId,String currentPage) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("currentPage", currentPage);
		return paramsMap;
	}
	/**
	 * 课程(包括直播)中的学友列表
	 * @param context
	 * @param userId
	 * @param courseId
	 * @param currentPage
	 * @return
	 */
	public static Map<String, String> schoolMateCourse(Context context,String userId, String courseId, String currentPage, String pageSize,String type,String otherCode,String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("userId", userId);
		paramsMap.put("courseId", courseId);
		paramsMap.put("currentPage", currentPage);
		paramsMap.put("pageSize", pageSize);
		paramsMap.put("version", "1");
		switch (type) {
		case "1":
			paramsMap.put("type", type);
			break;
		case "2":
			paramsMap.put("type", type);
			paramsMap.put("otherCode", otherCode);
			break;
		case "3":
			paramsMap.put("type", type);
			paramsMap.put("otherCode", otherCode);
			break;
		case "4":
			paramsMap.put("type", type);
			paramsMap.put("otherCode", otherCode);
			break;

		default:
			break;
		}
		if (!StringUtil.isEmpty(content)) {
			paramsMap.put("content", content);
		}
		return paramsMap;
	}
	
	/**
	 * 向好友索要笔记，分享笔记
	 * @param context
	 * @param uid
	 * @param tuid
	 * @param courseId
	 * @param content
	 * @return
	 */
	public static Map<String, String> askForNote(Context context,String uid, String tuid, String courseId, String content) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", uid);
		paramsMap.put("tuid", tuid);
		paramsMap.put("courseId", courseId);
		paramsMap.put("content", content);
		return paramsMap;
	}
	/**
	 * 打赏好友成长币
	 * @param context
	 * @param uid
	 * @param tuid
	 * @param gcoin
	 * @param content
	 * @return
	 */
	public static Map<String, String> awardGCoin(Context context,String uid, String tuid, String gcoin, String content,String recordId) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("uid", uid);
		paramsMap.put("tuid", tuid);
		paramsMap.put("gcoin", gcoin);
		paramsMap.put("content", content);
		paramsMap.put("recordId", recordId);
		return paramsMap;
	}
	/**
	 * 向微店请求商品列表
	 * @param context
	 * @param param
	 * @param pub
	 * @return
	 */
	public static Map<String, String> getListFromVDian(Context context,String param, String pub) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("param", param);
		paramsMap.put("public", pub);
		return paramsMap;
	}
	
	/**
	 * 请求token ，过期flag为1
	 * @param context
	 * @param flag
	 * @return
	 */
	public static Map<String, String> getToken(Context context,String flag) {
		Map<String, String> paramsMap = getParamsMap(context);
		paramsMap.put("flag", flag);
		return paramsMap;
	}
}