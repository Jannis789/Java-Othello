import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/*
    1. Der Zug muss auf einem leeren Feld erfolgen. [o]
    2. Der Stein muss horizontal, vertikal oder diagonal an eine Reihe von eigenen Steinen anschließen. [o]
    3. Zwischen dem platzierten Stein und einem benachbarten eigenen Stein muss sich mindestens ein gegnerischer Stein befinden. [o]
    3. Durch das Setzen des Steins werden alle gegnerischen Steine, die sich zwischen dem neu platzierten Stein und einem anderen eigenen Stein befinden, umgedreht. [o]
    4. Der Zug muss das Spielbrett in einen gültigen Zustand versetzen, in dem alle Regeln eingehalten werden. [o]

    1. Koordinaten: (0,0) Vektor:<-1,-1> Richtung: Diagonal
    2. Koordinaten: (0,2) Vektor:<-1,1> Richtung: Diagonal
    3. Kordinaten: (2,2) Vektor:<1,1> Richtung: Diagonal
    4. Koordinaten: (2,0) Vektor:<1,-1> Richtung: Diagonal
    ___
    5. Koordinaten: (1,0) Vektor:<0,-1> Richtung: Horizontal
    6. Koordinaten: (1,2) Vektor:<0,1> Richtung: Horizontal
    ___
    7. Koordinaten: (0,1) Vektor:<-1,0> Richtung: Vertikal
    8. Koordinaten: (2,1) Vektor:<1,0> Richtung: Vertikal

Die Vektoren<-1,0> und < 1,0> zeigen in vertikale Richtung (oben
 */

public class GameAction {

    private static GameBoard gameBoard;

    private static JButton clickedButton;

    public static void gameAction() {
        gameBoard = Main.getGameBoard();
        ActionListener actionListener = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                clickedButton = (JButton) e.getSource();
                int[] selectedTile = getPositions();
                int player;
                if (Main.currentTurn % 2 != 0) {
                    player = 0;
                } else {
                    player = 1;
                }
                if (isTileBlank(selectedTile)) {
                    addNewPositions(selectedTile, player);
                }
            }
        };

        for (int i = 0; i < gameBoard.getTilesPerRow(); i++) {
            for (int j = 0; j < gameBoard.getTilesPerRow(); j++) {
                gameBoard.getButton(i, j).addActionListener(actionListener);
            }
        }

    }

    public static int[] getPositions() {
        int[] positions = new int[2];

        for (int i = 0; i < gameBoard.getTilesPerRow(); i++) {
            for (int j = 0; j < gameBoard.getTilesPerRow(); j++) {
                if (gameBoard.getButton(i, j) == clickedButton) {
                    positions[0] = i;
                    positions[1] = j;
                    // System.out.println("Button wurde gedrückt --> x:" + i + "|y:" + j);
                    return positions;
                }
            }
        }

        return positions;
    }

    public static void colorize(List < int[] > positionList, Color color) {
        for (int[] tilePos: positionList) {
            // System.out.println("Kästschen wurde Eingefärbt --> x:" + tilePos[0] + "|y:" + tilePos[1]);
            gameBoard.getButton(tilePos[0], tilePos[1]).setBackground(color);;
        }
    }

    public static boolean isTileBlank(int[] currentPosition) {
        // player = 0 (Schwarz) 
        // player = 1 (Weiß)
        int x = currentPosition[0];
        int y = currentPosition[1];

        // Prüft, ob das aktuelle Kästchen leer ist (weder Schwarz noch Weiß).
        boolean blank = true;
        // System.out.println("Position erhalten --> x:" + x + "|y:" + y);

        for (int[] tilePos: Main.blackTilesPositionList) {
            if (tilePos[0] == x && tilePos[1] == y) {
                blank = false;
                break;
            }
        }

        if (blank) {
            for (int[] tilePos: Main.whiteTilesPositionList) {
                if (tilePos[0] == x && tilePos[1] == y) {
                    blank = false;
                    break;
                }
            }
        }
        return blank;
    }

    private static void addNewPositions(int[] currentPosition, int player) {
        int[][] vectorsList = { {-1, -1}, {-1, 1}, {1, 1}, {1, -1}, {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        List < int[] > validDirectionsList = new ArrayList < > ();
        List < int[] > validPositionList = new ArrayList < > ();
        List < int[] > cValidDirectionsList = new ArrayList < > ();
        List < int[] > cValidPositionList = new ArrayList < > ();
        boolean isValidTurn = false;
        for (int[] direction: vectorsList) {
            int count = 1;
            int newX, newY;
            boolean activeLoop = true;

            while (activeLoop) {
                newX = currentPosition[0] + (count * direction[0]);
                newY = currentPosition[1] + (count * direction[1]);
                int[] newPos = new int[] {
                    newX,
                    newY
                };

                if (isEnemyTile(newPos, player) || isPlayerTile(newPos, player)) {
                    count++;
                    if (isPlayerTile(newPos, player)) {
                        validDirectionsList.add(direction);
                        cValidDirectionsList.add(direction);
                        validPositionList.add(newPos);
                        cValidPositionList.add(newPos);

                    }
                } else {
                    activeLoop = false;
                }
            }
        }
        for (int[] vPos: validPositionList) {
            for (int[] vDir: validDirectionsList) {

                if (currentPosition[0] + vDir[0] == vPos[0] && currentPosition[1] + vDir[1] == vPos[1]) { // gucke ob ein Feld ein gegnerisches Feld zwischen vPos und currentPosition liegt
                    int[] tempPos = new int[2];
                    tempPos[0] = currentPosition[0] + vDir[0];
                    tempPos[1] = currentPosition[1] + vDir[1];
                    int[] tempDir = new int[2];
                    tempDir[0] = vDir[0];
                    tempDir[1] = vDir[1];

                    deleteElementFromArrayList(cValidPositionList, tempPos);
                    deleteElementFromArrayList(cValidDirectionsList, tempDir);
                } else {

                }
            }
        }
        List < Integer > validSteps = new ArrayList < > ();
        for (int[] vPos: cValidPositionList) {
            int[] directionVector = new int[2];
            directionVector[0] = vPos[0] - currentPosition[0];
            directionVector[1] = vPos[1] - currentPosition[1];

            int[] tempPos = new int[2];
            tempPos[0] = currentPosition[0];
            tempPos[1] = currentPosition[1];

            int steps = 0;

            while (!Arrays.equals(tempPos, vPos)) {
                tempPos[0] += Integer.signum(directionVector[0]);
                tempPos[1] += Integer.signum(directionVector[1]);
                steps++;

            }
            validSteps.add(steps);
        }
        validPositionList.clear();

        if (!validSteps.isEmpty() && !cValidPositionList.isEmpty() && !cValidDirectionsList.isEmpty() && validPositionList.isEmpty()) {
            validPositionList.add(currentPosition);
            isValidTurn = true;
            for (int i = 0; i < cValidDirectionsList.size(); i++) {
                int[] directionVector = cValidDirectionsList.get(i);
                int steps = validSteps.get(i);

                for (int step = 1; step <= steps - 1; step++) {
                    int newX = currentPosition[0] + (step * directionVector[0]);
                    int newY = currentPosition[1] + (step * directionVector[1]);

                    // Hinzufügen der Position zu validPositionList
                    validPositionList.add(new int[] {
                        newX,
                        newY
                    });
                }
            }
        }

        if (isValidTurn) {
            if (player == 0) {
                for (int[] pos: validPositionList) {
                    deleteElementFromArrayList(Main.whiteTilesPositionList, pos);
                    System.out.println("!Position wird Schwarz umgefärbt --> x:" + pos[0] + "|y:" + pos[1]);
                    Main.blackTilesPositionList.add(pos);
                }
            } else if (player == 1) {
                for (int[] pos: validPositionList) {
                    deleteElementFromArrayList(Main.blackTilesPositionList, pos);
                    System.out.println("!Position wird Weiß umgefärbt --> x:" + pos[0] + "|y:" + pos[1]);
                    Main.whiteTilesPositionList.add(pos);
                }
            }
            Main.nextTurn();
        }

    }

    public static boolean isEnemyTile(int[] enemyTile, int player) {
        // player = 0 (Schwarz) 
        // player = 1 (Weiß)
        int x = enemyTile[0];
        int y = enemyTile[1];
        List < int[] > playersTilesPositionList = new ArrayList < > (); // Eigene eingefärbte Felder
        List < int[] > enemysTilesPositionList = new ArrayList < > (); // Gegnerische eingefärbte Felder
        if (player == 0) {
            enemysTilesPositionList.addAll(Main.whiteTilesPositionList);
            playersTilesPositionList.addAll(Main.blackTilesPositionList);
        } else if (player == 1) {
            enemysTilesPositionList.addAll(Main.blackTilesPositionList);
            playersTilesPositionList.addAll(Main.whiteTilesPositionList);
        }

        for (int[] tile: enemysTilesPositionList) {
            if (tile[0] == x && tile[1] == y) {
                return true;
            }

        }

        return false;
    }

    private static boolean isPlayerTile(int[] enemyTile, int player) {
        // player = 0 (Schwarz) 
        // player = 1 (Weiß)
        int x = enemyTile[0];
        int y = enemyTile[1];
        List < int[] > playersTilesPositionList = new ArrayList < > (); // Eigene eingefärbte Felder
        List < int[] > enemysTilesPositionList = new ArrayList < > (); // Gegnerische eingefärbte Felder
        if (player == 0) {
            enemysTilesPositionList.addAll(Main.whiteTilesPositionList);
            playersTilesPositionList.addAll(Main.blackTilesPositionList);
        } else if (player == 1) {
            enemysTilesPositionList.addAll(Main.blackTilesPositionList);
            playersTilesPositionList.addAll(Main.whiteTilesPositionList);
        }

        for (int[] tile: playersTilesPositionList) {
            if (tile[0] == x && tile[1] == y) {
                return true;
            }

        }

        return false;
    }

    private static void deleteElementFromArrayList(List < int[] > combinedList, int[] searchedElement) {
        for (int i = combinedList.size() - 1; i >= 0; i--) {
            int[] element = combinedList.get(i);
            if (Arrays.equals(element, searchedElement)) {
                combinedList.remove(i);
            }
        }
    }

}