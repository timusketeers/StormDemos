package com.howbuy.onlinecalc.utils;

/**
 * 统计维度
 * 
 * @author guangbao.wang
 * 
 */
public enum Dimension {
	MONTH1("1Y", 1101), // 近一月
	MONTH3("3Y", 1102), // 近3月
	MONTH6("6Y", 1103), // 近6月
	CURYEAR("JN", 1204), // 今年以来
	YEAR1("1N", 1201), // 近一年
	YEAR2("2N", 1202), // 近两年
	YEAR3("3N", 1203), // 近三年
	ALL("CL", 1401);// 成立以来

	private String name;
	private int value;

	private Dimension(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
