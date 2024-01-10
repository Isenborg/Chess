package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

public class Queen extends AbstractPiece
{
    public Queen(final int boardPos, final PieceColor pieceColor){
	super(boardPos, PieceType.QUEEN, pieceColor);
    }

    @Override public List<Integer> getPossibleMoves(final Board board) {
	List<Integer> possibleMoves = new ArrayList<>();
	if (!isPlayersTurn(board.isWhiteToMove()))return possibleMoves;
	List<Integer> rookMoves = new Rook(pos,color).getPossibleMoves(board);
	List<Integer> bishopMoves = new Bishop(pos,color).getPossibleMoves(board);

	possibleMoves.addAll(rookMoves);
	possibleMoves.addAll(bishopMoves);
	return possibleMoves;
    }


}
