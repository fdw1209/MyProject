package com.ch.sockettest.socket;

import com.ch.sockettest.shell.MainView;

/**
 * 基于TCP的Socket通信
 * 
 * @author fdw
 *
 */
public class Serversocket {

	public static boolean createServerSocket() {

		int count = 0;
		while (count < 6) {
			MainView.wifiDatas.get(count).id = count + 1;
			if (count < 3) {
				new Thread(new PostThread(MainView.wifiDatas.get(count))).start();// 上行客户端线程
			} else {
				new Thread(new ReceiveThread(MainView.wifiDatas.get(count))).start();// 下行客户端线程
			}
			count++;
		}
//		try {
//			TimeUnit.MILLISECONDS.sleep(5000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if (ReceiveThread.isConnect && PostThread.isConnect) {
			return true;
		} else {
			return false;
		}
	}
}
