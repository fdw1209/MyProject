package com.ch.sockettest.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.telnet.TelnetClient;
import org.eclipse.swt.SWT;
import org.json.JSONObject;

import com.ch.sockettest.main.MainView;
import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.StateUpdater;
import com.ch.sockettest.until.Utils;

/**
 * Telnet管理器
 * 
 * @author fdw
 *
 */
public class TelnetManager {

	private String ip = null;
	private String prompt = "#"; // 结束标识字符串,Windows中是>,Linux中是#
	private char promptChar = '#'; // 结束标识字符

	private boolean close = false;
	private boolean closed2 = true;
	private boolean closed3 = true;
	private boolean haslogin = false;

	private InputStreamReader isr = null;
	private OutputStreamWriter osr = null;
	private BufferedReader br = null;
	private PrintStream out; // 向服务器写入 命令

	private TelnetClient telnet = null;

	public TelnetManager(String ip) {
		this.ip = Utils.isIP(ip) ? ip : this.ip;

		telnet = new TelnetClient();

	}

	public void setIp(String ip) throws Exception {
		if (Utils.isIP(ip)) {
			this.ip = ip;
			haslogin = false;
			telnet.disconnect();
		} else {
			throw new Exception(ip + " is not a Ipaddress");
		}
	}

	public String getIp() {
		return this.ip;
	}

	public void start() {
		close = false;
		EnableTelnet();
//		tryConnect("Dev1");
//		tryConnect("Dev2");
//		login("CMCCAdmin", "aDm8H%MdA");
//		getData("sismac 2 512");
	}

	public boolean isClosed() {
		return closed2 && closed3;
	}

	/**
	 * 启用Telnet
	 */
	public void EnableTelnet() {
		/**
		 * 通过接口地址：http://192.168.1.1/app_telnet_info.gch打开Telnet开关
		 */
		String url = "http://192.168.1.1/app_telnet_info.gch";
		Map<String, String> params = new HashMap<>();
		params.put("Username", "CMCCAdmin");
		params.put("Password", "aDm8H%MdA");
		params.put("ENABLE_FLAG", "1");
		try {
			String response = HttpUtils.sendPostMethod(url, params, "utf-8");
			JSONObject jsonObject = new JSONObject(response.replace(";", ""));
			String resultCode = jsonObject.getString("ResultCode");
			if (resultCode.equals("200")) {
				MainView.getInstance().setStatusString("Telnet已启用",SWT.COLOR_DARK_GREEN);
			} else {
				MainView.getInstance().setStatusString("Telnet启用失败",SWT.COLOR_RED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据设备名连接
	 * @param devName
	 */
	public void tryConnect(String devName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (close == false && StateUpdater.closed == false) {
					if (telnet.isConnected() == false) {
						haslogin = false;
						isr = null;
						osr = null;
						br = null;
						try {
							if (devName.equals("Dev1")) {
								//connect(ip, 23, "192.168.1.10", 0);
								connect(ip, 23, IPUtil.getNicInfo(MainView.getProp().getProperty("Dev1_CableNet")), 0);
							} else {
								connect(ip, 23, IPUtil.getNicInfo(MainView.getProp().getProperty("Dev2_CableNet")), 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}, "TelnetconectChecker").start();
	}
	/**
	 * 连接目标Telnet
	 * @param ip
	 * @param port
	 * @param localip
	 * @param localport
	 * @throws Exception
	 */
	private void connect(String ip, int port, String localip, int localport) throws Exception {
		if (!Utils.isIP(ip))
			return;
		telnet.connect(ip, port, InetAddress.getByName(localip), localport);
		if (telnet.isConnected()) {
			isr = new InputStreamReader(telnet.getInputStream());
			osr = new OutputStreamWriter(telnet.getOutputStream());
			br = new BufferedReader(isr);
			out = new PrintStream(telnet.getOutputStream());
		}
	}
	/**
	 * 登录Telnet
	 * @param username
	 * @param password
	 */
	public void login(String username, String password) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String line = "";
				int c = 0;
				closed2 = false;
				try {
					while (close == false && StateUpdater.closed == false) {
						if (telnet.isConnected() && !haslogin) {
							while (br != null && ((c = br.read()) != -1)) {
								//System.out.print((char) c);
								line += (char) c;
								haslogin = loginByDeviceType(line, (char) c, username, password);
								if (haslogin) {
									System.out.println("Telnet登录成功");
									//MainView.getInstance().setStatus("Telnet登录成功", SWT.COLOR_DARK_GREEN, false);
									break;
								}
							}
						}
						TimeUnit.MILLISECONDS.sleep(1500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				closed2 = true;
			}

		}, "TelnetLogin").start();
	}
	/**
	 * 输入用户名及密码
	 * @param line
	 * @param c
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private boolean loginByDeviceType(String line, char c, String username, String password) throws Exception {
		boolean b = false;
		if (c == ':' && line.endsWith("Login:")) {
			osr.write(username);
			osr.write("\n");
			osr.flush();
			line = "";
		}
		if (c == ':' && line.endsWith("Password:")) {
			osr.write(password);
			osr.write("\n");
			osr.flush();
			line = "";
		}
		if (c == '#' && line.endsWith("#")) {
			b = true;
		}
		return b;
	}
	/**
	 * 获取返回数据
	 * @param cmd
	 */
	public void getData(String cmd) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				closed3 = false;
				while (close == false && StateUpdater.closed == false) {
					int c = 0;
					String line = "";
					try {
						if (haslogin && cmd != null) {
							osr.write(cmd);
							osr.write("\n");
							osr.flush();
							while ((c = br.read()) != -1) {
								//System.out.print((char) c);
								line += (char) c;
								if (getDataByDeviceType(line, (char) c)) {
									break;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						haslogin = false;
					}
					try {
						TimeUnit.MILLISECONDS.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				closed3 = true;
			}

		}, "TelnetRefreshData").start();
	}

	private boolean getDataByDeviceType(String line, char c) {
		boolean b = false;
		if ((char) c == promptChar) {
			String rs = line.substring(line.indexOf(":") + 1, line.lastIndexOf("tag")).trim();
			try {
				String result = Utils.asciiString2String(rs, 16);
				System.out.println(result+"!!!!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
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
			while ((code = isr.read()) != -1) {
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

	/**
	 * 关闭连接
	 */
	public void close() {
		close = true;
		if (telnet != null) {
			try {
				telnet.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		TelnetManager telnet = new TelnetManager("192.168.1.1"); // Windows,用VT220,否则会乱码
		telnet.start();
		// String rs = telnet.sendCommand("sismac show");
		// String rs = telnet.sendCommand("sismac 2 512");// SN = CMDCA40007E6
		// String rs = telnet.sendCommand("sismac 2 1024");//SSID1 = CMCC-FxFh
		// String rs = telnet.sendCommand("sismac 2 1280");//Password1 = 6nvsnett
		// String rs = telnet.sendCommand("sismac 2 1028");//SSID2 = CMCC-FxFh-5G
//		String rs = telnet.sendCommand("sismac 2 1296");// Password2 = 6nvsnett
//		rs = rs.substring(rs.indexOf(":") + 1, rs.lastIndexOf("tag")).trim();
//		System.out.println(rs);
//		try {
//			System.out.println(Utils.asciiString2String(rs, 16));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
