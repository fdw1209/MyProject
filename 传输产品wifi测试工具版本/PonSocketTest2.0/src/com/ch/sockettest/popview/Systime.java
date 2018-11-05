package com.ch.sockettest.popview;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Systime extends Composite {
	public Systime(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		Label lblSystime = new Label(this, SWT.NONE);
		lblSystime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_lbl = new GridData(GridData.FILL_VERTICAL);
		gd_lbl.widthHint = 77;
		lblSystime.setText("\u7CFB\u7EDF\u65F6\u95F4:");
		lblSystime.setLayoutData(gd_lbl);

		final Text txtSystime = new Text(this, SWT.READ_ONLY);
		txtSystime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		// txtSystime.setText(new SimpleDateFormat("yyyy 年 M 月 d 日   HH:mm:ss")
		// .format(System.currentTimeMillis()));
		GridData gd_txtSystime = new GridData(GridData.FILL_BOTH);
		gd_txtSystime.widthHint = 384;
		txtSystime.setLayoutData(gd_txtSystime);
		new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					try {
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								if (txtSystime.isDisposed())
									return;
								txtSystime.setText(new SimpleDateFormat(
										"yyyy 年 M 月 d 日   HH:mm:ss")
										.format(System.currentTimeMillis()));
								txtSystime.getParent().layout();
							}

						});
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				} while (!txtSystime.isDisposed());

			}
		}).start();
	}

}
