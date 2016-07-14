package GUI.Merchant;

import java.awt.BorderLayout;

import GUI.DetailPanel;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import MerchantResources.Resource;

public class CenterDetailPanel extends DetailPanel {
	private GUIElements.MyTextArea descriptionTextArea;
	
	public CenterDetailPanel(){
		super(GuildPanel.PanelType.MERCHANT);
		setLayout(new BorderLayout());
		descriptionTextArea = new GUIElements.MyTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setEditable(false);
		add(descriptionTextArea, BorderLayout.CENTER);
	}

	public void selectedResourceChanged(Resource m) {
		if(m != null){
			descriptionTextArea.setText(m.description());
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
