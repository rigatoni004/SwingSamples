import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DriveAround {
	/**
	 * MAIN METHOD
	 * This main method starts the GUI and runs the createMainWindow() method.
     * This method should not be changed.
	 */
	public static void main (String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createMainWindow();
			}
		});
	}
	
	
	/**
	 * STATIC VARIABLES AND CONSTANTS
	 * Declare the objects and variables that you want to access across
     * multiple methods.
	 */

	// Holds the panel and its size
	static JPanel panel; 
	static int panelWidth = 800;
	static int panelHeight = 600;

	// Will hold pictures of a car and a truck
	static JLabel car = SwingHelpers.createScaledImage("resources/car.png", 60, 30);
	static JLabel truck = SwingHelpers.createScaledImage("resources/truck.png", 60, 40);

	// Stores the position of the car and truck at their center
	static int xCar = 600;
	static int yCar = 300;
	static int xTruck = 200;
	static int yTruck = 300;

	// Offsets for the position of the car and truck
	final static int X_CAR_OFFSET = 30;
	final static int Y_CAR_OFFSET = 15;
	final static int X_TRUCK_OFFSET = 30;
	final static int Y_TRUCK_OFFSET = 20;
	
	// How far to move the vehicles for each timer event
	final static int SPEED = 100;
	final static int MOVE_DISTANCE = 5;
	static int deltaXCar = 0;
	static int deltaYCar = 0;
	static int deltaXTruck = 0;
	static int deltaYTruck = 0;

	
	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	public static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame("Mouse Location");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.addComponentListener(new ResizeHandler());

		// Create a panel to hold the components
		panel = new JPanel();
		panel.setLayout(null);

		// Add the car and truck
		panel.add(car);
		car.setBounds(xCar - X_CAR_OFFSET, yCar - Y_CAR_OFFSET, 60, 30);
		panel.add(truck);
		truck.setBounds(xTruck - X_TRUCK_OFFSET, yTruck - Y_TRUCK_OFFSET, 60, 40);
		
		// Create the timer that moves the vehicles
		Timer movementTimer = new Timer(SPEED, new MovementTimerListener());
		movementTimer.setRepeats(true);
		movementTimer.start();

		// Add listener for mouse movement
		KeyTypedHandler handler = new KeyTypedHandler();
		frame.addKeyListener(handler);

		// Add the panel to the frame
		frame.setContentPane(panel);

		// Show the frame
		frame.setVisible(true);
		frame.requestFocus();
	}
	
	
	/**
     * HELPER METHODS
     * Methods that you create to manage repetitive tasks.
     */

	/**
	 * Moves the car by the given x- and y- values.
	 * @param deltaX Distance to move the car right.
	 * @param deltaY Distance to move the car down.
	 */
	public static void moveCar () {
		xCar = Math.max(Math.min(xCar + deltaXCar, panelWidth - X_CAR_OFFSET), X_CAR_OFFSET);
		yCar = Math.max(Math.min(yCar + deltaYCar, panelHeight - Y_CAR_OFFSET), Y_CAR_OFFSET);
		car.setLocation(xCar - X_CAR_OFFSET, yCar - Y_CAR_OFFSET);
	}

	/**
	 * Moves the truck buy the given x- and y-values.
	 * @param deltaX Distance to move the car right.
	 * @param deltaY Distance to move the car down.
	 */
	public static void moveTruck () {
		xTruck = Math.max(Math.min(xTruck + deltaXTruck, panelWidth - X_TRUCK_OFFSET), X_TRUCK_OFFSET);
		yTruck = Math.max(Math.min(yTruck + deltaYTruck, panelHeight - Y_TRUCK_OFFSET), Y_TRUCK_OFFSET);
		truck.setLocation(xTruck - X_TRUCK_OFFSET, yTruck - Y_TRUCK_OFFSET);
	}
	
	
	/**
     * EVENT LISTENERS
     * Subclasses that handle events (button clicks, mouse clicks and moves,
     * key presses, timer expirations)
     */

	/**
	 * Handles mouse movement.
	 */
	public static class KeyTypedHandler implements KeyListener {
		/** This method does nothing. */
		public void keyTyped (KeyEvent e) {}

		/** When a key is pressed, change the deltas. */
		public void keyPressed (KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				deltaXCar = -MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				deltaXCar = MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				deltaYCar = -MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				deltaYCar = MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_A) {
				deltaXTruck = -MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_D) {
				deltaXTruck = MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_W) {
				deltaYTruck = -MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_S) {
				deltaYTruck = MOVE_DISTANCE;
			}
		}

		/** When a key is released, unchange the deltas. */
		public void keyReleased (KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				deltaXCar = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				deltaXCar = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				deltaYCar = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				deltaYCar = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_A) {
				deltaXTruck = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_D) {
				deltaXTruck = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_W) {
				deltaYTruck = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_S) {
				deltaYTruck = 0;
			}
		}
	}
	
	/**
	 * Handles when the frame is resized.
	 */
	public static class ResizeHandler implements ComponentListener {
		/**
		 * When the frame is resized, the panel's new dimensions are stored.  If the car
		 * or truck is off the screen, then their positions are changed to still be on
		 * the screen.
		 */
		public void componentResized (ComponentEvent e) {
			// Get the new size of the panel
			panelWidth = panel.getWidth();
			panelHeight = panel.getHeight();
			
			// Keep the car and truck on the screen
			xCar = Math.min(xCar, panelWidth - X_CAR_OFFSET);
			yCar = Math.min(yCar, panelHeight - Y_CAR_OFFSET);
			car.setLocation(xCar - X_CAR_OFFSET, yCar - Y_CAR_OFFSET);
			xTruck = Math.min(xTruck, panelWidth - X_TRUCK_OFFSET);
			yTruck = Math.min(yTruck, panelHeight - Y_TRUCK_OFFSET);
			truck.setLocation(xTruck - X_TRUCK_OFFSET, yTruck - Y_TRUCK_OFFSET);
		}

		/** This method does nothing. */
		public void componentMoved (ComponentEvent e) {}

		/** This method does nothing. */
		public void componentShown (ComponentEvent e) {}

		/** This method does nothing. */
		public void componentHidden (ComponentEvent e) {}
		
	}
	
	/** Handles the car and truck movement when keys are down. */
	private static class MovementTimerListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			moveCar();
			moveTruck();
		}
	}
}
