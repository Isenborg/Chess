package se.liu.marfr380;

public class MainGame
{
    public static void main(String[] args) {
        Board board = new Board();
        BoardViewer viewer = new BoardViewer(board);
        viewer.show();
    }
}
