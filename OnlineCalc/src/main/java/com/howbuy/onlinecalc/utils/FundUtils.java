package com.howbuy.onlinecalc.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class FundUtils {

	/**
	 * 获得基金代码和基金的成立日期.
	 * 
	 * @return
	 */
	public static List<FundVo> getFundInfo() {
		return FundService.getFundVos();
	}
	
	
	/**
	 * 获取私募基金经理人员代码。
	 * @return
	 */
	public static Set<String> getSmjlRydm()
	{
		Set<String> rydms = new HashSet<String>();
		List<Smjr> smjrs = FundService.getSmjlrs();
		for (Smjr smjr : smjrs) {
			rydms.add(smjr.getRydm());
		}
		return rydms;
	}
	
	/**
	 * 获取某个基金经理管理的基金
	 * @param rydm
	 * @return
	 */
	public static List<Smjr> getSmjrList(String rydm)
	{
		List<Smjr> smjrs = FundService.getSmjlrs();
		List<Smjr> data = new ArrayList<Smjr>();
		for (Smjr smjr : smjrs) {
			if (rydm.equals(smjr.getRydm())) {
				data.add(smjr);
			}
		}
		return data;
	}

	/**
	 * strom计算调用入口。
	 * 
	 * @param jsrq
	 *            计算日期
	 * @param jjdm
	 *            基金代码
	 * @param dimension
	 *            计算维度
	 */
	public static double[] calcProfitLost(Date jsrq, Date clrq, String jjdm, Dimension dimension) {
		try {
			String end = DateUtils.getDateString(jsrq);
			String start = "";
			switch (dimension) {
			case MONTH1:
				start = DateUtils.getBeforeDate(jsrq, Calendar.MONTH, 1);
				break;
			case MONTH3:
				start = DateUtils.getBeforeDate(jsrq, Calendar.MONTH, 3);
				break;
			case MONTH6:
				start = DateUtils.getBeforeDate(jsrq, Calendar.MONTH, 6);
				break;
			case YEAR1:
				start = DateUtils.getBeforeDate(jsrq, Calendar.YEAR, 1);
				break;
			case YEAR2:
				start = DateUtils.getBeforeDate(jsrq, Calendar.YEAR, 2);
				break;
			case YEAR3:
				start = DateUtils.getBeforeDate(jsrq, Calendar.YEAR, 3);
				break;
			case CURYEAR:
				// 今年以来
				start = Calendar.getInstance().get(Calendar.YEAR) + "0101";
				break;
			case ALL:
				// 成立以来
				break;
			default:
				break;
			}
			double[] result = new double[2];
			if (StringUtils.isNotBlank(start) && clrq.getTime() > DateUtils.parserDate(start).getTime()) {
				result[0] = 99999;
				result[1] = 99999;
			}
			result = FundService.calcRealGm(jjdm, start, end);// 计算
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取公募最大赢利和最大回撤的计算结果对象。
	 * 
	 * @param jjdm
	 * @param clrq
	 * @param start
	 * @param end
	 * @param dimension
	 * @return
	 */
	public static List<GmProfitLost> getGmProfitLosts(String jjdm, String clrq, String start, String end, Dimension dimension) {
		List<GmProfitLost> list = new ArrayList<GmProfitLost>();
		try {
			int count = DateUtils.daysBetween(start, end);
			for (int i = 0; i < count; i++) {
				String jzrq = DateUtils.getNextDate(DateUtils.parserDate(start), Calendar.DAY_OF_MONTH, i);
				System.out.println("*******************jzrq="+jzrq);
				double[] result = calcProfitLost(DateUtils.parserDate(jzrq), DateUtils.parserDate(clrq), jjdm, dimension);
				GmProfitLost gmProfitLost = new GmProfitLost();
				gmProfitLost.setJjdm(jjdm);
				gmProfitLost.setJzrq(jzrq);
				gmProfitLost.setZdyl(result[0]);
				gmProfitLost.setZdhc(result[1]);
				gmProfitLost.setZblb(String.valueOf(dimension.getValue()));
				list.add(gmProfitLost);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
