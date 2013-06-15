package gui.main;

import backend.DataService.DataServiceImpl;
import gui.Constants;
import gui.DateTimeUtils;
import gui.main.listeners.WaitlistPatientListener;
import gui.sub.AddToWaitlistUI;
import gui.sub.ChangePriorityUI;
import gui.sub.DisplayWaitingPatientUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import backend.DataTransferObjects.*;

import java.sql.Date;

/**
 * WaitListPane displays the wait list pane on the right-hand side of the application when the "Wait List" button is clicked.
 * The user can add or remove patients from the wait list. The pane can be closed by clicking the "Hide Wait List" button.
 */
public class WaitListPane extends JPanel {
	
	private Component owner;
	
	private JTable specTable;
	private JComboBox typeSelector;
	private JButton addPatientButton = new JButton("Add Patient to List");
	public JButton schedulePatientButton = new JButton("Schedule Patient from List");
	public JButton removePatientButton = new JButton("Remove Patient from List");
	public JButton changePriorityButton = new JButton("Change Priority");
	private ArrayList<TypeDto> types;
        private WaitListPane pane;
	
	/**
	 * This method is called by MainWindow (the owner). It creates the UI for the pane, including all
	 * labels and the table of results.
	 * 
	 * @param owner - the component that owns this pane
	 */
	public WaitListPane(Component owner) {
		this.owner = owner;
                this.pane = this;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JPanel typeSelectionPanel = new JPanel(new GridLayout(0,1));
		types = (ArrayList<TypeDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes();
		TypeDto noFilter = new TypeDto();
		noFilter.setField(TypeDto.TYPE_NAME, "All");
		noFilter.setField(TypeDto.TYPE_ID, -1);
		typeSelector = new JComboBox();
		typeSelector.addItem(noFilter);
		for (TypeDto type : types) {
			typeSelector.addItem(type);
		}
		if (!types.isEmpty()) {
			typeSelector.setSelectedIndex(0);
		}
		typeSelector.addActionListener(new BoxListener());
		JLabel typeLabel = new JLabel("Filter Waitlist By Practitioner Type:");
		typeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		typeSelector.setFont(Constants.DIALOG);
		typeLabel.setFont(Constants.DIALOG);
		typeSelectionPanel.add(typeLabel);
		typeSelectionPanel.add(typeSelector);
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
    	addPatientButton.setAction(addPatientAction);
    	schedulePatientButton.setAction(schedulePatientAction);
    	removePatientButton.setAction(removePatientAction);
    	changePriorityButton.setAction(changePriorityAction);
    	schedulePatientButton.setEnabled(false);
    	removePatientButton.setEnabled(false);
    	changePriorityButton.setEnabled(false);
    	JLabel actionLabel = new JLabel(" ");
    	actionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    	actionLabel.setFont(Constants.DIALOG);
    	addPatientButton.setFont(Constants.DIALOG);
    	schedulePatientButton.setFont(Constants.DIALOG);
    	removePatientButton.setFont(Constants.DIALOG);
    	changePriorityButton.setFont(Constants.DIALOG);
    	buttonPanel.add(actionLabel);
    	buttonPanel.add(addPatientButton);
    	buttonPanel.add(schedulePatientButton);
    	buttonPanel.add(removePatientButton);
    	buttonPanel.add(changePriorityButton);
		
    	JPanel topPanel = new JPanel(new BorderLayout());
    	topPanel.add(typeSelectionPanel, BorderLayout.NORTH);
    	topPanel.add(buttonPanel, BorderLayout.SOUTH);
    	
		add(topPanel, BorderLayout.NORTH);
		
		JPanel specTablePanel = new JPanel(new BorderLayout());
		WaitlistTableModel model = new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false);
		specTable = new JTable(model);
		specTable.setRowHeight(specTable.getRowHeight() + 30);
		specTable.setDragEnabled(true);
		specTable.setFont(Constants.DIALOG);
		specTable.addMouseListener(new WaitlistPatientListener(specTable, this, this));
		specTable.getSelectionModel().addListSelectionListener(new WaitlistPatientListener(specTable, this, this));
		specTable.setAutoCreateRowSorter(true);
    	specTable.getTableHeader().setReorderingAllowed(false);
    	specTable.getTableHeader().setFont(Constants.DIALOG);
    	specTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	specTable.getColumnModel().getColumn(0).setMinWidth(100);
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
			int change = AddToWaitlistUI.ShowDialog(owner);
			TypeDto type = (TypeDto) typeSelector.getSelectedItem();
			if (change == -1) {
				return;
			} else if (change == type.getTypeID()) {
				filter(typeSelector);
			} else {
				typeSelector.setSelectedIndex(0);
				specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
			}
			specTable.getColumnModel().getColumn(0).setMinWidth(100);
		}
	};
	
	private final AbstractAction schedulePatientAction = new AbstractAction("Schedule Patient from Waitlist") {
		public void actionPerformed(ActionEvent e) {
			if (specTable.getSelectedRow() < 0) {
				return;
			}
			WaitlistDto wp = ((WaitlistTableModel)specTable.getModel()).getPatient(specTable.getSelectedRow());
			String newComment = DisplayWaitingPatientUI.ShowDialog(getParent(), wp, pane);
			wp.setComments(newComment);
			DataServiceImpl.GLOBAL_DATA_INSTANCE.updateWaitlist(wp);
			resetModel();
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
			TypeDto type = new TypeDto();
			type.setField(TypeDto.TYPE_ID, w.getTypeID());
			DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromWaitlist(w.getPatient(), type);
			filter(typeSelector);
			specTable.getColumnModel().getColumn(0).setMinWidth(100);
		}
	};
	
	/**
	 * This allows the user to change the priority of a waitlist entry by changing the entry's timestamp.
	 */
	private final AbstractAction changePriorityAction = new AbstractAction("Change Priority") {
		public void actionPerformed(ActionEvent e) {
			if (specTable.getSelectedRow() < 0) return;
			WaitlistTableModel model = (WaitlistTableModel)specTable.getModel();
			WaitlistDto w = model.getPatient(specTable.getSelectedRow());
			ChangePriorityUI.ShowDialog(owner, w);
			filter(typeSelector);
		}
	};

	/**
	 * This resets the model.
	 */
	public void resetModel() {
            specTable.setModel(new WaitlistTableModel((ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist(), false));
        	specTable.getColumnModel().getColumn(0).setMinWidth(100);
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
			if (specific) columnNames = new String[] { "Priority Date", "Name", "Phone Number", "Comment" };
			else columnNames = new String[] { "Priority Date", "Name", "Phone Number", "Type", "Comment" };
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
				return "<html>" + p.getDate() + "<br>" + p.getTimeAdded() + "</html>";
			else if (col == 1) 
				return "<html>" + p.getPatient().getFirst() + "<br>" + p.getPatient().getLast() + "</html>";
			else if (col == 2)
				return p.getPatient().getPhone();
			else if (col == 3) {
				if (specific) return p.getComments();
				else return p.getTypeName();
			}
			else return p.getComments();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	/**
	 * Returns the full list of people on the wait list.
	 * 
	 * @return entire list of people on the wait list
	 */
	public List<WaitlistDto> getWaitList() {
		return DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist();
	}
	
	/**
	 * Returns a waitlist filtered by a specified service type.
	 * 
	 * @param t - the type to filter the waitlist
	 * @return a list of waitlist patients that have been filtered by a type
	 */
	public List<WaitlistDto> getWaitList(TypeDto t) {
		List<WaitlistDto> sub = new ArrayList<WaitlistDto>();
		List<WaitlistDto> fullWaitlist = DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist();
		for (WaitlistDto w : fullWaitlist) {
			if (w.getTypeID() == t.getTypeID())
				sub.add(w);
		}
		return sub;
	}
	
	/**
	 * This class sets the table model to include the appropriate types of columns according to what has been selected in 
	 * the JComboBox that allows the user to choose the type of service.
	 */
	public class BoxListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        JComboBox cb = (JComboBox)e.getSource();
	        filter(cb);
	    }
	}

	void filter(JComboBox cb) {
		ArrayList<WaitlistDto> waitlist = (ArrayList<WaitlistDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getWaitlist();
		if (((TypeDto)cb.getSelectedItem()).getTypeID() == -1) {
        	specTable.setModel(new WaitlistTableModel(waitlist, false));
        } else {
        	ArrayList<WaitlistDto> filtered = new ArrayList<WaitlistDto>();
        	for (WaitlistDto entry : waitlist) {
        		if (entry.getTypeID() == ((TypeDto)cb.getSelectedItem()).getTypeID()) {
        			filtered.add(entry);
        		}
        	}
        	specTable.setModel(new WaitlistTableModel(filtered, false));
        }
	}
        
    public void refreshAppointments(Date day){
        ((MainWindow) owner).refreshAppointments(day);
    }
}
