package com.ch.sockettest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ch.sockettest.main.MainView;

public class StopThread extends Thread {
	
	public String devName;
	
	public StopThread(String devName) {
		this.devName = devName;
	}

	@Override
	public void run() {
		try {
			// 1.创建Socket对象，并指明服务器的IP地址（或主机名）和端口号
			Socket socket = new Socket();
			if (devName.equals("Dev1")) {
				socket.bind(new InetSocketAddress(MainView.getProp().getProperty("Dev1_WirelessIp"), 0));
				socket.connect(new InetSocketAddress(MainView.getProp().getProperty("Dev1_CableIp"), 7788), 5000);
			}else {
				socket.bind(new InetSocketAddress(MainView.getProp().getProperty("Dev2_WirelessIp"), 0));
				socket.connect(new InetSocketAddress(MainView.getProp().getProperty("Dev2_CableIp"), 8899), 5000);
			}
			// 2.调用Socket实例的getOutputStream()方法，向服务器发送内容
			OutputStream os = socket.getOutputStream();
			// PrintWriter writer = new PrintWriter(os);
			/**
			 * 接收服务器端相应的数据(GET)
			 */
			String content_stopstream = "GET 123456789\n";// 向服务器发送"停止"命令
			os.write(content_stopstream.getBytes());
			os.flush();
			socket.shutdownOutput();
			new Thread(new Runnable() {
				public void run() {
					try {
						// 3.调用Socket实例的getInputStream()方法，接收服务器发送来的信息
						InputStream is = socket.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						System.out.println(reader.readLine());
						String line = null;
						while ((line = reader.readLine()) != null) {
							System.out.println("我是客户端，接收到服务器发来的信息为：" + line);
						}
						socket.shutdownInput();
						// 4.关闭资源
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
