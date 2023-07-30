import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static JFrame frame;
    private static GameBoard gameBoard = new GameBoard();
    public static List < int[] > blackTilesPositionList = new ArrayList < > ();
    public static List < int[] > whiteTilesPositionList = new ArrayList < > ();
    public static int currentTurn = 0; // GameAction.colorize muss immer ausgeführt werden wenn currentTurn angehoben wird
    public static void main(String[] args) {
        frame = new JFrame("Othello");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        gameBoard.createPanel(frame);
        gameBoard.createButtons();
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gameBoard.updateGameBoard(frame);
                gameBoard.updateButtons();

            }
        });
        nextTurn();
        
    }
    public static void nextTurn() {
        currentTurn++;
        GameAction.gameAction();
        GameAction.colorize(blackTilesPositionList, Color.BLACK);
        GameAction.colorize(whiteTilesPositionList, Color.WHITE);
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }
}