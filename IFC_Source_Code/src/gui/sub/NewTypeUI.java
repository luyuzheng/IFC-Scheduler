package gui.sub;

import backend.DataService.DataServiceImpl;
import gui.Constants;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.DataTransferObjects.*;

public class NewTypeUI extends JDialog implements ActionListener {
	private static NewTypeUI newTypeUI;
	private static TypeDto t;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");

	private JTextField typeField;
	
	private NewTypeUI() {
		setModal(true);
		setTitle("New Type");
		
		setLayout(new GridLayout(0,1));
		typeField = new JTextField();
		typeField.setFont(Constants.PARAGRAPH);
		add(typeField);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		cancelButton.setFont(Constants.DIALOG);
		buttonPanel.add(cancelButton);
		
		add(buttonPanel);
		
		setResizable(false);
		
	}
    
	
	public static TypeDto ShowDialog(Component owner) {
		t = null;
		newTypeUI = new NewTypeUI();
		newTypeUI.pack();
		newTypeUI.setLocationRelativeTo(owner);
		newTypeUI.setVisible(true);
		return t;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			String type = typeField.getText();
			if (type.equals("")) newTypeUI.setVisible(false);
			t = DataServiceImpl.GLOBAL_DATA_INSTANCE.addNewPractitionerType(type);
		} 
		newTypeUI.setVisible(false);
    }
	
}