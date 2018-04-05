package cc.upedu.online.domin;

import java.util.List;

/**
 * 查询收入统计信息的javabean
 * 
 * @author Administrator
 * 
 */
public class MyWalletGetIncomeChartBean {
	
	public String message;
	public String success;
	public Entity entity;

	public class Entity {
		public String total;
		public PieList pielist;
		public List<BarList> barlist;

	}
	/**
	 * 饼状图集合
	 * @author Administrator
	 *
	 */
	public class PieList{
		/**
		 * 退款金额数量
		 */
		public String refund;
		/**
		 * 现金充值金额数量
		 */
		public String cashload;
		/**
		 * 消费，出账金额数量
		 */
		public String sales;
		/**
		 * 代理商收入金额数量
		 */
		public String agentincome;
		/**
		 * 导师收入金额数量
		 */
		public String teacherincome;
		/**
		 * 其他金额数量
		 */
		public String other;

	}
	/**
	 * 柱状图的集合
	 * @author Administrator
	 *
	 */
	public class BarList{
		/**
		 * 柱形图单元名称（可能是课程名，or学员名称）
		 */
		public String barName;
		/**
		 * 柱形图单元数量
		 */
		public String barNum;

	}
}
