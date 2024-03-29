package com.howbuy.onlinecalc.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据库连接类。
 * @author guangbao.wang
 */
public class DBConnection {

	private static Connection conn = null;
	private static Properties props = null;
	private static DruidDataSource druidDataSource = null;

	/**
	 * 加载数据库配置文件。
	 */
	static {
		props = new Properties();
		try {
			props.load(DBConnection.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获得Druid连接池对象。
	 * @return
	 */
	public static DruidDataSource geteDataSource()
	{
		if(druidDataSource == null)
		{
			druidDataSource = new DruidDataSource();
			druidDataSource.setDriverClassName(props.getProperty("jdbc.driverClassName"));
			druidDataSource.setUrl(props.getProperty("jdbc.url"));
			druidDataSource.setUsername(props.getProperty("jdbc.username"));
			druidDataSource.setPassword(props.getProperty("jdbc.password"));
			druidDataSource.setInitialSize(8);
			druidDataSource.setMaxActive(50);
			druidDataSource.setMinIdle(5);
			druidDataSource.setMaxWait(2000);
			druidDataSource.setPoolPreparedStatements(true);
			druidDataSource.setMaxOpenPreparedStatements(100);
		}
		return druidDataSource;
	}
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConn() {
		try {
	        conn = geteDataSource().getConnection();	
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭连接
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭连接
	 * @param stmt
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭连接
	 * @param stmt
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}