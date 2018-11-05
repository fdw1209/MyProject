package com.ch.sockettest.popview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.json.JSONObject;

import com.ch.sockettest.socket.PostThread;
import com.ch.sockettest.socket.ReceiveThread;
import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.LabelShow;
import com.ch.sockettest.wifi.HttpUtils;
import com.ch.sockettest.wifi.TelnetOperator;
import com.ch.sockettest.wifi.WifiConnect;

/**
 * 双网卡配置视图
 * 
 * @author fdw
 * 
 */
public class CompDev2NICConfView extends AbsPopWindow {

	private Combo combo3;
	private Text txtip3;
	private Text txtip4;
	private Combo combo4;
	private Label tips;
	private Text text_sn1;
	private Text text_ssid1;
	private Text text_pass1;
	private Button btn_R3;
	private Button btn_R4;
	private TelnetOperator telnet = new TelnetOperator();
	public static boolean isConfirm = false;
	public static long lastStatusSetTime;
	private static Properties prop = new Properties();
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "NetConfig.xml";

	public CompDev2NICConfView(Display display, Shell parent) {
		super(display);
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				telnet.distinct();
			}
		});
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setText("网络配置");
		setSize(488, 663);
		setParentShell(parent);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group1 = new Group(composite, SWT.NONE);
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("\u8BBE\u59072");
		group1.setBounds(10, 0, 440, 502);

		Label label = new Label(group1, SWT.NONE);
		label.setBounds(67, 21, 160, 17);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label.setText("\u6709\u7EBF\u7F51\u5361");

		combo3 = new Combo(group1, SWT.READ_ONLY);
		combo3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev2Cable();
			}
		});
		combo3.setBounds(67, 44, 288, 25);

		Label lblip = new Label(group1, SWT.NONE);
		lblip.setBounds(67, 76, 61, 17);
		lblip.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblip.setText("\u7F51\u5361IP");

		txtip3 = new Text(group1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip3.setBounds(134, 75, 221, 47);
		txtip3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		Label label_1 = new Label(group1, SWT.NONE);
		label_1.setBounds(67, 140, 160, 17);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setText("\u65E0\u7EBF\u7F51\u5361");

		combo4 = new Combo(group1, SWT.READ_ONLY);
		combo4.setBounds(67, 162, 288, 28);
		combo4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev2Wire();
			}
		});

		Label label_2 = new Label(group1, SWT.NONE);
		label_2.setBounds(67, 193, 61, 17);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setText("\u7F51\u5361IP");

		txtip4 = new Text(group1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip4.setBounds(134, 193, 221, 47);
		txtip4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		Label lblSn = new Label(group1, SWT.NONE);
		lblSn.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSn.setBounds(67, 271, 51, 20);
		lblSn.setText("SN");

		text_sn1 = new Text(group1, SWT.BORDER);
		text_sn1.setBounds(134, 266, 221, 34);

		Label lblSsid = new Label(group1, SWT.NONE);
		lblSsid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSsid.setBounds(67, 331, 51, 20);
		lblSsid.setText("SSID");

		text_ssid1 = new Text(group1, SWT.BORDER);
		text_ssid1.setBounds(134, 326, 221, 34);

		Label lblPass = new Label(group1, SWT.NONE);
		lblPass.setText("\u5BC6\u7801");
		lblPass.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblPass.setBounds(67, 390, 51, 20);

		text_pass1 = new Text(group1, SWT.BORDER | SWT.PASSWORD);
		text_pass1.setBounds(134, 390, 221, 34);

		Label label_3 = new Label(group1, SWT.NONE);
		label_3.setText("\u7F51\u7EDC\u6807\u51C6");
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_3.setBounds(67, 447, 61, 20);

		btn_R3 = new Button(group1, SWT.RADIO);
		btn_R3.setAlignment(SWT.CENTER);
		btn_R3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R3.setSelection(true);
				btn_R4.setSelection(false);
				setSSIDAndPass(telnet);
			}
		});
		btn_R3.setSelection(true);
		btn_R3.setBounds(147, 447, 61, 20);
		btn_R3.setText("2.4G");

		btn_R4 = new Button(group1, SWT.RADIO);
		btn_R4.setAlignment(SWT.CENTER);
		btn_R4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R4.setSelection(true);
				btn_R3.setSelection(false);
				setSSIDAndPass(telnet);
			}
		});
		btn_R4.setText("5G");
		btn_R4.setBounds(279, 447, 51, 20);

		tips = new Label(composite, SWT.NONE);
		tips.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tips.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		tips.setBounds(10, 528, 251, 42);

		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// 连接指定wifi
					boolean isConnect = WifiConnect.Connect(text_ssid1.getText(), text_pass1.getText(), combo4.getText());
					if (!isConnect) {
						TimeUnit.MILLISECONDS.sleep(1000);
						isConnect = WifiConnect.Connect(text_ssid1.getText(), text_pass1.getText(), combo4.getText());
						if (!isConnect) {
							// "线程停止"限制
							PostThread.stop = true;
							ReceiveThread.stop = true;
							MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
							mb.setText("错误提示");
							mb.setMessage("WIFI连接失败!\n");
							if (mb.open() == SWT.OK) {
								LabelShow.show(tips, "WiFi连接失败", SWT.COLOR_DARK_RED);
							}
						} else {
							// 取消"线程停止"限制
							PostThread.stop = false;
							ReceiveThread.stop = false;
							setDev2Wire();
							LabelShow.show(tips, "WiFi连接成功", SWT.COLOR_DARK_GREEN);
							// 保存配置
							save(defConfPath);
							if (!txtip4.getText().equals("")) {
								isConfirm = true;
								close();
							}
						}
					} else {
						// 取消"线程停止"限制
						PostThread.stop = false;
						ReceiveThread.stop = false;
						setDev2Wire();
						LabelShow.show(tips, "WiFi连接成功", SWT.COLOR_DARK_GREEN);
						// 保存配置
						save(defConfPath);
						if (!txtip4.getText().equals("")) {
							isConfirm = true;
							close();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		button_2.setText("\u786E\u8BA4");
		button_2.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		button_2.setBounds(304, 528, 129, 42);
	}

	/**
	 * 保存配置文件
	 * 
	 * @param pathfile
	 * @throws Exception
	 */
	private void save(String pathfile) throws Exception {
		try {
			String file = null;
			if (pathfile == null) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setText("保存配置文件");
				fd.setFileName("NetConfig.xml");
				fd.setFilterExtensions(new String[] { "*.xml" });
				file = fd.open();
				if (file == null)
					return;
			} else {
				file = pathfile;
			}
			prop.setProperty("Dev2_CableNet", combo3.getText());
			prop.setProperty("Dev2_WirelessNet", combo4.getText());
			prop.setProperty("Dev2_CableIp", txtip3.getText());
			prop.setProperty("Dev2_WirelessIp", txtip4.getText());
			prop.setProperty("Dev2_Sn", text_sn1.getText());
			prop.setProperty("Dev2_SSID", text_ssid1.getText());
			prop.setProperty("Dev2_Pass", text_pass1.getText());
			prop.setProperty("Dev2_NetType", btn_R3.getSelection() ? "2.4G" : "5G");

			prop.storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			LabelShow.show(tips, "配置保存成功", SWT.COLOR_DARK_GREEN);
		} catch (Exception e1) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("配置保存失败:\n" + e1.getMessage());
			mb.open();
		}

	}

	/**
	 * 读取配置文件
	 * 
	 * @param pathfile
	 * @throws Exception
	 */
	private void load(String pathfile) {
		try {
			String file = null;
			if (pathfile == null) {
				FileDialog fd = new FileDialog(getShell());
				fd.setText("请选择一个配置文件进行导入");
				fd.setFilterExtensions(new String[] { "*.xml" });
				file = fd.open();
				if (file == null)
					return;
			} else {
				file = pathfile;
			}
			Properties p = new Properties();
			p.loadFromXML(new FileInputStream(file));
			prop.putAll(p);
			// 导入数据
			loadData();
			// LabelShow.show(tips, "配置导入成功", SWT.COLOR_DARK_GREEN);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("配置导入失败:\n" + e.getMessage());
			mb.open();
		}

	}

	/**
	 * 导入数据
	 */
	private void loadData() {
		combo3.setText(prop.getProperty("Dev2_CableNet", ""));
		combo4.setText(prop.getProperty("Dev2_WirelessNet", ""));
		btn_R3.setSelection(prop.getProperty("Dev2_NetType").equals("2.4G") ? true : false);
		btn_R4.setSelection(prop.getProperty("Dev2_NetType").equals("5G") ? true : false);
	}

	@Override
	protected void createContents() {

	}

	@Override
	public void initData() {
		startStatusCleanner();
		List<String> list = IPUtil.getNIClist();
		String[] items = (String[]) list.toArray(new String[list.size()]);
		combo3.setItems(items);
		combo4.setItems(items);
		isConfirm = false;
		load(defConfPath);
		setDev2Cable();
		setDev2Wire();
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
				LabelShow.show(tips, "Telnet已启用", SWT.COLOR_DARK_GREEN);
			} else {
				MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Telnet启用失败!\n");
				mb.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 状态字符清除
	 */
	public void startStatusCleanner() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (tips.isDisposed() == false) {
					if (lastStatusSetTime > 0 && System.currentTimeMillis() - lastStatusSetTime > 1000) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								tips.setText("");
							}
						});
						lastStatusSetTime = 0;
					}
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 设置设备2有线IP及SSID信息
	 */
	public void setDev2Cable() {
		String selected = combo3.getText();
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				txtip3.setText(ips);
				// 启用Telnet
				EnableTelnet();
				// Telnet登录
				boolean flag = telnet.login("192.168.1.1", 23, ips, 0, "CMCCAdmin", "aDm8H%MdA");
				if (flag) {
					setSSIDAndPass(telnet);
					LabelShow.show(tips, "Telnet登录成功", SWT.COLOR_DARK_GREEN);
				} else {
					MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("Telnet登录失败!\n");
					mb.open();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void setSSIDAndPass(TelnetOperator telnet) {
		text_sn1.setText(telnet.getResult("sismac 2 512"));// SN = CMDCA40007E6
		if (btn_R3.getSelection()) {
			text_ssid1.setText(telnet.getResult("sismac 2 1024"));// SSID1 =
																	// CMCC-FxFh
			text_pass1.setText(telnet.getResult("sismac 2 1280"));// Password1 =
																	// 6nvsnett
		} else {
			text_ssid1.setText(telnet.getResult("sismac 2 1028"));// SSID1 =
																	// CMCC-FxFh-5G
			text_pass1.setText(telnet.getResult("sismac 2 1280"));// Password1 =
																	// 6nvsnett
		}

	}

	public void setDev2Wire() {
		String selected = combo4.getText();
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				txtip4.setText(ips);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 获取当前的配置信息
	 * 
	 * @return
	 */
	public static Properties getProp() {
		return prop;
	}

	public static void main(String[] argvs) {
		CompDev2NICConfView view = new CompDev2NICConfView(Display.getDefault(), null);
		view.open();
	}
}
