package com.ch.sockettest.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)// @Retention����˵����ע�������������,RetentionPolicy.RUNTIMEע�Ᵽ���ڳ��������ڼ䣬��ʱ����ͨ�������ö�����ĳ�����ϵ�����ע�⡣
@Target(ElementType.FIELD)//@Target:Target ��ʾ��ע������ʲô�ط�,ֻ�������ֶλ�����;
public @interface Language {
	String value();

	String cn() default "";
}
//������Ԫ�ص�ע��
//1��Ԫ����,Ҳ��Ԫע�⣬�Ƿ��ڱ������һ��ע�����ǰ�� ���Ƕ�ע��һ�����ơ�ֻ�������� @Retention �� @Targe
//2��ע��Ķ���ʹ�ùؼ��� @interface,��������һ��ע��@Rentention(arg) ����@Target(args) 
