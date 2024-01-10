package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends AbstractPiece
{
    public Bishop(final int boardPos, final PieceColor pieceColor) {
	super(boardPos, PieceType.BISHOP, pieceColor);
    }

    @Override public List<Integer> getPossibleMoves(final Board board) {
	List<Integer> possibleMoves = new ArrayList<>();
	if (!isPlayersTurn(board.isWhiteToMove()))return possibleMoves;
	List<Integer> offsets = getOffsets();
	List<Integer> toBeRemoved = new ArrayList<>();
	int i = 1;
	while (!offsets.isEmpty()) {
	    for (Integer offset : offsets) {
		int nextMove = pos + (offset * i);
		if (nextMove > 63 || nextMove < 0) {
		    toBeRemoved.add(offset);

		} else if (Math.abs(Board.getColumnAtSquare(pos)-Board.getColumnAtSquare(nextMove)) != i) {
		    toBeRemoved.add(offset);
		} else if (Math.abs(Board.getRowAtSquare(pos)- Board.getRowAtSquare(nextMove)) !=i) {
		    toBeRemoved.add(offset);

		} else if (board.getPieceAt(nextMove) == null) {
		    possibleMoves.add(nextMove);
		} else {
		    Piece pieceOnNextSquare = board.getPieceAt(nextMove);
		    if (pieceOnNextSquare.getColor() == this.color) {
			toBeRemoved.add(offset);
		    } else {
			possibleMoves.add(nextMove);
			toBeRemoved.add(offset);
		    }
		}


	    }
	    offsets.removeAll(toBeRemoved);
	    toBeRemoved.clear();
	    i++;
	}

	return possibleMoves;
    }
}
