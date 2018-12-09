package com.inoxapps.connect_4.logic;

import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.gameObjects.Player;
import com.inoxapps.connect_4.logic.Board.Move;

public class Board2 {

	private static int[][] evaluationBoard = new int[][] {
			{ 3, 4, 5, 7, 5, 4, 3 }, { 4, 6, 8, 10, 8, 6, 4 },
			{ 5, 8, 11, 13, 11, 8, 5 }, { 5, 8, 11, 13, 11, 8, 5 },
			{ 4, 6, 8, 10, 8, 6, 4 }, { 3, 4, 5, 7, 5, 4, 3 } };
	private static final int width = Configurations.BOARD_COLUMNS;
	private static int moveScore[] = new int[width];
	private static byte[][] board;
	private static int invalidScore = 1000000;
	public static Move moveMade = new Move();

	private static void makeMove(byte column, Player player) {

		for (byte row = 0; row < Configurations.BOARD_ROWS; row++) {

			if (board[row][column] == 0) {
				board[row][column] = player.getID();
				moveMade.row = row;
				moveMade.column = column;
				return;
			}
		}
	}

	private static byte[] getMoves() {
		byte[] columns = new byte[Configurations.BOARD_COLUMNS];
		for (byte column = 0; column < Configurations.BOARD_COLUMNS; column++) {
			// negative moves in columns indicate invalid moves
			columns[column] = column;
			if (board[Configurations.BOARD_ROWS - 1][column] != 0) {
				columns[column] = -1;
			}
		}
		return columns;
	}

	private static boolean checkNextMove(Player player) {
		boolean flag = false;
		byte[] allMoves = getMoves();
		for (byte move : allMoves) {
			if (move != -1) {
				makeMove(move, player);
				if (Board.checkForWin(moveMade.row, moveMade.column, board, player,
						Configurations.NUM_TO_CHECK, false)) {
					flag = true;
				}
				undoMove(moveMade.row, moveMade.column);
				if (flag)
					break;
			}
		}
		return flag;
	}
	
	
	public static int guessMove(byte board[][], Player player) {

		Board2.board = board;

		 if (checkNextMove(Player.PlayerTwo)) {
		 return moveMade.column;
		 } else if (checkNextMove(Player.PlayerOne)) {
		 return moveMade.column;
		 } else {
		if (player == Player.PlayerOne) {

			int maxScore = -invalidScore;
			int maxMove = 0;
			int count = 0;
			int row, col;

			byte[] allMoves = getMoves();

			for (byte move : allMoves) {

				if (canMove(move)) {

					count++;
					Board2.makeMove(move, Player.PlayerOne);
					row = moveMade.row;
					col = moveMade.column;

					int score = alpha_Beta(board, Player.PlayerTwo,
							-invalidScore, invalidScore, 0, moveMade.row,
							moveMade.column);

					Board2.moveScore[move] = score;

					if (score >= maxScore) {
						maxScore = score;
						maxMove = move;
					}
					Board2.undoMove(row, col);
				} else if (move == -1) {
					Board2.moveScore[count++] = invalidScore;
				}
			}

			return maxMove;

		}

		else if (player == Player.PlayerTwo) {

			int row, col;

			int minScore = invalidScore;
			int minMove = 0;
			int count = 0;
			byte[] allMoves = getMoves();

			for (byte move : allMoves) {

				if (canMove(move)) {
					count++;
					Board2.makeMove(move, Player.PlayerTwo);
					row = moveMade.row;
					col = moveMade.column;

					int score = alpha_Beta(board, Player.PlayerOne,
							-invalidScore, invalidScore, 0, moveMade.row,
							moveMade.column);
					Board2.moveScore[move] = score;

					if (score < minScore) {
						minScore = score;
						minMove = move;
					}

					Board2.undoMove(row, col);

				} else if (move == -1)
					Board2.moveScore[count++] = invalidScore;
			}

			return minMove;
		} else
			return -1;
		 }

	}

	private static boolean canMove(int column) {
		if (column != -1)
			return true;
		else
			return false;
	}

	private static int score() {

		int score = 0;

		for (int x = 0; x < width; ++x) {
			// int columnscore = (width / 2) - x;
			// if (columnscore < 0)
			// columnscore = -columnscore;
			// columnscore = (width / 2) - columnscore;

			for (int y = 0; y < Configurations.BOARD_ROWS; ++y) {
				// int rowscore = (Configurations.BOARD_ROWS / 2) - y;
				// if (rowscore < 0)
				// rowscore = -rowscore;
				// rowscore = (Configurations.BOARD_ROWS / 2) - rowscore;
				if (board[y][x] == Player.PlayerOne.getID())
					score += evaluationBoard[y][x];
				if (board[y][x] == Player.PlayerTwo.getID())
					score -= evaluationBoard[y][x];
			}
		}

		return score;

	}

	public static int alpha_Beta(byte board[][], Player player, int alpha,
			int beta, int depth, int r, int c) {

		player = switchPlayer(player);

		if (checkForWin(r, c, board, player, (byte) 4) == true) {

			if (player == Player.PlayerOne)
				return 100;
			else if (player == Player.PlayerTwo)
				return -100;
		}

		player = switchPlayer(player);
		
//		if (depth == Connect4.difficulty.getDepth() - 1)
//			player = switchPlayer(player);

		if (depth == Connect4.difficulty.getDepth()-1) {

			return score();

		}

		if (player == Player.PlayerOne) {

			int row, col;
			// maximize score
			for (byte move = 0; move < width; move++)
				if (canMove(move)) {

					Board2.makeMove(move, player);
					row = moveMade.row;
					col = moveMade.column;
					int score = alpha_Beta(board, Player.PlayerTwo, alpha,
							beta, depth + 1, moveMade.row, moveMade.column);

					Board2.undoMove(row, col);
					if (score > alpha)
						alpha = score;
					if (alpha >= beta)
						return alpha;
				}
			return alpha;

		} else if (player == Player.PlayerTwo) {
			// minimize score
			int row, col;
			for (byte move = 0; move < width; move++)
				if (canMove(move)) {

					Board2.makeMove(move, player);

					row = moveMade.row;
					col = moveMade.column;
					int score = alpha_Beta(board, Player.PlayerOne, alpha,
							beta, depth + 1, moveMade.row, moveMade.column);

					Board2.undoMove(row, col);

					if (score < beta)
						beta = score;
					if (alpha >= beta)
						return beta;
				}
			return beta;
		}

		else
			return 0;

	}

	private static Player switchPlayer(Player player) {
		if (player == Player.PlayerTwo)
			player = Player.PlayerOne;
		else if (player == Player.PlayerOne)
			player = Player.PlayerTwo;
		return player;
	}

	private static void undoMove(int row, int col) {
		board[row][col] = 0;
	}

	public static boolean checkForWin(int row, int column, byte board[][],
			Player player, byte num_to_check) {

		Board2.board = board;
		boolean win = false;

		if (checkHorizontally(row, column, player, num_to_check, win) == true)
			return true;
		else if (checkVertically(row, column, player, num_to_check) == true)
			return true;
		else if (checkLeftDiagonal(row, column, player, num_to_check) == true)
			return true;
		else if (checkRightDiagonal(row, column, player, num_to_check) == true)
			return true;

		return false;

	}

	private static boolean checkHorizontally(int row, int column,
			Player player, byte num_to_check, boolean win) {

		int x1, x2;
		x1 = x2 = column;
		// System.out.print(row + "   " + column);
		while (x1 < Configurations.BOARD_COLUMNS
				&& board[row][x1] == player.getID()) {
			++x1;
		}

		while (x2 >= 0 && board[row][x2] == player.getID()) {
			--x2;
		}

		if (x1 - x2 > 4) {
			return true;
		}
		return false;
	}

	private static boolean checkVertically(int row, int column, Player player,

	byte num_to_check) {

		int y1, y2;
		y1 = y2 = row;

		while (y1 < Configurations.BOARD_ROWS
				&& board[y1][column] == player.getID())
			++y1;

		while (y2 >= 0 && board[y2][column] == player.getID())
			--y2;

		if (y1 - y2 > 4) {
			return true;
		}
		return false;

	}

	private static boolean checkLeftDiagonal(int row, int column,
			Player player, byte num_to_check) {

		int x1, x2;
		x1 = x2 = row;
		int y1, y2;
		y1 = y2 = column;

		while (y1 >= 0 && x1 < Configurations.BOARD_ROWS
				&& board[x1][y1] == player.getID()) {
			++x1;
			--y1;
		}

		while (y2 < Configurations.BOARD_COLUMNS && x2 >= 0
				&& board[x2][y2] == player.getID()) {
			--x2;
			++y2;
		}

		if (x1 - x2 > 4) {
			return true;
		}

		return false;
	}

	private static boolean checkRightDiagonal(int row, int column,
			Player player, byte num_to_check) {

		int x1, x2;
		x1 = x2 = row;
		int y1, y2;
		y1 = y2 = column;

		while (y1 >= 0 && x1 >= 0 && board[x1][y1] == player.getID()) {
			--x1;
			--y1;
		}

		while (y2 < Configurations.BOARD_COLUMNS
				&& x2 < Configurations.BOARD_ROWS
				&& board[x2][y2] == player.getID()) {
			++x2;
			++y2;
		}

		if (x2 - x1 > 4) {
			return true;
		}

		return false;
	}

}