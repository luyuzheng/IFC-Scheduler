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

import data.Practitioner;

public class NewPractitionerListener extends MouseAdapter {
	
	RoomPanel owner;
	Component parent;
	
	public NewPractitionerListener(RoomPanel owner, Component parent) {
		this.owner = owner;
		this.parent = parent;
	}
	
	public void mouseClicked(MouseEvent e) {
		//looking for double click events
		if (e.getClickCount() >= 2) { 
			Practitioner p; 
			Practitioner old = owner.getRoom().getPractitioner();
			if (old != null) 
				p = DisplayPractitionerUI.ShowDialog(parent, old);
			else
				p = SelectPractitionerUI.ShowDialog(parent);
			if (p == null) {
				owner.getDayPanel().clearRoom(owner);
			}
			else if (old != p)
				owner.setPractitioner(p);			
			
		}
		
	}
}
