package com.ch.sockettest.until;

/**
 * 16������String/Byte֮���ת��������
 * 
 * @author fw
 */
public class HexUtil {

	/**
	 * �ַ���ת����16�����ַ���
	 * 
	 * @param str ��ת����ASCII�ַ���
	 * @return ת�����16�����ַ�����Byte֮���ÿո�ֿ����磺[61 6C 6B]��ÿ16��Byte����
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
	 * 16�����ַ���ת��Ϊ�ַ���
	 * 
	 * @param hexStr ��ת����16�����ַ�����Byte֮���޷ָ�������[616C6B]
	 * @return ת������ַ���
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
	 * Byte����ת��Ϊ16�����ַ���
	 * 
	 * @param bs ��ת��Byte����
	 * @returnת�����16�����ַ�����Byte֮���ÿո�ֿ���ÿ16��Byte����
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
	 * 16�����ַ���ת��ΪByte����
	 * 
	 * @param hexStr ��ת����16�����ַ�����Byte֮��û�зָ���
	 * @returnת�����Byte����
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