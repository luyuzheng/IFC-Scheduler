package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.WaitlistDto;

/**
 * Dialog popup that allows the date entered set for a waitlist entry to be changed in order
 * to alter the priorities.
 */
@SuppressWarnings("serial")
public class ChangePriorityUI extends JDialog implements ActionListener {
	private static ChangePriorityUI changePriorityUI;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JButton moveToTopButton = new JButton("Move to Top");
	
	private JTextField dateField;
	private JTextField timeField;
	
	private WaitlistDto entry;
	
	private static int change = -1;
	
	private ChangePriorityUI(String name) {
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		
		JPanel instructionsPanel = new JPanel(new BorderLayout());
		instructionsPanel.setBorder(new EmptyBorder(10,10,10,10));
		JLabel instructions = new JLabel();
		instructions.setText("<html>Entries in waitlist are sorted by date added.<br>" +
				"The priority of the entry can be changed by changing the date added.<br>" +
				"You can also click the Move to Top button to move an entry directly to " +
				"the top of the waitlist.</html>");
		instructions.setFont(Constants.PARAGRAPH);
		instructionsPanel.add(instructions, BorderLayout.CENTER);
		
		add(instructionsPanel, BorderLayout.NORTH);
		
		JPanel fieldsAndMoveButtonPanel = new JPanel();
		fieldsAndMoveButtonPanel.setLayout(new GridLayout(5, 1));
		fieldsAndMoveButtonPanel.setBorder(new EmptyBorder(10,10,10,10));
		moveToTopButton.addActionListener(this);
		moveToTopButton.setActionCommand("move to top");
		moveToTopButton.setFont(Constants.DIALOG);
		fieldsAndMoveButtonPanel.add(moveToTopButton);
		
		JLabel dateFieldLabel = new JLabel();
		dateFieldLabel.setText("Date: (YYYY-MM-DD)");
		dateFieldLabel.setFont(Constants.PARAGRAPH);
		fieldsAndMoveButtonPanel.add(dateFieldLabel);

		dateField = new JTextField();
		dateField.setFont(Constants.DIALOG);
		fieldsAndMoveButtonPanel.add(dateField);
		
		JLabel timeFieldLabel = new JLabel();
		timeFieldLabel.setText("Time: (00:00 - 23:59)");
		timeFieldLabel.setFont(Constants.PARAGRAPH);
		fieldsAndMoveButtonPanel.add(timeFieldLabel);
		
		timeField = new JTextField();
		timeField.setFont(Constants.DIALOG);
		fieldsAndMoveButtonPanel.add(timeField);
		
		add(fieldsAndMoveButtonPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		okButton.addActionListener(this);
		okButton.setActionCommand("ok");
		okButton.setFont(Constants.DIALOG);
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		cancelButton.setFont(Constants.DIALOG);
		buttonPanel.add(cancelButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(330, 350));
		setResizable(false);
		
	}
	
	/**
	 * This causes this dialog box to pop up in the middle of the specified owner.
	 * This method will return 0 if everything went smoothly or -1 if otherwise or canceled
	 */
	public static int ShowDialog(Component owner, WaitlistDto entry) {
		changePriorityUI = new ChangePriorityUI("Change Priority");
		changePriorityUI.entry = entry;
		changePriorityUI.pack();
		changePriorityUI.setLocationRelativeTo(owner);
		changePriorityUI.setVisible(true);
		return change;
	}
	
	public void actionPerformed(ActionEvent e) {
		change = -1;
		JLabel msg = new JLabel();
		msg.setFont(Constants.PARAGRAPH);
		if (e.getActionCommand().equals("ok")) {
			if (!checkDate()) {
				msg.setText("The date and/or time is invalid.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Timestamp time;
			try {
				time = new Timestamp(
						dateFormat.parse(dateField.getText() + " " + timeField.getText()).getTime());
			} catch (ParseException excptn) {
				msg.setText("The date and/or time is invalid.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			boolean success = DataServiceImpl.GLOBAL_DATA_INSTANCE.updateWaitlistTime(entry, time);
            change = success ? 0 : -1;
		} else if (e.getActionCommand().equals("move to top")) {
			Timestamp time = DataServiceImpl.GLOBAL_DATA_INSTANCE.getOldestWaitlistTime();
			time = new Timestamp(time.getTime() - 1);
			boolean success = DataServiceImpl.GLOBAL_DATA_INSTANCE.updateWaitlistTime(entry, time);
			change = success ? 0 : -1;
		}
		changePriorityUI.setVisible(false);
    }
	
	private boolean checkDate() {
		if (!dateField.getText().trim().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			return false;
		}
		if (!timeField.getText().trim().matches("[0-2]\\d:[0-5]\\d")) {
			return false;
		}
		return true;
	}
}