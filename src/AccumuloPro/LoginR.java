package AccumuloPro;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;

import org.apache.accumulo.core.client.Connector;

import java.awt.Color;

public class LoginR extends JFrame {

	private JPanel contentPane;
	static JTextField txtName;
	private JPasswordField txtPassword;
	static Connector connection;
	static String user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginR frame = new LoginR();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginR() {
		setResizable(false);
		setTitle("frame1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 664, 498);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
							
				try{
					String username = txtName.getText();
					String Password = new String(txtPassword.getPassword());
					
				    AccumuloAuth auth = new AccumuloAuth ();
					auth.setUser(username);
					auth.setPassword(Password);
					LoginR.connection = auth.getConnection();
					LoginR.user = username;
					//connection = auth.getConnection();					
					dispose();
					//Search j2 = new Search();
					//JOptionPane.showConfirmDialog(null, username + ", "+ Password);
					if(username.equals("root") && Password.equals("secret")){
						admin.adminmain(null);						
					} else{
						Search.searchMain(null);
					}
					
					
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "Invalid username or password");
				}
				
			}
		});
		btnLogin.setBounds(343, 270, 134, 30);
		contentPane.add(btnLogin);
		
		txtName = new JTextField();
		txtName.setBounds(401, 162, 107, 30);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName_1 = new JLabel("Name");
		lblName_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName_1.setBounds(302, 157, 59, 36);
		contentPane.add(lblName_1);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(302, 208, 71, 25);
		contentPane.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(401, 204, 107, 30);
		contentPane.add(txtPassword);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginR.class.getResource("/images/side.png")));
		lblNewLabel.setBounds(24, 131, 268, 247);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(LoginR.class.getResource("/images/top.png")));
		lblNewLabel_1.setBounds(0, 0, 658, 114);
		contentPane.add(lblNewLabel_1);
	}
}
