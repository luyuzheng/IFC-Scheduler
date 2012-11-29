package gui.main.listeners;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import gui.sub.DisplayWaitingPatientUI;
import gui.sub.DisplayWaitingPatientUI.*;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;
import backend.DataTransferObjects.TypeDto;
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
				PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(waitlistPatient.getPatientID());
				TypeDto type = DataServiceImpl.GLOBAL_DATA_INSTANCE.getType(waitlistPatient.getTypeName());
				
				DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(patient.getPatID(), appt);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromWaitlist(patient, type);
				parent.setVisible(false);
			}
		}
	}
}