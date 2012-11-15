package gui.main.listeners;

import gui.main.AppointmentConfirmationPane;
import gui.main.AppointmentConfirmationPane.AppointmentConfirmationTableModel;
import gui.sub.DisplayAppointmentConfirmationUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;

/**
 * Waits for mouse clicks on patients in appointment confirmation table. If a patient is double clicked,
 * a pop up window displays more information about that patient and his/her appointment.
 */
public class AppointmentConfirmationListener extends MouseAdapter {
	JTable owner;
	Component parent;
	
	/**
	 * Constructor.
	 * 
	 * @param owner - component that owns this listener (the table of the AppointmentConfirmationPane)
	 * @param parent - the parent of this listener (AppointmentConfirmationPane)
	 */
	public AppointmentConfirmationListener(JTable owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	/**
	 * Looks for double click events in order to display the pop up window with more patient and appointment information.
	 */
	public void mouseClicked(MouseEvent e) {
		// Looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				AppointmentDto appt = ((AppointmentConfirmationTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
				DisplayAppointmentConfirmationUI.ShowDialog(parent.getParent(), appt);
				//TODO: Need a method to update appointments!
				//AppointmentConfirmationManager acm = new AppointmentConfirmationManager();
				//acm.updateAppointmentConfirmation(appt);
				AppointmentConfirmationPane acp = (AppointmentConfirmationPane) parent;
				acp.resetModel();
			}
		}
	}
	
}