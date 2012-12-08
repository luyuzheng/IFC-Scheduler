package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

/**
 * DisplayPatientSearchUI shows information about a patient when a patient in the search table
 * is clicked.
 */
public class DisplayPatientSearchUI extends JDialog implements ActionListener {
	private static DisplayPatientSearchUI displayPatientSearchUI;
	
	private JButton okButton = new JButton("OK");
	private JTextArea textArea;
	
	/**
	 * Constructor - creates the actual UI to display the patient information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param pat - the patient information to be displayed
	 */
	private DisplayPatientSearchUI(String name, PatientDto pat) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 200));
		
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = "Patient Name: " + pat.getFirst() + " " + pat.getLast() +
				  	  "\nPhone Number: " + pat.getPhone() +
				  	  "\nComments: " + pat.getNotes() +
				  	  "\nNumber of No Shows: " + pat.getNoShows() +
				  	  "\n";
					  // Add info about the patient's appointment
					  // Date
					  // Appt Time
					  // Confirmed
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		infoPanel.add(textArea);

		okButton.setFont(Constants.DIALOG);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		
		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}

	/**
	 * Makes the pop up window visible when a patient from the table is selected.
	 * 
	 * @param owner - the component that owns this pane (the SearchListener)
	 * @param appt  - the appointment information
	 */
	public static void ShowDialog(Component owner, PatientDto pat) {
		displayPatientSearchUI = new DisplayPatientSearchUI("View Patient", pat);
		displayPatientSearchUI.pack();
		displayPatientSearchUI.setLocationRelativeTo(owner);
		displayPatientSearchUI.setVisible(true);
	}
	
	/**
	 * Closes the window once the user hits the "OK" button.
	 */
	public void actionPerformed(ActionEvent arg0) {
		displayPatientSearchUI.setVisible(false);
	}
}