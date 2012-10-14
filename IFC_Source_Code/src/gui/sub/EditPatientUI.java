package gui.sub;

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

import data.Patient;
import data.PhoneNumber;
import data.managers.PatientManager;

public class EditPatientUI extends JDialog implements ActionListener {
	private static EditPatientUI editPatientUI;
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JTextArea note = new JTextArea();
	private JTextField areaCodeField = new JTextField();
	private JTextField numberPart1Field = new JTextField();
	private JTextField numberPart2Field = new JTextField();
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	
	private static Patient p;
	
	private EditPatientUI(String name) {
		setModal(true);
		setTitle(name);
		
		firstNameField.setText(p.getFirstName());
		lastNameField.setText(p.getLastName());
		if(p.getNumberString()!=""){
			String[] number = p.getNumberString().split("-");
			areaCodeField.setText(number[0]);
			numberPart1Field.setText(number[1]);
			numberPart2Field.setText(number[2]);
		}
		note.setText((p.getNote()).replaceAll("\t\t", "\n")) ;
		
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel input = new JPanel(new GridLayout(0,1));
    	JPanel fName = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("First Name: ");
    	fName.add(label, BorderLayout.NORTH);
    	firstNameField.setColumns(15);
    	fName.add(firstNameField, BorderLayout.CENTER);
    	
    	JPanel lName = new JPanel(new BorderLayout());
    	label = new JLabel("Last Name: ");
    	lName.add(label, BorderLayout.NORTH);
    	lastNameField.setColumns(15);
    	lName.add(lastNameField, BorderLayout.CENTER);
    	
    	JPanel names = new JPanel(new FlowLayout());
    	names.add(fName);
    	names.add(lName);
    	
    	input.add(names);
    	
    	JPanel num = new JPanel(new BorderLayout());
    	label = new JLabel("Phone Number: ", JLabel.CENTER);
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
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Patient notes: ", JLabel.CENTER);
    	notePanel.add(label, BorderLayout.NORTH);
    	note.setFont(phonePanel.getFont());
    	//note.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    	note.setLineWrap(true);
    	note.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(note);
    	notePane.setPreferredSize(new Dimension(175,100)); //Added by Aakash    	
    	notePanel.add(notePane, BorderLayout.CENTER);
    		
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("okNew");
    	okButton.addActionListener(this);
    	cancelButton.addActionListener(this);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(input, BorderLayout.NORTH);
    	panel.add(notePanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	add(panel, BorderLayout.CENTER);		
		
		setResizable(false);
		
	}
    
	
	public static Patient ShowDialog(Component owner, Patient pat) {
		p = pat;
		editPatientUI = new EditPatientUI("Edit Patient");
		editPatientUI.pack();
		editPatientUI.setLocationRelativeTo(owner);
		editPatientUI.setVisible(true);
		return p;
	}
	
	public void actionPerformed(ActionEvent e) {
		String firstName = firstNameField.getText();
		if (firstName.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a first name.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String lastName = lastNameField.getText();
		if (lastName.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a last name.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String areaCode = areaCodeField.getText();
		String numberPart1 = numberPart1Field.getText();
		String numberPart2 = numberPart2Field.getText();
		boolean blank = false;
		if (areaCode.equals("") && numberPart1.equals("") && numberPart2.equals("")) {
			blank = true;
			if (JOptionPane.showConfirmDialog(this, "The Phone Number field is blank. Would you like to continue?", "Missing Phone Number", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
		}
		PhoneNumber num;
		try {
			if (blank) num = null;
			else if (areaCode.length() != 3 || numberPart1.length() != 3 || numberPart2.length() != 4) {
				JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
//				int a = Integer.parseInt(areaCode);
//				int p1 = Integer.parseInt(numberPart1);
//				int p2 = Integer.parseInt(numberPart2);
//				num = new PhoneNumber(a, p1, p2);
					num = new PhoneNumber(areaCode, numberPart1, numberPart2);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String noteText = note.getText().replaceAll("[\r\n]+", "\t\t");
		
		PatientManager pm = new PatientManager();
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setNote(noteText);
		p.setNumber(num);
		pm.updatePatient(p);
		
		
		editPatientUI.setVisible(false);
    }
	
}
