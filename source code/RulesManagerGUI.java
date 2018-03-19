import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;


	public class RulesManagerGUI  extends JFrame{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		 JFrame frame;
		
		public  ArrayList<NodeList> list; 
		private ClusterManagerGUI clusterManagerGui;
		
//Constructor of RulesManagerGUI		
		public RulesManagerGUI (ClusterManagerGUI clusterManagerGui){
			frame = new JFrame();
			list = new ArrayList<NodeList>();
			this.clusterManagerGui = clusterManagerGui;	
		}

		
		@SuppressWarnings("rawtypes")
		private static DefaultListModel dlm = new DefaultListModel(); 	
		
		/*** @wbp.parser.entryPoint*********/
		
// This method created to show all rules/filters on the screen	
		@SuppressWarnings("unchecked")
		public  void showList(ArrayList<NodeList> l){
			list.addAll(l);
			for(NodeList temp: list)
				dlm.addElement(temp);
		}
	
		
		/**
		 * @wbp.parser.entryPoint
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		
//Initialization of the variables
		public void initRulesManagerGUI(){
			
			frame.getContentPane().setBackground(Color.LIGHT_GRAY);
			frame.setBounds(100, 100, 440, 412);
			frame.setTitle("Rules Manager");
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);		
			
			JButton btnDelete = new JButton("Delete");
			btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnDelete.setBounds(250, 300, 115, 29);
			frame.getContentPane().add(btnDelete);
			
			JPanel panelName = new JPanel();
			panelName.setBounds(54, 41, 311, 206);
			frame.getContentPane().add(panelName);
		
			JList listName = new JList(dlm);
			JScrollPane scrollPaneList = new JScrollPane(listName);
			scrollPaneList.setPreferredSize(new Dimension(300,200));
			listName.setValueIsAdjusting(false);
			
			listName.setLayoutOrientation(JList.VERTICAL);
			panelName.add(scrollPaneList);
			panelName.setPreferredSize(new Dimension(300, 50));				
		
//Action Listener of "delete" button
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(listName.getSelectedValue() != null){
							list.remove(listName.getSelectedValue());						
							System.out.println(list);
							dlm.removeElement(listName.getSelectedValue());
							clusterManagerGui.updateList(list);
						}
					}catch(Exception ex){
						JOptionPane.showMessageDialog(null, ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
			
			JButton btnBack = new JButton("Back");
			btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnBack.setBounds(54, 300, 115, 29);
			frame.getContentPane().add(btnBack);
			
//Action Listener of "Back" button
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event){
					dlm.removeAllElements();
					list.removeAll(list);
					frame.setVisible(false);
					frame.dispose();
				}
			});		
			
	}
}

