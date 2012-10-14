package gui.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class PanelButton extends JButton {
	public PanelButton(String text) {
		super(text);
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(100,100));
		this.setPreferredSize(new Dimension(100,100));
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) this.setForeground(Color.BLACK);
		else this.setForeground(Color.LIGHT_GRAY);
	}
	
}
