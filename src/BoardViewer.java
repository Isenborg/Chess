package se.liu.marfr380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for displaying the chess board.
 */
public class BoardViewer {
    private JFrame frame;
    private BoardComponent comp;
    private Board board;

    /**
     * Constructs a BoardViewer object with the specified board.
     *
     * @param board The chess board to display.
     */
    public BoardViewer(final Board board){
	this.board = board;
	comp = new BoardComponent(board);
	comp.showSquareNumber(false);
	initialize();
    }

    /**
     * Initializes the frame and sets up the components.
     */
    private void initialize(){
	frame = new JFrame("Chess");
	frame.setLayout(new BorderLayout());
	frame.setSize(1000, 1000);
	frame.setLocationRelativeTo(null);
	frame.add(comp, BorderLayout.CENTER);
	loadBar();
    }

    /**
     * Loads the menu bar with options.
     */
    private void loadBar(){
	JMenuBar bar = new JMenuBar();
	JMenuItem exitOption = new JMenuItem("Exit");

	exitOption.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		tryExit();
	    }
	});
	JMenuItem restartOption = new JMenuItem("Restart");
	restartOption.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		restartGame();
	    }
	});
	JMenu options = new JMenu("Options");
	options.add(restartOption);
	options.add(exitOption);

	bar.add(options);

	frame.setJMenuBar(bar);
    }

    /**
     * Restarts the game by resetting the board to the start position.
     */
    private void restartGame(){
	board.resetToStartPosition();
    }

    /**
     * Asks the user for confirmation before exiting the game.
     */
    private void tryExit(){
	Object[] options = {
		"Yes",
		"No"
	};
	int optionChosen = JOptionPane.showOptionDialog(
		frame,
		"Are you sure you want to exit?",
		"Exit",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.WARNING_MESSAGE,
		null,
		options,
		options[0]
	);

	if (optionChosen == 0){
	    System.exit(0);
	}
    }

    /**
     * Displays the frame and makes it visible.
     */
    public void show(){
	frame.setVisible(true);
    }
}
