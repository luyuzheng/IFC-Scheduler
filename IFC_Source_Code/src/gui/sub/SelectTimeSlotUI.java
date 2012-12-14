package gui.sub;

import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.Constants;
import gui.TimeSlot;

/**
 * Popup that allows the hours of operation in a day to be changed.
 * It contains two simple fields where a start time and end time can be set.
 * The fields must be set in format HH:mm between the range 00:00 and 23:59.
 * The end time must also be greater than the start time.
 * This popup is used by the DayPanel, which will change the hours set in the
 * day view when OK is clicked. Practitioner what already have appointments
 * in the day will lose appointments that are outside the range of the day.
 */
public class SelectTimeSlotUI extends JDialog implements ActionListener {

	private static SelectTimeSlotUI selectTimeSlotUI;
	private static TimeSlot timeSlot;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JButton okButton;
	private JButton cancelButton;
	
	private SelectTimeSlotUI(String name) {
		timeSlot = null;
		setModal(true);
		setTitle(name);
		startTimeField = new JTextField();
		endTimeField = new JTextField();
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		setLayout(new BorderLayout());
		add(makeTimeSlotPanel(), BorderLayout.CENTER);
		setResizable(false);
		pack();
	}
	
	public static TimeSlot ShowDialog(Component owner) {
		selectTimeSlotUI = new SelectTimeSlotUI("Change Hours");
		selectTimeSlotUI.setLocationRelativeTo(owner);
		selectTimeSlotUI.setVisible(true);
		
		return timeSlot;
	}
	
	private JComponent makeTimeSlotPanel() {
    	JPanel panel = new JPanel(new GridLayout(0,1));
    	
    	JLabel label = new JLabel("Start Time (00:00 - 23:59): ");
    	label.setFont(Constants.PARAGRAPH);
    	startTimeField.setFont(Constants.PARAGRAPH);
    	panel.add(label);
    	panel.add(startTimeField);
    	
    	label = new JLabel("End Time (00:00 - 23:59, greater than start time): ");
    	label.setFont(Constants.PARAGRAPH);
    	endTimeField.setFont(Constants.PARAGRAPH);
    	panel.add(label);
    	panel.add(endTimeField);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	cancelButton.addActionListener(this);
    	cancelButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(buttonPanel);
    	
    	return panel;
    }
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			int t1, t2;
			String time1 = startTimeField.getText();
			JLabel msg = new JLabel();
			msg.setFont(Constants.PARAGRAPH);
			if (time1.equals("")) {
				msg.setText("Please enter a start time.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (time1.matches("[0-2][0-9]:[0-5][0-9]")) {
				String hour = time1.replaceAll(":[0-5][0-9]", "");
				String minute = time1.replaceAll("[0-2][0-9]:", "");
				try { 
					t1 = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
				} catch(Exception ex) {
					msg.setText("Invalid start time.");
					JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				msg.setText( "Invalid start time.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String time2 = endTimeField.getText();
			if (time2.equals("")) {
				msg.setText("Please enter an end time.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (time2.matches("[0-2][0-9]:[0-5][0-9]")) {
				String hour = time2.replaceAll(":[0-5][0-9]", "");
				String minute = time2.replaceAll("[0-2][0-9]:", "");
				try { 
					t2 = 60 * Integer.parseInt(hour) + Integer.parseInt(minute);
				} catch(Exception ex) {
					msg.setText("Invalid end time.");
					JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				msg.setText("Invalid end time.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (t1 >= t2) {
				msg.setText("End time must be after start time.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			msg.setText("Changing the day's hours will clear any appointments that have been set. Continue?");
			if (JOptionPane.showConfirmDialog(this, msg, "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
			timeSlot = new TimeSlot(t1,t2);
			
		}
		selectTimeSlotUI.setVisible(false);
    }
}
