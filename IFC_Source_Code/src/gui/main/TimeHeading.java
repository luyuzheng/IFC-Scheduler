/**
 * The time heading is currently the blank gray square in the top left hand side of the scroll pane area. 
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Constants;
import data.Date;


public class TimeHeading extends JPanel {
	JTextArea textArea;
	private Date currentDate;

	public TimeHeading(Date d) {
		setLayout(new BorderLayout());
		setBackground(new Color(215,255,215));
		setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		setPreferredSize(new Dimension(Constants.HEADING_PANEL_HEIGHT,Constants.TIMES_PANEL_WIDTH));

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.BOLD,10));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		add(textArea, BorderLayout.CENTER);

		GregorianCalendar cal = new GregorianCalendar();		
		currentDate = new Date(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.YEAR));

		//textArea.setText(currentDate.toFormalString());
		//textArea.setText("Today:\n"+(cal.get(Calendar.MONTH) + 1)+"-"+cal.get(Calendar.DATE)+",\n"+cal.get(Calendar.YEAR));
		textArea.setText("Showing:\n"+(d.getMonth())+"-"+d.getDay()+",\n"+d.getYear());
		
	}
}
