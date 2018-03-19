import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

class Filter{
	String ruleFilter;
	String ruleLabel;
}

public class SybilDetectorGUI {
	JFrame frame;
	Connection connection=null;
	boolean connectedToDB=false;
	String []parametersList = {"# of user comments(day)","# of votes (per day)","Users has # of connections","User's (in cluster) creation date","# of votes (per day)","Word in comments","User's DownVotes","Word in post title","Creation date of friends","Reputation of user","# of users in community","Connectivity of cluster"};
	String []rulesFilter = {"equal","more than","less than","not equal","within (#of days)"};
	public  ArrayList<NodeList> rulesList;
	public ArrayList<Filter> filters = new ArrayList<Filter>();
	private MainWindowGUI mainWindow;
	public PreparedStatement ps;
	String[][] sybil=new String[11012][3];
	public ArrayList<CreationDate> commentsDateList;
	public SybilDetectorGUI(MainWindowGUI mainGui){
		frame = new JFrame();
		
		for(int i=1;i<sybil.length;i++){
			sybil[i][0]="no";
			sybil[i][1]="0";
			sybil[i][2]="0";
		}
		rulesList = new ArrayList<NodeList>(); //Created new ArrayList object
		filters = new ArrayList<Filter>();
		this.mainWindow = mainGui;
		buildFilters(); // call to buildFilers function
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	
private static DefaultListModel dlm = new DefaultListModel(); 	
private static DefaultListModel dlm2 = new DefaultListModel();

//Connection with the database	
public void initConnection(){
	try{
		Class.forName("com.mysql.jdbc.Driver");
		connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/academia","root","2015");
		System.out.println("Connection  to DB Established Succcesfully...");
		connectedToDB=true;
		}
	catch(Exception e)
		{
		}
}

//This method created to show all rules/filters on the screen
	public  void showList(ArrayList<NodeList> l){
		rulesList.addAll(l);
		for(NodeList temp: rulesList)
			dlm.addElement(temp);
	}
//buildFilters function realization		
	public void buildFilters(){
		for(int i=0;i<rulesFilter.length;i++){
		Filter temp=new Filter();
		String label=findLabel(rulesFilter[i]);
		temp.ruleLabel=label;
		temp.ruleFilter=rulesFilter[i];
		filters.add(temp);
		}
	}
	
//Checking filters' value 	
	public String findLabel(String filter){
		if(filter.equals("equal"))return "=";
		if(filter.equals("more than"))return ">";
		if(filter.equals("less than"))return "<";
		if(filter.equals("not equal"))return "!=";
		if(filter.equals("within (#of days)"))return "between";
		return " ";
	}
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	
//Initialization of parameters and elements 	
		public void initSybilDetectorGUI(){

			frame.getContentPane().setBackground(Color.LIGHT_GRAY);
			frame.setBounds(100, 100, 793, 412);
			frame.setTitle("Sybil Detector");
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
	
		
		JPanel panelName = new JPanel();
		panelName.setBounds(54, 41, 340, 206);
		frame.getContentPane().add(panelName);
	
		JList listName = new JList(dlm);
		JScrollPane scrollPaneList = new JScrollPane(listName);
		scrollPaneList.setPreferredSize(new Dimension(330,200));
		listName.setValueIsAdjusting(false);
		
		listName.setLayoutOrientation(JList.VERTICAL);
		panelName.add(scrollPaneList);
		panelName.setPreferredSize(new Dimension(320, 50));
	
		JPanel panelChosen = new JPanel();
		panelChosen.setBounds(440, 41, 300, 206);
		frame.getContentPane().add(panelChosen);
		
		JList listChosen = new JList(dlm2);
		JScrollPane scrollPaneList2 = new JScrollPane(listChosen);
		scrollPaneList2.setPreferredSize(new Dimension(295,200));
		listChosen.setValueIsAdjusting(false);
		
		listChosen.setLayoutOrientation(JList.VERTICAL);
		panelChosen.add(scrollPaneList2);
		panelChosen.setPreferredSize(new Dimension(300, 50));
				
		JButton btnClearResults = new JButton("Clear results");
		
//Action Listener of "Clear results" button	
		btnClearResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dlm2.removeAllElements();	
				for(int i=1;i<sybil.length;i++){
					sybil[i][0]="no";
					sybil[i][1]="false";
					sybil[i][2]="0";
				}
				
			}
		});
		btnClearResults.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnClearResults.setBounds(500, 300, 160, 29);
		frame.getContentPane().add(btnClearResults);
		
		JButton btnSybelComm = new JButton("Start rule analisys");
		btnSybelComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
// if dose not connect to DB, create connection by calling to "initConnection function
				if(!connectedToDB)initConnection(); 
				
							
			if(rulesList.size()>0){	
				int rulesListSize=rulesList.size();
				for(int rulesIndex=0;rulesIndex<rulesListSize;rulesIndex++){
					NodeList rule=rulesList.get(rulesIndex);
					String ruleValue=rule.valueTextField;
					String ruleParameter=rule.lblAddParameter;
					String ruleFilter=rule.comboBox;
					findAndExecuteQuery(ruleParameter,ruleFilter,ruleValue);
					saveRuleResult(rule,rulesIndex);
					clearTempResults();
				}
				int globalCounter=0;
				for(int k=1;k<sybil.length;k++){
					if(sybil[k][0].equals("yes")||Integer.parseInt(sybil[k][2])>0){globalCounter++;
					dlm2.addElement("UserID: "+k+" detected by number of rules: "+sybil[k][2]);
				}}
				dlm2.addElement("Total number of sybils detected: "+globalCounter);
				
			}else JOptionPane.showMessageDialog(frame,"No Rules.Please create rules!");	
			
			}
			public void clearTempResults(){
				for(int i=1;i<sybil.length;i++){
					sybil[i][0]="no";
					
				}
			}
			public String getUserDetails(int userID){
				String details="";
				try {
					ps=connection.prepareStatement("SELECT User_ID,DisplayName,CreationDate,Location,AccountId FROM academia.users where User_ID="+userID);
						ResultSet rst1 = ps.executeQuery();
						while(rst1.next()){
							details="User_ID: "+rst1.getString(1)+", DisplayName: "+rst1.getString(2)+", CreationDate: "+rst1.getString(3)+", Location: "+rst1.getString(4)+", AccountId: "+rst1.getString(5);
						}
					
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return details;
			}
			
			public void saveRuleResult(NodeList rule,int ruleIndex) {
				try {
					Date date=new Date();
					BufferedWriter bfw2= null;
					String ruleName=rule.lblAddParameter+"_"+rule.comboBox+"_"+rule.valueTextField;
					
					bfw2 = new BufferedWriter(new FileWriter("C:\\academia.stackexchange.com\\Results\\Rule_"+ruleName+".txt"));
					bfw2.write(date.toString());
					bfw2.newLine();
					bfw2.write("Rule: "+rule.lblAddParameter+" "+rule.comboBox+" "+rule.valueTextField);
					bfw2.newLine();
									
					for(int index=1;index<sybil.length;index++){
						if(sybil[index][0].equals("yes")){
						String userDetails=getUserDetails(index);
						bfw2.write(userDetails);
						bfw2.newLine();
					}
					}						
					bfw2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				
			    }//end of while	
				
			}
		});
		
		btnSybelComm.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSybelComm.setBounds(250, 300, 170, 29);
		frame.getContentPane().add(btnSybelComm);
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBounds(54, 300, 90, 29);
		frame.getContentPane().add(btnBack);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				dlm.removeAllElements();
				rulesList.removeAll(rulesList);
				frame.setVisible(false);
				frame.dispose();
			}
		});	
									
		}

		public void findAndExecuteQuery(String param,String filter,String value){
			
			String table;
			
			if(param.equals("Reputation of user")){
				table="users";
				
				String label=findLabel(filter);
				String query="Select User_ID from "+table+" where Reputation"+label+value+";";
				System.out.println(query);
				try {
					ps=connection.prepareStatement(query);
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						int index=Integer.parseInt(rst1.getString(1));
						sybil[index][0]="yes";
						sybil[index][2]=checkRuleNumber(sybil[index][2]);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					    
				}//end of reputation case
			if(param.equals("Word in comments")){
				
				table="comments";
				String query="Select distinct(User_ID) from "+table+" where concat(Text) like '%"+value+"%';";
				try {
					ps=connection.prepareStatement(query);
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						int index=Integer.parseInt(rst1.getString(1));
						sybil[index][0]="yes";
						sybil[index][2]=checkRuleNumber(sybil[index][2]);
					}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}	//end of word in comments
			if(param.equals("Word in post title")){
				
				table="posts";
				String query="Select distinct(OwnerUserId) from "+table+" where concat(Title) like '%"+value+"%';";
				try {
					ps=connection.prepareStatement(query);
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						int index=Integer.parseInt(rst1.getString(1));
						sybil[index][0]="yes";
						sybil[index][2]=checkRuleNumber(sybil[index][2]);
					}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//end of word in post title
			if(param.equals("User's DownVotes")){
				String label=findLabel(filter);
				table="users";
				String query="Select distinct(User_ID) from "+table+" where DownVotes"+label+value+";";
				
				try {
					ps=connection.prepareStatement(query);
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						int index=Integer.parseInt(rst1.getString(1));
						sybil[index][0]="yes";
						sybil[index][2]=checkRuleNumber(sybil[index][2]);
					}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//end of word in post title
			
			if(param.equals("# of user comments(day)")){
				String label=findLabel(filter);
				int result;
				ArrayList<CreationDate> commentsDateList = new ArrayList<CreationDate>();
				String query="Select CreationDate,User_ID from comments;";
				System.out.println(query);
				try {
					ps=connection.prepareStatement(query);
					ResultSet rst1 = ps.executeQuery();
					System.out.println("start query");
					int user_id;
					boolean exist=false;
					boolean flag=false;
					while(rst1.next()){
						String Fulldate=rst1.getString(1);
						String[] date = Fulldate.split(" ");
						user_id=Integer.parseInt(rst1.getString(2));
						exist=false;
						flag=false;
						for(CreationDate temp:commentsDateList){
							String tempDate=temp.date;
							if(tempDate.equals(date[0])){//date exist
								if(temp.getNodeListSize()>0){
									for(Node node:temp.usersSameDateCount){
										if(node.userId==user_id){//user exist
										node.counter++;
										exist=true;
										flag=true;
										
									}
								}
							}
								if(!exist){
									temp.addNode(user_id,1);//date exist ,user not exist
									flag=true;
									
								}
							}
						}
						if(!flag){
							CreationDate newDate=new CreationDate(date[0]);
							newDate.addNode(user_id,1);
				    		commentsDateList.add(newDate);
				    		
						}
			   
						}
					int value2=Integer.parseInt(value);
					for(CreationDate date:commentsDateList){
						for(Node tempNode:date.usersSameDateCount){
							
							if(tempNode.counter>value2){sybil[tempNode.userId][0]="yes";
							if(sybil[tempNode.userId][1].equals("false")){
							sybil[tempNode.userId][2]=checkRuleNumber(sybil[tempNode.userId][2]);
							sybil[tempNode.userId][1]="true";
							}
							
						}}
						 
						 
					}
					
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//end of # of user comments(day)	
			if(param.equals("Users has # of connections")){
				try {
					ps=connection.prepareStatement("SELECT UserId FROM academia.comvector where Community_ID in (SELECT Community_ID FROM academia.comsizesvector where Com_size<2);");
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						int index2=Integer.parseInt(rst1.getString(1));
						sybil[index2][0]="yes";
						sybil[index2][2]=checkRuleNumber(sybil[index2][2]);
						
			}}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(param.equals("User's (in cluster) creation date")){
				try {
					ps=connection.prepareStatement("select * from comsizesvector;");
					ResultSet rst1 = ps.executeQuery();
					while(rst1.next()){
						String community=rst1.getString(1);
						String comSize=rst1.getString(2);
						checkUsersDateCreation(community,comSize,value);
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
				
			}
		
			
		}//end of findAndExecuteQuery method
		public void checkUsersDateCreation(String com,String comSize,String period){
			try{
				ps=connection.prepareStatement("Select CreationDate,User_ID from users where User_ID in(select UserId from comvector where Community_ID="+com+");");
				ResultSet rst2=ps.executeQuery();
				boolean exist=false;
				String tempDate="";
				ArrayList<CreationDate> usersInCommunity = new ArrayList<CreationDate>();
				while(rst2.next()){
						String Fulldate=rst2.getString(1);
						String[] date = Fulldate.split(" ");
						tempDate=date[0];
						for(CreationDate tempdate:usersInCommunity){
							if(tempdate.date.equals(date[0])){
								tempdate.counter++;
								tempdate.addNode(Integer.parseInt(rst2.getString(2)), 1);
								exist=true;
							}
						}
					if(!exist){CreationDate newDate= new CreationDate(tempDate);
					newDate.addNode(Integer.parseInt(rst2.getString(2)), 1);
					usersInCommunity.add(newDate);						
				}	
					exist=false;
				 
				}
								 
				if(usersInCommunity.size()>0&&usersInCommunity.size()<=Integer.parseInt(period)){
					
					for(CreationDate newDate2:usersInCommunity){
					if(newDate2.getNodeListSize()>10){	
					ps=connection.prepareStatement("select UserId from comvector where Community_ID="+com+";");
					ResultSet rst3=ps.executeQuery();
					while(rst3.next()){
					int userID= rst3.getInt(1);
					sybil[userID][0]="yes";
					sybil[userID][2]=checkRuleNumber(sybil[userID][2]);
					
					}
										
				}
				}
				}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		public String checkRuleNumber(String index){
			int number=Integer.parseInt(index);
			number++;
			String result=Integer.toString(number);
			return result;
		}
}