package com.ch.sockettest.dao;
/**
 * ����״̬ö����
 * @author yuanji
 *
 */
public enum State {
	CONNECTED,
	NOTREADY,
	OFF;
	public String toStringCh(){
		if(this.equals(CONNECTED)){
			return "�豸������";
		}else if(this.equals(NOTREADY)){
			return "�ȴ��豸����...";
		}else if(this.equals(OFF)){
			return "�豸���ӶϿ�";
		}else {
			return "δ֪״̬";
		}
	}
}
