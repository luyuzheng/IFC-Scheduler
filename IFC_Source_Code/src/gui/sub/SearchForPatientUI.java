package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import data.Patient;
import data.managers.SearchManager;

public class SearchForPatientUI extends JDialog implements ActionListener {
	private static SearchForPatientUI searchForPatientUI;
	
	private SearchManager sm = new SearchManager();
	
	private static Patient p;
	private JLabel searchLabel;
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JButton searchButton = new JButton("Search");
	private JButton cancelButton = new JButton("Cancel");
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public SearchForPatientUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(350, 220));
		setResizable(false);
		
		// Create panels for the search area and the buttons
		JPanel searchPanel = new JPanel(new GridLayout(0, 1));
		JPanel namesPanel  = new JPanel(new GridLayout(2, 2, 0, 10));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create search labels and set the font for all fields
		searchLabel = new JLabel("Enter a Patient's Name: ");
		firstNameLabel = new JLabel("First Name: ");
		lastNameLabel = new JLabel("Last Name: ");
		searchLabel.setFont(font);
		firstNameLabel.setFont(font);
		lastNameLabel.setFont(font);
		firstNameField.setFont(font);
		lastNameField.setFont(font);
		
		// Add search info to search panel
		namesPanel.add(firstNameLabel);
		namesPanel.add(firstNameField);
		namesPanel.add(lastNameLabel);
		namesPanel.add(lastNameField);
		
		searchPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		searchPanel.add(searchLabel);
		searchPanel.add(namesPanel);
		add(searchPanel, BorderLayout.CENTER);
		
		// Create buttons to search and cancel
		searchButton.addActionListener(this);
		searchButton.setActionCommand("Search");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		
		searchButton.setFont(font);
		cancelButton.setFont(font);
		
		// Add buttons to button panel
		buttonPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static Patient ShowDialog(Component owner) {
		searchForPatientUI = new SearchForPatientUI("Search for a Patient");
		searchForPatientUI.pack();
		searchForPatientUI.setLocationRelativeTo(owner);
		searchForPatientUI.setVisible(true);
		return p;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Search") {
			if (p == null) {
				JLabel errorMessage = new JLabel("Please enter a patient's name.");
				errorMessage.setFont(font);
				JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Get search manager to search for the patient and return results
		} 
		searchForPatientUI.setVisible(false);
	}
}