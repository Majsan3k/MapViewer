//Maja Lund, 920723-0765

import javax.swing.*;

public class AddPlace extends JPanel{

	private JTextField newName = new JTextField(10);

	public AddPlace(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel firstRow = new JPanel();
		firstRow.add(new JLabel ("Name: "));
		firstRow.add(newName);
		add(firstRow);
	}

	public String getName(){
		return newName.getText();
	}
}