package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.TypeDto;

public class EditTypeUI extends JDialog implements ActionListener {
	private static EditTypeUI editTypeUI;
	
	private JPanel panel;
	private JComboBox typeCombo;
	private JTextField editTypeField = new JTextField();
	private JButton saveButton = new JButton("Save");
	private JButton newTypeButton = new JButton("New");
	private JButton editTypeButton = new JButton("Edit");
	//private JButton deleteTypeButton = new JButton("Delete");
	private JButton closeButton = new JButton("Close");
	
	private Component owner;
	
	private EditTypeUI(String name, Component owner) {
		setModal(true);
		setTitle(name);

		this.owner = owner;
		
		setPreferredSize(new Dimension(400, 175));
		
		panel = makePanel();
		add(panel, BorderLayout.CENTER);		

		setResizable(false);
	}
	
	private JPanel makePanel() {
		// Put the list of existing types in the combo box
		JLabel typeLabel = new JLabel("Select a Type: ", JLabel.CENTER);
		typeLabel.setFont(Constants.PARAGRAPH);
		typeCombo = new JComboBox((DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes().toArray()));
		typeCombo.setFont(Constants.DIALOG);
		
		JPanel typePanel = new JPanel(new FlowLayout());
		typePanel.add(typeLabel);
		typePanel.add(typeCombo);
		
		editTypeField.setPreferredSize(new Dimension(200, 30));
		editTypeField.setEditable(false);
		editTypeField.setFont(Constants.DIALOG);
		
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		saveButton.setActionCommand("save");
		saveButton.setFont(Constants.DIALOG);
		
		JPanel editPanel = new JPanel(new FlowLayout());
		editPanel.add(editTypeField);
		editPanel.add(saveButton);
		
		newTypeButton.addActionListener(this);
		newTypeButton.setActionCommand("new");
		newTypeButton.setFont(Constants.DIALOG);
		
		editTypeButton.addActionListener(this);
		editTypeButton.setActionCommand("edit");
		editTypeButton.setFont(Constants.DIALOG);
		
		/*
		deleteTypeButton.addActionListener(this);
		deleteTypeButton.setActionCommand("delete");
		deleteTypeButton.setFont(Constants.DIALOG);
		*/
		
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		closeButton.setFont(Constants.DIALOG);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(newTypeButton);
		buttonPanel.add(editTypeButton);
		//buttonPanel.add(deleteTypeButton);
		buttonPanel.add(closeButton);
		
		JPanel fullPanel = new JPanel(new BorderLayout());
		fullPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		fullPanel.add(typePanel, BorderLayout.NORTH);
		fullPanel.add(editPanel, BorderLayout.CENTER);
		fullPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		return fullPanel;	
	}

	public static void ShowDialog(Component owner) {
		editTypeUI = new EditTypeUI("Edit Service Types", owner);
		editTypeUI.pack();
		editTypeUI.setLocationRelativeTo(owner);
		editTypeUI.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("new")) {
			editTypeField.setEditable(false);
			TypeDto t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			typeCombo.addItem(t);
			typeCombo.setSelectedItem(t);
			
		} else if (e.getActionCommand().equals("edit")) {
			if (editTypeButton.getText().equals("Edit")) {
				editTypeButton.setText("Cancel Edit");
				typeCombo.setEnabled(false);
				saveButton.setEnabled(true);
				editTypeField.setEditable(true);
				editTypeField.setText(((TypeDto)typeCombo.getSelectedItem()).toString());
			} else {
				editTypeButton.setText("Edit");
				typeCombo.setEnabled(true);
				saveButton.setEnabled(false);
				editTypeField.setEditable(false);
				editTypeField.setText("");
			}
				
	/*	} else if (e.getActionCommand().equals("delete")) {
			JLabel msg = new JLabel("Are you sure you want to delete this service type?: " + 
								    ((TypeDto)typeCombo.getSelectedItem()).toString() + 
								    "This will delete ALL practitioners and appointments connected to this service type.");
			msg.setFont(Constants.PARAGRAPH);
			int selection = JOptionPane.showConfirmDialog(this, msg);
			if (selection == JOptionPane.YES_OPTION) {
				DataServiceImpl.GLOBAL_DATA_INSTANCE.removePractitionerType(((TypeDto)typeCombo.getSelectedItem()).toString());
				typeCombo.removeItem(typeCombo.getSelectedItem());
				((gui.main.MainWindow) owner).refreshAppointments(((gui.main.MainWindow) owner).getCurrentDay().getDate());
			}*/
			
		} else if (e.getActionCommand().equals("save")) {
			// Save the new type name
			DataServiceImpl.GLOBAL_DATA_INSTANCE.updatePractitionerType(editTypeField.getText(), (TypeDto)typeCombo.getSelectedItem());
			
			// Update the type name in the combo box and refresh the main window
			typeCombo.removeItem(typeCombo.getSelectedItem());
			TypeDto newType = DataServiceImpl.GLOBAL_DATA_INSTANCE.getType(editTypeField.getText());
			((gui.main.MainWindow) owner).refreshAppointments(((gui.main.MainWindow) owner).getCurrentDay().getDate());
			typeCombo.addItem(newType);
			typeCombo.setSelectedItem(newType);
			
			// Reset the textbox and buttons
			typeCombo.setEnabled(true);
			saveButton.setEnabled(false);
			editTypeField.setEditable(false);
			editTypeField.setText("");
			editTypeButton.setText("Edit");
			
		} else {
			editTypeUI.setVisible(false);
		}
		
	}
}