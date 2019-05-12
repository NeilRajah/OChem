package ochem.welcome;
/*
 *WelcomeButton Controller 
 * 
 */

//import packages
import java.awt.event.*;

import ochem.Model;

public class WelcomeController implements ActionListener
{
	private WelcomeGUI view;
	private Model model;
	
	public WelcomeController(WelcomeGUI view, Model model)
	{
		this.model=model;
		this.view=view;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().contentEquals("Naming"))
			model.setGUI(view);
	}
}