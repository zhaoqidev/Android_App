package cc.upedu.online.utils;

/**
 * 常量类--网络请求url前缀常量,视频播放常量
 */
public class ConstantsOnline {
	public static final String SERVER_URL="http://www.upedu.cc/";
	//public static final String SERVER_URL="http://192.168.1.147:8080/";
	public static final String SERVER_IMAGEURL="http://static.upedu.cc";
	//属相和星座请求的前缀
	public static final String SERVER_IMAGEURL2="http://www.upedu.cc";
	public static final String IP_IMAGEURL="http://www.upedu.cc";
	public static final String IP_URL="http://192.168.1.147:8080";
	//	public static final String SNS_URL="http://192.168.1.147:8081/";//	http://sns.upedu.cc/
	public static final String SNS_URL="http://sns.upedu.cc/";

	public static final String UU="3e58f2d148";
//	public static final String VU="6838c2177c";
	public static final String KEY="4d273614af43535abebfbf492e056492";
	
	/**
	 * cc视频用户Id
	 */
	public static final String CC_USERID = "AC7D44782F7F3973";
	/**
	 * cc视频key
	 */
	public static final String CC_API_KEY = "W3aNrRBi0z8y2zcJNKdmETzge6tJYkFd";
	
	/**
	 * 联网失败
	 */
	public static final int NET_FAILED=0;
	/**
	 * 联网成功
	 */
	public static final int NET_SUCCESS=1;
	/**
	 * 获取数据成功
	 */
	public static final int SUCCESS=200;
	/**
	 * 获取数据失败
	 */
	public static final int FAILED=-1;
	/**
	 * 获取数据,登录已过期
	 */
	public static final int TIMEOUT=5000;
	/**
	 * 轮播图广告
	 */
	public static String HOME_ROLLVIEW = SERVER_URL+"webapp/queryappbanner";
	/**
	 * 课程体系列表
	 */
	public static String HOME_ARCHITECTURE = SERVER_URL+"webapp/getsubjectlist";
	/**
	 * 课程体系列表详情
	 */
	public static String HOME_SHOWARCHITECTURE = SERVER_URL+"webapp/showsubjectinfo";
	/**
	 * 首页公告
	 */
	public static String HOME_INDEXNOTIC = SERVER_URL+"webapp/queryindexNotice";
	/**
	 * 首页推荐课程列表
	 */
	public static String HOME_RECMDCOURSE = SERVER_URL+"webapp/queryRecmdCourse";
	/**
	 * 首页推荐课程列表
	 */
	public static String HOME_INDEXPAGE = SERVER_URL+"webapp/indexpage";
	/**
	 * 首页去除后的课程列表
	 */
	public static String COURSE_INDEXPAGE = SERVER_URL+"webapp/courseindexpage";
	/**
	 * 直播列表接口
	 */
	public static String HOME_LIVE = SERVER_URL+"webapp/appsearchLiveList";
	/**
	 * 课程列表接口
	 */
	public static String COURSE = SERVER_URL+"webapp/appallcourselist";
	/**
	 * 体系中课程列表接口
	 */
	public static String COURSE_SUBJECT = SERVER_URL+"webapp/showsubjectCourseList";
	/**
	 * 课程介绍主界面接口
	 */
	public static String COURSE_INTRODUCE = SERVER_URL+"webapp/showcourseinfo";
	/**
	 * 课程介绍主界面接口
	 */
	public static String COURSE_FREELIVE = SERVER_URL+"webapp/enrollfreelive";
	/**
	 * 学习记录接口
	 */
	public static String USERLEARNING_RECORDS = SERVER_URL+"webapp/userlearningrecords";
	/**
	 * 课程介绍界面中的同学感悟接口
	 */
	public static String COURSE_ASSESS = SERVER_URL+"webapp/showcourseAssess";
	/**
	 * 课程学习界面中的笔记pager的接口
	 */
	public static String COURSE_NOTICES = SERVER_URL+"webapp/getCourseNoteList";
	/**
	 * 课程学习界面中的提问pager的接口
	 */
	public static String COURSE_QUESTIONS = SERVER_URL+"webapp/getCourseQuestionList";
	/**
	 * 课程学习界面中的章节目录接口
	 */
	public static String COURSE_CATALOG = SERVER_URL+"webapp/getCourseCatalog";
	/**
	 * 课程学习界面中的答疑pager中的文字答疑接口
	 */
	public static String COURSE_TEXTANSWER = SERVER_URL+"webapp/getCourseQAWordsList";
	/**
	 * 课程学习界面中的答疑pager中的视频答疑接口
	 */
	public static String COURSE_VIDEOANSWER = SERVER_URL+"webapp/getCourseQAVidioList";
	/**
	 * 登陆的接口地址
	 */
	public static String LOGIN_URL = SERVER_URL + "webapp/applogin";
	
	/**
	 * 发送短信验证码的接口地址
	 */
	public static String SEND_SMS = SERVER_URL + "webapp/sendsms";
	
	/**
	 * 注册的接口地址
	 */
	public static String REGIST_URL = SERVER_URL + "webapp/appregister";
	/**
	 * 找回密码接口地址
	 */
	public static String FIND_URL = SERVER_URL + "webapp/appforgetpwd";
	/**
	 * 重置密码接口地址
	 */
	public static String RESET_URL = SERVER_URL + "webapp/appupdatepwd";
	/**
	 * 用户名片
	 */
	public static String USER_CARD = SERVER_URL+"webapp/appshowUserInfo";
	
	/**
	 * 侧拉栏---我的课程列表
	 */
	public static String MY_COURSE = SERVER_URL+"webapp/querycusbuycourselist";
	/**
	 * 侧拉栏---我的笔记中我的笔记课程列表/他人笔记课程列表/分享的笔记课程列表
	 */
	public static String MY_NOTECOURSE = SERVER_URL+"webapp/getnoteclasslist";
	/**
	 * 侧拉栏---我的笔记中某个课程的笔记列表
	 */
	public static String MY_NOTEONECOURSE = SERVER_URL+"webapp/getnotelist";
	/**
	 * 侧拉栏---我的笔记中某个课程的笔记列表
	 */
	public static String MY_NOTERECORD = SERVER_URL+"webapp/getnoterecord";
	/**
	 * 智库pager的列表
	 */
	public static String ZHIKU_GRADEVIEW = SERVER_URL+"webapp/getsubjectlist";
	/**
	 * 导师的列表
	 */
	public static String DAOSHI_LIST = SERVER_URL+"webapp/appsearchTeacherList";
	/**
	 * 学友的列表
	 */
	public static String SCHOOL_MATE_LIST = SERVER_URL+"webapp/appsearchStudentList";
	/**
	 * 侧拉栏---我的代言列表
	 */
	public static String MY_REPREAENT = SERVER_URL+"webapp/userRepreCusList";
	/**
	 * 导师名片，基本信息
	 */
	public static String DAOSHI_CARD = SERVER_URL+"webapp/showteacherinfo";
	/**
	 * 导师名片，课程信息
	 */
	public static String DAOSHI_CAUSRE = SERVER_URL+"webapp/showteachercourse";
	
	/**
	 * 导师名片，寄语信息
	 */
	public static String DAOSHI_WISHES = SERVER_URL+"webapp/showteacherwords";
	/**
	 * 导师名片，提交寄语
	 */
	public static String NEW_WISHES = SERVER_URL+"webapp/addTeacherWords";
	/**
	 * 导师名片，文章信息
	 */
	public static String DAOSHI_ARTICLE = SERVER_URL+"webapp/showteacherarticle";
	/**
	 * 智库文章信息
	 */
	public static String ZHIKU_ARTICLE = SERVER_URL+"webapp/applibArticleList";
	/**
	 * 文章信息
	 */
	public static String ARTICLE_DETAILS = SERVER_URL+"front/articlelistone?id=";
	/**
	 * 活动信息
	 */
	public static String SPORT = SNS_URL+"api/appsearchActivityList";
	/**
	 * 活动详情
	 */
	public static String SPORT_DETAIL = SNS_URL+"api/appshowActivityInfo";
	/**
	 * 加入活动
	 */
	public static String JOIN_SPORT = SNS_URL+"api/appjoinActivity";
	/**
	 * 收藏活动
	 */
	public static String COLLECT_SPORT = SNS_URL+"api/appcollectActivity";
	/**
	 * 发布或更新活动
	 */
	public static String ISSUE_SPORT = SNS_URL+"api/appsaveActivity";
	/**
	 * 侧拉栏---我的活动列表
	 */
	public static String MY_SPORT = SNS_URL+"api/appuserActivityList";
	/**
	 * 侧拉栏---我的收藏中的收藏课程
	 */
	public static String COURSE_COLLECTCOURE = SERVER_URL+"webapp/coursecollectionlist";
	/**
	 * 侧拉栏---我的收藏中的收藏文章
	 */
	public static String COURSE_COLLECTARTICLE = SERVER_URL+"webapp/articlecollectionlist";
	/**
	 * 侧拉栏---我消息列表
	 */
	public static String MY_MESSAGE = SERVER_URL+"webapp/showuserMsgList";
	/**
	 * 侧拉栏---我钱包列表
	 */
	public static String MY_WALLET = SERVER_URL+"webapp/showuserPurse";
	/**
	 * 侧拉栏---我钱包列表--收支明细
	 */
	public static String MY_WALLET_USER = SERVER_URL+"webapp/showuserPurse/accioList";
	/**
	 * 侧拉栏---我钱包列表--导师收入明细
	 */
	public static String MY_WALLET_DAOSHI = SERVER_URL+"webapp/accIncomeList/teacher";
	/**
	 * 侧拉栏---我钱包列表--代理商收入明细
	 */
	public static String MY_WALLET_AGENT = SERVER_URL+"webapp/accIncomeList/agent";
	/**
	 * 侧拉栏---我钱包列表--成长币收入明细
	 */
	public static String MY_WALLET_COIN = SERVER_URL+"webapp/showuserPurse/coinioList";
	/**
	 * 侧拉栏---我钱包列表--查询收入统计信息
	 */
	public static String GET_INCOME_CHART= SERVER_URL+"webapp/getIncomeChart";
	/**
	 * 侧拉栏---我的关注，好友
	 */
	public static String MY_SCHOOLMATE = SNS_URL+"api/queryfollowList";
	
	/**
	 * 侧拉栏---删除我的消息的接口
	 */
	public static String DELETE_MY_MESSAGE = SERVER_URL+"webapp/deleteusermsg";
	
	/**
	 * 验证登陆是否失效
	 */
	public static String LOGIN_IN_FAILURE = SERVER_URL+"webapp/checkapplogin";
	/**
	 * 侧拉栏---删除我的收藏接口
	 */
	public static String DELETE_MY_COLLECT = SERVER_URL+"webapp/deletecollection";
	/**
	 * 收藏课程接口
	 */
	public static String COLLECTION_COURSE = SERVER_URL+"webapp/collectioncourse";
	/**
	 * 记录学习记录接口
	 */
	public static String COUSER_PLAYERTIMES = SERVER_URL+"webapp/couser/playertimes";
	/**
	 * 收藏文章接口
	 */
	public static String COLLECTION_ARTICLE = SERVER_URL+"webapp/collectionarticle";
	/**
	 * 分享接口
	 */
	public static String SHARE_COURSE = SERVER_URL+"webapp/sharecourse";
	/**
	 * 展示课程(包括直播)笔记接口
	 */
	public static String SHOW_NOTE_TELECAST_COURSE = SERVER_URL+"webapp/showCourseNote";
	/**
	 * 保存课程(包括直播)笔记接口
	 */
	public static String SAVE_NOTE_TELECAST_COURSE = SERVER_URL+"webapp/saveCourseNote";
	/**
	 * 保存课程提问接口
	 */
	public static String ADD_COURSEQUESTION = SERVER_URL+"webapp/addCourseQuestion";
	/**
	 * 关注学友接口
	 */
	public static String ATTENTION_OTHERS = SNS_URL+"api/appaddFriend";
	/**
	 * 保存反馈信息接口
	 */
	public static String SAVE_FEEDBACK = SERVER_URL+"webapp/addfeedback";
	/**
	 * 用户退出接口
	 */
	public static String APPLOG_OUT = SERVER_URL+"webapp/applogout";
	/**
	 * 更改用户信息
	 */
	public static String UPDATE_USERINFO = SERVER_URL+"webapp/appupdateUserInfo";
	/**
	 * 设置个性背景
	 */
	public static String UPDATE_USERBANNER = SERVER_URL+"webapp/updateuserBanner";
	/**
	 * 侧拉栏---删除我的关注的接口
	 */
	public static String DELETE_MY_ATTENTION = SNS_URL+"api/cancelfollow";
	/**
	 * 侧拉栏---发送站内信的接口
	 */
	public static String SEND_MESSAGE = SNS_URL+"api/appsendMsg";
	/**
	 * 分享课程的url链接地址
	 */
	public static String SHAR_COURSE = SERVER_URL+"front/coursecard/";
	/**
	 * 分享导师的url链接地址
	 */
	public static String SHAR_TEACHER = SERVER_URL+"front/appteacher/";
	/**
	 * 分享活动的url链接地址
	 * TODO 更改url
	 */
	public static String SHAR_SPORT = SNS_URL+"activity/info/";
	/**
	 * 分享体系的url链接地址
	 */
	public static String SHAR_SUBJECT = SERVER_URL+"college/tixiCard/";
	/**
	 * 分享名片的url链接地址
	 */
	public static String SHAR_USERCARD = SERVER_URL+"shareuser/";
	/**
	 * 分享二维码后跳转到的注册界面
	 */
	public static String SHAR_REGIEST = SERVER_URL+"appregister/";
	/**
	 * 购物车添加课程接口
	 */
	public static String MY_SHOPPING = SERVER_URL+"webapp/addshopcartItem";
	/**
	 * 购物车删除课程接口
	 */
	public static String DEL_SHOPPINGITEM = SERVER_URL+"webapp/deleteshopcartItem";
	/**
	 * 图片上传
	 */
	public static String IMAGE_UPLOAD = SERVER_URL+"webapp/FileUpload";
	/**
	 * 侧拉栏---我的钱包-成长币--赠送给好友
	 */
	public static String SEND_COIN = SERVER_URL+"webapp/giveCoin";
	/**
	 * 基础数据接口
	 * 含有：
	 		1：兴趣爱好
			2：职务
			3：企业所属行业
			4：属相 
			5：星座
	 */
	public static String BASIC_DATA = SERVER_URL+"webapp/appgetBasicData";
	/**
	 * 确认订单接口
	 */
	public static String CONFIRM_ORDER=SERVER_URL+"webapp/create/pay/order";
	/**
	 * 支付前检测
	 */
	public static String CHECK_ORDER=SERVER_URL+"webapp/order/payment";
	/**
	 * 支付成功的回调
	 */
	public static String PAY_SUCCESS=SERVER_URL+"webapp/order/paysuccess";
	
	/**
	 * 我的订单列表接口
	 */
	public static String MY_ORDER=SERVER_URL+"webapp/appshowmyorder";
	
	/**
	 * 取消订单接口
	 */
	public static String CANCEL_ORDER=SERVER_URL+"webapp/appcancelmyorder";
	/**
	 * 软件更新接口
	 */
	public static String UPDATA_APP=SERVER_URL+"webapp/app/update";
	/**
	 * 软件更新接口
	 */
	public static String RECHARGE=SERVER_URL+"webapp/accrecharge";
	/**
	 * 微商城，商品列表接口
	 */
	public static String MICROMALL=SERVER_URL+"webapp/pointshoplist";
	/**
	 * 微商城，添加/修改收货地址接口
	 */
	public static String ADD_RECEIVINGADDRESS=SERVER_URL+"webapp/addreceivingaddress";
	/**
	 * 微商城，确认订单接口
	 */
	public static String CREATE_POINTORDER=SERVER_URL+"webapp/createpointorder";
	/**
	 * 我的收货地址列表接口
	 */
	public static String MY_RECEIVE_ADDRESS=SERVER_URL+"webapp/queryreceivingaddress";
	/**
	 * 我的收货地址列表,设置默认地址的接口
	 */
	public static String MY_RECEIVE_ADDRESS_DEFAULT=SERVER_URL+"webapp/setdefaultreceivingaddress";
	/**
	 * 我的收货地址列表,删除收货地址的接口
	 */
	public static String MY_RECEIVE_ADDRESS_DELETE=SERVER_URL+"webapp/deletereceivingaddress";
	/**
	 * 微商城，兑换记录接口
	 */
	public static String MY_POINTORDER=SERVER_URL+"webapp/pointorder";
	/**
	 * 获取个人概要信息接口
	 */
	public static String GET_USER_INFO=SERVER_URL+"webapp/getuserinfo";
	/**
	 * 课程(包括直播)学友的列表
	 */
	public static String SCHOOLMATE_LIST_TELECAST_COURSE = SERVER_URL+"webapp/getclassmatelist";
	/**
	 * 根据个人id获取个人概要信息
	 */
	public static String GET_USERINFO = SERVER_URL+"webapp/getuserinfo";
	/**
	 * 向好友的索要笔记
	 */
	public static String ASKFOR_NOTE = SERVER_URL+"webapp/getnotefromfriend";
	/**
	 * 分享笔记给好友
	 */
	public static String SHARE_NOTE = SERVER_URL+"webapp/sharenote";
	/**
	 * 打赏好友
	 */
	public static String AWARD_GCOIN = SERVER_URL+"webapp/awardgcoin";
	/**
	 * 获取微店AccessToken接口
	 */
	public static String ACCESSTOKEN = SERVER_URL+"webapp/getweishopaccesstoken";
	/**
	 * 从微店获取全部商品列表
	 */
	public static String GET_LIST_VDIAN = "http://api.vdian.com/api";
	
}
