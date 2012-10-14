package gui.main;

import gui.main.listeners.WaitlistPatientListener;
import gui.sub.AddToWaitlistUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;

import data.Type;
import data.WaitingPatient;
import data.managers.TypeManager;
import data.managers.WaitlistManager;

public class WaitListPane extends JPanel {
	
	private TypeManager tm = new TypeManager();
	private WaitlistManager wm = new WaitlistManager();
	private Component owner;
	
	private JTable specTable;
	private JComboBox typeSelector;
	private JButton addPatientButton = new JButton("Add Patient to List");
	private JButton removePatientButton = new JButton("Remove Patient from List");
	private ArrayList<Type> types;
	
	public WaitListPane(Component owner) {
		this.owner = owner;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0,1));
		types = tm.getTypeList();
		Type general = new Type(-1, "View All");
		types.add(0, general);
		typeSelector = new JComboBox(types.toArray());
		typeSelector.setSelectedIndex(0);
		typeSelector.addActionListener(new BoxListener());
		JLabel typeLabel = new JLabel("Select Practitioner Type:");
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
    	removePatientButton.setAction(removePatientAction);
    	addPatientButton.setAction(addPatientAction);
    	buttonPanel.add(new JLabel("Perform Action:"));
    	buttonPanel.add(addPatientButton);
    	buttonPanel.add(removePatientButton);
		
    	JPanel topPanel = new JPanel(new BorderLayout());
    	topPanel.add(typeSelectionPanel, BorderLayout.NORTH);
    	topPanel.add(buttonPanel, BorderLayout.SOUTH);
    	
		add(topPanel, BorderLayout.NORTH);
		
		JPanel specTablePanel = new JPanel(new BorderLayout());
		WaitlistTableModel model = new WaitlistTableModel(wm.getWaitList(), false);
		specTable = new JTable(model);
		specTable.setDragEnabled(true);
		specTable.addMouseListener(new WaitlistPatientListener(specTable, this));
		//specTable.setTransferHandler(new WaitlistTransferHandler());
		specTable.setAutoCreateRowSorter(true);
    	specTable.getTableHeader().setReorderingAllowed(false);
    	specTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	specTablePanel.add(specTable.getTableHeader(), BorderLayout.PAGE_START);
    	specTablePanel.add(specTable, BorderLayout.CENTER);
    	JScrollPane scrollPane = new JScrollPane(specTablePanel);
    	add(scrollPane, BorderLayout.CENTER);
    	
	}
	
	private final AbstractAction addPatientAction = new AbstractAction("Add Patient to Waitlist") {
		public void actionPerformed(ActionEvent e) {
			AddToWaitlistUI.ShowDialog(owner);
			wm.updateWaitlist();
			if (typeSelector.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false));
			else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true));
		}
	};
	private final AbstractAction removePatientAction = new AbstractAction("Remove Patient from Waitlist") {
		public void actionPerformed(ActionEvent e) {
			if (specTable.getSelectedRow() < 0) return;
			WaitlistTableModel model = (WaitlistTableModel)specTable.getModel();
			WaitingPatient w = model.getPatient(specTable.getSelectedRow());
			wm.removeWaitingPatient(w);
			wm.updateWaitlist();
			if (typeSelector.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false));
			else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true));
		}
	};
	
	class WaitlistTransferHandler extends TransferHandler {
		public Transferable createTransferable(JComponent c) {
			JTable table = (JTable) c;
		    return new StringSelection(table.toString());
		}
	}
	
	public void resetModel() {
		if (typeSelector.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false));
		else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true));
	}
	
	public class WaitlistTableModel extends AbstractTableModel {

		private String[] columnNames;
		private boolean specific;
		private ArrayList<WaitingPatient> waits;
		
		public WaitlistTableModel(ArrayList<WaitingPatient> waits, boolean specific) {
			this.waits = waits;
			if (specific) columnNames = new String[] { "Date Added", "First Name", "Last Name", "Phone Number", "Comment" };
			else columnNames = new String[] { "Date Added", "First Name", "Last Name", "Phone Number", "Type", "Comment" };
			this.specific = specific;
		}
		
		public WaitingPatient getPatient(int row) {
			return waits.get(row);
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return waits.size();
		}

		public Object getValueAt(int row, int col) {
			WaitingPatient p = waits.get(row);
			if (col == 0) 
				return p.getDateString();
			else if (col == 1) 
				return p.getPatient().getFirstName();
			else  if (col == 2)
				return p.getPatient().getLastName();
			else if (col == 3)
				return p.getPatient().getNumberString();
			else if (col == 4) {
				if (specific) return p.getComment();
				else return p.getType().toString();
			}
			else return p.getComment();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
	public class BoxListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        JComboBox cb = (JComboBox)e.getSource();
	        if (cb.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false));
	        else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(cb.getSelectedIndex())), true));
	    }
	}

}
