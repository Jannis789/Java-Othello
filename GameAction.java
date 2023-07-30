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
                    List < int[] > directionVectors = getDirectionVectors(selectedTile, player);
                    List < int[][] > d_intArrayList = combinePositionAndDirections(selectedTile, directionVectors);
                    // read2DArray(d_intArrayList);
                    checkNextTiles(d_intArrayList, player);
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

    private static List<int[]> getDirectionVectors(int[] currentPosition, int player) {
        // Die Liste der Richtungsvektoren für die Überprüfung der benachbarten Kästchen.
        int[][] vectorsList = { {-1, -1}, {-1, 1}, {1, 1}, {1, -1}, {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
    
        List<int[]> directionsList = new ArrayList<>(); // Richtungsvektoren zu den anliegenden Positionen
    
        List<int[]> enemyTilesPositionList;
    
        if (player == 0) {
            enemyTilesPositionList = Main.whiteTilesPositionList;
        } else {
            enemyTilesPositionList = Main.blackTilesPositionList;
        }
    
        int boardSize = gameBoard.getTilesPerRow(); // Die Anzahl der Kästchen pro Zeile auf dem Spielbrett
    
        // Durchlaufe alle Richtungsvektoren, um die benachbarten Kästchen zu überprüfen.
        for (int[] vector : vectorsList) {
            int newX = currentPosition[0] + vector[0];
            int newY = currentPosition[1] + vector[1];
    
            // Überprüfe, ob die Position innerhalb der Grenzen des Spielbretts liegt
            if (newX >= 0 && newX < boardSize && newY >= 0 && newY < boardSize) {
                // Durchlaufe die Positionenliste des Gegners, um zu prüfen, ob das Kästchen in der Richtung besetzt ist.
                for (int[] enemyTilePos : enemyTilesPositionList) {
                    if (newX == enemyTilePos[0] && newY == enemyTilePos[1]) {
                        directionsList.add(new int[]{vector[0], vector[1]});
                        break;
                    }
                }
            }
        }
    
        return directionsList;
    }

	private static List < int[][] > combinePositionAndDirections(int[] positionArray, List < int[] > directionArray) {
		List < int[][] > combinedList = new ArrayList<>();

		for (int[] vector: directionArray) {
			int[][] pd = {
				{
					positionArray[0], positionArray[1]
				},
				{
					vector[0], vector[1]
				}
			};
			combinedList.add(pd);
		}

		return combinedList;
	}

	public static boolean isEnemyTile(int[] enemyTile, int player) {
		// player = 0 (Schwarz) 
		// player = 1 (Weiß)
		int x = enemyTile[0];
		int y = enemyTile[1];
		List < int[] > playersTilesPositionList = new ArrayList<>(); // Eigene eingefärbte Felder
		List < int[] > enemysTilesPositionList = new ArrayList<>(); // Gegnerische eingefärbte Felder
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
		List < int[] > playersTilesPositionList = new ArrayList<>(); // Eigene eingefärbte Felder
		List < int[] > enemysTilesPositionList = new ArrayList<>(); // Gegnerische eingefärbte Felder
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
    private static void checkNextTiles(List<int[][]> combinedList, int player) {
        // Man guckt wie oft man den Vektor addieren muss, bis isPlayerTile() = true erreicht ist
        List<int[]> validDirection = new ArrayList<>();
        List<int[]> changedPositions = new ArrayList<>(); // Declare and initialize the changedPositions list
        int[] position = new int[2];
        boolean reachedBlackTile = false;
        changedPositions.clear();
        for (int i = 0; i < combinedList.size(); i++) {
            int[][] entry = combinedList.get(i);
            position = entry[0];
            int[] direction = entry[1];
            int newX;
            int newY;
            int counter = 0;
            boolean stopLoop = false;
            int[] tempPlayerPosition = new int[2];

            while (!stopLoop) {
                counter++;
                newX = position[0] + (counter * direction[0]);
                newY = position[1] + (counter * direction[1]);
                int[] newPositionArray = new int[] { newX, newY };
    
                if (gameBoard.getTilesPerRow() + 1 == newX || gameBoard.getTilesPerRow() + 1 == newY) {
                    stopLoop = true;
                }
    
                if (isPlayerTile(newPositionArray, player)) {
                    tempPlayerPosition[0] = newX;
                    tempPlayerPosition[1] = newY;
                    validDirection.add(new int[] { direction[0], direction[1] });
                    stopLoop = true;
                    reachedBlackTile = true;
                }
    
                if (isTileBlank(newPositionArray)) {
                    stopLoop = true;
                }
            }
    
    
            if (!validDirection.isEmpty()) { // Füge diese Bedingung hinzu, um zu überprüfen, ob gültige Richtungsvektoren vorhanden sind
                for (int j = 1; j < counter; j++) {
                    for (int[] dir : validDirection) {
                        newX = position[0] + (j * dir[0]);
                        newY = position[1] + (j * dir[1]);
                        changedPositions.add(new int[] { newX, newY });
                        
                        if (tempPlayerPosition[0] == newX && tempPlayerPosition[1] == newY) {
                            break; 
                        }
                    }
                }
            }

        }
        changedPositions.add(position);
        if (reachedBlackTile) {
            if (player == 0) {
                for (int[] pos : changedPositions) {
                    // Entferne die Position aus beiden Listen
                    deleteElementFromArrayList(Main.whiteTilesPositionList, pos);
                    System.out.println("!Position wird Schwarz umgefärbt --> x:" + pos[0] + "|y:" + pos[1]);
                    // Füge die Position der Schwarzen Liste hinzu
                    Main.blackTilesPositionList.add(pos);
                }
                Main.nextTurn();
            } else if (player == 1) {
                for (int[] pos : changedPositions) {
                    // Entferne die Position aus beiden Listen
                    deleteElementFromArrayList(Main.blackTilesPositionList, pos);
                    System.out.println("!Position wird Weiß umgefärbt --> x:" + pos[0] + "|y:" + pos[1]);
                    // Füge die Position der Weißen Liste hinzu
                    Main.whiteTilesPositionList.add(pos);
                }
            Main.nextTurn();
        }
        
    }
}
    private static void deleteElementFromArrayList(List<int[]> combinedList, int[] searchedElement) {
        for (int i = combinedList.size() - 1; i >= 0; i--) {
            int[] element = combinedList.get(i);
            if (Arrays.equals(element, searchedElement)) {
                combinedList.remove(i);
            }
        }
    }
    
}