//Maja Lund, 920723-0765

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIStart extends JFrame{

	private TopPanel topPanel = new TopPanel(this);

	public GUIStart(){
		add(topPanel, BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new Exit());
		pack();
		setVisible(true);
	}

	class Exit extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent wev){
			if(topPanel.getChanged()){
				int svar = JOptionPane.showConfirmDialog(GUIStart.this, "Unsaved changes, do you want to exit anyway?", 
						"Exit",	JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE); 
				if(svar == JOptionPane.CANCEL_OPTION){
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}else{
					System.exit(0);
				}
			}
		}
	}

	public static void main(String[] args){
		JFrame GUIStart = new GUIStart();
	}
}