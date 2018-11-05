package com.ch.sockettest.dao;

import java.math.BigDecimal;
import java.util.List;

/**
 * ��ȡԤ�����ݽӿ�
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
