package com.ch.sockettest.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 * DAO���ģʽ��Data Access Object���ݷ��ʶ��󣬷���������Ϣ����
 * ��װ�˶����ݻ���������ɾ���ġ��鷽�����Թ�����̳�ʹ�ã�ʵ�ֹ��ܵ�ģ�黯 ��ǰDAOֱ���ڷ����л�ȡ���ݿ�����
 * 
 * @author fdw
 * @param <T>:��ǰDAO�����ʵ�����������ʲô
 */
public class DAO<T> {

	private Class<T> clazz;

	private QueryRunner queryRunner = new QueryRunner();

	private static DbManager manager = new DbManager();

	@SuppressWarnings("unchecked")
	public DAO() {
		Type superClass = getClass().getGenericSuperclass();

		if (superClass instanceof ParameterizedType) {

			ParameterizedType parameterizedType = (ParameterizedType) superClass;

			Type[] typeArgs = parameterizedType.getActualTypeArguments();

			if (typeArgs != null && typeArgs.length > 0) {
				if (typeArgs[0] instanceof Class) {
					clazz = (Class<T>) typeArgs[0];
				}
			}
		}
	}

	/**
	 * �÷�����װ��Insert��Delete��Update����
	 * 
	 * @param sql��sql���
	 * @param args�����sql����ռλ��
	 */
	public void update(String sql, Object... args) {
		Connection connection = null;
		try {
			connection = manager.getConnection();
			queryRunner.update(connection, sql, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbManager.releaseConnection(connection);
		}
	}

	/**
	 * ��ѯһ����¼�����ض�Ӧ��T��һ��ʵ�������
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public T get(String sql, Object... args) {
		Connection connection = null;
		try {
			connection = manager.getConnection();
			return queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbManager.releaseConnection(connection);
		}
		return null;
	}

	/**
	 * ��ѯ������¼������T����Ӧ��List����
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<T> getForList(String sql, Object... args) {
		Connection connection = null;
		try {
			connection = manager.getConnection();
			return queryRunner.query(connection, sql, new BeanListHandler<>(clazz), args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbManager.releaseConnection(connection);
		}
		return null;
	}

	/**
	 * ����ĳ����¼��ĳһ�ֶε�ֵ�����緵��ĳһ����¼��Stu_name���򷵻����ݱ����ж�������¼��
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E getForValue(String sql, Object... args) {
		Connection connection = null;
		try {
			connection = manager.getConnection();
			return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbManager.releaseConnection(connection);
		}
		return null;
	}

}
