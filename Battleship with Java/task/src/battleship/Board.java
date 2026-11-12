package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private static final int SIZE = 10;
    private final char[][] grid;
    private final List<List<int[]>> placedShips;

    /**
     * Inicializa a matriz 10x10 preenchida com '~' e a lista de navios posicionados.
     */
    public Board() {
        this.grid = new char[SIZE][SIZE];
        for (char[] row : grid) {
            Arrays.fill(row, '~');
        }
        this.placedShips = new ArrayList<>();
    }

    /**
     * Valida as coordenadas, verifica proximidade/sobreposição e posiciona o navio no tabuleiro.
     */
    public boolean placeShip(String input, ShipType ship) {
        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            System.out.println("Error! Invalid input format. Try again:");
            return false;
        }

        String start = parts[0];
        String end = parts[1];

        if (!CoordinateValidator.isValid(start) || !CoordinateValidator.isValid(end)) {
            System.out.println("Error! Invalid coordinate format. Try again:");
            return false;
        }

        int startRow = CoordinateValidator.parseRow(start);
        int startCol = CoordinateValidator.parseCol(start);
        int endRow = CoordinateValidator.parseRow(end);
        int endCol = CoordinateValidator.parseCol(end);

        boolean sameRow = (startRow == endRow);
        boolean sameCol = (startCol == endCol);

        if (!sameRow && !sameCol) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        int actualLength = sameRow ? (maxCol - minCol + 1) : (maxRow - minRow + 1);
        if (actualLength != ship.getLength()) {
            System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.getName());
            return false;
        }

        for (int r = minRow; r <= maxRow; r++) {
            for (int c = minCol; c <= maxCol; c++) {
                if (isTooCloseOrOverlapping(r, c)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        }

        List<int[]> shipCoordinates = new ArrayList<>();
        for (int r = minRow; r <= maxRow; r++) {
            for (int c = minCol; c <= maxCol; c++) {
                grid[r][c] = 'O';
                shipCoordinates.add(new int[]{r, c});
            }
        }
        placedShips.add(shipCoordinates);
        return true;
    }

    /**
     * Inspeciona a vizinhança 3x3 ao redor de uma célula para impedir navios adjacentes.
     */
    private boolean isTooCloseOrOverlapping(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < SIZE && j >= 0 && j < SIZE) {
                    if (grid[i][j] == 'O') {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Processa um disparo na coordenada indicada, atualiza a matriz e imprime o resultado.
     * Retorna true se todos os navios deste tabuleiro foram afundados.
     */
    public boolean receiveShot(int row, int col) {
        if (grid[row][col] == 'O' || grid[row][col] == 'X') {
            boolean wasAlreadyHit = (grid[row][col] == 'X');
            grid[row][col] = 'X';

            if (allShipsSunk()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                return true;
            } else if (!wasAlreadyHit && isShipSunkAt(row, col)) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You hit a ship!");
            }
        } else {
            grid[row][col] = 'M';
            System.out.println("You missed!");
        }
        return false;
    }

    /**
     * Verifica se o navio atingido na posição dada teve todas as suas células destruídas.
     */
    private boolean isShipSunkAt(int row, int col) {
        for (List<int[]> shipCoords : placedShips) {
            boolean belongsToThisShip = false;
            for (int[] coord : shipCoords) {
                if (coord[0] == row && coord[1] == col) {
                    belongsToThisShip = true;
                    break;
                }
            }
            if (belongsToThisShip) {
                for (int[] coord : shipCoords) {
                    if (grid[coord[0]][coord[1]] != 'X') {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se ainda resta algum caractere 'O' (navio intacto) no tabuleiro.
     */
    private boolean allShipsSunk() {
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Imprime o tabuleiro completo revelando os navios intactos ('O').
     */
    public void printRevealed() {
        printBoard(false);
    }

    /**
     * Imprime o tabuleiro ocultando os navios intactos sob a névoa ('~').
     */
    public void printFogOfWar() {
        printBoard(true);
    }

    /**
     * Lógica compartilhada de renderização do tabuleiro no console.
     */
    private void printBoard(boolean hideShips) {
        System.out.print("  ");
        for (int j = 0; j < SIZE; j++) {
            System.out.printf("%d ", j + 1);
        }
        System.out.println();

        char letter = 'A';
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("%c ", letter + i);
            for (int j = 0; j < SIZE; j++) {
                char cell = grid[i][j];
                if (hideShips && cell == 'O') {
                    System.out.print("~ ");
                } else {
                    System.out.printf("%c ", cell);
                }
            }
            System.out.println();
        }
    }
}
