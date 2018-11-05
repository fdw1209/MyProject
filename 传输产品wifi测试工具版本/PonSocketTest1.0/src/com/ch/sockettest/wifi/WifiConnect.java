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
	 * 根据SSID及Password连接wifi
	 * 
	 * @param ssid
	 * @param password
	 * @return 
	 */
	public static String Connect(String ssid, String password) {
		// 修改配置文件
		String defConfPath = System.getProperty("user.dir") + File.separator + "wifiConfig.xml";
		// 使用SAXReader解析xml文档
		SAXReader saxReader = new SAXReader();
		try {
			// dom4j处理带命名空间的XML
			Map<String, String> map = new HashMap<String, String>();
			map.put("wlan", "http://www.microsoft.com/networking/WLAN/profile/v1");
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
			// 读取xml文档
			Document doc = saxReader.read(new File(defConfPath));
			doc.selectSingleNode("//wlan:name[1]").setText(ssid);
			doc.selectSingleNode("//wlan:SSIDConfig/wlan:SSID/wlan:name").setText(ssid);
			doc.selectSingleNode("//wlan:keyMaterial").setText(password);
			// 指定文件输出的位置
			FileOutputStream outputStream = new FileOutputStream(defConfPath);
			// 指定文本的写出的格式
			OutputFormat format = OutputFormat.createPrettyPrint(); // 漂亮格式：有空格换行
			format.setEncoding("UTF-8");
			// 创建写出对象
			XMLWriter writer = new XMLWriter(outputStream, format);
			// 写出Document对象
			writer.write(doc);
			// 关闭流
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 添加配置文件
		exeCmd("netsh wlan add profile filename=wifiConfig.xml");
		// 连接SSID
		String str = exeCmd("netsh wlan connect name=" + ssid);
		return str;
	}

	/**
	 * @param commandStr
	 *            cmd 控制台命令
	 * @return 该控制台命令commandStr运行的结果
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
		// 导出已有配置文件netsh wlan export profile key=clear（clear表示以明文方式显示密码）
		// System.out.println(exeCmd("netsh wlan export profile key=clear"));
		// System.out.println(exeCmd("netsh wlan delete profile
		// name=PONTeam5G"));
		// System.out.println(
		// exeCmd("netsh wlan add profile filename=WLAN-PONTeam5G.xml"));
		// System.out.println(exeCmd("netsh wlan connect name=CMCC-FxFh"));
		// System.out.println(exeCmd("netsh wlan show profile"));
	}

}
