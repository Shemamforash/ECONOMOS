package Merchant;

import java.awt.BorderLayout;

import GUI.GUIElements;
import MerchantResources.MarketResource;
import MerchantResources.MerchantResource;

public class CenterDetailPanel extends DetailPanel{	
	private GUIElements.MyTextArea descriptionTextArea;
	
	public CenterDetailPanel(){
		setLayout(new BorderLayout());
		descriptionTextArea = new GUIElements.MyTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setEditable(false);
		add(descriptionTextArea, BorderLayout.CENTER);
	}

	public void selectedResourceChanged(MarketResource m) {
		if(m != null){
			descriptionTextArea.setText(m.getDescription());
		} else {
			descriptionTextArea.setText("");
		}
	}

	public void selectedGuildChanged(String g) {
		// TODO Auto-generated method stub
		
	}

	public void receiveUpdate() {
		
	}

}
