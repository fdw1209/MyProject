package com.ch.sockettest.until;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sockettest.popview.CompNICConfView;

/**
 * 显示指定控件的文字和颜色
 * 
 * @author yuanji
 * 
 */
public class LabelShow {
	public static void show(final Control control, final String text,
			final int swt_color) {
		CompNICConfView.lastStatusSetTime = System.currentTimeMillis();

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (control.isDisposed())
					return;
				if (control instanceof Label) {
					((Label) control).setText(text);
				} else if (control instanceof Text) {
					((Text) control).setText(text);
				}
				if (swt_color != SWT.NONE) {
					control.setForeground(SWTResourceManager
							.getColor(swt_color));
				}
				control.setData("lastshowtime", new Date().getTime());
			}
		});
		Thread timer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
				} catch (InterruptedException e) {
				}
				if (!control.isDisposed()) {
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (control.isDisposed())
								return;
							long lastshowtime = (Long) control
									.getData("lastshowtime");
							if (new Date().getTime() - lastshowtime < 3000)
								return;
							clearControl(control);
						}

					});
				}
			}
		});
		timer.start();

	}

	private static void clearControl(final Control control) {
		if (control instanceof Label) {
			((Label) control).setText("");
			control.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		} else if (control instanceof Text) {
			((Text) control).setText("");
			control.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}
		control.setVisible(false);
	}
}
