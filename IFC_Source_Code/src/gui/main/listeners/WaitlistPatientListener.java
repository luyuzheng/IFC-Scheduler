/**
 * This listener handles mouse events that occur in the waitlist pane. 
 */

package gui.main.listeners;

import gui.main.WaitListPane;
import gui.main.WaitListPane.WaitlistTableModel;
import gui.sub.DisplayWaitingPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.DataTransferObjects.WaitlistDto;


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
				DisplayWaitingPatientUI.ShowDialog(parent.getParent(), wp, waitlist);

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
			wp.changePriorityButton.setEnabled(true);
		} else if (owner.getSelectedRow() < 0) {
			wp.schedulePatientButton.setEnabled(false);
			wp.removePatientButton.setEnabled(false);
			wp.changePriorityButton.setEnabled(false);
		}
	}
}