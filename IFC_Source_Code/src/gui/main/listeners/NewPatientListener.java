/**
 * This listener handles mouse events that are supposed to open 
 * the Select Patient dialog. 
 */

package gui.main.listeners;

import backend.DataService.DataServiceImpl;
import gui.main.AppointmentBlock;
import gui.sub.EditAppointmentUI;
import gui.sub.SelectPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import backend.DataTransferObjects.*;
import backend.DataTransferObjects.AppointmentDto;

import gui.main.MainWindow;

/**
 * Waits for mouse clicks for an appointment block. If no patient has been scheduled yet, it opens
 * the SelectPatientUI to schedule the patient. If a patient has already been scheduled in that block,
 * the EditAppointmentUI so that the user can view appointment details.
 */
public class NewPatientListener extends MouseAdapter {
	
	AppointmentBlock owner;
	Component parent;
    MainWindow main;
	
    /**
     * Constructor.
     * 
     * @param owner - component that owns this listener (AppointmentBlock)
	 * @param parent - the parent of this listener (None)
	 * @param main - the main window for the entire application (MainWindow)
     */
	public NewPatientListener(AppointmentBlock owner, Component parent, MainWindow main) {
		this.owner = owner;
		this.parent = parent;
		this.main = main;
	}
	
	/** 
	 * Looks for double clicks to open the Select Patient of Edit Appointment dialogs. 
	 */
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) {
			AppointmentDto a = owner.getAppointment();
                        
			if (owner.getAppointment().getPatientID() != null) {
				a = EditAppointmentUI.ShowDialog(parent,a, main);
			}
            else {
                PatientDto patient = SelectPatientUI.ShowDialog(parent);
                // User hit the "Cancel" button
                if (patient != null) {
                    DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(
                    		patient.getPatID(), a);
                    a.setPatientID(patient.getPatID());
                    owner.setPatient(patient.getPatID());
                }
            }
            owner.setNote(a.getNote());
		}
	}
}
