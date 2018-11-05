package com.ch.sockettest.popview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.until.LabelShow;

/**
 * 状态信息管理
 * 
 * @author fdw
 *
 */
public class CompStatus extends Composite {
	final Text txtShow;
	Label dbstatus;

	public CompStatus(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData grddata = new GridData(GridData.FILL_HORIZONTAL);
		grddata.heightHint = 38;
		setLayoutData(grddata);
		setLayout(new GridLayout(3, false));
		txtShow = new Text(this, SWT.READ_ONLY);
		txtShow.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		txtShow.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.BOLD));
		txtShow.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtShow.setText("");

		Label lbl_sep = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		lbl_sep.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_sp2 = new GridData(GridData.FILL_VERTICAL);
		gd_sp2.widthHint = 5;
		lbl_sep.setLayoutData(gd_sp2);
		Composite systime = new Systime(this, SWT.NONE);
		systime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		GridData gd_systime = new GridData(GridData.FILL_VERTICAL);
		gd_systime.widthHint = 313;
		systime.setLayoutData(gd_systime);
	}

	public void setStatus(final String msg, final int color, boolean stay) {
		if (stay) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (txtShow.isDisposed())
						return;
					txtShow.setText(msg);
					txtShow.setForeground(SWTResourceManager.getColor(color));

				}
			});

		} else {
			LabelShow.show(txtShow, "" + msg, color);
		}
	}

	public void clearStatus() {
		txtShow.setText("");
	}

}
