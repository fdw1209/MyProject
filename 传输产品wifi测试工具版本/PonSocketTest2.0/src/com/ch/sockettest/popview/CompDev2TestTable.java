package com.ch.sockettest.popview;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.main.MainView;
import com.ch.sockettest.main.ManagerPanel;
import com.ch.sockettest.socket.PostThread;
import com.ch.sockettest.socket.ReceiveThread;
import com.ch.sockettest.socket.ServerThread;
import com.ch.sockettest.socket.Serversocket;
import com.ch.sockettest.socket.StopThread;
import com.ch.sockettest.until.ExportTestReport;
import com.ch.sockettest.until.XmlParse;
/**
 * 测试数据列表
 * @author fdw
 *
 */
public class CompDev2TestTable extends AbstractComposite {
	private final String DevName = "Dev2";
	private Text text;
	private Button btnFreshData;
	private static Table table;
	private Button btnStart;
	private Spinner spinner;
	private Label label_1;
	private Label label_2;
	private Label lbl_result;
	public static List<WifiData> wifiDatas;

	public CompDev2TestTable(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setSize(800, 600);
		setLayout(new GridLayout(1, false));

		final Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_composite = new GridData(GridData.FILL_HORIZONTAL);
		gd_composite.heightHint = 52;
		composite.setLayoutData(gd_composite);

		text = new Text(composite, SWT.NONE);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text.setEditable(false);
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.BOLD));
		text.setText("\u6D4B\u8BD5\u6570\u636E\u5217\u8868");
		text.setBounds(10, 19, 133, 28);

		btnFreshData = new Button(composite, SWT.CENTER);
		btnFreshData.setText("\u5237\u65B0\u6570\u636E");
		btnFreshData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 更新数据
				updateData();
			}
		});
		btnFreshData.setImage(null);
		btnFreshData.setBounds(554, 14, 110, 34);

		btnStart = new Button(composite, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				/**
				 * 开始测试，打开网络配置窗口
				 */
				CompDev2NICConfView view = new CompDev2NICConfView(getDisplay(), getShell());
				view.open();
				if (view.isDisposed() && CompDev2NICConfView.isConfirm) {
					ManagerPanel.getInstance().setStatus("设备2WiFi吞吐量开始测试", SWT.COLOR_DARK_GREEN, false);
					lbl_result.setText("");
					lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
					// 初始化列表
					initTableData();
					/**
					 * 创建socket通信线程 1.上行创建三个线程，由客户端向服务端发送数据包
					 * 2.下行创建三个线程，由服务器向客户端发送数据包
					 * 3.实时测试当前速率(每秒传输的数据量)及平均速率(传输数据总量/消耗总时间)
					 * 4.将数据保存到data.xml文件中
					 */
					// 创建通信线程
					if (ManagerPanel.isDev2first) {
						ServerThread.startServer("server1", 8899);
						ServerThread.startServer("server2", 9988);// 服务器端线程
						ManagerPanel.isDev2first = false;
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
		});
		btnStart.setBounds(670, 14, 110, 33);
		btnStart.setText("\u5F00\u59CB\u6D4B\u8BD5");

		label_1 = new Label(composite, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setBounds(250, 23, 75, 20);
		label_1.setText("\u6D4B\u8BD5\u65F6\u95F4");

		spinner = new Spinner(composite, SWT.BORDER);
		spinner.setSelection(20);
		spinner.setBounds(325, 19, 57, 26);

		label_2 = new Label(composite, SWT.NONE);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setBounds(390, 23, 25, 20);
		label_2.setText("\u79D2");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(GridData.FILL_BOTH));

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
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

		Group group1 = new Group(this, SWT.NONE);
		GridData gd_group1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group1.widthHint = 788;
		group1.setLayoutData(gd_group1);
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("测试结果");

		lbl_result = new Label(group1, SWT.NONE);
		lbl_result.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lbl_result.setAlignment(SWT.CENTER);
		lbl_result.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 20, SWT.BOLD));
		lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lbl_result.setBounds(10, 25, 775, 50);
		lbl_result.setText("");

		initData();
	}

	/**
	 * 实时更新数据列表线程
	 */
	protected void startFreshDataThread() {
		int time = Integer.parseInt(spinner.getText());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long start = System.currentTimeMillis();
					while (System.currentTimeMillis() - start <= time * 1000) {
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
				ManagerPanel.getInstance().setStatus("设备2WIFI吞吐量测试结束", SWT.COLOR_DARK_GREEN, false);
				if (wifiDatas.get(6).averageRate >= Double
						.parseDouble(ManagerPanel.getProp().getProperty("MinUpAverageRate"))
						&& wifiDatas.get(7).averageRate >= Double
								.parseDouble(ManagerPanel.getProp().getProperty("MinDownAverageRate"))) {
					lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					lbl_result.setText("Pass");
				} else {
					lbl_result.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					lbl_result.setText("Fail");
				}
				try {
					if (wifiDatas.get(6).averageRate != 0 && wifiDatas.get(7).averageRate != 0) {
						String netType = CompDev2NICConfView.getProp().getProperty("Dev2_NetType");
						XmlParse.saveDataForXML(CompDev2NICConfView.getProp().getProperty("Dev2_Sn"), netType,
								wifiDatas.get(6).averageRate, wifiDatas.get(7).averageRate,
								wifiDatas.get(8).averageRate, lbl_result.getText(),
								new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					}
					// 导出测试报告
					ExportTestReport.testReportToDesktop();
					ManagerPanel.getInstance().setStatus("设备2测试报告成功导出", SWT.COLOR_DARK_GREEN, false);
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
}
