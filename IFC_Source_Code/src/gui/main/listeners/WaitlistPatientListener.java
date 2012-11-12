/**
 * This listener handles mouse events that are supposed to open 
 * the Select Patient dialog. 
 */

package gui.main.listeners;

import gui.main.WaitListPane;
import gui.main.WaitListPane.WaitlistTableModel;
import gui.sub.DisplayWaitingPatientUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import backend.DataTransferObjects.PatientDto;

import data.WaitingPatient;
import data.managers.WaitlistManager;

public class WaitlistPatientListener extends MouseAdapter {
	
	JTable owner;
	Component parent;
	
	public WaitlistPatientListener(JTable owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) {
			if (owner.getSelectedRow() >= 0) {
				PatientDto wp = ((WaitlistTableModel)owner.getModel()).getPatient(owner.getSelectedRow());
				String newComment = DisplayWaitingPatientUI.ShowDialog(parent.getParent(), wp);
				wp.setComment(newComment);
				WaitlistManager wm = new WaitlistManager();
				wm.updateWaitingPatient(wp);
				WaitListPane wlp = (WaitListPane) parent;
				wlp.resetModel();
			}
		}
	}
}