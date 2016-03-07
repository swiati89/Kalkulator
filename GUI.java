import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Choice;

public class GUI {

	private JFrame frame;
	private JTextField tfName;
	private JTextField tfInsulinResistance;
	private JTextField tfWeight;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnCreateNewUser = new JButton("New button");
		btnCreateNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				User nowy = new User();
				
				try{
				String name = tfName.getText();
				nowy.setName(name);
				System.out.println(name);
				}catch(IllegalArgumentException e){};
				
				try{					
				double insulinResistance = Double.parseDouble(tfInsulinResistance.getText());
				nowy.setInsulinResistance(insulinResistance);
				}catch(NumberFormatException e){};
				
				try{				
				int weight = Integer.parseInt(tfWeight.getText());
				nowy.setWeight(weight);
				}catch(NumberFormatException e){};
					
								
			}
		});
		btnCreateNewUser.setBounds(161, 193, 89, 23);
		frame.getContentPane().add(btnCreateNewUser);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 25, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		tfName = new JTextField();
		tfName.setBounds(10, 50, 86, 20);
		frame.getContentPane().add(tfName);
		tfName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(10, 81, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		tfInsulinResistance = new JTextField();
		tfInsulinResistance.setBounds(10, 106, 86, 20);
		frame.getContentPane().add(tfInsulinResistance);
		tfInsulinResistance.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(10, 137, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		tfWeight = new JTextField();
		tfWeight.setBounds(10, 162, 86, 20);
		frame.getContentPane().add(tfWeight);
		tfWeight.setColumns(10);
	}
}
