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
		setText("�����Ʒwifi���� V2.0��");
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
	 * ��������ؼ�
	 */
	private void createControl() {
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("��ʼ");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});

		MenuItem menuItem_1 = new MenuItem(menu, SWT.CASCADE);
		menuItem_1.setText("���ݿ�");
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��������������󣬴����ݿ����ý���
				new AbsPopWindow(Display.getCurrent(), getShell()) {

					@Override
					public void initData() {
					}

					@Override
					protected void createContents() {
						setLayout(new GridLayout(1, false));
						setSize(360, 300);
						setText("���ݿ�����");
						new CompDatabaseConfView(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_BOTH));
					}
				}.open();
			}
		});

		MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
		menuItem_2.setText("��������");
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//��������
				CompNICConfView v = new CompNICConfView(Display.getDefault(),
						instance);
				v.open();
			}
		});

		MenuItem menuItem_3 = new MenuItem(menu, SWT.CASCADE);
		menuItem_3.setText("��������");
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeProperties(defConfPath);
			}
		});

		MenuItem menuItem_4 = new MenuItem(menu, SWT.CASCADE);
		menuItem_4.setText("ֹͣ");
		menuItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		MenuItem menuItem_5 = new MenuItem(menu, SWT.CASCADE);
		menuItem_5.setText("���ݵ���");
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// �������Ա���
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setText("������������");
					fd.setFileName("WiFi��������������.xls");
					fd.setFilterExtensions(new String[] { "*.xls" });
					String file = fd.open();
					if (file == null)
						return;
					// ���Ϊ
					ExportTestReport.testReportSave(file);
					// setStatusString("�ɹ��������Ա���");

					MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
					mb.setMessage("�����ѵ�����:\n" + file + "\n�Ƿ�Ҫ������");
					if (mb.open() == SWT.YES) {
						Desktop.getDesktop().open(new File(file));
					}
				} catch (Exception e1) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage("�����쳣��" + e1.getMessage());
					mb.open();
					e1.printStackTrace();
				}
			}
		});

		MenuItem menuItem_6 = new MenuItem(menu, SWT.CASCADE);
		menuItem_6.setText("����");
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
		group1.setText("��������");
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		labFile = new Label(group1, SWT.NONE);
		labFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labFile.setText("�����ļ� :");
		labFile.setBounds(55, 38, 82, 25);
		// ��ʾ�����ļ�
		text_File = new Text(group1, SWT.BORDER | SWT.WRAP);
		text_File.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_File.setEnabled(false);
		text_File.setEditable(false);
		text_File.setBounds(145, 35, 332, 30);
		

		btnOpenFile = new Button(group1, SWT.NONE);
		btnOpenFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// �������ļ�
				loadProperties(null);
			}
		});
		btnOpenFile.setImage(SWTResourceManager.getImage(MainView.class, "/icon/document-open-folder.png"));
		btnOpenFile.setToolTipText("�������ļ�");
		btnOpenFile.setBounds(480, 35, 30, 30);
		

		labDeviceType = new Label(group1, SWT.NONE);
		labDeviceType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labDeviceType.setSize(85, 25);
		labDeviceType.setLocation(580, 38);
		labDeviceType.setText("��Ʒ�ͺ� :");

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
					mb.setText("������ʾ");
					mb.setMessage("��֧�ָ��豸����!\n");
					mb.open();
				} else {
					// �����豸�����ж�Ԥ���������ֶ�
					// for (String key : allkeys) {
					// if (map.get(key) != null) {
					// switch (map.get(key)) {
					// case "SSID":
					// case "SSID1":
					// case "SSID2":
					// keySSIDlist.add(key);
					// break;
					// case "SSID����":
					// case "SSID1����":
					// case "SSID2����":
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
		label.setText("��׼��Ҫ�� :");
		
		Label labUpMin = new Label(group1, SWT.NONE);
		labUpMin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labUpMin.setBounds(145, 100, 82, 20);
		labUpMin.setText("������Сֵ :");
		
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
		label_1.setText("������Сֵ :");
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
		label_3.setText("�ϼ���Сֵ :");
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

		navi.addItem("�豸1", new CompDev1TestTable(navi.getStatckComposite(), SWT.NONE));
		navi.addItem("�豸2", new CompDev2TestTable(navi.getStatckComposite(), SWT.NONE));
		
		statusBar = new CompStatus(composite, SWT.NONE);
		statusBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData grddata = new GridData(GridData.FILL_HORIZONTAL);
		grddata.heightHint = 40;
		statusBar.setLayoutData(grddata);
	
	}
	
	/**
	 * ��������
	 * 
	 * @param pathfile
	 */
	protected void storeProperties(String pathfile) {
		try {
			String file = null;
			if (pathfile == null) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setText("���������ļ�");
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
			setStatus("���ñ���ɹ�", SWT.COLOR_DARK_GREEN, true);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("���ñ���ʧ��:\n" + e.getMessage());
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
	 * ���������ļ�
	 * 
	 * @param pathname
	 */
	private void loadProperties(String pathname) {
		try {
			String file = null;
			if (pathname == null) {
				FileDialog fd = new FileDialog(getShell());
				fd.setText("��ѡ��һ�������ļ����е���");
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
			// ��������
			loadData();
			if (pathname == null) {
				text_File.setText(file);
			} else {
				text_File.setText(pathname);
			}
			setStatus("�����ļ�����ɹ�", SWT.COLOR_DARK_GREEN, true);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("���õ���ʧ��:\n" + e.getMessage());
			mb.open();
		}
	}

	/**
	 * ��������
	 */
	private void loadData() {
		text.setText(ManagerPanel.getProp().getProperty("MinDownAverageRate", "20"));
		text_1.setText(ManagerPanel.getProp().getProperty("MinUpAverageRate", "20"));
		text_2.setText(ManagerPanel.getProp().getProperty("MinTotalAverageRate", "20"));
	}
	
	/**
	 * ��ȡ��ǰ��������Ϣ
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
