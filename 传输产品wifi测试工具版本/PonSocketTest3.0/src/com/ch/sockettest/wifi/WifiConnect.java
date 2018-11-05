package com.ch.sockettest.wifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ch.sockettest.until.IPUtil;

public class WifiConnect {
	/**
	 * 根据SSID及Password连接wifi
	 * 
	 * @param ssid
	 * @param password
	 * @param selected
	 * @return
	 * @throws Exception
	 */
	public static boolean Connect(String ssid, String password, String selected) throws Exception {
		boolean flag = false;
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
		String[] infos = selected.trim().split("/");
		System.out.println(
				infos[2] + "////////////////////////////////////" + IPUtil.getWireInterfaceMap().get(infos[2]));
		// 添加配置文件
		exeCmd("netsh wlan add profile filename=wifiConfig.xml");
		String interName = IPUtil.getWireInterfaceMap().get(infos[2]);
		// 指定无线网络接口连接SSID
		String str = exeCmd("netsh wlan connect name=" + ssid + " interface=\"" + interName + "\"");
		System.out.println(str);
		TimeUnit.MILLISECONDS.sleep(2000);
		if (str.contains("已成功完成连接请求") && IPUtil.getNicInfo(selected) != null
				&& !IPUtil.getNicInfo(selected).equals("")) {
			flag = true;
		}
		return flag;
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

	/**
	 * 获取信号强度
	 * 
	 * @param ssid
	 * @param type
	 * @return
	 */
	public static String getInfo(String ssid, String type) {
		String wifiInfoStr = exeCmd("GetWifiInfo.exe list");
		String[] wifiInfos = wifiInfoStr.substring(wifiInfoStr.indexOf("SSID:")).split("dbm");
		for (String str : wifiInfos) {
			if (str.contains(ssid)) {
				System.out.println(str);
				if (type.contains("Signal Quality")) {
					String SignalQual = str.substring(str.indexOf("Signal Quality:"), str.indexOf("RSSI:"));
					return SignalQual.substring(SignalQual.indexOf(":") + 1).trim();
				} else if (type.contains("RSSI")) {
					String Rssi = str.substring(str.indexOf("RSSI:"));
					return Rssi.substring(Rssi.indexOf(":") + 1).trim();
				} else {
					return "";
				}
			}
		}
		return "";
	}

	public static void main(String[] args) {
		// Connect("World", "8888888");
		// System.out.println(Connect("PONTeam5G", "PonTeamOnly"));
		// 导出已有配置文件netsh wlan export profile key=clear（clear表示以明文方式显示密码）
		// System.out.println(exeCmd("netsh wlan export profile key=clear"));
		// System.out.println(exeCmd("netsh wlan delete profile
		// name=PONTeam5G"));
		// System.out.println(
		// exeCmd("netsh wlan add profile filename=WLAN-PONTeam5G.xml"));
		System.out.println(exeCmd("netsh wlan connect name=PONTeam5G interface=无线网络连接1"));
		// System.out.println(exeCmd("netsh wlan show profile"));

		// String str = exeCmd("Netsh WLAN show interfaces");
		// System.out.println(str);

		System.out.println(getInfo("PONTeam5G", "Signal Quality"));
		System.out.println(getInfo("PONTeam5G", "RSSI"));
	}

}
