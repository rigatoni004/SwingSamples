// Import the GUI libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideScroller {
	/**
	 * MAIN METHOD
	 * This main method starts the GUI and runs the createMainWindow() method.
     * This method should not be changed.
	 */
	public static void main (String [] args) {
		javax.swing.SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				createMainWindow ();
			}
		});
	}


	/**
	 * STATIC VARIABLES AND CONSTANTS
	 * Declare the objects and variables that you want to access across
     * multiple methods.
	 */
	static final int MAX_X = 1280;
	static final int MAX_Y = 720;

	static final int NUM_BACKGROUNDS = 8;
	static JLabel [] leftBackgrounds;
	static JLabel [] rightBackgrounds;

	static JLabel penguin;
	static final int PENGUIN_X = 604;
	static final int PENGUIN_Y = 528;

	static final int NUM_WALK_PENGUINS = 4;
	static ImageIcon [] penguinWalkImages;
	static ImageIcon [] penguinWalkImagesFlipped;
	static final int PENGUIN_WALK_SPEED = 20;
	static int penguinWalkIndex;
	static boolean isWalking = false;
	static boolean leftDown = false;
	static boolean rightDown = false;
	static boolean directionRight = true;

	static final int NUM_JUMP_PENGUINS = 4;
	static ImageIcon [] penguinJumpImages;
	static ImageIcon [] penguinJumpImagesFlipped;

	static final int UPDATE_SPEED = 10;
	static final int MOVE_DISTANCE = 1;
	static int [] backgroundOffsets;
	static int [] backgroundSpeeds = {1, 4, 5, 10, 5, 10, 0, 0};
	static boolean [] backgroundIndependents = {false, false, false, false, true, true, false, false};
	static final int UPDATE_COUNT_MAX = 20;
	static int updateCount = 0;

	static boolean isJumping = false;
	static boolean isJumpingForward = false;
	static int jumpCount = 0;


	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	private static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame ("Frame Title");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setResizable (false);

		// The panel that will hold the components in the frame.
		JLayeredPane contentPane = new JLayeredPane ();
		contentPane.setPreferredSize(new Dimension(MAX_X, MAX_Y));

        // Add the backgrounds
		leftBackgrounds = new JLabel [NUM_BACKGROUNDS];
		rightBackgrounds = new JLabel [NUM_BACKGROUNDS];
		for (int index = 0; index < NUM_BACKGROUNDS; index++) {
			leftBackgrounds[index] = SwingHelpers.createScaledImage("resources/backgrounds/snow/layers/l" + (8 - index) + ".png", MAX_X, MAX_Y);
			rightBackgrounds[index] = SwingHelpers.createScaledImage("resources/backgrounds/snow/layers/l" + (8 - index) + ".png", MAX_X, MAX_Y);
			leftBackgrounds[index].setSize(MAX_X, MAX_Y);
			rightBackgrounds[index].setSize(MAX_X, MAX_Y);
			rightBackgrounds[index].setLocation(MAX_X, 0);
			contentPane.add(leftBackgrounds[index]);
			contentPane.add(rightBackgrounds[index]);
			contentPane.setLayer(leftBackgrounds[index], -100 - index);
			contentPane.setLayer(rightBackgrounds[index], -100 - index);
		}
		backgroundOffsets = new int [NUM_BACKGROUNDS];

		// Add the penguin
		penguinWalkImages = new ImageIcon [NUM_WALK_PENGUINS];
		penguinWalkImagesFlipped = new ImageIcon [NUM_WALK_PENGUINS];
		for (int index = 0; index < NUM_WALK_PENGUINS; index++) {
			penguinWalkImages[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 1) + ".png");
			penguinWalkImagesFlipped[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 1) + "_flip.png");
		}
		penguinJumpImages = new ImageIcon [NUM_JUMP_PENGUINS];
		penguinJumpImagesFlipped = new ImageIcon [NUM_JUMP_PENGUINS];
		for (int index = 0; index < NUM_JUMP_PENGUINS; index++) {
			penguinJumpImages[index] = new ImageIcon("resources/penguins/penguin_jump0" + (index + 1) + ".png");
			penguinJumpImagesFlipped[index] = new ImageIcon("resources/penguins/penguin_jump0" + (index + 1) + "_flip.png");
		}
		penguin = new JLabel(penguinWalkImages[0]);
		penguin.setSize(72, 64);
		penguin.setLocation(PENGUIN_X, PENGUIN_Y);
		contentPane.add(penguin);

		// Start the update timer
		Timer updateTimer = new Timer(UPDATE_SPEED, new UpdateTimerHandler());
		updateTimer.start();

		// Add the key listener
		frame.addKeyListener(new KeyHandler());

		// Add the panel to the frame
		frame.setContentPane(contentPane);

		//size the window.
		frame.pack();
		frame.setLocationRelativeTo(null);
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

	/**  */
	private static class UpdateTimerHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			updateCount++;
			if (updateCount == UPDATE_COUNT_MAX) {
				updateCount = 0;
			}
			for (int index = 0; index < NUM_BACKGROUNDS; index++) {
				if ((isWalking || backgroundIndependents[index]) && backgroundSpeeds[index] != 0 &&
						updateCount % backgroundSpeeds[index] == 0) {
					if (directionRight) {
						backgroundOffsets[index]++;
					}
					else {
						backgroundOffsets[index]--;
					}
					if (backgroundOffsets[index] >= MAX_X) {
						backgroundOffsets[index] -= MAX_X;
					}
					else if (backgroundOffsets[index] < 0) {
						backgroundOffsets[index] += MAX_X;
					}
					leftBackgrounds[index].setLocation(-backgroundOffsets[index], 0);
					rightBackgrounds[index].setLocation(MAX_X - backgroundOffsets[index], 0);
				}
			}
			if (updateCount % PENGUIN_WALK_SPEED == 0) {
				if (isWalking) {
					penguinWalkIndex++;
					if (penguinWalkIndex == NUM_WALK_PENGUINS) {
						penguinWalkIndex = 0;
					}
				}
				else {
					penguinWalkIndex = 0;
				}
				if (directionRight) {
					penguin.setIcon(penguinWalkImages[penguinWalkIndex]);
				}
				else {
					penguin.setIcon(penguinWalkImagesFlipped[penguinWalkIndex]);
				}
			}

			Toolkit.getDefaultToolkit().sync();
		}
	}


	/**  */
    private static class KeyHandler implements KeyListener {
        public void keyTyped (KeyEvent event) {
        }

        public void keyPressed (KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_UP) {
				isJumping = true;
				isJumpingForward = isWalking;
			}
			else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				leftDown = true;
				directionRight = false;
				penguin.setIcon(penguinWalkImagesFlipped[penguinWalkIndex]);
			}
			else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightDown = true;
				directionRight = true;
				penguin.setIcon(penguinWalkImages[penguinWalkIndex]);
			}
			isWalking = leftDown || rightDown;
        }

        public void keyReleased (KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				leftDown = false;
				if (rightDown) {
					directionRight = true;
					penguin.setIcon(penguinWalkImages[penguinWalkIndex]);
				}
			}
			else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightDown = false;
				if (leftDown) {
					directionRight = false;
					penguin.setIcon(penguinWalkImagesFlipped[penguinWalkIndex]);
				}
			}
			isWalking = leftDown || rightDown;
        }
    }
}
