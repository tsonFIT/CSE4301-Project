import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static boolean validMove(String[][] board, int x, int y, String symbol) {
        String oppSymbol = symbol.equals("W") ? "B" : "W";
        boolean isValid = false;

        // Ensure the selected cell itself is empty
        if (!board[y][x].equals("-")) {
            return false;
        }

        // Check all adjacent cells safely
        if (isOpponentPiece(board, x + 1, y, oppSymbol) || // Right
            isOpponentPiece(board, x - 1, y, oppSymbol) || // Left
            isOpponentPiece(board, x, y + 1, oppSymbol) || // Down
            isOpponentPiece(board, x, y - 1, oppSymbol) || // Up
            isOpponentPiece(board, x + 1, y + 1, oppSymbol) || // Down-right
            isOpponentPiece(board, x + 1, y - 1, oppSymbol) || // Up-right
            isOpponentPiece(board, x - 1, y + 1, oppSymbol) || // Down-left
            isOpponentPiece(board, x - 1, y - 1, oppSymbol)) { // Up-left
            isValid = true;
        }

        return isValid;
    }

    private static boolean isOpponentPiece(String[][] board, int x, int y, String oppSymbol) {
        if (x >= 0 && x < board[0].length && y >= 0 && y < board.length) {
            return board[y][x].equals(oppSymbol);
        }
        return false;
    }

    public static void printBoard(String[][] board) {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void makeMove(String[][] board, int x, int y, String symbol) {
    // Place the player's symbol at the chosen location
    board[y][x] = symbol;

    // Define opponent's symbol
    String oppSymbol = symbol.equals("W") ? "B" : "W";
    
    // Directions: {row, col}
    int[][] directions = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Vertical and horizontal
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
    };

    // Check each direction to flip opponent's pieces
    for (int[] direction : directions) {
        int dx = direction[0], dy = direction[1];
        int currentX = x + dx, currentY = y + dy;
        boolean hasOpponent = false;
        List<int[]> toFlip = new ArrayList<>();

        // Move in the current direction while we have opponent's pieces
        while (currentX >= 0 && currentX < board[0].length &&
               currentY >= 0 && currentY < board.length &&
               board[currentY][currentX].equals(oppSymbol)) {
            hasOpponent = true;
            toFlip.add(new int[]{currentY, currentX});
            currentX += dx;
            currentY += dy;
        }

        // If there are opponent pieces and it ends with the player's symbol, flip them
        if (hasOpponent &&
            currentX >= 0 && currentX < board[0].length &&
            currentY >= 0 && currentY < board.length &&
            board[currentY][currentX].equals(symbol)) {
            for (int[] pos : toFlip) {
                board[pos[0]][pos[1]] = symbol;
            }
        }
    }
}


    public static boolean hasValidMove(String[][] board, String symbol) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (validMove(board, x, y, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
    String[][] board = new String[8][8];
    Scanner input = new Scanner(System.in);

    // Initialize the board with starting positions
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
            if ((i == 3 && j == 3) || (i == 4 && j == 4))
                board[i][j] = "W";
            else if ((i == 3 && j == 4) || (i == 4 && j == 3))
                board[i][j] = "B";
            else
                board[i][j] = "-";
        }
    }

    String currentPlayer = "B";
    boolean gameRunning = true;

    while (gameRunning) {
        printBoard(board);
        if (hasValidMove(board, currentPlayer)) {
            System.out.println(currentPlayer + "'s turn.");
            System.out.print("Enter Y coordinate: ");
            int y = input.nextInt();
            System.out.print("Enter X coordinate: ");
            int x = input.nextInt();

            // Make a move if it's valid
            if (validMove(board, x, y, currentPlayer)) {
                makeMove(board, x, y, currentPlayer);
                printBoard(board);

                // Switch players
                currentPlayer = currentPlayer.equals("B") ? "W" : "B";
            } else {
                System.out.println("Invalid move. Try again.");
            }
        } else {
            System.out.println(currentPlayer + " has no valid moves and must pass.");
            currentPlayer = currentPlayer.equals("B") ? "W" : "B";

            // Check if both players have no moves
            if (!hasValidMove(board, currentPlayer)) {
                System.out.println("No valid moves for both players. Game over.");
                gameRunning = false;
            }
        }
    }

    // Determine the winner
    int blackCount = 0, whiteCount = 0;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (board[i][j].equals("B")) blackCount++;
            if (board[i][j].equals("W")) whiteCount++;
        }
    }

    System.out.println("Game over. B: " + blackCount + ", W: " + whiteCount);
    if (blackCount > whiteCount) {
        System.out.println("Black wins!");
    } else if (whiteCount > blackCount) {
        System.out.println("White wins!");
    } else {
        System.out.println("It's a tie!");
    }
}

}
