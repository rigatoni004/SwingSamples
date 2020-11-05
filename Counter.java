import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Counter {
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

	static JTextField valueField;

	
	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	public static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame("Counter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 100);
		frame.setLocationRelativeTo(null);

		// Create a panel to hold the components
		JPanel panel = new JPanel();

		// Create the label
		JLabel label = new JLabel("Counter:");
		label.setFont(new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, 16));
		panel.add(label);

		// Create a textfield with 10 columns
		valueField = new JTextField("0", 10);
		panel.add(valueField);

		// Create an increment button
		JButton incrementButton = new JButton("Increment");
		ButtonHandler handler = new ButtonHandler();
		incrementButton.addActionListener(handler);
		panel.add(incrementButton);

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

	/**
	 * Implements the listener for the buttons on the board.
	 */
	private static class ButtonHandler implements ActionListener {
		/**
		 * Method called when the button is pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			int value = Integer.parseInt(valueField.getText());
			valueField.setText(String.valueOf(value + 1));
		}
	}
}
