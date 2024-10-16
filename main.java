import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static String[][] copyBoard(String[][] board) {
        String[][] newBoard = new String[board.length][board[0].length];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j]; // Copy each element
            }
        }
        
        return newBoard;
    }
    public static List<int[]> getAllLegalMoves(String[][] board, String symbol) {
        List<int[]> legalMoves = new ArrayList<>();

        // Iterate over every cell on the board
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                // If it's a valid move for the current player, add it to the list
                if (validMove(board, x, y, symbol)) {
                    legalMoves.add(new int[]{y, x});
                }
            }
        }

        return legalMoves;
    }
    public static boolean isGameOver(String[][] board) {
        boolean black = hasValidMove(board, "B");
        boolean white = hasValidMove(board, "W");
        return !(black || white);
    }
    public static int evaluateBoard(String[][] board, String symbol) {
        int score = 0;
        String oppSymbol = symbol.equals("W") ? "B" : "W";

        // Count the number of pieces for both players
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(symbol)) {
                    score += 1;
                } else if (board[i][j].equals(oppSymbol)) {
                    score -= 1;
                }
            }
        }

        return score;
    }
    public static int minimax(String[][] board, int depth, int alpha, int beta, boolean maximizingPlayer, String currentPlayer) {
    // Base case: if we've reached the maximum depth or the game is over
    if (depth == 0 || isGameOver(board)) {
        return evaluateBoard(board, maximizingPlayer ? "B" : "W");
    }

    // Determine opponent symbol
    String opponent = currentPlayer.equals("B") ? "W" : "B";

    // Maximizing player's move (AI)
    if (maximizingPlayer) {
        int maxEval = Integer.MIN_VALUE;
        List<int[]> legalMoves = getAllLegalMoves(board, currentPlayer);

        for (int[] move : legalMoves) {
            // Copy board and make the move
            String[][] newBoard = copyBoard(board);
            makeMove(newBoard, move[1], move[0], currentPlayer);

            // Call minimax recursively, now for the minimizing player (opponent)
            int eval = minimax(newBoard, depth - 1, alpha, beta, false, opponent);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            // Alpha-Beta pruning: if beta is less than or equal to alpha, cut off further exploration
            if (beta <= alpha) {
                break; // Beta cut-off
            }
        }
        return maxEval;

    // Minimizing player's move (opponent)
    } else {
        int minEval = Integer.MAX_VALUE;
        List<int[]> legalMoves = getAllLegalMoves(board, currentPlayer);

        for (int[] move : legalMoves) {
            // Copy board and make the move
            String[][] newBoard = copyBoard(board);
            makeMove(newBoard, move[1], move[0], currentPlayer);

            // Call minimax recursively, now for the maximizing player (AI)
            int eval = minimax(newBoard, depth - 1, alpha, beta, true, opponent);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            // Alpha-Beta pruning: if beta is less than or equal to alpha, cut off further exploration
            if (beta <= alpha) {
                break; // Alpha cut-off
            }
        }
        return minEval;
    }
}

    
    public static int[] findBestMove(String[][] board, String symbol) {
    int bestValue = Integer.MIN_VALUE;  // Initialize to the worst possible value for maximizing player
    int[] bestMove = null;
    
    List<int[]> legalMoves = getAllLegalMoves(board, symbol); // Get all possible moves for AI

    // Iterate through each move
    for (int[] move : legalMoves) {
        String[][] newBoard = copyBoard(board);  // Copy the current board state
        makeMove(newBoard, move[1], move[0], symbol);  // Make the move on the copied board

        // Call minimax with Alpha-Beta pruning. Depth of 5 for difficulty adjustment
        int boardValue = minimax(newBoard, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false, symbol);

        // Update best move if this move has a better value
        if (boardValue > bestValue) {
            bestValue = boardValue;
            bestMove = move;  // Update the best move
        }
    }

    return bestMove; // Return the best move for the AI
}

public static boolean validMove(String[][] board, int x, int y, String symbol) {
    String oppSymbol = symbol.equals("W") ? "B" : "W";
    
    // Ensure the selected cell itself is empty
    if (!board[y][x].equals("-")) {
        return false;
    }

    // Define directions to check for enclosing opponent pieces
    int[][] directions = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Vertical and horizontal
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
    };

    boolean isValid = false;

    // Check in each direction
    for (int[] direction : directions) {
        int dx = direction[0], dy = direction[1];
        int currentX = x + dx, currentY = y + dy;
        boolean hasOpponent = false;

        // Move in the current direction while we have opponent's pieces
        while (currentX >= 0 && currentX < board[0].length &&
               currentY >= 0 && currentY < board.length &&
               board[currentY][currentX].equals(oppSymbol)) {
            hasOpponent = true;
            currentX += dx;
            currentY += dy;
        }

        // If there are opponent pieces and it ends with the player's piece, it's a valid move
        if (hasOpponent &&
            currentX >= 0 && currentX < board[0].length &&
            currentY >= 0 && currentY < board.length &&
            board[currentY][currentX].equals(symbol)) {
            isValid = true;
            break; // We can stop as soon as we find one valid direction
        }
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
    System.out.println("What pieces do you want to play with?\nBlack (B) or White (W)? ");
    String answer = input.nextLine();
    if (answer.equals("W")) {
        while (gameRunning) {
            printBoard(board);

            if (hasValidMove(board, currentPlayer)) {
                if (currentPlayer.equals("B")) {
                    System.out.println("AI (B) is making a move...");
                    int[] aiMove = findBestMove(board, "B");
                    makeMove(board, aiMove[1], aiMove[0], "B");
                } else {
                    System.out.println("Player (W)'s turn.");
                    System.out.print("Enter Y coordinate: ");
                    int y = input.nextInt();
                    System.out.print("Enter X coordinate: ");
                    int x = input.nextInt();

                    if (validMove(board, x, y, currentPlayer)) {
                        makeMove(board, x, y, currentPlayer);
                    } else {
                        System.out.println("Invalid move. Try again.");
                        continue;
                    }
                }

                // Switch players
                currentPlayer = currentPlayer.equals("B") ? "W" : "B";
            } else {
                System.out.println(currentPlayer + " has no valid moves and must pass.");
                currentPlayer = currentPlayer.equals("B") ? "W" : "B";

                if (!hasValidMove(board, currentPlayer)) {
                    System.out.println("No valid moves for both players. Game over.");
                    gameRunning = false;
                }
            }
        }
    }
    else if (answer.equals("B")) {
        while (gameRunning) {
            printBoard(board);
            if (hasValidMove(board, currentPlayer)) {
                if (currentPlayer.equals("B")) {
                    System.out.println("Player (B)'s turn.");
                    System.out.print("Enter Y coordinate: ");
                    int y = input.nextInt();
                    System.out.print("Enter X coordinate: ");
                    int x = input.nextInt();

                    if (validMove(board, x, y, currentPlayer)) {
                        makeMove(board, x, y, currentPlayer);
                    } else {
                        System.out.println("Invalid move. Try again.");
                        continue;
                    }
                } else {
                    System.out.println("AI (W) is making a move...");
                    int[] aiMove = findBestMove(board, "W");
                    makeMove(board, aiMove[1], aiMove[0], "W");
                }

                // Switch players
                currentPlayer = currentPlayer.equals("B") ? "W" : "B";
            } else {
                System.out.println(currentPlayer + " has no valid moves and must pass.");
                currentPlayer = currentPlayer.equals("B") ? "W" : "B";

                if (!hasValidMove(board, currentPlayer)) {
                    System.out.println("No valid moves for both players. Game over.");
                    gameRunning = false;
                }
            }
        }
    }
    else {
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
