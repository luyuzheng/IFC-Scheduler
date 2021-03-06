/**
 * Displays the heading for the monthly view.
 */

package gui.main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import gui.Constants;


@SuppressWarnings("serial")
public class MonthHeadingPanel extends JPanel {
	private JTextArea text;
	
	/** Constructs the header for the month in month view given the month's name as a string. */
	public MonthHeadingPanel(String month) {
		setLayout(new BorderLayout());
		
		JPanel monthHeading = new JPanel(new GridLayout(1,0));
		JLabel label = new JLabel(month, JLabel.CENTER);
		label.setFont(Constants.HEADER);
		monthHeading.add(label, BorderLayout.CENTER);
		monthHeading.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		monthHeading.setBackground(new Color(255,215,215));
		add(monthHeading);
		
		JPanel dayHeading = new JPanel(new GridLayout(1,0));
		for (int i = 0; i < 7; i++) {
			String day;
			if (i==0) day = "Sunday";
			else if (i==1) day = "Monday";
			else if (i==2) day = "Tuesday";
			else if (i==3) day = "Wednesday";
			else if (i==4) day = "Thursday";
			else if (i==5) day = "Friday";
			else day = "Saturday";
			
			text = new JTextArea();
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setEditable(false);
			text.setFont(Constants.PARAGRAPH);
			text.setOpaque(false);
			text.setHighlighter(null);
			text.setText(day);
		
			
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(text, BorderLayout.CENTER);
			panel.setBackground(new Color(255,215,215));
			//panel.setBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK));
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK), new EmptyBorder(5,5,5,5)));
			setPreferredSize(new Dimension(0, Constants.HEADING_PANEL_HEIGHT));
			dayHeading.add(panel);
			
		}		
		
		add(monthHeading, BorderLayout.NORTH);
		add(dayHeading, BorderLayout.CENTER);
			
	}
}
