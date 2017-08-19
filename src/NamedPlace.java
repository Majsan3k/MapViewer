//Maja Lund, 920723-0765

public class NamedPlace extends Place {

	protected String name;

	public NamedPlace(String placeType, String category, Position position, String name){
		super(position, category);
		this.name = name;
	}

	public String toString(){
		return(name + " " + getCategory() + " " + getPosition().getX() + " " + getPosition().getY());
	}

	public String getName(){
		return name;
	}
}