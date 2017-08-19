//Maja Lund, 920723-0765

import javax.swing.*;

public class AddDescribedPlace extends AddPlace{

	private JTextField description = new JTextField(10);

	public AddDescribedPlace(){
		JPanel secondRow = new JPanel();
		secondRow.add(new JLabel ("Description: "));
		secondRow.add(description);
		add(secondRow);
	}

	public String getDescription(){
		return description.getText();
	}
}