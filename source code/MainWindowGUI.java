import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.Color;


public class MainWindowGUI extends JFrame{
	public  ArrayList<NodeList> list;
	private static final long serialVersionUID = 1L;
	private JTextField NUmOfFeaturestextField;
	private JTextField NumOfRawsTextFiled;
	PreparedStatement ps=null;
	PreparedStatement ps2=null;
	ResultSet rs=null;
	Statement stmt;
	boolean comListExist=false;
	GraphManagerGUI graphManagerGui;
	ClusterManagerGUI clusterManagerGui;
	SybilDetectorGUI sybilDetectorGui;
	String filePath=null;
	Connection connection=null;
	String createDB[]=new String [10];
	String importDB[]=new String [10];
	String tablesList[]=new String[10];
	boolean conEstablished=false;
	
	ArrayList<String> fields = new ArrayList<String>();
	
	int i=0;
	private JLabel lblImage;
	
	
	public int counter=0;
	static JFrame frame = new JFrame();
	boolean exists=false;
	public MainWindowGUI ()	{
		list = new ArrayList<NodeList>();
		clusterManagerGui = new ClusterManagerGUI(this);
		graphManagerGui = new GraphManagerGUI(this);
		sybilDetectorGui = new SybilDetectorGUI(this);
		getContentPane().setLayout(null);
				

	if(!conEstablished)initConnection();
	initTableList();
	createDatabase();
	
	}
//Update list of results	
	public void updateList(ArrayList<NodeList> uL){
		list.removeAll(list);
			list.addAll(uL);
			
	}
//Initialization 	
	public void initMainWindowGUI(){
		
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 548, 332);
		frame.setTitle("Main Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

		JButton btnGraphManager = new JButton("Graph Manager");
		btnGraphManager.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnGraphManager.setBounds(356, 89, 147, 29);
		frame.getContentPane().add(btnGraphManager);
		btnGraphManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		
		JButton btnClusterManager = new JButton("Cluster Manager");
		btnClusterManager.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClusterManager.setBounds(356, 175, 147, 29);
		frame.getContentPane().add(btnClusterManager);
		

		
		JButton btnSybilDetector = new JButton("Sybil Detector");
		btnSybilDetector.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSybilDetector.setBounds(356, 215, 147, 29);
		frame.getContentPane().add(btnSybilDetector);
		

		
		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLoadData.setBounds(356, 45, 147, 29);
		frame.getContentPane().add(btnLoadData);
		btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton btnClusteringProcess = new JButton("Clustering Proccess");
		btnClusteringProcess.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClusteringProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnClusteringProcess.setBounds(356, 135, 147, 29);
		frame.getContentPane().add(btnClusteringProcess);
		
		btnGraphManager.addActionListener(new btnGraphManagerActionListener());
 		btnLoadData.addActionListener(new btnLoadDataActionListener());
 		btnClusteringProcess.addActionListener(new btnClusteringProccessActionListener());
 		btnClusterManager.addActionListener(new btnClusterMnagaerActionListener());
 		btnSybilDetector.addActionListener(new btnSybilDetectorActionListener());
 

 		lblImage = new JLabel("");
 		Image img = new ImageIcon(this.getClass().getResource("/social.jpg")).getImage();
 		lblImage.setIcon(new ImageIcon (img));
 		lblImage.setBounds(1, 1, 325, 270);
 		frame.getContentPane().add(lblImage);
	}
	
//Initialize connection	to DB
	public void initConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/academia","root","root");
			
			conEstablished=true;
			}
		catch(Exception e)
			{
			JOptionPane.showMessageDialog(this, e.getMessage());//show message
			}
	}
	
//Database creating	
	public void createDatabase() {
		
		createDB[1]="CREATE TABLE IF NOT EXISTS academia.Users(User_ID int NOT NULL,Reputation varchar(255),CreationDate DATETIME NULL,DisplayName varchar(255),LastAccessDate DATETIME NULL,WebsiteUrl varchar(255), Location varchar(255),AboutMe varchar(255),"
				+"Views varchar(255),UpVotes varchar(255),DownVotes varchar(255),Age varchar(45),AccountId varchar(255),PRIMARY KEY(User_ID));";
		createDB[2]="CREATE TABLE IF NOT EXISTS academia.Votes(Vote_ID int NOT NULL,Post_ID int,VoteTypeId varchar(255),CreationDate DATETIME NULL,PRIMARY KEY(Vote_ID));";
		createDB[3]="CREATE TABLE IF NOT EXISTS academia.Comments(Comment_ID int NOT NULL,Post_ID int,Score varchar(255),Text varchar(255),CreationDate DATETIME NULL,User_ID int,PRIMARY KEY(Comment_ID));";
		createDB[4]="CREATE TABLE IF NOT EXISTS academia.Posts(Post_ID int NOT NULL,PostTypeId int,AcceptedAnswerId varchar(255),CreationDate DATETIME NULL,Score varchar(255),ViewCount varchar(255),Body varchar(255),OwnerUserId varchar(255),LastEditorUserId varchar(255),LastEditDate DATETIME NULL,LastActivityDate DATETIME NULL,Title varchar(255),Tags varchar(255),AnswerCount varchar(255),CommentCount varchar(255),FavoriteCount varchar(255),PRIMARY KEY(Post_ID));";
		createDB[5]="CREATE TABLE IF NOT EXISTS academia.Friends(Id int NOT NULL,User_ID varchar(255),Friend_ID varchar(255),PRIMARY KEY(Id));";
	
					for(int j=1;j<6;j++){
					try {
						ps=connection.prepareStatement(createDB[j]);
						ps.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					}
		
					initTableFields();
	}
			
	
	
//Import data from external files	
	public void importDatabase() throws SQLException, IOException {
					
		boolean importTable=true;
		boolean friendsTable=false;
		JFileChooser chooser=new JFileChooser(new File("c:\\academia.stackexchange.com\\"));
	  	chooser.setMultiSelectionEnabled(false);
		chooser.setVisible(true);
		chooser.showOpenDialog(this);
		File file=chooser.getSelectedFile();
		if(file!=null){	filePath=file.getPath();}
		String filename=file.getName();
		filename=filename.substring(0, filename.lastIndexOf("."));
		if(filePath!=null && check()){
		   		int result=0;
				ResultSet rst;
				 
					PreparedStatement ps3=connection.prepareStatement("SELECT count('*') FROM "+filename+";");
					rst = ps3.executeQuery();
					while(rst.next()){
									result=Integer.parseInt(rst.getString("count('*')"));
															 
			 						}
				 
				if(result>0) {		
				int n = JOptionPane.showConfirmDialog(frame, "Table "+filename+" already exists. Overide it?","Warning",JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                	importTable=true;          	        						
               	}else if (n == JOptionPane.NO_OPTION) {
               		importTable=false;
                	
                	} //end of else
				}
				if(importTable){
					if(filename.equals("Users")){i=1;friendsTable=false;}
        			if(filename.equals("Votes")){i=2;friendsTable=false;}
        			if(filename.equals("Comments")){i=3;friendsTable=false;}
        			if(filename.equals("Posts")){i=4;friendsTable=false;}
        			if(filename.equals("Friends")){ importFromFileToDB(filename,filePath); 
        			friendsTable=true;
        			}
        							 
        				
        			if(!friendsTable){
        				importDB[1]="LOAD XML LOCAL INFILE ? INTO TABLE academia.Users(User_ID,Reputation,CreationDate,DisplayName,LastAccessDate,WebsiteUrl,Location,AboutMe,Views,UpVotes,DownVotes,AccountId);";
        				importDB[2]="LOAD XML LOCAL INFILE ? INTO TABLE academia.Votes(Vote_ID,Post_ID,VoteTypeId,CreationDate);";
        				importDB[3]="LOAD XML LOCAL INFILE ? INTO TABLE academia.Comments(Comment_ID,Post_ID,Score,Text,CreationDate,User_ID);";
        				importDB[4]="LOAD XML LOCAL INFILE ? INTO TABLE academia.Posts(Post_ID,PostTypeId,AcceptedAnswerId,CreationDate,Score,ViewCount,Body,OwnerUserId,LastEditorUserId,LastEditDate,LastActivityDate,Title,Tags,AnswerCount,CommentCount,FavoriteCount);";
        			
        			
        				if(i>0)importFromXMLfileToDB(importDB[i],filePath,filename);
        				}					
				}
			
				}else JOptionPane.showMessageDialog(this,"Please select file of type xml/txt/csv");		
			}//end of importDatabase

//Checking if external files are end with extension ".xml", ".txt", ".csv"
	private boolean check() {
		if(filePath!=null) 
			{
			if(filePath.endsWith(".xml")||filePath.endsWith(".txt")||filePath.endsWith(".csv"))
				{
				return true;
				}
			return false;
			}
		return false;
		}//end of check method
	
//import from XML file to DB	
	public void importFromXMLfileToDB(String query,String filePath,String tableName) throws SQLException, IOException{
		int result;
		ps=connection.prepareStatement("TRUNCATE `academia`.`"+tableName+"`;");
		ps.executeUpdate();		
		FileInputStream fileInputStream = new FileInputStream(filePath);
		ps=connection.prepareStatement(query);
		ps.setObject(1, filePath);
		byte b[]=new byte[fileInputStream.available()];
		fileInputStream.read(b);
		fileInputStream.close();
		result=ps.executeUpdate();
		if(result>=1)	JOptionPane.showMessageDialog(this, "Table "+tableName+" succesfully stored");
		
		}
	
	public void importFromFileToDB(String tableName,String inputFile){
		int indexTable=0;
		
		PreparedStatement ps3;			
			Scanner scan;			 
				try {
					ps3=connection.prepareStatement("TRUNCATE `academia`.`"+tableName+"`;");
					ps3.executeUpdate();
					
					ps3 = connection.prepareStatement("INSERT INTO academia."+tableName+" values (?,?,?);");
					scan = new Scanner(new File(inputFile));
					while(scan.hasNextLine()){
				        String line = scan.nextLine();
				        String[] vars = line.split(" ");
				        indexTable++;
				        
				        ps3.setObject(1, indexTable);
				        ps3.setObject(2, vars[0]);
						ps3.setObject(3, vars[1]);
						ps3.executeUpdate();
						
						}
					
					int result=0;
					ResultSet rst;
					 
						ps3=connection.prepareStatement("SELECT count('*') FROM "+tableName+";");
						rst = ps3.executeQuery();
						while(rst.next()){
										result=Integer.parseInt(rst.getString("count('*')"));
																 
				 						}
					 
					if(result>0) 
					JOptionPane.showMessageDialog(this, "Table "+tableName+" succesfully stored");
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	public void importFileFromMatlab()throws SQLException, IOException{
		JFileChooser chooser2=new JFileChooser(new File("c:\\academia.stackexchange.com\\"));
	  	chooser2.setMultiSelectionEnabled(false);
		chooser2.setVisible(true);
		chooser2.showOpenDialog(this);
		File file=chooser2.getSelectedFile();
		if(file!=null){	filePath=file.getPath();}
		String filename=file.getName();
		filename=filename.substring(0, filename.lastIndexOf("."));
		ps=connection.prepareStatement("CREATE TABLE IF NOT EXISTS academia.comVector(UserId int NOT NULL,Community_ID int,PRIMARY KEY(UserId));");
		ps.executeUpdate();
		ps=connection.prepareStatement("CREATE TABLE IF NOT EXISTS academia.ComSizesVector(Community_ID varchar(255) NOT NULL,Com_size varchar(255),PRIMARY KEY(Community_ID));");
		ps.executeUpdate();
		int result=0;
		
		ResultSet rst;
		PreparedStatement ps3=connection.prepareStatement("SELECT count('*') FROM "+filename+";");
		rst = ps3.executeQuery();
		while(rst.next()){
				result=Integer.parseInt(rst.getString("count('*')"));
								 
	 					}
		 
		if(result>0) {
			//tableExists=true;
			
		
		int n = JOptionPane.showConfirmDialog(frame, "Table "+filename+" already exists. Overide it?","Warning",JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
        		ps=connection.prepareStatement("TRUNCATE `academia`.`"+filename+"`;");
        		ps.executeUpdate();	
        		if(filename.equals("comVector"))importComVector(file,filename);
        		if(filename.equals("ComSizesVector"))importComSizesVector(file,filename);
        		
       	}else if (n == JOptionPane.NO_OPTION) {
       		
        	
        	} //end of else
		}
		else {if(filename.equals("comVector"))importComVector(file,filename);
		if(filename.equals("ComSizesVector"))importComSizesVector(file,filename);
		}
		
	}
	
	public void importComVector(File file,String filename){
		Scanner scan;
		int indexTable=0;
		PreparedStatement ps3;
		try {
			ps3 = connection.prepareStatement("INSERT INTO academia."+filename+" values (?,?);");
			scan = new Scanner(file);
			while(scan.hasNextLine()){
		        String line = scan.nextLine();
		        String[] vars = line.split(" ");
		        indexTable++;
		        ps3.setObject(1, indexTable);
		        ps3.setObject(2, vars[0]);
				ps3.executeUpdate();
				
				}
			
			int result=0;
			ResultSet rst;
			 
				ps3=connection.prepareStatement("SELECT count('*') FROM "+filename+";");
				rst = ps3.executeQuery();
				while(rst.next()){
								result=Integer.parseInt(rst.getString("count('*')"));
														 
		 						}
			 
			if(result>0) 
			JOptionPane.showMessageDialog(this, "Clustering proccess finished");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void importComSizesVector(File file,String filename){
		Scanner scan;
		int indexTable=0;
		PreparedStatement ps3;
		try {
			ps3 = connection.prepareStatement("INSERT INTO academia."+filename+" values (?,?);");
			scan = new Scanner(file);
			while(scan.hasNextLine()){
		        String line = scan.nextLine();
		        String[] vars = line.split(" ");
		        indexTable++;
		        ps3.setObject(1, indexTable);
		        ps3.setObject(2, vars[0]);
				ps3.executeUpdate();
				
				}
			
			int result=0;
			ResultSet rst;
			 
				ps3=connection.prepareStatement("SELECT count('*') FROM "+filename+";");
				rst = ps3.executeQuery();
				while(rst.next()){
								result=Integer.parseInt(rst.getString("count('*')"));
														 
		 						}
			 
			if(result>0) 
			JOptionPane.showMessageDialog(this, "Table "+filename+" succesfully stored");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initTableList(){
		
		tablesList[1]="Users";
		tablesList[2]="Votes";
		tablesList[3]="Comments";
		tablesList[4]="Posts";
		tablesList[5]="Friends";
		
		
		
	}
	public void initTableFields(){
		
		String stmt[]=new String [5];
		stmt[0]="show fields from academia.Users;";
		stmt[1]="show fields from academia.Votes;";
		stmt[2]="show fields from academia.Comments;";
		stmt[3]="show fields from academia.Posts;";
		stmt[4]="show fields from academia.Friends;";
				
		
				try {
					int i;
					for(i=0;i<5;i++){
					ps=connection.prepareStatement(stmt[i]);
					ResultSet rst = ps.executeQuery();
					while(rst.next())
			 			{
						String temp = rst.getString("Field");
							if(fields.indexOf(temp)<0)
							fields.add(temp);
			 			}//end of while
					}//end of for
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	}//end of initTableFields
			
	public void showTableStatistics(String filename){
		try {
		if(filename.equals("Users")){				
			Statement st = connection.createStatement();
			
			ResultSet rst = st.executeQuery("SELECT count('*') FROM academia.Users;");
			while(rst.next())
	 		{
				JOptionPane.showMessageDialog(this,"The "+ filename + " table " + "has " + 
						 rst.getString("count('*')")+ " raws!");//----------//
				 
	 		}
			st.close();
		}
			
		if(filename.equals("Votes")){				
			Statement st = connection.createStatement();
			ResultSet rst = st.executeQuery("SELECT count('*') FROM academia.Votes;");
			while(rst.next())
	 		{
				JOptionPane.showMessageDialog(this,"The "+ filename + " table " + "has " + 
											 rst.getString("count('*')")+ " raws!");//----------//
				 
	 		}
			st.close();
		}
		
		
		
		if(filename.equals("Comments")){				
			Statement st = connection.createStatement();
			ResultSet rst = st.executeQuery("SELECT count('*') FROM academia.Comments;");
			while(rst.next())
	 		{
				JOptionPane.showMessageDialog(this,"The "+ filename + " table " + "has " + 
						 rst.getString("count('*')")+ " raws!");//----------//
				 
	 		}
			st.close();
		}
		
		if(filename.equals("Posts")){				
			Statement st = connection.createStatement();
			ResultSet rst = st.executeQuery("SELECT count('*') FROM academia.Posts;");
			while(rst.next())
	 		{
				JOptionPane.showMessageDialog(this,"The "+ filename + " table " + "has " + 
						 rst.getString("count('*')")+ " raws!");//----------//
				 
	 		}
			st.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//ActionListener
			 class btnLoadDataActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event){
					 
					try {
						importDatabase();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}	
			}
	
	//ActionListener
		 class btnGraphManagerActionListener implements ActionListener {
			public void actionPerformed(ActionEvent event){
				
				graphManagerGui.initGraphManagerGUI(fields);
				setVisible(false);
				dispose();
			}
		 }
		
		
			class btnClusteringProccessActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event){
									
					try {
						importFileFromMatlab();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
						
										
						
			
				}}		 
		//ActionListener
			
				 class btnClusterMnagaerActionListener implements ActionListener {
					public void actionPerformed(ActionEvent event){
						
						clusterManagerGui.initClusterManagerGUI();												
						setVisible(false);
						dispose();
					
					}	
				}
			
				//ActionListener
				class btnSybilDetectorActionListener implements ActionListener {
					public void actionPerformed(ActionEvent event){
						sybilDetectorGui.initSybilDetectorGUI();
						sybilDetectorGui.showList(list);
						setVisible(false);
						dispose();
					}	
				}
		 }

