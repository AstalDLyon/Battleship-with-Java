package battleship;

import java.util.Scanner;

public class Main {

    /**
     * Ponto de entrada da aplicação: cria os tabuleiros dos dois jogadores,
     * conduz a etapa de posicionamento e executa o loop de turnos da batalha.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Board player1Board = new Board();
        Board player2Board = new Board();

        System.out.println("Player 1, place your ships on the game field\n");
        setupPlayerFleet(scanner, player1Board);
        promptEnterKey(scanner);

        System.out.println("Player 2, place your ships to the game field\n");
        setupPlayerFleet(scanner, player2Board);
        promptEnterKey(scanner);

        boolean isPlayer1Turn = true;

        while (true) {
            Board currentBoard = isPlayer1Turn ? player1Board : player2Board;
            Board opponentBoard = isPlayer1Turn ? player2Board : player1Board;
            String currentPlayer = isPlayer1Turn ? "Player 1" : "Player 2";

            opponentBoard.printFogOfWar();
            System.out.println("---------------------");
            currentBoard.printRevealed();

            System.out.printf("%n%s, it's your turn:%n%n", currentPlayer);

            boolean gameWon = executeTurnShot(scanner, opponentBoard);
            if (gameWon) {
                break;
            }

            promptEnterKey(scanner);
            isPlayer1Turn = !isPlayer1Turn;
        }
    }

    /**
     * Conduz o jogador pelo posicionamento sequencial de todos os navios do Enum.
     */
    private static void setupPlayerFleet(Scanner scanner, Board board) {
        board.printRevealed();
        ShipType[] ships = ShipType.values();
        int index = 0;

        while (index < ships.length) {
            ShipType currentShip = ships[index];
            System.out.printf("%nEnter the coordinates of the %s (%d cells):%n%n",
                    currentShip.getName(), currentShip.getLength());
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            if (board.placeShip(input, currentShip)) {
                System.out.println();
                board.printRevealed();
                index++;
            }
        }
    }

    /**
     * Lê o disparo do jogador atual, valida via CoordinateValidator e repassa ao tabuleiro inimigo.
     */
    private static boolean executeTurnShot(Scanner scanner, Board opponentBoard) {
        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                return true;
            }

            String shotInput = scanner.nextLine().trim();
            if (shotInput.isEmpty()) {
                continue;
            }

            if (!CoordinateValidator.isValid(shotInput)) {
                System.out.printf("%nError! You entered the wrong coordinates! Try again:%n%n");
                continue;
            }

            int row = CoordinateValidator.parseRow(shotInput);
            int col = CoordinateValidator.parseCol(shotInput);

            System.out.println();
            return opponentBoard.receiveShot(row, col);
        }
    }

    /**
     * Pausa o jogo entre turnos aguardando o pressionamento da tecla Enter.
     */
    private static void promptEnterKey(Scanner scanner) {
        System.out.println("\nPress Enter and pass the move to another player");
        System.out.print("...");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
