package gui.main;

import backend.DataService.DataServiceImpl;
import gui.main.listeners.WaitlistPatientListener;
import gui.sub.AddToWaitlistUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import backend.DataTransferObjects.*;

/**
 * WaitListPane displays the wait list pane on the right-hand side of the application when the "Wait List" button is clicked.
 * The user can add or remove patients from the wait list. The pane can be closed by clicking the "Hide Wait List" button.
 */
public class WaitListPane extends JPanel {
	
	private Component owner;
	
	private JTable specTable;
	private JComboBox typeSelector;
	private JButton addPatientButton = new JButton("Add Patient to List");
	private JButton removePatientButton = new JButton("Remove Patient from List");
	private ArrayList<TypeDto> types;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	/**
	 * This method is called by MainWindow (the owner). It creates the UI for the pane, including all
	 * labels and the table of results.
	 * 
	 * @param owner - the component that owns this pane
	 */
	public WaitListPane(Component owner) {
		this.owner = owner;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0,1));
		types = (ArrayList<TypeDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes();
		//Type general = new Type(-1, "View All");
		//types.add(0, general); TODO: VIEW ALL
		typeSelector = new JComboBox(types.toArray());
		typeSelector.setSelectedIndex(0);
		typeSelector.addActionListener(new BoxListener());
		JLabel typeLabel = new JLabel("Select Practitioner Type:");
		typeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		typeSelector.setFont(font);
		typeLabel.setFont(font);
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
    	removePatientButton.setAction(removePatientAction);
    	addPatientButton.setAction(addPatientAction);
    	JLabel actionLabel = new JLabel("Perform Action:");
    	actionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    	actionLabel.setFont(font);
    	addPatientButton.setFont(font);
    	removePatientButton.setFont(font);
    	buttonPanel.add(actionLabel);
    	buttonPanel.add(addPatientButton);
    	buttonPanel.add(removePatientButton);
		
    	JPanel topPanel = new JPanel(new BorderLayout());
    	topPanel.add(typeSelectionPanel, BorderLayout.NORTH);
    	topPanel.add(buttonPanel, BorderLayout.SOUTH);
    	
		add(topPanel, BorderLayout.NORTH);
		
		JPanel specTablePanel = new JPanel(new BorderLayout());
		WaitlistTableModel model = new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false);
		specTable = new JTable(model);
		specTable.setDragEnabled(true);
		specTable.setFont(font);
		specTable.addMouseListener(new WaitlistPatientListener(specTable, this));
		//specTable.setTransferHandler(new WaitlistTransferHandler());
		specTable.setAutoCreateRowSorter(true);
    	specTable.getTableHeader().setReorderingAllowed(false);
    	specTable.getTableHeader().setFont(font);
    	specTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	specTablePanel.add(specTable.getTableHeader(), BorderLayout.PAGE_START);
    	specTablePanel.add(specTable, BorderLayout.CENTER);
    	JScrollPane scrollPane = new JScrollPane(specTablePanel);
    	add(scrollPane, BorderLayout.CENTER);
    	
	}
	
	/**
	 * This calls the appropriate pop up window to add a patient when the "Add Patient to Waitlist" button is clicked. 
	 * It also resets the table model to include the appropriate types of columns.
	 */
	private final AbstractAction addPatientAction = new AbstractAction("Add Patient to Waitlist") {
		public void actionPerformed(ActionEvent e) {
			AddToWaitlistUI.ShowDialog(owner);
                        specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
			//if (typeSelector.getSelectedIndex() == 0) specTable.setModel(
                        //        new WaitlistTableModel(wm.getWaitList(), false));
			//else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true));
		}
	};
	
	/**
	 * This removes a patient from the table when the "Remove Patient from Waitlist" button is clicked. It also resets
	 * the table model to include the appropriate types of columns if there are no more patients.
	 */
	private final AbstractAction removePatientAction = new AbstractAction("Remove Patient from Waitlist") {
		public void actionPerformed(ActionEvent e) {
			if (specTable.getSelectedRow() < 0) return;
			WaitlistTableModel model = (WaitlistTableModel)specTable.getModel();
			WaitlistDto w = model.getPatient(specTable.getSelectedRow());
			DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromWaitlist(w.getPatient(), w.getTypeID()); //TODO: remove waitlistDto directly
			specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
                        /*if (typeSelector.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false)); TODO: filter
			else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true)); */
		}
	};
	
	class WaitlistTransferHandler extends TransferHandler {
		public Transferable createTransferable(JComponent c) {
			JTable table = (JTable) c;
		    return new StringSelection(table.toString());
		}
	}
	
	/**
	 * This resets the model.
	 */
	public void resetModel() {
            specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
		/*if (typeSelector.getSelectedIndex() == 0) specTable.setModel(new WaitlistTableModel(wm.getWaitList(), false)); TODO: FILTER
		else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(typeSelector.getSelectedIndex())), true)); */
	}
	
	/**
	 * Displays the table of results after adding or removing a patient.
	 */
	public class WaitlistTableModel extends AbstractTableModel {

		private String[] columnNames;
		private boolean specific;
		private ArrayList<WaitlistDto> waits;
		
		/**
		 * Constructor to produce a wait list table model.
		 */ 
		public WaitlistTableModel(ArrayList<WaitlistDto> waits, boolean specific) {
			this.waits = waits;
			if (specific) columnNames = new String[] { "Date Added", "First Name", "Last Name", "Phone Number", "Comment" };
			else columnNames = new String[] { "Date Added", "First Name", "Last Name", "Phone Number", "Type", "Comment" };
			this.specific = specific;
		}
		
		/**
		 * Returns a representation of a waiting patient.
		 * 
		 * @param row - the row number in the table
		 * @return the patient at a particular row number
		 */
		public WaitlistDto getPatient(int row) {
			return waits.get(row);
		}
		
		/**
		 * Returns the number of columns in the table.
		 * 
		 * @return number of columns in the table
		 */
		public int getColumnCount() {
			return columnNames.length;
		}
		
		/**
		 * Returns the name of a specified column in the table.
		 * 
		 * @param col - the column number
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * Returns the number of patients on the wait list.
		 * 
		 * @returns the number of rows in the table
		 */
		public int getRowCount() {
			return waits.size();
		}

		/**
		 * Returns the cell of information specified at a particular row and column.
		 * 
		 * @param row - the row number
		 * @param col - the column number
		 */
		public Object getValueAt(int row, int col) {
			WaitlistDto p = waits.get(row);
			if (col == 0) 
				return p.getDate();
			else if (col == 1) 
				return p.getPatient().getFirst();
			else  if (col == 2)
				return p.getPatient().getLast();
			else if (col == 3)
				return p.getPatient().getPhone();
			else if (col == 4) {
				if (specific) return p.getComment();
				else return p.getTypeName();
			}
			else return p.getComment();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	/**
	 * This class sets the table model to include the appropriate types of columns according to what has been selected in 
	 * the JComboBox that allows the user to choose the type of service.
	 */
	public class BoxListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        JComboBox cb = (JComboBox)e.getSource();
                specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
	        /*if (cb.getSelectedIndex() == 0) specTable.setModel( TODO:FILTER
                        new WaitlistTableModel(wm.getWaitList(), false));
	        else specTable.setModel(new WaitlistTableModel(wm.getWaitList(types.get(cb.getSelectedIndex())), true));
                 * */
	    }
	}

}
