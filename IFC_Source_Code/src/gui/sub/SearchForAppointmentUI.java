package gui.sub;

import gui.main.WaitListPane.BoxListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Appointment;
import data.Type;
import data.managers.SearchManager;
import data.managers.TypeManager;

public class SearchForAppointmentUI extends JDialog implements ActionListener {
	private static SearchForAppointmentUI searchForAppointmentUI;
	
	private TypeManager tm = new TypeManager();
	private SearchManager sm = new SearchManager();
	
	private static Appointment a;
	private JTextField apptLengthField = new JTextField();
	private JCheckBox monday = new JCheckBox("Mon");
	private JCheckBox tuesday = new JCheckBox("Tues");
	private JCheckBox wednesday = new JCheckBox("Wed");
	private JCheckBox thursday = new JCheckBox("Thurs");
	private JCheckBox friday = new JCheckBox("Fri");
	private JButton searchButton = new JButton("Search");
	private JButton cancelButton = new JButton("Cancel");
	private JComboBox typeSelector;
	private ArrayList<data.Type> types;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public SearchForAppointmentUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(350, 250));
		setResizable(false);
		
		// Create panels for the search area and the buttons
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0,1));
		JPanel daysOfTheWeekPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create drop down box of types of services
		types = tm.getTypeList();
		data.Type general = new data.Type(-1, "View All");
		types.add(0, general);
		typeSelector = new JComboBox(types.toArray());
		typeSelector.setSelectedIndex(0);
		//typeSelector.addActionListener(new BoxListener()); // PROBABLY NEED TO ADD THIS BACK IN LATER!!!
		JLabel typeLabel = new JLabel("Select Type of Service:");
		typeLabel.setFont(font);
		typeSelector.setFont(font);
		
		// Create search labels
		JLabel apptLengthLabel = new JLabel("Appointment Length (in minutes): ");
		JLabel daysOfTheWeekLabel = new JLabel("Select Days of the Week: ");
		
		// Set font for all fields
		apptLengthLabel.setFont(font);
		daysOfTheWeekLabel.setFont(font);
		apptLengthField.setFont(font);
		
		// Add drop down info to the panel
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		typeSelectionPanel.add(apptLengthLabel);
		typeSelectionPanel.add(apptLengthField);
		typeSelectionPanel.add(daysOfTheWeekLabel);
		add(typeSelectionPanel, BorderLayout.NORTH);
		
		
		// Create checkboxes for days of the week and add to panel
		monday.setFont(font);
		tuesday.setFont(font);
		wednesday.setFont(font);
		thursday.setFont(font);
		friday.setFont(font);
		
		daysOfTheWeekPanel.add(monday);
		daysOfTheWeekPanel.add(tuesday);
		daysOfTheWeekPanel.add(wednesday);
		daysOfTheWeekPanel.add(thursday);
		daysOfTheWeekPanel.add(friday);
		add(daysOfTheWeekPanel, BorderLayout.CENTER);
		
		// Create buttons to search and cancel
		searchButton.addActionListener(this);
		searchButton.setActionCommand("Search");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		
		searchButton.setFont(font);
		cancelButton.setFont(font);
		
		// Add buttons to button panel
		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static Appointment ShowDialog(Component owner) {
		searchForAppointmentUI = new SearchForAppointmentUI("Search for a Practitioner");
		searchForAppointmentUI.pack();
		searchForAppointmentUI.setLocationRelativeTo(owner);
		searchForAppointmentUI.setVisible(true);
		return a;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Search") {
			if (a == null) {
				JLabel errorMessage = new JLabel("Please enter a practitioner's name.");
				errorMessage.setFont(font);
				JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Get search manager to search for practitioners and return results
		} 
		searchForAppointmentUI.setVisible(false);
	}
}