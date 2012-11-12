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

import backend.DataTransferObjects.AppointmentDto;

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
			AppointmentDto a = owner.getAppointment();
			if (owner.getAppointment() != null)
				a = EditAppointmentUI.ShowDialog(parent,a);
			else
				a.setPatientID(SelectPatientUI.ShowDialog(parent).getPatID());
			owner.setPatient(a.getPatientID());
			owner.setNote(a.getNote());
		}
	}
}
