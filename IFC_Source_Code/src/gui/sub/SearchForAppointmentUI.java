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
import javax.swing.border.EmptyBorder;

import data.Appointment;
import data.Type;
import data.managers.SearchManager;
import data.managers.TypeManager;

/**
 * Displays the pop up window that allows the user to search for the next available appointment.
 */
public class SearchForAppointmentUI extends JDialog implements ActionListener {
	private static SearchForAppointmentUI searchForAppointmentUI;
	
	private TypeManager tm = new TypeManager();
	private SearchManager sm = new SearchManager();
	
	private static Appointment a;
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
	
	/**
	 * Constructor - creates the actual UI for the pop up window.
	 * 
	 * @param name - the title to be displayed at the top bar of the GUI
	 */
	public SearchForAppointmentUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(350, 250));
		setResizable(false);
		
		// Create panels for the search area and the buttons
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0, 1));
		JPanel daysOfTheWeekPanel = new JPanel(new GridLayout(0, 1));
		JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create drop down box of types of services
		types = tm.getTypeList();
		data.Type general = new data.Type(-1, "View All");
		types.add(0, general);
		typeSelector = new JComboBox(types.toArray());
		typeSelector.setSelectedIndex(0);
		//typeSelector.addActionListener(new BoxListener()); // PROBABLY NEED TO ADD THIS BACK IN LATER!!!
		JLabel typeLabel = new JLabel("Select Type of Service:");
		
		// Set font for fields
		typeLabel.setFont(font);
		typeSelector.setFont(font);

		
		// Add drop down info to the panel
		typeSelectionPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		add(typeSelectionPanel, BorderLayout.NORTH);
		
		
		// Create label for days of the week
		JLabel daysOfTheWeekLabel = new JLabel("Select Days of the Week: ");
		
		// Create checkboxes for days of the week and add to panel
		monday.setSelected(true);
		tuesday.setSelected(true);
		wednesday.setSelected(true);
		thursday.setSelected(true);
		friday.setSelected(true);
		
		// Set fonts for days of the week
		daysOfTheWeekLabel.setFont(font);
		monday.setFont(font);
		tuesday.setFont(font);
		wednesday.setFont(font);
		thursday.setFont(font);
		friday.setFont(font);

		checkboxPanel.add(monday);
		checkboxPanel.add(tuesday);
		checkboxPanel.add(wednesday);
		checkboxPanel.add(thursday);
		checkboxPanel.add(friday);
		
		daysOfTheWeekPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		daysOfTheWeekPanel.add(daysOfTheWeekLabel);
		daysOfTheWeekPanel.add(checkboxPanel);
		add(daysOfTheWeekPanel, BorderLayout.CENTER);
		
		// Create buttons to search and cancel
		searchButton.addActionListener(this);
		searchButton.setActionCommand("Search");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		
		searchButton.setFont(font);
		cancelButton.setFont(font);
		
		// Add buttons to button panel
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Makes the pop up window visible when the "Search for Next Available Appointment" button is clicked.
	 * 
	 * @param owner - the component that owns this pane (the SearchPane)
	 * @return an appointment
	 */
	public static Appointment ShowDialog(Component owner) {
		searchForAppointmentUI = new SearchForAppointmentUI("Search for Next Available Appointment");
		searchForAppointmentUI.pack();
		searchForAppointmentUI.setLocationRelativeTo(owner);
		searchForAppointmentUI.setVisible(true);
		return a; // SHOULD IT ACTUALLY BE RETURNING AN ARRAYLIST OF APPOINTMENTS???
	}
	
	/**
	 * Checks if the Search or Cancel button has been hit. If conducting a search, the input information is sent to 
	 * the search manager for processing. The window is then closed.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Search") {
			/*if (a == null) {
				JLabel errorMessage = new JLabel("Please enter a practitioner's name.");
				errorMessage.setFont(font);
				JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}*/
			// Get search manager to search for appointments and return results!!!
		} 
		searchForAppointmentUI.setVisible(false);
	}
}