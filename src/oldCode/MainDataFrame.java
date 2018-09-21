package oldCode;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MainDataFrame {
	ArrayList<JPanel> update=new ArrayList<JPanel>();
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainDataFrame window = new MainDataFrame();
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
	public MainDataFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 676, 508);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel panel_3 = new JPanel();
		mainPanel.add(panel_3);
		
		JPanel statusPanel = new JPanel();
		frame.getContentPane().add(statusPanel, BorderLayout.NORTH);
		statusPanel.setLayout(new GridLayout(2, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		statusPanel.add(lblNewLabel);
		
		JProgressBar progressBar = new JProgressBar();
		statusPanel.add(progressBar);
		progressBar.setStringPainted(true);
		progressBar.setToolTipText("Signal");
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(10);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.setPreferredSize(new Dimension(120,0));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		panel.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)arg0.getSource();
				for(JPanel i:update){
					i.setPreferredSize(new Dimension(host.getWidth()-20,25));
				}
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)arg0.getSource();
				for(JPanel i:update){
					i.setPreferredSize(new Dimension(host.getWidth()-20,25));
				}
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)arg0.getSource();
				for(JPanel i:update){
					i.setPreferredSize(new Dimension(host.getWidth()-20,25));
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)arg0.getSource();
				for(JPanel i:update){
					i.setPreferredSize(new Dimension(host.getWidth()-20,25));
				}
			}
			
		});
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel title = new JPanel();
		
		panel_3.add(title);
		title.setPreferredSize(new Dimension(538,30));
		
		panel_3.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				title.setPreferredSize(new Dimension(host.getWidth(),30));
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				title.setPreferredSize(new Dimension(host.getWidth(),30));
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				title.setPreferredSize(new Dimension(host.getWidth(),30));
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				JPanel host=(JPanel)e.getSource();
				title.setPreferredSize(new Dimension(host.getWidth(),50));
			}
			
		});
		title.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		title.add(lblNewLabel_1);
		JButton btnNewButton_2 = new JButton("add");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanel panels = new JPanel(new GridLayout(1, 1));
				panel.add(panels);
				panels.add(new JButton("test"+update.size()));
				update.add(panels);
				System.out.println(update);
				panel.repaint();
				panel.setPreferredSize(new Dimension(130,1));
				panel.setPreferredSize(new Dimension(120,0));
				for(JPanel i:update){
					i.setPreferredSize(new Dimension(panel.getWidth()-20,25));
				}
//				frame.setSize(frame.getWidth(), frame.getHeight()+update.get(0).getHeight());
				panel.updateUI();
			}
		});
		title.add(btnNewButton_2);
		panel.setAutoscrolls(true);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanel newPanel=new JPanel();
				JPanel titlePanel=new JPanel();
				JLabel jl=new JLabel("TestPanel");
				titlePanel.add(jl);
//				newPanel.add(comp)
			}
		});
		panel_1.add(btnNewButton);
		
	}
	

}
