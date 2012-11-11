/**
 * A RoomPanel has a HeadingPanel and a RoomSubpanel. It corresponds to a single room in the data structure. 
 */

package gui.main;

import gui.main.listeners.NewPractitionerListener;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import backend.DataTransferObjects.PractitionerDto;
import backend.DataTransferObjects.SchedulePractitionerDto;

import gui.Constants;

public class RoomPanel extends JPanel {
	HeadingPanel headingPanel;
	RoomSubpanel roomSubpanel;
	SchedulePractitionerDto room;
	NewPractitionerListener npl = new NewPractitionerListener(this, this.getParent());
	DayPanel dp;
	
	public RoomPanel(SchedulePractitionerDto room, DayPanel dp) {
		this.dp = dp;
		this.room = room;
		roomSubpanel = new RoomSubpanel(room, dp);
		headingPanel = new HeadingPanel(room, this, roomSubpanel);
		setLayout(new BorderLayout());
		add(headingPanel, BorderLayout.NORTH);
		add(roomSubpanel, BorderLayout.CENTER);
		addMouseListener(npl);
		//setPreferredSize(new Dimension(Constants.APPT_SUBPANEL_WIDTH,Constants.HEADING_PANEL_HEIGHT));
	}
	
	/**
	 * Sets the practitioner. When the practitioner is null, it adds a mouse-listener to look for a double click
	 * event, which opens up the Select Practitioner UI 
	 * @param p the practitioner to set
	 */
	/**public void setPractitioner(PractitionerDto p) {
		room.setPractitioner(p);
		headingPanel.setText();
		roomSubpanel.setAppointments();
		if (p == null) addMouseListener(npl);
		else removeMouseListener(npl);
		new DaySaver().storeDay(dp.getDay());
	}**/
	
	public SchedulePractitionerDto getRoom() {
		return room;
	}

	public DayPanel getDayPanel() {
		return dp;
	}
	
}
