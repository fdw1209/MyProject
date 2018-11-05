package com.ch.sockettest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * （1）服务器端的实现步骤： <1>创建ServerSocket，指定端口号
 * <2>调用ServerSocket的accept()方法（该方法为阻塞方法，直到有客户端连接进来才会执行该方法之后的代码），等待客户端的连接，
 * 当有客户端连接后就会返回一个Socket实例 <3>调用Scoket实例的getInputStream()方法，读取客户端发送过来的信息
 * <4>调用Socket实例的getOutputStream()方法，向客户端写入服务器发送给该客户端的信息 <5>关闭所有资源
 * 
 * @author fdw
 *
 */
public class ServerThread extends Thread {

	static ServerSocket serversocket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	private OutputStream output;
	private InputStream input;
	Socket socket;
	String sname;

	public ServerThread(Socket socket, String sname) {
		this.socket = socket;
		this.sname = sname;
	}

	@Override
	public void run() {

		try {
			byte[] arrayOfByte = new byte[102400];
			/**
			 * 读取客户端发送的数据
			 */
			// 4.调用Scoket实例的getInputStream()方法，读取客户端发送过来的信息
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str1 = bufferedReader.readLine();

			System.out.println("我是服务器，客户端发来的信息是：" + str1);

			String[] arrayOfString = str1.split(" ");
			String str2 = arrayOfString[0];
			String str3 = arrayOfString[1].substring(0, 9);
			String str4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E a").format(new Date(System.currentTimeMillis()));
			System.out.println(str4);

			/**
			 * 服务器响应客户端
			 */
			input = socket.getInputStream();
			output = socket.getOutputStream();
			printWriter = new PrintWriter(output);
			if ("GET".equalsIgnoreCase(str2)) {
				// Log.d("SocketReceiver",
				// "prefix:4rrrrrrrrrrrrrrrrrrrrrrrrrrr " + str3);
				if ("/anysize/".equalsIgnoreCase(str3)) {
					printWriter.write("HTTP/1.1 200 OK\r\n");
					printWriter.write("Connection: Close\r\n");
					printWriter.write("Content-Type: text/plain\r\n");
					printWriter.write("Content-Length: ");
					printWriter.write(arrayOfString[1].substring(9));
					printWriter.write("\r\n\r\n");
					printWriter.flush();
					int j = 0;
					while (j < Integer.MAX_VALUE) {
						this.output.write(arrayOfByte);
						j++;
						continue;
					}
					printWriter.close();
					output.close();
					socket.close();
					// Log.d("SocketReceiver", "------over1-------");
					return;
				}
				printWriter.write("HTTP/1.1 200 OK\r\n");
				printWriter.write("Connection: Close\r\n");
				printWriter.write("Content-Type: text/plain\r\n");
				printWriter.write("\r\n");
				printWriter.write("This is a java process for wifi throutput test!\r\n");
				printWriter.write("Author: fdw\r\n");
				printWriter.write(str4);
				printWriter.flush();
				printWriter.close();
				output.close();
				socket.close();
				// Log.d("SocketReceiver", "------over2-------");
				return;
			} else {
				int j = 0;
				// if ("POST".equalsIgnoreCase(str2)) {
				while (j < Integer.MAX_VALUE) {
					this.input.read(arrayOfByte);
					j++;
					continue;
				}
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void startServer(final String sname, final int port) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ServerSocket server = null;
				try {
					// 1.创建ServerSocket，指定端口号
					server = new ServerSocket(port);
					System.out.println("Listen on " + port);
					while (true) {
						// 2.调用ServerSocket的accept()方法,等待客户端的连接
						Socket socket = server.accept();
						new ServerThread(socket, sname).start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, sname).start();
	}

	public static void main(String[] args) {
		startServer("server1", 8899);
		startServer("server2", 9988);
	}

}
