/**
 * An appointment subpanel is the panel containing the set of rooms (but not the time indicators on the 
 * left hand side of the appointment panel. 
 */

package gui.main;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import backend.DataTransferObjects.SchedulePractitionerDto;

@SuppressWarnings("serial")
public class AppointmentSubpanel extends JPanel {
	
	EmptyDayPanel empty = new EmptyDayPanel();
	DayPanel dp;
	ArrayList<RoomPanel> rooms = new ArrayList<RoomPanel>();
	
	public AppointmentSubpanel(DayPanel dp) {
		this.dp = dp;
		GridLayout gl = new GridLayout(1,0);
		gl.setHgap(0);
		gl.setVgap(0);
		setLayout(gl);
		
		
		add(empty);
		
		dp.registerAppointmentSubpanel(this);
	}
	
	public void addRoom(SchedulePractitionerDto room) {
		if (rooms.size() == 0) remove(empty);
		RoomPanel r = new RoomPanel(room, dp);
		rooms.add(r);
		add(r);
		revalidate();
	}
	
	public void removeRoom(SchedulePractitionerDto room) {
		ArrayList<RoomPanel> remRooms = new ArrayList<RoomPanel>();
		for (RoomPanel r : rooms) {
			if (r.getRoom().equals(room))
				remRooms.add(r);
		}
		for (RoomPanel r : remRooms) {
			rooms.remove(r);
			remove(r);
		}
		if (rooms.size() == 0) add(empty); 
		revalidate();
	}
	
}
