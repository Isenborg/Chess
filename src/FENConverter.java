package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

public class FENConverter
{
    public static List<Piece> loadBoardFromFEN(String fen){
        List<Piece> pieces = new ArrayList<>();
        int squareNum = 0;
	for (int i = 0; i < fen.length(); i++){
            char current = fen.charAt(i);
            switch(current){
                case 'r' -> pieces.add(new Rook(squareNum, PieceColor.BLACK));
                case 'R' -> pieces.add(new Rook(squareNum, PieceColor.WHITE));
                case 'n' -> pieces.add(new Knight(squareNum, PieceColor.BLACK));
                case 'N' -> pieces.add(new Knight(squareNum, PieceColor.WHITE));
                case 'b' -> pieces.add(new Bishop(squareNum, PieceColor.BLACK));
                case 'B' -> pieces.add(new Bishop(squareNum, PieceColor.WHITE));
                case 'q' -> pieces.add(new Queen(squareNum, PieceColor.BLACK));
                case 'Q' -> pieces.add(new Queen(squareNum, PieceColor.WHITE));
                case 'k' -> pieces.add(new King(squareNum, PieceColor.BLACK));
                case 'K' -> pieces.add(new King(squareNum, PieceColor.WHITE));
                case 'p' -> pieces.add(new Pawn(squareNum, PieceColor.BLACK));
                case 'P' -> pieces.add(new Pawn(squareNum, PieceColor.WHITE));
                case '/' -> squareNum--;
                default -> squareNum += Character.getNumericValue(current)-1;
            }
            squareNum++;
        }
        return pieces;
    }
}
