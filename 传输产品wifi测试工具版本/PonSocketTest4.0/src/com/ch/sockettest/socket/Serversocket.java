package com.ch.sockettest.socket;

import com.ch.sockettest.main.MainView;
import com.ch.sockettest.popview.CompDev1TestTable;
import com.ch.sockettest.popview.CompDev2TestTable;

/**
 * ����TCP��Socketͨ��
 * 
 * @author fdw
 *
 */
public class Serversocket {

	public static boolean createServerSocket(String devName) {

		int count = 0;
		int streamCount = Integer.parseInt(MainView.getProp().getProperty("StreamCount"));
		while (count < streamCount * 2) {
			if (devName.equals("Dev1")) {
				CompDev1TestTable.wifiDatas.get(count).id = count + 1;
				if (count < streamCount) {
					new Thread(new PostThread(CompDev1TestTable.wifiDatas.get(count), devName)).start();// ���пͻ����߳�
				} else {
					new Thread(new ReceiveThread(CompDev1TestTable.wifiDatas.get(count), devName)).start();// ���пͻ����߳�
				}
			} else {
				CompDev2TestTable.wifiDatas.get(count).id = count + 1;
				if (count < 3) {
					new Thread(new PostThread(CompDev2TestTable.wifiDatas.get(count), devName)).start();// ���пͻ����߳�
				} else {
					new Thread(new ReceiveThread(CompDev2TestTable.wifiDatas.get(count), devName)).start();// ���пͻ����߳�
				}
			}
			count++;
		}
		// try {
		// TimeUnit.MILLISECONDS.sleep(5000);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		if (ReceiveThread.isConnect && PostThread.isConnect) {
			return true;
		} else {
			return false;
		}
	}
}
