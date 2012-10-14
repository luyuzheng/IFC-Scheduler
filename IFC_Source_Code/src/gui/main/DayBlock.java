package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Constants;
import data.Date;
import data.Day;
import data.DayLoader;
import data.Room;

public class DayBlock extends JPanel {

	private JTextArea textArea;
	
	public DayBlock(Date date) {
		this(date, Color.WHITE);
	}
	
	public DayBlock(Date date, Color color) {
		textArea = new JTextArea();
		String text = date.toFormalString() + "\n\n";
		
		DayLoader dl = new DayLoader();
		Day d = dl.loadDay(date);
		if (color.equals(Color.WHITE) && d == null) color = new Color(220,220,220);
		else if (color.equals(Color.WHITE)){
			boolean hasPrac = false;
			for (Room r : d.getRooms()) {
				if (r.hasPrac()) {
					text += r.getPractitioner().getName() + " - " + r.getPractitioner().getType();
					if (r.isFull()) text += " *FULL* ";
					text += "\n";
					hasPrac = true;
				}
			}
			if (!hasPrac) color = new Color(220,220,220);
		}
			
		textArea.setText(text);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		setPreferredSize(new Dimension(Constants.MONTH_BLOCK_WIDTH, Constants.MONTH_BLOCK_HEIGHT));
		setBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK));
		setLayout(new BorderLayout());
		setBackground(color);
		add(textArea);
		
	}
	
}
