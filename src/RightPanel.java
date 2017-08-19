//Maja Lund, 920723-0765

import java.awt.*;
import javax.swing.*;

public class RightPanel extends JPanel {

	private JList <String> categoryList;
	private JButton hideCategory;

	public RightPanel(JList <String> categoryList, JButton hideCategory){
		this.categoryList = categoryList;
		this.hideCategory = hideCategory;

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel categoriesLabel = new JLabel("Categories");
		categoriesLabel.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(categoriesLabel);

		categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categoryList.setFixedCellWidth(110);
		categoryList.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(categoryList);

		hideCategory.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(hideCategory);

		c.gridx = 0;
		c.gridy = 1;
		add(panel, c);
	}
}