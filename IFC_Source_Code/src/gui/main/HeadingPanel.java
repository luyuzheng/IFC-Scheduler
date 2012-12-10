/**
 * The HeadingPanel is the panel above the list of appointments for a room/practitioner. It contains
 * information about the practitioner, such as name and practitioner type. It also provides a focus 
 * listener for gui manipulation.
 */

package gui.main;

import gui.main.listeners.NewPractitionerListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataTransferObjects.SchedulePractitionerDto;

import gui.Constants;

@SuppressWarnings("serial")
public class HeadingPanel extends JPanel implements FocusListener {
	/** The scheduled practitioner. */
	private SchedulePractitionerDto room;
	/** The text to display in the heading panel. */
	private JTextArea text;
	/** The room subpanel containing the appointment blocks for the scheduled practitioner. */
	private RoomSubpanel rs;
	/** The heading and time slots for the scheduled practitioner. */
	private RoomPanel rp;
	/** The mouse listener for the heading panel. */
	NewPractitionerListener npl;
	
	/** Constructs the heading panel based on the scheduled practitioner. */
	public HeadingPanel(SchedulePractitionerDto room, RoomPanel rp, RoomSubpanel rs) {
		this.rp = rp;
		this.rs = rs;
		this.room = room;
		text = new JTextArea();
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setFont(Constants.DIALOG);
		text.setOpaque(false);
		text.setHighlighter(null);
		
		setText();
		setBackground(new Color(215,255,215));
		setPreferredSize(new Dimension(Constants.APPT_SUBPANEL_WIDTH,Constants.HEADING_PANEL_HEIGHT));
		//setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK), new EmptyBorder(5,5,5,5)));
		setLayout(new BorderLayout());
		add(text);
		text.addFocusListener(this);
	}
	
	/** Sets the text for the heading panel. */
	public void setText() {
		if (room.getPractitioner() == null) { 
			text.setText("No Practitioner Selected. \nDouble-click below to add Practitioner.");
		} else { 
			text.setText(room.getPractitioner().getFirst() + " "+ room.getPractitioner().getLast() + 
					"\n" + room.getPractitioner().getTypeName() + "\n" + room.getPractitioner().getShortNote(30));			
			text.setToolTipText("Double-click to see more information");
		}
	}

	/**
	 * When focus is gained, register it with the DayPanel
	 */
	public void focusGained(FocusEvent arg0) {
		rs.getDayPanel().setRemovePracButtonEnabled(true, rp);
		setBackground(new Color(175, 255, 175));
		npl = new NewPractitionerListener(rp, this.getParent().getParent().getParent().getParent()); // the final parent is the MainWindow
		text.addMouseListener(npl);
	}

	/**
	 * When focus is lost, unregister from the DayPanel
	 */
	public void focusLost(FocusEvent e) {
		rs.getDayPanel().setRemovePracButtonEnabled(false, null);
		setBackground(new Color(215, 255, 215));
		text.removeMouseListener(npl);
	}

}
