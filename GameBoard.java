import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    int tilesPerRow = 8; // nur gerade Zahlen!
    int panelSize;
    int tilesSize;
    Color othelloDarkGreen = new Color(0, 64, 0);

    JButton[][] buttons = new JButton[tilesPerRow][tilesPerRow];

    public void createPanel(JFrame frame) {
        frame.add(this, BorderLayout.CENTER);
    }

    public void updateGameBoard(JFrame frame) {
        panelSize = Math.min(frame.getWidth(), frame.getHeight());
        int startX = (frame.getWidth() - panelSize) / 2;
        int startY = (frame.getHeight() - panelSize) / 2;
        this.setBounds(startX, startY, panelSize, panelSize);
        tilesSize = panelSize / tilesPerRow;
    }

    public JPanel getCurrentJPanel() {
        return this;
    }

    public void createButtons() {
        for (int i = 0; i < tilesPerRow; i++) {
            for (int j = 0; j < tilesPerRow; j++) {
                buttons[i][j] = new JButton();
                this.add(buttons[i][j]);
                buttons[i][j].setBackground(othelloDarkGreen);
            }
        }
        int x = tilesPerRow / 2;
        int j = x - 1;

        Main.blackTilesPositionList.add(new int[] {j,x});
        Main.blackTilesPositionList.add(new int[] {x,j});
        Main.whiteTilesPositionList.add(new int[] {x,x});
        Main.whiteTilesPositionList.add(new int[] {j,j});
    }

    public void updateButtons() {
        for (int i = 0; i < tilesPerRow; i++) {
            for (int j = 0; j < tilesPerRow; j++) {
                buttons[i][j].setBounds(i * tilesSize, j * tilesSize, tilesSize, tilesSize);
            }
        }
    }

    public JButton getButton(int x, int y) {
        return buttons[x][y];
    }

    public JButton[][] getButtonsArray() {
        return buttons;
    }

    public int getTilesPerRow() {
        return tilesPerRow;
    }
}