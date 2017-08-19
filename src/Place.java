//Maja Lund, 920723-0765

import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

abstract class Place extends JComponent{

	private Color categoryColor;
	private Position position;
	protected String categoryName;
	private boolean folded = false;  //show place as a triangle while folded
	private boolean selected = false;

	public Place(Position position, String categoryName){
		this.position = position;
		this.categoryName = categoryName;

		switch(categoryName){
		case "Bus" :
			categoryColor = Color.RED;
			break;
		case "Metro" :
			categoryColor = Color.BLUE;
			break;
		case "Train":
			categoryColor = Color.GREEN;
			break;
		case "None":
			categoryColor = Color.BLACK;
			break;
		default :
			throw new IllegalArgumentException("Category \""+categoryName+"\" is in wrong format, it must be UTF-8!");
		}
		setVisible(true);
		setBounds((position.getX()-10), (position.getY()-20), 20, 20);
		addMouseListener(new FoldedPlace());
	}

	public void setFolded(boolean b){
		this.folded = b;
		if(!folded){
			setBounds((position.getX()-10), (position.getY()-20), 20, 20);
		}else{
			setBounds((position.getX()-10), (position.getY()-20), 50, 50);			
			}
		}

	public void setSelected(boolean b){
		if(b){
			setBorder(BorderFactory.createLineBorder(Color.red));
			this.selected = true;
		}else{
			setBorder(BorderFactory.createEmptyBorder());
			this.selected = false;
		}
	}

	public boolean getFolded(){
		return folded;
	}

	public boolean getSelected(){
		return selected;
	}

	public String getCategory(){
		return categoryName;		
	}

	public Position getPosition(){
		return position;
	}

	public Color getColor(){
		return categoryColor;
	}

	@Override
	protected void paintComponent(Graphics g){
		if(!folded){
			int xpoints[] = {0, 10, 20};
			int ypoints[] = {0, 20, 0};

			g.setColor(categoryColor);
			g.fillPolygon(xpoints, ypoints, 3);
		}else{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 50, 50);
			g.setColor(Color.BLACK);
			g.drawString(this.getName(), 0,20);
		}
	}

	class FoldedPlace extends MouseAdapter{
		public void mouseClicked(MouseEvent mev){
			if(mev.getButton() == MouseEvent.BUTTON3){
				boolean folded = getFolded(); 
				folded = !folded;
				setFolded(folded);
				repaint();
			}
		}
	}
}