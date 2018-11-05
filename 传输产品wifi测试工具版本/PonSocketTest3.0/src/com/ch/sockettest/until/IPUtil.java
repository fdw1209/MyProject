package com.ch.sockettest.until;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip工具类 Created by fdw on 2018/9/21.
 */
public class IPUtil {

	/**
	 * 检查IP是否合法
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean ipValid(String ip) {
		String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
		regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(ip);
		return m.matches();
	}

	/**
	 * 获取本地ip 适合windows与linux
	 *
	 * @return
	 */
	public static String getLocalIP() {
		String localIP = "127.0.0.1";
		try {
			Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				InetAddress ip = ni.getInetAddresses().nextElement();
				if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					localIP = ip.getHostAddress();
					break;
				}
			}
		} catch (Exception e) {
			try {
				localIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		}
		return localIP;
	}

	/**
	 * 获取客户机的ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

	/**
	 * 把ip转化为整数
	 * 
	 * @param ip
	 * @return
	 */
	public static long translateIP2Int(String ip) {
		String[] intArr = ip.split("\\.");
		int[] ipInt = new int[intArr.length];
		for (int i = 0; i < intArr.length; i++) {
			ipInt[i] = new Integer(intArr[i]).intValue();
		}
		return ipInt[0] * 256 * 256 * 256 + +ipInt[1] * 256 * 256 + ipInt[2] * 256 + ipInt[3];
	}

	/**
	 * 通过网络接口的名称来获取ip地址
	 * 
	 * @param networkInterfaceName
	 *            网络接口名
	 * @return ip地址
	 */
	public static String getIpAddrByName(String networkInterfaceName) {
		// get network interface by name
		try {
			NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
			if (networkInterface == null) {
				return null;
			}
			// System.out.println("network interface: " +
			// networkInterface.getDisplayName());

			InetAddress ipAddress = null;
			// get all ip addresses band to this interface
			Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

			while (addresses.hasMoreElements()) {
				ipAddress = addresses.nextElement();

				if (ipAddress != null && ipAddress instanceof Inet4Address) {
					// System.out.println("ip address: " +
					// ipAddress.getHostAddress());
					return ipAddress.getHostAddress();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取本机所有IP
	 * 
	 * @return ip地址集合
	 */
	public static String[] getAllLocalHostIP() {
		List<String> res = new ArrayList<String>();
		Enumeration<?> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<?> nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					ip = (InetAddress) nii.nextElement();
					if (ip.getHostAddress().indexOf(":") == -1) {
						res.add(ip.getHostAddress());
						// System.out.println("本机的ip=" + ip.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return (String[]) res.toArray(new String[0]);
	}

	/**
	 * 获取有线网卡列表
	 * 
	 * @return
	 */
	public static List<String> getNIClist() {

		List<String> list = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("route", "print");
		Process p = null;
		BufferedReader br = null;
		try {
			p = pb.start();
			int exitValue = 0;// p.waitFor();
			if (exitValue == 0) {
				InputStream is = p.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				String line = null;

				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					if (line.contains("......") && !line.contains("Loopback") && !line.contains("Virtual")) {
						StringBuilder str = new StringBuilder();
						str.append(line.substring(0, line.indexOf("...")).trim());
						str.append("/");
						str.append(line.substring(line.indexOf("...") + 3, line.indexOf("......")).trim());
						str.append("/");
						str.append(line.substring(line.indexOf("......") + 6).trim());
						list.add(str.toString());

					}
					if (line.contains("永久路由:")) {
						break;
					}
				}
			} else {
				InputStream is = p.getErrorStream();
				br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	/**
	 * 获取网卡详细信息
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String getNicInfo(String name) throws Exception {

		Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
		StringBuilder sb = new StringBuilder();
		name = name.trim();
		String[] infos = name.split("/");
		if (infos.length != 3)
			return null;

		while (en.hasMoreElements()) {
			NetworkInterface ni = en.nextElement();
			byte[] ha = ni.getHardwareAddress();
			if (ha == null || ha.length == 0)
				continue;
			String mac = HexUtil.bytes2Hex(ha);
			if (Utils.validatemac(mac, infos[1])) {
				List<InterfaceAddress> list = ni.getInterfaceAddresses();
				Iterator<InterfaceAddress> it = list.iterator();

				while (it.hasNext()) {
					InterfaceAddress ia = it.next();
					String host = ia.getAddress().getHostAddress();
					// System.out.println(host);
					if (!host.contains("%")) {
						sb.append(host);
					}
				}
			}

		}
		return sb.toString();
	}

	/**
	 * 获取系统无线接口map集合
	 * key:无线网卡名称
	 * value:无线接口名
	 * @return
	 */
	public static Map<String, String> getWireInterfaceMap() {
		Map<String, String> map = new HashMap<>();
		List<String> list = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("Netsh", "WLAN", "show", "interfaces");
		Process p = null;
		BufferedReader br = null;
		try {
			p = pb.start();
			int exitValue = 0;// p.waitFor();
			if (exitValue == 0) {
				InputStream is = p.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				String line = null;

				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					if (line.contains("名称") || line.contains("描述")) {
						StringBuilder str = new StringBuilder();
						str.append(line.substring(line.indexOf(":") + 1).trim());
						list.add(str.toString());
					}
				}
				if (list.size() % 2 == 0) {
					for (int i = 0; (2 * i + 1) < list.size(); i++) {
						map.put(list.get(2 * i + 1), list.get(2 * i));
					}
				}

			} else {
				InputStream is = p.getErrorStream();
				br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static void main(String[] args) {
		// Enumeration<NetworkInterface> nis;
		// try {
		// nis = NetworkInterface.getNetworkInterfaces();
		// while (nis.hasMoreElements()){
		// System.out.println(nis.nextElement());
		// }
		// } catch (SocketException e) {
		// e.printStackTrace();
		// }
		List<String> lists = getNIClist();
		for (String str : lists) {
			System.out.println(str + "!!!");
			try {
				System.out.println(getNicInfo(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// String wip = getIpAddrByName("wlan2");//Intel(R) Dual Band
		// Wireless-AC8265
		// String yip = getIpAddrByName("eth8");// ASIX AX88772C USB2.0 to Fast
		// Ethernet Adapter
		// System.out.println("无线ip address: " + wip);
		// System.out.println("有线ip address: " + yip);

//		Map<String, String> map = getWireInterfaceMap();
//		// 遍历1 ： 通过键值对对象entrySet获取键与值
//		Set<Entry<String, String>> entrySet = map.entrySet();
//		for (Entry<String, String> entry : entrySet) {
//			String key = entry.getKey();
//			String value = entry.getValue();
//			System.out.println(key + ":" + value);
//		}
	}
}
