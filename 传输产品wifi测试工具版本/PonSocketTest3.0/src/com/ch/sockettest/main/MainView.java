package com.ch.sockettest.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.State;
import com.ch.sockettest.popview.AbsPopWindow;
import com.ch.sockettest.popview.CompDatabaseConfView;
import com.ch.sockettest.popview.CompDev1TestTable;
import com.ch.sockettest.popview.CompDev2TestTable;
import com.ch.sockettest.popview.CompNICConfView;
import com.ch.sockettest.popview.CompParaConfigView;
import com.ch.sockettest.popview.CompStatus;
import com.ch.sockettest.popview.Navigator;
import com.ch.sockettest.popview.VersionsView;
import com.ch.sockettest.until.ExportTestReport;
import com.ch.sockettest.until.StateUpdater;

public class MainView extends AbsPopWindow {
	public static Navigator navi;
	private static MainView instance;
	private CompStatus statusBar;
	private Label lbl_status;
	private Label lbl_tip;
	private long lastStatusSetTime;
	public static boolean isDev1first = true;
	public static boolean isDev2first = true;
	private static Properties prop = new Properties();
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public MainView(Display display) {
		super(display);
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),
						SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				mb.setText("�˳�ϵͳ��ʾ");
				mb.setMessage("�Ƿ�Ҫ�˳�����ϵͳ��");
				int k = mb.open();
				if (k == SWT.YES) {
					close(true);
				} else {
					e.doit = false;
				}
			}
		});
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setText("�����Ʒwifi���� V2.0��");
		setMinimumSize(new Point(1000, 800));
		// setSize(1024, 900);
		setSize(1912, 897);
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
		menuItem_1.setText("��������");
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CompParaConfigView view = new CompParaConfigView(Display.getDefault(), instance);
				view.open();
			}
		});

		MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
		menuItem_2.setText("���ݿ�����");
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��������������󣬴����ݿ����ý���
				CompDatabaseConfView view = new CompDatabaseConfView(getDisplay(), instance);
				view.open();
			}
		});

		MenuItem menuItem_3 = new MenuItem(menu, SWT.CASCADE);
		menuItem_3.setText("��������");
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��������
				CompNICConfView v = new CompNICConfView(Display.getDefault(), instance);
				v.open();
			}
		});

		MenuItem menuItem_4 = new MenuItem(menu, SWT.CASCADE);
		menuItem_4.setText("���ݵ���");
		menuItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// �������Ա���
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setText("������������");
					fd.setFileName("�����ƷWiFi��������������.xls");
					fd.setFilterExtensions(new String[] { "*.xls" });
					String file = fd.open();
					if (file == null)
						return;
					// ���Ϊ
					ExportTestReport.testReportSave(file);
					// setStatusString("�ɹ��������Ա���");

					MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
					mb.setText("ϵͳ��ʾ");
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

		Composite comp = new Composite(this, SWT.NONE);
		comp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		comp.setLayout(new GridLayout(1, false));
		GridData gd_comp = new GridData(GridData.FILL);
		gd_comp.grabExcessHorizontalSpace = true;
		gd_comp.horizontalAlignment = SWT.FILL;
		gd_comp.heightHint = 60;
		comp.setLayoutData(gd_comp);

		lbl_status = new Label(comp, SWT.WRAP | SWT.CENTER);
		GridData gd_lbl_status = new GridData(GridData.FILL_HORIZONTAL);
		gd_lbl_status.heightHint = 50;
		lbl_status.setLayoutData(gd_lbl_status);
		lbl_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lbl_status.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 20, SWT.BOLD));
		lbl_status.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		lbl_status.setText("�ȴ��豸����...");

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite1 = new Composite(composite, SWT.NONE);
		composite1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite1.setLayout(new GridLayout(2, false));
		GridData gd_composite1 = new GridData(GridData.FILL_HORIZONTAL);
		gd_composite1.heightHint = 672;
		composite1.setLayoutData(gd_composite1);
		// �豸1�����б�
		Composite comp1 = new CompDev1TestTable(composite1, SWT.NONE);
		comp1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		comp1.setLayoutData(new GridData(GridData.FILL_BOTH));
		// �豸2�����б�
		Composite comp2 = new CompDev2TestTable(composite1, SWT.NONE);
		comp2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		comp2.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite2 = new Composite(composite, SWT.NONE);
		composite2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite2.setLayout(new GridLayout(2, false));
		composite2.setLayoutData(new GridData(GridData.FILL_BOTH));

		lbl_tip = new Label(composite2, SWT.NONE);
		lbl_tip.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lbl_tip.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.BOLD));
		lbl_tip.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lbl_tip.setLayoutData(new GridData(GridData.FILL_BOTH));

		statusBar = new CompStatus(composite2, SWT.NONE);
		statusBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData grddata = new GridData(GridData.FILL_BOTH);
		grddata.heightHint = 40;
		statusBar.setLayoutData(grddata);

	}

	@Override
	public void initData() {
		loadProperties(defConfPath);
		startStatusCleanner();
		new StateUpdater().init();
		startStateUpdate();
	}

	/**
	 * ״̬�ַ����
	 */
	public void startStatusCleanner() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (lbl_tip.isDisposed() == false) {
					if (lastStatusSetTime > 0 && System.currentTimeMillis() - lastStatusSetTime > 5000) {
						setStatusString("", SWT.COLOR_DARK_GREEN);
						lastStatusSetTime = 0;
					}
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * ������ʾ״̬
	 * 
	 * @param string
	 */
	public void setStatusString(final String status, final int color) {

		lastStatusSetTime = System.currentTimeMillis();
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (lbl_tip.isDisposed())
					return;
				lbl_tip.setText(status);
				lbl_tip.setForeground(SWTResourceManager.getColor(color));
			}
		});
	}

	/**
	 * �豸״̬����
	 */
	private void startStateUpdate() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!disposed()) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							update(lbl_status, StateUpdater.NC_ETH_STATE);
						}
					});
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private void update(Label label, State st) {
				if (label.isEnabled() == false)
					return;
				if (st == State.NOTREADY) {
					label.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
				} else if (st == State.CONNECTED) {
					label.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				} else {
					label.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				label.setText(st.toStringCh());
			}
		}).start();
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
			setStatusString("�����ļ�����ɹ�", SWT.COLOR_DARK_GREEN);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("���õ���ʧ��:\n" + e.getMessage());
			mb.open();
		}
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
			prop.storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			setStatusString("���ñ���ɹ�", SWT.COLOR_DARK_GREEN);
		} catch (Exception e) {
			MessageBox mb = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
			mb.setMessage("���ñ���ʧ��:\n" + e.getMessage());
			mb.open();
		}
	}

	/**
	 * ��ȡ��ǰ��������Ϣ
	 * 
	 * @return
	 */
	public static Properties getProp() {
		return prop;
	}

	private boolean disposed() {
		return super.isDisposed();
	}

	public void close(boolean close) {
		StateUpdater.closed = true;
		disposed();
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		MainView mp = MainView.getInstance();
		mp.open();
	}

	public static MainView getInstance() {
		if (instance == null || instance.isDisposed()) {
			instance = new MainView(Display.getDefault());
		}
		return instance;
	}

	public void setStatus(final String msg, final int color, boolean stay) {
		if (statusBar != null && statusBar.isDisposed() == false)
			statusBar.setStatus(msg, color, stay);
	}
}
