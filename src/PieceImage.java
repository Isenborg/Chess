package se.liu.marfr380;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

public class PieceImage
{
    private final static String IMAGE_PATH = "resources/images/";


    public static Image loadImage(PieceType pieceType, PieceColor pieceColor){
        final EnumMap<PieceType, String> whitePieceMap = createWhitePieceMap();
        final EnumMap<PieceType, String> blackPieceMap = createBlackPieceMap();
        Image image;
        if (pieceColor == PieceColor.WHITE){
            image = new ImageIcon(IMAGE_PATH + whitePieceMap.get(pieceType)).getImage();
        }
        else {
            image = new ImageIcon(IMAGE_PATH + blackPieceMap.get(pieceType)).getImage();
        }
        return image;
    }

    private static EnumMap<PieceType, String> createBlackPieceMap() {
        EnumMap<PieceType, String> blackPieces = new EnumMap<>(PieceType.class);
        blackPieces.put(PieceType.KING, "Black_king.png");
        blackPieces.put(PieceType.QUEEN, "Black_queen.png");
        blackPieces.put(PieceType.BISHOP, "Black_bishop.png");
        blackPieces.put(PieceType.KNIGHT, "Black_knight.png");
        blackPieces.put(PieceType.PAWN, "Black_pawn.png");
        blackPieces.put(PieceType.ROOK, "Black_rook.png");
        return blackPieces;
    }

    private static EnumMap<PieceType, String> createWhitePieceMap() {
        EnumMap<PieceType, String> whitePieces = new EnumMap<>(PieceType.class);
        whitePieces.put(PieceType.KING, "White_king.png");
        whitePieces.put(PieceType.QUEEN, "White_queen.png");
        whitePieces.put(PieceType.BISHOP, "White_bishop.png");
        whitePieces.put(PieceType.KNIGHT, "White_knight.png");
        whitePieces.put(PieceType.PAWN, "White_pawn.png");
        whitePieces.put(PieceType.ROOK, "White_rook.png");
        return whitePieces;
    }
}
