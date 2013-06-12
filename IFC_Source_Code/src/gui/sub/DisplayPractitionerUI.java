package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataTransferObjects.PractitionerDto;

/**
 * Displays the info on a practitioner when a scheduled one is double clicked.
 * It also allows you the clear the practitioner scheduled for that time.
 */
public class DisplayPractitionerUI extends JDialog implements ActionListener {
	private static DisplayPractitionerUI displayPractitionerUI;
	
	private JButton okButton = new JButton("OK");
	private JButton clearButton = new JButton("Clear Practitioner");
	private JTextArea textArea;
	
	private static PractitionerDto practitioner;
	
	private DisplayPractitionerUI(String name, PractitionerDto p) {
		practitioner = p;
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		String text = "Practitioner Name: " + p.getFirst() + " " + p.getLast();
		text += "\nType: " + p.getTypeName().toString();
		text += "\nAppointment Length: " + p.getApptLength() + " Minutes";
		text += "\nPractitioner Note: ";
		
		if (p.getNotes().isEmpty()) {
			text+= "No Notes to Display";
		} else {
			text += p.getNotes();
		}

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setBorder(new EmptyBorder(10, 10, 0, 10));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		clearButton.setFont(Constants.DIALOG);
		buttonPanel.add(clearButton);
		buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		
		add(textArea, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(350, 220));
		setResizable(false);
		
	}
	
	public static PractitionerDto ShowDialog(Component owner, PractitionerDto p) {
		displayPractitionerUI = new DisplayPractitionerUI("Practitioner Details", p);
		displayPractitionerUI.pack();
		displayPractitionerUI.setLocationRelativeTo(owner);
		displayPractitionerUI.setVisible(true);
		return practitioner;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("clear")) {
			practitioner = null;
		}
		displayPractitionerUI.setVisible(false);
    }
	
}