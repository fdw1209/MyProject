package com.ch.sockettest.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)// @Retention用来说明该注解类的生命周期,RetentionPolicy.RUNTIME注解保留在程序运行期间，此时可以通过反射获得定义在某个类上的所有注解。
@Target(ElementType.FIELD)//@Target:Target 表示该注解用于什么地方,只适用于字段或属性;
public @interface Language {
	String value();

	String cn() default "";
}
//定义了元素的注解
//1、元数据,也叫元注解，是放在被定义的一个注解类的前面 ，是对注解一种限制。只有两个： @Retention 和 @Targe
//2、注解的定义使用关键词 @interface,并在上面一行注明@Rentention(arg) 或者@Target(args) 
