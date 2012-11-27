/**
 * Represents a day block in the date picker. 
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.sql.Date;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TinyDayBlock extends JPanel implements FocusListener {

	/** The text in the day block. */
	private JTextArea textArea = new JTextArea();
	/** The date associated with the day block. */
	private Date date;
	/** The date picker object that contains the day block. */
	private DatePicker dp;
	
	/**
	 * Constructs a day block in the date picker. 
	 * @param dp - date picker object
	 * @param date - date associated with the day block
	 * @param today - whether or not the date associated with the day block is equal to today's date
	 * */
	public TinyDayBlock(DatePicker dp, Date date, boolean today) {
		this(dp, date, Color.WHITE, today);
	}
	
	/** Constructs a day block in the date picker given a background color. */
	public TinyDayBlock(DatePicker dp, Date date, Color color, boolean today) {
		this.date = date;
		this.dp = dp;
		
		Calendar cal = Calendar.getInstance();
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		
		cal.setTime(date);
		textArea.setText(cal.get(Calendar.DATE) + "");
		
		setLayout(new BorderLayout());
		textArea.setFocusable(true);
		textArea.addFocusListener(this);
		setBackground(color);
		setPreferredSize(new Dimension(20,20));
		add(textArea, BorderLayout.CENTER);
		if (today) {
			dp.registerCurrentDay(this);
			setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.RED));
			grabFocus();
		}
	}

	/** Changes the background color of the day block when it is selected in the date picker. */
	public void select() {
		setBackground(new Color(215, 215, 255));
	}
	
	/** Resets the background color of the day block when it is deselected in the date picker. */
	public void deselect() {
		setBackground(Color.WHITE);
	}
	
	/** Returns the date associated with the day block. */
	public Date getDate() {
		return date;
	}

	/** Sets the date associated with the day block. */
	public void setDate(Date d) {
		date=d;
	}
	
	@Override
	/** Modifies behavior of the day block when it is selected. */
	public void focusGained(FocusEvent arg0) {
		
		dp.reportFocusGained(this);
		
	}

	@Override
	/** Modifies behavior of the day block when it is deselected. */
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}