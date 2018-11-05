package com.ch.sockettest.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.json.JSONObject;

import com.ch.sockettest.dao.DeviceType;
import com.ch.sockettest.dao.PreDataConfig;
import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.popview.AbsPopWindow;
import com.ch.sockettest.popview.CompDatabaseConfView;
import com.ch.sockettest.popview.VersionsView;
import com.ch.sockettest.socket.PostThread;
import com.ch.sockettest.socket.ReceiveThread;
import com.ch.sockettest.socket.ServerThread;
import com.ch.sockettest.socket.Serversocket;
import com.ch.sockettest.socket.StopThread;
import com.ch.sockettest.until.ExportTestReport;
import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.XmlParse;
import com.ch.sockettest.wifi.HttpUtils;
import com.ch.sockettest.wifi.TelnetOperator;

public class MainView extends AbsPopWindow {

	private static MainView instance = null;
	private Label txtFile;
	private Text text_File;
	private Button btnOpenFile;
	// private Label txtTask;
	// private Text text_Task;
	private Label txtWPort;
	private Text text_WPort;
	private Label txtUpStream;
	private Combo text_UpStream;
	private Label txtDownStream;
	private Combo text_DownStream;
	private Label txtDurTime;
	private Label txtRemainTime;
	private Text text_ReTime;
	private Label txtProgress;
	private Label txtReadyTime;
	private Label txtReadyTimeCount;
	private Label txtTip;
	private Text textTip;
	private Label txtSSID;
	private Text text_SSID;
	private Label txtPassWord;
	private Text text_PassWord;
	private Label txtConnect;
	private Label txtMethod;
	private Combo text_Method;
	private Label txtUpMin;
	private Text text_UpMin;
	private Label txtDownMin;
	private Text text_DownMin;
	private Label txtAllMin;
	private Text text_AllMin;
	private Text text_Result;
	private Label txtResult;
	private static Table table;
	private Spinner spinTimeMinute;
	private Spinner spinTimeSecond;
	private long lastStatusSetTime;
	private Spinner spinReadyTime;
	private ProgressBar progressBar;
	private Button button;
	private Composite comp3;
	private Composite comp2;
	private Composite comp1;
	private Label lblNewLabel;
	private Label label_SN;
	private Text text_SN;
	private String sn;
	private Button btn_net;
	private Button btn_net1;
	private Label txtWip;
	private Text text_Wip;
	private Label txtYip;
	private Text text_Yip;
	private Label txtYPort;
	private Text text_YPort;
	private Label txtDeviceType;
	private Combo text_DeviceType;
	private boolean stop = false;
	private boolean isfirst = true;
	private static Properties prop = new Properties();
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";
	public static List<WifiData> wifiDatas = null;
	String[] allkeys = new String[] { "sn", "sn1", "sn2", "sn3", "mac", "mac1", "mac2", "mac3", "rsv1", "rsv2", "rsv3",
			"rsv4", "rsv5", "rsv6" };
	List<String> keySSIDlist = new ArrayList<String>();
	List<String> keyPasslist = new ArrayList<String>();
	private TelnetOperator telnet;

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public MainView(Display display) {
		super(display);
		setText("�����Ʒwifi���� V1.0��");
		setMinimumSize(new Point(1024, 847));
		setSize(1024, 900);
		// setSize(getMonitor().getClientArea().width,
		// getMonitor().getClientArea().height);
		// center();
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
	protected void createContents() {

	}

	/**
	 * ��������ؼ�
	 */
	protected void createControl() {
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("�༭");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_UpMin.setEnabled(true);
				text_DownMin.setEnabled(true);
				text_AllMin.setEnabled(true);
			}
		});

		MenuItem menuItem_6 = new MenuItem(menu, SWT.CASCADE);
		menuItem_6.setText("���ݿ�");
		menuItem_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��������������󣬴����ݿ����ý���
				new AbsPopWindow(Display.getCurrent(), getShell()) {

					@Override
					public void initData() {
						// TODO Auto-generated method stub

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

		MenuItem menuItem_1 = new MenuItem(menu, SWT.CASCADE);
		menuItem_1.setText("��������");
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeProperties(defConfPath);
				text_UpMin.setEnabled(false);
				text_DownMin.setEnabled(false);
				text_AllMin.setEnabled(false);
			}
		});

		MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
		menuItem_2.setText("��ʼ");
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��ʼ����
				if (!text_SN.getText().trim().equals("")) {
					sn = text_SN.getText().trim();
				} else {
					sn = "";
				}
				if (!checkSN(sn)) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("SN��ʽ����\n");
					mb.open();
				} else {
					start();
				}
			}
		});

		MenuItem menuItem_3 = new MenuItem(menu, SWT.CASCADE);
		menuItem_3.setText("ֹͣ");
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
				button.setText("��ʼ����");
				setStatusString("ֹͣ����");
				// ֹͣ��־λ
				setStop(true);
				PostThread.stop = true;
				ReceiveThread.stop = true;
				// ��������ʣ��ʱ��
				progressBar.setSelection(progressBar.getMaximum());
				text_ReTime.setText("0");
				lblNewLabel.setText("100%");
				// �����б�����
				try {
					Thread.sleep(1000);
					// ֹͣ�߳�
					new Thread(new StopThread("Dev1")).start();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		MenuItem menuItem_4 = new MenuItem(menu, SWT.CASCADE);
		menuItem_4.setText("��������");
		menuItem_4.addSelectionListener(new SelectionAdapter() {
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
					setStatusString("�ɹ��������Ա���");

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

		MenuItem menuItem_5 = new MenuItem(menu, SWT.CASCADE);
		menuItem_5.setText("����");
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new VersionsView(getDisplay(), SWT.CLOSE).open();
			}
		});

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite1 = new Composite(composite, SWT.NONE);
		composite1.setLayout(new GridLayout(3, false));
		GridData gd_composite1 = new GridData(GridData.FILL_HORIZONTAL);
		gd_composite1.heightHint = 394;
		composite1.setLayoutData(gd_composite1);

		comp1 = new Composite(composite1, SWT.BORDER);
		comp1.setLayout(new GridLayout(1, true));
		GridData gd_comp1 = new GridData(GridData.FILL_VERTICAL);
		gd_comp1.horizontalAlignment = SWT.FILL;
		gd_comp1.grabExcessHorizontalSpace = true;
		gd_comp1.heightHint = 325;
		comp1.setLayoutData(gd_comp1);

		Group group1 = new Group(comp1, SWT.NONE);
		group1.setText("�豸��������");
		GridData gd_group1 = new GridData(GridData.FILL_BOTH);
		gd_group1.widthHint = 460;
		group1.setLayoutData(gd_group1);
		group1.setLayout(new GridLayout(4, false));

		txtFile = new Label(group1, SWT.NONE);
		txtFile.setText("�����ļ� :");

		Composite comp11 = new Composite(group1, SWT.NONE);
		comp11.setLayout(new GridLayout(2, false));
		GridData gd_textFile = new GridData();
		gd_textFile.grabExcessHorizontalSpace = true;
		gd_textFile.horizontalAlignment = GridData.FILL;
		gd_textFile.horizontalSpan = 3;
		comp11.setLayoutData(gd_textFile);

		text_File = new Text(comp11, SWT.BORDER | SWT.WRAP);
		text_File.setEnabled(false);
		text_File.setEditable(false);
		text_File.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// ��ʾ�����ļ�

		btnOpenFile = new Button(comp11, SWT.NONE);
		btnOpenFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// �������ļ�
				loadProperties(null);
			}
		});
		btnOpenFile.setImage(SWTResourceManager.getImage(MainView.class, "/icon/document-open-folder.png"));
		btnOpenFile.setToolTipText("�������ļ�");

		txtWip = new Label(group1, SWT.NONE);
		txtWip.setText("����IP :");

		Composite comp12 = new Composite(group1, SWT.NONE);
		comp12.setLayout(new GridLayout(1, false));
		GridData gd_textWip = new GridData();
		gd_textWip.horizontalAlignment = GridData.FILL;
		gd_textWip.horizontalSpan = 1;
		comp12.setLayoutData(gd_textWip);

		text_Wip = new Text(comp12, SWT.BORDER | SWT.WRAP);
		text_Wip.setEditable(false);
		text_Wip.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		txtWPort = new Label(group1, SWT.NONE);
		txtWPort.setText("�˿� :");

		Composite comp121 = new Composite(group1, SWT.NONE);
		GridData gd_comp121 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		comp121.setLayoutData(gd_comp121);
		comp121.setLayout(new GridLayout(1, false));
		GridData gd_textPort = new GridData();
		gd_textPort.grabExcessHorizontalSpace = true;
		gd_textPort.horizontalAlignment = GridData.FILL;
		comp121.setLayoutData(gd_textPort);

		text_WPort = new Text(comp121, SWT.BORDER | SWT.WRAP);
		text_WPort.setEditable(false);
		GridData gd_text_Port = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_Port.widthHint = 94;
		text_WPort.setLayoutData(gd_text_Port);
		// ��ʾ�˿�

		txtYip = new Label(group1, SWT.NONE);
		txtYip.setText("����IP :");

		Composite comp13 = new Composite(group1, SWT.NONE);
		comp13.setLayout(new GridLayout(1, false));
		GridData gd_textYip = new GridData();
		gd_textYip.horizontalAlignment = GridData.FILL;
		gd_textYip.horizontalSpan = 1;
		comp13.setLayoutData(gd_textYip);

		text_Yip = new Text(comp13, SWT.BORDER | SWT.WRAP);
		text_Yip.setEditable(false);
		text_Yip.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		txtYPort = new Label(group1, SWT.NONE);
		txtYPort.setText("�˿� :");

		Composite comp131 = new Composite(group1, SWT.NONE);
		GridData gd_comp131 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		comp131.setLayoutData(gd_comp131);
		comp131.setLayout(new GridLayout(1, false));
		GridData gd_textPort1 = new GridData();
		gd_textPort1.grabExcessHorizontalSpace = true;
		gd_textPort1.horizontalAlignment = GridData.FILL;
		comp131.setLayoutData(gd_textPort1);

		text_YPort = new Text(comp131, SWT.BORDER | SWT.WRAP);
		text_YPort.setEditable(false);
		GridData gd_text_Port1 = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_Port1.widthHint = 94;
		text_YPort.setLayoutData(gd_text_Port1);

		txtUpStream = new Label(group1, SWT.NONE);
		txtUpStream.setText("������ :");

		Composite comp14 = new Composite(group1, SWT.NONE);
		comp14.setLayout(new GridLayout(2, false));
		GridData gd_textUpStream = new GridData();
		gd_textUpStream.horizontalAlignment = GridData.FILL;
		gd_textUpStream.horizontalSpan = 1;
		comp14.setLayoutData(gd_textUpStream);

		// ������
		text_UpStream = new Combo(comp14, SWT.BORDER);
		text_UpStream.setItems(new String[] { "1", "2", "3" });
		text_UpStream.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text_UpStream.setText("3");
		text_UpStream.setEnabled(false);

		Label stream = new Label(comp14, SWT.NONE);
		stream.setText("��");

		txtDownStream = new Label(group1, SWT.NONE);
		txtDownStream.setText("������ :");

		Composite comp15 = new Composite(group1, SWT.NONE);
		comp15.setLayout(new GridLayout(2, false));
		GridData gd_Stream = new GridData();
		gd_Stream.horizontalAlignment = GridData.FILL;
		gd_Stream.horizontalSpan = 1;
		comp15.setLayoutData(gd_Stream);

		// ������
		text_DownStream = new Combo(comp15, SWT.BORDER);
		text_DownStream.setItems(new String[] { "1", "2", "3" });
		text_DownStream.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text_DownStream.setText("3");
		text_DownStream.setEnabled(false);

		Label stream1 = new Label(comp15, SWT.NONE);
		stream1.setText("��");

		txtDurTime = new Label(group1, SWT.NONE);
		txtDurTime.setText("����ʱ�� :");

		Composite comp16 = new Composite(group1, SWT.NONE);
		comp16.setLayout(new GridLayout(4, false));
		GridData gd_textDurTime = new GridData();
		gd_textDurTime.horizontalAlignment = GridData.FILL;
		gd_textDurTime.horizontalSpan = 1;
		comp16.setLayoutData(gd_textDurTime);

		spinTimeMinute = new Spinner(comp16, SWT.BORDER);
		spinTimeMinute.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Label stream3 = new Label(comp16, SWT.NONE);
		stream3.setText("��");
		spinTimeSecond = new Spinner(comp16, SWT.BORDER | SWT.WRAP);
		spinTimeSecond.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Label stream4 = new Label(comp16, SWT.NONE);
		stream4.setText("��");

		txtRemainTime = new Label(group1, SWT.NONE);
		txtRemainTime.setText("ʣ��ʱ�� :");

		Composite comp151 = new Composite(group1, SWT.NONE);
		GridData gd_comp151 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		comp151.setLayoutData(gd_comp151);
		comp151.setLayout(new GridLayout(1, false));
		GridData gd_texReTime = new GridData();
		gd_texReTime.grabExcessHorizontalSpace = true;
		gd_texReTime.horizontalAlignment = GridData.FILL;
		comp151.setLayoutData(gd_texReTime);
		// ��ʾʣ��ʱ��
		text_ReTime = new Text(comp151, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_text_ReTime = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_ReTime.widthHint = 109;
		text_ReTime.setLayoutData(gd_text_ReTime);

		txtProgress = new Label(group1, SWT.NONE);
		txtProgress.setText("���� :");

		Composite comp17 = new Composite(group1, SWT.NONE);
		comp17.setLayout(new GridLayout(4, false));
		GridData gd_textProgress = new GridData();
		gd_textProgress.grabExcessHorizontalSpace = true;
		gd_textProgress.widthHint = 367;
		gd_textProgress.horizontalAlignment = GridData.FILL;
		gd_textProgress.horizontalSpan = 3;
		comp17.setLayoutData(gd_textProgress);

		progressBar = new ProgressBar(comp17, SWT.BORDER | SWT.SMOOTH);
		progressBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		progressBar.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		GridData gd_progressBar = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_progressBar.heightHint = 27;
		gd_progressBar.widthHint = 314;
		progressBar.setLayoutData(gd_progressBar);

		lblNewLabel = new Label(comp17, SWT.RIGHT);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 40;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("100%");
		new Label(comp17, SWT.NONE);

		txtReadyTime = new Label(group1, SWT.NONE);
		txtReadyTime.setText("���Ӻ�");

		Composite comp18 = new Composite(group1, SWT.NONE);
		comp18.setLayout(new GridLayout(2, false));
		GridData gd_textReadyTime = new GridData();
		gd_textReadyTime.grabExcessHorizontalSpace = true;
		gd_textReadyTime.heightHint = 35;
		gd_textReadyTime.horizontalAlignment = GridData.FILL;
		gd_textReadyTime.horizontalSpan = 3;
		comp18.setLayoutData(gd_textReadyTime);

		spinReadyTime = new Spinner(comp18, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		spinReadyTime.setEnabled(false);
		txtReadyTimeCount = new Label(comp18, SWT.NONE);
		txtReadyTimeCount.setText("���ʼͳ������");
		txtReadyTimeCount.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		txtTip = new Label(group1, SWT.NONE);
		txtTip.setText("��ʾ :");

		Composite comp19 = new Composite(group1, SWT.NONE);
		comp19.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		comp19.setLayout(new GridLayout(1, false));
		GridData gd_textTip = new GridData();
		gd_textTip.grabExcessHorizontalSpace = true;
		gd_textTip.horizontalAlignment = GridData.FILL;
		gd_textTip.horizontalSpan = 3;
		comp19.setLayoutData(gd_textTip);

		textTip = new Text(comp19, SWT.BORDER | SWT.READ_ONLY);
		textTip.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		GridData gd_textTip1 = new GridData(GridData.FILL_HORIZONTAL);
		gd_textTip1.heightHint = 28;
		textTip.setLayoutData(gd_textTip1);
		textTip.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));

		comp2 = new Composite(composite1, SWT.BORDER);
		comp2.setLayout(new GridLayout(1, true));
		GridData gd_comp2 = new GridData(GridData.FILL_VERTICAL);
		gd_comp2.horizontalAlignment = SWT.FILL;
		gd_comp2.heightHint = 335;
		gd_comp2.grabExcessHorizontalSpace = true;
		comp2.setLayoutData(gd_comp2);

		Group group2 = new Group(comp2, SWT.NONE);
		group2.setText("WiFi���ú�״̬");
		GridData gd_group2 = new GridData(GridData.FILL_HORIZONTAL);
		gd_group2.heightHint = 110;
		group2.setLayoutData(gd_group2);
		group2.setLayout(new GridLayout(3, false));

		txtSSID = new Label(group2, SWT.NONE);
		txtSSID.setText("SSID :");

		text_SSID = new Text(group2, SWT.BORDER);
		text_SSID.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		GridData gd_text_SSID = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_SSID.heightHint = 25;
		gd_text_SSID.horizontalSpan = 2;
		text_SSID.setLayoutData(gd_text_SSID);

		txtPassWord = new Label(group2, SWT.NONE);
		txtPassWord.setText("���� :");

		text_PassWord = new Text(group2, SWT.BORDER | SWT.PASSWORD);
		text_PassWord.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		GridData gd_text_PassWord = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_PassWord.heightHint = 25;
		gd_text_PassWord.horizontalSpan = 2;
		text_PassWord.setLayoutData(gd_text_PassWord);

		txtConnect = new Label(group2, SWT.CENTER);
		GridData gd_txtConnect = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
		gd_txtConnect.heightHint = 30;
		txtConnect.setLayoutData(gd_txtConnect);
		txtConnect.setText("�����׼ :");

		btn_net = new Button(group2, SWT.RADIO | SWT.CENTER);
		btn_net.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_net.setSelection(true);
				btn_net1.setSelection(false);
				setSSIDAndPass(telnet);
			}
		});
		btn_net.setText("2.4G");

		btn_net1 = new Button(group2, SWT.RADIO | SWT.CENTER);
		btn_net1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_net1.setSelection(true);
				btn_net.setSelection(false);
				setSSIDAndPass(telnet);
			}
		});
		btn_net1.setText("5G");

		// text_Connect = new Text(group2, SWT.BORDER | SWT.READ_ONLY);
		// text_Connect.setFont(SWTResourceManager.getFont("Microsoft YaHei UI",
		// 10, SWT.NORMAL));
		// GridData gd_text_Connect = new GridData(GridData.FILL_HORIZONTAL);
		// gd_text_Connect.heightHint = 25;
		// text_Connect.setLayoutData(gd_text_Connect);

		Composite comp_21 = new Composite(comp2, SWT.NONE);
		comp_21.setLayout(new GridLayout(3, true));
		GridData gd_comp_21 = new GridData(GridData.FILL_BOTH);
		gd_comp_21.heightHint = 196;
		comp_21.setLayoutData(gd_comp_21);

		txtDeviceType = new Label(comp_21, SWT.NONE);
		GridData gd_DeviceType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_DeviceType.heightHint = 30;
		txtDeviceType.setLayoutData(gd_DeviceType);
		txtDeviceType.setText("��Ʒ�ͺ� :");

		text_DeviceType = new Combo(comp_21, SWT.READ_ONLY);
		text_DeviceType.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		GridData gd_deviceType = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_deviceType.widthHint = 144;
		gd_deviceType.heightHint = 50;
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
					for (String key : allkeys) {
						if (map.get(key) != null) {
							switch (map.get(key)) {
							case "SSID":
							case "SSID1":
							case "SSID2":
								keySSIDlist.add(key);
								break;
							case "SSID����":
							case "SSID1����":
							case "SSID2����":
								keyPasslist.add(key);
								break;
							default:
								break;
							}
						}
					}
				}

			}
		});

		// txtTask = new Label(comp_21, SWT.NONE);
		// GridData gd_taskLab = new GridData(SWT.LEFT, SWT.CENTER, false,
		// false, 1, 1);
		// gd_taskLab.heightHint = 25;
		// txtTask.setLayoutData(gd_taskLab);
		// txtTask.setText("����ID :");
		//
		// text_Task = new Text(comp_21, SWT.BORDER);
		// text_Task.setFont(SWTResourceManager.getFont("Microsoft YaHei UI",
		// 10, SWT.NORMAL));
		// GridData gd_Task = new GridData(SWT.FILL, SWT.CENTER, true, false, 2,
		// 1);
		// gd_Task.widthHint = 144;
		// gd_Task.heightHint = 25;
		// text_Task.setLayoutData(gd_Task);

		label_SN = new Label(comp_21, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_label.heightHint = 30;
		label_SN.setLayoutData(gd_label);
		label_SN.setText("�豸����:");

		text_SN = new Text(comp_21, SWT.BORDER);
		text_SN.setFont(SWTResourceManager.getFont("Monaco", 20, SWT.BOLD));
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_text.heightHint = 57;
		text_SN.setLayoutData(gd_text);
		text_SN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					// String sn =
					// "111001061196979";111050090000004;1002180000000001
					// String sn = CH2018091100000121
					// String seri = "17D0063243";
					// String sn1="000000038B479"
					// String sn="100900022018000200000001"
					// String sn="0043188LT000000001"

					sn = text_SN.getText().trim();

					boolean b = checkSN(sn);
					if (sn.length() > 24 && sn.length() % 24 == 0) {
						sn = sn.substring(0, 24);
					}
					if (sn.length() > 18 && sn.length() % 18 == 0 && sn.matches(".*[a-zA-z].*")) {
						sn = sn.substring(0, 18);
					}
					if (sn.length() > 16 && sn.length() % 16 == 0) {
						sn = sn.substring(0, 16);
					}
					if (sn.length() > 15 && sn.length() % 15 == 0 && !sn.substring(0, 15).matches(".*[a-zA-z].*")) {
						sn = sn.substring(0, 15);
					}
					if (sn.length() > 10 && sn.length() % 10 == 0 && sn.matches(".*[a-zA-z].*")) {
						sn = sn.substring(0, 10);
					}
					if (sn.length() > 13 && sn.length() % 13 == 0 && sn.matches(".*[a-zA-z].*")) {
						sn = sn.substring(0, 13);
					}
					if (b) {
						start();
						System.out.println("SN��ʽ��ȷ");
					} else {
						MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
						mb.setMessage("SN��ʽ����!\n");
						mb.open();
					}
				}
			}
		});

		button = new Button(comp_21, SWT.NONE);
		button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��ʼ����
				if (!text_SN.getText().trim().equals("")) {
					sn = text_SN.getText().trim();
				} else {
					sn = "";
				}
				if (!checkSN(sn)) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("SN��ʽ����\n");
					mb.open();
				} else {
					start();
				}
			}
		});
		button.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.BOLD));
		GridData gd_Button = new GridData(GridData.FILL_HORIZONTAL);
		gd_Button.horizontalSpan = 3;
		gd_Button.widthHint = 207;
		gd_Button.heightHint = 83;
		button.setLayoutData(gd_Button);
		button.setText("��ʼ����");
		new Label(comp_21, SWT.NONE);
		new Label(comp_21, SWT.NONE);
		new Label(comp_21, SWT.NONE);

		comp3 = new Composite(composite1, SWT.BORDER);
		comp3.setLayout(new GridLayout(1, true));
		comp3.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group3 = new Group(comp3, SWT.NONE);
		group3.setText("��ֵ����(��λMbps)��״̬");
		group3.setLayoutData(new GridData(GridData.FILL_BOTH));
		group3.setLayout(new GridLayout(2, false));

		txtMethod = new Label(group3, SWT.NONE);
		txtMethod.setText("��ʽ : ");
		text_Method = new Combo(group3, SWT.BORDER);
		GridData gd_text_Method = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_Method.heightHint = 40;
		text_Method.setLayoutData(gd_text_Method);
		text_Method.setText("ȫ��ָ��");
		text_Method.setEnabled(false);

		txtUpMin = new Label(group3, SWT.NONE);
		txtUpMin.setText("������Сƽ��ֵ :");

		text_UpMin = new Text(group3, SWT.BORDER);
		text_UpMin.setText("20");
		text_UpMin.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		GridData gd_text_UpMin = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_UpMin.heightHint = 30;
		text_UpMin.setLayoutData(gd_text_UpMin);
		text_UpMin.setEnabled(false);

		txtDownMin = new Label(group3, SWT.NONE);
		txtDownMin.setText("������Сƽ��ֵ :");

		text_DownMin = new Text(group3, SWT.BORDER);
		text_DownMin.setText("20");
		text_DownMin.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		GridData gd_text_DownMin = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_DownMin.heightHint = 30;
		text_DownMin.setLayoutData(gd_text_DownMin);
		text_DownMin.setEnabled(false);

		txtAllMin = new Label(group3, SWT.NONE);
		txtAllMin.setText("�ϼ���Сƽ��ֵ :");

		text_AllMin = new Text(group3, SWT.BORDER);
		text_AllMin.setText("40");
		text_AllMin.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		GridData gd_text_AllMin = new GridData(GridData.FILL_HORIZONTAL);
		gd_text_AllMin.heightHint = 30;
		text_AllMin.setLayoutData(gd_text_AllMin);
		text_AllMin.setEnabled(false);
		new Label(group3, SWT.NONE);
		new Label(group3, SWT.NONE);

		txtResult = new Label(group3, SWT.NONE);
		txtResult.setText("\u6D4B\u8BD5\u7ED3\u679C : ");
		GridData gd_txtResult = new GridData(GridData.FILL_HORIZONTAL);
		gd_txtResult.heightHint = 30;
		txtResult.setLayoutData(gd_txtResult);
		new Label(group3, SWT.NONE);

		text_Result = new Text(group3, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		text_Result.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 30, SWT.BOLD));
		GridData gd_textResult = new GridData(GridData.FILL_HORIZONTAL);
		gd_textResult.heightHint = 82;
		gd_textResult.horizontalSpan = 2;
		text_Result.setLayoutData(gd_textResult);

		Composite composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayout(new GridLayout(1, false));
		composite2.setLayoutData(new GridData(GridData.FILL_BOTH));

		table = new Table(composite2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
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
		tblclmnClom.setWidth(80);
		tblclmnClom.setText("���");

		TableColumn tblclmnClom_1 = new TableColumn(table, SWT.CENTER);
		tblclmnClom_1.setWidth(60);
		tblclmnClom_1.setText("ͼ��");

		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(150);
		tableColumn_2.setText("������");

		TableColumn tblclmnColom_3 = new TableColumn(table, SWT.CENTER);
		tblclmnColom_3.setWidth(150);
		tblclmnColom_3.setText("��ǰ����(Mbps)");

		TableColumn tblclmnCol_4 = new TableColumn(table, SWT.CENTER);
		tblclmnCol_4.setWidth(150);
		tblclmnCol_4.setText("ƽ������(Mbps)");

		TableColumn tblclmnCol_5 = new TableColumn(table, SWT.CENTER);
		tblclmnCol_5.setWidth(150);
		tblclmnCol_5.setText("״̬");

		TableColumn tableColumn_6 = new TableColumn(table, SWT.CENTER);
		tableColumn_6.setWidth(179);
		tableColumn_6.setText("�����ϼ�(�ֽ�)");

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
				wifiData.upDownStream = "����" + (i + 1);
			} else if (i >= 3 && i < 6) {
				wifiData.image = "/icon/arrow2.png";
				wifiData.upDownStream = "����" + (i + 1);
			} else if (i >= 6 && i < 8) {
				wifiData.image = "/icon/all.png";
				if (i == 6) {
					wifiData.upDownStream = "���кϼ�";
				} else {
					wifiData.upDownStream = "���кϼ�";
				}
			} else {
				wifiData.image = "/icon/total.png";
				wifiData.upDownStream = "�ܼ�";
			}
			wifiDatas.add(wifiData);
		}
		text_DeviceType.setItems(DeviceType.list());
		text_DeviceType.select(0);
		// ���ݿ��ʼ��
		// DbManager.getInstance().init();
		loadProperties(defConfPath);
		startStatusCleanner();
		initTableData();
		// �ж������Ƿ�����(��������)
		try {
			if (IPUtil.getIpAddrByName("wlan2") == null) {
				text_Wip.setText("");
			} else {
				text_Wip.setText(IPUtil.getIpAddrByName("wlan2"));
			}
			text_Yip.setText(IPUtil.getIpAddrByName("eth8"));
			// ����Telnet
			EnableTelnet();
			// Telnet��¼
			telnet = new TelnetOperator();
			boolean flag = telnet.login("192.168.1.1", 23, "192.168.1.10", 0, "CMCCAdmin", "aDm8H%MdA");
			if (flag) {
				setSSIDAndPass(telnet);
				setStatusString("Telnet��¼�ɹ�");
			} else {
				text_SN.setText("");
				text_SSID.setText("");
				text_PassWord.setText("");
				MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Telnet��¼ʧ��!\n");
				mb.open();
			}
		} catch (Exception e) {
			text_Wip.setText("");
			text_Yip.setText("");
			text_SN.setText("");
			text_SSID.setText("");
			text_PassWord.setText("");
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setText("������ʾ");
			mb.setMessage("����δ����,������߲���Wlan!\n");
			mb.open();
		}
	}

	/**
	 * ����Telnet
	 */
	public void EnableTelnet() {
		/**
		 * ͨ���ӿڵ�ַ��http://192.168.1.1/app_telnet_info.gch��Telnet����
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
				setStatusString("Telnet������");
			} else {
				MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Telnet����ʧ��!\n");
				mb.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����SSID������
	 * 
	 * @param telnet
	 */
	public void setSSIDAndPass(TelnetOperator telnet) {
		text_SN.setText(telnet.getResult("sismac 2 512"));// SN = CMDCA40007E6
		if (btn_net.getSelection()) {
			text_SSID.setText(telnet.getResult("sismac 2 1024"));// SSID1 =
																	// CMCC-FxFh
			text_PassWord.setText(telnet.getResult("sismac 2 1280"));// Password1
																		// =
																		// 6nvsnett
		} else {
			text_SSID.setText(telnet.getResult("sismac 2 1028"));// SSID2 =
																	// CMCC-FxFh-5G
			text_PassWord.setText(telnet.getResult("sismac 2 1296"));// Password2
																		// =
																		// 6nvsnett
		}
	}

	/**
	 * ���SN��ʽ
	 * 
	 * @return
	 */
	public boolean checkSN(String sn) {
		boolean b = false;
		String reg = "^[\\d]{15}$|[a-zA-Z0-9]{13}$|[0-9]+[A-Z]\\d{7}$|[\\d]{24}$|[a-zA-Z0-9]{18}$|[\\d]{16}$||[a-zA-Z0-9]{12}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(sn);
		b = matcher.matches();
		return b;
	}

	/**
	 * ��ʼ����
	 */
	public void start() {
		if (text_File.getText().equals("")) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("�뵼�������ļ�\n");
			mb.open();
		} else {
			if (button.getImage().equals(SWTResourceManager.getImage(MainView.class, "/icon/start.png"))) {
				button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/pause.png"));
				button.setText("ֹͣ����");
				setStatusString("��ʼ����");
				text_Result.setText("");
				// boolean isConnect = WifiConnect.Connect(text_SSID.getText(),
				// text_PassWord.getText());
				boolean isConnect = false;
				if (!isConnect) {
					// ֹͣ��־λ
					setStop(true);
					// "�߳�ֹͣ"����
					PostThread.stop = true;
					ReceiveThread.stop = true;
					MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
					mb.setText("������ʾ");
					mb.setMessage("WIFI����ʧ��!\n");
					if (mb.open() == SWT.OK) {
						button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
						button.setText("��ʼ����");
						setStatusString("ֹͣ����");
					}
				} else {
					// ȡ��"ֹͣ����"����
					setStop(false);
					// ȡ��"�߳�ֹͣ"����
					PostThread.stop = false;
					ReceiveThread.stop = false;
					// ��ʼ���б�
					initTableData();
					/**
					 * ����socketͨ���߳� 1.���д��������̣߳��ɿͻ��������˷������ݰ�
					 * 2.���д��������̣߳��ɷ�������ͻ��˷������ݰ�
					 * 3.ʵʱ���Ե�ǰ����(ÿ�봫���������)��ƽ������(������������/������ʱ��)
					 * 4.�����ݱ��浽data.xml�ļ���
					 */
					try {
						TimeUnit.MILLISECONDS.sleep(2000);
						// ��ȡ��������ʾ���߼�����IP��ַ
						text_Wip.setText(IPUtil.getIpAddrByName("wlan2"));
						text_Yip.setText(IPUtil.getIpAddrByName("eth8"));
					} catch (Exception e) {
						// ֹͣ��־λ
						setStop(true);
						// "�߳�ֹͣ"����
						PostThread.stop = true;
						ReceiveThread.stop = true;
						MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
						mb.setMessage("��ȡIPʧ��,��������!\n");
						if (mb.open() == SWT.OK) {
							button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
							button.setText("��ʼ����");
							setStatusString("ֹͣ����");
						}
					}
					// ��������
					storeProperties(defConfPath);
					// ����ͨ���߳�
					if (isfirst) {
						ServerThread.startServer("server1", 8899);
						ServerThread.startServer("server2", 9988);// ���������߳�
						isfirst = false;
					}
					Serversocket.createServerSocket("Dev1");// �ͻ����߳�
					/**
					 * ʵʱ���������б��߳�
					 */
					startFreshDataThread();
					/**
					 * ��������
					 */
					for (WifiData wifiData : wifiDatas) {
						wifiData.curRate = 0;
						wifiData.averageRate = 0;
						wifiData.totalTraffic = 0;
					}
				}
			} else {
				button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
				button.setText("��ʼ����");
				setStatusString("ֹͣ����");
				// ֹͣ��־λ
				setStop(true);
				PostThread.stop = true;
				ReceiveThread.stop = true;
				// ��������ʣ��ʱ��
				progressBar.setSelection(progressBar.getMaximum());
				text_ReTime.setText("0");
				// �����б�����
				try {
					Thread.sleep(1000);
					// ֹͣ�߳�
					new Thread(new StopThread("Dev1")).start();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * ʵʱ���������б��߳�
	 */
	private void startFreshDataThread() {
		/**
		 * ʵʱ���������б��߳� 1.���Ӻ�spinReadyTime���ʼͳ������
		 * 2.ÿ��1��ˢ��һ�������б����Թ��̳���ʱ��spinTimeSecond�� 3.���½����� 4.ͳ�ƺϼ�����
		 * ��SWT�̵߳��߳�����Ҫ�޸�SWT����Display.getDefault().asyncExec(new Runnable() {});
		 */
		int time = Integer.parseInt(spinTimeMinute.getText()) * 60 + Integer.parseInt(spinTimeSecond.getText());
		// int readyTime = Integer.parseInt(spinReadyTime.getText());
		new Thread(new Runnable() {
			private int pace;

			@Override
			public void run() {
				try {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							pace = progressBar.getMinimum();
							lblNewLabel.setText(pace + "%");
						}
					});
					long start = System.currentTimeMillis();
					while (!stop && System.currentTimeMillis() - start <= time * 1000) {
						// while (!stop && System.currentTimeMillis() - start <=
						// (time + readyTime) * 1000) {
						// if (System.currentTimeMillis() - start < readyTime
						// *1000) {
						// Thread.sleep(1000);
						// continue;
						// } else {
						// pace = (int) ((double) (System.currentTimeMillis() -
						// start - readyTime * 1000) / (time * 1000) * 100);
						pace = (int) ((double) (System.currentTimeMillis() - start) / (time * 1000) * 100);
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								freshData();
								progressBar.setSelection(pace);
								lblNewLabel.setText(pace + "%");
								text_ReTime.setText((time - (System.currentTimeMillis() - start) / 1000) + "");
								// text_ReTime.setText((time -
								// (System.currentTimeMillis() - start -
								// readyTime * 1000) / 1000) + "");
							}
						});
						Thread.sleep(1000);
						// }
					}
					PostThread.stop = true;
					ReceiveThread.stop = true;
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							progressBar.setSelection(progressBar.getMaximum());
							lblNewLabel.setText("100%");
							text_ReTime.setText("0");
						}
					});
					// ͳ�ƺϼ�����
					consultTotalData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ʼ���б�
	 */
	public static List<TableItem> initTableData() {
		table.removeAll();
		List<TableItem> items = new ArrayList<>();

		for (WifiData data : wifiDatas) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, data.getID() + "");
			item.setImage(1, SWTResourceManager.getImage(MainView.class, data.getImage()));
			item.setText(2, data.getUpDownStream());
			item.setData(data);
			table.getParent().layout();
			items.add(item);
		}
		return items;
	}

	/**
	 * �����б�����
	 */
	public static void freshData() {
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

	/**
	 * ����ϼ����ݲ��жϽ������������
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
		wifiDatas.get(6).status = "�ѽ���";
		wifiDatas.get(6).totalTraffic = upTotalTraffic;
		wifiDatas.get(6).id = 7;

		wifiDatas.get(7).curRate = downTotalCurRate;
		wifiDatas.get(7).averageRate = downTotalAveRate;
		wifiDatas.get(7).status = "�ѽ���";
		wifiDatas.get(7).totalTraffic = downTotalTraffic;
		wifiDatas.get(7).id = 8;

		wifiDatas.get(8).curRate = new BigDecimal(upTotalCurRate + downTotalCurRate)
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		wifiDatas.get(8).averageRate = new BigDecimal(upTotalAveRate + downTotalAveRate)
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		wifiDatas.get(8).status = "�ѽ���";
		wifiDatas.get(8).totalTraffic = upTotalTraffic + downTotalTraffic;
		wifiDatas.get(8).id = 9;

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				freshData();
				setStatusString("���Խ���");
				button.setImage(SWTResourceManager.getImage(MainView.class, "/icon/start.png"));
				button.setText("��ʼ����");
				if (wifiDatas.get(6).averageRate >= Double.parseDouble(text_UpMin.getText())
						&& wifiDatas.get(7).averageRate >= Double.parseDouble(text_DownMin.getText())) {
					text_Result.setText("Pass");
					text_Result.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				} else {
					text_Result.setText("Fail");
					text_Result.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				try {
					if (wifiDatas.get(6).averageRate != 0 && wifiDatas.get(7).averageRate != 0) {
						String netType = "";
						if (btn_net.getSelection()) {
							netType = "2.4G";
						}
						if (btn_net1.getSelection()) {
							netType = "5G";
						}
						XmlParse.saveDataForXML(sn, netType, wifiDatas.get(6).averageRate, wifiDatas.get(7).averageRate,
								wifiDatas.get(8).averageRate, text_Result.getText(),
								new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					}
					// �������Ա���
					ExportTestReport.testReportToDesktop();
					setStatusString("�ɹ��������Ա���");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			prop.setProperty("ClientIp", text_Wip.getText());
			prop.setProperty("ClientPort", text_WPort.getText());
			prop.setProperty("ServerIp", text_Yip.getText());
			prop.setProperty("ServerPort", text_YPort.getText());
			prop.setProperty("UpStreamCount", text_UpStream.getText());
			prop.setProperty("DownStreamCount", text_DownStream.getText());
			prop.setProperty("DurationMinutes", spinTimeMinute.getText());
			prop.setProperty("DurationSeconds", spinTimeSecond.getText());
			// prop.setProperty("AfterSecsStat", spinReadyTime.getText());
			prop.setProperty("SSID", text_SSID.getText());
			prop.setProperty("Passsword", text_PassWord.getText());
			prop.setProperty("NetType", btn_net.getSelection() ? "2.4G" : "5G");
			// prop.setProperty("TaskId", text_Task.getText());
			prop.setProperty("SN", text_SN.getText());
			prop.setProperty("MinUpAverageRate", text_UpMin.getText());
			prop.setProperty("MinDownAverageRate", text_DownMin.getText());
			prop.setProperty("MinTotalAverageRate", text_AllMin.getText());

			prop.storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			setStatusString("���ñ���ɹ�");
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("���ñ���ʧ��:\n" + e.getMessage());
			mb.open();
		}
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
				setStatusString("���õ���ɹ�");
				text_File.setText(file);
			} else {
				text_File.setText(pathname);
			}
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
		// text_Task.setText(MainView.getProp().getProperty("TaskId",
		// "180918104712"));
		text_SN.setText(MainView.getProp().getProperty("SN", "1002180000000001"));
		text_Wip.setText(MainView.getProp().getProperty("ClientIp", "192.168.1.4"));
		text_WPort.setText(MainView.getProp().getProperty("ClientPort", "9988"));
		text_Yip.setText(MainView.getProp().getProperty("ServerIp", "192.168.1.3"));
		text_YPort.setText(MainView.getProp().getProperty("ServerPort", "8899"));
		text_UpStream.setText(MainView.getProp().getProperty("UpStreamCount", "3"));
		text_DownStream.setText(MainView.getProp().getProperty("DownStreamCount", "3"));
		spinTimeMinute.setSelection(Integer.parseInt(MainView.getProp().getProperty("DurationMinutes", "0")));
		spinTimeSecond.setSelection(Integer.parseInt(MainView.getProp().getProperty("DurationSeconds", "20")));
		spinReadyTime.setSelection(Integer.parseInt(MainView.getProp().getProperty("AfterSecsStat", "2")));
		text_SSID.setText(MainView.getProp().getProperty("SSID", "scty"));
		text_PassWord.setText(MainView.getProp().getProperty("Passsword", "123456789"));
		btn_net.setSelection(MainView.getProp().getProperty("NetType").equals("2.4G") ? true : false);
		btn_net1.setSelection(MainView.getProp().getProperty("NetType").equals("5G") ? true : false);
		text_UpMin.setText(MainView.getProp().getProperty("MinUpAverageRate", "20"));
		text_DownMin.setText(MainView.getProp().getProperty("MinDownAverageRate", "20"));
		text_AllMin.setText(MainView.getProp().getProperty("MinTotalAverageRate", "40"));
	}

	/**
	 * ������ʾ״̬
	 * 
	 * @param string
	 */
	public void setStatusString(final String status) {

		lastStatusSetTime = System.currentTimeMillis();
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (textTip.isDisposed())
					return;
				textTip.setText(status);
			}
		});
	}

	/**
	 * ״̬�ַ����
	 */
	public void startStatusCleanner() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (textTip.isDisposed() == false) {
					if (lastStatusSetTime > 0 && System.currentTimeMillis() - lastStatusSetTime > 8000) {
						setStatusString("");
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
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		MainView mv = MainView.getInstance();
		mv.open();
	}

	public static MainView getInstance() {
		if (instance == null || instance.isDisposed()) {
			instance = new MainView(Display.getDefault());
		}
		return instance;
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
	 * ����ֹͣ��־λ
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}
}
