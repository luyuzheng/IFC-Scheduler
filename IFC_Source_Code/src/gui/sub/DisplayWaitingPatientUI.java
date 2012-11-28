package gui.sub;

import gui.sub.SelectPatientUI.PatTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;

@SuppressWarnings("serial")
public class DisplayWaitingPatientUI extends JDialog implements ActionListener {
	private static DisplayWaitingPatientUI displayWaitingPatientUI;
	
	private ArrayList<AppointmentDto> apt;
	private TypeDto type;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JTextArea textArea;
	private JTextArea noteArea;
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	private static String comment;
	
	private DisplayWaitingPatientUI(String name, WaitlistDto wp) {
		setModal(true);
		setTitle(name);
		
		
		apt= (ArrayList) DataServiceImpl.GLOBAL_DATA_INSTANCE.searchForAppointments(wp.getTypeID());
		
		JComponent panel= displayAvailAppts();
		panel.setPreferredSize(new Dimension(500,250));
		
			
		
		setLayout(new BorderLayout());
		/*
		String text = "Patient Name: " + wp.getPatient().getFullName();
		text += "\nPhone Number: " + wp.getPatient().getPhone();
		text += "\nPatient Note: " + wp.getPatient().getNotes().replaceAll("\t\t", "\n");

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(font);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		
		JPanel notePanel = new JPanel(new BorderLayout());
		JLabel noteLabel = new JLabel("Appointment Note:");
		JScrollPane notePane = new JScrollPane();
		notePane.setPreferredSize(new Dimension(500,250));
		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		noteArea.setWrapStyleWord(true);
		noteArea.setFont(font);
		noteArea.setText(wp.getComments().replaceAll("\t\t", "\n"));
		notePane.setViewportView(noteArea);
		notePanel.add(noteLabel, BorderLayout.NORTH);
		notePanel.add(notePane, BorderLayout.CENTER);
		*/
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(font);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("ok");
		cancelButton.setFont(font);
		buttonPanel.add(cancelButton);
		
		//add(textArea, BorderLayout.NORTH);
		//add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}
	
	private JComponent displayAvailAppts() {
		
		JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	
    	//searchField.addKeyListener(this);
    	
    	
    	patTable = new JTable(new ApptTableModel(pat)) {  
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

    	patTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	patTable.setFont(font);
    	patTable.getTableHeader().setFont(font);
    	panel.add(patTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(patTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	editButton.setActionCommand("edit");
    	editButton.addActionListener(this);
    	editButton.setFont(font);
    	buttonPanel.add(editButton);
    	okButton1.setActionCommand("okOld");
    	okButton1.addActionListener(this);
    	okButton1.setFont(font);
    	cancelButton1.addActionListener(this);
    	cancelButton1.setFont(font);
    	buttonPanel.add(okButton1);
    	buttonPanel.add(cancelButton1);
    	
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
        searchField.setFont(font);
        p.add(searchField, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        return p;
		
		
		return null;
	}
    
	
	public static String ShowDialog(Component owner, WaitlistDto waitp) {
		
		String name= "Schedule Appointment for " + waitp.getPatient().getFullName();
		displayWaitingPatientUI = new DisplayWaitingPatientUI(name, waitp);
		displayWaitingPatientUI.pack();
		displayWaitingPatientUI.setLocationRelativeTo(owner);
		displayWaitingPatientUI.setVisible(true);
		return comment;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			comment = noteArea.getText();
		} 
		displayWaitingPatientUI.setVisible(false);
    }
	
	class ApptTableModel extends AbstractTableModel {	

		ArrayList<AppointmentDto> appointments = new ArrayList<AppointmentDto>();
		
		public ApptTableModel(ArrayList<AppointmentDto> appointments) {
			this.appointments = appointments;
		}
		
		public AppointmentDto getAppointment(int row) {
			return appointments.get(row);
		}
		
		private String[] columnNames = { "Date", "Start Time", "End Time", "Practitioner" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return appointments.size();
		}

		public Object getValueAt(int row, int col) {
			AppointmentDto a = appointments.get(row);
			if (col == 0) 
				return a.getApptDate();
			else  if (col == 1)
				return a.getStart();
			else if (col == 2)
				return a.getEnd();
			else
				return a.getPractSchedID();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}