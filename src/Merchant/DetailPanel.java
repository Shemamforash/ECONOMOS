package Merchant;

import javax.swing.SpringLayout;

import GUI.GUIElements;
import economos.SelectedResourceListener;
import economos.UpdateCaller;
import economos.UpdateListener;

public abstract class DetailPanel extends GUIElements.MyPanel implements SelectedResourceListener, UpdateListener {
	protected SpringLayout springLayout;
	public DetailPanel(){
		super(true);
		springLayout = new SpringLayout();
		setLayout(springLayout);
		UpdateCaller.addListener(this);
	}
}
