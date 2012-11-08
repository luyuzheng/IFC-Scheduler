package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import data.Appointment;

public class DisplayAppointmentConfirmationUI extends JDialog implements ActionListener {
	private static DisplayAppointmentConfirmationUI displayAppointmentConfirmationUI;
	
	private JButton okButton = new JButton("OK");
	private JTextArea textArea;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	private DisplayAppointmentConfirmationUI(String name, Appointment appt) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = "Date: " + appt.getDate().toFormalString() + "\n" +
					  "Patient Name: " + appt.getPatient().getFullName() +
					  "Phone Number: " + appt.getPatient().getNumber() +
					  "Confirmed: " + appt.getConfirmed();
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(font);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		infoPanel.add(textArea);

		okButton.setFont(font);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		
		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
	}
	
	public static void ShowDialog(Component owner, Appointment appt) {
		displayAppointmentConfirmationUI = new DisplayAppointmentConfirmationUI("View Appointment", appt);
		displayAppointmentConfirmationUI.pack();
		displayAppointmentConfirmationUI.setLocationRelativeTo(owner);
		displayAppointmentConfirmationUI.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		displayAppointmentConfirmationUI.setVisible(false);
	}
	
}