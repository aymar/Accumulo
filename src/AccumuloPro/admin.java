package AccumuloPro;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.CardLayout;

import javax.swing.JTextField;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

public class admin extends JFrame {

	private JPanel contentPane;
	private JPanel searchPane;
	private JPanel createTablePane;
	private JPanel insertDataPane;
	private JLabel lblWlcome;
	
	
	
	
	/**
	 * Launch the application.
	 */
	public static void adminmain(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					admin frame = new admin();
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
	public admin() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 507);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBounds(42, 156, 147, 136);
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(null);
		
		JButton btnCreateTable = new JButton("Create Table");
		btnCreateTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			createTablePane.setVisible(true);
			insertDataPane.setVisible(false);
			searchPane.setVisible(false);
				
			}
		});
		btnCreateTable.setBackground(new Color(255, 222, 173));
		btnCreateTable.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCreateTable.setBounds(0, 11, 139, 23);
		buttonPanel.add(btnCreateTable);
		
		JButton btnInsertFromBP = new JButton("Insert Data");
		btnInsertFromBP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTablePane.setVisible(false);
				insertDataPane.setVisible(true);
				searchPane.setVisible(false);
			}
		});
		btnInsertFromBP.setBackground(new Color(255, 222, 173));
		btnInsertFromBP.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnInsertFromBP.setBounds(0, 57, 139, 23);
		buttonPanel.add(btnInsertFromBP);
		
		JButton btnScan = new JButton("Search");
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTablePane.setVisible(false);
				insertDataPane.setVisible(false);
				searchPane.setVisible(true);
			}
		});
		btnScan.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnScan.setBackground(new Color(255, 222, 173));
		btnScan.setBounds(0, 98, 139, 23);
		buttonPanel.add(btnScan);
		
		JLabel lblTop = new JLabel("");
		lblTop.setIcon(new ImageIcon(admin.class.getResource("/images/top.png")));
		lblTop.setBounds(0, 0, 694, 88);
		contentPane.add(lblTop);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(198, 138, 487, 166);
		contentPane.add(displayPanel);
		displayPanel.setLayout(new CardLayout(0, 0));
		
		createTablePane = new JPanel();
		createTablePane.setBorder(new LineBorder(new Color(0, 0, 0)));
		createTablePane.setLayout(null);
		createTablePane.setBackground(new Color(192, 192, 192));
		displayPanel.add(createTablePane, "name_62688577061866");
		
		final JTextField txtTableName = new JTextField();
		txtTableName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtTableName.setColumns(10);
		txtTableName.setBounds(212, 50, 167, 20);
		createTablePane.add(txtTableName);
		
		JLabel label_6 = new JLabel("Table Name");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_6.setBounds(106, 50, 96, 20);
		createTablePane.add(label_6);
		
		JButton btnCreate = new JButton("Create");
		
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateTableCommand createTbl = new CreateTableCommand();
				createTbl.setConnection(LoginR.connection);
				String tableName = txtTableName.getText();
				try {
					createTbl.setTable(tableName);
					createTbl.run();
					JOptionPane.showMessageDialog(null, "Created table: " + tableName);
				} catch (AccumuloException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AccumuloSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TableExistsException e1) {
					JOptionPane.showMessageDialog(null, "Table already exists");
				} catch (RuntimeException e1) {
					JOptionPane.showMessageDialog(null, "Table already exists");
				}
				
			}
		});
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCreate.setBounds(173, 92, 89, 23);
		createTablePane.add(btnCreate);
		
		insertDataPane = new JPanel();
		insertDataPane.setLayout(null);
		insertDataPane.setBackground(new Color(192, 192, 192));
		displayPanel.add(insertDataPane, "name_62713028447079");
		
		JLabel lblTableName = new JLabel("Table Name");
		lblTableName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTableName.setBounds(10, 11, 86, 14);
		insertDataPane.add(lblTableName);
		
		final JTextField txtTblName = new JTextField();
		txtTblName.setColumns(10);
		txtTblName.setBounds(10, 36, 86, 20);
		insertDataPane.add(txtTblName);
		
		JLabel label_1 = new JLabel("Value");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_1.setBounds(289, 11, 46, 14);
		insertDataPane.add(label_1);
		
		final JTextField txtValue = new JTextField();
		txtValue.setColumns(10);
		txtValue.setBounds(287, 36, 190, 110);
		insertDataPane.add(txtValue);
		
		JLabel label_2 = new JLabel("Row Id");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_2.setBounds(132, 11, 46, 14);
		insertDataPane.add(label_2);
		
		final JTextField txtRowId = new JTextField();
		txtRowId.setColumns(10);
		txtRowId.setBounds(122, 36, 86, 20);
		insertDataPane.add(txtRowId);
		
		JLabel label_3 = new JLabel("Col Family");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_3.setBounds(10, 66, 76, 14);
		insertDataPane.add(label_3);
		
		final JTextField txtColFamily = new JTextField();
		txtColFamily.setColumns(10);
		txtColFamily.setBounds(10, 81, 86, 20);
		insertDataPane.add(txtColFamily);
		
		JLabel label_4 = new JLabel("Col Qualifier");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_4.setBounds(122, 67, 86, 14);
		insertDataPane.add(label_4);
		
		final JTextField txtColQual = new JTextField();
		txtColQual.setColumns(10);
		txtColQual.setBounds(122, 81, 86, 20);
		insertDataPane.add(txtColQual);
		
		JLabel lblVisibilty = new JLabel("Visibilty");
		lblVisibilty.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblVisibilty.setBounds(10, 112, 76, 14);
		insertDataPane.add(lblVisibilty);
		
		final JTextField txtVisibilty = new JTextField();
		txtVisibilty.setColumns(10);
		txtVisibilty.setBounds(10, 126, 86, 20);
		insertDataPane.add(txtVisibilty);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsertRowCommand insertRow = new InsertRowCommand();
				insertRow.setConnection(LoginR.connection);
				try {
					insertRow.run(txtTblName.getText(), txtRowId.getText(), txtColFamily.getText(), txtColQual.getText(), txtVisibilty.getText(), txtValue.getText());
					JOptionPane.showMessageDialog(null, "Successfully Inserted the values");
				} catch (MutationsRejectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TableNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnInsert.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnInsert.setBounds(153, 136, 89, 23);
		insertDataPane.add(btnInsert);
		
		
		
		searchPane = new JPanel();
		searchPane.setLayout(null);
		searchPane.setBackground(Color.LIGHT_GRAY);
		displayPanel.add(searchPane, "name_62757800485557");
		
		final JTextField txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBounds(150, 50, 197, 20);
		searchPane.add(txtSearch);
		
		final JTextPane txtRlts = new JTextPane();
		txtRlts.setBackground(Color.LIGHT_GRAY);
		txtRlts.setBounds(0, 299, 642, 156);
		
		JLabel label_5 = new JLabel("Search");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_5.setBounds(86, 28, 65, 14);
		searchPane.add(label_5);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String keyword = txtSearch.getText();
				ScanCommand scan = new ScanCommand();
				//JOptionPane.showMessageDialog(null, LoginR.connection);
				scan.setConnection(LoginR.connection);
				scan.setRow(keyword);
				scan.setUser(LoginR.user);
				try {
					scan.run("keyword");
				} catch (TableNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AccumuloException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AccumuloSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String totalResult = scan.getOutPutResult();
				
				txtRlts.setContentType("text/html");
				txtRlts.setText(totalResult);
				txtRlts.setCaretPosition(0);
				contentPane.add(txtRlts);
				
				JScrollPane scrollPane = new JScrollPane(txtRlts);
				
				//scrollPane.add(txtRlts);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setVisible(true);
				scrollPane.setBounds(0, 320, 692, 162);
				contentPane.add(scrollPane);			
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSubmit.setBounds(208, 98, 89, 23);
		searchPane.add(btnSubmit);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				LoginR.main(null);
			}
		});
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogout.setBounds(576, 97, 105, 30);
		contentPane.add(btnLogout);
		
		lblWlcome = new JLabel("");
		lblWlcome.setBackground(Color.BLUE);
		lblWlcome.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblWlcome.setBounds(10, 99, 156, 30);
		String WelcomeUserName = LoginR.user;
		lblWlcome.setText(" Welcome "+ WelcomeUserName +" !");	
		contentPane.add(lblWlcome);
	}
}
