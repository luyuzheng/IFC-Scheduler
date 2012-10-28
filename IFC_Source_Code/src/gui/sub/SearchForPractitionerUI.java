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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Practitioner;
import data.Type;
import data.managers.SearchManager;
import data.managers.TypeManager;

public class SearchForPractitionerUI extends JDialog implements ActionListener {
	private static SearchForPractitionerUI searchForPractitionerUI;
	
	private TypeManager tm = new TypeManager();
	private SearchManager sm = new SearchManager();
	
	private static Practitioner p;
	private JTextField searchField = new JTextField();
	private JButton searchButton = new JButton("Search");
	private JButton cancelButton = new JButton("Cancel");
	private JComboBox typeSelector;
	private ArrayList<data.Type> types;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public SearchForPractitionerUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 200));
		setResizable(false);
		
		// Create panels for the search area and the buttons
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0,1));
		JPanel searchPanel = new JPanel(new GridLayout(0, 1));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create drop down box of types of services
		types = tm.getTypeList();
		data.Type general = new data.Type(-1, "View All");
		types.add(0, general);
		typeSelector = new JComboBox(types.toArray());
		typeSelector.setSelectedIndex(0);
		//typeSelector.addActionListener(new BoxListener()); // PROBABLY NEED TO ADD THIS BACK IN LATER!!!
		JLabel typeLabel = new JLabel("Select Practitioner Type:");
		typeLabel.setFont(font);
		typeSelector.setFont(font);
		
		// Add drop down info to the panel
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		add(typeSelectionPanel, BorderLayout.NORTH);
		
		// Create search label to search for a practitioner
		JLabel orLabel = new JLabel("OR");
		JLabel searchLabel = new JLabel("Enter a Practitioner's Name: ");
		
		// Set font for all fields
		orLabel.setFont(font);
		searchLabel.setFont(font);
		searchField.setFont(font);
		
		// Add search info to search panel
		searchPanel.add(orLabel);
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		add(searchPanel, BorderLayout.CENTER);
		
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
	
	public static Practitioner ShowDialog(Component owner) {
		searchForPractitionerUI = new SearchForPractitionerUI("Search for a Practitioner");
		searchForPractitionerUI.pack();
		searchForPractitionerUI.setLocationRelativeTo(owner);
		searchForPractitionerUI.setVisible(true);
		return p;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Search") {
			if (p == null) {
				JLabel errorMessage = new JLabel("Please enter a practitioner's name.");
				errorMessage.setFont(font);
				JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Get search manager to search for practitioners and return results
		} 
		searchForPractitionerUI.setVisible(false);
	}
}