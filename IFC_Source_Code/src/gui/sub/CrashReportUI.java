package gui.sub;

import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CrashReportUI extends JDialog {
	private static CrashReportUI crashReportUI;

	private JButton saveButton = new JButton("Save Report");
	private JButton closeButton = new JButton("Close");
	private JTextArea report;
	
	private Exception exceptionThrown;
	
	private String crashDescriptionString = "<b>The application appears to have crashed!</b> " +
			"We are really sorry! To help fix these bugs and improve our product, " +
			"please save a crash report and email it and the error logs to either Daisy Fan, " +
			"or the development team. " +
			"The crash report will be saved to a file called \"report.log\" with the errors logged " +
			"in \"error.log\" in the directory where the application is installed.";

	private CrashReportUI(Exception e) {
		exceptionThrown = e;
		
		setModal(true);
		setTitle("Save Crash Report");

		setPreferredSize(new Dimension(450,400));
		setLayout(new BorderLayout());
		
		JPanel reportPanel = new JPanel();
		reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
		reportPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		JLabel description = new JLabel("<html>" + crashDescriptionString + "</html>");
		description.setFont(Constants.PARAGRAPH);
		descriptionPanel.add(description, BorderLayout.CENTER);
		JLabel reportLabel = new JLabel("<html><br>Describe what you were doing:</html>");
		reportLabel.setFont(Constants.PARAGRAPH);
		descriptionPanel.add(reportLabel, BorderLayout.PAGE_END);
		reportPanel.add(descriptionPanel);
		
		report = new JTextArea();
		report.setFont(Constants.PARAGRAPH);
		report.setEditable(true);
		report.setLineWrap(true);
		report.setWrapStyleWord(true);
		JScrollPane scroller = new JScrollPane(report);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setPreferredSize(new Dimension(280, 400));
		scroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		reportPanel.add(scroller);
		add(reportPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		saveButton.addActionListener(saveReport);
		saveButton.setActionCommand("save");
		saveButton.setFont(Constants.DIALOG);
		closeButton.addActionListener(close);
		closeButton.setActionCommand("close");
		closeButton.setFont(Constants.DIALOG);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(closeButton);
		
		add(buttonsPanel, BorderLayout.PAGE_END);
	}
	
	public static int ShowDialog(Exception e) {
		crashReportUI = new CrashReportUI(e);
		crashReportUI.pack();
		crashReportUI.setLocationByPlatform(true);
		crashReportUI.setVisible(true);
		System.exit(0); //TODO find a more elegant way of closing the UI thread
		return 0;
	}

	private ActionListener saveReport = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				FileWriter fileWriter = new FileWriter("report.log");
				BufferedWriter out = new BufferedWriter(fileWriter);
				out.write("REPORT:\r\n" + report.getText() + "\r\n");
				out.write("EXCEPTION MESSSAGE:\r\n" + exceptionThrown.getMessage() + "\r\n");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exceptionThrown.printStackTrace(pw);
				out.write("STACK TRACE:\r\n" + sw.toString() + "\r\n");
				out.close();
			} catch (Exception e) {
				// TODO: failed to save report
			} finally {
				setVisible(false);
			}
		}
	};
	
	private ActionListener close = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			crashReportUI.setVisible(false);
		}
	};
}
