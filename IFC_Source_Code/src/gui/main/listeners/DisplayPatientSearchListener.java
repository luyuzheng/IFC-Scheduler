package gui.main.listeners;

import gui.sub.DisplayAppointmentConfirmationUI;
import gui.sub.DisplayPatientSearchUI;
import gui.sub.DisplayPatientSearchUI.FutureAppointmentsTableModel;

import gui.main.MainWindow;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import backend.DataTransferObjects.*;

/**
 * Waits for mouse clicks on patients in appointment confirmation table. If a patient is double clicked,
 * a pop up window displays more information about that patient and his/her appointment.
 */
public class DisplayPatientSearchListener extends MouseAdapter {
	private JTable owner;
	private Component parent;
	private MainWindow main;
        
	/**
	 * Constructor.
	 * 
	 * @param owner - component that owns this listener (the table of the AppointmentConfirmationPane)
	 * @param parent - the parent of this listener (AppointmentConfirmationPane)
	 */
	public DisplayPatientSearchListener(JTable owner, Component parent, MainWindow main) {
		this.owner = owner;
		this.parent = parent;
                this.main = main;
	}
	
	/**
	 * Looks for click events in order to display the pop up window with more patient and appointment information
	 * and to change the button label depending on whether the appointment is confirmed or unconfirmed.
	 */
	public void mouseClicked(MouseEvent e) {
		// Looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				AppointmentDto appt = ((FutureAppointmentsTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
				DisplayAppointmentConfirmationUI.ShowDialog(parent.getParent(), appt, main);
				DisplayPatientSearchUI dps = (DisplayPatientSearchUI) parent;
				dps.resetModel();
			}
		}
	}
}