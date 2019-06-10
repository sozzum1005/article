package sbs.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

	private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final String DB_URL = "jdbc:mysql://localhost:3306/a6?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";

	private static final String DB_ID = "root";

	private static final String DB_PW = "";

	public enum SqlTypeCode {
		QUERY, UPDATE
	}

	// 쿼리종류구분(세부)코드
	public enum SqlType2Code {
		SELECT, INSERT, UPDATE, DELETE
	}

	public static class DBLink {
		private Connection con;
		private Statement stmt;
		private ResultSet rs;
		private ResultSetMetaData rsMd;
		private int columnsCount;
		private int rowsCount;
		private int lastInsertId;
		private SqlTypeCode sqlTypeCode;
		private SqlType2Code sqlType2Code;

		public int getColumnsCount() {
			return columnsCount;
		}

		public int getRowsCount() {
			return rowsCount;
		}

		public int getLastInsertId() {
			return lastInsertId;
		}

		private void makeNewConnectionIfEmpty() {
			if (con == null) {
				con = DBUtil.getNewConnection();

				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					System.out.print("SQLException : " + e.getMessage());
				}
			}
		}

		private DBLink() {
			makeNewConnectionIfEmpty();
		}

		public void executeQuery(String sql) {
			if (sql == null) {
				sql = "";
			}

			sql = sql.trim();

			makeNewConnectionIfEmpty();

			sqlTypeCode = SqlTypeCode.QUERY;
			sqlType2Code = SqlType2Code.SELECT;

			if (sql.toLowerCase().startsWith("select") == false) {
				sqlTypeCode = SqlTypeCode.UPDATE;
				if (sql.toLowerCase().startsWith("update")) {
					sqlType2Code = SqlType2Code.UPDATE;
				} else if (sql.toLowerCase().startsWith("insert")) {
					sqlType2Code = SqlType2Code.INSERT;
				} else {
					sqlType2Code = SqlType2Code.DELETE;
				}
			}

			try {
				if (sqlTypeCode == SqlTypeCode.QUERY) {
					rs = stmt.executeQuery(sql);
					rsMd = rs.getMetaData();
					columnsCount = rsMd.getColumnCount();
					rs.last();
					rowsCount = rs.getRow();
					rs.beforeFirst();
					lastInsertId = 0;
				} else if (sqlType2Code == SqlType2Code.INSERT) {
					rowsCount = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
					rs = stmt.getGeneratedKeys();
					rsMd = rs.getMetaData();
					columnsCount = 0;
					rs.next();
					lastInsertId = rs.getInt(1);
				} else {
					rsMd = null;
					rowsCount = stmt.executeUpdate(sql);
					columnsCount = 0;
					lastInsertId = 0;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void close() {
			rsMd = null;
			DBUtil.close(rs);
			rs = null;
			DBUtil.close(stmt);
			stmt = null;
			DBUtil.close(con);
			con = null;
		}

		public List<Map<String, Object>> getRows(String sql) {
			executeQuery(sql);

			List<Map<String, Object>> rows = new ArrayList<>();

			try {

				if (sqlType2Code == SqlType2Code.SELECT) {
					if (rowsCount > 0) {
						while (rs.next()) {
							Map<String, Object> row = new HashMap<>();

							for (int i = 1; i <= columnsCount; i++) {
								String columnName = rsMd.getColumnName(i);
								row.put(columnName, rs.getObject(i));
							}
							rows.add(row);
						}
					}
				}
			} catch (SQLException e) {
				System.out.println("SQLException : " + e.getMessage());
			}

			return rows;
		}

		public Map<String, Object> getRow(String sql) {
			List<Map<String, Object>> rows = getRows(sql);

			if (rows.size() > 0) {
				return rows.get(0);
			}

			return new HashMap<String, Object>();
		}

		public Object getRowValue(String sql) {
			Object value = null;

			Map<String, Object> row = getRow(sql);

			if (row.size() > 0) {
				String firstKey = row.keySet().iterator().next();

				value = row.get(firstKey);
			}

			return value;
		}

		public int getRowIntValue(String sql) {
			Object value = getRowValue(sql);

			if (value != null) {
				if (value instanceof Integer) {
					return (int) value;
				}

				return Integer.parseInt(value.toString());
			}

			return 0;
		}

		public String getRowStringValue(String sql) {
			Object value = getRowValue(sql);

			if (value != null) {
				return value.toString();
			}

			return "";
		}
	}

	public static DBLink getNewDBLink() {
		DBLink dbLink = new DBLink();

		return dbLink;
	}

	private static void loadDBDriver() {

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			System.out.println("ClassNotFoundException : " + e.getMessage());

		}

	}

	private static Connection makeNewConnection() {

		Connection con = null;

		try {

			con = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);

		} catch (SQLException e) {

			System.out.println("SQLException : " + e.getMessage());

		}

		return con;

	}

	public static Connection getNewConnection() {

		loadDBDriver();

		Connection con = makeNewConnection();

		return con;

	}

	public static void close(Connection con) {

		if (con != null) {

			try {

				con.close();

			} catch (SQLException e) {

				System.out.println("SQLException : " + e.getMessage());

			}

		}

	}

	public static void close(Statement stmt) {

		if (stmt != null) {

			try {

				stmt.close();

			} catch (SQLException e) {

				System.out.println("SQLException : " + e.getMessage());

			}

		}

	}

	public static void close(ResultSet rs) {

		if (rs != null) {

			try {

				rs.close();

			} catch (SQLException e) {

				System.out.println("SQLException : " + e.getMessage());

			}

		}

	}

}
