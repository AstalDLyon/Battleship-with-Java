package battleship;

public class CoordinateValidator {

    /**
     * Verifica se uma string representa uma coordenada válida do tabuleiro
     * (letra de A a J seguida de um número inteiro de 1 a 10).
     */
    public static boolean isValid(String coord) {
        if (coord == null || coord.length() < 2 || coord.length() > 3) {
            return false;
        }
        char row = coord.charAt(0);
        if (row < 'A' || row > 'J') {
            return false;
        }
        try {
            int col = Integer.parseInt(coord.substring(1));
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Converte a letra da linha (A-J) para o índice numérico correspondente (0-9).
     */
    public static int parseRow(String coord) {
        return coord.charAt(0) - 'A';
    }

    /**
     * Converte o número da coluna (1-10) para o índice numérico correspondente (0-9).
     */
    public static int parseCol(String coord) {
        return Integer.parseInt(coord.substring(1)) - 1;
    }
}
