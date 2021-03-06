package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;

/**
 * This class is pop up dialog box that is used to add patients to the waitlist.
 * It allows you to select a patient and service type, as well as add comments.
 */
@SuppressWarnings("serial")
public class AddToWaitlistUI extends JDialog implements ActionListener {
	private static AddToWaitlistUI addToWaitlistUI;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JButton selectPatientButton = new JButton("Select Patient");
	
	private PatientDto patient;
	private JLabel patientLabel;
	private JComboBox<TypeDto> typeCombo;
	private JTextArea commentArea;
	
	private static int change = -1; // -1 if canceled, other is the typeID
	
	private AddToWaitlistUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel(new BorderLayout());
		
		JPanel patientPanel = new JPanel(new BorderLayout());
		patientPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		patientLabel = new JLabel("No Patient Selected", JLabel.CENTER);
		patientLabel.setFont(Constants.HEADER);
		patientPanel.add(patientLabel, BorderLayout.CENTER);
		
		topPanel.add(patientPanel, BorderLayout.NORTH);
		
		JPanel typePanel = new JPanel(new BorderLayout());
		typePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel typeLabel = new JLabel("Select Specific Wait List: ");
		typeLabel.setFont(Constants.DIALOG);
		List<TypeDto> types = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes();
		typeCombo = new JComboBox<TypeDto>(types.toArray(new TypeDto[types.size()]));
		typeCombo.setFont(Constants.DIALOG);
		typePanel.add(typeLabel, BorderLayout.NORTH);
		typePanel.add(typeCombo, BorderLayout.CENTER);
		
		topPanel.add(typePanel, BorderLayout.SOUTH);
		add(topPanel, BorderLayout.NORTH);
		
		JPanel commentPanel = new JPanel(new BorderLayout());
		commentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel commentLabel = new JLabel("Comments: ");
		commentLabel.setFont(Constants.DIALOG);
		commentArea = new JTextArea();
		commentArea.setFont(Constants.DIALOG);
    	commentArea.setLineWrap(true);
    	commentArea.setWrapStyleWord(true);
    	JScrollPane commentPane = new JScrollPane(commentArea);
		commentPanel.add(commentLabel, BorderLayout.NORTH);
		commentPanel.add(commentPane, BorderLayout.CENTER);
		
		add(commentPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		selectPatientButton.addActionListener(this);
		selectPatientButton.setActionCommand("select");
		selectPatientButton.setFont(Constants.DIALOG);
		buttonPanel.add(selectPatientButton, BorderLayout.CENTER);
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		cancelButton.setFont(Constants.DIALOG);
		buttonPanel.add(cancelButton);
		
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(330, 300));
		setResizable(false);
		
	}
    
	/**
	 * This causes this dialog box to pop up in the middle of the specified owner.
	 * This method will return the ID of the added the type ID when OK is clicked or -1 if cancel
	 */
	public static int ShowDialog(Component owner) {
		addToWaitlistUI = new AddToWaitlistUI("Add to Waitlist");
		addToWaitlistUI.pack();
		addToWaitlistUI.setLocationRelativeTo(owner);
		addToWaitlistUI.setVisible(true);
		return change;
	}
	
	public void actionPerformed(ActionEvent e) {
		change = -1;
		JLabel errorMsg = new JLabel();
		errorMsg.setFont(Constants.PARAGRAPH);
		if (e.getActionCommand().equals("ok")) {
			if (patient == null) {
				errorMsg.setText("Please select a patient.");
				JOptionPane.showMessageDialog(this, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} 
			if (typeCombo.getSelectedIndex() < 0 || ((TypeDto)typeCombo.getSelectedItem()).getTypeName().equals("")) {
				errorMsg.setText("Please select a type.");
				JOptionPane.showMessageDialog(this, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			TypeDto type = (TypeDto)typeCombo.getSelectedItem();
			String comment = commentArea.getText().replaceAll("[\r\n]+","\t\t"); 
			List<WaitlistDto> waitlist = DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist();
			for (int i = 0; i < waitlist.size(); i++) {
				// If patient is already on the waitlist, give a warning
				if (waitlist.get(i).getPatientID().intValue() == patient.getPatID().intValue() && 
					waitlist.get(i).getTypeID().intValue() == type.getTypeID().intValue()) {
					errorMsg.setText("This patient is already on the waitlist for this type of service. " +
									 "Do you still wish to continue?");
					int selection = JOptionPane.showConfirmDialog(this, errorMsg, "Please Confirm", JOptionPane.YES_NO_OPTION);
					if (selection == JOptionPane.YES_OPTION) {
						break;
					} else {
						return;
					}
				} 
			}
			DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToWaitlist(patient, type, comment);
            change = type.getTypeID();
		} else if (e.getActionCommand().equals("select")) {
			PatientDto newPatient = SelectPatientUI.ShowDialog(this);
			if (newPatient != null) {
				patient = newPatient;
			}
			if (patient != null) patientLabel.setText(patient.getFirst() + " " + patient.getLast());
			return;
		}
		addToWaitlistUI.setVisible(false);
    }
	
}