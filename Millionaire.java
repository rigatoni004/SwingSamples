import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Millionaire {
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

	static final String VERSION = "0.2";

	// Used for generating random numbers
	static Random random = new Random();

	// Components that are needed in multiple methods
	static JFrame frame;
	static JButton [] answerButtons = new JButton [4];
	static JLabel [] prizeLabels = new JLabel [15];
	static JLabel questionLabel;
	static JLayeredPane centerPanel;
	static JLabel background = new JLabel(new ImageIcon("resources/chairs.jpg"));
	static JLabel confetti = new JLabel(new ImageIcon("resources/confetti.png"));
	static JLabel nameBox = new JLabel();
	static JLabel amount = new JLabel();
	static JPanel finalAnswerBox = new JPanel();
	static JLabel finalAnswerBoxQuestion = new JLabel();
	static JButton finalAnswerBoxConfirm = new JButton("YES");
	static JButton finalAnswerBoxDeny = new JButton("NO");
	static DefaultListModel<String> leaderboard;
	static JFrame leaderboardWindow;
	static ImageIcon logo = new ImageIcon(new ImageIcon("resources/millionaire_icon.png").getImage()
			.getScaledInstance(48, 48, Image.SCALE_SMOOTH));

	// The prize amount for each level
	static final int [] PRIZES = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000,
		64000, 125000, 250000, 500000, 1000000};

	// Constants for the colors used.
	static final Color BACKGROUND_COLOR = new Color(44, 64, 115);
	static final Color BORDER_COLOR = new Color(200, 195, 174);
	static final Color BORDER_COLOR_HIGHLIGHT = new Color(214, 209, 186);
	static final Color BORDER_COLOR_SHADOW = new Color(128, 123, 101);
	static final Color PRIZE_COLOR = new Color(217, 141, 38);
	static final Color BOX_COLOR = new Color(45, 44, 173);
	static final Color CORRECT_COLOR = new Color(59, 199, 36);
	static final Color CHOICE_COLOR = new Color(219, 131, 1);

	static Border raisedBorder = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(
			BevelBorder.RAISED, BORDER_COLOR_HIGHLIGHT, BORDER_COLOR_SHADOW),
			BorderFactory.createLineBorder(BORDER_COLOR, 3));
	static Border raisedBorderThick = BorderFactory.createCompoundBorder(
			BorderFactory.createBevelBorder(BevelBorder.RAISED, BORDER_COLOR_HIGHLIGHT,
					BORDER_COLOR_SHADOW), BorderFactory.createLineBorder(BORDER_COLOR, 6));

	// File resource locations
	static final String QUESTION_DATABASE = "resources/millionaire.txt";
	static final String LEADERBOARD_FILE = "resources/leaderboard.txt";
	static final String INSTRUCTIONS_FILE = "resources/millionaire_instructions.txt";
	static final String LICENSE_FILE = "LICENSE.txt";

	// Web links
	static final String WIKIPEDIA_PAGE = "https://en.wikipedia.org/wiki/Who_Wants_to_Be_a_Millionaire%3F";

	// Constants for the audio files used
	static final String MUSIC_MAIN_BACKGROUND = "resources/millionaire.mp3";
	static final String MUSIC_CORRECT_ANSWER = "resources/correctanswer.mp3";
	static final String MUSIC_WRONG_ANSWER = "resources/wronganswer.mp3";
	static final String MUSIC_FINAL_ANSWER = "resources/finalanswer.mp3";
	static final String MUSIC_LETS_PLAY = "resources/letsplay.mp3";
	static final String MUSIC_THEME = "resources/millionaire_theme.mp3";

	// Constant numbers needed
	static final int REVEAL_DELAY = 3000;

	// The game state
	static String userName;
	static String [][] questions;
	static int level = 0;
	static int questionIndex;
	static int correct;
	static int choice = -1;
	static MP3Player audioPlaying;

	// Settings
	static boolean useMusic = true;

	// Leaderboard
	static int [] leaderboardAmounts;
	static String [] leaderboardNames;


	/**
	 * CREATE MAIN WINDOW
	 * This method is called by the main method to set up the main GUI window.
	 */
	public static void createMainWindow () {
		// Create and set up the window.
		frame = new JFrame("Who Wants to be a Millionaire?");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new CloseHandler());
		frame.setIconImage(logo.getImage());

		// Create menubar
		JMenuBar menubar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_G);
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newGameItem.addActionListener(new NewGameHandler());
		gameMenu.add(newGameItem);
		JMenuItem walkAwayItem = new JMenuItem("Walk Away", KeyEvent.VK_W);
		walkAwayItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		walkAwayItem.addActionListener(new WalkAwayHandler());
		gameMenu.add(walkAwayItem);
		JMenuItem showLeaderboardItem = new JMenuItem("Show Leaderboard...", KeyEvent.VK_L);
		showLeaderboardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		showLeaderboardItem.addActionListener(new ShowLeaderboardHandler());
		gameMenu.add(showLeaderboardItem);
		JMenuItem quitItem = new JMenuItem("Quit Program", KeyEvent.VK_Q);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quitItem.addActionListener(new QuitHandler());
		gameMenu.add(quitItem);
		menubar.add(gameMenu);
		JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic(KeyEvent.VK_S);
		JMenuItem changeNameItem = new JMenuItem("Change Name...", KeyEvent.VK_N);
		changeNameItem.addActionListener(new ChangeNameHandler());
		settingsMenu.add(changeNameItem);
		JCheckBoxMenuItem toggleMusicItem = new JCheckBoxMenuItem("Music", true);
		toggleMusicItem.setMnemonic(KeyEvent.VK_M);
		toggleMusicItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		toggleMusicItem.addItemListener(new ToggleMusicHandler());
		settingsMenu.add(toggleMusicItem);
		menubar.add(settingsMenu);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem instructionsItem = new JMenuItem("Show Instructions...", KeyEvent.VK_I);
		instructionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		instructionsItem.addActionListener(new InstructionsHandler());
		helpMenu.add(instructionsItem);
		JMenuItem whatIsItem = new JMenuItem("What is Who Wants to Be a Millionaire?...", KeyEvent.VK_W);
		whatIsItem.addActionListener(new WhatIsHandler());
		helpMenu.add(whatIsItem);
		JMenuItem aboutItem = new JMenuItem("About This Program...", KeyEvent.VK_A);
		aboutItem.addActionListener(new AboutHandler());
		helpMenu.add(aboutItem);
		menubar.add(helpMenu);
		frame.setJMenuBar(menubar);

		// Create a panel to hold the components
		JPanel panel = new JPanel();
		panel.setSize(1000, 800);
		panel.setLayout(new BorderLayout());

		// Create the question panel
		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new GridLayout(2, 1, 10, 10));
		questionPanel.setPreferredSize(new Dimension(1000, 300));
		questionPanel.setBackground(BACKGROUND_COLOR);
		questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Create the box that holds the question
		JLabel questionBox = new JLabel("QUESTION");
		questionBox.setOpaque(true);
		questionBox.setBackground(BOX_COLOR);
		questionBox.setForeground(Color.WHITE);
		questionBox.setBorder(raisedBorder);
		questionBox.setHorizontalAlignment(JLabel.CENTER);
		questionBox.setFont(new Font("Arial",Font.BOLD, 24));
		questionPanel.add(questionBox);
		questionLabel = questionBox;

		// Create the panel with the multiple choice answers.
		JPanel answersPanel = new JPanel();
		answersPanel.setLayout(new GridLayout(2, 2, 10, 10));
		answersPanel.setBackground(BACKGROUND_COLOR);

		// Create the buttons that hold the multiple choice answers
		ChoiceButtonHandler choiceHandler = new ChoiceButtonHandler();
		for (int i = 0; i < 4; i++) {
			JButton answerButton = new JButton(Character.toString((char) (i + 65)));
			answerButton.setBackground(BOX_COLOR);
			answerButton.setForeground(Color.WHITE);
			answerButton.setBorder(raisedBorder);
			answerButton.setFont(new Font("Arial", Font.BOLD, 20));
			answerButton.addActionListener(choiceHandler);
			answerButton.setMnemonic((char) (i + 65));
			answersPanel.add(answerButton);
			answerButtons[i] = answerButton;
		}
		questionPanel.add(answersPanel);
		panel.add(questionPanel, BorderLayout.SOUTH);

		// Create the money ladder
		JPanel ladderPanel = new JPanel();
		int ladderSize = PRIZES.length - 1;
		ladderPanel.setLayout(new GridLayout(ladderSize, 1, 10, 10));
		ladderPanel.setPreferredSize(new Dimension(300, 500));
		ladderPanel.setBackground(BACKGROUND_COLOR);
		ladderPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// Create the labels that hold the money amounts in the ladder
		for (int i = ladderSize; i > 0; i--) {
			JLabel prizeLabel = new JLabel("$" + PRIZES[i]);
			prizeLabel.setHorizontalAlignment(JLabel.CENTER);
			prizeLabel.setForeground(i % 5 == 0 ? Color.WHITE : PRIZE_COLOR);
			prizeLabel.setOpaque(true);
			prizeLabel.setBackground(BACKGROUND_COLOR);
			prizeLabel.setFont(new Font("Verdana", Font.BOLD, 20));
			ladderPanel.add(prizeLabel);
			prizeLabels[i - 1] = prizeLabel;
		}
		panel.add(ladderPanel, BorderLayout.EAST);

		// Create the central panel
		centerPanel = new JLayeredPane();
		background.setBounds(0, 0, 700, 500);
		centerPanel.add(background, Integer.valueOf(-100));
		confetti.setBounds(0, 0, 700, 500);
		centerPanel.add(confetti, Integer.valueOf(-1));
		amount.setFont(new Font("Verdana", Font.BOLD, 64));
		amount.setOpaque(true);
		amount.setBackground(BOX_COLOR);
		amount.setForeground(Color.WHITE);
		amount.setHorizontalAlignment(JLabel.CENTER);
		amount.setVerticalAlignment(JLabel.CENTER);
		amount.setBorder(raisedBorderThick);
		amount.setBounds(50, 350, 600, 120);
		centerPanel.add(amount);
		panel.add(centerPanel, BorderLayout.CENTER);

		// Create the name box
		nameBox.setBounds(200, 20, 300, 40);
		nameBox.setFont(new Font("Verdana", Font.BOLD, 20));
		nameBox.setOpaque(true);
		nameBox.setBorder(raisedBorder);
		nameBox.setBackground(BOX_COLOR);
		nameBox.setForeground(Color.WHITE);
		nameBox.setHorizontalAlignment(JLabel.CENTER);
		nameBox.setVerticalAlignment(JLabel.CENTER);
		centerPanel.add(nameBox);

		// Create the final answer box
		finalAnswerBox.setBounds(150, 150, 400, 200);
		finalAnswerBox.setLayout(new BorderLayout());
		finalAnswerBox.setOpaque(true);
		finalAnswerBox.setBorder(raisedBorderThick);
		finalAnswerBox.setBackground(BOX_COLOR);
		finalAnswerBoxQuestion.setText(makeWrapable("Is that your final answer?", 260));
		finalAnswerBoxQuestion.setFont(new Font("Verdana", Font.BOLD, 24));
		finalAnswerBoxQuestion.setHorizontalAlignment(JLabel.CENTER);
		finalAnswerBoxQuestion.setVerticalAlignment(JLabel.CENTER);
		finalAnswerBoxQuestion.setForeground(Color.WHITE);
		finalAnswerBoxQuestion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		finalAnswerBox.add(finalAnswerBoxQuestion, BorderLayout.CENTER);
		JPanel finalAnswerBoxButtons = new JPanel();
		DialogButtonHandler dialogHandler = new DialogButtonHandler();
		finalAnswerBoxButtons.setLayout(new GridLayout(1, 2, 20, 20));
		finalAnswerBoxConfirm.setFont(new Font("Verdana", Font.BOLD, 18));
		finalAnswerBoxConfirm.setBorder(raisedBorder);
		finalAnswerBoxConfirm.setBackground(BORDER_COLOR);
		finalAnswerBoxConfirm.setForeground(Color.BLACK);
		finalAnswerBoxConfirm.addActionListener(dialogHandler);
		finalAnswerBoxConfirm.setMnemonic(KeyEvent.VK_Y);
		finalAnswerBoxButtons.add(finalAnswerBoxConfirm);
		finalAnswerBoxDeny.setFont(new Font("Verdana", Font.BOLD, 18));
		finalAnswerBoxDeny.setBorder(raisedBorder);
		finalAnswerBoxDeny.setBackground(BORDER_COLOR);
		finalAnswerBoxDeny.setForeground(Color.BLACK);
		finalAnswerBoxDeny.addActionListener(dialogHandler);
		finalAnswerBoxDeny.setMnemonic(KeyEvent.VK_N);
		finalAnswerBoxButtons.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		finalAnswerBoxButtons.setBackground(BOX_COLOR);
		finalAnswerBoxButtons.add(finalAnswerBoxDeny);
		finalAnswerBox.add(finalAnswerBoxButtons, BorderLayout.SOUTH);
		centerPanel.add(finalAnswerBox);

		// Create the leaderboard
		leaderboardWindow = new JFrame("Leaderboard");
		leaderboardWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JPanel leaderboardPanel = new JPanel();
		leaderboardPanel.setLayout(new BorderLayout());
		leaderboard = new DefaultListModel<String>();
		readLeaderboard();
		JList<String> leaderboardList = new JList<String>(leaderboard);
		leaderboardList.setFont(new Font("Verdana", 0, 16));
		JScrollPane leaderboardScroller = new JScrollPane(leaderboardList);
		leaderboardScroller.setPreferredSize(new Dimension(400, 300));
		leaderboardScroller.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		leaderboardPanel.add(leaderboardScroller, BorderLayout.CENTER);
		JPanel leaderboardButtons = new JPanel();
		leaderboardButtons.setLayout(new BoxLayout(leaderboardButtons, BoxLayout.LINE_AXIS));
		leaderboardButtons.add(Box.createHorizontalGlue());
		JButton leaderboardReset = new JButton("Reset");
		leaderboardReset.addActionListener(new ResetLeaderboardHandler());
		leaderboardButtons.add(leaderboardReset);
		leaderboardButtons.add(Box.createRigidArea(new Dimension(20, 0)));
		JButton leaderboardClose = new JButton("Close");
		leaderboardClose.addActionListener(new CloseLeaderboardHandler());
		leaderboardButtons.add(leaderboardClose);
		leaderboardButtons.add(Box.createHorizontalGlue());
		leaderboardButtons.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		leaderboardPanel.add(leaderboardButtons, BorderLayout.SOUTH);
		leaderboardWindow.setContentPane(leaderboardPanel);
		leaderboardWindow.pack();

		// Read in the questions from the question bank.
		readQuestions();

		// Start the music
		audioPlaying = new MP3Player(MUSIC_THEME);
		audioPlaying.play();

		// Get the user's name
		getName();

		// Start a new game
		newGame();

		// Add the panel to the frame
		frame.setContentPane(panel);

		// Show the frame
		frame.setResizable (false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	/**
	 * HELPER METHODS
	 * Methods that you create to manage repetitive tasks.
	 */

	/**
	 * Opens a dialog to get the user's name.  The name is updated in the program.
	 */
	public static void getName () {
		// Open the dialog box and wait.
		String newUserName = (String) JOptionPane.showInputDialog(frame,
				"What is your name?", "Name", JOptionPane.PLAIN_MESSAGE, null, null, userName);

		if (userName == null && newUserName == null) {
			// Cancel on initial name input.  Quit the program.
			int confirmed = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to quit the program?", "Quit",
					JOptionPane.YES_NO_OPTION);

			if (confirmed == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
			getName();
		}
		else if (newUserName != null && newUserName.equals("")) {
			// The name is empty.  Ask again.
			JOptionPane.showMessageDialog(frame, "You must give a name!",
					"Invalid Name", JOptionPane.ERROR_MESSAGE);
			getName();
		}
		else {
			// Name is good.  Change it.
			userName = newUserName;
			nameBox.setText(userName);
		}
	}

	/**
	 * Reads the questions from the database and stores them in "questions".
	 */
	public static void readQuestions () {
		try {
			// Open the database file.
			Scanner input = new Scanner(new File(QUESTION_DATABASE));

			// Read in the number of questions
			int numQuestions = Integer.parseInt(input.nextLine());
			questions = new String [numQuestions][];

			for (int i = 0; i < numQuestions; i++) {
				// Read the line and separate it wherever there is a tab
				// The fields on each line are:
				//  0 - question
				//  1 - correct answer (A, B, C, or D)
				//  2 - choice A
				//  3 - choice B
				//  4 - choice C
				//  5 - choice D
				String line = input.nextLine();
				questions[i] = line.split("\t");
			}

			// Close the file
			input.close();
		}
		catch (FileNotFoundException ex) {
			// The questions file was not found.  End the program.
			System.err.println("Questions file not found!");
			System.exit(1);  // Exit with non-zero error code
		}
	}

	/**
	 * Randomly picks a question from the questions array and puts it in
	 * the correct boxes.
	 */
	public static void pickQuestion () {
		// Pick a random question
		questionIndex = random.nextInt(questions.length);
		String [] question = questions[questionIndex];

		// Put the question on the screen
		questionLabel.setText(makeWrapable(question[0], 720));

		// The choices are randomized and put in the buttons on the screen
		boolean [] filled = new boolean [4];
		for (int i = 0; i < 4; i++) {
			int r = random.nextInt(4 - i);
			int index = 0;
			while (r > 0 || filled[index]) {
				if (!(filled[index]))
				{
					r--;
				}
				index++;
			}
			filled[index] = true;
			if (i == (int) (question[1].charAt(0)) - 65) {
				correct = index;
			}
			answerButtons[index].setText((char) (index + 65) + ": " + question[i + 2]);
			answerButtons[index].setBackground(BOX_COLOR);
			answerButtons[index].setForeground(Color.WHITE);
		}
		answerButtons[0].requestFocus();

		// Reset the choice variable
		choice = -1;
	}

	/**
	 * Starts a new game.
	 * The screen is reset and the first question is selected.
	 */
	public static void newGame () {
		// Play the "let's play" music
		if (useMusic) {
			audioPlaying.stop();
			audioPlaying = new MP3Player(MUSIC_LETS_PLAY);
			audioPlaying.play();
			audioPlaying.playNext(MUSIC_MAIN_BACKGROUND);
			audioPlaying.setRepeats(true);
		}

		// Reset the screen
		for (int i = 0; i < 4; i++) {
			answerButtons[i].setBackground(BOX_COLOR);
			answerButtons[i].setForeground(Color.WHITE);
		}
		for (int i = 1; i <= 15; i++) {
			prizeLabels[i - 1].setBackground(BACKGROUND_COLOR);
			prizeLabels[i - 1].setForeground(i % 5 == 0 ? Color.WHITE : PRIZE_COLOR);
		}
		level = 0;
		confetti.setVisible(false);
		amount.setBackground(BOX_COLOR);
		amount.setVisible(false);
		finalAnswerBox.setVisible(false);

		// Pick the first question
		pickQuestion();
	}

	/**
	 * Asks the user if their choice is their "final answer".
	 * This is called after one of the choice buttons is clicked.
	 */
	public static void finalAnswer () {
		// Highlight the choice
		answerButtons[choice].setBackground(CHOICE_COLOR);
		answerButtons[choice].setForeground(Color.BLACK);

		// Play the "final answer" music
		if (useMusic) {
			audioPlaying.stop();
			audioPlaying = new MP3Player(MUSIC_FINAL_ANSWER);
			audioPlaying.play();
			audioPlaying.playNext(MUSIC_MAIN_BACKGROUND);
			audioPlaying.setRepeats(true);
		}

		// Ask if its the final answer
		finalAnswerBox.setVisible(true);
		finalAnswerBoxConfirm.requestFocus();
	}

	/**
	 * Checks if the chosen answer is correct and responds appropriately for each case.
	 */
	public static void checkAnswer () {
		answerButtons[correct].setBackground(CORRECT_COLOR);
		answerButtons[correct].setForeground(Color.BLACK);
		if (choice == correct) {
			correctAnswer();
		}
		else {
			incorrectAnswer();
		}
	}

	/**
	 * Carries out the actions necessary when the user answers correctly.
	 */
	public static void correctAnswer () {
		// The "correct answer" music is played
		if (useMusic) {
			audioPlaying.stop();
			audioPlaying = new MP3Player(MUSIC_CORRECT_ANSWER);
			audioPlaying.play();
			audioPlaying.playNext(MUSIC_MAIN_BACKGROUND);
			audioPlaying.setRepeats(true);
		}

		// Adjusts the money ladder
		if (level > 0) {
			prizeLabels[level - 1].setBackground(BACKGROUND_COLOR);
			prizeLabels[level - 1].setForeground(level % 5 == 0 ? Color.WHITE : PRIZE_COLOR);
		}
		level++;
		prizeLabels[level - 1].setBackground(PRIZE_COLOR);
		prizeLabels[level - 1].setForeground(Color.BLACK);

		// If this was the "million dollar question", we want to celebrate.
		if (level == 15) {
			confetti.setVisible(true);
			amount.setText("MILLIONAIRE");
			amount.setBackground(CORRECT_COLOR);
			addLeaderboardEntry();
		}
		else {
			amount.setText("$" + PRIZES[level]);
		}
		amount.setVisible(true);

		// Set a timer for when the next actions should take place.
		Timer correctTimer = new Timer(REVEAL_DELAY, new CorrectTimerHandler());
		correctTimer.setRepeats(false);
		correctTimer.start();
	}

	/**
	 * Carries out the actions necessary when a question is answered incorrectly.
	 */
	public static void incorrectAnswer () {
		// Play the "wrong answer" music
		if (useMusic) {
			audioPlaying.stop();
			audioPlaying = new MP3Player(MUSIC_WRONG_ANSWER);
			audioPlaying.play();
		}

		// Determine how much money was won.
		level = (level / 5) * 5;

		amount.setBackground(CHOICE_COLOR);
		gameOver();
	}

	/**
	 * Carries out the actions necessary when a wrong answer is given.
	 */
	public static void gameOver () {
		// Show how much they won.
		amount.setText("$" + PRIZES[level]);
		amount.setVisible(true);

		// Update the leaderboard
		addLeaderboardEntry();

		// Set a timer for when the next actions should take place.
		Timer incorrectTimer = new Timer(REVEAL_DELAY, new IncorrectTimerHandler());
		incorrectTimer.setRepeats(false);
		incorrectTimer.start();
	}

	/**
	 * Ask the user if they would like to play again.  If so, start a new game.
	 * If not, quit the program.
	 */
	public static void replay () {
		int response = JOptionPane.showConfirmDialog(frame, "Would you like to play again?",
				"Game Over", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.NO_OPTION) {
			System.exit(0);  // Zero error code means no errors
		}
		newGame();
	}

	/**
	 * Makes a string for a JLabel wrappable.  By default JLabel does not wrap
	 * when it is full.
	 * NOTE: for some reason the HTML pixel width does not match the Swing component pixel width.
	 */
	public static String makeWrapable(String text, int width) {
		return "<html><body align=\"center\" style=\"width:" + width + "px;\">" + text +
				"</body></html>";
	}

	/**
	 * Reads the leaderboard entries from the file.
	 */
	public static void readLeaderboard () {
		try {
			// Open the database file.
			Scanner input = new Scanner(new File(LEADERBOARD_FILE));

			// Read in the number of questions
			int numLeaders = Integer.parseInt(input.nextLine());
			leaderboardAmounts = new int [numLeaders];
			leaderboardNames = new String [numLeaders];

			leaderboard.clear();
			for (int i = 0; i < numLeaders; i++) {
				String line = input.nextLine();
				String [] parts = line.split("\t");
				leaderboardAmounts[i] = Integer.parseInt(parts[0]);
				leaderboardNames[i] = parts[1];
				leaderboard.addElement(leaderboardAmounts[i] + " - " + leaderboardNames[i]);
			}

			// Close the file
			input.close();
		}
		catch (FileNotFoundException ex) {
			// The leaderboard file was not found.  Make it blank.
			System.err.println("Leaderboard file not found!");
			leaderboardAmounts = new int [0];
			leaderboardNames = new String [0];
		}
	}

	/**
	 * Writes the leaderboard entries to the file.
	 */
	public static void writeLeaderboard () {
		try {
			// Open the database file.
			BufferedWriter output = new BufferedWriter(new FileWriter(LEADERBOARD_FILE));

			// Write in the number of questions
			output.write(leaderboardNames.length + "\n");

			// Write the entries
			for (int i = 0; i < leaderboardNames.length; i++) {
				output.write(leaderboardAmounts[i] + "\t" + leaderboardNames[i] + "\n");
			}

			// Close the file
			output.close();
		}
		catch (IOException ex) {
			// There was an error reading the file.  End the program.
			System.err.println("Error while reading the leaderboard file!");
		}
	}

	/**
	 * Adds this game to the leaderboard.  Games that result if $0 are not added.
	 * This will also update the leaderboard file.
	 */
	public static void addLeaderboardEntry () {
		if (level > 0) {
			// Create new arrays with one more element.
			int [] newAmounts = new int [leaderboardAmounts.length + 1];
			String [] newNames = new String [leaderboardNames.length + 1];

			// Pass through the array until the correct position is found.
			int index = 0;
			while (index < leaderboardAmounts.length && leaderboardAmounts[index] >= PRIZES[level]) {
				newAmounts[index] = leaderboardAmounts[index];
				newNames[index] = leaderboardNames[index];
				index++;
			}

			// Add this game
			newAmounts[index] = PRIZES[level];
			newNames[index] = userName;
			leaderboard.add(index, PRIZES[level] + " - " + userName);

			// Continue passing through the rest of the elements
			while (index < leaderboardAmounts.length) {
				newAmounts[index + 1] = leaderboardAmounts[index];
				newNames[index + 1] = leaderboardNames[index];
				index++;
			}

			// Update the variables and save to the file.
			leaderboardAmounts = newAmounts;
			leaderboardNames = newNames;
			writeLeaderboard();
		}
	}

	/**
	 * Implements the action taken when one of the choice buttons is clicked.
	 */
	public static class ChoiceButtonHandler implements ActionListener {
		/** Method called when the button is clicked. */
		public void actionPerformed (ActionEvent e) {
			// Only go on if a button wasn't previously clicked.
			if (choice == -1) {
				// Figure out which button was clicked and store choice
				for (int i = 0; i < 4; i++) {
					if (e.getSource() == answerButtons[i]) {
						choice = i;
					}
				}

				// Ask if this is their "final answer".
				finalAnswer();
			}
		}
	}

	/**
	 * Implements the action taken when one of the dialog buttons is clicked.
	 */
	public static class DialogButtonHandler implements ActionListener {
		/** Method called when the button is clicked. */
		public void actionPerformed (ActionEvent e) {
			if (e.getSource() == finalAnswerBoxConfirm) {
				finalAnswerBox.setVisible(false);
				checkAnswer();
			}
			else if (e.getSource() == finalAnswerBoxDeny) {
				// Unselect the choice
				answerButtons[choice].setBackground(BOX_COLOR);
				answerButtons[choice].setForeground(Color.WHITE);
				choice = -1;
				finalAnswerBox.setVisible(false);
			}
		}
	}

	/**
	 * Implements the action taken after the correct answer timer has elapsed.
	 */
	public static class CorrectTimerHandler implements ActionListener {
		/** Method called when the time has elapsed. */
		public void actionPerformed (ActionEvent e) {
			if (level == 15) {
				// The user is a millionaire.  Congratulate them and end the game.
				replay();
			}
			else {
				// Not to a million yet.  Ask another question.
				amount.setVisible(false);
				pickQuestion();
			}
		}
	}

	/**
	 * Implements the action taken after the correct answer timer has elapsed.
	 */
	public static class IncorrectTimerHandler implements ActionListener {
		/** Method called when the time has elapsed. */
		public void actionPerformed (ActionEvent e) {
			replay();
		}
	}

	/**
	 * Implements the action taken when the close button is clicked.
	 */
	public static class CloseHandler implements WindowListener {
		/** Method called when the frame close button is clicked. */
		public void windowClosing(WindowEvent e) {
			int confirmed = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to quit the program?", "Quit",
					JOptionPane.YES_NO_OPTION);

			if (confirmed == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

		/** This method does nothing. */
		public void windowOpened(WindowEvent e) {}

		/** This method does nothing. */
		public void windowClosed(WindowEvent e) {}

		/** This method does nothing. */
		public void windowIconified(WindowEvent e) {}

		/** This method does nothing. */
		public void windowDeiconified(WindowEvent e) {}

		/** This method does nothing. */
		public void windowActivated(WindowEvent e) {}

		/** This method does nothing. */
		public void windowDeactivated(WindowEvent e) {}
	}

	/**
	 * Implements the actions taken when "New Game" menu item is clicked.
	 */
	public static class NewGameHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			int confirmed = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to start a new game?", "New Game",
					JOptionPane.YES_NO_OPTION);

			if (confirmed == JOptionPane.YES_OPTION) {
				newGame();
			}
		}
	}

	/**
	 * Implements the actions taken when "Walk Away" menu item is clicked.
	 */
	public static class WalkAwayHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			int confirmed = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to walk away with your winnings?", "Walk Away",
					JOptionPane.YES_NO_OPTION);

			if (confirmed == JOptionPane.YES_OPTION) {
				// Play the "wrong answer" music
				if (useMusic) {
					audioPlaying.stop();
					audioPlaying = new MP3Player(MUSIC_CORRECT_ANSWER);
					audioPlaying.play();
				}
				amount.setBackground(CORRECT_COLOR);
				gameOver();
			}
		}
	}

	/**
	 * Implements the actions taken when "Show Leaderboard" menu item is clicked.
	 */
	public static class ShowLeaderboardHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			leaderboardWindow.setVisible(true);
		}
	}

	/**
	 * Implements the actions taken when "Quit Program" menu item is clicked.
	 */
	public static class QuitHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			int confirmed = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to quit the program?", "Quit",
					JOptionPane.YES_NO_OPTION);

			if (confirmed == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	/**
	 * Implements the actions taken when "Change Name" menu item is clicked.
	 */
	public static class ChangeNameHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			getName();
		}
	}

	/**
	 * Implements the actions taken when "Music" menu item is clicked.
	 */
	public static class ToggleMusicHandler implements ItemListener {
		/** Method called when a menu item is clicked. */
		public void itemStateChanged (ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				useMusic = true;
				audioPlaying = new MP3Player(MUSIC_MAIN_BACKGROUND);
				audioPlaying.setRepeats(true);
				audioPlaying.play();

			}
			else if (e.getStateChange() == ItemEvent.DESELECTED) {
				useMusic = false;
				audioPlaying.stop();
			}
		}
	}

	/**
	 * Implements the actions taken when "Show Instructions" menu item is clicked.
	 */
	public static class InstructionsHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			try {
				// Read the instructions file
				Scanner input = new Scanner(new File(INSTRUCTIONS_FILE));
				String line;
				String instructions = "";
				while ((line = input.nextLine()) != null) {
					instructions += line + "\n";
				}
				input.close();

				// Show the instructions dialog
				JOptionPane.showMessageDialog(frame, instructions, "Instructions",
						JOptionPane.INFORMATION_MESSAGE, logo);
			}
			catch (Exception ex) {
				System.err.println("Could not open instructions file.");
				JOptionPane.showMessageDialog(frame, "COULD NOT OPEN INSTRUCTIONS!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Implements the actions taken when "What is ..." menu item is clicked.
	 */
	public static class WhatIsHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			try {
				// Open the webpage in a browser.
				Desktop.getDesktop().browse(new URL(WIKIPEDIA_PAGE).toURI());
			}
			catch (Exception ex) {
				System.err.println("Could not open the webpage!");
				JOptionPane.showMessageDialog(frame, "Visit " + WIKIPEDIA_PAGE +
						" for more information.", "What Is ...", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * Implements the actions taken when "About..." menu item is clicked.
	 */
	public static class AboutHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			String license = "";
			try {
				// Read the license file
				Scanner input = new Scanner(new File(LICENSE_FILE));
				String line;
				while ((line = input.nextLine()) != null) {
					license += line + "\n";
				}
				input.close();
			}
			catch (Exception ex) {
				System.err.println("Could not open license file.");
				license = "Copyright 2020 Casey Devet";
			}

			// Show the about dialog.
			JOptionPane.showMessageDialog(frame, "Version " + VERSION + "\n\n" + license,
					"About This Program", JOptionPane.INFORMATION_MESSAGE, logo);
		}
	}

	/**
	 * Implements the action when "Close" is clicked in the Leaderboard.
	 */
	public static class CloseLeaderboardHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			leaderboardWindow.setVisible(false);
		}
	}

	/**
	 * Implements the action when "Reset" is clicked in the Leaderboard.
	 */
	public static class ResetLeaderboardHandler implements ActionListener {
		/** Method called when a menu item is clicked. */
		public void actionPerformed (ActionEvent e) {
			leaderboardAmounts = new int [0];
			leaderboardNames = new String [0];
			leaderboard.clear();
			writeLeaderboard();
		}
	}
}
