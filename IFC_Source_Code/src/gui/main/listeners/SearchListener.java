package gui.main.listeners;

import gui.main.MainWindow;
import gui.main.SearchPane;
import gui.main.SearchPane.AppointmentResultsTableModel;
import gui.main.SearchPane.PatientResultsTableModel;
import gui.sub.DisplayAppointmentSearchUI;
import gui.sub.DisplayPatientSearchUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;


/**
 * Waits for mouse clicks on patients or appointments in the search results table. If a patient or appointment is double clicked,
 * a pop up window displays more information about that patient or appointment.
 */
public class SearchListener extends MouseAdapter {
	JTable owner;
	Component parent;
	MainWindow mw;
	
	/**
	 * Constructor.
	 * 
	 * @param owner - component that owns this listener (the patient or appointment results table of the SearchPane)
	 * @param parent - the parent of this listener (SearchPane)
	 */
	public SearchListener(JTable owner, Component parent, MainWindow mw) {
		this.owner = owner;
		this.parent = parent;
		this.mw = mw;
	}
	
	/**
	 * Looks for double click events in order to display the pop up window with more patient or appointment information.
	 */
	public void mouseClicked(MouseEvent e) {
		// Looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				// The patient search results table was clicked
				if (owner.getModel() instanceof PatientResultsTableModel) {
					PatientDto pat = ((PatientResultsTableModel)owner.getModel()).getPatient(owner.getSelectedRow());
					DisplayPatientSearchUI.ShowDialog(parent.getParent(), pat, mw);
					SearchPane sp = (SearchPane) parent;
					sp.resetModel();
					
				// Otherwise the appointment search results table was clicked
				} else {
					AppointmentDto appt = ((AppointmentResultsTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
					mw.getDatePicker().setDate(appt.getApptDate());
					//DisplayAppointmentSearchUI.ShowDialog(parent.getParent(), appt);
					SearchPane sp = (SearchPane) parent;
					sp.resetModel();
				}
			}
		}
	}
	
 }