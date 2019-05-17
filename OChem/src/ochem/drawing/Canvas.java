package ochem.drawing;

/*
 * Canvas
 * Created by: Neil Balaskandarajah
 * Last modified: 05/13/2019
 * Components that handles drawing components to the screen
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import ochem.View;
import ochem.organic.Chain;
import ochem.organic.Compound;

public class Canvas extends JComponent {
	//Attributes
	private int width; //width of component
	private int height; //height of component
	private Palette palette; //instance of the palette
	
	private Node mouse; //for hovering effects
	
	public static Color BACKGROUND_COLOR; //background color
	
	private Compound master; //compound being drawn
	private Chain mainChain; //main chain
	private ArrayList<Node> mainNodes; //nodes for the main chain
	
	private ArrayList<Chain> sideChains; //side chains
	private ArrayList<Node> sideNodes; //nodes for the side chain
	
	private ActionType type; //type of action
	
	private boolean mainOnScreen; //whether a main chain is on the screen
	
	private int mainStep; //step for the "main" button
	
	/*
	 * Types of action to determine different drawing features
	 */
	public static enum ActionType {
		CLEAR,
		MAIN,
		SIDE,
		FUNC_GROUP
	} //end enum
	
	/*
	 * Different directions the chain can be drawn in
	 */
	private enum DrawDirection {
		UP_RIGHT,
		RIGHT,
		DOWN_RIGHT,
		DOWN_LEFT,
		LEFT,
		UP_LEFT
	} //end enum
	
	/*
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 * Palette palette - instance of the palette
	 */
	public Canvas(int width, int height, Palette palette) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.palette = palette;
		
		//set the size of the component
		this.setPreferredSize(new Dimension(this.width, this.height));
		
		//instantiate the nodes list, create the mouse node
		mouse = new Node(20);
		
		//pale blue
		BACKGROUND_COLOR = new Color(224, 255, 253);
		
		//set the type
		type = ActionType.CLEAR;
		
		//set the main on screen to false
		mainOnScreen = false;
		
		//instantiate the chains
		mainChain = new Chain(0, "-1");		
		sideChains = new ArrayList<Chain>();
		
		//instantiate lists for the nodes on the main chain and side chains
		mainNodes = new ArrayList<Node>();
		sideNodes = new ArrayList<Node>();
		
		//set the step numbers to zero when not being used, increment when needed
		mainStep = 0;
		
		//add the controllers to the canvas
		registerControllers();
	} //end constructor
	
	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		//cast the more capable Graphics2D onto g
		Graphics2D g2 = (Graphics2D) g;
		
		//background
		g2.setBackground(BACKGROUND_COLOR);
		g2.clearRect(View.PAD/2,View.PAD, width - 2*View.PAD, height - 2*View.PAD);
		
		//update the type to the palette's type
		this.type = palette.getSelectedType();
		
		//handle the actions for each type
		clearAction(g2);
		mainAction(g2);
	}  //end paintComponent
	
	/*
	 * Handles the actions for the clear flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void clearAction(Graphics2D g2) {
		//only draw the node if the type is CLEAR
		if (type == ActionType.CLEAR) {
			g2.setColor(new Color(50,238,50, 100)); //transparent green
			drawNode(g2, mouse);
			
			//set the main step to zero and for there to be no main chain on screen
			mainOnScreen = false;
			mainStep = 0;
		} //if
	} //end clearAction
	
	
	/*
	 * Handles the actions for the main flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void mainAction(Graphics2D g2) {
		//change what to draw based on what the value of main step is
		switch (mainStep) {
			//do nothing step
			case 0: 
			break;
				
			//size definition step
			case 1: 
				mainOnScreen = false;
				DrawingGUI.showMessage("Enter size of main chain: (ENTER)");
				g2.setColor(new Color(50,50,238, 100));
				drawNode(g2, mouse);
			break;
			
			//location selection step
			case 2: 
				mainOnScreen = false;
				DrawingGUI.showMessage("Select location for main chain: (CLICK)");
				g2.setColor(new Color(200,200,200, 100));
				drawChain(g2, mouse, DrawDirection.RIGHT, mainChain.getSize());
			break;
			
			//fixed on screen step
			case 3: 
				mainOnScreen = true;
				DrawingGUI.clear();
				g2.setColor(Color.BLACK);
				drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, mainChain.getSize());
			break;
		} //switch
	} //end mainAction
	
	/*
	 * Draw a node to the screen
	 * Graphics2D g2 - object responsible for drawing
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.fillOval(n.getCenterX(), n.getCenterY(), n.getDia(), n.getDia());
	} //end drawNode
	
	/*
	 * Draws the main chain from a starting point
	 * Node start - starting point
	 * DrawDirection dir - direction to draw in
	 * int chainSize - size of chain to draw
	 */
	private void drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize) {
		//stroke object for drawing
		BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);
		
		//starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();
		
		double[] angles = angleFromDirection(dir); //get the angles based on the direction
		int arm = 150; //length of bonds in pixels
		
		for (int i = 0; i < chainSize-1; i++) {
			//current angle to draw (alternate between the 2 angles)
			double ang = angles[i % 2]; 
			
			//calculate the end points for the line
			int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
			int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;
			
			//draw the line
			g2.drawLine(x1, y1, x2, y2);
			
			//change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
		} //loop
	} //end drawChain
	
	/*
	 * Choose the pair of angles from the direction chosen
	 * DrawDirection dir - direction to draw in
	 */
	private double[] angleFromDirection(DrawDirection dir) {
		double[] angles; //array of angles to draw with
		
		//different directions require different pairs of numbers to switch between
		switch (dir) {
			case DOWN_LEFT: 
				angles = new double[] {90,120};
				break;
				
			case DOWN_RIGHT:
				angles = new double[] {90,60};
				break;
				
			case LEFT: 
				angles = new double[] {-150,150};
				break;
				
			case RIGHT:
				angles = new double[] {-30,30};
				break;
				
			case UP_LEFT:
				angles = new double[] {270,240};
				break;
				
			case UP_RIGHT:
				angles = new double[] {270,300};
				break;
				
			default:
				throw new IllegalArgumentException("What direction is this???");
		} //switch
		
		return angles;
	} //end angleFromDirection
	
	private void sideAction(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		for (Node n : sideNodes) {
			int startX = n.getCenterX();
			int startY = n.getCenterY();
			
			double angle = Math.toRadians(90);
			int arm = 150;
			
			for (int i = 1; i < mainChain.getSize(); i++) {
				//change angle based on even/odd
				if (i % 2 == 0) { 
					angle = 90;
				} else {
					angle = 60;
				}
				
				//calculate the arm offset
				int endX = (int) (arm * Math.cos(angle)) + startX;
				int endY = (int) (arm * Math.sin(angle)) + startY;
				
				//draw the line
				g2.drawLine(startX, startY, endX, endY);
				
				startX = endX;
				startY = endY;
			}
		}
	}
	
	/*
	 * Set the size of the main chain
	 * int main - size of the main chain
	 */
	public void setMainSize(int main) {
		mainChain = new Chain(main, "-1");
	} //end setMainSize
	
	/*
	 * Update the screen
	 */
	public void update() {
		repaint();
	} //end update
	
	/*
	 * Add the CanvasController to this component
	 */
	private void registerControllers() {
		CanvasController cc = new CanvasController(this);
		this.addMouseListener(cc);
		this.addMouseMotionListener(cc);
	} //end registerControllers
	
	/*
	 * Set the x and y of the mouse
	 * int x - mouse x
	 * int y - mouse y
	 */
	public void setMouseXY(int x, int y) {
		mouse.setXY(x, y);
		
		//if there is no main chain on the screen, set the potential position to the mouse point
		if (!mainOnScreen) {
			mainNodes.clear();
			mainNodes.add(new Node(mouse.getX(), mouse.getY(), 10));
		} //if 
	} //end setMouseXY
	
	/*
	 * Tell the canvas that there is a main chain on the screen already
	 * boolean val - whether the main chain is on the screen
	 */
	public void setMainOnScreen(boolean val) {
		mainOnScreen = val;
	} //end setMainOnScreen
	
	/*
	 * Get whether there is a main chain on the screen
	 * return mainOnScreen - whether the main chain is on the screen
	 */
	public boolean getMainOnScreen() {
		return mainOnScreen;
	} //end getMainOnScreen
	
	/*
	 * Get the current type for the canvas
	 * return type - action type of the canvas
	 */
	public ActionType getType() {
		return type;
	} //end getType
	
	/*
	 * Get the nodes on the main chain
	 * return nodes - list of all the nodes of the main chain
	 */
	public ArrayList<Node> getMainNodes() {
		return mainNodes;
	} //end getMainNodes
	
	/*
	 * Add side node to the side nodes list
	 * Node n - node to add to the side Nodes
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
	} //end addSideNode
	
	/*
	 * Get the side chains
	 * return sideChains - list of all the side chains
	 */
	public ArrayList<Chain> getSideChains() {
		return sideChains;
	} //end getSideChains
	
	/*
	 * Update the type of the canvas from the palette type
	 */
	public void updateActionType() {
		type = palette.getSelectedType();
		
		//if the type was set to clear, wipe the main chain
		if (type == ActionType.CLEAR) {
			mainOnScreen = false;
		} //if
	} //end updateActionType
	
	/*
	 * Set the step for main drawing
	 * int step - step for main drawing
	 */
	public void setMainStep(int step) {
		//if outside of the range
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too big for me :(");
		} else {
			mainStep = step;
		}  //if
		
		this.update();
	} //end setMainStep
} //end class
