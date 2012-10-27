package gui.sub;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Type;
import data.managers.TypeManager;

public class NewTypeUI extends JDialog implements ActionListener {
	private static NewTypeUI newTypeUI;
	private static Type t;
	
	private TypeManager tm = new TypeManager();
	
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");

	private JTextField typeField;
	
	private NewTypeUI() {
		setModal(true);
		setTitle("New Type");
		
		setLayout(new GridLayout(0,1));
		typeField = new JTextField();
		add(typeField);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		buttonPanel.add(cancelButton);
		
		add(buttonPanel);
		
		setResizable(false);
		
	}
    
	
	public static Type ShowDialog(Component owner) {
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
			t = new Type(tm.getNewId(), type);
			tm.addType(t);
		} 
		newTypeUI.setVisible(false);
    }
	
}