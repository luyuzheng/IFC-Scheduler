/**
 * Represents the day view when there are no practitioners scheduled.
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Constants;

@SuppressWarnings("serial")
public class EmptyDayPanel extends JPanel {
	JLabel textLabel;
	
	public EmptyDayPanel() {
		setLayout(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		topPanel.setBackground(new Color(215,255,215));
		topPanel.setPreferredSize(new Dimension(Constants.APPT_SUBPANEL_WIDTH,Constants.HEADING_PANEL_HEIGHT));
		topPanel.setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));
		
		textLabel = new JLabel("There are no practitioners currently selected for this day.", JLabel.CENTER);
		textLabel.setFont(new Font("Arial", Font.PLAIN, 28));
		textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		mainPanel.add(textLabel, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));
		
		add(topPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}
}
