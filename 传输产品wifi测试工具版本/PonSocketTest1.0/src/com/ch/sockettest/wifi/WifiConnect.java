package com.ch.sockettest.wifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class WifiConnect {
	/**
	 * ����SSID��Password����wifi
	 * 
	 * @param ssid
	 * @param password
	 * @return 
	 */
	public static String Connect(String ssid, String password) {
		// �޸������ļ�
		String defConfPath = System.getProperty("user.dir") + File.separator + "wifiConfig.xml";
		// ʹ��SAXReader����xml�ĵ�
		SAXReader saxReader = new SAXReader();
		try {
			// dom4j����������ռ��XML
			Map<String, String> map = new HashMap<String, String>();
			map.put("wlan", "http://www.microsoft.com/networking/WLAN/profile/v1");
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
			// ��ȡxml�ĵ�
			Document doc = saxReader.read(new File(defConfPath));
			doc.selectSingleNode("//wlan:name[1]").setText(ssid);
			doc.selectSingleNode("//wlan:SSIDConfig/wlan:SSID/wlan:name").setText(ssid);
			doc.selectSingleNode("//wlan:keyMaterial").setText(password);
			// ָ���ļ������λ��
			FileOutputStream outputStream = new FileOutputStream(defConfPath);
			// ָ���ı���д���ĸ�ʽ
			OutputFormat format = OutputFormat.createPrettyPrint(); // Ư����ʽ���пո���
			format.setEncoding("UTF-8");
			// ����д������
			XMLWriter writer = new XMLWriter(outputStream, format);
			// д��Document����
			writer.write(doc);
			// �ر���
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��������ļ�
		exeCmd("netsh wlan add profile filename=wifiConfig.xml");
		// ����SSID
		String str = exeCmd("netsh wlan connect name=" + ssid);
		return str;
	}

	/**
	 * @param commandStr
	 *            cmd ����̨����
	 * @return �ÿ���̨����commandStr���еĽ��
	 */
	public static String exeCmd(String commandStr) {
		String result = null;
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec(commandStr);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// Connect("World", "8888888");
		// Connect("PONTeam5G");
		// �������������ļ�netsh wlan export profile key=clear��clear��ʾ�����ķ�ʽ��ʾ���룩
		// System.out.println(exeCmd("netsh wlan export profile key=clear"));
		// System.out.println(exeCmd("netsh wlan delete profile
		// name=PONTeam5G"));
		// System.out.println(
		// exeCmd("netsh wlan add profile filename=WLAN-PONTeam5G.xml"));
		// System.out.println(exeCmd("netsh wlan connect name=CMCC-FxFh"));
		// System.out.println(exeCmd("netsh wlan show profile"));
	}

}
