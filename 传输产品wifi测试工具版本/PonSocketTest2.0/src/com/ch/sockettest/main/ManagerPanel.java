package com.ch.sockettest.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.DeviceType;
import com.ch.sockettest.dao.PreDataConfig;
import com.ch.sockettest.popview.AbsPopWindow;
import com.ch.sockettest.popview.CompDatabaseConfView;
import com.ch.sockettest.popview.CompStatus;
import com.ch.sockettest.popview.CompDev1TestTable;
import com.ch.sockettest.popview.CompDev2TestTable;
import com.ch.sockettest.popview.CompNICConfView;
import com.ch.sockettest.popview.Navigator;
import com.ch.sockettest.popview.VersionsView;
import com.ch.sockettest.until.ExportTestReport;

public class ManagerPanel extends AbsPopWindow {
	private Composite comp1;
	private Label labFile;
	private Text text_File;
	private Button btnOpenFile;
	private Label labDeviceType;
	private Combo text_DeviceType;
	private Text text_1;
	private Text text;
	private Text text_2;
	public static Navigator navi;
	private static ManagerPanel instance;
	private CompStatus statusBar;
	private static Properties prop = new Properties();
	public static boolean isDev1first = true;
	public static boolean isDev2first = true;
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public ManagerPanel(Display display) {
		super(display);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setText("传输产品wifi测试 V2.0版");
		setMinimumSize(new Point(1024, 900));
		setSize(1024, 900);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginRight = 1;
		gridLayout.marginLeft = 1;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 2;
		gridLayout.verticalSpacing = 1;
		setLayout(gridLayout);
		createControl();
	}

	/**
	 * Create contents of the shell.
	 */
	@Override
	protected void createContents() {

	}

	/**
	 * 创建界面控件
	 */
	private void createControl() {
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("开始");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});

		MenuItem menuItem_1 = new MenuItem(menu, SWT.CASCADE);
		menuItem_1.setText("数据库");
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 创建抽象窗体类对象，打开数据库配置界面
				new AbsPopWindow(Display.getCurrent(), getShell()) {

					@Override
					public void initData() {
					}

					@Override
					protected void createContents() {
						setLayout(new GridLayout(1, false));
						setSize(360, 300);
						setText("数据库配置");
						new CompDatabaseConfView(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_BOTH));
					}
				}.open();
			}
		});

		MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
		menuItem_2.setText("网络配置");
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//网口配置
				CompNICConfView v = new CompNICConfView(Display.getDefault(),
						instance);
				v.open();
			}
		});

		MenuItem menuItem_3 = new MenuItem(menu, SWT.CASCADE);
		menuItem_3.setText("保存配置");
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeProperties(defConfPath);
			}
		});

		MenuItem menuItem_4 = new MenuItem(menu, SWT.CASCADE);
		menuItem_4.setText("停止");
		menuItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		MenuItem menuItem_5 = new MenuItem(menu, SWT.CASCADE);
		menuItem_5.setText("数据导出");
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// 导出测试报告
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setText("导出测试数据");
					fd.setFileName("WiFi吞吐量测试数据.xls");
					fd.setFilterExtensions(new String[] { "*.xls" });
					String file = fd.open();
					if (file == null)
						return;
					// 另存为
					ExportTestReport.testReportSave(file);
					// setStatusString("成功导出测试报告");

					MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
					mb.setMessage("数据已导出到:\n" + file + "\n是否要立即打开");
					if (mb.open() == SWT.YES) {
						Desktop.getDesktop().open(new File(file));
					}
				} catch (Exception e1) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage("导出异常：" + e1.getMessage());
					mb.open();
					e1.printStackTrace();
				}
			}
		});

		MenuItem menuItem_6 = new MenuItem(menu, SWT.CASCADE);
		menuItem_6.setText("关于");
		menuItem_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new VersionsView(getDisplay(), SWT.CLOSE).open();
			}
		});

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		comp1 = new Composite(composite, SWT.BORDER);
		comp1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		comp1.setLayout(new GridLayout(1, true));
		GridData gd_comp1 = new GridData(GridData.FILL_BOTH);
		gd_comp1.grabExcessVerticalSpace = false;
		gd_comp1.heightHint = 173;
		comp1.setLayoutData(gd_comp1);

		Group group1 = new Group(comp1, SWT.NONE);
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("参数配置");
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		labFile = new Label(group1, SWT.NONE);
		labFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labFile.setText("配置文件 :");
		labFile.setBounds(55, 38, 82, 25);
		// 显示配置文件
		text_File = new Text(group1, SWT.BORDER | SWT.WRAP);
		text_File.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_File.setEnabled(false);
		text_File.setEditable(false);
		text_File.setBounds(145, 35, 332, 30);
		

		btnOpenFile = new Button(group1, SWT.NONE);
		btnOpenFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 打开配置文件
				loadProperties(null);
			}
		});
		btnOpenFile.setImage(SWTResourceManager.getImage(MainView.class, "/icon/document-open-folder.png"));
		btnOpenFile.setToolTipText("打开配置文件");
		btnOpenFile.setBounds(480, 35, 30, 30);
		

		labDeviceType = new Label(group1, SWT.NONE);
		labDeviceType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labDeviceType.setSize(85, 25);
		labDeviceType.setLocation(580, 38);
		labDeviceType.setText("产品型号 :");

		text_DeviceType = new Combo(group1, SWT.READ_ONLY);
		text_DeviceType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_DeviceType.setSize(260, 28);
		text_DeviceType.setLocation(665, 35);
		text_DeviceType.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		GridData gd_deviceType = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_deviceType.widthHint = 144;
		text_DeviceType.setLayoutData(gd_deviceType);
		text_DeviceType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DeviceType dt = DeviceType.getValue(text_DeviceType.getText());
				System.out.println(dt);
				Map<String, String> map = PreDataConfig.preDataLabelMap.get(dt.toString());
				System.out.println(map);
				if (map == null) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
					mb.setText("错误提示");
					mb.setMessage("不支持该设备类型!\n");
					mb.open();
				} else {
					// 根据设备类型判断预配置数据字段
					// for (String key : allkeys) {
					// if (map.get(key) != null) {
					// switch (map.get(key)) {
					// case "SSID":
					// case "SSID1":
					// case "SSID2":
					// keySSIDlist.add(key);
					// break;
					// case "SSID密码":
					// case "SSID1密码":
					// case "SSID2密码":
					// keyPasslist.add(key);
					// break;
					// default:
					// break;
					// }
					// }
					// }
				}

			}
		});
		
		Label label = new Label(group1, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label.setBounds(55, 81, 90, 20);
		label.setText("标准与要求 :");
		
		Label labUpMin = new Label(group1, SWT.NONE);
		labUpMin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labUpMin.setBounds(145, 100, 82, 20);
		labUpMin.setText("上行最小值 :");
		
		text_1 = new Text(group1, SWT.BORDER | SWT.CENTER);
		text_1.setText("20");
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_1.setBounds(246, 94, 73, 31);
		
		Label lblMbps = new Label(group1, SWT.NONE);
		lblMbps.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblMbps.setBounds(337, 100, 56, 20);
		lblMbps.setText("Mbps");
		
		Label label_1 = new Label(group1, SWT.NONE);
		label_1.setText("下行最小值 :");
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setBounds(420, 100, 90, 20);
		
		text = new Text(group1, SWT.BORDER | SWT.CENTER);
		text.setText("20");
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text.setBounds(525, 94, 73, 31);
		
		Label label_2 = new Label(group1, SWT.NONE);
		label_2.setText("Mbps");
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setBounds(615, 100, 56, 20);
		
		Label label_3 = new Label(group1, SWT.NONE);
		label_3.setText("合计最小值 :");
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_3.setBounds(700, 100, 90, 20);
		
		text_2 = new Text(group1, SWT.BORDER | SWT.CENTER);
		text_2.setText("40");
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_2.setBounds(796, 94, 73, 31);
		
		Label label_4 = new Label(group1, SWT.NONE);
		label_4.setText("Mbps");
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_4.setBounds(881, 100, 56, 20);

		navi = new Navigator(composite, SWT.NONE);
		((GridData) navi.getStatckComposite().getLayoutData()).heightHint = 495;
		navi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_navi = new GridData(GridData.FILL_BOTH);
		gd_navi.heightHint = 505;
		navi.setLayoutData(gd_navi);

		navi.addItem("设备1", new CompDev1TestTable(navi.getStatckComposite(), SWT.NONE));
		navi.addItem("设备2", new CompDev2TestTable(navi.getStatckComposite(), SWT.NONE));
		
		statusBar = new CompStatus(composite, SWT.NONE);
		statusBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData grddata = new GridData(GridData.FILL_HORIZONTAL);
		grddata.heightHint = 40;
		statusBar.setLayoutData(grddata);
	
	}
	
	/**
	 * 保存配置
	 * 
	 * @param pathfile
	 */
	protected void storeProperties(String pathfile) {
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
			prop.setProperty("MinUpAverageRate", text_1.getText());
			prop.setProperty("MinDownAverageRate", text.getText());
			prop.setProperty("MinTotalAverageRate", text_2.getText());

			prop.storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			setStatus("配置保存成功", SWT.COLOR_DARK_GREEN, true);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("配置保存失败:\n" + e.getMessage());
			mb.open();
		}
	}


	@Override
	public void initData() {
		text_DeviceType.setItems(DeviceType.list());
		text_DeviceType.select(0);
		loadProperties(defConfPath);
		navi.refreshCurrentControl();
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @param pathname
	 */
	private void loadProperties(String pathname) {
		try {
			String file = null;
			if (pathname == null) {
				FileDialog fd = new FileDialog(getShell());
				fd.setText("请选择一个配置文件进行导入");
				fd.setFilterExtensions(new String[] { "*.xml" });
				file = fd.open();
				if (file == null)
					return;
			} else {
				file = pathname;
			}
			Properties p = new Properties();
			p.loadFromXML(new FileInputStream(file));
			prop.putAll(p);
			// 导入数据
			loadData();
			if (pathname == null) {
				text_File.setText(file);
			} else {
				text_File.setText(pathname);
			}
			setStatus("配置文件导入成功", SWT.COLOR_DARK_GREEN, true);
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
		text.setText(ManagerPanel.getProp().getProperty("MinDownAverageRate", "20"));
		text_1.setText(ManagerPanel.getProp().getProperty("MinUpAverageRate", "20"));
		text_2.setText(ManagerPanel.getProp().getProperty("MinTotalAverageRate", "20"));
	}
	
	/**
	 * 获取当前的配置信息
	 * 
	 * @return
	 */
	public static Properties getProp() {
		return prop;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		ManagerPanel mp = ManagerPanel.getInstance();
		mp.open();
	}

	public static ManagerPanel getInstance() {
		if (instance == null || instance.isDisposed()) {
			instance = new ManagerPanel(Display.getDefault());
		}
		return instance;
	}

	public void setStatus(final String msg, final int color, boolean stay) {
		if (statusBar != null && statusBar.isDisposed() == false)
			statusBar.setStatus(msg, color, stay);
	}
}
