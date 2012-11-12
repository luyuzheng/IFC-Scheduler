/**
 * The SidePanel is the panel that is on the left-hand side of the scroll pane, with the day's time information on it
 */

package gui.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gui.TimeSlot;

public class SidePanel extends JPanel {
	public SidePanel(DayPanel dp) {
		setLayout(new BorderLayout());
		add(new TimeHeading(dp.getDay().getDate()), BorderLayout.NORTH);
		
		add(new TimesPanel(new TimeSlot(dp.getDay().getStart(),dp.getDay().getEnd())), BorderLayout.CENTER);
	}
}
