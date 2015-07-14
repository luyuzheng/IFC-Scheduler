package gui.main.listeners;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import gui.sub.DisplayWaitingPatientUI;
import gui.sub.DisplayWaitingPatientUI.*;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.WaitlistDto;

public class ScheduleWaitlistPatientListener extends MouseAdapter {
	
	JTable owner;
	Component parent;
	
	public ScheduleWaitlistPatientListener(JTable owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	/** Schedules a waitlisted patient to the day view. */
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				AppointmentDto appt = ((ApptTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
				WaitlistDto waitlistPatient = ((DisplayWaitingPatientUI)parent).getWaitlistPatient();
				DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(waitlistPatient.getPatientID(), appt);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromWaitlist(waitlistPatient.getWaitlistID());
                                
				parent.setVisible(false);
			}
		}
	}
}