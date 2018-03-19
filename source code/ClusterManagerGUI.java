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

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JComboBox;




public class ClusterManagerGUI extends JFrame {
	
	public  ArrayList<NodeList> list; 
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private static JTextField valueTextField;
	private static JLabel lblAddParameter;	
	public  MainWindowGUI mainframe;
	public RulesManagerGUI rulesManagerGui;
	
	
	public void updateList(ArrayList<NodeList> uL){
		list.removeAll(list);
			list.addAll(uL);
			
	}
	
	public ClusterManagerGUI(MainWindowGUI main1){
		this.mainframe = main1;
		 list = new ArrayList<NodeList>();/***************/
		 frame = new JFrame();
		 rulesManagerGui = new RulesManagerGUI(this); 
		 valueTextField = new JTextField();
		
	}
	

	/**
	 * @wbp.parser.entryPoint
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initClusterManagerGUI(){
		
		String []parametersList = {"# of user comments(day)","# of votes (per day)","User's (in cluster) creation date","Word in comments","User's DownVotes","Word in post title","Users has # of connections","Creation date of friends","Reputation of user","# of users in community","Connectivity of cluster"};
		String []rulesFilter = {"equal","more than","less than","not equal","within (#of days)"};

		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 793, 412);
		frame.setTitle("Cluster Manager");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
		DefaultListModel dlm = new DefaultListModel(); //-------------------//
//----------------------------------------------------------------------/			
		JPanel panel = new JPanel();
		panel.setBounds(568, 41, 188, 23);
		frame.getContentPane().add(panel);
		
		lblAddParameter = new JLabel("Add parameter");
		panel.add(lblAddParameter);
			
		valueTextField.setBounds(568, 155, 110, 20);
		frame.add(valueTextField);
		valueTextField.setColumns(10);
	
		JButton btnRemove = new JButton("Clear");
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRemove.setBounds(330, 130, 115, 29);
		frame.getContentPane().add(btnRemove);
		
		JPanel panelName = new JPanel();
		panelName.setBounds(54, 41, 260, 206);
		frame.getContentPane().add(panelName);
	
		JList listName = new JList(dlm);
		JScrollPane scrollPaneList = new JScrollPane(listName);
		scrollPaneList.setPreferredSize(new Dimension(250,200));
		listName.setValueIsAdjusting(false);
		
		listName.setLayoutOrientation(JList.VERTICAL);
		panelName.add(scrollPaneList);
		panelName.setPreferredSize(new Dimension(260, 218));				

		JButton btnAdd = new JButton("Add>>");
		btnAdd.setForeground(UIManager.getColor("Button.disabledForeground"));
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(330, 62, 115, 29);
		frame.getContentPane().add(btnAdd);
		
		
		JButton btnAddRule = new JButton("Add rule");
		btnAddRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnAddRule.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAddRule.setBounds(568, 221, 142, 29);
		frame.getContentPane().add(btnAddRule);
		
		
		JComboBox comboBox = new JComboBox(rulesFilter);
		comboBox.setBounds(568, 100, 180, 20);
		frame.getContentPane().add(comboBox);
		
	//-----------------------------------------------------------------------------------//	
		for(int i = 0;i<parametersList.length;i++)
			dlm.addElement(parametersList[i]);
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if((listName.getSelectedValue() != null) && (lblAddParameter.getText() == "Add parameter")){
						String s = (String) listName.getSelectedValue();
						lblAddParameter.setText(s);
						dlm.removeElement(listName.getSelectedValue());
					}	
					else
						if((listName.getSelectedValue() != null) && (lblAddParameter.getText()!= "Add parameter")){
							dlm.addElement(lblAddParameter.getText());
							String s = (String) listName.getSelectedValue();
							lblAddParameter.setText(s);
							dlm.removeElement(listName.getSelectedValue());
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(lblAddParameter.getText() != "Add parameter"){
						dlm.addElement(lblAddParameter.getText());
						lblAddParameter.setText("Add parameter");	
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		
		JButton btnRuleManager = new JButton("Rules Manager");
		btnRuleManager.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRuleManager.setBounds(568, 300, 142, 29);
		frame.getContentPane().add(btnRuleManager);
	
		btnRuleManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				rulesManagerGui.initRulesManagerGUI();
				rulesManagerGui.showList(list);
			
			}
		});	
		
		btnAddRule.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event){
						try{
							String value = valueTextField.getText();	
								
							String parameter = lblAddParameter.getText();					
							String filter =(String) comboBox.getSelectedItem();
										
							if((parameter.equals("Add parameter"))||(value.equals("")))
								JOptionPane.showMessageDialog(null,"Please select parameter and value");					
							else {
								NodeList rule = new NodeList(parameter,filter,value);							
								list.add(rule);
								valueTextField.setText("");/*********************/
										
								}					
								
						}catch(Exception ex){
							JOptionPane.showMessageDialog(null, ex.getMessage());
							ex.printStackTrace();
						}
					}
				});
		
		
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBounds(54, 300, 142, 29);
		frame.getContentPane().add(btnBack);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				if(!(lblAddParameter.getText().equals("Add parameter"))){
					dlm.addElement(lblAddParameter.getText());				
					lblAddParameter.setText("Add parameter");
					
				}
					
				mainframe.initMainWindowGUI();//===========
				mainframe.updateList(list);//---------------
					frame.setVisible(false);
					frame.dispose();	
			}
		});
		
		
		JLabel lblParameter = new JLabel("Parameter:");
		lblParameter.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblParameter.setBounds(471, 41, 82, 29);
		frame.getContentPane().add(lblParameter);
		
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblValue.setBounds(471, 158, 46, 14);
		frame.getContentPane().add(lblValue);
		
		
		
		JLabel lblFilter = new JLabel("Filter:");
		lblFilter.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFilter.setBounds(471, 103, 46, 14);
		frame.getContentPane().add(lblFilter);
		
		lblAddParameter.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		
		
			
	}	
}