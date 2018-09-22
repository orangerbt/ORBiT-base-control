package oldCode;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class ValueProfileMaker {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_2;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ValueProfileMaker window = new ValueProfileMaker();
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
	public ValueProfileMaker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 577, 415);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Type");
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblName = new JLabel("Name");
		panel_2.add(lblName);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		panel_2.add(textField_1);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblDisplay = new JLabel("Display");
		panel_3.add(lblDisplay);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1,2,3,4"}));
		panel_3.add(comboBox);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblUnit = new JLabel("Unit");
		panel_4.add(lblUnit);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		panel_4.add(textField_3);
		
		JPanel panel_8 = new JPanel();
		panel.add(panel_8);
		panel_8.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblOrder = new JLabel("Order");
		panel_8.add(lblOrder);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		panel_8.add(textField_6);
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblHighlimited = new JLabel("High Limited");
		panel_5.add(lblHighlimited);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		panel_5.add(textField_4);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblLowlimited = new JLabel("Low Limited");
		panel_6.add(lblLowlimited);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		panel_6.add(textField_2);
		
		JPanel panel_7 = new JPanel();
		panel.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblPassvalue = new JLabel("Pass Value");
		panel_7.add(lblPassvalue);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		panel_7.add(textField_5);
		
		JPanel panel_9 = new JPanel();
		panel.add(panel_9);
		panel_9.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnNewButton_1 = new JButton("New button");
		panel_9.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("New button");
		panel_9.add(btnNewButton);
		panel.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				panel_1.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_2.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_3.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_4.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_5.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_6.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_7.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_8.setPreferredSize(new Dimension(host.getWidth(),30));
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				panel_1.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_2.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_3.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_4.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_5.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_6.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_7.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_8.setPreferredSize(new Dimension(host.getWidth(),30));
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				panel_1.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_2.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_3.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_4.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_5.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_6.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_7.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_8.setPreferredSize(new Dimension(host.getWidth(),30));
//				panel_9.setPreferredSize(new Dimension(host.getWidth(),20));
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				panel_1.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_2.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_3.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_4.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_5.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_6.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_7.setPreferredSize(new Dimension(host.getWidth(),30));
				panel_8.setPreferredSize(new Dimension(host.getWidth(),30));
			}
			
		});
	}

}
