//Maja Lund, 920723-0765

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TopPanel extends JPanel{

	private JFrame parentFrame;

	private String[] categories = {"Bus", "Metro", "Train"};
	private JList <String>categoryList = new JList<>(categories);
	private JButton hideCategory = new JButton("Hide category");

	private HashSet<Place> markedPlaces = new HashSet<Place>(); 
	private HashMap<Position, Place> places = new HashMap<>();
	private HashMap<String, ArrayList<Place>> placesByName = new HashMap<>();
	private HashMap<String, ArrayList<Place>> placesByCategory = new HashMap<>();

	private CreatePlace mouseCP = new CreatePlace();
	private WhatIsHere mouseWIH = new WhatIsHere();
	private ActionListener actionCP;
	private ActionListener actionWIH;

	private boolean changed;
	private MapPanel newMap;
	private JScrollPane scroll = new JScrollPane();

	private String[] createPlace = {"Named", "Described"};
	private JMenu archive = new JMenu("Archive");
	private JComboBox<String> createNewPlace = new JComboBox<>(createPlace);

	private JTextField search;
	private JButton whatIsHere = new JButton("What is here?");
	private JButton searchButton = new JButton("Search");
	private JButton hide = new JButton("Hide");
	private JButton remove = new JButton("Remove");

	public TopPanel(JFrame parentFrame) {

		actionWIH = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setEnabledMenus(false);
				parentFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				newMap.addMouseListener(mouseWIH);
			}
		};

		actionCP= new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setEnabledMenus(false);
				parentFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				newMap.addMouseListener(mouseCP);
			}
		};

		hideCategory.addActionListener(event -> {
			String category = categoryList.getSelectedValue();
			ArrayList<Place> placeList = placesByCategory.get(category);
			if(placeList != null){
				for(Place p : placeList){
					p.setSelected(false);
					p.setVisible(false);
				}
			}
		});

		RightPanel rightPanel = new RightPanel(categoryList, hideCategory);
		categoryList.addListSelectionListener(new ChooseCategory());
		parentFrame.add(rightPanel, BorderLayout.EAST);

		JMenuBar balk = new JMenuBar();
		parentFrame.setJMenuBar(balk);
		balk.add(archive);

		JMenuItem newMap = new JMenuItem("New Map");
		archive.add(newMap);
		newMap.addActionListener(event -> {
			newMap(event);
		});
		JMenuItem loadPlaces = new JMenuItem("Load Places");
		archive.add(loadPlaces);
		loadPlaces.addActionListener(event -> {
			loadPlaces(event);
		});
		JMenuItem save = new JMenuItem("Save");
		archive.add(save);
		save.addActionListener(event -> {
			savePlaces(event);
		});
		JMenuItem exit = new JMenuItem("Exit");
		archive.add(exit);
		exit.addActionListener(ActionEvent->{
			parentFrame.dispatchEvent(new WindowEvent(parentFrame,WindowEvent.WINDOW_CLOSING));
		});
		this.parentFrame = parentFrame;
		setLayout(new GridLayout(0, 1));

		JPanel middlePanel = new JPanel();
		JLabel newPlace = new JLabel("New: ");
		middlePanel.add(newPlace);
		createNewPlace.setEditable(false);
		middlePanel.add(createNewPlace);
		search = new JTextField(10);
		middlePanel.add(search);
		middlePanel.add(searchButton);
		searchButton.addActionListener(event -> {
			search();
		});
		middlePanel.add(hide);
		hide.addActionListener(event -> {
			if(!markedPlaces.isEmpty()){
				for(Place p: markedPlaces){
					p.setVisible(false);
					p.setSelected(false);
				}
			}else{
				JOptionPane.showMessageDialog(TopPanel.this, "No place is marked");
			}
			markedPlaces.clear();
		});
		middlePanel.add(remove);
		remove.addActionListener(event -> {
			if(!markedPlaces.isEmpty()){
				removePlace();
				return;
			}
			JOptionPane.showMessageDialog(TopPanel.this, "No place is marked");
			return;
		});
		middlePanel.add(whatIsHere);
		add(middlePanel);
	}

	private void setEnabledMenus(boolean b){
		if(b == true){
			createNewPlace.setEnabled(true);
			whatIsHere.setEnabled(true);
			archive.setEnabled(true);
			search.setEnabled(true);
			searchButton.setEnabled(true);
			hide.setEnabled(true);
			remove.setEnabled(true);
			hideCategory.setEnabled(true);
			categoryList.setEnabled(true);
		}else{
			createNewPlace.setEnabled(false);
			whatIsHere.setEnabled(false);
			archive.setEnabled(false);
			search.setEnabled(false);
			searchButton.setEnabled(false);
			hide.setEnabled(false);
			remove.setEnabled(false);
			hideCategory.setEnabled(false);
			categoryList.setEnabled(false);
		}
	}

	public boolean getChanged(){
		return changed; 
	}

	private void search(){
		for(Place marked : markedPlaces){
			marked.setSelected(false);
			marked.repaint();
		}
		markedPlaces.clear();
		String searchName = search.getText();
		ArrayList<Place> placeList = placesByName.get(searchName);
		if(placeList != null){
			for(Place p : placeList){
				p.setVisible(true);
				p.setFolded(false);
				markedPlaces.add(p);
				p.setSelected(true);
				p.repaint();
			}
		}else{
			JOptionPane.showMessageDialog(TopPanel.this, "There is no place with that name");
		}
		search.setText("");
	}

	private void removePlace(){
		for(Place p : markedPlaces){ 
			String name = p.getName();
			ArrayList<Place>placeList = placesByName.get(name);
			ArrayList<Place>placeListCategory = placesByCategory.get(p.getCategory());

			if(placeList.contains(p)){
				p.setVisible(false);
				places.remove(p.getPosition());
				placeListCategory.remove(p);
				placeList.remove(p);

				if(placeList.isEmpty()){
					placesByName.remove(name);
				}
			}
		}
		markedPlaces.clear();
		changed = true;
		updateMap();
	}

	private boolean exit(){
		
		boolean newPlace = false;
	
		int svar = JOptionPane.showConfirmDialog(TopPanel.this, "Unsaved changes", "Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(svar == JOptionPane.OK_OPTION){
			for(Place p : places.values()){
				p.setVisible(false);
				newMap.remove(p);
			}
			markedPlaces.clear();
			places.clear();
			placesByName.clear();
			placesByCategory.clear();
			newPlace = true;
		}else{
			newPlace = false;
		}
		return newPlace;
	}
	
	private void newMap(ActionEvent e) {
		if(changed == true){
			if(exit() == false){
				return;
			}
		}

		JFileChooser selectMap = new JFileChooser("");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pictures (png, jpg)", "png", "jpg");
		selectMap.setFileFilter(filter);

		int svar = selectMap.showOpenDialog(this);
		if (svar != JFileChooser.APPROVE_OPTION){
			return;
		}else if(newMap != null){
			scroll.remove(newMap);
			parentFrame.remove(scroll);
			newMap.removeMouseListener(mouseCP);
			newMap.removeMouseListener(mouseWIH);
			createNewPlace.removeActionListener(actionCP);
			createNewPlace.removeActionListener(actionWIH);
		}
		File fil = selectMap.getSelectedFile();
		String mapPath = fil.getAbsolutePath();

		newMap = new MapPanel(mapPath);
		scroll = new JScrollPane(newMap);
		scroll.setBorder(null);
		parentFrame.add(scroll, BorderLayout.CENTER);
		parentFrame.pack();
		parentFrame.validate();
		parentFrame.repaint();
		changed = false;

		createNewPlace.addActionListener(actionCP);
		whatIsHere.addActionListener(actionWIH);
	}

	public void savePlaces(ActionEvent e1){   
		JFileChooser savePlaces = new JFileChooser("");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt");
		savePlaces.setFileFilter(filter);
		int svar = savePlaces.showOpenDialog(this);
		if (svar != JFileChooser.APPROVE_OPTION)
			return;

		try{
			FileWriter file = new FileWriter(savePlaces.getSelectedFile());
			PrintWriter out = new PrintWriter(file);
			for(Place p : places.values()){

				String type = p.getClass().getName();
				String typeName = null;
				if(type.equals("NamedPlace")){
					typeName = "Named";
				}else if(type.equals("DescribedPlace")){
					typeName = "Described";
				}
				String savedPlace = (typeName) + "," + p.getCategory() + "," + (p.getPosition().getX()) + "," + 
						(p.getPosition().getY())+ ","  + p.getName();
				if(type.equals("DescribedPlace")){
					out.println(savedPlace + "," + ((DescribedPlace)p).getDescription());
				}else{

					out.println(savedPlace);
					changed = false;
				}
			}
			out.close();
			file.close();

		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(TopPanel.this, "Can not open the file");

		}catch(IOException e){
			JOptionPane.showMessageDialog(TopPanel.this, "Error: " + e.getMessage());
		}
	}

	public void loadPlaces(ActionEvent e){ 
		if(changed == true && exit() == false){
			return;
		}

		if(newMap == null){
			JOptionPane.showMessageDialog(parentFrame, "You have to add a map before load places");
			return;
		}
		else{

			JFileChooser loadPlaces = new JFileChooser("");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt", "UTF-8");
			loadPlaces.setFileFilter(filter);
			int svar = loadPlaces.showOpenDialog(this);

			if (svar != JFileChooser.APPROVE_OPTION)
				return;

			try{
				File file = loadPlaces.getSelectedFile();
				InputStreamReader inFile = new InputStreamReader(new FileInputStream(file));
				BufferedReader in = new BufferedReader(inFile);

				String line;
				while ((line = in.readLine()) != null){
					Place newPlace;
					String[] splitedLine = line.split(",", 6);
					String placeType = splitedLine[0];
					String category = splitedLine[1];
					int x = Integer.parseInt(splitedLine[2]);
					int y = Integer.parseInt(splitedLine[3]);
					Position pos = new Position(x, y);
					String name = splitedLine[4];

					if(places.containsKey(pos)){
						JOptionPane.showMessageDialog(parentFrame, "One or more of your places can not be added" + '\n' +  "because it is already an existing place on that position!");
						return;
					}
					if(category.equals("Train")){
						category = "train";
					}

					if(placeType.equals("Described")){
						String description = splitedLine[5];
						newPlace = new DescribedPlace(placeType, category, pos, name, description);
					}
					else{
						newPlace = new NamedPlace(placeType, category, pos, name);
					}

					if(placesByName.containsKey(name)){
						ArrayList<Place> existingPlaces = placesByName.get(name);
						existingPlaces.add(newPlace);
						placesByName.put(name, existingPlaces);
					}else{
						ArrayList<Place> newPlaceList = new ArrayList<Place>();
						newPlaceList.add(newPlace);
						placesByName.put(name, newPlaceList);
					}

					if(placesByCategory.containsKey(category)){
						ArrayList<Place> existingPlaces = placesByCategory.get(category);
						existingPlaces.add(newPlace);
						placesByCategory.put(category, existingPlaces);

					}else{
						ArrayList<Place> newCategoryList = new ArrayList<Place>();
						newCategoryList.add(newPlace);
						placesByCategory.put(category, newCategoryList);
					}

					places.put(pos, newPlace);
					newMap.add(newPlace);
					newPlace.addMouseListener(new Selected());
					updateMap();
					changed = true;
				}
				in.close();

			}catch(FileNotFoundException e1){
				System.err.println("Can not open");
			}catch(IOException e1){
				System.err.println("Wrong: ");
			}
		}
	}

	private void updateMap(){
		newMap.revalidate();
		newMap.repaint();
	}

	class WhatIsHere extends MouseAdapter{
		public void mouseClicked(MouseEvent e){

			int x = e.getX();
			int y = e.getY();

			for(int newX = -10; newX<11; newX++){
				for(int newY = -10; newY<11; newY++){
					Position p = new Position(x+newX, y+newY);
					if(places.containsKey(p)){
						places.get(p).setFolded(false);
						places.get(p).setVisible(true);
						
					}
				}
			}
			newMap.removeMouseListener(mouseWIH);
			parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			setEnabledMenus(true);
		}
	}

	class CreatePlace extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			
			String name = null;
			String category;
			if(!categoryList.isSelectionEmpty()){
				category = categoryList.getSelectedValue();
			}
			else{
				category = "None";
			}

			Place newPlace = null;
			int x = e.getX();
			int y = e.getY();

			Position position = new Position(x, y);

			if(places.containsKey(position)){
				JOptionPane.showMessageDialog(parentFrame, "There is already a place here");
				return;
			}

			String selected = (String) createNewPlace.getSelectedItem();
			try {
				if (selected.equals("Named")) {
					AddPlace addNamedPlace = new AddPlace();
					int answer = JOptionPane.showConfirmDialog(TopPanel.this, addNamedPlace, "Add named place",
							JOptionPane.OK_CANCEL_OPTION);
					if(answer == JOptionPane.CANCEL_OPTION ){
						parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						newMap.removeMouseListener(mouseCP);
						createNewPlace.setEnabled(true);
						setEnabledMenus(true);
						return;
					}
					name = addNamedPlace.getName();

					newPlace = new NamedPlace("Named", category, position, name);
					if (name == null || name.isEmpty()) {
						JOptionPane.showMessageDialog(parentFrame, "No name! Try again");
						updateMap();
						return;
					}
				}else if (selected.equals("Described")){
					AddDescribedPlace addDescribedPlace = new AddDescribedPlace();
					int answer = JOptionPane.showConfirmDialog(TopPanel.this, addDescribedPlace, "New described place",
							JOptionPane.OK_CANCEL_OPTION);
					if(answer == JOptionPane.CANCEL_OPTION ){
						createNewPlace.setEnabled(true);
						parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						newMap.removeMouseListener(mouseCP);
						setEnabledMenus(true);
						return;
					}
					name = addDescribedPlace.getName();
					String description = addDescribedPlace.getDescription();

					newPlace = new DescribedPlace("Described", category , position, name, description);
					if (name == null || name.isEmpty()){
						JOptionPane.showMessageDialog(parentFrame, "No name! Try agin");
						if(description != null && !description.isEmpty()){
							updateMap();
							return;
						}
					}
					if(description == null || description.isEmpty()) {
						JOptionPane.showMessageDialog(parentFrame, "No description! Try again");
						updateMap();
						return;
					}
				}
			}catch (NumberFormatException b) {
				JOptionPane.showMessageDialog(TopPanel.this, "Wrong value, try again");
			}

			places.put(position, newPlace);
			if(placesByName.containsKey(name)){
				ArrayList<Place> existingPlaces = placesByName.get(name);
				existingPlaces.add(newPlace);
				placesByName.put(name, existingPlaces);
			}else{
				ArrayList<Place> newPlaceList = new ArrayList<Place>();
				newPlaceList.add(newPlace);
				placesByName.put(name, newPlaceList);
			}
			if(placesByCategory.containsKey(category)){
				ArrayList<Place> existingPlaces = placesByCategory.get(category);
				existingPlaces.add(newPlace);
				placesByCategory.put(category, existingPlaces);
			}else{
				ArrayList<Place> newCategoryList = new ArrayList<Place>();
				newCategoryList.add(newPlace);
				placesByCategory.put(category, newCategoryList);
			}

			newPlace.addMouseListener(new Selected());
			newMap.add(newPlace);
			updateMap();
			changed = true;
			parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			newMap.removeMouseListener(mouseCP);
			setEnabledMenus(true);
		}
	}

	private void checkMarked(ArrayList<Place> categoryList){
		for(Place p : categoryList){
			p.setVisible(true);
			p.setSelected(false);
			if(markedPlaces.contains(p)){
				markedPlaces.remove(p);
			}
		}
	}

	class ChooseCategory implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e){
			if(categoryList.getSelectedValue() != null){
				if (e.getValueIsAdjusting()) {
					if((categoryList.getSelectedValue()).equals("Bus")){
						ArrayList<Place> busList = placesByCategory.get("Bus");
						if(busList != null){
							checkMarked(busList);
						}
					}
				}
				if((categoryList.getSelectedValue()).equals("Metro")){
					ArrayList<Place> metroList = placesByCategory.get("Metro");
					if(metroList != null){
						checkMarked(metroList);
					}
				}
				if((categoryList.getSelectedValue()).equals("Train")){
					ArrayList<Place> trainList = placesByCategory.get("Train");
					if(trainList != null){
						checkMarked(trainList);
					}
				}
			}
		}
	}

	class Selected extends MouseAdapter{
		public void mouseClicked(MouseEvent mev){
			Place p = (Place) mev.getSource();
			if(mev.getButton() == MouseEvent.BUTTON1){

				if(!p.getSelected()){
					markedPlaces.add(p);
					p.setSelected(true);
				}else{
					markedPlaces.remove(p);
					p.setSelected(false);
				}
			}
			repaint();
			updateMap();
		}
	}
}