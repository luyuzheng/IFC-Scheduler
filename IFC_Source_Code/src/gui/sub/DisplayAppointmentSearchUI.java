package gui.sub;

import gui.Constants;
import gui.DateTimeUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PractitionerDto;

/**
 * DisplayAppointmentSearchUI shows information about an appointment when an appointment in the table
 * is clicked.
 */
@SuppressWarnings("serial")
public class DisplayAppointmentSearchUI extends JDialog implements ActionListener {
	private static DisplayAppointmentSearchUI displayAppointmentSearchUI;
	
	private JButton okButton = new JButton("OK");
	private JTextPane textPane;
	private JScrollPane scrollPane;
	
	/**
	 * Constructor - creates the actual UI to display appointment information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param appt - the appointment information to be displayed
	 */
	private DisplayAppointmentSearchUI(String name, AppointmentDto appt) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(380, 280));

		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		PractitionerDto pract = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPractitioner(appt.getPractID());
		
		String text = getApptSearchText(pract, appt);
		
		textPane = new JTextPane();
		textPane.setFont(Constants.PARAGRAPH);
		textPane.setContentType("text/html");
		textPane.setText("<html>" + text + "</html>");
		textPane.setEditable(false);
		textPane.setCaretPosition(0);
		textPane.setOpaque(false);
		textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); // Used so that font will display properly
	
		scrollPane = new JScrollPane(textPane);
		TitledBorder title = BorderFactory.createTitledBorder("Practitioner/Appointment Information");
		title.setTitleFont(Constants.PARAGRAPH_BOLD);
		scrollPane.setBorder(title);
		scrollPane.setPreferredSize(new Dimension(380, 250));
		infoPanel.add(scrollPane);

		okButton.setFont(Constants.DIALOG);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		
		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(true);
	}
	
	private String getApptSearchText(PractitionerDto pract, AppointmentDto appt) {
		String date = DateTimeUtils.prettyPrintMonthDay(appt.getApptDate());
				
		String[] labels = {"Date", "Time", "Practitioner Name", "Practitioner Type", "Appointment Length"};
		String[] info = {date, 
						 appt.prettyPrintStart() + " - " + appt.prettyPrintEnd(), 
						 appt.getPractName(),
						 pract.getTypeName(),
						 (appt.getEnd() - appt.getStart()) + "",
						 };
		
		String text = "<table>";
		for (int i = 0; i < labels.length; i++) {
			text += "<tr><td><b>" + labels[i] + ": </b></td><td align='left'>" + info[i] + "</td></tr><br />";
		}
		text += "</table>";

		return text;		
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