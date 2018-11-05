package com.ch.sockettest.dao;
/**
 * 连接状态枚举类
 * @author yuanji
 *
 */
public enum State {
	CONNECTED,
	NOTREADY,
	OFF;
	public String toStringCh(){
		if(this.equals(CONNECTED)){
			return "设备已连接";
		}else if(this.equals(NOTREADY)){
			return "等待设备连接...";
		}else if(this.equals(OFF)){
			return "设备连接断开";
		}else {
			return "未知状态";
		}
	}
}
