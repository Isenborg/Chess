package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends AbstractPiece
{
    public Pawn(final int boardPos, final PieceColor pieceColor) {
	super(boardPos, PieceType.PAWN, pieceColor);
    }
    public List<Integer> getPossibleMoves(final Board board) {
	List<Integer> possibleMoves = new ArrayList<>();

	if (!isPlayersTurn(board.isWhiteToMove())) {
	    return possibleMoves;
	}

	int direction = color == PieceColor.WHITE ? -1 : 1;
	int nextMove = pos + (8 * direction);

	if (nextMove >= 0 && nextMove <= 63 && board.getPieceAt(nextMove) == null) {
	    possibleMoves.add(nextMove);
	    if (!hasMoved && nextMove + (8 * direction) >= 0 && nextMove + (8 * direction) <= 63 &&
		board.getPieceAt(nextMove + (8 * direction)) == null) {
		possibleMoves.add(nextMove + (8 * direction));
	    }
	}

	nextMove = pos + (7 * direction);
	if (nextMove >= 0 && nextMove <= 63 && Math.abs(Board.getColumnAtSquare(pos) - Board.getColumnAtSquare(nextMove)) == 1) {
	    if (board.getPieceAt(nextMove) != null && board.getPieceAt(nextMove).getColor() != color) {
		possibleMoves.add(nextMove);
	    }
	}

	nextMove = pos + (9 * direction);
	if (nextMove >= 0 && nextMove <= 63 && Math.abs(Board.getColumnAtSquare(pos) - Board.getColumnAtSquare(nextMove)) == 1) {
	    if (board.getPieceAt(nextMove) != null && board.getPieceAt(nextMove).getColor() != color) {
		possibleMoves.add(nextMove);
	    }
	}

	return possibleMoves;
    }

}
