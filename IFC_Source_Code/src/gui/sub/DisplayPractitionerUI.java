package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Practitioner;

public class DisplayPractitionerUI extends JDialog implements ActionListener {
	private static DisplayPractitionerUI displayPractitionerUI;
	
	private JButton okButton = new JButton("Ok");
	private JButton changeButton = new JButton("Change Practitioner");
	private JButton clearButton = new JButton("Clear Room");
	private JTextArea textArea;
	
	private static Practitioner practitioner;
	
	private DisplayPractitionerUI(String name, Practitioner p) {
		practitioner = p;
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		String text = "Practitioner Name: " + p.getName();
		text += "\nType: " + p.getType().toString();
		text += "\nAppointment Length: " + p.getApptLength() + " Minutes";
		text += "\nPractitioner Note: " + p.getNote();

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		buttonPanel.add(okButton);
		changeButton.addActionListener(this);
		changeButton.setActionCommand("change");
		buttonPanel.add(changeButton);
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		buttonPanel.add(clearButton);
		
		add(textArea, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}
    
	
	public static Practitioner ShowDialog(Component owner, Practitioner p) {
		displayPractitionerUI = new DisplayPractitionerUI("Edit Practitioner", p);
		displayPractitionerUI.pack();
		displayPractitionerUI.setLocationRelativeTo(owner);
		displayPractitionerUI.setVisible(true);
		return practitioner;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("change")) {
			Practitioner p = SelectPractitionerUI.ShowDialog(this);
			if (JOptionPane.showConfirmDialog(this, "Changing practitioners will clear all the appointments for this practitioner. Continue anyways?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return;
			}
			practitioner = p;
		} else if (e.getActionCommand().equals("clear")) {
			practitioner = null;
		}
		displayPractitionerUI.setVisible(false);
    }
	
}