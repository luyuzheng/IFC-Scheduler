package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import backend.DataTransferObjects.PractitionerDto;

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
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		//changeButton.addActionListener(this);
		//changeButton.setActionCommand("change");
		//buttonPanel.add(changeButton);
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		clearButton.setFont(Constants.DIALOG);
		buttonPanel.add(clearButton);
		
		add(textArea, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(350, 150));
		setResizable(false);
		
	}
	
	public static PractitionerDto ShowDialog(Component owner, PractitionerDto p) {
		displayPractitionerUI = new DisplayPractitionerUI("Edit Practitioner", p);
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