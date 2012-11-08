package gui.main.listeners;

import gui.main.WaitListPane;
import gui.main.AppointmentConfirmationPane.AppointmentConfirmationTableModel;
import gui.sub.DisplayWaitingPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import data.Appointment;

public class AppointmentConfirmationListener extends MouseAdapter {
	JTable owner;
	Component parent;
	
	public AppointmentConfirmationListener(JTable owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	public void mouseClicked(MouseEvent e) {
		// Looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				Appointment appt = ((AppointmentConfirmationTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
				//DisplayAppointmentConfirmationUI.ShowDialog(parent.getParent(), appt);
				//AppointmentConfirmationManager acm = new AppointmentConfirmationManager();
				//acm.updateAppointmentConfirmation(appt);
				//AppointmentConfirmationPane acp = (AppointmentConfirmationPane) parent;
				//acp.resetModel();
			}
		}
	}
	
}