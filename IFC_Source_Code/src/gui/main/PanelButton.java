package gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class PanelButton extends JButton {
	public PanelButton(String text) {
		super(text);
		this.setMinimumSize(new Dimension(200,50));
		this.setMaximumSize(new Dimension(200,50));
		this.setPreferredSize(new Dimension(200,50));
		this.setFont(new Font("Arial", Font.BOLD, 14));
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) this.setForeground(Color.BLACK);
		else this.setForeground(Color.LIGHT_GRAY);
	}
	
}
