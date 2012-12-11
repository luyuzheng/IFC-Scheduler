package gui.main.listeners;

import gui.main.AppointmentConfirmationPane;
import gui.main.AppointmentConfirmationPane.AppointmentConfirmationTableModel;
import gui.sub.DisplayAppointmentConfirmationUI;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;

import gui.main.MainWindow;

/**
 * Waits for mouse clicks on patients in appointment confirmation table. If a patient is double clicked,
 * a pop up window displays more information about that patient and his/her appointment.
 */
public class AppointmentConfirmationListener extends MouseAdapter implements ListSelectionListener {
	JTable owner;
	Component parent;
        MainWindow main;
	
	/**
	 * Constructor.
	 * 
	 * @param owner - component that owns this listener (the table of the AppointmentConfirmationPane)
	 * @param parent - the parent of this listener (AppointmentConfirmationPane)
	 */
	public AppointmentConfirmationListener(JTable owner, Component parent, MainWindow main) {
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
				AppointmentDto appt = ((AppointmentConfirmationTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
				DisplayAppointmentConfirmationUI.ShowDialog(parent.getParent(), appt, main);
				AppointmentConfirmationPane acp = (AppointmentConfirmationPane) parent;
				acp.resetModel();
			}
		}
	}

	/** 
	 * Determines whether the confirmation button in the AppointmentConfirmationPane is activated or not
	 * (i.e. whether a row is selected in the results table). It also changes the label of the button
	 * depending on whether the selected appointment needs to be confirmed or unconfirmed.
	 */
	public void valueChanged(ListSelectionEvent e) {
		AppointmentConfirmationPane acp = (AppointmentConfirmationPane) parent;
		if (owner.getSelectedRow() >= 0) {
			AppointmentDto appt = ((AppointmentConfirmationTableModel)owner.getModel()).getAppointment(owner.getSelectedRow());
			acp.confirmButton.setEnabled(true);
			if (!appt.getConfirmation()) {
				acp.confirmButton.setText("<html>Confirm Selected Appointment</html>");
			} else {
				acp.confirmButton.setText("<html>Undo Appointment Confirmation</html>");
			}
		} else if (owner.getSelectedRow() == -1) {
			acp.confirmButton.setEnabled(false);
		}
	}
}