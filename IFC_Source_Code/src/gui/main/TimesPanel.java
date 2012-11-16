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
import gui.TimeSlot;


public class TimesPanel extends JPanel {

	private TimeSlot slot;
	
	public TimesPanel(TimeSlot slot) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(215,255,215));
		if (slot == null)
			this.slot = new TimeSlot(Constants.DEFAULT_START_TIME, Constants.DEFAULT_END_TIME);
		else
			this.slot = slot;
		setupPanel();
	}
	
	public TimesPanel() {
		this(new TimeSlot(Constants.DEFAULT_START_TIME, Constants.DEFAULT_END_TIME));
	}
	
	private void setupPanel() {
		int start = slot.getStartTime();
		int end = slot.getEndTime();
		
		int min = start;
		
		if (min % 60 > 0) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(215,255,215));
			panel.setPreferredSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			panel.setMinimumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			panel.setMaximumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, (60-(min%60))*Constants.PIXELS_PER_MINUTE));
			Integer hours = ((Integer) ((start / 60) % 12));
			if (hours == 0) hours = 12;
			String timeSt = hours.toString() + ":" + String.format("%02d", start % 60);
			String amPm = (((start / 60) / 12) == 0) ? "am" : "pm";
			JLabel label = new JLabel(timeSt + amPm);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			panel.add(label);
			panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
			add(panel);
			start = start + 60 - min % 60;
		}
		
		
		while (start + 60 <= end) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(215,255,215));
			panel.setPreferredSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			panel.setMaximumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			panel.setMinimumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, 60*Constants.PIXELS_PER_MINUTE));
			Integer hours = ((Integer) ((start / 60) % 12));
			if (hours == 0) hours = 12;
			String timeSt = hours.toString() + ":" + String.format("%02d", start % 60);
			String amPm = (((start / 60) / 12) == 0) ? "am" : "pm"; 
			JLabel startLabel= new JLabel(timeSt + amPm);
			startLabel.setFont(new Font("Arial", Font.BOLD, 14));
			panel.add(startLabel);
			panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
			add(panel);
			start += 60;
		}
		
		//render the last section if it's smaller than an hour
		int leftover = end - start;
		if (leftover < 60) {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(215,255,215));
			panel.setPreferredSize(new Dimension(Constants.TIMES_PANEL_WIDTH, leftover*Constants.PIXELS_PER_MINUTE));
			panel.setMaximumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, leftover*Constants.PIXELS_PER_MINUTE));
			panel.setMinimumSize(new Dimension(Constants.TIMES_PANEL_WIDTH, leftover*Constants.PIXELS_PER_MINUTE));Integer hours = ((Integer) ((start / 60) % 12));
			hours = ((Integer) ((start / 60) % 12));
			if (hours == 0) hours = 12;
			String timeSt = hours.toString() + ":" + String.format("%02d", start % 60);
			String amPm = (((start / 60) / 12) == 0) ? "am" : "pm"; 
			JLabel startLabel= new JLabel(timeSt + amPm);
			startLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			panel.add(startLabel);
			panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
			add(panel);
		}
	}
	
}
