package com.ch.sockettest.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PreDataDAOImpl extends DAO<PreData> implements PreDataDAO {

	@Override
	public PreData get(long taskId, String sn) {
		String sql = "SELECT * FROM preData where taskId=? and sn=? ORDER BY id";
		List<PreData> preDatas = getForList(sql, taskId, sn);
		if (preDatas.size() > 0) {
			return preDatas.get(0);
		}
		return get(sql, taskId, sn);

	}

	@Override
	public List<PreData> getAll() {
		String sql = "SELECT * FROM preData";
		return getForList(sql);
	}

	@Override
	public List<BigDecimal> getTaskId() {
		List<BigDecimal> list = new ArrayList<>();
		String sql = "SELECT ID FROM tasks";
		try {
			Connection conn = DbManager.getInstance().getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getBigDecimal(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String getForValue(long taskId, String sn, String key) {
		String value = null;
		String sql = "SELECT * FROM preData where taskId='" + taskId + "' and sn='" + sn + "'";
		try {
			Connection conn = DbManager.getInstance().getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				value = rs.getString(key);
				return value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		// return getForValue(sql, key, taskId, sn);
	}

}
