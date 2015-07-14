package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;
import backend.DataTransferObjects.PractitionerDto;

import gui.main.MainWindow;

@SuppressWarnings("serial")
/** 
 * Dialog window to display appointment details. 
 * Allows editing of appointment notes, confirming appointments, marking patients as no-shows.
 * **/
public class EditAppointmentUI extends JDialog implements ActionListener {
	private static EditAppointmentUI editAppointmentUI;
	
	private JButton editButton = new JButton("Edit Patient");
	private JButton confirmButton = new JButton("Confirm");
	private JButton okButton = new JButton("Save");
	private JButton cancelButton = new JButton("Cancel");
	private JCheckBox noShowsCheckBox = new JCheckBox();
	private JTextPane textPane;
	private JTextPane noteArea;
	private JScrollPane scrollPane;
	private JScrollPane notePane;
	
	private static AppointmentDto appointment;
    private MainWindow window;
	
    /** Creates the dialog box for editing an appointment **/    
	private EditAppointmentUI(String name, AppointmentDto a, MainWindow window) {
		appointment = a;
        this.window = window;
		
        setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(480, 600));
		setResizable(true);
		
		JPanel textPanel = new JPanel(new BorderLayout());
		JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel notePanel = new JPanel(new BorderLayout());
		
		String text = getPatientInfoText(a);
		
		textPane = new JTextPane();
		textPane.setFont(Constants.PARAGRAPH);
		textPane.setContentType("text/html");
		textPane.setText("<html>" + text + "</html>");
		textPane.setEditable(false);
		textPane.setCaretPosition(0);
		textPane.setOpaque(false);
		textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); // Used so that font will display properly
	
		scrollPane = new JScrollPane(textPane);
		TitledBorder infoTitle = BorderFactory.createTitledBorder("Appointment Details");
		infoTitle.setTitleFont(Constants.PARAGRAPH_BOLD);
		scrollPane.setBorder(infoTitle);
		scrollPane.setPreferredSize(new Dimension(480, 250));
		
		JLabel noShowsLabel = new JLabel("No Show");
		noShowsLabel.setFont(Constants.PARAGRAPH);
		if (appointment.isNoShow()) {
			noShowsCheckBox.setSelected(true);
		} else {
			noShowsCheckBox.setSelected(false);
		}

		checkBoxPanel.add(noShowsCheckBox);
		checkBoxPanel.add(noShowsLabel);
		
		textPanel.add(scrollPane, BorderLayout.NORTH);
		textPanel.add(checkBoxPanel, BorderLayout.CENTER);
		
		noteArea = new JTextPane();
		noteArea.setFont(Constants.PARAGRAPH);

		noteArea.setText((a.getNote()).replaceAll("\t\t", "\n"));
		notePane = new JScrollPane(noteArea);
		TitledBorder noteTitle = BorderFactory.createTitledBorder("Appointment Note");
		noteTitle.setTitleFont(Constants.PARAGRAPH_BOLD);
		notePane.setBorder(noteTitle);
		notePane.setPreferredSize(new Dimension(480,200));
		notePanel.add(notePane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		editButton.addActionListener(this);
		editButton.setActionCommand("edit");
		editButton.setFont(Constants.DIALOG);
		buttonPanel.add(editButton);
		
		confirmButton.setAction(changeConfirmationAction);
		isConfirmedValidate();
		confirmButton.setActionCommand("confirm");
		confirmButton.setFont(Constants.DIALOG);
		buttonPanel.add(confirmButton);

		okButton.addActionListener(this);
		okButton.setActionCommand("save");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		
		cancelButton.addActionListener(this); 
		cancelButton.setActionCommand("cancel");
		cancelButton.setFont(Constants.DIALOG);
		buttonPanel.add(cancelButton);
		
		// Add borders to all panels
		textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		notePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		add(textPanel, BorderLayout.NORTH);
		add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
    
	
	public static AppointmentDto ShowDialog(Component owner, AppointmentDto a, MainWindow w) {
		editAppointmentUI = new EditAppointmentUI("Appointment Details", a, w);
		editAppointmentUI.pack();
		editAppointmentUI.setLocationRelativeTo(owner);
		editAppointmentUI.setVisible(true);
		return appointment;
	}
	
	private String getPatientInfoText(AppointmentDto appt) {
		PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appt.getPatientID());
		PractitionerDto pract = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPractitioner(appointment.getPractID());
		
		// Display patient information
		String[] labels = {"Time Slot", "Patient Name", "Phone Number"};
		String[] info = {appointment.prettyPrintStart() + " - " + appointment.prettyPrintEnd(),
						 patient.getFirst() + " " + patient.getLast(),
						 patient.getPhone()
		};
		
		String text = "<table>";
		for (int i = 0; i < labels.length; i++) {
			text += "<tr><td><b>" + labels[i] + ": </b></td><td align='left'>" + info[i] + "</td></tr><br />";
		}
		text += "<tr><td colspan='2'><b>Patient Note: </b>";
		
		if (patient.getNotes().isEmpty()) {
			text += "No Notes to Display";
		} else {
			text += patient.getNotes().replaceAll("\t\t", "\n");
		}
		text += "</td></tr><br />";
		
		// Display practitioner information
		String[] moreLabels = {"Practitioner Name", "Practitioner Type", "Appointment Confirmed"};
		String[] moreInfo = {appointment.getPractName(),
							 pract.getTypeName(),
							 appointment.getConfirmation() ? "Yes" : "No"
		};
		
		for (int i = 0; i < moreLabels.length; i++) {
			text += "<tr><td><b>" + moreLabels[i] + ": </b></td><td align='left'>" + moreInfo[i] + "</td></tr><br />";
		}
		text += "</table>";
		
		return text;
	}
	
	private void refreshPatientInfo(AppointmentDto appt) {
		String text = getPatientInfoText(appt);
		textPane.setText(text);
		textPane.updateUI();
	}
	
	/** Changes the text of the confirm button depending on whether the appointment has been confirmed **/
	public void isConfirmedValidate() {
		if (appointment.getConfirmation()) {
			confirmButton.setText("<html>Unconfirm</html>");
		} else {
			confirmButton.setText("<html>Confirm</html>");
		}
	}
	
	/** Changes the status of the appointment confirmation **/
	private final AbstractAction changeConfirmationAction = new AbstractAction("<html>Confirm</html>") {
		public void actionPerformed(ActionEvent e) {
			if (!appointment.getConfirmation()) {
				appointment.setConfirmation(true);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.confirmAppointment(appointment);
				confirmButton.setText("<html>Unconfirm</html>");
			} else {
				appointment.setConfirmation(false);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.unConfirmAppointment(appointment);
				confirmButton.setText("<html>Confirm</html>");
			}
			refreshPatientInfo(appointment);
		}
	};
	
	/** Saves the information to the databased that was updated in the window **/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			// Saves the noShow status
			if (noShowsCheckBox.isSelected() && !appointment.isNoShow()) {
				DataServiceImpl.GLOBAL_DATA_INSTANCE.checkAsNoShow(appointment);
			} else if (!noShowsCheckBox.isSelected() && appointment.isNoShow()) {
				DataServiceImpl.GLOBAL_DATA_INSTANCE.uncheckAsNoShow(appointment);
			} else {
				// nothing needs to be done
			}
		} else if (e.getActionCommand().equals("edit")) {
			PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appointment.getPatientID());
			PatientDto editedPatient = EditPatientUI.ShowDialog(this, patient);
			DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(editedPatient.getPatID(), appointment);
                        
            refreshPatientInfo(appointment);
			return;
		} else if (e.getActionCommand().equals("cancel")) {
			editAppointmentUI.setVisible(false);
			return;
		}
        
		appointment.setNote((noteArea.getText()).replaceAll("[\r\n]+", "\t\t"));
        DataServiceImpl.GLOBAL_DATA_INSTANCE.addNotesToAppointment(appointment);
        editAppointmentUI.setVisible(false);
        window.refreshConfirmationPane();
        window.refreshAppointments(window.getCurrentDay().getDate());
    }
}