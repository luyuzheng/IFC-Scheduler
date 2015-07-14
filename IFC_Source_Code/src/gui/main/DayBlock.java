/**
 * Represents the square for a given day in the month view of the scheduler. Shows all the practitioners scheduled for that day.
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import gui.Constants;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.DayDto;
import backend.DataTransferObjects.SchedulePractitionerDto;

@SuppressWarnings("serial")
public class DayBlock extends JPanel {

	/** The text to be displayed in the day block, i.e. which practitioners are scheduled. */
	private JTextArea textArea;
	
	/** Constructs a day block given a date object. */
	public DayBlock(Date date) {
		this(date, Color.WHITE);
	}
	
	/** Constructs a day block given a date object and a background color. */
	public DayBlock(Date date, Color color) {
		textArea = new JTextArea();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		//String text = date.getDate() + "\n";
		String text = cal.get(Calendar.DATE) + "\n";
		
		DayDto d = DataServiceImpl.GLOBAL_DATA_INSTANCE.getOrCreateDay(date);
		
		if (color.equals(Color.WHITE) && d == null) color = new Color(220,220,220);
		else if (color.equals(Color.WHITE)){
			boolean hasPrac = false;
			for (SchedulePractitionerDto r : DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(d)) {
				if (r.getPractSchedID() != null) {
					text += r.getPractitioner().getFirst() + ' ' + 
							r.getPractitioner().getLast() + " - " + 
							r.getPractitioner().getTypeName();
					if (r.isFull()) text += " *FULL* ";
					text += "\n";
					hasPrac = true;
				}
			}
			if (!hasPrac) color = new Color(220,220,220);
		}
			
		textArea.setText(text);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH_TINY);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		setPreferredSize(new Dimension(Constants.MONTH_BLOCK_WIDTH, Constants.MONTH_BLOCK_HEIGHT));
		setBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK));
		setLayout(new BorderLayout());
		setBackground(color);
		add(textArea);
		
	}
	
}
