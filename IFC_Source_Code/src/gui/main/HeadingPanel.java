/**
 * The HeadingPanel is the panel above the list of appointments for a room/practitioner. It lists
 * information about the practitioner. It also provides a focus listener for gui manipulation.
 */

package gui.main;

import gui.main.listeners.NewPractitionerListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Constants;
import data.Room;

public class HeadingPanel extends JPanel implements FocusListener {
	private Room room;
	private JTextArea text;
	private RoomSubpanel rs;
	private RoomPanel rp;
	NewPractitionerListener npl;
	
	public HeadingPanel(Room room, RoomPanel rp, RoomSubpanel rs) {
		this.rp = rp;
		this.rs = rs;
		this.room = room;
		text = new JTextArea();
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setFont(new Font("Tahoma",Font.PLAIN,11));
		text.setOpaque(false);
		text.setHighlighter(null);
		
		setText();
		setBackground(new Color(215,255,215));
		setPreferredSize(new Dimension(Constants.APPT_SUBPANEL_WIDTH,Constants.HEADING_PANEL_HEIGHT));
		setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));
		setLayout(new BorderLayout());
		add(text);
		text.addFocusListener(this);
	}
	
	public void setText() {
		if (room.getPractitioner() == null) 
			text.setText("No Practitioner Selected. \nDouble-click below to add Practitioner.");
		else 
			text.setText(room.getPractitioner().getName() + "\n" + room.getPractitioner().getType() + "\n" + room.getPractitioner().getNote());
			
	}

	/**
	 * When focus is gained, register it with the DayPanel
	 */
	public void focusGained(FocusEvent arg0) {
		rs.getDayPanel().setRemovePracButtonEnabled(true, rp);
		setBackground(new Color(175, 255, 175));
		npl = new NewPractitionerListener(rp, this.getParent());
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
