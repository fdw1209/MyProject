package com.ch.sockettest.popview;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.DeviceType;
import com.ch.sockettest.dao.PreDataConfig;
import com.ch.sockettest.main.MainView;

/**
 * ����������ͼ
 * 
 * @author fdw
 *
 */
public class CompParaConfigView extends AbsPopWindow {

	private Label labFile;
	private Text text_File;
	private Button btnOpenFile;
	private Label labDeviceType;
	private Combo text_DeviceType;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Spinner spinner;
	public static final String defConfPath = System.getProperty("user.dir") + File.separator + "config.xml";
	private Label label_5;
	private Combo text_Stream;

	public CompParaConfigView(Display display, Shell parent) {
		super(display);
		setText("��������");
		setSize(484, 620);
		setParentShell(parent);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		labFile = new Label(composite, SWT.NONE);
		labFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labFile.setText("�����ļ� :");
		labFile.setBounds(25, 22, 82, 25);

		// ��ʾ�����ļ�
		text_File = new Text(composite, SWT.BORDER | SWT.WRAP);
		text_File.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_File.setEnabled(false);
		text_File.setEditable(false);
		text_File.setBounds(60, 53, 332, 30);
		
		btnOpenFile = new Button(composite, SWT.NONE);
		btnOpenFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// �������ļ�
				loadProperties(null);
			}
		});
		btnOpenFile.setImage(SWTResourceManager.getImage(MainView.class, "/icon/document-open-folder.png"));
		btnOpenFile.setToolTipText("�������ļ�");
		btnOpenFile.setBounds(398, 53, 30, 30);
		
		labDeviceType = new Label(composite, SWT.NONE);
		labDeviceType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labDeviceType.setSize(85, 25);
		labDeviceType.setLocation(25, 103);
		labDeviceType.setText("��Ʒ�ͺ� :");

		text_DeviceType = new Combo(composite, SWT.READ_ONLY);
		text_DeviceType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		text_DeviceType.setSize(368, 28);
		text_DeviceType.setLocation(60, 134);
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
		
		Label label = new Label(composite, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label.setBounds(25, 182, 90, 20);
		label.setText("��׼��Ҫ�� :");

		Label labUpMin = new Label(composite, SWT.NONE);
		labUpMin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		labUpMin.setBounds(102, 223, 82, 20);
		labUpMin.setText("������Сֵ :");
		
		text = new Text(composite, SWT.BORDER | SWT.CENTER);
		text.setText("20");
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text.setBounds(219, 218, 73, 31);
		
		Label lblMbps = new Label(composite, SWT.NONE);
		lblMbps.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblMbps.setBounds(308, 223, 56, 20);
		lblMbps.setText("Mbps");
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("������Сֵ :");
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setBounds(102, 270, 90, 20);

		text_1 = new Text(composite, SWT.BORDER | SWT.CENTER);
		text_1.setText("20");
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_1.setBounds(219, 268, 73, 31);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("Mbps");
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setBounds(308, 272, 56, 20);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText("�ϼ���Сֵ :");
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_3.setBounds(102, 320, 90, 20);

		text_2 = new Text(composite, SWT.BORDER | SWT.CENTER);
		text_2.setText("40");
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		text_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		text_2.setBounds(219, 318, 73, 31);

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setText("Mbps");
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_4.setBounds(308, 320, 56, 20);
		
		label_1 = new Label(composite, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_1.setBounds(25, 456, 75, 20);
		label_1.setText("\u6D4B\u8BD5\u65F6\u95F4 \uFF1A");

		spinner = new Spinner(composite, SWT.BORDER);
		spinner.setSelection(20);
		spinner.setBounds(102, 486, 82, 26);

		label_2 = new Label(composite, SWT.NONE);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_2.setBounds(200, 488, 25, 24);
		label_2.setText("\u79D2");
		
		Button btn_save = new Button(composite, SWT.NONE);
		btn_save.setSelection(true);
		btn_save.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btn_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeProperties(defConfPath);
				close();
			}
		});
		btn_save.setBounds(316, 510, 112, 39);
		btn_save.setText("��������");
		
		label_5 = new Label(composite, SWT.NONE);
		label_5.setText("\u4E0A\u4E0B\u884C\u6D41 \uFF1A");
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_5.setBounds(25, 362, 75, 20);
		
		// ������
		text_Stream = new Combo(composite, SWT.BORDER);
		text_Stream.setBounds(102, 398, 112, 28);
		text_Stream.setItems(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
		text_Stream.setText("3");
		text_Stream.setEnabled(true);
		
		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		label_6.setBounds(232, 401, 30, 20);
		label_6.setText("\u6761");
	}

	@Override
	protected void createContents() {

	}

	@Override
	public void initData() {
		text_DeviceType.setItems(DeviceType.list());
		text_DeviceType.select(0);
		loadProperties(defConfPath);
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
			MainView.getProp().setProperty("MinUpAverageRate", text.getText());
			MainView.getProp().setProperty("MinDownAverageRate", text_1.getText());
			MainView.getProp().setProperty("MinTotalAverageRate", text_2.getText());
			MainView.getProp().setProperty("TestTime", spinner.getText());
			MainView.getProp().setProperty("DeviceType", text_DeviceType.getText());
			MainView.getProp().setProperty("StreamCount", text_Stream.getText());

			MainView.getProp().storeToXML(new FileOutputStream(file), new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			MainView.getInstance().setStatusString("���ñ���ɹ�",SWT.COLOR_DARK_GREEN);
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
			MainView.getProp().putAll(p);
			// ��������
			loadData();
			if (pathname == null) {
				text_File.setText(file);
			} else {
				text_File.setText(pathname);
			}
			MainView.getInstance().setStatusString("�����ļ�����ɹ�",SWT.COLOR_DARK_GREEN);
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
		text.setText(MainView.getProp().getProperty("MinUpAverageRate", "20"));
		text_1.setText(MainView.getProp().getProperty("MinDownAverageRate", "20"));
		text_2.setText(MainView.getProp().getProperty("MinTotalAverageRate", "20"));
		spinner.setSelection(Integer.parseInt(MainView.getProp().getProperty("TestTime", "20")));
		text_DeviceType.setText(MainView.getProp().getProperty("DeviceType"));
		text_Stream.setText(MainView.getProp().getProperty("StreamCount"));
	}
}
