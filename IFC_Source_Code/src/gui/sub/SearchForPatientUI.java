package gui.sub;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import data.Patient;
import data.WaitingPatient;

public class SearchForPatientUI extends JDialog implements ActionListener {
	private static SearchForPatientUI searchForPatientUI;
	
	private static Patient p;
	
	public static Patient ShowDialog(Component owner) {
		return p;
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
}