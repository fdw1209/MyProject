package com.ch.sockettest.shell;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.dao.DbManager;
import com.ch.sockettest.dao.DbType;
import com.ch.sockettest.until.LabelShow;
import com.ch.sockettest.until.Utils;

/**
 * 数据库配置视图
 * 
 * @author LYJ
 * 
 */
public class CompDatabaseConfView extends Composite {
	private Text text;
	private Text text_ip;
	private Text text_2;
	private Text text_port;
	private Text text_user;
	private Text text_5;
	private Text text_6;
	private Text text_pass;
	private Combo combo;
	private Label tipsLabel;

	public CompDatabaseConfView(Composite parent, int style) {
		super(parent, style);

		text = new Text(this, SWT.NONE);
		text.setText("数据库主机");
		text.setEditable(false);
		text.setBounds(22, 74, 92, 23);

		text_ip = new Text(this, SWT.BORDER);
		text_ip.setBounds(139, 71, 149, 23);

		text_2 = new Text(this, SWT.NONE);
		text_2.setText("数据库端口");
		text_2.setEditable(false);
		text_2.setBounds(22, 103, 92, 23);

		text_port = new Text(this, SWT.BORDER);
		text_port.setBounds(140, 100, 149, 23);

		text_user = new Text(this, SWT.BORDER);
		text_user.setBounds(140, 129, 149, 23);

		text_5 = new Text(this, SWT.NONE);
		text_5.setText("数据库用户名");
		text_5.setEditable(false);
		text_5.setBounds(22, 132, 92, 23);

		text_6 = new Text(this, SWT.NONE);
		text_6.setText("数据库密码  ");
		text_6.setEditable(false);
		text_6.setBounds(22, 161, 92, 23);

		text_pass = new Text(this, SWT.BORDER);
		text_pass.setBounds(140, 158, 149, 23);

		tipsLabel = new Label(this, SWT.NONE);
		tipsLabel.setBounds(22, 208, 156, 20);

		Button button_save = new Button(this, SWT.NONE);
		button_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean b = true;
				if (!Utils.isIP(text_ip.getText())) {
					text_ip.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					b = false;
				}
				if (!text_port.getText().matches("\\d+")) {
					text_port.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					b = false;
				}
				if (!text_user.getText().matches("\\w+")) {
					text_user.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					b = false;
				}
				if (!text_pass.getText().matches("\\w+")) {
					text_pass.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					b = false;
				}

				if (b) {
					// 保存数据库配置
					saveProperties();
					// 检查数据库连接
					try {
						DbManager db = DbManager.getInstance();
						db.init();
						if (!db.isReady()) {
							LabelShow.show(tipsLabel, "数据库连接失败！", SWT.COLOR_RED);
						} else {
							close();
							MainView.getInstance().setStatusString("数据库配置保存成功");
						}
					} catch (Exception e1) {
						LabelShow.show(tipsLabel, "数据库连接失败:\n" + e1.getMessage(), SWT.COLOR_RED);
					}
				}
			}
		});
		button_save.setText("保存");
		button_save.setBounds(210, 201, 80, 27);

		Label label = new Label(this, SWT.NONE);
		label.setBounds(22, 23, 64, 19);
		label.setText("数据库");

		combo = new Combo(this, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo.getText().equals(""))
					return;
				if (!combo.getText().equals(MainView.getProp().getProperty("C_DB_TYPE"))) {
					if (combo.getText().equals(DbType.MicrosoftSQL.toString())) {
						text_ip.setText("10.3.8.44");
						text_port.setText("1433");
						text_user.setText("sa");
						text_pass.setText("pass");
					} else if (combo.getText().equals(DbType.Mysql.toString())) {
						text_ip.setText("127.0.0.1");
						text_port.setText("3306");
						text_user.setText("root");
						text_pass.setText("fdw860327");
					} else if (combo.getText().equals(DbType.Oracle.toString())) {
						text_ip.setText("127.0.0.1");
						text_port.setText("1521");
						text_user.setText("scott");
						text_pass.setText("tiger");
					}
				} else {
					text_ip.setText(MainView.getProp().getProperty("C_DB_IPADDR", ""));
					text_port.setText(MainView.getProp().getProperty("C_DB_PORT", ""));
					text_user.setText(MainView.getProp().getProperty("C_DB_USER", ""));
					text_pass.setText(MainView.getProp().getProperty("C_DB_PASS", ""));
				}
			}
		});
		combo.setItems(Utils.EnumToArray(DbType.class));
		combo.setBounds(141, 15, 147, 27);
		combo.select(0);

		// 初始化
		initData();
	}

	private void initData() {
		text_ip.setText("10.3.8.44");
		text_port.setText("1433");
		text_user.setText("sa");
		text_pass.setText("pass");
	}

	public void refresh() {
		combo.setText(MainView.getProp().getProperty("C_DB_TYPE", "MicrosoftSQL"));
		text_ip.setText(MainView.getProp().getProperty("C_DB_IPADDR", ""));
		text_port.setText(MainView.getProp().getProperty("C_DB_PORT", ""));
		text_user.setText(MainView.getProp().getProperty("C_DB_USER", ""));
		text_pass.setText(MainView.getProp().getProperty("C_DB_PASS", ""));

	}

	public void saveProperties() {
		try {
			String file = System.getProperty("user.dir") + File.separator + "config.xml";
			MainView.getProp().setProperty("C_DB_TYPE", combo.getText());
			MainView.getProp().setProperty("C_DB_IPADDR", text_ip.getText());
			MainView.getProp().setProperty("C_DB_PORT", text_port.getText());
			MainView.getProp().setProperty("C_DB_USER", text_user.getText());
			MainView.getProp().setProperty("C_DB_PASS", text_pass.getText());
			MainView.getProp().storeToXML(new FileOutputStream(file),
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		} catch (Exception e) {
			LabelShow.show(tipsLabel, "配置保存失败", SWT.COLOR_RED);
		}
	}

	private void close() {
		this.getParent().dispose();
	}
}
