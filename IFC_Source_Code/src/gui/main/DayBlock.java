/**
 * Represents a day block in the month view
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import gui.Constants;
import java.sql.Date;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.SchedulePractitionerDto;

public class DayBlock extends JPanel {

	private JTextArea textArea;
	
	public DayBlock(Date date) {
		this(date, Color.WHITE);
	}
	
	public DayBlock(Date date, Color color) {
		textArea = new JTextArea();
		String text = date.getDay() + "\n";
		
		DayLoader dl = new DayLoader();
		DayDto d = dl.loadDay(date);
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
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		setPreferredSize(new Dimension(Constants.MONTH_BLOCK_WIDTH, Constants.MONTH_BLOCK_HEIGHT));
		setBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK));
		setLayout(new BorderLayout());
		setBackground(color);
		add(textArea);
		
	}
	
}
