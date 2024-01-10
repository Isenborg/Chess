package se.liu.marfr380;

import java.awt.*;
import java.util.List;

public interface Piece
{
    void setMoveStatus();
    int getPos();
    void setPos(final int pos);
    PieceType getType();
    PieceColor getColor();
    List<Integer> getPossibleMoves(Board board);
    List<Integer> findAllMoves(Board board);

    boolean hasMoved();


    boolean isLegalMove(final int square,Board board);
    Image getImage();
}
