package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.PatientDto;

/**
 * Displays the pop up window that allows the user to search for a patient.
 */
@SuppressWarnings("serial")
public class SearchForPatientUI extends JDialog implements ActionListener {
	private static SearchForPatientUI searchForPatientUI;
	
	private static List<PatientDto> ps;
	private JLabel searchLabel;
	private JTextField nameField = new JTextField();
	private JButton searchButton = new JButton("Search");
	private JButton cancelButton = new JButton("Cancel");
	
	/**
	 * Constructor - creates the actual UI for the pop up window.
	 * 
	 * @param name - the title to be displayed at the top bar of the GUI
	 */
	public SearchForPatientUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 220));
		setResizable(false);
		
		// Create panels for the search area and the buttons
		JPanel searchPanel = new JPanel(new GridLayout(0, 1));
		JPanel namesPanel  = new JPanel(new GridLayout(2, 2, 0, 10));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create search labels and set the font for all fields
		searchLabel = new JLabel("Please Enter a Patient's First and/or Last Name: ");
		searchLabel.setFont(Constants.PARAGRAPH);
		nameField.setFont(Constants.PARAGRAPH);
		
		// Add search info to search panel
		namesPanel.add(nameField);
		
		searchPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		searchPanel.add(searchLabel);
		searchPanel.add(namesPanel);
		add(searchPanel, BorderLayout.CENTER);
		
		// Create buttons to search and cancel
		searchButton.addActionListener(this);
		searchButton.setActionCommand("Search");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		
		searchButton.setFont(Constants.DIALOG);
		cancelButton.setFont(Constants.DIALOG);
		
		// Add buttons to button panel
		buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Makes the pop up window visible when the "Search for a Patient" button is clicked.
	 * 
	 * @param owner - the component that owns this pane (the SearchPane)
	 * @return a patient
	 */
	public static List<PatientDto> ShowDialog(Component owner) {
		searchForPatientUI = new SearchForPatientUI("Search for a Patient");
		searchForPatientUI.pack();
		searchForPatientUI.setLocationRelativeTo(owner);
		searchForPatientUI.setVisible(true);
		return ps;
	}
	
	public ArrayList<PatientDto> searchFilter(ArrayList<PatientDto> allPatients, String name) {
	    // Filters patients
		String filter = name.toLowerCase();
		String[] filters = filter.trim().split(" ");
        ArrayList<PatientDto> filteredPat = new ArrayList<PatientDto>();
        for (PatientDto p : allPatients) {
        	String patName = p.getFullName().toLowerCase();
        	
        	for (String f : filters) {
	        	if (patName.indexOf(f) >= 0) {
	        		filteredPat.add(p);
	        		break;
	        	}
        	}
        }
        return filteredPat;
	}
	
	/**
	 * Checks if the Search or Cancel button has been hit. If conducting a search, the program checks to make sure that
	 * input information has been given; otherwise, an error message is sent. The input information is then sent to 
	 * the search manager for processing. Finally, the window is closed.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Search") {
			if ((nameField.getText() == null || nameField.getText().isEmpty())) {
				JLabel errorMessage = new JLabel("Please enter a patient's first and/or last name.");
				errorMessage.setFont(Constants.PARAGRAPH);
				JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Get search manager to search for the patient and return results
			String name = nameField.getText();
			ArrayList<PatientDto> pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
			ArrayList<PatientDto> searchResults = searchFilter(pat, name);
			ps = searchResults;
		} 
		searchForPatientUI.setVisible(false);
	}
}