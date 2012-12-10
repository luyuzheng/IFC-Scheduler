/**
 * This listener handles mouse events that occur in the waitlist pane. 
 */

package gui.main.listeners;

import backend.DataService.DataServiceImpl;
import gui.main.AppointmentConfirmationPane;
import gui.main.WaitListPane;
import gui.main.AppointmentConfirmationPane.AppointmentConfirmationTableModel;
import gui.main.WaitListPane.WaitlistTableModel;
import gui.sub.DisplayWaitingPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.WaitlistDto;

import gui.main.WaitListPane;

//import data.managers.WaitlistManager;

public class WaitlistPatientListener extends MouseAdapter implements ListSelectionListener {
	
	JTable owner;
	Component parent;
        WaitListPane waitlist;
	
	public WaitlistPatientListener(JTable owner, Component parent, WaitListPane waitlist) {
		this.owner = owner;
		this.parent = parent;
                this.waitlist = waitlist;
	}
	
	/** Displays a pop-up to add or remove a patient to the waitlist. */
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {

				WaitlistDto wp = ((WaitlistTableModel)owner.getModel()).getPatient(owner.getSelectedRow());
				String newComment = DisplayWaitingPatientUI.ShowDialog(parent.getParent(), wp, waitlist);
				wp.setComments(newComment);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.updateWaitlist(wp);
				WaitListPane wlp = (WaitListPane) parent;
				wlp.resetModel();
			}
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		WaitListPane wp = (WaitListPane) parent;
		if (owner.getSelectedRow() >= 0) {
			wp.schedulePatientButton.setEnabled(true);
			wp.removePatientButton.setEnabled(true);
		} else if (owner.getSelectedRow() < 0) {
			wp.schedulePatientButton.setEnabled(false);
			wp.removePatientButton.setEnabled(false);
		}
	}
}