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
 * DAO设计模式：Data Access Object数据访问对象，访问数据信息的类
 * 封装了对数据基本的增、删、改、查方法，以供子类继承使用，实现功能的模块化 当前DAO直接在方法中获取数据库链接
 * 
 * @author fdw
 * @param <T>:当前DAO处理的实体类的类型是什么
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
	 * 该方法封装了Insert、Delete、Update操作
	 * 
	 * @param sql：sql语句
	 * @param args：填充sql语句的占位符
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
	 * 查询一条记录，返回对应的T的一个实例类对象
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
	 * 查询多条记录，返回T所对应的List集合
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
	 * 返回某条记录的某一字段的值，例如返回某一条记录的Stu_name，或返回数据表中有多少条记录。
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
