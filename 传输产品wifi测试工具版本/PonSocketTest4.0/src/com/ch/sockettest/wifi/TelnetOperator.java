package com.ch.sockettest.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;

import org.apache.commons.net.telnet.TelnetClient;

import com.ch.sockettest.until.Utils;

/**
 * Telnet������ ����commons-net-3.6.jar
 * 
 * @author fdw
 *
 */
public class TelnetOperator {

	private String prompt = "#"; // ������ʶ�ַ���,Windows����>,Linux����#
	private char promptChar = '#'; // ������ʶ�ַ�
	private TelnetClient telnet;
	private InputStream in; // ������,���շ�����Ϣ
	private PrintStream out; // �������д�� ����

	/**
	 * @param termtype
	 *            Э�����ͣ�VT100��VT52��VT220��VTNT��ANSI
	 * @param prompt
	 *            ���������ʶ
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
	 * ��¼��Ŀ������
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
	 * ��ȡ�������
	 * 
	 * @param pattern
	 *            ƥ�䵽���ַ���ʱ���ؽ��
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

				// ƥ�䵽������ʶʱ���ؽ��
				if (flag) {
					if (ch == lastChar && sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				} else {
					// ���ûָ��������ʶ,ƥ�䵽Ĭ�Ͻ�����ʶ�ַ�ʱ���ؽ��
					if (ch == promptChar)
						return sb.toString();
				}
				// ��¼ʧ��ʱ���ؽ��
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
	 * ��������
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
	 * ��������,����ִ�н��
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
	 * �ر�����
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
	 * ����Telnet������ش��������ݽ��
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
		TelnetOperator telnet = new TelnetOperator(); // Windows,��VT220,���������
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
