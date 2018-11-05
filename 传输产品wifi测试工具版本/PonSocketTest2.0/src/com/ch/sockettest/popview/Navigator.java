package com.ch.sockettest.popview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
/**
 * 导航菜单composite组件
 * @author fdw
 *
 */
public class Navigator extends Composite {
	List<Button> lscomp = new ArrayList<Button>();
	Map<Integer, Object> mpComp_Control = new HashMap<Integer, Object>();
	Map<Integer, Object> mpBtns = new HashMap<Integer, Object>();
	Group nav;
	Composite controls;
	StackLayout stacklayout = new StackLayout();

	public Navigator(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		nav = new Group(this, SWT.NONE);
		nav.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		nav.setText("导航菜单");
		GridData gd_nav = new GridData(GridData.FILL_VERTICAL);
		gd_nav.widthHint = 159;
		nav.setLayoutData(gd_nav);
		nav.setLayout(new GridLayout(1, true));

		controls = new Composite(this, SWT.BORDER);
		controls.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		controls.setLayoutData(new GridData(GridData.FILL_BOTH));
		controls.setLayout(stacklayout);

	}
	/**
	 * 添加菜单项
	 * @param name
	 * @param control
	 */
	public void addItem(final String name, final AbstractComposite control) {
		final Button btn = new Button(nav, SWT.NONE);
		btn.setText(name);
		btn.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		btn.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.BOLD));
		GridData gd_Button = new GridData(GridData.FILL_HORIZONTAL);
		gd_Button.heightHint = 80;
		btn.setLayoutData(gd_Button);
		lscomp.add(btn);
		mpComp_Control.put(btn.handle, control);
		mpBtns.put(btn.handle, btn);
		if (stacklayout.topControl == null) {
			stacklayout.topControl = control;
		}
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stacklayout.topControl = (AbstractComposite) mpComp_Control
						.get(btn.handle);
				clearBtnColor();
				btn.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
				((AbstractComposite) mpComp_Control.get(btn.handle))
						.freshData();
				controls.layout();
			}
		});
	}

	private void clearBtnColor() {
		Iterator<Object> it = mpBtns.values().iterator();
		while (it.hasNext()) {
			Button btn = (Button) it.next();
			btn.setBackground(SWTResourceManager
					.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
	}

	public void refreshCurrentControl() {
		AbstractComposite comp = (AbstractComposite) stacklayout.topControl;
		if (comp != null) {
			comp.freshData();
			controls.layout();
		}
	}

	public Composite getStatckComposite() {
		return controls;
	}

}
