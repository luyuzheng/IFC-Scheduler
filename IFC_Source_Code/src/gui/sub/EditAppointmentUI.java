package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.Appointment;
import data.Patient;

public class EditAppointmentUI extends JDialog implements ActionListener {
	private static EditAppointmentUI editAppointmentUI;
	
	private JButton okButton = new JButton("Ok");
	private JButton editButton = new JButton("Edit Patient");
	private JButton changeButton = new JButton("Change Patient");
	private JButton clearButton = new JButton("Clear");
	private JButton cancelButton = new JButton("Cancel");
	private JTextArea textArea;
	private JTextArea noteArea;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	private static Appointment appointment;
	
	private EditAppointmentUI(String name, Appointment a) {
		appointment = a;
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());


		String text = "Time Slot: " + a.getTimeSlot().toString();
		text += "\nPatient Name: " + a.getPatient().getFullName();
		text += "\nPhone Number: " + a.getPatient().getNumberString();
		text += "\nPatient Note: " + (a.getPatient().getNote()).replaceAll("\t\t", "\n");

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(font);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		
		JPanel notePanel = new JPanel(new BorderLayout());
		JLabel noteLabel = new JLabel("Appointment Note:");
		JScrollPane notePane = new JScrollPane();
		notePane.setPreferredSize(new Dimension(200,200));
		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		noteArea.setWrapStyleWord(true);
		noteArea.setFont(font);
		noteArea.setText((a.getNote()).replaceAll("\t\t", "\n"));
		notePane.setViewportView(noteArea);
		notePanel.add(noteLabel, BorderLayout.NORTH);
		notePanel.add(notePane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(font);
		buttonPanel.add(okButton);
		changeButton.addActionListener(this);
		changeButton.setActionCommand("change");
		changeButton.setFont(font);
		buttonPanel.add(changeButton);
		editButton.addActionListener(this);
		editButton.setActionCommand("edit");
		editButton.setFont(font);
		buttonPanel.add(editButton);
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		clearButton.setFont(font);
		buttonPanel.add(clearButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		cancelButton.setFont(font);
		buttonPanel.add(cancelButton);
		
		add(textArea, BorderLayout.NORTH);
		add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}
    
	
	public static Appointment ShowDialog(Component owner, Appointment a) {
		editAppointmentUI = new EditAppointmentUI("Appointment Details", a);
		editAppointmentUI.pack();
		editAppointmentUI.setLocationRelativeTo(owner);
		editAppointmentUI.setVisible(true);
		return appointment;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("change")) {
			Patient newPatient = SelectPatientUI.ShowDialog(this);
			if (newPatient != null) {
				appointment.setPatient(newPatient);
			} else return;
		} else if (e.getActionCommand().equals("edit")) {
			appointment.setPatient(EditPatientUI.ShowDialog(this, appointment.getPatient()));
		}else if (e.getActionCommand().equals("clear")) {
			appointment.setPatient(null);
			appointment.setNote("");
		} else if (e.getActionCommand().equals("cancel")) {
			editAppointmentUI.setVisible(false);
			return;
		}
		appointment.setNote((noteArea.getText()).replaceAll("[\r\n]+", "\t\t"));
		editAppointmentUI.setVisible(false);
		
    }
	
}