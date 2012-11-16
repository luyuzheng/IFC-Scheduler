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

import backend.DataTransferObjects.AppointmentDto;

/**
 * DisplayAppointmentSearchUI shows information about an appointment when an appointment in the table
 * is clicked.
 */
public class DisplayAppointmentSearchUI extends JDialog implements ActionListener {
	private static DisplayAppointmentSearchUI displayAppointmentSearchUI;
	
	private JButton okButton = new JButton("OK");
	private JTextArea textArea;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	/**
	 * Constructor - creates the actual UI to display appointment information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param appt - the appointment information to be displayed
	 */
	private DisplayAppointmentSearchUI(String name, AppointmentDto appt) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = "Date: " + appt.getApptDate().toString() + "\n" +
				      "Time: " + appt.getStart() + " - " + appt.getEnd();

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
	
	/**
	 * Makes the pop up window visible when a patient from the table is selected.
	 * 
	 * @param owner - the component that owns this pane (the SearchListener)
	 * @param appt  - the appointment information
	 */
	public static void ShowDialog(Component owner, AppointmentDto appt) {
		displayAppointmentSearchUI = new DisplayAppointmentSearchUI("View Appointment", appt);
		displayAppointmentSearchUI.pack();
		displayAppointmentSearchUI.setLocationRelativeTo(owner);
		displayAppointmentSearchUI.setVisible(true);
	}

	/**
	 * Closes the window once the user hits the "OK" button.
	 */
	public void actionPerformed(ActionEvent e) {
		displayAppointmentSearchUI.setVisible(false);
	}
}