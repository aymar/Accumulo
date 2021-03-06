package AccumuloPro;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.TableNotFoundException;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;


public class Search extends JFrame {

	private JPanel contentPane;
	private JRadioButton rdbtnKeyword, rdbtnTopic;
	private JPanel panelCard;
	private JPanel panelTopicCard;
	private JPanel panelKeyCard;
	private JLabel lblWlcome;
	private JTextPane txtRlts;
	private JScrollPane scrollPane;
	ButtonGroup group;	

	/**
	 * Launch the application.
	 */
	public static void searchMain(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Search frame = new Search();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void searchTopic()
	{
		panelTopicCard.setBackground(Color.WHITE);
		panelCard.add(panelTopicCard, "name_339578849588021");
		panelTopicCard.setLayout(null);
		
		JLabel lblSelectTopic = new JLabel("Select Topic");
		lblSelectTopic.setBounds(59, 10, 114, 25);
		panelTopicCard.add(lblSelectTopic);
		lblSelectTopic.setFont(new Font("Tahoma", Font.PLAIN, 17));
		String[] categories = {"network","science","computer"};
		final JComboBox bxcombo = new JComboBox(categories);
		bxcombo.setBounds(183, 11, 113, 25);
		panelTopicCard.add(bxcombo);
		bxcombo.setBackground(new Color(222, 184, 135));
		bxcombo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(59, 82, 82, 23);
		panelTopicCard.add(btnSelect);
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				String course = bxcombo.getSelectedItem().toString();
				ScanCommand scan = new ScanCommand();
				scan.setConnection(LoginR.connection);
				scan.setTable();
				scan.setColumn(course);
				scan.setUser(LoginR.user);
				try {
					scan.run("topic");
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

				displayResults(scan.getOutPutResult());
			}
		});
		btnSelect.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(11, 10, 38, 31);
		panelTopicCard.add(lblNewLabel_1);
		lblNewLabel_1.setIcon(new ImageIcon(Search.class.getResource("/images/search.png")));
	} // end of search topic
	
	public void searchKeyword()
	{			
		panelKeyCard.setBackground(Color.WHITE);
		panelCard.add(panelKeyCard, "name_339584614638264");
		panelKeyCard.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(23, 26, 29, 28);
		panelKeyCard.add(lblNewLabel_2);
		lblNewLabel_2.setIcon(new ImageIcon(Search.class.getResource("/images/search.png")));
		
		JLabel lblKeyword = new JLabel("Keyword");
		lblKeyword.setBounds(70, 26, 81, 25);
		panelKeyCard.add(lblKeyword);
		lblKeyword.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		final JTextField txtKeyword = new JTextField();
		txtKeyword.setText("");
		txtKeyword.setBounds(161, 26, 122, 25);
		panelKeyCard.add(txtKeyword);
		txtKeyword.setColumns(10);
		
		JButton btnSelect_1 = new JButton("Select");
		btnSelect_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				String keyword = txtKeyword.getText();
				ScanCommand scan = new ScanCommand();			
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

				displayResults(scan.getOutPutResult());
			}
		});
		btnSelect_1.setBounds(80, 82, 81, 23);
		panelKeyCard.add(btnSelect_1);
		btnSelect_1.setFont(new Font("Tahoma", Font.BOLD, 15));
	} // end of search keyword

	public void displayResults(String result)
	{
		txtRlts.setContentType("text/html");
		txtRlts.setText(result);
		txtRlts.setCaretPosition(0);
		scrollPane.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public Search() {
		panelKeyCard = new JPanel();
		panelTopicCard = new JPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 704, 494);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Search.class.getResource("/images/top.png")));
		lblNewLabel.setBounds(0, 0, 700, 88);
		contentPane.add(lblNewLabel);
						
		JPanel panelRadio = new JPanel();
		panelRadio.setBackground(Color.WHITE);
		panelRadio.setBounds(81, 158, 149, 112);
		contentPane.add(panelRadio);
		panelRadio.setLayout(null);
		
		rdbtnKeyword = new JRadioButton("Keyword");
		rdbtnKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchKeyword();
				panelKeyCard.setVisible(true);
				panelTopicCard.setVisible(false);
			}
		});
		rdbtnKeyword.setBackground(Color.WHITE);
		rdbtnKeyword.setBounds(23, 69, 94, 27);
		rdbtnKeyword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelRadio.add(rdbtnKeyword);
		// topic radio button section
	    rdbtnTopic = new JRadioButton("Topic");
	    rdbtnTopic.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		searchTopic();
	    		panelKeyCard.setVisible(false);
	    		panelTopicCard.setVisible(true);
	    	}
	    });
		rdbtnTopic.setBackground(Color.WHITE);
		rdbtnTopic.setBounds(24, 19, 80, 27);
		rdbtnTopic.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelRadio.add(rdbtnTopic);
		group = new ButtonGroup();
		group.add(rdbtnKeyword);
		group.add(rdbtnTopic);
		
		//card panel
		panelCard = new JPanel();
		panelCard.setBackground(Color.WHITE);
		panelCard.setBounds(240, 140, 344, 148);
		contentPane.add(panelCard);
		panelCard.setLayout(new CardLayout(0, 0));
		
		lblWlcome = new JLabel("");
		lblWlcome.setBackground(Color.BLUE);
		lblWlcome.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblWlcome.setBounds(10, 106, 156, 30);
		String WelcomeUserName = LoginR.user;
		lblWlcome.setText(" Welcome "+ WelcomeUserName +" !");	
		contentPane.add(lblWlcome);
		
		txtRlts = new JTextPane();
		txtRlts.setBackground(Color.LIGHT_GRAY);
		txtRlts.setBounds(0, 299, 642, 156);
		
		scrollPane = new JScrollPane(txtRlts);
		scrollPane.setVisible(false);
		scrollPane.setBounds(0, 299, 700, 160);
		contentPane.add(scrollPane);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				LoginR.main(null);
			}
		});
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogout.setBounds(514, 106, 105, 30);
		contentPane.add(btnLogout);		
	}
}
