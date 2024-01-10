package se.liu.marfr380;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Abstract class representing a chess piece.
 */
public abstract class AbstractPiece implements Piece
{
    protected int pos;
    protected PieceType type;
    protected PieceColor color;
    protected Image image;

    protected boolean isHeld = false;

    protected boolean hasMoved = false;

    private final EnumMap<PieceType, Offset> offsets = createOffsetMap();

    /**
     * Constructs an abstract piece with the specified position, type, and color.
     *
     * @param boardPos   The position of the piece on the board.
     * @param pieceType  The type of the piece.
     * @param pieceColor The color of the piece.
     */
    protected AbstractPiece(int boardPos, PieceType pieceType, PieceColor pieceColor) {
	this.pos = boardPos;
	this.type = pieceType;
	this.color = pieceColor;
	this.image = PieceImage.loadImage(pieceType, pieceColor);
    }

    /**
     * Sets the position of the piece.
     *
     * @param pos The new position of the piece.
     */
    public void setPos(final int pos) {
	this.pos = pos;
    }

    @Override public void setMoveStatus() {
	hasMoved = true;
    }

    /**
     * Returns the position of the piece.
     *
     * @return The position of the piece.
     */
    public int getPos() {
	return this.pos;
    }

    /**
     * Returns the type of the piece.
     *
     * @return The type of the piece.
     */
    public PieceType getType() {
	return this.type;
    }

    /**
     * Returns the color of the piece.
     *
     * @return The color of the piece.
     */
    public PieceColor getColor() {
	return this.color;
    }

    /**
     * Returns the image representation of the piece.
     *
     * @return The image representation of the piece.
     */
    public Image getImage() {
	return this.image;
    }

    /**
     * Returns the offsets for the piece's possible moves.
     *
     * @return The offsets for the piece's possible moves.
     */
    public List<Integer> getOffsets() {
	return offsets.get(type).getOffsets();
    }

    /**
     * Checks if the piece has moved.
     *
     * @return True if the piece has moved, false otherwise.
     */
    public boolean hasMoved() {
	return hasMoved;
    }

    /**
     * Checks if a move to the specified square is a legal move for the piece on the given board.
     *
     * @param square The square to move to.
     * @param board  The current state of the chess board.
     *
     * @return True if the move is legal, false otherwise.
     */
    public boolean isLegalMove(final int square, Board board) {
	return findAllMoves(board).contains(square);
    }

    /**
     * Creates an enum map for the piece offsets.
     *
     * @return The enum map for the piece offsets.
     */
    private static EnumMap<PieceType, Offset> createOffsetMap() {
	EnumMap<PieceType, Offset> pieceOffsets = new EnumMap<>(PieceType.class);
	pieceOffsets.put(PieceType.KING, Offset.KING);
	pieceOffsets.put(PieceType.BISHOP, Offset.BISHOP);
	pieceOffsets.put(PieceType.KNIGHT, Offset.KNIGHT);
	pieceOffsets.put(PieceType.PAWN, Offset.PAWN);
	pieceOffsets.put(PieceType.QUEEN, Offset.QUEEN);
	pieceOffsets.put(PieceType.ROOK, Offset.ROOK);
	return pieceOffsets;
    }

    /**
     * Checks if it is the player's turn to move.
     *
     * @param whiteToMove Indicates if it is currently white's turn to move.
     *
     * @return True if it is the player's turn, false otherwise.
     */
    public boolean isPlayersTurn(boolean whiteToMove) {
	if (whiteToMove && color == PieceColor.WHITE)
	    return true;
	else if (!whiteToMove && color == PieceColor.BLACK)
	    return true;
	else
	    return false;
    }

    /**
     * Finds all possible moves for the piece on the given board.
     *
     * @param board The current state of the chess board.
     *
     * @return A list of all possible moves for the piece.
     */
    public List<Integer> findAllMoves(Board board) {
	boolean copyHasMoved = this.hasMoved;
	int startPos = this.pos;
	List<Integer> movesToTest = getPossibleMoves(board);
	List<Integer> possibleMoves = new ArrayList<>();
	for (Integer move : movesToTest) {
	    Board copy = new Board(board);
	    copy.movePiece(startPos, move, false);
	    if (!copy.findAllPossibleMoves(getOpponentColor()).contains(copy.getKingPos(this.color)))
		possibleMoves.add(move);
	    this.hasMoved = copyHasMoved;
	    this.pos = startPos;
	}
	possibleMoves.addAll(findSpecialMoves(board));
	return possibleMoves;
    }

    /**
     * Finds the special moves for the piece on the given board.
     *
     * @param board The current state of the chess board.
     *
     * @return A list of special moves for the piece.
     */
    private List<Integer> findSpecialMoves(Board board) {
	List<Integer> specialMoves = new ArrayList<>();
	if (this.getType() == PieceType.KING && this.isPlayersTurn(board.isWhiteToMove())) {
	    if (canCastleQueenSide(board))
		specialMoves.add(pos - 2);
	    if (canCastleKingSide(board))
		specialMoves.add(pos + 2);
	}
	if (this.getType() == PieceType.PAWN && this.isPlayersTurn(board.isWhiteToMove())) {
	    if (color == PieceColor.WHITE) {
		if (canEnPassantRight(board))
		    specialMoves.add(pos - 7);
		if (canEnPassantLeft(board))
		    specialMoves.add(pos - 9);
	    }
	    if (color == PieceColor.BLACK) {
		if (canEnPassantRight(board))
		    specialMoves.add(pos + 9);
		if (canEnPassantLeft(board))
		    specialMoves.add(pos + 7);
	    }
	}

	return specialMoves;
    }

    /**
     * Checks if the pawn can perform en passant capture to the right.
     *
     * @param board The current state of the chess board.
     *
     * @return True if en passant capture to the right is possible, false otherwise.
     */
    private boolean canEnPassantRight(Board board) {
	Piece rightOfPawn = board.getPieceAt(pos + 1);
	if (rightOfPawn instanceof Pawn && rightOfPawn.getColor() == getOpponentColor()) {
	    Move lastMove = board.getLastMove();
	    if (lastMove.getTo() == pos + 1 && Math.abs(lastMove.getTo() - lastMove.getFrom()) == 16)
		return true;
	}
	return false;
    }

    /**
     * Checks if the pawn can perform en passant capture to the left.
     *
     * @param board The current state of the chess board.
     *
     * @return True if en passant capture to the left is possible, false otherwise.
     */
    private boolean canEnPassantLeft(Board board) {
	Piece rightOfPawn = board.getPieceAt(pos - 1);
	if (rightOfPawn instanceof Pawn && rightOfPawn.getColor() == getOpponentColor()) {
	    Move lastMove = board.getLastMove();
	    if (lastMove.getTo() == pos - 1 && Math.abs(lastMove.getTo() - lastMove.getFrom()) == 16)
		return true;
	}
	return false;
    }

    /**
     * Checks if the king can perform a king-side castle.
     *
     * @param board The current state of the chess board.
     *
     * @return True if king-side castle is possible, false otherwise.
     */
    public boolean canCastleKingSide(Board board) {
	if (hasMoved)
	    return false;
	Piece cornerPiece = board.getPieceAt(pos + 3);
	if (cornerPiece instanceof Rook && !cornerPiece.hasMoved()) {
	    List<Integer> opponentsMoves = board.findAllPossibleMoves(getOpponentColor());
	    for (int i = 1; i < 3; i++) {
		if (board.getPieceAt(pos + i) != null)
		    return false;
		if (opponentsMoves.contains(pos + i))
		    return false;
	    }
	    return true;
	}
	return false;
    }

    /**
     * Checks if the king can perform a queen-side castle.
     *
     * @param board The current state of the chess board.
     *
     * @return True if queen-side castle is possible, false otherwise.
     */
    public boolean canCastleQueenSide(Board board) {
	if (hasMoved)
	    return false;
	Piece cornerPiece = board.getPieceAt(pos - 4);
	if (cornerPiece instanceof Rook && !cornerPiece.hasMoved()) {
	    List<Integer> opponentsMoves = board.findAllPossibleMoves(getOpponentColor());
	    for (int i = 1; i < 4; i++) {
		if (board.getPieceAt(pos - i) != null)
		    return false;
		if (opponentsMoves.contains(pos - i))
		    return false;
	    }
	    return true;
	}
	return false;
    }

    /**
     * Returns the color of the opponent player.
     *
     * @return The color of the opponent player.
     */
    public PieceColor getOpponentColor() {
	PieceColor opponentColor;
	if (this.color == PieceColor.WHITE)
	    opponentColor = PieceColor.BLACK;
	else
	    opponentColor = PieceColor.WHITE;
	return opponentColor;
    }
}
