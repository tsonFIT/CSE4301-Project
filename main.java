
public class main {
	public static void main(String[] args) {
		String[][] board;
		board = new String[8][8];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if((i == 3 && j == 3) || (i == 4 && j == 4))
					board[i][j] = "W";
				else if((i == 3 && j == 4) || (i == 4 && j == 3))
					board[i][j] = "B";
				else
					board[i][j] = "-";
			}
		}
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}

}
