import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
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
import java.awt.List;
import javax.swing.UIManager;
import javax.swing.JLabel;


//Creating Graph manager window
public class GraphManagerGUI {
	public String UserCount;
	public String EdgesCount;
	private JLabel labelConnected;
	private JLabel labelEdges;
	private JLabel labelCreated;
	static JFrame frame = new JFrame();
	private JLabel labelSingle;
	ArrayList<String> newList = new ArrayList<String>();
	ArrayList<User> UsersList = new ArrayList<User>();
	private static DefaultListModel dlm = new DefaultListModel(); 
	@SuppressWarnings("rawtypes")
	private static DefaultListModel dlm2 = new DefaultListModel(); 
	PreparedStatement ps=null;
	ResultSet rs=null;
	int friend=0;
	boolean reputationFlag=false;
	ArrayList<Reputation> repList;
	ArrayList<CreationDate> dateList;
	ArrayList<CreationDate> LocationList;
	boolean weightsList=false;
	MainWindowGUI mainframe;
	boolean graphExists=false;
	boolean connectedToDB=false;
	String filePath=null;
	Connection connection=null;
	Connection connection2=null;
	
	public GraphManagerGUI (MainWindowGUI main){
			mainframe=main;
		
	}
//Initialization		
	public void initGraphManagerGUI(ArrayList<String> fields){
		newList.addAll(fields);
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 793, 445);
		frame.setTitle("Graph Manager");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
			
		JButton btnRemove = new JButton("<<Remove");
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRemove.setBounds(333, 97, 115, 29);
		frame.getContentPane().add(btnRemove);
		
		JPanel panelName = new JPanel();
		panelName.setBounds(54, 41, 205, 206);
		frame.getContentPane().add(panelName);
	
		JList listName = new JList(dlm);
		JScrollPane scrollPaneList = new JScrollPane(listName);
		scrollPaneList.setPreferredSize(new Dimension(200,200));
		listName.setValueIsAdjusting(false);
		
		listName.setLayoutOrientation(JList.VERTICAL);
		panelName.add(scrollPaneList);
		panelName.setPreferredSize(new Dimension(300, 50));
						

		JButton btnAdd = new JButton("Add>>");
		btnAdd.setForeground(UIManager.getColor("Button.disabledForeground"));
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(333, 52, 115, 29);
		frame.getContentPane().add(btnAdd);
		
		JPanel panelChosen = new JPanel();
		panelChosen.setBounds(517, 41, 205, 206);
		frame.getContentPane().add(panelChosen);
		
		JList listChosen = new JList(dlm2);
		JScrollPane scrollPaneList2 = new JScrollPane(listChosen);
		scrollPaneList2.setPreferredSize(new Dimension(200,200));
		listChosen.setValueIsAdjusting(false);
		
		listChosen.setLayoutOrientation(JList.VERTICAL);
		panelChosen.add(scrollPaneList2);
		panelChosen.setPreferredSize(new Dimension(300, 50));
		
		for(String temp: newList)dlm.addElement(temp);//Adding elements to the window
//Action Listener to "Add" button- add elements to the list Name after selection	
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(listName.getSelectedValue() != null){
				dlm2.addElement(listName.getSelectedValue());
				dlm.removeElement(listName.getSelectedValue());
					}	
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage());
					ex.printStackTrace();
				}
			}
		});

//Action Listener to "Remove" button- remove elements from the list Name after selection		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(listChosen.getSelectedValue() != null){
				dlm.addElement(listChosen.getSelectedValue());
				dlm2.removeElement(listChosen.getSelectedValue());	
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		

		JButton btnBuildGraph = new JButton("Build graph");
		btnBuildGraph.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBuildGraph.setBounds(334, 162, 114, 46);
		frame.getContentPane().add(btnBuildGraph);
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBounds(54, 300, 142, 29);
		frame.getContentPane().add(btnBack);
		
		JLabel lblCreatedNodes = new JLabel("Created Nodes:");
		lblCreatedNodes.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCreatedNodes.setBounds(292, 300, 150, 16);
		frame.getContentPane().add(lblCreatedNodes);
		
		JLabel lblNumOfEdges = new JLabel("Num of Edges:");
		lblNumOfEdges.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNumOfEdges.setBounds(292, 320, 150, 16);
		frame.getContentPane().add(lblNumOfEdges);
		
			
		labelCreated = new JLabel("0");
		labelCreated.setBounds(400, 300, 150, 16);
		frame.getContentPane().add(labelCreated);
		
		labelEdges = new JLabel("0");
		labelEdges.setBounds(400, 320, 150, 16);
		frame.getContentPane().add(labelEdges);
		
			
		JLabel lblBuildingGraph = new JLabel("Graph info");
		lblBuildingGraph.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblBuildingGraph.setBounds(340, 256, 150, 16);
		frame.getContentPane().add(lblBuildingGraph);
		
		JPanel panel = new JPanel();
		panel.setBounds(275, 280, 232, 80);
		frame.getContentPane().add(panel);
		
		JButton saveGraphButton = new JButton("Save graph");
		saveGraphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		saveGraphButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		saveGraphButton.setBounds(576, 300, 142, 29);
		frame.getContentPane().add(saveGraphButton);
				
		btnBack.addActionListener(new btnMainWindowActionListener());
		btnBuildGraph.addActionListener(new buildGraphActionListener());
		saveGraphButton.addActionListener(new saveGraphActionListener());
		}	

//Save created table/graph on the computer
			class saveGraphActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event){	
				if(graphExists)	{
					
					int weightF;
		    		int idFriend;
		    		File weights=new File("C:\\academia.stackexchange.com\\Weights.txt");
					if(!weights.exists()) weights.delete();	
					BufferedWriter weightsFile;
					try {
						weightsFile = new BufferedWriter(new FileWriter("C:\\academia.stackexchange.com\\Weights.txt"));
						for(User user:UsersList){
							if(!user.FriendsListEmpty()){
								int numOfFriends=user.getNumOfFriends();
								for(int s=1;s<numOfFriends;s++){
										idFriend=user.getIdOfFriend(s);
										weightF=user.getWeightOfFriend(s);
																			
									try{	
										weightsFile.write(Integer.toString(user.UserId));
										weightsFile.write(" ");
										weightsFile.write(Integer.toString(idFriend));
										weightsFile.write(" ");
										weightsFile.write(Integer.toString(weightF));
										weightsFile.newLine();
										
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
									
								}}
						}
						
						weightsFile.close();	
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(frame, "Table weights saved to txt file");
					}
				}
			class btnMainWindowActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event){	
					frame.setVisible(false);
					 frame.dispose();
				}
			}
//Creation graph by selected variables			
			class buildGraphActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event){	
					if(!connectedToDB)initConnection();
					if(!graphExists){
					   try {
						ps=connection.prepareStatement("CREATE TABLE IF NOT EXISTS academia.Weights(Id int NOT NULL,User_ID int,friend_ID int,weight int,PRIMARY KEY(Id));");
						ps.executeUpdate();
						if(dlm2.contains("CreationDate")){
							int dateIndex;
							int user_id;
							dateList=new ArrayList<CreationDate>();
							ResultSet rst1 = ps.executeQuery("SELECT CreationDate,User_ID from users;");
							while(rst1.next()){
								String Fulldate=rst1.getString(1);
								String[] date = Fulldate.split(" ");
								user_id=Integer.parseInt(rst1.getString(2));
								dateIndex=CreationDateListHas(date[0],user_id);
								if(dateIndex<0){
						    		CreationDate newDate=new CreationDate(date[0]);
						    		newDate.addUser(user_id);
						    		dateList.add(newDate);
						    	}
							}
							int tempIndex;
							int userExist;
							
							int totalRows=0;
																				
							for(CreationDate date:dateList){
								if(date.getGroupSize()>0){
								for(int i=0;i<date.getGroupSize();i++){
									int tempUserId=date.getIndex(i);
									int userTempIndex=ListHasUser(tempUserId);
									if(userTempIndex>0){//connect with other users from community
										User tempUser=UsersList.get(userTempIndex);
										for(int innerIndex=i+1;innerIndex<date.getGroupSize();innerIndex++){
											int idOfFriend=date.getIndex(innerIndex);
											int indexOfFriend=tempUser.userHasFriend(idOfFriend);
											if(indexOfFriend>0){//user exists friend exists
							    			tempUser.addWeightToFriend(indexOfFriend,1);
							    			}
											else { //user exists, friend not exists
							    			idOfFriend=date.getIndex(innerIndex);
							    			tempUser.addFriend(idOfFriend,1);
							    				
							    				}	
											}
									}
									else {//user doesn't exist
							    		User temp2=new User(tempUserId);//create new user 
							    		for(int innerIndex2=i;innerIndex2<date.getGroupSize();innerIndex2++){
							    			int idOfFriend=date.getIndex(innerIndex2);
							    			temp2.addFriend(idOfFriend,1);
							    			
							    			}
							    		UsersList.add(temp2);
							    	}
									
								}
							
						}
						}
							
							
							weightsList=true;
						    graphExists=true;	
						}//end of creation date
						if(dlm2.contains("Vote_ID")){
							
							ps=connection.prepareStatement("create or replace view PostOwnerVoteTypeView as SELECT p.OwnerUserId AS postownerid,v.VoteTypeId FROM votes v INNER JOIN posts p ON v.Post_ID = p.Post_ID;");
							ps.executeUpdate();
							ResultSet rst1 = ps.executeQuery("SELECT * from PostOwnerVoteTypeView;");
							int i=0;		
							int index;
							int VoteType;
																		
							while(rst1.next()){
					    	index=Integer.parseInt(rst1.getString(1));
					    	VoteType=Integer.parseInt(rst1.getString(2));
					    	int userIndex;
					    	if((userIndex=ListHasUser(index))>0){// 
					    		User tempUser=UsersList.get(userIndex);
					    		if(VoteType==2)tempUser.updateUserRate(1);
					    		if(VoteType==1){
					    			if(tempUser.rate>1)tempUser.updateUserRate(-1);
					    		}
					    		
					    	}
					    	else {//user doesn't exists
					    		User temp=new User(index);
					    		if(VoteType==2)temp.updateUserRate(1);
					    		if(VoteType==1){
					    			if(temp.rate>1) temp.updateUserRate(-1);
					    		}
					    		UsersList.add(temp);
					    	}
							}	
							weightsList=true;
						    graphExists=true;	
						}//end of Vote_ID
					   					
						if(dlm2.contains("Comment_ID")){
							
						    ps=connection.prepareStatement("create or replace view PostComments as SELECT p.OwnerUserId,c.User_ID FROM posts p INNER JOIN comments c ON p.Post_ID = c.Post_ID;");
						    ps.executeUpdate();
												
						    ResultSet rst2 = ps.executeQuery("SELECT * FROM PostComments where OwnerUserId<>User_ID;");   
						    int j=0;
						   
						    int UserId;	
						    int edgeWeight2=1;
						    int FriendID2;
						  
						    while(rst2.next()){
						   	UserId=Integer.parseInt(rst2.getString(1));
							FriendID2=Integer.parseInt(rst2.getString(2));
							int userIndex2;
					    	int location2;
					    	if((userIndex2=ListHasUser(UserId))>0){//user exists
					    		User tempUser=UsersList.get(userIndex2);//get user from list
					    		if((location2=tempUser.userHasFriend(FriendID2))>0){//user exists friend exists
					    			tempUser.addWeightToFriend(location2,edgeWeight2);//add weight to friend
					    			}
					    		else { //user exists, friend not exists
					    			tempUser.addFriend(FriendID2,edgeWeight2);
					    				
					    			}
					    		
					    		
					    	}
					    	else {//user doesn't exist
					    		User temp=new User(UserId);//add new user
					    		temp.addFriend(FriendID2,edgeWeight2);//add new friend
					    		UsersList.add(temp);//add user to list
					    	}
					    	
						    }//end of while
						    weightsList=true;
						    graphExists=true;
						}//end of comments case
						
						if(dlm2.contains("Reputation")){
							repList=new ArrayList<Reputation>();
							int user_id;
							int reputation;
							int repIndex3;
							
							ResultSet rst4 = ps.executeQuery("SELECT Reputation,User_ID FROM academia.users;");
							while(rst4.next()){
								reputation=Integer.parseInt(rst4.getString(1));
								user_id=Integer.parseInt(rst4.getString(2));
								repIndex3=reputationListHas(reputation,user_id);
								if(repIndex3<0){//not exist 
						    		Reputation temp5=new Reputation(reputation);//new reputation group
						    		temp5.addUser(user_id);//add user 
						    		repList.add(temp5);
						    	}
								
							}
							int tempIndex;
							int temp;
							int total=0;
							int UserHasFriend;
							int IdOfFriend;
							// add weights to users with the same reputation
							for(Reputation rep:repList){
								for(int userIndex=0,friendIndex=0;userIndex<rep.getGroupSize();userIndex++){
									tempIndex=rep.getIndex(userIndex);
									temp=ListHasUser(tempIndex);//check if user exist
									if(temp>0){ //true if exist
										User tempUser=UsersList.get(temp);//get user from list
										friendIndex=userIndex;
										friendIndex++;
										/////start
										for(int innerIndex=friendIndex;innerIndex<rep.getGroupSize();innerIndex++){
											IdOfFriend=rep.getIndex(innerIndex);
											friendIndex=tempUser.userHasFriend(IdOfFriend);
											if(friendIndex>0){//user exists friend exists
								    			tempUser.addWeightToFriend(friendIndex,1);//add weight to friend
								    			total++;
								    			}
								    		else { //user exists, friend not exists
								    			IdOfFriend=rep.getIndex(friendIndex);
								    			tempUser.addFriend(IdOfFriend,1);
								    			total++;
								    			}	
											}
										//end
										
										
							    		
									}
									else {//user doesn't exist
										User newUser=new User(tempIndex);
										for(int innerIndex2=userIndex;innerIndex2<rep.getGroupSize();innerIndex2++){
							    			int idOfFriend=rep.getIndex(innerIndex2);
							    			newUser.addFriend(idOfFriend,1);
							    			total++;
							    			
							    			}
							    		 		
							    		UsersList.add(newUser);
							    	}
									
								}
							}
							//reputationFlag=true;
							
							
							 weightsList=true;
							  graphExists=true;	
								
						}
						if(dlm2.contains("Location")){
							int dateIndex;
							int user_id;
							LocationList=new ArrayList<CreationDate>();
							ResultSet rst1 = ps.executeQuery("SELECT Location,User_ID from users where Location!='Null';");
							while(rst1.next()){
								String FullLocation=rst1.getString(1);
								String[] location = FullLocation.split(",");
								user_id=Integer.parseInt(rst1.getString(2));
								dateIndex=CreationDateListHas2(location[0],user_id);
								if(dateIndex<0){
						    		CreationDate newDate=new CreationDate(location[0]);
						    		newDate.addUser(user_id);
						    		LocationList.add(newDate);
						    	}
							}
							int tempIndex;
							int userExist;
							
							int totalRows=0;
							// add weights 													
							for(CreationDate location:LocationList){
								if(location.getGroupSize()>0){
								for(int i=0;i<location.getGroupSize();i++){
									int tempUserId=location.getIndex(i);
									int userTempIndex=ListHasUser(tempUserId);
									if(userTempIndex>0){//connect with other users from community
										User tempUser=UsersList.get(userTempIndex);
										for(int innerIndex=i+1;innerIndex<location.getGroupSize();innerIndex++){
											int idOfFriend=location.getIndex(innerIndex);
											int indexOfFriend=tempUser.userHasFriend(idOfFriend);
											if(indexOfFriend>0){//user exists friend exists
							    			tempUser.addWeightToFriend(indexOfFriend,1);
							    			
							    			}
											else { //user exists, friend not exists
							    			idOfFriend=location.getIndex(innerIndex);
							    			tempUser.addFriend(idOfFriend,1);
							    				
							    				}	
											}
									}
									else {//user doesn't exist
							    		User temp2=new User(tempUserId);//create new user 
							    		for(int innerIndex2=i;innerIndex2<location.getGroupSize();innerIndex2++){
							    			int idOfFriend=location.getIndex(innerIndex2);
							    			temp2.addFriend(idOfFriend,1);
							    			
							    			}
							    		UsersList.add(temp2);
							    	}
									
								}
							
						}
						}
														
							weightsList=true;
						    graphExists=true;	
						}//end of location 
						if(dlm2.contains("Friend_ID")){
							
							ResultSet rst3 = ps.executeQuery("SELECT * FROM friends;");   
						    int j=0;
						    int index3;	
						    int edgeWeight3=1;
						    int FriendID3;
						    while(rst3.next()){
							index3=Integer.parseInt(rst3.getString(2));
							FriendID3=Integer.parseInt(rst3.getString(3));
							int userIndex3;
					    	int location3;
					    	if((userIndex3=ListHasUser(index3))>0){//friend exists
					    		User tempUser=UsersList.get(userIndex3);
					    		if((location3=tempUser.userHasFriend(FriendID3))>0){//user exists friend exists
					    			tempUser.addWeightToFriend(location3,edgeWeight3);//add weight to friend
					    			}
					    		else { //user exists, friend not exists
					    			tempUser.addFriend(FriendID3,edgeWeight3);
					    				
					    			}
					    		
					    		
					    	}
					    	else {//user doesn't exists
					    		User temp=new User(index3);
					    		temp.addFriend(FriendID3,edgeWeight3);//add friend
					    		UsersList.add(temp);
					    	}
					    							   
						    weightsList=true;
						    graphExists=true;
						}}
						
						    
							
						} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					   Statement st2 = null;
					   try {
						   st2 = connection.createStatement();
					   } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   	e.printStackTrace();
					   }
					   ResultSet rst2 = null;
					   try {
						   rst2 = st2.executeQuery("SELECT count('*') FROM academia.Users;");
					   } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
					   }
					   try {
						   while(rst2.next())
						   {
							   try {
								   UserCount = rst2.getString("count('*')");
								 //*************************
									labelCreated.setText(UserCount);
									//***************************
							   } catch (SQLException e) {
								   // TODO Auto-generated catch block
								   e.printStackTrace();
							   }//+++++-------------+++++++//
							  
		 
						   }
					   } catch (SQLException e1) {
						   // TODO Auto-generated catch block
						   e1.printStackTrace();
					   }
					   try {
						   st2.close();
					   } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
					   }
					   ///////////////
					  
					
			    	if(weightsList){
			    		int result=0;
			    		
						ResultSet rst;
						try {
							rst = ps.executeQuery("SELECT count('*') FROM academia.weights;");
							while(rst.next())
					 		{
								result=Integer.parseInt(rst.getString("count('*')"));
								
					 		}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(result>0){
							
			    		int n = JOptionPane.showConfirmDialog(
	                            frame, "Table weights already exists. Overide it?",
	                            "Warning",
	                            JOptionPane.YES_NO_OPTION);
	                    if (n == JOptionPane.YES_OPTION) {
	                    	SaveWeightsToDB();
	                    	
	    				 
	                    } else if (n == JOptionPane.NO_OPTION) {
	                    	
	                    	} //end of else
	    				
	                    
			    		}//end of if	
						else SaveWeightsToDB();
					}
						//else {JOptionPane.showMessageDialog(frame, "Graph already exists");}
				}//end of actionperfomed
			}	//end of build graph action listener
			
			public int ListHasUser(int index) {//input: user_id ,output: index of user if exist
				for(User user:UsersList){
					if(user.UserId==index)return UsersList.indexOf(user);
				}
				
				return -1;
			}
			
			public void SaveWeightsToDB(){
				try {
					PreparedStatement ps3=connection.prepareStatement("TRUNCATE `academia`.`weights`;");
					ps3.executeUpdate();
					ps3=connection.prepareStatement("INSERT INTO Weights values (?,?,?,?);");
		    		int tableIndex=0;
		    		int weightF;
		    		int idFriend;
		    		for(User user:UsersList){
						if(!user.FriendsListEmpty()){
							int numOfFriends=user.getNumOfFriends();
							for(int s=1;s<numOfFriends;s++){
									tableIndex++;
									idFriend=user.getIdOfFriend(s);
									weightF=user.getWeightOfFriend(s);
									
									ps3.setInt(1, tableIndex);
									ps3.setInt(2, user.UserId);
									ps3.setInt(3, idFriend);
									ps3.setInt(4, weightF);
									ps3.executeUpdate();
								
								
							}}
					}
					
		    		ResultSet rst;
		    		String result;
					try {
						rst = ps.executeQuery("SELECT count('*') FROM academia.weights;");
						while(rst.next())
				 		{
							result=rst.getString("count('*')");
							labelEdges.setText(result);
				 		}
						//ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(frame, "Graph successfully saved in DB");
				
			}
			public int reputationListHas(int index,int user_id){
				for(Reputation temp:repList){
					if(temp.reputation==index){
						temp.addUser(user_id);
						return 1;
					}
				}
				return -1;
			}
			public int CreationDateListHas(String index,int user_id){
				for(CreationDate temp:dateList){
					if(temp.date.equals(index)){
						temp.addUser(user_id);
						return 1;
					}
				}
				return -1;
			}
			public int CreationDateListHas2(String index,int user_id){
				for(CreationDate temp:LocationList){
					if(temp.date.equals(index)){
						temp.addUser(user_id);
						return 1;
					}
				}
				return -1;
			}
			public void initConnection(){
				try{
					Class.forName("com.mysql.jdbc.Driver");
					connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/academia","root","2015");
					connection2=DriverManager.getConnection("jdbc:mysql://localhost:3306/academia","root","2015");
					
					connectedToDB=true;
					}
				catch(Exception e)
					{
					
					}
			}
		
		
}}