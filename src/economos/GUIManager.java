package economos;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;

public class GUIManager {
	
	/*
	public static JPanel newItemInfoGUI(){
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new BorderLayout());
		
		JPanel itemInfo = ResourceGUI.getResourcePanel();
		itemPanel.add(itemInfo, BorderLayout.CENTER);
		
		graph = new GraphPanel();
		graph.setPreferredSize(new Dimension(itemPanel.getWidth(), 200));
		graph.repaint();
		itemPanel.add(graph, BorderLayout.SOUTH);
		
		return itemPanel;
	}*/
	
	
	
	/*
	public static JPanel newItemListGUI(){
		JPanel itemListPanel = new JPanel();
		itemListPanel.setLayout(new BorderLayout());
		
		updateComboBox(EconomosMain.getCurrentUser());
		itemCategory.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateJList(EconomosMain.getCurrentUser(), (String)itemCategory.getSelectedItem());
			}
		});
		itemCategory.setSelectedIndex(0);
		updateJList(EconomosMain.getCurrentUser(), (String)itemCategory.getSelectedItem());
		
		resourceJList.setSize(new Dimension(200, 30));
		resourceJList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting()){
					setSelectedResource((String)itemCategory.getSelectedItem(), resourceJList.getSelectedValue());
					repaintGraph();
				}
			}
		});
		
		itemListPanel.add(itemCategory, BorderLayout.NORTH);
		itemListPanel.add(resourceJList, BorderLayout.CENTER);
		
		return itemListPanel;
	}*/
}
