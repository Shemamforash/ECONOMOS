package economos;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;

public class GUIManager {
	private static final JList<String> resourceJList = new JList<String>(new String[]{"Empty"});
	private static final JComboBox<String> itemCategory = new JComboBox<String>(new String[]{""});
	private static UserResource selectedResource = null;
	private static GraphPanel graph = null;
	
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
	
	public static void repaintGraph(){
		if(graph != null){
			graph.repaint();
		}
	}
	
	public static int getGraphWidth(){
		if(graph != null){
			return graph.getWidth();
		}
		return 0;
	}
	
	public static void updateComboBox(User user){
		itemCategory.removeAllItems();
		ArrayList<ResourceType<UserResource>> arr = new ArrayList<ResourceType<UserResource>>(user.getResourceMap().getResourceTypes().values());
		for(int i = 0; i < arr.size(); ++i){
			itemCategory.addItem(arr.get(i).getType());
		}
	}
	
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
	
	public static void setSelectedResource(String type, String name){
		ResourceMap<UserResource> m = EconomosMain.getCurrentUser().getResourceMap();
		ResourceType<UserResource> t = m.getResourceTypes().get(type);
		selectedResource = (UserResource)t.getResourceOfType().get(name);
		repaintGraph();
	}
	
	public static UserResource getSelectedResource(){
		return selectedResource;
	}
	
	public static void updateJList(User user, String type, JList list){				
		if(EconomosMain.getCurrentUser().getResourceMap().getResourceTypes().containsKey(type)){
			ArrayList<UserResource> arr = new ArrayList<UserResource>(user.getResourceMap().getResourceTypes().get(type).getResourceOfType().values());
			String[] strarr = new String[arr.size()];
			for(int i = 0; i < strarr.length; ++i){
				strarr[i] = ((Resource)arr.get(i)).getName();
			}
			list.setListData(strarr);
		}
	}
}
