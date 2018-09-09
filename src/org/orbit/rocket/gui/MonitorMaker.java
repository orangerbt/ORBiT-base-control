package org.orbit.rocket.gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class MonitorMaker {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorMaker window = new MonitorMaker();
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
	public MonitorMaker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 553, 506);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 4, 0, 0));
		
		JLabel DataPort = new JLabel("Data Port");
		panel.add(DataPort);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel DiscoverPort = new JLabel("Discover Port");
		panel.add(DiscoverPort);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.WEST);
		
		String a[]={"2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2"};
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
//		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		JList list = new JList(a);
		list.setPreferredSize(new Dimension(100,500));
//		scrollPane.setPreferredSize(new Dimension(100,400));
		scrollPane.setViewportView(list);
		
		JPanel main = new JPanel();
		frame.getContentPane().add(main, BorderLayout.CENTER);
		
		JPanel NamePanel = new JPanel();
		NamePanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Monitor Name");
		NamePanel.add(lblNewLabel);
		
		textField_2 = new JTextField();
		NamePanel.add(textField_2);
		textField_2.setColumns(10);
		
		JPanel CheckPanel = new JPanel();
		CheckPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Is GPSReady");
		CheckPanel.add(chckbxNewCheckBox);
		
		JPanel ListPanel = new JPanel();
		ListPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		ListPanel.add(scrollPane_1);
		
		JList list_1 = new JList(a);
		scrollPane_1.setViewportView(list_1);
		main.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		main.add(NamePanel);
		main.add(CheckPanel);
		
		JPanel ButtonPanel = new JPanel();
		main.add(ButtonPanel);
		ButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnNewButton = new JButton("Load Value Profile");
		ButtonPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Unload Value Profile");
		ButtonPanel.add(btnNewButton_1);
		main.add(ListPanel);
		
		JPanel SavePanel = new JPanel();
		main.add(SavePanel);
		SavePanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton_2 = new JButton("Save");
		SavePanel.add(btnNewButton_2);
		main.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)arg0.getSource();
				ListPanel.setPreferredSize(new Dimension(host.getWidth(),host.getHeight()-(80+65)));
				CheckPanel.setPreferredSize(new Dimension(host.getWidth(),20));
				NamePanel.setPreferredSize(new Dimension(host.getWidth(),30));
				ButtonPanel.setPreferredSize(new Dimension(host.getWidth(),30));
				SavePanel.setPreferredSize(new Dimension(host.getWidth(),30));
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

}
