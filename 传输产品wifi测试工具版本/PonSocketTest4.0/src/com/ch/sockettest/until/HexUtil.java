package com.ch.sockettest.until;

/**
 * 16进制与String/Byte之间的转换工具类
 * 
 * @author fw
 */
public class HexUtil {

	/**
	 * 字符串转换成16进制字符串
	 * 
	 * @param str 待转换的ASCII字符串
	 * @return 转换后的16进制字符串，Byte之间用空格分开，如：[61 6C 6B]，每16个Byte换行
	 */
	public static String str2Hex(String str) {
		char[] hexChars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0xf0) >> 4;
			sb.append(hexChars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(hexChars[bit]);
			sb.append(" ");
			if (i != 0 && i % 16 == 0) {
				sb.append("\n");
			}
		}
		return sb.toString().trim();
	}

	/**
	 * 16进制字符串转换为字符串
	 * 
	 * @param hexStr 待转换的16进制字符串，Byte之间无分隔符，如[616C6B]
	 * @return 转换后的字符串
	 */
	public static String hex2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexChars = hexStr.toCharArray();
		byte[] bs = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bs.length; i++) {
			n = str.indexOf(hexChars[2 * i]) << 4;
			n += str.indexOf(hexChars[2 * i + 1]);
			bs[i] = (byte) (n & 0xff);
		}
		return new String(bs);
	}

	/**
	 * Byte数组转换为16进制字符串
	 * 
	 * @param bs 待转换Byte数组
	 * @return转换后的16进制字符串，Byte之间用空格分开，每16个Byte换行
	 */
	public static String bytes2Hex(byte[] bs) {
		String temp = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			temp = Integer.toHexString(bs[i] & 0xff);
			sb.append((temp.length() == 1) ? "0" + temp : temp);
			sb.append(" ");
			if (i != 0 && i % 16 == 0) {
				sb.append("\n");
			}
		}
		return sb.toString().toUpperCase().trim();
	}

	/**
	 * 16进制字符串转换为Byte数组
	 * 
	 * @param hexStr 待转换的16进制字符串，Byte之间没有分隔符
	 * @return转换后的Byte数组
	 */
	public static byte[] hex2Bytes(String hexStr) {
		byte[] bs = new byte[hexStr.length() / 2];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = Byte.decode("0x" + hexStr.substring(i * 2, i * 2 + 1)
					+ hexStr.substring(i * 2 + 1, (i + 1) * 2));
		}
		return bs;
	}

}