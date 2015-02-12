package com.howbuy.onlinecalc.utils;

/**
 * ͳ��ά��
 * 
 * @author guangbao.wang
 * 
 */
public enum Dimension {
	MONTH1("1Y", 1101), // ��һ��
	MONTH3("3Y", 1102), // ��3��
	MONTH6("6Y", 1103), // ��6��
	CURYEAR("JN", 1204), // ��������
	YEAR1("1N", 1201), // ��һ��
	YEAR2("2N", 1202), // ������
	YEAR3("3N", 1203), // ������
	ALL("CL", 1401);// ��������

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
