/**
 * This listener handles mouse events that are supposed to open 
 * the Select Practitioner dialog. 
 */

package gui.main.listeners;

import gui.main.RoomPanel;
import gui.sub.DisplayPractitionerUI;
import gui.sub.SelectPractitionerUI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import backend.DataTransferObjects.PractitionerDto;

public class NewPractitionerListener extends MouseAdapter {
	
	RoomPanel owner;
	Component parent;
	
	public NewPractitionerListener(RoomPanel owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	/** Looks for double-click events to show the Select Practitioner dialog. */
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) { 
			PractitionerDto p;
			PractitionerDto old = owner.getRoom().getPractitioner();
			if (old != null) 
				p = DisplayPractitionerUI.ShowDialog(parent, old);
			else {
				SelectPractitionerUI selectPractitionerUI = SelectPractitionerUI.ShowDialog(
						parent, owner.getRoom().getStart(), owner.getRoom().getEnd());
				p = selectPractitionerUI.getPractitioner();
			}
			if (p == null) {
				owner.getDayPanel().clearRoom(owner);
			}
			else if (old != p)
				owner.setPractitioner(p);			
			
		}
		
	}
}
