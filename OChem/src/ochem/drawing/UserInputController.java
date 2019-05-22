	package ochem.drawing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInputController implements KeyListener {
	//Attributes
	private Canvas canvas;
	private Palette palette;
	
	//Constants
	private final int ENTER_KEY = 10;
	
	public UserInputController(Canvas canvas, Palette palette) {
		this.canvas = canvas;
		this.palette = palette;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent k) {
		if ((int) k.getKeyChar() == ENTER_KEY ) {
			if (isNumber(DrawingGUI.getUserInput())) {
				int num = Integer.parseInt(DrawingGUI.getUserInput());
				
				switch (palette.getSelectedType()) {
					//main button
					case MAIN:
						//check to see if number entered is within range
						if (num < 2) {
							DrawingGUI.reportError("Size entered too small!");
						} else if (num > 10) {
							DrawingGUI.reportError("Size entered too big!");
						} else {
							//set the size and step forward
							canvas.setMainSize(num);
							canvas.setMainStep(2);
							DrawingGUI.clear();
						} //if
						
						break;
						
					//side button
					case SIDE:
						//add a size and step forward
						canvas.addSideSize(num);
						canvas.setSideStep(2);				
						
						break;
				} //switch
			
			//if letter was typed
			} else {
				String in = DrawingGUI.getUserInput();
				
				if (in.equalsIgnoreCase("Y")) {
					canvas.setMainCyclo(true);
				} else if (in.equalsIgnoreCase("N")) {
					canvas.setMainCyclo(false);
				} //if
				
				canvas.setMainStep(3);
				
			} //if
		} //outer if
	} //end keyTyped

	/*
	 * Checks if a String is a valid number
	 * String text - String to check for number
	 */
	private boolean isNumber(String text) {
		//if parse succeeds, string is a number and true is returned
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException n) {
			return false;
		} //try-catch
	} //end isNumber
}
