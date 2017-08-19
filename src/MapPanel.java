//Maja Lund, 920723-0765

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

	private ImageIcon map;

	public MapPanel(String mapName){
		setLayout(null);
		map = new ImageIcon(mapName);
		int w = map.getIconWidth();
		int h = map.getIconHeight();
		setPreferredSize(new Dimension(w,h));
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(map.getImage(), 0, 0, this);
	}
}