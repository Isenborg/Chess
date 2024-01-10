package se.liu.marfr380;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionComponent extends JComponent
{

    private  List<Piece> pieces = new ArrayList<>();

    public PromotionComponent(PieceColor color, int pos){
	int offset;
	if (color == PieceColor.WHITE) offset = 8;
	else offset = -8;

	pieces.add(new Queen(pos, color));
	pieces.add(new Knight(pos-offset, color));
	pieces.add(new Bishop(pos-(offset*2), color));
	pieces.add(new Rook(pos-(offset*3), color));
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	for (Piece piece : pieces) {
	    int c = Board.getColumnAtSquare(piece.getPos());
	    int r = Board.getRowAtSquare(piece.getPos());
	    int squaresize = BoardComponent.SQUARESIZE;
	    g2d.fillRect(c * squaresize, r * squaresize, squaresize, squaresize);
	    g2d.drawImage(piece.getImage(), c * squaresize, r * squaresize, squaresize, squaresize, this);
	}
    }
}
