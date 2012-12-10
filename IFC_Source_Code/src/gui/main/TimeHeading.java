/**
 * The time heading displays the current date in the appointment panel. 
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import gui.Constants;
import java.util.Date;


@SuppressWarnings("serial")
public class TimeHeading extends JPanel {
	/** The time heading text. */
	JTextArea textArea;
	/** The current date selected in the appointment panel. */
	private Date currentDate;

	/** Constructs a time heading given a date object. */
	public TimeHeading(Date d) {
		setLayout(new BorderLayout());
		setBackground(new Color(215,255,215));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK), new EmptyBorder(5,5,5,5)));
		setPreferredSize(new Dimension(Constants.HEADING_PANEL_HEIGHT,Constants.TIMES_PANEL_WIDTH));

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.HEADER);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		add(textArea, BorderLayout.CENTER);

		currentDate = d;
		textArea.setText(shortDate(currentDate));
		
	}
	
	/** Returns a string of the abbreviated month and date for the date object. */
	public String shortDate(Date d){
		Date date = new Date(d.getTime());
		String dateString = date.toString().substring(4, 10);
		return dateString;
	}
}
