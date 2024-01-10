package se.liu.marfr380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;


public class BoardComponent extends JComponent implements MouseListener, MouseMotionListener
{

    public final static Color LIGHTSQUARE = new Color(0xE0CBA3);
    public final static Color DARKSQUARE = new Color(0x443731);
    private Board board;
    private boolean showSquareNumber = false;
    private Piece heldPiece = null;
    private Clip move = AudioUtils.loadClip("audio/move.wav");
    private Clip capture = AudioUtils.loadClip("audio/capture.wav");
    public final static int SQUARESIZE = 100;
    private List<Piece> promotionPieces = null;
    private List<Integer> possibleMoves = null;

    private Point mousePos;

    /**
     * Constructs a BoardComponent object with the specified board.
     *
     * @param board The chess board to display.
     */
    public BoardComponent(final Board board){
	this.board = board;
	addMouseListener(this);
	addMouseMotionListener(this);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	drawBoard(g2d);
	if(heldPiece!=null) drawPossibleMoves(g2d);
	drawPieces(g2d);
	if (board.upForPromotion != null){
	    displayPromotionMenu(g2d);
	}
    }

    private void displayPromotionMenu(Graphics2D g2d){
	promotionPieces = loadPromotionPieces(board.promotionMenuStartSquare, board.upForPromotion.getColor());
	for (Piece piece : promotionPieces) {
	    int c = Board.getColumnAtSquare(piece.getPos());
	    int r = Board.getRowAtSquare(piece.getPos());
	    g2d.setColor(Color.WHITE);
	    if (squareFromCoordinate(mousePos.x, mousePos.y) == r * 8 + c) g2d.setColor(Color.gray);
	    g2d.fillRect(c * SQUARESIZE, r * SQUARESIZE, SQUARESIZE, SQUARESIZE);
	    g2d.drawImage(piece.getImage(), c * SQUARESIZE, r * SQUARESIZE, SQUARESIZE, SQUARESIZE, this);
	}
    }

    private void drawPossibleMoves(Graphics2D g2d){
	int size = SQUARESIZE / 4;
	int offset = (SQUARESIZE / 2) - (size / 2);
	g2d.setColor(Color.gray);
	for (Integer move : possibleMoves) {
	    int column = Board.getColumnAtSquare(move);
	    int row = Board.getRowAtSquare(move);
	    if (board.getPieceAt(move) == null) g2d.fillOval(column*SQUARESIZE + offset,row*SQUARESIZE + offset,size,size);
	    else {
		g2d.setStroke(new BasicStroke(7));
		g2d.drawOval(column*SQUARESIZE+4, row*SQUARESIZE+4, SQUARESIZE-8, SQUARESIZE-8);
	    }
	}
    }

    private void drawPieces(Graphics2D g2d){
	int squareNum = 0;
	for (int r = 0; r < Board.getROWS(); r++){
	    for (int c = 0; c < Board.getCOLUMNS(); c++){
		Piece current = board.getPieceAt(squareNum);
		if (current != null && !current.equals(heldPiece)) {
		    g2d.drawImage(current.getImage(), c * SQUARESIZE, r * SQUARESIZE, SQUARESIZE, SQUARESIZE, this);
		}
		squareNum++;
	    }
	}
	if (heldPiece != null){
	    if (getMousePosition() != null) {
		g2d.drawImage(heldPiece.getImage(), getMousePosition().x - (SQUARESIZE / 2), getMousePosition().y - (SQUARESIZE / 2),
			      SQUARESIZE, SQUARESIZE, this);
	    }
	}
    }

    private void promotionInteraction(int x, int y){
	int square = squareFromCoordinate(x,y);
	for (Piece piece : promotionPieces) {
	    if(piece.getPos() == square) board.promotionType = piece.getType();
	}
	board.promote(board.upForPromotion, board.promotionType);
	board.upForPromotion = null;
    }

    private void drawBoard(Graphics2D g2d){
	int squareNum = 0;
	for (int r = 0; r < Board.getROWS(); r++) {
	    for (int c = 0; c < Board.getCOLUMNS(); c++) {
		if ((r + c) % 2 == 0)
		    g2d.setColor(LIGHTSQUARE);
		else
		    g2d.setColor(DARKSQUARE);
		g2d.fillRect(c * SQUARESIZE, r * SQUARESIZE, SQUARESIZE, SQUARESIZE);

		if (showSquareNumber) {
		    g2d.setColor(Color.white);
		    g2d.drawString(Integer.toString(squareNum), c * SQUARESIZE, r * SQUARESIZE + 15);
		}
		squareNum++;
	    }
	}
    }

    /**
     * Shows or hides the square numbers on the board.
     *
     * @param showSquareNumber True to show square numbers, false to hide them.
     */
    public void showSquareNumber(final boolean showSquareNumber) {
	this.showSquareNumber = showSquareNumber;
    }

    private static List<Piece> loadPromotionPieces(int pos, PieceColor color){
	int offset;
	if (color == PieceColor.WHITE) offset = -8;
	else offset = 8;
	List<Piece> pieces = new ArrayList<>();
	pieces.add(new Queen(pos, color));
	pieces.add(new Knight(pos-offset, color));
	pieces.add(new Bishop(pos-(offset*2), color));
	pieces.add(new Rook(pos-(offset*3), color));
	return pieces;
    }

    private int squareFromCoordinate(int x, int y){
	int row = x / SQUARESIZE;
	int column = y / SQUARESIZE;
	int square = row + (column * 8);
	return square;
    }

    @Override public void mouseClicked(final MouseEvent e) {

    }

    @Override public void mousePressed(final MouseEvent e) {
	if (board.upForPromotion != null){
		promotionInteraction(e.getX(), e.getY());
		board.hasPressed = true;
		return;
	}
	int clickedSquare = squareFromCoordinate(e.getX(), e.getY());
	Piece clickedPiece = board.getPieceAt(clickedSquare);
	if (clickedPiece != null){
	    heldPiece = clickedPiece;
	    possibleMoves = heldPiece.findAllMoves(board);
	}
	repaint();
    }

    @Override public void mouseReleased(final MouseEvent e) {
	if(heldPiece == null)return;
	int releaseSquare = squareFromCoordinate(e.getX(), e.getY());
	boolean captured = board.getPieceAt(releaseSquare) != null;
	if(heldPiece.isLegalMove(releaseSquare,board)){
	    board.setLastMove(new Move(heldPiece.getPos(), releaseSquare));
	    board.movePiece(heldPiece.getPos(), releaseSquare, true);
	    if (captured) {
		capture.stop();
		capture.setMicrosecondPosition(0); // Reset sound
	    	capture.start();
	    }
	    else {
		move.stop();
		move.setMicrosecondPosition(0);
		move.start();
	    }
	}
	heldPiece = null;
	repaint();
	if(board.isMate()){
	    System.out.println("Mate!!!!!");
	}
    }

    @Override public void mouseEntered(final MouseEvent e) {

    }

    @Override public void mouseExited(final MouseEvent e) {

    }

    @Override public void mouseDragged(final MouseEvent e) {
	repaint();
    }

    @Override public void mouseMoved(final MouseEvent e) {
	mousePos = e.getPoint();
	repaint();
    }
}


