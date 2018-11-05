package com.ch.sockettest.dao;

import java.math.BigDecimal;
import java.util.List;

/**
 * 获取预置数据接口
 * 
 * @author fdw
 *
 */
public interface PreDataDAO {

	public List<PreData> getAll();

	public PreData get(long taskId, String sn);

	public String getForValue(long taskId, String sn, String key);

	public List<BigDecimal> getTaskId();

}
