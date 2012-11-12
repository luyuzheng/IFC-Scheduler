package gui.sub;

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

import data.Time;
import gui.TimeSlot;

public class SelectTimeSlotUI extends JDialog implements ActionListener {

	private static SelectTimeSlotUI selectTimeSlotUI;
	private static TimeSlot timeSlot;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JButton okButton;
	private JButton cancelButton;
	private Font font= new Font("Tahoma", Font.PLAIN, 14);
	
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
    	label.setFont(font);
    	startTimeField.setFont(font);
    	panel.add(label);
    	panel.add(startTimeField);
    	
    	label = new JLabel("End Time (00:00 - 23:59, greater than start time): ");
    	label.setFont(font);
    	endTimeField.setFont(font);
    	panel.add(label);
    	panel.add(endTimeField);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.addActionListener(this);
    	cancelButton.addActionListener(this);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(buttonPanel);
    	
    	return panel;
    }
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			Time t1, t2;
			String time1 = startTimeField.getText();
			if (time1.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a start time.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (time1.matches("[0-2][0-9]:[0-5][0-9]")) {
				String hour = time1.replaceAll(":[0-5][0-9]", "");
				String minute = time1.replaceAll("[0-2][0-9]:", "");
				try { 
					t1 = new Time(Integer.parseInt(hour), Integer.parseInt(minute));
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Invalid start time.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(this, "Invalid start time.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String time2 = endTimeField.getText();
			if (time2.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter an end time.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (time2.matches("[0-2][0-9]:[0-5][0-9]")) {
				String hour = time2.replaceAll(":[0-5][0-9]", "");
				String minute = time2.replaceAll("[0-2][0-9]:", "");
				try { 
					t2 = new Time(Integer.parseInt(hour), Integer.parseInt(minute));
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Invalid end time.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(this, "Invalid end time.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (t1.timeInMinutes() >= t2.timeInMinutes()) {
				JOptionPane.showMessageDialog(this, "End time must be after start time.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (JOptionPane.showConfirmDialog(this, "Changing the day's hours will clear any appointments that have been set. Continue?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
			timeSlot = new TimeSlot(t1,t2);
			
		}
		selectTimeSlotUI.setVisible(false);
    }
}
