/** 
 * Provides a manual for the scheduler and includes instructions on how to perform specific functions.
 * */
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import gui.Constants;

public class HelpUI extends JDialog implements KeyListener, ActionListener {

	private String helpText =
		 " TABLE OF CONTENTS\n\n"
		+ " 0. Installation Instructions	 \n"
		+ " 1. Definition of main terms	 \n"
		+ " 2. Overview of Functionality	 \n"
		+ " \t2.1 Setting up a new day	 \n"
		+ " \t2.2 Changing hours of operation	 \n"
		+ " \t2.3 Adding practitioners	 \n"
		+ " \t2.4 Removing practitioners from the day's roster	 \n"
		+ " \t2.5 Editing practitioners	 \n"
		+ " \t2.6 Scheduling appointments	 \n"
		+ " \t2.7 Adding or editing an appointment note	 \n"
		+ " \t2.8 Removing scheduled appointments	 \n"
		+ " \t2.9 Viewing the wait list	 \n"
		+ " \t2.10 Adding a patient to the wait list	 \n"
		+ " \t2.11 Removing a patient from the wait list	 \n"
		+ " \t2.12 Scheduling a patient from the wait list	 \n"
		+ " \t2.13 Editing patients	 \n"
		+ " \t2.14 Printing the current day view	 \n"
		+ " \t2.15 Printing the current month view	 \n"
		+ " \t2.16 Switching to the month view	 \n"
		+ " \t2.17 Switching to the day view	 \n"
		+ " \t2.18 Setting the default hours for a day	 \n"
		+ "\n"
		+ " **0. Installation Instructions**	 \n"
		+ " 1) Make sure the latest version of Java is installed. The program requires Java JRE version 1.6_04 or higher. \n"
		+ " 2) Create a folder in a safe location, such as C:\\ or C:\\Program Files\\ to house the program. \n"
		+ " 3) Drag the files \"IFC_Scheduler.jar\" and \"README\" into the program. \n"
		+ " 4) Optional: Create a shortcut of the program by right clicking on \"IFC_Scheduler.jar\" and clicking,  \"Send to Desktop\". \n"
		+ "\n"
		+ " **1. Definition of main terms**	 \n"
		+ "\n"
		+ " *Options Panel: This is the button panel on the top left-hand side of the main window, above the calendar. It has six buttons, and contains the bulk of the functionality for the scheduling application.	 \n"
		+ " *Roster: This is the list of current practitioners for a day. \n"
		+ " *Day View: This is the view of the current practitioners and appointments for a particular day. \n"
		+ "\n"
		+ " **2. Overview of Functionality**	 \n"
		+ "\n"
		+ " 2.1 Setting up a new day:	 \n"
		+ "\n"
		+ " 1) Click on the correct day on the calendar. When it is selected, it will be highlighted in blue. \n"
		+ " 2) If the default hours of operation are not correct, change them by clicking on the \"Change Hours of Operation\" button in the panel on the left-hand side of the main window. Enter in the correct start and end times in military time (00:00 - 23:59), and click \"Ok\". See \"Changing hours of operation\" for more information.	 \n"
		+ " 3) The day is now ready to appoint practitioners. To add a practitioner to the current roster, click \"Add Practitioner\" in the panel on the left-hand side of the main window. See \"Adding practitioners\" for more information.	 \n"
		+ " 4) To begin scheduling patients, click on the desired time slot under one of the day's practitioners. You can either double-click on this slot, or click on the \"Schedule Patient\" button in the options panel to open the patient scheduling dialog. See \"Scheduling patients\" for more information.	 \n"
		+ "\n"
		+ " 2.2 Changing hours of operation:	 \n"
		+ "\n"
		+ " 1) Click the \"Change Hours of Operation\" button from the options panel. This will bring up the \"Change Hours\" dialog. \n"
		+ " 2) You must enter in two times into this dialog: the start and end time. The start time should come before the end time, and both should be typed in military time (00:00 - 23:59). \n"
		+ " 3) If invalid times are entered in the fields, the user will be prompted to fix the input. \n"
		+ " 4) If there have been appointments set and hours of operation for a day change, these appointments will be cleared. The user is prompted when such a situation is going to occur. \n"
		+ "\n"
		+ " 2.3 Adding practitioners:	 \n"
		+ "\n"
		+ " 1) Click the \"Add Practitioner\" button from the options panel. This will bring up the \"Add Practitioner\" dialog. \n"
		+ " 2) If trying to add an existing practitioner to the day's roster, select a practitioner from the Existing Practitioners table. \n"
		+ " 3) If trying to add a new practitioner to the day's roster, click on the \"New Practitioner\" tab and fill out the practitioner's information. \n"
		+ " 4) If the type of the new practitioner does not appear in the types combo box, click \"New Type\" to enter a new type. \n"
		+ " 5) Store helpful notes about the practitioner, such as information about limited hours, in the \"Practitioner notes\" field. \n"
		+ " 6) When the practitioner is selected/entered, click \"Ok\" to add him or her to the day's roster. \n"
		+ "\n"
		+ " 2.4 Removing a practitioner from a day's roster:	 \n"
		+ "\n"
		+ " 1) Click on the desired practitioner in the Day View. When selected, the practitioner information box will turn from light green to dark green. \n"
		+ " 2) Click \"Remove Practitioner\" from the options panel. \n"
		+ " 3) Confirm that this practitioner should be removed. This operation will remove all scheduled appointments, and cannot be taken back. \n"
		+ "\n"
		+ " 2.5 Editing practitioners: \n"
		+ "\n"
		+ " 1) At the top menu, click \"File\" -> \"Edit Practitioners\" to open the \"Edit Practitioners\" dialog. \n"
		+ " 2) To edit an existing practitioner, select the practitioner from the table and click the \"Edit\" button. Any of the practitioner's information can be changed from here. NOTE: If the appointment length is modified, the practitioner's old appointment length will be preserved wherever that practitioner is currently appointed. For future appointments, the new value will be used. \n"
		+ " 3) To remove a practitioner, select the practitioner from the table and click \"Remove\". Old data will be preserved, but this practitioner can never be scheduled again in the future. \n"
		+ " 4) To create a new practitioner, click \"New\" and enter the new practitioner's information. \n"
		+ "\n"
		+ " 2.6 Scheduling appointments:	 \n"
		+ " 1) To schedule a patient with a practitioner on the current day's roster, click on an empty appointment slot and click \"Schedule Patient\" in the options panel. \n"
		+ " 2) Select an existing patient from the Existing Patients table, or click the \"New Patient\" tab to enter in a new patient's information, including an optional patient note (helpful for keeping track of patients who don't show up to appointments). When the patient has been selected/entered, click \"Ok\". \n"
		+ " 3) If you try and schedule a patient with a note, a pop up dialog will appear, asking you to confirm the appointment considering that the patient has a note attached. \n"
		+ " 4) To change a patient from an existing appointment, you can select the appointment block in the day view, turning the block red, and click \"Remove Patient\" to clear the appointment, then click \"Schedule Patient\" to enter a new patient. Alternatively, you can double click on the appointment and click \"Change Patient\" to select a new patient. \n"
		+ "\n"
		+ " 2.7 Adding or editing an appointment note	 \n"
		+ " 1) To add or modify a note for a particular appointment, double click on the specific appointment in the day view. This will bring up the \"Appointment Details\" dialog. \n"
		+ " 2) The existing note is listed in the \"Appointment Note\" field (if there is no note, this field is blank). Type the modifications to the appointment into this field. \n"
		+ " 3) Click \"Ok\" to make the changes. The updated note should appear next to the time slot for the appointment in the day view. \n"
		+ "\n"
		+ " 2.8 Removing scheduled appointments:	 \n"
		+ " 1) To remove a scheduled appointment, click on the desired appointment. The block should turn red, indicating it has been selected. \n"
		+ " 2) Click the \"Remove Patient\" button from the options panel to clear the appointment. \n"
		+ "\n"
		+ " 2.9 Viewing the wait list:	 \n"
		+ " 1) Click \"Show Wait List\" from the options panel to toggle the Wait List pane. \n"
		+ " 2) To select a specific wait list, choose the desired wait list from the \"Select Practitioner Type\" combo box. This will bring up a list of patients on the waitlist for the specific type of practitioner. To view a general wait list, select \"View All\" from the combo box. \n"
		+ " 3) To hide it again, click \"Hide Wait List\". \n"
		+ "\n"
		+ " 2.10 Adding a patient to the wait list:	 \n"
		+ " 1) To add a patient to a wait list, click on the \"Add Patient to Waitlist\" button in the Waitlist Pane. \n"
		+ " 2) Click \"Select Patient\" to choose the desired patient from the existing patient list, or to enter in a new patient. When a patient is selected, the text that reads \"No Patient Selected\" will indicate the patient's name. \n"
		+ " 3) Select the specific practitioner type the patient is waiting for from the \"Specific Wait List\" combo box. \n"
		+ " 4) Enter in any desired comment about the patient in the comments field. \n"
		+ " 5) Click \"Ok\" to add the patient to the wait list.	 \n"
		+ "\n"
		+ " 2.11 Removing a patient from the wait list:	 \n"
		+ " 1) Click on the patient from the wait list and click \"Remove Patient from Waitlist\" from the wait list pane to remove the patient from the specific wait list. \n"
		+ "\n"
		+ " 2.12 Scheduling a patient from the wait list:	 \n"
		+ " 1) In the current version, there is no direct way to put a patient from the wait list into an appointment. You must first schedule the patient into an appointment, and then manually remove him or her from the wait list. \n"
		+ " 2) Select the appointment, click \"Schedule Patient\" from the options panel, and find the patient from the wait list. \n"
		+ " 3) Select the patient in the wait list pane and click \"Remove Patient from Waitlist\" from the wait list pane to remove the patient that has just been scheduled. \n"
		+ "\n"
		+ " 2.13 Editing patients:	 \n"
		+ " 1) At the top menu, click \"File\" -> \"Edit Patients\" to open the \"Edit Patients\" dialog. \n"
		+ " 2) To edit an existing patient, select the patient from the table and click the \"Edit\" button. Any of the patients's information can be changed from here, including adding a note if necessary.	 \n"
		+ " 3) To remove a patient, select the patient from the table and click \"Remove\". Old data will be preserved, but this patient can never be scheduled again in the future. \n"
		+ " 4) To create a new patient, click \"New\" and enter the new patient's information. \n"
		+ "\n"
		+ " 2.14 Printing the current day view:	 \n"
		+ " 1) Make sure the desired day is selected on the calendar on the left side of the main window.	 \n"
		+ " 2) From the main menu, select \"File\" -> \"Print\". Choose the desired printing options (it is recommended to select the default options), and hit \"Ok\". \n"
		+ " 3) NOTES: It is suggested the defualt options are chosen. This will print the current day, 3 practitioners to a page. The printing functionality does not work very well if there are more than 4 hours for the current day's time slot. \n"
		+ "\n"
		+ " 2.15 Printing the current month view:	 \n"
		+ " 1) Make sure the desired month is selected, and the calendar is in the the month view. \n"
		+ " 2) From the main menu, select \"File\" -> \"Print\". Choose the desired printint options (it is recommended to select the default options), and hit \"Ok\".	 \n"
		+ " 3) NOTES: It is suggested the defualt options are chosen. This will print the calendar in portrait mode, where each each day of the month lists the types of each practitioner working on that day with a (*) indicating that the practitioner is full. \n"
		+ "\n"
		+ " 2.16 Switching to the month view:	 \n"
		+ " 1) To switch to the month view, click the \"Switch to Month View\" button from the options panel. \n"
		+ " 2) All of the days with practitioners scheduled will be listed in white. All other days will be gray. For days in white, the list of practitioners will be included, as well as a tag *FULL* that appears if the practitioner is fully booked for the specific day. \n"
		+ "\n"
		+ " 2.17 Switching to the day view:	 \n"
		+ " 1) To switch back to the day view from the month view, click the \"Switch to Day View\" button from the options panel. \n"
		+ "\n"
		+ " 2.18 Setting the default hours for a day:	 \n"
		+ " 1) To change the default hours of each day, click \"File\" -> \"Edit Default Hours\". \n"
		+ " 2) Enter in the desired default hours for each day and click \"Ok\". Every new day that is selected from the calendar will set this as it's default time slot. \n"
		;

	
	private static HelpUI helpUI;
	
	private JButton okButton = new JButton("OK");
//	private JButton cancelButton = new JButton("Cancel");
//	private JTextField helpText = new JTextField();
	private JTextArea textArea;
	

	

	public HelpUI(String s) {
		setModal(true);
		setTitle(s);
		
		add(helpWindow(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		
	}
  /*  public void componentResized(ActionEvent e) {
		int MIN_WIDTH = 200;
		int MIN_HEIGHT = 200;
		if (getWidth()<MIN_WIDTH) setSize(MIN_WIDTH,getHeight());
		//int width = getWidth();
	//	pack();
    }
    */
	private JComponent helpWindow() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(true);
		textArea.setHighlighter(null);
		add(textArea, BorderLayout.CENTER);
    	textArea.setText(helpText);
    	textArea.setCaretPosition(0);

    	//helpText.addKeyListener(this);
    	//String s1 = new StringBuilder().append("Help1\n\n Help2").append("\n\n").toString();
//    	helpText.setText(s1);
//    	helpText.setEditable(false);

    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.setFont(Constants.DIALOG);
    	okButton.addActionListener(this);
    	buttonPanel.add(okButton);
    	
//    	cancelButton.setActionCommand("cancel");
//    	cancelButton.addActionListener(this);
//    	buttonPanel.add(cancelButton);
    	textArea.setFont(Constants.PARAGRAPH);
    	panel.add(textArea,BorderLayout.NORTH);
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(575,520));
        scrollPane.setMinimumSize(new Dimension(200,200));
        
    //    p.add(helpText, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.NORTH);
        p.add(buttonPanel, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(575,550));
        p.setMinimumSize(new Dimension(575,550));
        return p;
    }
	/*
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
	*/
	public static void ShowDialog(Component owner) {
		helpUI = new HelpUI("Help Window");
		helpUI.pack();
		helpUI.setLocationRelativeTo(owner);
		helpUI.setVisible(true);
	}
		
	public void keyTyped(KeyEvent arg0) {
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			helpUI.setVisible(false);
		} 
	}
}
