/**
 * This listener handles mouse events that are supposed to open 
 * the Select Patient dialog. 
 */

package gui.main.listeners;

import gui.main.AppointmentBlock;
import gui.sub.EditAppointmentUI;
import gui.sub.SelectPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import data.Appointment;

public class NewPatientListener extends MouseAdapter {
	
	AppointmentBlock owner;
	Component parent;
	
	public NewPatientListener(AppointmentBlock owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) {
			Appointment a = owner.getAppointment();
			if (owner.getAppointment().isFilled())
				a = EditAppointmentUI.ShowDialog(parent,a);
			else
				a.setPatient(SelectPatientUI.ShowDialog(parent));
			owner.setPatient(a.getPatient());
			owner.setNote(a.getNote());
		}
	}
}
