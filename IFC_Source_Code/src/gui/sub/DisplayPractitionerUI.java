package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import backend.DataTransferObjects.PractitionerDto;

/**
 * Displays the info on a practitioner when a scheduled one is double clicked.
 * It also allows you the clear the practitioner scheduled for that time.
 */
@SuppressWarnings("serial")
public class DisplayPractitionerUI extends JDialog implements ActionListener {
	private static DisplayPractitionerUI displayPractitionerUI;
	
	private JButton okButton = new JButton("OK");
	private JButton clearButton = new JButton("Clear Practitioner");
	private JTextPane textPane;
	private JScrollPane scrollPane;
	
	private static PractitionerDto practitioner;
	
	private DisplayPractitionerUI(String name, PractitionerDto p) {
		practitioner = p;
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(450, 350));
		setResizable(true);
		
		JPanel practPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = getPractInfoText(p);
		
		textPane = new JTextPane();
		textPane.setFont(Constants.PARAGRAPH);
		textPane.setContentType("text/html");
		textPane.setText("<html>" + text + "</html>");
		textPane.setEditable(false);
		textPane.setCaretPosition(0);
		textPane.setOpaque(false);
		textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); // Used so that font will display properly
	
		scrollPane = new JScrollPane(textPane);
		TitledBorder title = BorderFactory.createTitledBorder("Practitioner Information");
		title.setTitleFont(Constants.PARAGRAPH_BOLD);
		scrollPane.setBorder(title);
		scrollPane.setPreferredSize(new Dimension(450, 320));
		practPanel.add(scrollPane);
		
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		clearButton.setFont(Constants.DIALOG);
		buttonPanel.add(clearButton);
		
		practPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		
		add(practPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private String getPractInfoText(PractitionerDto p) {
		String[] labels = {"Practitioner Name", "Practitioner Type", "Appointment Length"};
		String[] info = {p.getFirst() + " " + p.getLast(), 
						 p.getTypeName().toString(), 
						 p.getApptLength() + " Minutes"};
		String text = "<table>";
		for (int i = 0; i < labels.length; i++) {
			text += "<tr><td><b>" + labels[i] + ": </b></td><td align='left'>" + info[i] + "</td></tr><br />";
		}
		text += "<tr><td colspan='2'><b>Practitioner Note: </b>";
		
		
		if (p.getNotes().isEmpty()) {
			text += "No Notes to Display";
		} else {
			text += p.getNotes();
		}
		
		text += "</td></tr></table>";
		
		return text;
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