/**
 * The TimesPanel lists every hour in the day's time slot. 
 */

package gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Constants;
import data.Time;
import data.TimeSlot;


public class TimesPanel extends JPanel {

	private TimeSlot slot;
	
	public TimesPanel(TimeSlot slot) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(215,255,215));
		if (slot == null)
			this.slot = Constants.DEFAULT_TIMESLOT;
		else
			this.slot = slot;
		setupPanel();
	}
	
	public TimesPanel() {
		this(Constants.DEFAULT_TIMESLOT);
	}
	
	private void setupPanel() {
		Time start = slot.getStartTime();
		Time end = slot.getEndTime();
		
		int min = start.timeInMinutes();
		
		if (min % 60 > 0) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(215,255,215));
			panel.setPreferredSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			panel.setMinimumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			panel.setMaximumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			panel.add(new JLabel(start.toString()));
			add(panel);
			start = start.addMinutes(60 - (min % 60));
		}
		
		
		while (start.timeInMinutes() + 60 <= end.timeInMinutes()) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(215,255,215));
			panel.setPreferredSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			panel.setMaximumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			panel.setMinimumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			JLabel startLabel= new JLabel(start.toString());
			startLabel.setFont(new Font("Arial", Font.BOLD, 14));
			panel.add(startLabel);
			panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
			add(panel);
			start = start.addMinutes(60);
		}
	}
	
}
