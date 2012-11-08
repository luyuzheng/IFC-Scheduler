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

import data.Practitioner;
import data.managers.PractitionerManager;

public class EditPractitionersUI extends JDialog implements KeyListener, ActionListener {
	private static EditPractitionersUI editPractitionersUI;
	private PractitionerManager pm = new PractitionerManager();
	private ArrayList<Practitioner> prac = pm.getPractitionerList();
	
	private JButton editButton = new JButton("Edit");
	private JButton okButton = new JButton("OK");
	private JButton newButton = new JButton("New");
	private JButton removeButton = new JButton("Remove");
	private JTable pracTable;
	private JTextField searchField = new JTextField();
	private Font font= new Font("Tahoma", Font.PLAIN, 14);
	
	public EditPractitionersUI(String s) {
		setModal(true);
		setTitle(s);
		
		add(makeExisPracPanel(), BorderLayout.CENTER);
		setResizable(false);
	}
	
	private JComponent makeExisPracPanel() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	searchField.addKeyListener(this);
    	
    	
    	pracTable = new JTable(new PracTableModel(prac)) {  
    		//Implement table cell tool tips.
    		public String getToolTipText(MouseEvent e) {
    		    String tip = null;
    		    java.awt.Point p = e.getPoint();
    		    int rowIndex = rowAtPoint(p);
    		    
    		    if (rowIndex >= 0) {
    		    	TableModel model = getModel();
    		    	String name = (String)model.getValueAt(rowIndex,0);
    		    	String type = (String)model.getValueAt(rowIndex,1);
    		    	String apptLength = (Integer)model.getValueAt(rowIndex, 2) + "";
    		    	String note = (String)model.getValueAt(rowIndex, 3);
    		    	ArrayList<String> notes = breakupString(note, 50);
    		    	if (notes.size() == 0 || notes.get(0).equals("")) {
    		    		notes.clear();
    		    		notes.add("None");
    		    	}
    		    	
    		    	tip = "<html>Practitioner: " + name + "<br>" +
    		    		  "Type: " + type + "<br>" + 
    		    		  "Appointment Length: " + apptLength + "<br>" + 
    		    		  "Notes: ";
    		    	for (String s : notes) 
    		    		tip += s + "<br>";
    		    	tip = tip.substring(0, tip.length() - 4);
    		    } else tip = "";
    		    return tip;
    		 
    		}
    	};
    	pracTable.setAutoCreateRowSorter(true);
    	pracTable.getTableHeader().setReorderingAllowed(false);

    	pracTable.setFont(font);
    	pracTable.getTableHeader().setFont(font);
    	
    	pracTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	panel.add(pracTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(pracTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("okOld");
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
        
        
        searchField.setFont(font);
        
        
        p.add(searchField, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(500, 250));
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
		editPractitionersUI = new EditPractitionersUI("Edit Practitioners");
		editPractitionersUI.pack();
		editPractitionersUI.setLocationRelativeTo(owner);
		editPractitionersUI.setVisible(true);
	}
	
	public void updateTable() {
		String filter = searchField.getText();
		if (filter.equals("")) pracTable.setModel(new PracTableModel(prac));
		else pracTable.setModel(new PracTableModel(pm.getFilteredPractitionerList(filter)));
	}
	
	class PracTableModel extends AbstractTableModel {

		ArrayList<Practitioner> practitioners = new ArrayList<Practitioner>();
		
		public PracTableModel(ArrayList<Practitioner> practitioners) {
			this.practitioners = practitioners;
		}
		
		public Practitioner getPractitioner(int row) {
			return practitioners.get(row);
		}
		
		private String[] columnNames = { "Name", "Type", "Appointment Length", "Note" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return practitioners.size();
		}

		public Object getValueAt(int row, int col) {
			Practitioner p = practitioners.get(row);
			if (col == 0) 
				return p.getName();
			else  if (col == 1)
				return p.getType().toString();
			else if (col == 2)
				return p.getApptLength();
			else
				return p.getNote();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		
		
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent arg0) {
		updateTable();
		
	}

	public void actionPerformed(ActionEvent e) {
		PracTableModel model = (PracTableModel)pracTable.getModel();
		if (e.getActionCommand().equals("edit")) {
			if (pracTable.getSelectedRow() < 0) return;
			else {
				EditPractitionerUI.ShowDialog(this, model.getPractitioner(pracTable.getSelectedRow()));
				prac = pm.getPractitionerList();
				pracTable.setModel(new PracTableModel(prac));
				return;
			}
		} else if (e.getActionCommand().equals("remove")) {
			if (pracTable.getSelectedRow() < 0) return;
			if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this practitioner? Removing this practitioner will not affect historical data, but you will no longer be able to schedule him or her.", "Really remove?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
				pm.retirePractitioner(model.getPractitioner(pracTable.getSelectedRow()));
				prac = pm.getPractitionerList();
				pracTable.setModel(new PracTableModel(prac));
			}
			return;	
		} else if (e.getActionCommand().equals("new")) {
			NewPractitionerUI.ShowDialog(this);
			prac = pm.getPractitionerList();
			pracTable.setModel(new PracTableModel(prac));
			return;
		}
		editPractitionersUI.setVisible(false);
	}
}
