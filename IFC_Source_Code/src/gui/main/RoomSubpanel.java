/**
 * A room subpanel is the parent of all the appointment blocks. If there is no practitioner set, 
 * this panel is blank. 
 */

package gui.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.SchedulePractitionerDto;

import gui.Constants;

public class RoomSubpanel extends JPanel{
	private SchedulePractitionerDto room;
	private DayPanel dp;
	
	public RoomSubpanel(SchedulePractitionerDto room, DayPanel dp) {
		this.dp = dp;
		this.room = room;
		//if (room.hasPrac()) 
			//setAppointments();
		setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.LIGHT_GRAY);
	}
	
	/**
	 * Adds all the appointments to this object, and adds leftover if necessary
	 */
	public void setAppointments() {
		removeAll();
		repaint();
		for (AppointmentDto a : room.getAppointments()) {
			AppointmentBlock app = new AppointmentBlock(a,dp);
			add(app);
		}
		/**if (room.getLeftover() > 0) {
			JPanel leftover = new JPanel();
			leftover.setPreferredSize(new Dimension(0,room.getLeftover()*Constants.PIXELS_PER_MINUTE));
			leftover.setBackground(Color.LIGHT_GRAY);
			add(leftover);
		}**/
	}
	
	public DayPanel getDayPanel() {
		return dp;
	}

}
