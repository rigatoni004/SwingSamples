import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FollowMouse {
	/**
	 * MAIN METHOD
	 * This main method starts the GUI and runs the createMainWindow() method.
     * This method should not be changed.
	 */
	public static void main (String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
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
	
	// Will hold a picture of a car
	static JLabel car = SwingHelpers.createScaledImage("resources/car.png", 40, 20);

	
	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	public static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame("Mouse Location");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);

		// Create a panel to hold the components
		JPanel panel = new JPanel();
		panel.setLayout(null);

		// Add the car
		panel.add(car);
		car.setBounds(280, 290, 40, 20);

		// Add listener for mouse movement
		MouseMoveHandler handler = new MouseMoveHandler();
		panel.addMouseMotionListener(handler);

		// Add the panel to the frame
		frame.setContentPane(panel);

		// Show the frame
		frame.setVisible(true);
	}
	
	
	/**
     * HELPER METHODS
     * Methods that you create to manage repetitive tasks.
     */



    /**
     * EVENT LISTENERS
     * Subclasses that handle events (button clicks, mouse clicks and moves,
     * key presses, timer expirations)
     */

	/** Implements the listener for mouse movement */
	public static class MouseMoveHandler implements MouseMotionListener {
		/** Called whenever the mouse is moved without clicking buttons. */
		public void mouseMoved(MouseEvent e) {
			// NOTE: If you comment out this line, the car only moved when clicking.
			moveCar(e);
		}

		/** Called whenever the mouse is moved with clicking buttons. */
		public void mouseDragged(MouseEvent e) {
			// NOTE: If you comment out this line, the car only moved when not clicking.
			moveCar(e);
		}

		/** Moves the car to the current mouse position. */
		public void moveCar (MouseEvent e)
		{
			car.setLocation(e.getX() - 20, e.getY() - 10);
		}
	}
}
