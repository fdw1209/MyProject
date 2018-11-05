package com.ch.sockettest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ch.sockettest.until.IPUtil;

public class StopThread extends Thread {
	
	@Override
	public void run() {
		try {
			// 1.����Socket���󣬲�ָ����������IP��ַ�������������Ͷ˿ں�
			Socket socket = new Socket();
			socket.bind(new InetSocketAddress(IPUtil.getIpAddrByName("wlan2"), 0));
			socket.connect(new InetSocketAddress(IPUtil.getIpAddrByName("eth8"), 8899), 5000);
			// 2.����Socketʵ����getOutputStream()���������������������
			OutputStream os = socket.getOutputStream();
			// PrintWriter writer = new PrintWriter(os);
			/**
			 * ���շ���������Ӧ������(GET)
			 */
			String content_stopstream = "GET 123456789\n";// �����������"ֹͣ"����
			os.write(content_stopstream.getBytes());
			os.flush();
			socket.shutdownOutput();
			new Thread(new Runnable() {
				public void run() {
					try {
						// 3.����Socketʵ����getInputStream()���������շ���������������Ϣ
						InputStream is = socket.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						System.out.println(reader.readLine());
						String line = null;
						while ((line = reader.readLine()) != null) {
							System.out.println("���ǿͻ��ˣ����յ���������������ϢΪ��" + line);
						}
						socket.shutdownInput();
						// 4.�ر���Դ
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
