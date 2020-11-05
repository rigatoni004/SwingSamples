import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe {
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

	// Holds the buttons
	// The indices of the buttons is as follows:
	//  0  1  2
	//  3  4  5
	//  6  7  8
	static JButton [] buttons = new JButton [9];

	// Tracks which player has chosen each square
	// 0 = not chosen, 1 = X, 2 = O
	static int [] squares = new int [9];

	// Tracks how many turns have been taken.
	static int turns = 0;


	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	private static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame("TIC-TAC-TOE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);

		// Create a 3x3 grid
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3, 5, 5));
		panel.setBackground(Color.BLACK);

		// Create the buttons
		for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
			buttons[buttonIndex] = new JButton();
			buttons[buttonIndex].setBackground(Color.WHITE);
			buttons[buttonIndex].setBorder(null);
			buttons[buttonIndex].setFont(new Font("Verdana", Font.BOLD, 36));
			buttons[buttonIndex].addActionListener(new ButtonHandler(buttonIndex));
			panel.add(buttons[buttonIndex]);
		}
		// Make disable button color be white
		UIManager.put("Button.disabledText", Color.WHITE);

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
		private int buttonIndex;

		/**
		 * Creates a ButtonHandler for the button as the given index.
		 * @param buttonIndex Index of the button.
		 */
		public ButtonHandler (int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}

		/**
		 * Method called when the button is pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			turns++;
			buttons[buttonIndex].setEnabled(false);
			if (turns % 2 == 1) {
				buttons[buttonIndex].setText("X");
				squares[buttonIndex] = 1;
				buttons[buttonIndex].setBackground(Color.RED);
			}
			else {
				buttons[buttonIndex].setText("O");
				squares[buttonIndex] = 2;
				buttons[buttonIndex].setBackground(Color.BLUE);
			}
			checkForWin();
		}
	}

	/**
	 * Checks to see if the game is over.  If it is, inform the user and ask if they want to play again.
	 */
	public static void checkForWin () {
		if (squares[0] == 1 && squares[1] == 1 && squares[2] == 1 ||
				squares[3] == 1 && squares[4] == 1 && squares[5] == 1 ||
				squares[6] == 1 && squares[7] == 1 && squares[8] == 1 ||
				squares[0] == 1 && squares[3] == 1 && squares[6] == 1 ||
				squares[1] == 1 && squares[4] == 1 && squares[7] == 1 ||
				squares[2] == 1 && squares[5] == 1 && squares[8] == 1 ||
				squares[0] == 1 && squares[4] == 1 && squares[8] == 1 ||
				squares[6] == 1 && squares[4] == 1 && squares[2] == 1) {
			JOptionPane.showMessageDialog(null, "Player 1 (X) Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
		}
		else if (squares[0] == 2 && squares[1] == 2 && squares[2] == 2 ||
				squares[3] == 2 && squares[4] == 2 && squares[5] == 2 ||
				squares[6] == 2 && squares[7] == 2 && squares[8] == 2 ||
				squares[0] == 2 && squares[3] == 2 && squares[6] == 2 ||
				squares[1] == 2 && squares[4] == 2 && squares[7] == 2 ||
				squares[2] == 2 && squares[5] == 2 && squares[8] == 2 ||
				squares[0] == 2 && squares[4] == 2 && squares[8] == 2 ||
				squares[6] == 2 && squares[4] == 2 && squares[2] == 2) {
			JOptionPane.showMessageDialog(null, "Player 2 (O) Wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
		}
		else if (squares[0] != 0 && squares[1] != 0 && squares[2] != 0 &&
				squares[3] != 0 && squares[4] != 0 && squares[5] != 0 &&
				squares[6] != 0 && squares[7] != 0 && squares[8] != 0) {
			JOptionPane.showMessageDialog(null, "Draw!", "Game Over", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			// The game is not done.  Continue the game.
			return;
		}

		// The game is over.  Ask the user if they want to play again.
		int answer = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "Play Again?",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			reset();
		}
		else if (answer == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Reset the game board to start a new game.
	 */
	public static void reset () {
		for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
			buttons[buttonIndex].setEnabled(true);
			buttons[buttonIndex].setText("");
			buttons[buttonIndex].setBackground(Color.WHITE);
			squares[buttonIndex] = 0;
			turns = 0;
		}
	}
}
