package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.PatientDto;

public class EditPatientsUI extends JDialog implements KeyListener, ActionListener {
	private static EditPatientsUI editPatientsUI;
	private List<PatientDto> pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
	
	private JButton editButton = new JButton("Edit");
	private JButton okButton = new JButton("Ok");
	private JButton newButton = new JButton("New");
	private JButton removeButton = new JButton("Remove");
	private JTable patTable;
	private JTextField searchField = new JTextField();
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public EditPatientsUI(String s) {
		setModal(true);
		setTitle(s);
		
		add(makeExisPatPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
	}
	
	private JComponent makeExisPatPanel() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	searchField.addKeyListener(this);
    	
    	patTable = new JTable(new PatTableModel(pat)) {  
    		//Implement table cell tool tips.
    		public String getToolTipText(MouseEvent e) {
    		    String tip = null;
    		    java.awt.Point p = e.getPoint();
    		    int rowIndex = rowAtPoint(p);
    		    
    		    if (rowIndex >= 0) {
    		    	TableModel model = getModel();
    		    	String firstName = (String)model.getValueAt(rowIndex,0);
    		    	String lastName = (String)model.getValueAt(rowIndex,1);
    		    	String phoneNumber = (String)model.getValueAt(rowIndex, 2);
    		    	String note = (String)model.getValueAt(rowIndex, 3);
    		    	ArrayList<String> notes = breakupString(note, 50);
    		    	if (notes.size() == 0 || notes.get(0).equals("")) {
    		    		notes.clear();
    		    		notes.add("None");
    		    	}
    		    	
    		    	tip = "<html>Patient: " + firstName + " " + lastName + "<br>" +
    		    		  "Phone Number: " + phoneNumber + "<br>" + 
    		    		  "Notes: ";
    		    	for (String s : notes) 
    		    		tip += s + "<br>";
    		    	tip = tip.substring(0, tip.length() - 4);
    		    } else tip = "";
    		    return tip;
    		 
    		}
    	};
    	patTable.setAutoCreateRowSorter(true);
    	patTable.getTableHeader().setReorderingAllowed(false);
    	patTable.getTableHeader().setFont(font);

    	patTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	panel.add(patTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(patTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.addActionListener(this);
    	okButton.setFont(font);
    	buttonPanel.add(okButton);
    	
    	editButton.setActionCommand("edit");
    	editButton.addActionListener(this);
    	editButton.setFont(font);
    	buttonPanel.add(editButton);
    	
    	removeButton.setActionCommand("remove");
    	removeButton.addActionListener(this);
    	removeButton.setFont(font);
    	buttonPanel.add(removeButton);
    	
    	newButton.setActionCommand("new");
    	newButton.addActionListener(this);
    	newButton.setFont(font);
    	buttonPanel.add(newButton);
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
        p.add(searchField, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(275,250));
        return p;
    }
	
	private ArrayList<String> breakupString(String s, int line) {
		ArrayList<String> strings = new ArrayList<String>();
		int startIndex = 0;
		int endIndex = line - 5; //- 5 to compensate for word "Note: " at beginning
		int len = s.length();
		while (endIndex < len) {
			while (endIndex < len && s.charAt(endIndex) != ' ' && s.charAt(endIndex) != '.') 
				endIndex++;
			strings.add(s.substring(startIndex, endIndex));
			startIndex = endIndex;
			endIndex = endIndex + line;
		}
		endIndex = s.length();
		strings.add(s.substring(startIndex, endIndex));
		return strings;
	}
	
	public static void ShowDialog(Component owner) {
		editPatientsUI = new EditPatientsUI("Edit Patients");
		editPatientsUI.pack();
		editPatientsUI.setLocationRelativeTo(owner);
		editPatientsUI.setVisible(true);
	}
	
	public void updateTable() {
		String filter = searchField.getText();
		if (filter.equals("")) patTable.setModel(new PatTableModel(pat));
		else patTable.setModel(new PatTableModel(pm.getFilteredPatientList(filter)));
	}
	
	class PatTableModel extends AbstractTableModel {

		List<PatientDto> patients = new ArrayList<PatientDto>();
		
		public PatTableModel(List<PatientDto> patients) {
			this.patients = patients;
		}
		
		public PatientDto getPatient(int row) {
			return patients.get(row);
		}
		
		private String[] columnNames = { "First Name", "Last Name", "Phone #", "Note" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return patients.size();
		}

		public Object getValueAt(int row, int col) {
			PatientDto p = patients.get(row);
			if (col == 0) 
				return p.getFirst();
			else  if (col == 1)
				return p.getLast();
			else if (col == 2)
				return p.getPhone();
			else
				return p.getNotes();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		
		
	}

	public void keyPressed(KeyEvent arg0) {
		
	}

	public void keyReleased(KeyEvent arg0) {
		updateTable();
		
	}

	public void actionPerformed(ActionEvent e) {
		PatTableModel model = (PatTableModel)patTable.getModel();
		if (e.getActionCommand().equals("edit")) {
			if (patTable.getSelectedRow() < 0) return;
			else {
				EditPatientUI.ShowDialog(this, model.getPatient(patTable.getSelectedRow()));
				pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
				return;
			}
		} else if (e.getActionCommand().equals("new")) {
			NewPatientUI.ShowDialog(this);
			pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
			patTable.setModel(new PatTableModel(pat));
			return;
		} else if (e.getActionCommand().equals("remove")) {
			if (patTable.getSelectedRow() < 0) return;
			if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this patient? Removing this patient will not affect historical data, but you will no longer be able to schedule him or her.", "Really remove?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
				pm.retirePatient(model.getPatient(patTable.getSelectedRow()));
				pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
			}
			return;	
		}
		editPatientsUI.setVisible(false);
	}
}
