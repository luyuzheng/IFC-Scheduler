/**
 * Properties of each button on the day panel. 
 */

package gui.main;

import gui.Constants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PanelButton extends JButton {
	
	/** Constructs a panel button given a string that is used to label the button. */
	public PanelButton(String text) {
		super(text);
		this.setMinimumSize(new Dimension(200,50));
		this.setMaximumSize(new Dimension(200,50));
		this.setPreferredSize(new Dimension(200,50));
		this.setFont(Constants.PARAGRAPH_BOLD);
	}
	
	/** Sets the button as clickable. */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) this.setForeground(Color.BLACK);
		else this.setForeground(Color.LIGHT_GRAY);
	}
	
}
