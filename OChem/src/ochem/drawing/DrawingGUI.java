package ochem.drawing;

/*
 * DrawingGUI
 * Created by: Neil Balaskandarajah
 * Last modified: 05/09/2019
 * The container for all the components responsible for drawing
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ochem.View;

public class DrawingGUI extends JPanel {
	//Attributes
	private int width; //width of panel
	private int height; //height of panel
	private Canvas canvas; //object responsible for drawing
	private Palette palette; //object holding all the feature buttons
	
	private static OBox dialog; //dialog box for relaying information to user
	private static JTextField userInput; //text field to get user input
	
	private JPanel dialogPanel; //panel for holding the bottom row of items
	
	private final double CANVAS_SCALE = 0.8; //how big the canvas is in terms of the whole container
	private final double ERROR_SCALE = 0.1; //how big the bottom is in terms of the whole container
	private final Color BG_COLOR = Color.WHITE; //background color for the panel
	
	/*
	 * Create a new DrawingGUI with a width and height
	 * int width - width of container
	 * int height - height of container
	 */
	public DrawingGUI(int width, int height) {	
		//initialize attributes
		this.width = width;
		this.height = height;
		
		//set the size
		this.setPreferredSize(new Dimension(width, height));
		
		//create and add all components
		layoutView();
	} //end constructor
	
	/*
	 * Create and add all the components to the container
	 */
	private void layoutView() {
		//border layout for this container
		this.setLayout(new BorderLayout());
		
		//create the components
		//palette
		palette = new Palette((int) ((1.0 - CANVAS_SCALE) * width), (int) ((1.0 - ERROR_SCALE) * height));
		
		//canvas
		canvas = new Canvas((int) (width * CANVAS_SCALE), (int) ((1.0 - ERROR_SCALE) * height), palette);
		palette.setCanvas(canvas);
		
		//bottom row
		createDialogBox();
		
		//add the components to the container
		this.add(palette, BorderLayout.WEST);
		this.add(canvas, BorderLayout.CENTER);
		this.add(dialogPanel, BorderLayout.SOUTH);		
		
		//add padding to the panel and set a background color
		this.setBorder(new EmptyBorder(View.PAD, View.PAD, View.PAD, View.PAD));
		this.setBackground(BG_COLOR);
	} //end layoutView
	
	/*
	 * Create the dialog box
	 */
	private void createDialogBox() {
		//create the panel
		dialogPanel = new JPanel();
		double DIALOG_SCALE = 1.0 - ERROR_SCALE;
		
		//create and configure the dialog box
		dialog = new OBox((int) (DIALOG_SCALE * width), (int) (height * ERROR_SCALE), "Start with a main chain");
		dialog.setBackgroundColor(Color.BLACK);
		dialog.setTextColor(Color.GREEN);
		dialog.setCornerRadius(20);
		dialog.setFontSize(80.0F);
		dialogPanel.add(dialog);
		
		//create and configure the text field
		userInput = new JTextField("", SwingConstants.CENTER);
		userInput.setPreferredSize(new Dimension((int) ((1 - DIALOG_SCALE) * width), (int) (height * ERROR_SCALE))); 
		userInput.setFont(userInput.getFont().deriveFont(80.0F));
		
		//add a controller to the text field
		UserInputController uic = new UserInputController(canvas, palette);
		userInput.addKeyListener(uic);
		
		//add the components to the dialog panel
		dialogPanel.add(userInput);
		dialogPanel.setBackground(BG_COLOR);
	} //end createDialogBox
	
	/*
	 * Report an error to the dialog box
	 * String message - error message to display
	 */
	public static void reportError(String message) {
		dialog.setTextColor(Color.RED);
		dialog.setText(message);
		dialog.update();
	} //end reportError
	
	/*
	 * Show a message in the dialog box
	 * String message - message to show
	 */
	public static void showMessage(String message) {
		dialog.setTextColor(Color.GREEN);
		dialog.setText(message);
		dialog.update();
	} //end showMessage
	
	/*
	 * Get the user input from the textbox
	 * return - text from the text field
	 */
	public static String getUserInput() {
		return userInput.getText();
	} //end getUserInput
	
	/*
	 * Clear the dialog box and the text field
	 */
	public static void clear() {
		dialog.setTextColor(Color.GREEN);
		dialog.setText("");
		dialog.update();
		
		userInput.setText("");
	} //end clear
} //end class
