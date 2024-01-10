package se.liu.marfr380;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rook extends AbstractPiece
{
    public Rook(final int boardPos, final PieceColor pieceColor){
	super(boardPos, PieceType.ROOK, pieceColor);

    }

    @Override public List<Integer> getPossibleMoves(final Board board) {
	List<Integer> possibleMoves = new ArrayList<>();
	if (!isPlayersTurn(board.isWhiteToMove()))return possibleMoves;
	List<Integer> offsets = getOffsets();
	List<Integer> toBeRemoved =new ArrayList<>();
	int i = 1;
	while(!offsets.isEmpty()) {
	    for (Integer offset : offsets) {
		int nextMove = pos + (offset * i);
		if (nextMove > 63 || nextMove < 0){
		    toBeRemoved.add(offset);

		} else if(Board.getColumnAtSquare(pos)!=Board.getColumnAtSquare(nextMove) && Board.getRowAtSquare(pos)!=Board.getRowAtSquare(nextMove)){
		    toBeRemoved.add(offset);
		} else if(board.getPieceAt(nextMove) == null) {
		    possibleMoves.add(nextMove);
		}else{
		    Piece pieceOnNextSquare = board.getPieceAt(nextMove);
		     if(pieceOnNextSquare.getColor() == this.color){
			 toBeRemoved.add(offset);
		     }else {
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

