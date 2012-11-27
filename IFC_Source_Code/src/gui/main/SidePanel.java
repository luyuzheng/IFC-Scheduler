/**
 * The SidePanel is the panel that is on the left-hand side of the scroll pane, with the day's time information on it
 */

package gui.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gui.TimeSlot;

public class SidePanel extends JPanel {

	/** The day panel associated with a side panel. */
	DayPanel dp;
	/** The heading in the appointment panel that displays the current date. */
	TimeHeading heading;
	/** The times panel contained within the side panel. */
	TimesPanel timesPanel;
	
	/** Constructs the side panel given the day panel. */
	public SidePanel(DayPanel dp) {
		this.dp = dp;
		setLayout(new BorderLayout());
		heading = new TimeHeading(dp.getDay().getDate());
		add(heading, BorderLayout.NORTH);
		
		timesPanel = new TimesPanel(new TimeSlot(dp.getDay().getStart(),dp.getDay().getEnd()));
		add(timesPanel, BorderLayout.CENTER);
	}
	
	/** Refreshes an appointment time slot to show updated information. */
	public void refreshTimeSlot(TimeSlot timeSlot) {
		this.remove(timesPanel);
		timesPanel = new TimesPanel(timeSlot);
		add(timesPanel, BorderLayout.CENTER);
		this.revalidate();
	}
}
