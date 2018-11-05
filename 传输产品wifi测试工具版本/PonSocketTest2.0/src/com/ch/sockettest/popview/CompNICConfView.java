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

import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.LabelShow;
import com.ch.sockettest.wifi.HttpUtils;
import com.ch.sockettest.wifi.TelnetOperator;

/**
 * 双网卡配置视图
 * 
 * @author fdw
 * 
 */
public class CompNICConfView extends AbsPopWindow {

	private Combo combo1;
	private Text txtip1;
	private Text txtip2;
	private Combo combo2;
	private Label tips;
	private Text text_sn;
	private Text text_ssid;
	private Text text_pass;
	private Text txtip3;
	private Text txtip4;
	private Text text_sn1;
	private Text text_ssid1;
	private Text text_pass1;
	private Combo combo4;
	private Combo combo3;
	public static long lastStatusSetTime;
	private static Properties prop = new Properties();
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "NetConfig.xml";

	public CompNICConfView(Display display, Shell parent) {
		super(display);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setText("网络配置");
		setSize(796, 596);
		setParentShell(parent);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group1 = new Group(composite, SWT.NONE);
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("\u8BBE\u59071");
		group1.setBounds(10, 0, 354, 447);

		Label label = new Label(group1, SWT.NONE);
		label.setBounds(10, 28, 160, 17);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label.setText("\u6709\u7EBF\u7F51\u5361");

		combo1 = new Combo(group1, SWT.READ_ONLY);
		combo1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev1Cable();
			}
		});
		combo1.setBounds(10, 51, 288, 25);

		Label lblip = new Label(group1, SWT.NONE);
		lblip.setBounds(10, 83, 61, 17);
		lblip.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblip.setText("\u7F51\u5361IP");

		txtip1 = new Text(group1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip1.setBounds(77, 82, 221, 47);
		txtip1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		Label label_1 = new Label(group1, SWT.NONE);
		label_1.setBounds(10, 147, 160, 17);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setText("\u65E0\u7EBF\u7F51\u5361");

		combo2 = new Combo(group1, SWT.READ_ONLY);
		combo2.setBounds(10, 169, 288, 28);
		combo2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev1Wire();
			}
		});

		Label label_2 = new Label(group1, SWT.NONE);
		label_2.setBounds(10, 200, 61, 17);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setText("\u7F51\u5361IP");

		txtip2 = new Text(group1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip2.setBounds(77, 200, 221, 47);
		txtip2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		Label lblSn = new Label(group1, SWT.NONE);
		lblSn.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSn.setBounds(10, 278, 51, 20);
		lblSn.setText("SN");

		text_sn = new Text(group1, SWT.BORDER);
		text_sn.setBounds(77, 273, 221, 34);

		Label lblSsid = new Label(group1, SWT.NONE);
		lblSsid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSsid.setBounds(10, 338, 51, 20);
		lblSsid.setText("SSID");

		text_ssid = new Text(group1, SWT.BORDER);
		text_ssid.setBounds(77, 333, 221, 34);

		Label lblPass = new Label(group1, SWT.NONE);
		lblPass.setText("\u5BC6\u7801");
		lblPass.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblPass.setBounds(10, 397, 51, 20);

		text_pass = new Text(group1, SWT.BORDER | SWT.PASSWORD);
		text_pass.setBounds(77, 397, 221, 34);

		

		Group group = new Group(composite, SWT.NONE);
		group.setText("\u8BBE\u59072");
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group.setBounds(404, 0, 354, 447);

		Label label_5 = new Label(group, SWT.NONE);
		label_5.setText("\u6709\u7EBF\u7F51\u5361");
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_5.setBounds(10, 28, 160, 17);

		combo3 = new Combo(group, SWT.READ_ONLY);
		combo3.setBounds(10, 51, 288, 28);
		combo3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev2Cable();
			}
		});

		Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("\u7F51\u5361IP");
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_4.setBounds(10, 83, 61, 17);

		txtip3 = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		txtip3.setBounds(77, 82, 221, 47);

		Label label_7 = new Label(group, SWT.NONE);
		label_7.setText("\u65E0\u7EBF\u7F51\u5361");
		label_7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_7.setBounds(10, 147, 160, 17);

		combo4 = new Combo(group, SWT.READ_ONLY);
		combo4.setBounds(10, 169, 288, 28);
		combo4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDev2Wire();
			}
		});

		Label label_6 = new Label(group, SWT.NONE);
		label_6.setText("\u7F51\u5361IP");
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_6.setBounds(10, 200, 61, 17);

		txtip4 = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		txtip4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		txtip4.setBounds(77, 200, 221, 47);

		Label lblSn_1 = new Label(group, SWT.NONE);
		lblSn_1.setText("SN");
		lblSn_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSn_1.setBounds(10, 278, 51, 20);

		text_sn1 = new Text(group, SWT.BORDER);
		text_sn1.setBounds(77, 273, 221, 34);

		Label lblSsid_1 = new Label(group, SWT.NONE);
		lblSsid_1.setText("SSID");
		lblSsid_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblSsid_1.setBounds(10, 338, 51, 20);

		text_ssid1 = new Text(group, SWT.BORDER);
		text_ssid1.setBounds(77, 333, 221, 34);

		Label labelPass_1 = new Label(group, SWT.NONE);
		labelPass_1.setText("\u5BC6\u7801");
		labelPass_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labelPass_1.setBounds(10, 397, 51, 20);

		text_pass1 = new Text(group, SWT.BORDER | SWT.PASSWORD);
		text_pass1.setBounds(77, 397, 221, 34);
		
		tips = new Label(composite, SWT.NONE);
		tips.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tips.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		tips.setBounds(10, 475, 402, 42);

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					save(defConfPath);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		button_1.setText("\u4FDD\u5B58\u914D\u7F6E");
		button_1.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		button_1.setBounds(604, 475, 129, 42);
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
			prop.setProperty("Dev1_CableNet", combo1.getText());
			prop.setProperty("Dev1_WirelessNet", combo2.getText());
			prop.setProperty("Dev1_CableIp", txtip1.getText());
			prop.setProperty("Dev1_WirelessIp", txtip2.getText());
			prop.setProperty("Dev1_Sn", text_sn.getText());
			prop.setProperty("Dev1_SSID", text_ssid.getText());
			prop.setProperty("Dev1_Pass", text_pass.getText());

			prop.setProperty("Dev2_CableNet", combo3.getText());
			prop.setProperty("Dev2_WirelessNet", combo4.getText());
			prop.setProperty("Dev2_CableIp", txtip3.getText());
			prop.setProperty("Dev2_WirelessIp", txtip4.getText());
			prop.setProperty("Dev2_Sn", text_sn1.getText());
			prop.setProperty("Dev2_SSID", text_ssid1.getText());
			prop.setProperty("Dev2_Pass", text_pass1.getText());
			
			prop.storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			LabelShow.show(tips, "配置保存成功", SWT.COLOR_DARK_GREEN);
			close();
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
	private void load(String pathfile){
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
			
			LabelShow.show(tips, "配置导入成功", SWT.COLOR_DARK_GREEN);
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
		combo1.setText(prop.getProperty("Dev1_CableNet", ""));
		combo2.setText(prop.getProperty("Dev1_WirelessNet", ""));
		combo3.setText(prop.getProperty("Dev2_CableNet", ""));
		combo4.setText(prop.getProperty("Dev2_WirelessNet", ""));
		setDev1Cable();
		setDev1Wire();
		setDev2Cable();
		setDev2Wire();
	}

	@Override
	protected void createContents() {

	}

	@Override
	public void initData() {
		startStatusCleanner();
		List<String> list = IPUtil.getNIClist();
		String[] items = (String[]) list.toArray(new String[list.size()]);
		combo1.setItems(items);
		combo2.setItems(items);
		combo3.setItems(items);
		combo4.setItems(items);
		load(defConfPath);
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
            	LabelShow.show(tips,"Telnet已启用",SWT.COLOR_DARK_GREEN);
            }else {
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
	 * 设置设备1有线IP及SSID信息
	 */
	public void setDev1Cable() {
		String selected = combo1.getText();
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				txtip1.setText(ips);
				//启用Telnet
				EnableTelnet();
				// Telnet登录
				TelnetOperator telnet = new TelnetOperator();
				boolean flag = telnet.login("192.168.1.1", 23, ips, 0, "CMCCAdmin", "aDm8H%MdA");
				if (flag) {
					text_sn.setText(telnet.getResult("sismac 2 512"));// SN = CMDCA40007E6
					text_ssid.setText(telnet.getResult("sismac 2 1024"));//SSID1 = CMCC-FxFh
					text_pass.setText(telnet.getResult("sismac 2 1280"));//Password1 = 6nvsnett
					LabelShow.show(tips,"Telnet登录成功",SWT.COLOR_DARK_GREEN);
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
	/**
	 * 设置设备2有线IP及SSID信息
	 */
	public void setDev2Cable() {
		String selected = combo3.getText();
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				txtip3.setText(ips);
				//启用Telnet
				EnableTelnet();
				// Telnet登录
				TelnetOperator telnet = new TelnetOperator();
				//boolean flag = telnet.login("192.168.1.1", 23, ips, 0, "root", "Pon521");
				boolean flag = telnet.login("192.168.1.1", 23, ips, 0, "CMCCAdmin", "aDm8H%MdA");
				if (flag) {
					text_sn1.setText(telnet.getResult("sismac 2 512"));// SN = CMDCA40007E6
					text_ssid1.setText(telnet.getResult("sismac 2 1024"));//SSID1 = CMCC-FxFh
					text_pass1.setText(telnet.getResult("sismac 2 1280"));//Password1 = 6nvsnett
					LabelShow.show(tips,"Telnet登录成功",SWT.COLOR_DARK_GREEN);
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

	public void setDev1Wire() {
		String selected = combo2.getText();
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				txtip2.setText(ips);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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

	public static void main(String[] argvs) {
		CompNICConfView view = new CompNICConfView(Display.getDefault(), null);
		view.open();
	}
}
