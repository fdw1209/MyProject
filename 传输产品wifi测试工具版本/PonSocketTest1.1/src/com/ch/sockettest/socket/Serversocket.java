package com.ch.sockettest.socket;

import com.ch.sockettest.shell.MainView;

/**
 * ����TCP��Socketͨ��
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
				new Thread(new PostThread(MainView.wifiDatas.get(count))).start();// ���пͻ����߳�
			} else {
				new Thread(new ReceiveThread(MainView.wifiDatas.get(count))).start();// ���пͻ����߳�
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
