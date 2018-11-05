package com.ch.sockettest.popview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.main.MainView;
import com.ch.sockettest.until.IPUtil;
import com.ch.sockettest.until.LabelShow;

/**
 * 双网卡配置视图
 * 
 * @author fdw
 * 
 */
public class CompNICConfView extends AbsPopWindow {

	private Combo combo1;
	private Combo combo2;
	private Label tips;
	private Combo combo4;
	private Combo combo3;
	private Button btn_R1;
	private Button btn_R2;
	private Button btn_R3;
	private Button btn_R4;
	public static long lastStatusSetTime;
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";

	public CompNICConfView(Display display, Shell parent) {
		super(display);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setText("网络配置");
		setSize(710, 431);
		setParentShell(parent);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group1 = new Group(composite, SWT.NONE);
		group1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group1.setText("\u8BBE\u59071");
		group1.setBounds(10, 0, 316, 262);

		Label label = new Label(group1, SWT.NONE);
		label.setBounds(10, 28, 160, 17);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label.setText("\u6709\u7EBF\u7F51\u5361");

		combo1 = new Combo(group1, SWT.READ_ONLY);
		combo1.setBounds(10, 51, 288, 25);

		Label label_1 = new Label(group1, SWT.NONE);
		label_1.setBounds(10, 108, 160, 17);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setText("\u65E0\u7EBF\u7F51\u5361");

		combo2 = new Combo(group1, SWT.READ_ONLY);
		combo2.setBounds(10, 130, 288, 28);

		Label label_3 = new Label(group1, SWT.NONE);
		label_3.setText("\u7F51\u7EDC\u6807\u51C6");
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_3.setBounds(10, 200, 61, 20);

		btn_R1 = new Button(group1, SWT.RADIO);
		btn_R1.setAlignment(SWT.CENTER);
		btn_R1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R1.setSelection(true);
				btn_R2.setSelection(false);
			}
		});
		btn_R1.setSelection(true);
		btn_R1.setBounds(97, 200, 61, 20);
		btn_R1.setText("2.4G");

		btn_R2 = new Button(group1, SWT.RADIO);
		btn_R2.setAlignment(SWT.CENTER);
		btn_R2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R2.setSelection(true);
				btn_R1.setSelection(false);
			}
		});
		btn_R2.setText("5G");
		btn_R2.setBounds(213, 200, 51, 20);

		Group group = new Group(composite, SWT.NONE);
		group.setText("\u8BBE\u59072");
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		group.setBounds(353, 0, 316, 262);

		Label label_5 = new Label(group, SWT.NONE);
		label_5.setText("\u6709\u7EBF\u7F51\u5361");
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_5.setBounds(10, 28, 160, 17);

		combo3 = new Combo(group, SWT.READ_ONLY);
		combo3.setBounds(10, 51, 288, 28);

		Label label_7 = new Label(group, SWT.NONE);
		label_7.setText("\u65E0\u7EBF\u7F51\u5361");
		label_7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_7.setBounds(10, 108, 160, 17);

		combo4 = new Combo(group, SWT.READ_ONLY);
		combo4.setBounds(10, 130, 288, 28);
		
		Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("\u7F51\u7EDC\u6807\u51C6");
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_4.setBounds(10, 200, 61, 20);

		btn_R3 = new Button(group, SWT.RADIO);
		btn_R3.setAlignment(SWT.CENTER);
		btn_R3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R3.setSelection(true);
				btn_R4.setSelection(false);
			}
		});
		btn_R3.setSelection(true);
		btn_R3.setBounds(96, 200, 61, 20);
		btn_R3.setText("2.4G");

		btn_R4 = new Button(group, SWT.RADIO);
		btn_R4.setAlignment(SWT.CENTER);
		btn_R4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_R4.setSelection(true);
				btn_R3.setSelection(false);
			}
		});
		btn_R4.setText("5G");
		btn_R4.setBounds(219, 200, 51, 20);
		
		tips = new Label(composite, SWT.NONE);
		tips.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		tips.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		tips.setBounds(10, 310, 402, 42);

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
		button_1.setBounds(519, 310, 129, 42);
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
				fd.setFileName("config.xml");
				fd.setFilterExtensions(new String[] { "*.xml" });
				file = fd.open();
				if (file == null)
					return;
			} else {
				file = pathfile;
			}
			MainView.getProp().setProperty("Dev1_CableNet", combo1.getText());
			MainView.getProp().setProperty("Dev1_WirelessNet", combo2.getText());
			MainView.getProp().setProperty("Dev1_NetType", btn_R1.getSelection() ? "2.4G" : "5G");
			MainView.getProp().setProperty("Dev2_CableNet", combo3.getText());
			MainView.getProp().setProperty("Dev2_WirelessNet", combo4.getText());
			MainView.getProp().setProperty("Dev2_NetType", btn_R3.getSelection() ? "2.4G" : "5G");
			
			MainView.getProp().storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
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
			MainView.getProp().putAll(p);
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
		combo1.setText(MainView.getProp().getProperty("Dev1_CableNet", ""));
		combo2.setText(MainView.getProp().getProperty("Dev1_WirelessNet", ""));
		combo3.setText(MainView.getProp().getProperty("Dev2_CableNet", ""));
		combo4.setText(MainView.getProp().getProperty("Dev2_WirelessNet", ""));
		btn_R1.setSelection(MainView.getProp().getProperty("Dev1_NetType").equals("2.4G") ? true : false);
		btn_R2.setSelection(MainView.getProp().getProperty("Dev1_NetType").equals("5G") ? true : false);
		btn_R3.setSelection(MainView.getProp().getProperty("Dev2_NetType").equals("2.4G") ? true : false);
		btn_R4.setSelection(MainView.getProp().getProperty("Dev2_NetType").equals("5G") ? true : false);
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

	public static void main(String[] argvs) {
		CompNICConfView view = new CompNICConfView(Display.getDefault(), null);
		view.open();
	}
}
