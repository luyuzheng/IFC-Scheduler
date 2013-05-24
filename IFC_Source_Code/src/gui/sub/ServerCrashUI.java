package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ServerCrashUI extends JDialog {
	private static ServerCrashUI serverCrashUI;

	private JButton okButton = new JButton("OK");
	
	private String crashDescriptionString = "The MySql or XAMPP server may not be running properly. " +
			"Please contact the system administrator about restarting the MySql server.";
	
	private ServerCrashUI() {
		setModal(true);
		setTitle("Save Crash Report");

		setPreferredSize(new Dimension(450,200));
		setLayout(new BorderLayout());
		
		JPanel reportPanel = new JPanel();
		reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
		reportPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		JLabel description = new JLabel("<html>" + crashDescriptionString + "</html>");
		description.setFont(Constants.PARAGRAPH);
		descriptionPanel.add(description, BorderLayout.CENTER);
		reportPanel.add(descriptionPanel);
		add(reportPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		okButton.addActionListener(close);
		okButton.setActionCommand("OK");
		okButton.setFont(Constants.DIALOG);
		buttonsPanel.add(okButton);
		
		add(buttonsPanel, BorderLayout.PAGE_END);
	}
	
	public static int ShowDialog() {
		serverCrashUI = new ServerCrashUI();
		serverCrashUI.pack();
		serverCrashUI.setLocationByPlatform(true);
		serverCrashUI.setVisible(true);
		return 0;
	}
	
	private ActionListener close = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			serverCrashUI.setVisible(false);
		}
	};
}
