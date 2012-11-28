package gui.sub;

import backend.DataService.DataServiceImpl;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import backend.DataTransferObjects.*;

public class SelectPractitionerUI extends JDialog implements ActionListener {

	private static SelectPractitionerUI selectPractitionerUI;
	private ArrayList<PractitionerDto> prac = (ArrayList) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitioners();
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JComboBox typeCombo;
	JTabbedPane tabbedPane;
	private JTextField apptLengthField = new JTextField();
	private JTextArea noteField = new JTextArea();
	private JButton okButton1 = new JButton("OK");
	private JButton cancelButton1 = new JButton("Cancel");
	private JButton okButton2 = new JButton("OK");
	private JButton cancelButton2 = new JButton("Cancel");
	private Font font= new Font("Tahoma", Font.PLAIN, 14);
	JTable pracTable;
	
	private static PractitionerDto practitioner;
	
	private SelectPractitionerUI(String name) {
		practitioner = null;
		setModal(true);
		setTitle(name);
		tabbedPane = new JTabbedPane();
		JComponent panel1 = makeExisPracPanel();
		tabbedPane.addTab("Existing Practitioner", null, panel1,
		                  "Select an Existing Practitioner");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeNewPracPanel();
		tabbedPane.addTab("New Practitioner", null, panel2,
		                  "Select a New Practitioner");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		setLayout(new GridLayout(1,1));
		tabbedPane.setPreferredSize(new Dimension(500,250));
		tabbedPane.setFont(font);
		add(tabbedPane);
		setResizable(false);
		
		okButton1.setActionCommand("okOld");
    	okButton1.addActionListener(this);
    	cancelButton1.addActionListener(this);
    	okButton2.setActionCommand("okNew");
    	okButton2.addActionListener(this);
    	cancelButton2.addActionListener(this);
	}
    
    private JComponent makeExisPracPanel() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	pracTable = new JTable(new PracTableModel());
    	pracTable.getTableHeader().setReorderingAllowed(false);
    	TableColumn column = null;
    	for (int i = 0; i < pracTable.getColumnCount(); i++) {
    	    column = pracTable.getColumnModel().getColumn(i);
    	    if (i == 2) {
    	        column.setPreferredWidth(200); //third column is bigger
    	    } else {
    	        column.setPreferredWidth(100);
    	    }
    	}
    	pracTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	pracTable.setFont(font);
    	pracTable.setBorder(new EmptyBorder(5,5,5,5));
    	pracTable.getTableHeader().setFont(font);
    	panel.add(pracTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(pracTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton1.setFont(font);
    	cancelButton1.setFont(font);
    	buttonPanel.add(okButton1);
    	buttonPanel.add(cancelButton1);
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        //scrollPane.setPreferredSize(new Dimension(410,200));
        
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        return p;
    }
    
    private JComponent makeNewPracPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	JPanel topSubpanel = new JPanel(new GridLayout(0, 1));
    	
    	JPanel nameLengthPanel = new JPanel(new GridLayout(0, 3));
    	JPanel firstNamePanel = new JPanel(new BorderLayout());
    	JPanel lastNamePanel = new JPanel(new BorderLayout());
    	JLabel firstNameLabel = new JLabel("First Name: ");
    	JLabel lastNameLabel = new JLabel("Last Name: ");
    	firstNameLabel.setFont(font);
    	lastNameLabel.setFont(font);
    	firstNameField.setFont(font);
    	lastNameField.setFont(font);
    	firstNamePanel.add(firstNameLabel, BorderLayout.NORTH);
    	firstNamePanel.add(firstNameField, BorderLayout.CENTER);
    	lastNamePanel.add(lastNameLabel, BorderLayout.NORTH);
    	lastNamePanel.add(lastNameField, BorderLayout.CENTER);
    	JPanel lengthPanel = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("Appt Length (Min): ");
    	label.setFont(font);
    	apptLengthField.setFont(font);
    	lengthPanel.add(label, BorderLayout.NORTH);
    	lengthPanel.add(apptLengthField, BorderLayout.CENTER);
    	nameLengthPanel.add(firstNamePanel);
    	nameLengthPanel.add(lastNamePanel);
    	nameLengthPanel.add(lengthPanel);
    	topSubpanel.add(nameLengthPanel);
    	
    	JPanel typePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner Type: ");
    	label.setFont(font);
    	typePanel.add(label, BorderLayout.NORTH);
    	JPanel typeComboPanel = new JPanel(new BorderLayout());
        
        ArrayList<TypeDto> typeList = (ArrayList<TypeDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes();
    	typeCombo = new JComboBox(typeList.toArray());
    	if (typeList.size() > 0)
    		typeCombo.setSelectedIndex(0);
    	typeCombo.setFont(font);
    	typeComboPanel.add(typeCombo, BorderLayout.CENTER);
    	JButton newTypeButton = new JButton("New Type");
    	newTypeButton.setFont(font);
    	newTypeButton.addActionListener(this);
    	newTypeButton.setActionCommand("New Type");
    	typeComboPanel.add(newTypeButton, BorderLayout.EAST);
    	typePanel.add(typeComboPanel, BorderLayout.CENTER);
    	topSubpanel.add(typePanel);
    	
    	
    	
    	panel.add(topSubpanel, BorderLayout.NORTH);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner notes: ", JLabel.CENTER);
    	label.setFont(font);
    	notePanel.add(label, BorderLayout.NORTH);
    	noteField.setFont(font);
    	noteField.setLineWrap(true);
    	noteField.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(noteField);
    	notePanel.add(notePane, BorderLayout.CENTER);
    	panel.add(notePanel, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	
    	okButton2.setFont(font);
    	cancelButton2.setFont(font);
    	buttonPanel.add(okButton2);
    	buttonPanel.add(cancelButton2);
    	
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static PractitionerDto ShowDialog(Component owner) {
		selectPractitionerUI = new SelectPractitionerUI("Select Practitioner");
		selectPractitionerUI.pack();
		selectPractitionerUI.setLocationRelativeTo(owner);
		selectPractitionerUI.setVisible(true);
		return practitioner;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Type")) {
			TypeDto t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			tabbedPane.remove(1);
			JComponent panel2 = makeNewPracPanel();
			tabbedPane.addTab("New Practitioner", null, panel2,
			                  "Select a New Practitioner");
			remove(tabbedPane);
			add(tabbedPane);
			repaint();
			validate();
			tabbedPane.setSelectedIndex(1);
			return;
		}
		else if (e.getActionCommand().equals("okNew")) {
			JLabel msg = new JLabel();
			msg.setFont(font);
			String firstName = firstNameField.getText();
			if (firstName.equals("")) {
				msg.setText( "Please enter a first name.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String lastName = lastNameField.getText();
			if (lastName.equals("")) {
				msg.setText("Please enter a last name.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String type = ((TypeDto)typeCombo.getSelectedItem()).getTypeName();
			if (type.equals("")) {
				msg.setText("Please enter a type.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int apptLength;
			try {
				apptLength = Integer.parseInt(apptLengthField.getText());
			} catch (Exception ex) {
				msg.setText("Appointment Length must be an integer value.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (apptLength <= 0) {
				msg.setText( "Appointment length must be positive.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String note = noteField.getText().replaceAll("[\r\n]+", "\t\t");
			
			TypeDto t = DataServiceImpl.GLOBAL_DATA_INSTANCE.getType(type);
			if (t == null) {
				t = DataServiceImpl.GLOBAL_DATA_INSTANCE.addNewPractitionerType(type);
			}
			
			practitioner = DataServiceImpl.GLOBAL_DATA_INSTANCE.addPractitioner(
                                t.getTypeID(), firstName, lastName, apptLength, "" , note);
		} else if (e.getActionCommand().equals("okOld")) {
			if (pracTable.getSelectedRow() > -1)
				practitioner = prac.get(pracTable.getSelectedRow());
			else {
				JLabel msg = new JLabel("Please select one of the practitioners in the table, or add a new one.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		selectPractitionerUI.setVisible(false);
    }
	
	class PracTableModel extends AbstractTableModel {

		private String[] columnNames = { "Name", "Type", "Appointment Length (Minutes)" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return prac.size();
		}

		public Object getValueAt(int row, int col) {
			PractitionerDto p = prac.get(row);
			if (col == 0) 
				return p.getFirst() + " " + p.getLast();
			else if (col == 1) 
				return p.getTypeName();
			else
				return p.getApptLength();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}
