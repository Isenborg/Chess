package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

public class King extends AbstractPiece
{
    public King(final int boardPos, final PieceColor pieceColor){
	super(boardPos, PieceType.KING, pieceColor);
    }

    @Override public List<Integer> getPossibleMoves(final Board board) {
	List<Integer> possibleMoves = new ArrayList<>();
	if (!isPlayersTurn(board.isWhiteToMove()))return possibleMoves;
	List<Integer> offsets = getOffsets();
	for (Integer offset : offsets) {
	    int nextMove = pos + (offset);
	    if (nextMove > 63 || nextMove < 0) {
		continue;
	    } else if (Math.abs(Board.getRowAtSquare(pos)- Board.getRowAtSquare(nextMove)) > 1) {
		continue;
	    }else if (Math.abs(Board.getColumnAtSquare(pos)- Board.getColumnAtSquare(nextMove)) > 1){
		continue;
	    } else if (board.getPieceAt(nextMove) == null) {
		possibleMoves.add(nextMove);
	    } else if (board.getPieceAt(nextMove).getColor()== color) {
		continue;
	    }else {
		possibleMoves.add(nextMove);
	    }
	}

	return possibleMoves;
    }




}
