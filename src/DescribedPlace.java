//Maja Lund, 920723-0765

import java.awt.Graphics;

public class DescribedPlace extends NamedPlace {

	private String description;

	public DescribedPlace(String placeType, String category, Position position, String name, String description) {
		super(placeType, category, position, name);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return (getName() + " " + description + " " + getCategory() + " " + getPosition().getX() + " "
				+ getPosition().getY());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(((DescribedPlace) this).getDescription(), 0, 30);
	}
}