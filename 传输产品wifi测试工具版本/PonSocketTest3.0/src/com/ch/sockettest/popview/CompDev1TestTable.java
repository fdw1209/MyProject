package com.ch.sockettest.popview;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.State;
import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.main.MainView;
import com.ch.sockettest.socket.PostThread;
import com.ch.sockettest.socket.ReceiveThread;
import com.ch.sockettest.socket.ServerThread;
import com.ch.sockettest.socket.Serversocket;
import com.ch.sockettest.socket.StopThread;
import com.ch.sockettest.until.ExportTestReport;
import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.StateUpdater;
import com.ch.sockettest.until.XmlParse;
import com.ch.sockettest.wifi.TelnetOperator;
import com.ch.sockettest.wifi.WifiConnect;

/**
 * 测试数据列表
 * 
 * @author fdw
 *
 */
public class CompDev1TestTable extends AbstractComposite {
	private final String DevName = "Dev1";
	private Label text;
	private static Table table;
	private Button btnStart;
	private Label lbl_result;
	public static List<WifiData> wifiDatas;
	private Text text_1;
	private Text text_2;
	private boolean stop = false;
	private TelnetOperator telnet;
	protected boolean stopUpdate = false;
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";

	public CompDev1TestTable(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setSize(790, 600);
		setLayout(new GridLayout(1, false));

		Composite comp = new Composite(this, SWT.BORDER);
		comp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp.setLayout(new GridLayout(1, false));

		final Composite composite = new Composite(comp, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_composite = new GridData(GridData.FILL_HORIZONTAL);
		gd_composite.heightHint = 52;
		composite.setLayoutData(gd_composite);

		text = new Label(composite, SWT.NONE);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.BOLD));
		text.setText("\u8BBE\u59071\u6D4B\u8BD5\u6570\u636E\u5217\u8868");
		text.setBounds(10, 19, 190, 28);

		btnStart = new Button(composite, SWT.CENTER);
		btnStart.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				/**
				 * 开始测试，打开网络配置窗口
				 */
				CompDev1NICConfView view = new CompDev1NICConfView(getDisplay(), getShell());
				view.open();
				if (view.isDisposed() && CompDev1NICConfView.isConfirm) {
					startTest();
				}
			}
		});
		btnStart.setBounds(627, 13, 110, 33);
		btnStart.setText("\u5F00\u59CB\u6D4B\u8BD5");

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setBounds(217, 22, 76, 20);
		label_1.setText("\u4FE1\u53F7\u8D28\u91CF");

		text_1 = new Text(composite, SWT.BORDER | SWT.CENTER);
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_1.setBounds(314, 16, 73, 30);

		Label lblRssi = new Label(composite, SWT.NONE);
		lblRssi.setText("RSSI");
		lblRssi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblRssi.setBounds(416, 22, 41, 20);

		text_2 = new Text(composite, SWT.BORDER | SWT.CENTER);
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_2.setBounds(475, 16, 73, 30);

		Label lblDbm = new Label(composite, SWT.NONE);
		lblDbm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblDbm.setBounds(565, 21, 41, 20);
		lblDbm.setText("dbm");

		Label label = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite composite_1 = new Composite(comp, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(GridData.FILL_BOTH));

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				TableColumn[] tcs = table.getColumns();
				if (tcs.length == 7) {
					int m = 0;
					for (int i = 0; i < tcs.length - 1; i++) {
						m += tcs[i].getWidth();
					}
					tcs[tcs.length - 1].setWidth(table.getBounds().width - m);
				}
			}
		});

		TableColumn tblclmnClom = new TableColumn(table, SWT.CENTER);
		tblclmnClom.setWidth(66);
		tblclmnClom.setText("\u7F16\u53F7");

		TableColumn tblclmnClom_1 = new TableColumn(table, SWT.CENTER);
		tblclmnClom_1.setWidth(62);
		tblclmnClom_1.setText("\u56FE\u6807");

		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("\u4E0A\u4E0B\u884C");

		TableColumn tblclmnColom_3 = new TableColumn(table, SWT.CENTER);
		tblclmnColom_3.setWidth(150);
		tblclmnColom_3.setText("\u5F53\u524D\u901F\u7387(Mbps)");

		TableColumn tblclmnColom_4 = new TableColumn(table, SWT.CENTER);
		tblclmnColom_4.setWidth(139);
		tblclmnColom_4.setText("\u5E73\u5747\u901F\u7387(Mbps)");
		TableColumn tblclmnCol_5 = new TableColumn(table, SWT.CENTER);
		tblclmnCol_5.setWidth(118);
		tblclmnCol_5.setText("\u72B6\u6001");

		TableColumn tblclmnCol_6 = new TableColumn(table, SWT.CENTER);
		tblclmnCol_6.setWidth(132);
		tblclmnCol_6.setText("\u6D41\u91CF\u5408\u8BA1(\u5B57\u8282)");

		Group group1 = new Group(comp, SWT.NONE);
		group1.setLayout(new GridLayout(1, false));
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("测试结果");

		lbl_result = new Label(group1, SWT.NONE);
		lbl_result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lbl_result.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lbl_result.setAlignment(SWT.CENTER);
		lbl_result.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 20, SWT.BOLD));
		lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lbl_result.setText("");

		initData();
	}

	/**
	 * 实时更新数据列表线程
	 */
	protected void startFreshDataThread() {
		int time = Integer.parseInt(MainView.getProp().getProperty("TestTime", "20"));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long start = System.currentTimeMillis();
					while (!stopUpdate && System.currentTimeMillis() - start <= time * 1000) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								updateData();
							}
						});
						Thread.sleep(1000);
					}
					PostThread.stop = true;
					ReceiveThread.stop = true;
					stopUpdate = true;
					// 停止线程
					new Thread(new StopThread(DevName)).start();
					// 统计合计数据
					consultTotalData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 计算合计数据并判断结果，导出数据
	 */
	public void consultTotalData() {

		double upTotalCurRate = 0.0;
		double downTotalCurRate = 0.0;
		double upTotalAveRate = 0.0;
		long upTotalTraffic = 0;
		double downTotalAveRate = 0.0;
		long downTotalTraffic = 0;
		for (int i = 0; i < 6; i++) {
			if (i < 3) {
				upTotalCurRate += wifiDatas.get(i).getCurRate();
				upTotalAveRate += wifiDatas.get(i).getAverageRate();
				upTotalTraffic += wifiDatas.get(i).getTotalTraffic();
			} else {
				downTotalCurRate += wifiDatas.get(i).getCurRate();
				downTotalAveRate += wifiDatas.get(i).getAverageRate();
				downTotalTraffic += wifiDatas.get(i).getTotalTraffic();
			}
		}

		upTotalCurRate = new BigDecimal(upTotalCurRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		upTotalAveRate = new BigDecimal(upTotalAveRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		downTotalCurRate = new BigDecimal(downTotalCurRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		downTotalAveRate = new BigDecimal(downTotalAveRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		wifiDatas.get(6).curRate = upTotalCurRate;
		wifiDatas.get(6).averageRate = upTotalAveRate;
		wifiDatas.get(6).status = "已结束";
		wifiDatas.get(6).totalTraffic = upTotalTraffic;
		wifiDatas.get(6).id = 7;

		wifiDatas.get(7).curRate = downTotalCurRate;
		wifiDatas.get(7).averageRate = downTotalAveRate;
		wifiDatas.get(7).status = "已结束";
		wifiDatas.get(7).totalTraffic = downTotalTraffic;
		wifiDatas.get(7).id = 8;

		wifiDatas.get(8).curRate = new BigDecimal(upTotalCurRate + downTotalCurRate)
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		wifiDatas.get(8).averageRate = new BigDecimal(upTotalAveRate + downTotalAveRate)
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		wifiDatas.get(8).status = "已结束";
		wifiDatas.get(8).totalTraffic = upTotalTraffic + downTotalTraffic;
		wifiDatas.get(8).id = 9;

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateData();
				MainView.getInstance().setStatusString("设备1WIFI吞吐量测试结束", SWT.COLOR_DARK_GREEN);
				if (wifiDatas.get(6).averageRate >= Double
						.parseDouble(MainView.getProp().getProperty("MinUpAverageRate"))
						&& wifiDatas.get(7).averageRate >= Double
								.parseDouble(MainView.getProp().getProperty("MinDownAverageRate"))) {
					lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					lbl_result.setText("Pass");
				} else {
					lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					lbl_result.setText("Fail");
				}
				try {
					if (wifiDatas.get(6).averageRate != 0 && wifiDatas.get(7).averageRate != 0) {
						String netType = MainView.getProp().getProperty("Dev1_NetType");
						XmlParse.saveDataForXML(MainView.getProp().getProperty("Dev1_Sn"), netType,
								wifiDatas.get(6).averageRate, wifiDatas.get(7).averageRate,
								wifiDatas.get(8).averageRate, lbl_result.getText(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}
					// 导出测试报告
					ExportTestReport.testReportToDesktop();
					telnet.distinct();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 更新数据
	 */
	public void updateData() {
		table.removeAll();
		List<TableItem> items = initTableData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (WifiData data : wifiDatas) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if (table.isDisposed())
								return;
							System.out.println(data.toString());
							TableItem item = items.get(data.getID() - 1);
							item.setText(3, data.getCurRate() + "");
							item.setText(4, data.getAverageRate() + "");
							if (data.id > 6 && data.averageRate != 0) {
								item.setBackground(4, SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
							}
							item.setText(5, data.getStatus());
							item.setText(6, data.getTotalTraffic() + "");
							item.setData(data);
							table.getParent().layout();
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void initData() {
		wifiDatas = new ArrayList<WifiData>();
		for (int i = 0; i < 9; i++) {
			WifiData wifiData = new WifiData();
			wifiData.id = i + 1;
			wifiData.status = "";
			if (i < 3) {
				wifiData.image = "/icon/arrow1.png";
				wifiData.upDownStream = "上行" + (i + 1);
			} else if (i >= 3 && i < 6) {
				wifiData.image = "/icon/arrow2.png";
				wifiData.upDownStream = "下行" + (i + 1);
			} else if (i >= 6 && i < 8) {
				wifiData.image = "/icon/all.png";
				if (i == 6) {
					wifiData.upDownStream = "上行合计";
				} else {
					wifiData.upDownStream = "下行合计";
				}
			} else {
				wifiData.image = "/icon/total.png";
				wifiData.upDownStream = "总计";
			}
			wifiDatas.add(wifiData);
		}
		initTableData();

		// 判断设备是否连接,如果连接，则开始测试
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (StateUpdater.closed == false) {
					if (!stop && StateUpdater.NC_ETH_STATE == State.CONNECTED) {
						setDev1NetConfig();
						boolean isConnect = connectSSID();
						if (isConnect) {
							PostThread.stop = false;
							ReceiveThread.stop = false;
							stopUpdate = false;
							stop = true;
							startTest();
						}
					}
					if (StateUpdater.NC_ETH_STATE == State.OFF) {
						PostThread.stop = true;
						ReceiveThread.stop = true;
						stopUpdate = true;
						stop = false;
					}
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 连接SSID
	 */
	public boolean connectSSID() {
		boolean flag = false;
		try {
			// 连接指定wifi
			String ssid = MainView.getProp().getProperty("Dev1_SSID");
			String pass = MainView.getProp().getProperty("Dev1_Pass");
			String wirenet = MainView.getProp().getProperty("Dev1_WirelessNet");
			while (!flag) {
				boolean isConnect = WifiConnect.Connect(ssid, pass, wirenet);
				if (isConnect) {
					// 取消"线程停止"限制
					PostThread.stop = false;
					ReceiveThread.stop = false;
					MainView.getInstance().setStatusString("WiFi连接成功", SWT.COLOR_DARK_GREEN);
					flag = true;
					break;
				} else {
					// "线程停止"限制
					// "线程停止"限制
					PostThread.stop = true;
					ReceiveThread.stop = true;
					MainView.getInstance().setStatusString("WiFi连接失败", SWT.COLOR_RED);
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 登录Telnet及保存设备1网络配置信息
	 */
	public void setDev1NetConfig() {
		String selected = MainView.getProp().getProperty("Dev1_CableNet");
		if (!selected.equals("")) {
			try {
				String ips = IPUtil.getNicInfo(selected);
				// Telnet登录
				telnet = new TelnetOperator();
				boolean flag;
				if (MainView.getProp().getProperty("DeviceType").equals("终端公司H2r-1")) {
					flag = telnet.login("192.168.1.1", 23, ips, 0, "CMCCAdmin", "aDm8H%MdA");
				} else {
					flag = telnet.login("192.168.1.1", 23, ips, 0, "root", "Pon521");
				}
				if (flag) {
					saveSSIDAndPass(telnet, defConfPath);
					MainView.getInstance().setStatusString("Telnet登录成功", SWT.COLOR_DARK_GREEN);
				} else {
					MainView.getInstance().setStatusString("Telnet登录失败!", SWT.COLOR_RED);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 保存IP、SN、SSID及密码
	 * 
	 * @param telnet
	 * @throws Exception
	 */
	private void saveSSIDAndPass(TelnetOperator telnet, String pathfile) throws Exception {
		try {
			String file = null;
			if (pathfile == null) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setText("保存配置文件");
				fd.setFileName("config.xml");
				fd.setFilterExtensions(new String[] { "*.xml" });
				file = fd.open();
				if (file == null)
					return;
			} else {
				file = pathfile;
			}
			MainView.getProp().setProperty("Dev1_CableIp",
					IPUtil.getNicInfo(MainView.getProp().getProperty("Dev1_CableNet")));
			MainView.getProp().setProperty("Dev1_WirelessIp",
					IPUtil.getNicInfo(MainView.getProp().getProperty("Dev1_WirelessNet")));
			MainView.getProp().setProperty("Dev1_Sn", telnet.getResult("sismac 2 512"));
			if (MainView.getProp().getProperty("Dev1_NetType").equals("2.4G")) {
				MainView.getProp().setProperty("Dev1_SSID", telnet.getResult("sismac 2 1024"));
				MainView.getProp().setProperty("Dev1_Pass", telnet.getResult("sismac 2 1280"));
			} else {
				MainView.getProp().setProperty("Dev1_SSID", telnet.getResult("sismac 2 1028"));
				MainView.getProp().setProperty("Dev1_Pass", telnet.getResult("sismac 2 1296"));
			}

			MainView.getProp().storeToXML(new FileOutputStream(file),
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		} catch (Exception e1) {
			MainView.getInstance().setStatusString("配置保存失败", SWT.COLOR_RED);
		}
	}

	public static List<TableItem> initTableData() {
		table.removeAll();
		List<TableItem> items = new ArrayList<>();

		for (WifiData data : wifiDatas) {
			TableItem item = new TableItem(table, SWT.CENTER);
			item.setText(0, data.getID() + "");
			item.setImage(1, SWTResourceManager.getImage(MainView.class, data.getImage()));
			item.setText(2, data.getUpDownStream());
			item.setData(data);
			table.getParent().layout();
			items.add(item);
		}
		return items;
	}

	@Override
	public void freshData() {

	}

	/**
	 * 开始测试
	 */
	public void startTest() {
		try {
			saveSSIDAndPass(telnet, defConfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainView.getInstance().setStatusString("设备1WiFi吞吐量开始测试", SWT.COLOR_DARK_GREEN);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				text_1.setText(WifiConnect.getInfo(MainView.getProp().getProperty("Dev1_SSID"), "Signal Quality"));
				text_2.setText(WifiConnect.getInfo(MainView.getProp().getProperty("Dev1_SSID"), "RSSI"));
				lbl_result.setText("");
				lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
				// 初始化列表
				initTableData();
			}
		});

		/**
		 * 创建socket通信线程 1.上行创建三个线程，由客户端向服务端发送数据包 2.下行创建三个线程，由服务器向客户端发送数据包
		 * 3.实时测试当前速率(每秒传输的数据量)及平均速率(传输数据总量/消耗总时间) 4.将数据保存到data.xml文件中
		 */
		// 创建通信线程
		if (MainView.isDev1first) {
			ServerThread.startServer("server1", 7788);
			ServerThread.startServer("server2", 8877);// 服务器端线程
			MainView.isDev1first = false;
		}
		Serversocket.createServerSocket(DevName);// 客户端线程
		/**
		 * 实时更新数据列表线程
		 */
		startFreshDataThread();
		/**
		 * 数据清零
		 */
		for (WifiData wifiData : wifiDatas) {
			wifiData.curRate = 0;
			wifiData.averageRate = 0;
			wifiData.totalTraffic = 0;
		}
	}
}
