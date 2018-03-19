import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;



public class LoginWindowGUI {
	private static   JTextField IDtextField;
	private static JPasswordField passwordField;
    private  JButton btnLogin;
    private JButton btnClear;
    static JFrame frame = new JFrame();
    private JLabel labelImage;
	/**
	 * @wbp.parser.entryPoint
	 */
 
 //Creating the Login window  
	public void init(){
		labelImage = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/login.png")).getImage();
		labelImage.setIcon(new ImageIcon (img));
		labelImage.setBounds(78, 81, 203, 236);
		frame.getContentPane().add(labelImage);
		
	
	frame.getContentPane().setBackground(Color.LIGHT_GRAY);
	 IDtextField = new JTextField();
	 passwordField = new JPasswordField();

	frame.setBounds(100, 100, 737, 430);
	frame.setTitle("Login");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	
	
	JLabel labelUserId = new JLabel("UserID");
	labelUserId.setFont(new Font("Tahoma", Font.BOLD, 24));
	labelUserId.setBounds(336, 120, 99, 26);
	frame.getContentPane().add(labelUserId);
		
	IDtextField.setBounds(478, 126, 157, 26);
	frame.getContentPane().add(IDtextField);
	IDtextField.setColumns(10);
		
	JLabel lblPassword = new JLabel("Password");
	lblPassword.setFont(new Font("Tahoma", Font.BOLD, 24));
	lblPassword.setBounds(336, 175, 128, 23);
	frame.getContentPane().add(lblPassword);
		
	passwordField = new JPasswordField();
	passwordField.setBounds(478, 179, 157, 26);
	frame.getContentPane().add(passwordField);
	
	btnLogin = new JButton("Login");
	btnLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
	btnLogin.setBounds(336, 265, 139, 33);
	frame.getContentPane().add(btnLogin);
		
		
	btnClear = new JButton("Clear");
	btnClear.setFont(new Font("Tahoma", Font.BOLD, 20));
	btnClear.setBounds(511, 265, 124, 33);
	frame.getContentPane().add(btnClear);
	
	
		
	btnLogin.addActionListener(new btnMainWindowActionListener());
	btnClear.addActionListener(new btnActionListener());
	
	
	}
	
	//LoginButtonActionListener
	static class btnMainWindowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event){
			String UserID = IDtextField.getText();
			@SuppressWarnings("deprecation")
			String Password = (passwordField.getText());
		int i =	UserID.compareTo("1");
		int j = Password.compareTo("1");
			if((i==0) && (j==0))
			{
			MainWindowGUI mainWindowGui = new MainWindowGUI();
			
			mainWindowGui.initMainWindowGUI();
			frame.dispose();
			}
			else
				JOptionPane.showMessageDialog(null, "User ID or Password isn't correct. Please try again!!!");
				IDtextField.setText("");
				passwordField.setText("");
			
		}
	}
	
	//ClearButtonActionListener
		static class btnActionListener implements ActionListener {
			public void actionPerformed(ActionEvent event){
				 IDtextField.setText("");
				 passwordField.setText("");
				}
			}
}

