/**
 * A RoomPanel has a HeadingPanel and a RoomSubpanel. It corresponds to a single room in the data structure. 
 */

package gui.main;

import gui.main.listeners.NewPractitionerListener;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import data.DaySaver;
import data.Practitioner;
import data.Room;

public class RoomPanel extends JPanel {
	HeadingPanel headingPanel;
	RoomSubpanel roomSubpanel;
	Room room;
	NewPractitionerListener npl = new NewPractitionerListener(this, this.getParent());
	DayPanel dp;
	
	public RoomPanel(Room room, DayPanel dp) {
		this.dp = dp;
		this.room = room;
		roomSubpanel = new RoomSubpanel(room, dp);
		headingPanel = new HeadingPanel(room, this, roomSubpanel);
		setLayout(new BorderLayout());
		add(headingPanel, BorderLayout.NORTH);
		add(roomSubpanel, BorderLayout.CENTER);
		addMouseListener(npl);
	}
	
	/**
	 * Sets the practitioner. When the practitioner is null, it adds a mouse-listener to look for a double click
	 * event, which opens up the Select Practitioner UI 
	 * @param p the practitioner to set
	 */
	public void setPractitioner(Practitioner p) {
		room.setPractitioner(p);
		headingPanel.setText();
		roomSubpanel.setAppointments();
		if (p == null) addMouseListener(npl);
		else removeMouseListener(npl);
		new DaySaver().storeDay(dp.getDay());
	}
	
	public Room getRoom() {
		return room;
	}

	public DayPanel getDayPanel() {
		return dp;
	}
	
}
