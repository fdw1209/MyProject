package com.ch.sockettest.popview;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractComposite extends Composite {

	public AbstractComposite(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void freshData();
	
	public abstract void initData();

}
