package com.ch.sockettest.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;

import org.apache.commons.net.telnet.TelnetClient;

import com.ch.sockettest.until.Utils;

/**
 * Telnet操作器 基于commons-net-3.6.jar
 * 
 * @author fdw
 *
 */
public class TelnetOperator {

	private String prompt = "#"; // 结束标识字符串,Windows中是>,Linux中是#
	private char promptChar = '#'; // 结束标识字符
	private TelnetClient telnet;
	private InputStream in; // 输入流,接收返回信息
	private PrintStream out; // 向服务器写入 命令

	/**
	 * @param termtype
	 *            协议类型：VT100、VT52、VT220、VTNT、ANSI
	 * @param prompt
	 *            结果结束标识
	 */
	public TelnetOperator(String termtype, String prompt) {
		telnet = new TelnetClient(termtype);
		setPrompt(prompt);
	}

	public TelnetOperator(String termtype) {
		telnet = new TelnetClient(termtype);
	}

	public TelnetOperator() {
		telnet = new TelnetClient();
	}

	/**
	 * 登录到目标主机
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public boolean login(String ip, int port, String localip, int localport, String username, String password) {
		boolean flag = true;
		try {
			// telnet.setConnectTimeout(1000);
			telnet.connect(ip, port, InetAddress.getByName(localip), localport);
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			write(username);
			write(password);
			String rs = readUntil(null);
			if (rs != null && (rs.contains("User name is incorrect") || rs.contains("Password is incorrect")
					|| rs.contains("Login timed out after 60 seconds"))) {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 读取分析结果
	 * 
	 * @param pattern
	 *            匹配到该字符串时返回结果
	 * @return
	 */
	public String readUntil(String pattern) {
		StringBuffer sb = new StringBuffer();
		try {
			char lastChar = (char) -1;
			boolean flag = pattern != null && pattern.length() > 0;
			if (flag)
				lastChar = pattern.charAt(pattern.length() - 1);
			char ch;
			int code = -1;
			while ((code = in.read()) != -1) {
				ch = (char) code;
				sb.append(ch);

				// 匹配到结束标识时返回结果
				if (flag) {
					if (ch == lastChar && sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				} else {
					// 如果没指定结束标识,匹配到默认结束标识字符时返回结果
					if (ch == promptChar)
						return sb.toString();
				}
				// 登录失败时返回结果
				if (sb.toString().contains("User name is incorrect") || sb.toString().contains("Password is incorrect")
						|| sb.toString().contains("Login timed out after 60 seconds")) {
					return sb.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 发送命令
	 * 
	 * @param value
	 */
	public void write(String value) {
		try {
			out.println(value);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送命令,返回执行结果
	 * 
	 * @param command
	 * @return
	 */
	public String sendCommand(String command) {
		try {
			write(command);
			return readUntil(prompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭连接
	 */
	public void distinct() {
		try {
			if (telnet != null){
				telnet.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPrompt(String prompt) {
		if (prompt != null) {
			this.prompt = prompt;
			this.promptChar = prompt.charAt(prompt.length() - 1);
		}
	}

	/**
	 * 根据Telnet命令，返回处理后的数据结果
	 * 
	 * @param command
	 * @return
	 */
	public String getResult(String command) {
		String result = "";
		String rs = sendCommand(command);
		rs = rs.substring(rs.indexOf(":") + 1, rs.lastIndexOf("tag")).trim();
		try {
			result = Utils.asciiString2String(rs, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		TelnetOperator telnet = new TelnetOperator(); // Windows,用VT220,否则会乱码
//		telnet.login("192.168.1.1", 23, "192.168.1.10", 0, "CMCCAdmin", "aDm8H%MdA");
		telnet.login("192.168.1.1", 23, "192.168.1.10", 0, "root", "Pon521");
		// String rs = telnet.sendCommand("sismac show");
		//String rs = telnet.sendCommand("sismac 2 512");// SN = CMDCA40007E6
		 String rs = telnet.sendCommand("sismac 2 1024");//SSID1 = CMCC-FxFh
		// String rs = telnet.sendCommand("sismac 2 1280");//Password1 = 6nvsnett
		// String rs = telnet.sendCommand("sismac 2 1028");//SSID2 = CMCC-FxFh-5G
		//String rs = telnet.sendCommand("sismac 2 1280");// Password2 = 6nvsnett
		rs = rs.substring(rs.indexOf(":") + 1, rs.lastIndexOf("tag")).trim();
		System.out.println(rs);
		try {
			System.out.println(Utils.asciiString2String(rs, 16));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
