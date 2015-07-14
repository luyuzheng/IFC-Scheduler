package gui.sub;

import backend.DataService.DataServiceImpl;
import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import backend.DataTransferObjects.PatientDto;

/**
 * Pop-up dialog for the editing of a single patient.
 */
@SuppressWarnings("serial")
public class EditPatientUI extends JDialog implements ActionListener {
	private static EditPatientUI editPatientUI;
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JTextArea note = new JTextArea();
	private JTextField areaCodeField = new JTextField();
	private JTextField numberPart1Field = new JTextField();
	private JTextField numberPart2Field = new JTextField();
	private JLabel numberOfNoShows = new JLabel();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	private static PatientDto p;
	
	/** Constructs a dialog box to edit a patient, given their name as a string **/
	private EditPatientUI(String name) {
		setModal(true);
		setTitle(name);
		
		firstNameField.setText(p.getFirst());
		firstNameField.setFont(Constants.PARAGRAPH);
		
		lastNameField.setText(p.getLast());
		lastNameField.setFont(Constants.PARAGRAPH);
		
		// sets the phone fields
		if(p.getPhone() != null){
			String phone= p.getPhone();
			phone = phone.replaceAll("-| |\\)|\\(", "");
			
			areaCodeField.setText(phone.substring(0,3));
			areaCodeField.setFont(Constants.PARAGRAPH);
			
			numberPart1Field.setText(phone.substring(3,6));
			numberPart1Field.setFont(Constants.PARAGRAPH);
			
			numberPart2Field.setText(phone.substring(6,phone.length()));
			numberPart2Field.setFont(Constants.PARAGRAPH);
		}
		note.setText((p.getNotes()).replaceAll("\t\t", "\n")) ;
		
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel input = new JPanel(new GridLayout(0,1));
    	
    	// builds the first name field
    	JPanel fName = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("First Name: ");
    	label.setFont(Constants.PARAGRAPH);
    	fName.add(label, BorderLayout.NORTH);
    	firstNameField.setColumns(15);
    	fName.add(firstNameField, BorderLayout.CENTER);
    	
    	// builds the last name field
    	JPanel lName = new JPanel(new BorderLayout());
    	label = new JLabel("Last Name: ");
    	label.setFont(Constants.PARAGRAPH);
    	lName.add(label, BorderLayout.NORTH);
    	lastNameField.setColumns(15);
    	lName.add(lastNameField, BorderLayout.CENTER);
    	
    	JPanel names = new JPanel(new FlowLayout());
    	names.add(fName);
    	names.add(lName);
    	
    	input.add(names);
    	
    	// builds the phone number fields
    	JPanel num = new JPanel(new BorderLayout());
    	label = new JLabel("Phone Number: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	num.add(label, BorderLayout.NORTH);
    	JPanel phonePanel = new JPanel(new FlowLayout());
    	areaCodeField.setColumns(5);
    	numberPart1Field.setColumns(5);
    	numberPart2Field.setColumns(7);
    	phonePanel.add(areaCodeField);
    	phonePanel.add(numberPart1Field);
    	phonePanel.add(numberPart2Field);
    	num.add(phonePanel, BorderLayout.CENTER);
    	input.add(num);
    	
    	// builds the no shows panel
    	JPanel noShowsPanel = new JPanel(new BorderLayout());
    	numberOfNoShows.setText("Number of no shows: " + p.getNoShows());
    	numberOfNoShows.setFont(Constants.PARAGRAPH);
    	numberOfNoShows.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    	numberOfNoShows.setHorizontalTextPosition(JLabel.CENTER);
    	noShowsPanel.add(numberOfNoShows);
    	input.add(noShowsPanel);
    	
    	// builds the notes panel
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Patient notes: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	notePanel.add(label, BorderLayout.NORTH);
    	note.setFont(Constants.PARAGRAPH);
    	note.setLineWrap(true);
    	note.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(note);
    	notePane.setPreferredSize(new Dimension(175,100));    	
    	notePanel.add(notePane, BorderLayout.CENTER);
    		
    	// builds the button panel
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	cancelButton.setActionCommand("cancel");
    	cancelButton.addActionListener(this);
    	cancelButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(input, BorderLayout.NORTH);
    	panel.add(notePanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	add(panel, BorderLayout.CENTER);		
		
		setResizable(true);
		
	}
    
	/** Sets the dialog as visible **/
	public static PatientDto ShowDialog(Component owner, PatientDto pat) {
		p = pat;
		editPatientUI = new EditPatientUI("Edit Patient");
		editPatientUI.pack();
		editPatientUI.setLocationRelativeTo(owner);
		editPatientUI.setVisible(true);
		return p;
	}
	
	/** Allows for editing of patient fields **/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			editPatientUI.setVisible(false);
			return;
		}
		
		JLabel msg = new JLabel();
		msg.setFont(Constants.PARAGRAPH);
		
		// edit first name
		String firstName = firstNameField.getText();
		if (firstName.equals("")) {
			msg.setText("Please enter a first name.");
			JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// edit last name
		String lastName = lastNameField.getText();
		if (lastName.equals("")) {
			msg.setText( "Please enter a last name.");
			JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// edit phone number
		String areaCode = areaCodeField.getText();
		String numberPart1 = numberPart1Field.getText();
		String numberPart2 = numberPart2Field.getText();
		boolean blank = false;
		if (areaCode.equals("") && numberPart1.equals("") && numberPart2.equals("")) {
			blank = true;
			msg.setText("The Phone Number field is blank. Would you like to continue?");
			if (JOptionPane.showConfirmDialog(this, msg, "Missing Phone Number", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
		}
		String num;
		try {
			if (blank) num = null;
			else if (areaCode.length() != 3 || numberPart1.length() != 3 || numberPart2.length() != 4) {
				msg.setText("Please enter a valid phone number (###-###-####) or leave the field blank.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				num = areaCode + "-" + numberPart1 + "-"+ numberPart2;
			}
		} catch (Exception ex) {
			msg.setText("Please enter a valid phone number (###-###-####) or leave the field blank.");
			JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// edit the patient notes
		String noteText = note.getText().replaceAll("[\r\n]+", "\t\t");
		
		p.setFirst(firstName);
		p.setLast(lastName);
		p.setNotes(noteText);
		p.setPhone(num);
		DataServiceImpl.GLOBAL_DATA_INSTANCE.updatePatient(p);
		
		
		editPatientUI.setVisible(false);
    }
	
}
