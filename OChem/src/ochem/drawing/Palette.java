package ochem.drawing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import ochem.drawing.Canvas.ActionType;

/*
 * Palette
 * Created by: Neil Balaskandarajah
 * Last modified: 05/09/2019
 * A palette class that lets the user select what they want to add to the canvas
 */

public class Palette extends JPanel {
	//Attributes
	private int width;
	private int height;
	
	//Buttons
	private OBox main;
	private OBox side;
	private OBox func;
	private OBox clear;
	
	private final int NUM_BUTTONS = 4;
	
	//current type
	private ActionType selectedType;
	
	/*
	 * Creates a palette with a width and a height
	 * int width - width of the palette
	 * int height - height of the palette
	 */
	public Palette(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.setPreferredSize(new Dimension(width, height));
		
		selectedType = ActionType.CLEAR;
		
		layoutView();
		registerControllers();
	} //end constructor

	/*
	 * Add all the buttons to the palette
	 */
	private void layoutView() {
		//main chain button
		main = new OBox(width, height/NUM_BUTTONS, "Main");
		main.setTextColor(Color.BLACK);
		this.add(main);
		
		//side chain button
		side = new OBox(width, height/NUM_BUTTONS, "Side");
		side.setTextColor(Color.BLACK);
		this.add(side);
		
		//functional group button 
		func = new OBox(width, height/NUM_BUTTONS, "Function");
		func.setTextColor(Color.BLACK);
		func.setFontSize(90.0F);
		this.add(func);
		
		//clear button
		clear = new OBox(width, height/NUM_BUTTONS, "Clear");
		clear.setTextColor(Color.BLACK);
		clear.setFontSize(90.0F);
		this.add(clear);
	} //end layoutView

	/*
	 * Add controllers to each button
	 */
	private void registerControllers() {
		//main chain button controller
		PaletteButtonController mainC = new PaletteButtonController(this, main);
		main.addMouseListener(mainC);

		//side chain button controller
		PaletteButtonController sideC = new PaletteButtonController(this, side);
		side.addMouseListener(sideC);

		//functional group button controller
		PaletteButtonController funcC = new PaletteButtonController(this, func);
		func.addMouseListener(funcC);
		
		//clear button controller
		PaletteButtonController clearC = new PaletteButtonController(this, clear);
		clear.addMouseListener(clearC);
	} //end registerControllers
	
	/*
	 * Return the selected type of node
	 * return selectedType - currently selected node type
	 */
	public ActionType getSelectedType() {
		return selectedType;
	} //end getSelectedType
	
	/*
	 * Set the selected type of node
	 * NodeType type - type of node selected
	 */
	public void setSelectedType(ActionType type) {
		selectedType = type;
	} //end setNodeType	
	
	/*
	 * Updates the dialog box based on the selected type
	 */
	public void update() {
		switch (selectedType) {
			case CLEAR:
				DrawingGUI.showMessage("Select a feature to add");
				break;
			
			case MAIN:
				DrawingGUI.showMessage("Enter size of the main chain");
				break;
				
			case SIDE:
				DrawingGUI.showMessage("Click the location of the side chain");
				break;
				
			case FUNC_GROUP:
				DrawingGUI.showMessage("Click the location of the functional group");				
				break;
		}
	}
} //end class
