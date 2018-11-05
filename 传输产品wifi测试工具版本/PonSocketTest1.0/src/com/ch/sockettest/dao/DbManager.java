package com.ch.sockettest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import com.ch.sockettest.shell.MainView;

/**
 * 数据库管理
 * 
 * @author yuanji
 * 
 */
public class DbManager {

	public static final String DRIVER_MSSQL = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	public static final String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
	public static String DB_NAME = "EOC_DEBUG_INFO_DB";

	private static DbManager instance = null;
	private static boolean isReady = false;
	private static boolean initialized = false;
	private static boolean closed = false;

	public static DbManager getInstance() {
		return instance == null ? new DbManager() : instance;
	}

	public boolean isReady() {
		return isReady;
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @param bool
	 * @return
	 * @author yuanji @2013-12-12 上午10:06:57
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		Connection con = null;
		try {
			Class.forName(getDriver());
			String URL = getUrl();
			con = DriverManager.getConnection(URL, MainView.getProp().getProperty("C_DB_USER"),
					MainView.getProp().getProperty("C_DB_PASS"));
		} catch (Exception e) {
			con = null;
			isReady = false;
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 获取连接到指定数据库的连接
	 * 
	 * @param bool
	 * @return
	 * @author yuanji @2013-12-12 上午10:58:28
	 * @throws Exception
	 */
	public Connection getConnection(String dbname) throws Exception {
		Connection con = null;
		DB_NAME = dbname;
		try {
			Class.forName(getDriver());
			String URL = getUrl();
			con = DriverManager.getConnection(URL, MainView.getProp().getProperty("C_DB_USER"),
					MainView.getProp().getProperty("C_DB_PASS"));
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			isReady = false;
		}
		return con;
	}

	public String getDriver() throws Exception {
		String dbtype = MainView.getProp().getProperty("C_DB_TYPE", "MicrosoftSQL");
		String driver = null;
		if ("MicrosoftSQL".equals(dbtype)) {
			driver = DRIVER_MSSQL;
		} else if ("Mysql".equals(dbtype)) {
			driver = DRIVER_MYSQL;
		} else if ("Oracle".equals(dbtype)) {
			driver = DRIVER_ORACLE;
		}
		if (driver != null) {
			return driver;
		} else {
			throw new Exception("不支持的数据库");
		}
	}

	public String getUrl() throws Exception {
		String dbtype = MainView.getProp().getProperty("C_DB_TYPE");
		String ip = MainView.getProp().getProperty("C_DB_IPADDR");
		String port = MainView.getProp().getProperty("C_DB_PORT");
		String url = null;
		if ("MicrosoftSQL".equals(dbtype)) {
			url = "jdbc:sqlserver://%s:%s;DatabaseName=%s";
		} else if ("Mysql".equals(dbtype)) {
			url = "jdbc:mysql://%s:%s/%s";
		} else if ("Oracle".equals(dbtype)) {
			url = "jdbc:oracle:thin:@%s:%s:%s";
		}
		if (url != null) {
			url = String.format(url, ip, port, DB_NAME);
		} else {
			throw new Exception("不支持的数据库");
		}

		return url;

	}

	/**
	 * 检查指定名称的数据库是否存在 执行数据表的初始化
	 * 
	 * @param dbname
	 * @return
	 * @author yuanji @2013-12-12 上午10:52:51
	 * @throws Exception
	 */
	public synchronized boolean checkDb(String dbname) throws Exception {
		boolean bool = false;
		Statement st = null;
		Connection con = getConnection(dbname);
		if (con == null) {
			return false;
		}
		if (initialized) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		try {
			st = con.createStatement();
		} catch (Exception e1) {
		}
		if (st != null) {
			try {
				// 创建数据库
				String sql_1 = "if not exists (select * from master.dbo.sysdatabases where name='%s')\n"
						+ "create database %s;\n";
				sql_1 = String.format(sql_1, dbname, dbname);
				st.execute(sql_1);

				bool = true;
				initialized = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
				con.close();
			} catch (SQLException e) {
			}
		}
		return bool;
	}

	/**
	 * 创建指定名称的数据库
	 * 
	 * @param dbName
	 * @return
	 * @author yuanji @2013-12-12 上午10:52:37
	 */
	// public boolean createDB(String dbName) {
	// return execute(String.format("create database %s", dbName));
	// }

	/**
	 * 创建一个statement
	 * 
	 * @return
	 * @author yuanji @2013-12-12 上午10:59:33
	 * @throws Exception
	 */
	public Statement getStatement() throws Exception {
		Statement st = null;
		Connection con = getConnection();
		if (con != null) {
			try {
				st = con.createStatement();
			} catch (SQLException e) {
			}
		}
		return st;
	}

	/**
	 * 初始化数据库
	 * 
	 * @author yuanji @2013-12-12 上午10:52:12
	 */
	public void init() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				initialized = false;
				while (!closed) {
					// while (isReady == false && initialized) {
					// isReady = checkDb(DB_NAME);
					// try {
					// TimeUnit.MILLISECONDS.sleep(5000);
					// } catch (InterruptedException e) {
					// }
					// }
					try {
						isReady = checkDb(DB_NAME);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				isReady = false;
			}

		}, "checkdatabase");
		t.start();
	}

	/**
	 * 执行SQL的方法
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sql) throws Exception {
		boolean bool = false;
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.execute(sql);
				bool = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				Connection con = statement.getConnection();
				statement.close();
				con.close();
			} catch (SQLException e) {
			}

		}
		return bool;
	}

	/**
	 * 获取查询结果
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public ResultSet query(String sql) throws Exception {
		Connection conn = getConnection();
		Statement st = null;
		ResultSet rs = null;
		if (conn != null) {
			try {
				st = conn.createStatement();
				rs = st.executeQuery(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * 查找指定table中column是否存在
	 * 
	 * @param table
	 * @param column
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private boolean checkColumnExists(String table, String column) throws Exception {
		boolean b = false;
		String sql = "SELECT 1 FROM SYSOBJECTS T1 INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID WHERE T1.NAME='" + table
				+ "' AND T2.NAME='" + column + "'";
		ResultSet rs = query(sql);
		return b;
	}

	public void close() {
		System.out.println("断开数据库连接");
		closed = true;
	}

	/**
	 * 释放Connection连接
	 * 
	 * @param connection
	 */
	public static void releaseConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection con = null;
		try {

			Class.forName(DRIVER_MSSQL);
			String URL = String.format("jdbc:sqlserver://%s:%s;DatabaseName=%s", "10.3.8.44", 1433, DB_NAME);
			con = DriverManager.getConnection(URL, "sa", "pass");
			System.out.println(con);
			String sql = "SELECT * FROM preData";
			Statement st = con.createStatement();
			ResultSet rSet = st.executeQuery(sql);
			System.out.println(rSet);
			while (rSet.next()) {
				String str=rSet.getString(3);
				System.out.println(str);
			}
			rSet.close();
			st.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
