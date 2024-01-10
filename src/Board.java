package se.liu.marfr380;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chess board.
 */
public class Board
{
    private final static String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    private final static int ROWS = 8;
    private final static int COLUMNS = 8;

    public Piece upForPromotion = null;
    public int promotionMenuStartSquare;
    public PieceType promotionType = null;
    public boolean hasPressed = false;

    private Move lastMove = null;

    private Piece[] board = new Piece[ROWS * COLUMNS];

    private boolean whiteToMove = true;

    /**
     * Constructs an empty chess board and resets it to the start position.
     */
    public Board() {
        emptyBoard();
        resetToStartPosition();
    }

    /**
     * Constructs a new board by copying the state of another board.
     *
     * @param board The board object to copy.
     */
    public Board(Board board) {
        this.board = board.getBoard();
        this.whiteToMove = board.isWhiteToMove();
    }

    /**
     * Returns the number of rows on the chess board.
     *
     * @return The number of rows.
     */
    public static int getROWS() {
        return ROWS;
    }

    /**
     * Returns the number of columns on the chess board.
     *
     * @return The number of columns.
     */
    public static int getCOLUMNS() {
        return COLUMNS;
    }

    /**
     * Returns a clone of the internal board array.
     *
     * @return A clone of the board array.
     */
    public Piece[] getBoard() {
        return board.clone();
    }

    /**
     * Clears the chess board by setting all squares to null.
     */
    public void emptyBoard() {
        for (int i = 0; i < ROWS * COLUMNS; i++) {
            board[i] = null;
        }
    }

    /**
     * Returns the piece at the specified square.
     *
     * @param square The square index.
     *
     * @return The piece at the specified square, or null if there is no piece.
     */
    public Piece getPieceAt(int square) {
        return board[square];
    }

    /**
     * Sets the internal board array with the specified board.
     *
     * @param board The board array to set.
     */
    public void setBoard(final Piece[] board) {
        this.board = board;
    }

    /**
     * Returns the last move made on the board.
     *
     * @return The last move made, or null if no move has been made yet.
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Sets the last move made on the board.
     *
     * @param lastMove The last move made.
     */
    public void setLastMove(final Move lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * Moves a piece from the specified source square to the specified target square.
     *
     * @param fromSquare The source square.
     * @param toSquare   The target square.
     * @param isRealMove Indicates if the move is a real move or just a temporary move for checking legality.
     */
    public void movePiece(final int fromSquare, final int toSquare, boolean isRealMove) {
        Piece piece = getPieceAt(fromSquare);
        if (piece instanceof King && Math.abs(fromSquare - toSquare) == 2) {
            castle(fromSquare, toSquare);
        }
        if (piece instanceof Pawn) {
            if (Math.abs(fromSquare - toSquare) % 8 != 0 && getPieceAt(toSquare) == null) {
                enPassant(fromSquare, toSquare);
            } else if (piece.getColor() == PieceColor.BLACK && getRowAtSquare(toSquare) == 7) {
                promotionType = PieceType.QUEEN;
                upForPromotion = piece;
                promotionMenuStartSquare = toSquare;
                promote(piece, promotionType);
            } else if (piece.getColor() == PieceColor.WHITE && getRowAtSquare(toSquare) == 0) {
                promotionType = PieceType.QUEEN;
                upForPromotion = piece;
                promotionMenuStartSquare = toSquare;
                promote(piece, promotionType);
            }
        }

        piece.setMoveStatus();
        board[fromSquare] = null;
        piece.setPos(toSquare);
        board[toSquare] = piece;
        whiteToMove = !whiteToMove;
    }

    /**
     * Promotes a pawn to the specified piece type.
     *
     * @param pieceToPromote The pawn piece to promote.
     * @param pieceType      The type of piece to promote to.
     */
    public void promote(Piece pieceToPromote, PieceType pieceType) {
        int pos = pieceToPromote.getPos();
        PieceColor color = pieceToPromote.getColor();
        Piece piece;
        switch (pieceType) {
            case QUEEN -> piece = new Queen(pos, color);
            case KNIGHT -> piece = new Knight(pos, color);
            case ROOK -> piece = new Rook(pos, color);
            case BISHOP -> piece = new Bishop(pos, color);
            default -> piece = null;
        }
        board[pieceToPromote.getPos()] = piece;
    }

    /**
     * Performs en passant capture for a pawn.
     *
     * @param fromSquare The source square of the pawn.
     * @param toSquare   The target square of the pawn.
     */
    private void enPassant(final int fromSquare, final int toSquare) {
        if (Math.abs(fromSquare - toSquare) == 7) {
            board[fromSquare + 1] = null;
        } else {
            board[fromSquare - 1] = null;
        }
    }

    /**
     * Performs castling move for a king and rook.
     *
     * @param fromSquare The source square of the king.
     * @param toSquare   The target square of the king.
     */
    private void castle(final int fromSquare, final int toSquare) {
        if (toSquare == 6 || toSquare == 62) {
            Piece rook = getPieceAt(toSquare + 1);
            rook.setMoveStatus();
            rook.setPos(toSquare - 1);
            board[toSquare + 1] = null;
            board[toSquare - 1] = rook;
        } else {
            Piece rook = getPieceAt(toSquare - 2);
            rook.setMoveStatus();
            rook.setPos(toSquare + 1);
            board[toSquare - 2] = null;
            board[toSquare + 1] = rook;
        }
    }

    /**
     * Resets the board to the start position.
     */
    public void resetToStartPosition() {
        List<Piece> pieces = FENConverter.loadBoardFromFEN(START_FEN);
        Piece[] newBoard = new Piece[ROWS * COLUMNS];
        for (Piece piece : pieces) {
            newBoard[piece.getPos()] = piece;
        }
        this.board = newBoard;
        whiteToMove = true;
    }
    /**
     * Returns the row index of the given square.
     *
     * @param square The square index.
     * @return The row index of the square.
     */
    public static int getRowAtSquare(int square){
        return square / ROWS;
    }
    /**
     * Returns the column index of the given square.
     *
     * @param square The square index.
     * @return The column index of the square.
     */
    public static int getColumnAtSquare(int square){
        return square % COLUMNS;
    }

    /**
     * Checks if it is currently white's turn to move.
     *
     * @return True if it is white's turn, false if it is black's turn.
     */
    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    /**
     * Finds all possible moves for the given color on the board.
     *
     * @param color The color of the pieces.
     * @return A list of all possible moves for the specified color.
     */
    public List<Integer> findAllPossibleMoves(PieceColor color){
        boolean savedState = isWhiteToMove();
        if(color == PieceColor.WHITE) whiteToMove = true;
        else whiteToMove = false;
        List<Integer> allPossibleMoves = new ArrayList<>();
        for (int i = 0; i < board.length; i++){
            if (getPieceAt(i) == null) continue;
            allPossibleMoves.addAll(getPieceAt(i).getPossibleMoves(this));
        }
        whiteToMove = savedState;
        return allPossibleMoves;
    }

    /**
     * Returns the position of the king of the specified color.
     *
     * @param color The color of the king.
     * @return The position of the king.
     */
    public int getKingPos(PieceColor color){
        for (int square = 0; square < board.length; square++){
            Piece piece = getPieceAt(square);
            if (piece == null) continue;
            else if (piece instanceof King && piece.getColor() == color) return square;
        }
        return 0;
    }

    /**
     * Checks if the current position is a checkmate.
     *
     * @return True if it is checkmate, false otherwise.
     */
    public boolean isMate(){
        PieceColor colorToMove;
        if (whiteToMove) colorToMove = PieceColor.WHITE;
        else colorToMove = PieceColor.BLACK;

        for (int i = 0; i < board.length; i++) {
            Piece current = getPieceAt(i);
            if (current == null) continue;
            // If we find a move for any piece, it is not mate
            if (current.getColor() == colorToMove && !current.findAllMoves(this).isEmpty()) return false;
        }
        // No moves found, could be mate or stalemate
        return true;
    }
}

