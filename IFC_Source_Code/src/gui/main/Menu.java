/**
 * Creates a menu bar for the scheduler with "Administrator" and "Help" submenus.
 * These menus contain information not intended for the average user; it allows the 
 * administrator to execute more complicated tasks. 
 */

package gui.main;
import gui.Constants;
import gui.sub.DefaultTimeslotsUI;
import gui.sub.EditPatientsUI;
import gui.sub.EditPractitionersUI;
import gui.sub.EditTypeUI;
import gui.sub.HelpUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Menu extends JMenuBar {
	
	/** Constructs the menu bar for the scheduler given the main window object. */
	public Menu(MainWindow window) {

		
		/** The main window associated with the scheduler. */
		final MainWindow frame = window;
		
		setBorder(new EmptyBorder(5,5,5,5));

		//File menu
		JMenu fileMenu= new JMenu("File");
		fileMenu.setFont(Constants.PARAGRAPH);
		
		//Print the schedule for today
		JMenuItem printItem = new JMenuItem("Print");
		printItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frame.inMonthView()) frame.getMonthPanel().actionPerformed(e);
				else frame.getAppPanel().actionPerformed(e);
			}
		});
		printItem.setFont(Constants.PARAGRAPH);
		fileMenu.add(printItem);	
		fileMenu.addSeparator();
		
		//Quit the scheduler
		JMenuItem byeMenuItem = new JMenuItem("Quit");
		byeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		byeMenuItem.setFont(Constants.PARAGRAPH);
		fileMenu.add(byeMenuItem);
		
		add(fileMenu);
		
		//Administrator menu
		JMenu adminMenu = new JMenu("Administrator");
		adminMenu.setFont(Constants.PARAGRAPH);
		
		//Edit all patients
		JMenuItem editPatientsItem = new JMenuItem("Edit Patients");
		editPatientsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditPatientsUI.ShowDialog(frame);
				frame.refresh();
			}
		});
		editPatientsItem.setFont(Constants.PARAGRAPH);
		adminMenu.add(editPatientsItem);
		
		//Edit all practitioners
		JMenuItem editPractitionersItem = new JMenuItem("Edit Practitioners");
		editPractitionersItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            
				EditPractitionersUI.ShowDialog(frame);
                                
			}
		});
		editPractitionersItem.setFont(Constants.PARAGRAPH);
		adminMenu.add(editPractitionersItem);
		
		JMenuItem editServiceTypesItem = new JMenuItem("Edit Service Types");
		editServiceTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditTypeUI.ShowDialog(frame);
			}
		});
		editServiceTypesItem.setFont(Constants.PARAGRAPH);
		adminMenu.add(editServiceTypesItem);
		
		//Change defaults hours of operation by day of week
		JMenuItem editDefaultHoursItem = new JMenuItem("Edit Default Hours");
		editDefaultHoursItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTimeslotsUI.ShowDialog(frame);
			}
		});
		editDefaultHoursItem.setFont(Constants.PARAGRAPH);
		adminMenu.add(editDefaultHoursItem);
		
		add(adminMenu);

		//Help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(Constants.PARAGRAPH);

		//Displays help information
		JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);    //This is how you add a hotkey
		helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	/*			File file = new File("README");
				if (file.exists()) {
					try {
						Runtime.getRuntime().exec("notepad.exe README");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "The help file could not be opened.", "Error!", JOptionPane.ERROR_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(frame, "The help file could not be found.", "Error!", JOptionPane.ERROR_MESSAGE);
*/
				HelpUI.ShowDialog(frame);		
				frame.refresh();
			}
		});
		helpItem.setFont(Constants.PARAGRAPH);
		helpMenu.add(helpItem);
		
		//Displays information about developers
		JMenuItem item = new JMenuItem("About...", KeyEvent.VK_A);    //This is how you add a hotkey
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String[] aboutMsg = { "IFC Scheduling Program",
				"Copyright \u00a9 2010 Aakash Jain, Rick Ducott; Cornell University \n\u00a9 2012 The Dev Squad \nClaire Cipriani, Kenneth Deakins, Jean Hooi Lee, Raisa Razzaque, Valerie Roske, Luyu Zheng; Cornell University" };
				JOptionPane.showMessageDialog(frame, aboutMsg, "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		item.setFont(Constants.PARAGRAPH);
		helpMenu.add(item);

		add(helpMenu);
	}
}
