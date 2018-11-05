package com.ch.sockettest.until;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类 Created by fdw on 2018/9/21.
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

	public static void main(String[] args) {
		Enumeration<NetworkInterface> nis;
		try {
			nis = NetworkInterface.getNetworkInterfaces();
			while (nis.hasMoreElements())
				System.out.println(nis.nextElement());
		} catch (SocketException e) {
			e.printStackTrace();
		}

		 String wip = getIpAddrByName("wlan2");//Intel(R) Dual Band Wireless-AC8265
		 String yip = getIpAddrByName("eth8");// ASIX AX88772C USB2.0 to Fast Ethernet Adapter
		 System.out.println("无线ip address: " + wip);
		 System.out.println("有线ip address: " + yip);
	}
}
