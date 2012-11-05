/**
 * Displays the heading for the monthly view.
 */

package gui.main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import data.Constants;


public class MonthHeadingPanel extends JPanel {
	private JTextArea text;
	
	public MonthHeadingPanel(String month) {
		setLayout(new BorderLayout());
		
		JPanel monthHeading = new JPanel(new GridLayout(1,0));
		JLabel label = new JLabel(month, JLabel.CENTER);
		label.setFont(new Font("Tahoma",Font.PLAIN,20));
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
			text.setFont(new Font("Tahoma",Font.PLAIN,14));
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
