package gui.sub;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.WaitingPatient;

public class DisplayWaitingPatientUI extends JDialog implements ActionListener {
	private static DisplayWaitingPatientUI displayWaitingPatientUI;
	
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private JTextArea textArea;
	private JTextArea noteArea;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	private static String comment;
	
	private DisplayWaitingPatientUI(String name, WaitingPatient wp) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		String text = "Patient Name: " + wp.getPatient().getFullName();
		text += "\nPhone Number: " + wp.getPatient().getNumberString();
		text += "\nPatient Note: " + wp.getPatient().getNote().replaceAll("\t\t", "\n");

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(font);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		
		JPanel notePanel = new JPanel(new BorderLayout());
		JLabel noteLabel = new JLabel("Appointment Note:");
		JScrollPane notePane = new JScrollPane();
		notePane.setPreferredSize(new Dimension(200,200));
		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		noteArea.setWrapStyleWord(true);
		noteArea.setFont(font);
		noteArea.setText(wp.getComment().replaceAll("\t\t", "\n"));
		notePane.setViewportView(noteArea);
		notePanel.add(noteLabel, BorderLayout.NORTH);
		notePanel.add(notePane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(font);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("ok");
		cancelButton.setFont(font);
		buttonPanel.add(cancelButton);
		
		add(textArea, BorderLayout.NORTH);
		add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}
    
	
	public static String ShowDialog(Component owner, WaitingPatient waitp) {
		displayWaitingPatientUI = new DisplayWaitingPatientUI("Edit Waitlist Entry", waitp);
		displayWaitingPatientUI.pack();
		displayWaitingPatientUI.setLocationRelativeTo(owner);
		displayWaitingPatientUI.setVisible(true);
		return comment;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			comment = noteArea.getText();
		} 
		displayWaitingPatientUI.setVisible(false);
    }
	
}