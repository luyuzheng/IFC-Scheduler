package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import data.Practitioner;
import data.Type;
import data.managers.PractitionerManager;
import data.managers.TypeManager;

public class SelectPractitionerUI extends JDialog implements ActionListener {

	private TypeManager tm = new TypeManager();
	private static SelectPractitionerUI selectPractitionerUI;
	private ArrayList<Practitioner> prac = new PractitionerManager().getPractitionerList();
	
	private JTextField nameField = new JTextField();
	private JComboBox typeCombo;
	JTabbedPane tabbedPane;
	private JTextField apptLengthField = new JTextField();
	private JTextArea noteField = new JTextArea();
	private JButton okButton1 = new JButton("Ok");
	private JButton cancelButton1 = new JButton("Cancel");
	private JButton okButton2 = new JButton("Ok");
	private JButton cancelButton2 = new JButton("Cancel");
	JTable pracTable;
	
	private static Practitioner practitioner;
	
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
		tabbedPane.setPreferredSize(new Dimension(275,250));
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
    	panel.add(pracTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(pracTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
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
    	
    	JPanel nameLengthPanel = new JPanel(new BorderLayout());
    	JPanel namePanel = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("Name: ");
    	namePanel.add(label, BorderLayout.NORTH);
    	namePanel.add(nameField, BorderLayout.CENTER);
    	JPanel lengthPanel = new JPanel(new BorderLayout());
    	label = new JLabel("Appt Length (Min): ");
    	lengthPanel.add(label, BorderLayout.NORTH);
    	lengthPanel.add(apptLengthField, BorderLayout.CENTER);
    	nameLengthPanel.add(namePanel, BorderLayout.CENTER);
    	nameLengthPanel.add(lengthPanel, BorderLayout.EAST);
    	topSubpanel.add(nameLengthPanel);
    	
    	JPanel typePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner Type: ");
    	typePanel.add(label, BorderLayout.NORTH);
    	JPanel typeComboPanel = new JPanel(new BorderLayout());
    	typeCombo = new JComboBox(tm.getTypeList().toArray());
    	if (tm.getTypeList().size() > 0)
    		typeCombo.setSelectedIndex(0);
    	typeComboPanel.add(typeCombo, BorderLayout.CENTER);
    	JButton newTypeButton = new JButton("New Type");
    	newTypeButton.addActionListener(this);
    	newTypeButton.setActionCommand("New Type");
    	typeComboPanel.add(newTypeButton, BorderLayout.EAST);
    	typePanel.add(typeComboPanel, BorderLayout.CENTER);
    	topSubpanel.add(typePanel);
    	
    	
    	
    	panel.add(topSubpanel, BorderLayout.NORTH);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner notes: ", JLabel.CENTER);
    	notePanel.add(label, BorderLayout.NORTH);
    	noteField.setFont(nameField.getFont());
    	noteField.setLineWrap(true);
    	noteField.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(noteField);
    	notePanel.add(notePane, BorderLayout.CENTER);
    	panel.add(notePanel, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	
    	buttonPanel.add(okButton2);
    	buttonPanel.add(cancelButton2);
    	
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static Practitioner ShowDialog(Component owner) {
		selectPractitionerUI = new SelectPractitionerUI("Select Practitioner");
		selectPractitionerUI.pack();
		selectPractitionerUI.setLocationRelativeTo(owner);
		selectPractitionerUI.setVisible(true);
		return practitioner;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Type")) {
			data.Type t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			tm = new TypeManager();
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
			String name = nameField.getText();
			if (name.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a name.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String type = ((data.Type)typeCombo.getSelectedItem()).toString();
			if (type.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a type.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int apptLength;
			try {
				apptLength = Integer.parseInt(apptLengthField.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Appointment Length must be an integer value.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (apptLength <= 0) {
				JOptionPane.showMessageDialog(this, "Appointment length must be positive.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String note = noteField.getText().replaceAll("[\r\n]+", "\t\t");
			
			int id = new PractitionerManager().getNewId();
			TypeManager tm = new TypeManager();
			data.Type t = tm.getType(type);
			if (t == null) {
				t = new data.Type(tm.getNewId(), type);
				tm.addType(t);
			}
			
			practitioner = new Practitioner(id, name, t, apptLength, note);
			new PractitionerManager().addPractitioner(practitioner);
		} else if (e.getActionCommand().equals("okOld")) {
			if (pracTable.getSelectedRow() > -1)
				practitioner = prac.get(pracTable.getSelectedRow());
			else {
				JOptionPane.showMessageDialog(this, "Please select one of the practitioners in the table, or add a new one.", "Error!", JOptionPane.ERROR_MESSAGE);
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
			Practitioner p = prac.get(row);
			if (col == 0) 
				return p.getName();
			else if (col == 1) 
				return p.getType();
			else
				return p.getApptLength();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}
