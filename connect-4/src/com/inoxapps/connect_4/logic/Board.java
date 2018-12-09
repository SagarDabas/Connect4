package com.inoxapps.connect_4.logic;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.gameObjects.Player;

public class Board {

	private static int evaluate(byte row, byte col, Player player) {
		counts.clearCounts();
		int score = 0;
		if (!checkForWin(row, col, board, player, Configurations.NUM_TO_CHECK,
				true)) {
			score += counts.getThreeCount() * Configurations.WEIGHT_3;
			score += counts.getTwoCount() * Configurations.WEIGHT_2;
			score += counts.getOneCount() * Configurations.WEIGHT_1;
		} else {
			score = Configurations.MAX_SCORE;
		}

		if (player == Player.PlayerOne)
			score *= -1;
		return score;
	}

	private static int alpha_beta(Player player, int alpha, int beta, byte row,
			byte col, byte currentDepth) {
		// TODO test if (currentDepth == MenuScreen.difficulty.getDepth()) this
		// statement replace it by >=
		if (currentDepth >= Connect4.difficulty.getDepth()) {
			return evaluate(row, col, switchPlayer(player));
		}
		byte[] allMoves = getMoves();
		int score;

		// if AI's turn
		if (player == Player.PlayerTwo) {
			for (byte move : allMoves) {
				if (move != -1) {
					// update board
					makeMove(move, player);
					row = moveMade.row;
					col = moveMade.column;
					score = alpha_beta(switchPlayer(player), alpha, beta, row,
							col, (byte) (currentDepth + 1));

					score = evaluateEveryNode(player, row, col, currentDepth,
							score);

					undoMove(row, col);

					if (score > alpha) {
						alpha = score;
						alphaBestMove = move;
					}

					// only if difficulty is medium
					if (Connect4.difficulty == Difficulty.MEDIUM
							&& alpha >= beta)
						return alpha;

				}
			}
			return alpha;
		} else if (player == Player.PlayerOne) {
			for (byte move : allMoves) {
				if (move != -1) {
					makeMove(move, player);
					row = moveMade.row;
					col = moveMade.column;
					score = alpha_beta(switchPlayer(player), alpha, beta, row,
							col, (byte) (currentDepth + 1));

					score = evaluateEveryNode(player, row, col, currentDepth,
							score);

					undoMove(row, col);
					if (score < beta) {
						beta = score;
					}
					if (Connect4.difficulty == Difficulty.MEDIUM
							&& alpha >= beta)
						return beta;
				}
			}
			return beta;
		}
		return -1;
	}

	private static int evaluateEveryNode(Player player, byte row, byte col,
			byte currentDepth, int score) {
		if (Connect4.difficulty == Difficulty.HARD
				&& currentDepth + 1 < Connect4.difficulty.getDepth()) {
			int tempScore = evaluate(row, col, player);
			if (tempScore == Math.abs(Configurations.MAX_SCORE))
				score = tempScore;
			else
				score += tempScore;
		}
		return score;
	}

	public static boolean checkForWin(int row, int column, byte board[][],
			Player player, byte num_to_check, boolean isCountFlag) {
		// winningCombinations = new Move[Configurations.NUM_TO_CHECK];
		Board.board = board;
		Player otherPlayer = switchPlayer(player);
		return checkHorizontally(row, column, player, otherPlayer,
				num_to_check, isCountFlag)
				|| checkVertically(row, column, player, otherPlayer,
						num_to_check, isCountFlag)
				|| checkLeftDiagonal(row, column, player, otherPlayer,
						num_to_check, isCountFlag)
				|| checkRightDiagonal(row, column, player, otherPlayer,
						num_to_check, isCountFlag);
	}

	private static boolean checkHorizontally(int row, int column,
			Player player, Player otherPlayer, byte num_to_check,
			boolean isCountFlag) {
		int begin = 0;
		int check = 0;
		if (num_to_check <= column) {
			begin = column - num_to_check + 1;
		}
		int maxColumnRight = column;
		if (column > Configurations.BOARD_COLUMNS - num_to_check) {
			maxColumnRight = Configurations.BOARD_COLUMNS - num_to_check;
		}

		counts.clearTempCounts();
		for (; begin <= maxColumnRight; begin++) {
			check = 0;
			int j = begin;
			for (; j < begin + num_to_check; j++) {
				if (board[row][j] == player.getID()) {
					check++;
					if (updateWinCombi)
						updateWinCombination(row, check, j);
				} else if (!isCountFlag) {
					begin = j++;
					break;
				} else if (isCountFlag) {
					// in case of other player block
					if (board[row][j] == otherPlayer.getID()) {
						counts.clearTempCounts();
						if (j < column) {
							begin = j++;
							break;
						} else if (j > column) {
							begin = maxColumnRight + 1;
							break;
						}
					}
				}
			}

			if (isConnected(num_to_check, check)) {
				return true;
			} else {
				if (isCountFlag && j == begin + num_to_check) {
					counts.incrementsCounts(check);
				}
			}
		}

		counts.updateCounts();
		return isConnected(num_to_check, check);
	}

	private static void updateWinCombination(int row, int check, int column) {
		Move tempMove = new Move();
		tempMove.row = (byte) row;
		tempMove.column = (byte) column;
		winningCombinations[check - 1] = tempMove;
	}

	private static boolean checkVertically(int row, int column, Player player,
			Player otherPlayer, byte num_to_check, boolean isCountFlag) {
		int begin = 0;
		int check = 0;
		if (num_to_check <= row) {
			begin = row - num_to_check + 1;
		}
		int maxRowsUp = row;
		if (row > Configurations.BOARD_ROWS - num_to_check) {
			maxRowsUp = Configurations.BOARD_ROWS - num_to_check;
		}

		counts.clearTempCounts();
		for (; begin <= maxRowsUp; begin++) {
			check = 0;
			int j = begin;
			for (; j < begin + num_to_check; j++) {
				if (board[j][column] == player.getID()) {
					check++;
					if (updateWinCombi)
						updateWinCombination(j, check, column);
				} else if (!isCountFlag) {
					begin = j++;
					break;
				} else if (isCountFlag) {
					// in case of other player block
					if (board[j][column] == otherPlayer.getID()) {
						counts.clearTempCounts();
						if (j < row) {
							begin = j++;
							break;
						} else if (j > row) {
							begin = maxRowsUp + 1;
							break;
						}
					}
				}
			}

			if (isConnected(num_to_check, check)) {
				return true;
			} else {
				if (isCountFlag && j == begin + num_to_check) {
					counts.incrementsCounts(check);
				}
			}
		}

		counts.updateCounts();
		return isConnected(num_to_check, check);
	}

	private static boolean checkLeftDiagonal(int row, int column,
			Player player, Player otherPlayer, byte num_to_check,
			boolean isCountFlag) {

		int beginRow;
		int beginColumn;

		if (column - num_to_check + 1 > 0 && row - num_to_check + 1 > 0) {
			beginRow = row - num_to_check + 1;
			beginColumn = column - num_to_check + 1;
		}

		else {
			// below diagonal
			if (column < row) {
				beginColumn = 0;
				beginRow = row - column;
			}
			// above diagonal
			else if (row < column) {
				beginRow = 0;
				beginColumn = column - row;
			}
			// on diagonal
			else {
				beginColumn = 0;
				beginRow = 0;
			}
		}

		int maxColumnRight = column;
		int maxRowsUp = row;

		if (column > Configurations.BOARD_COLUMNS - num_to_check) {
			maxColumnRight = Configurations.BOARD_COLUMNS - num_to_check;
		}
		if (row > Configurations.BOARD_ROWS - num_to_check) {
			maxRowsUp = Configurations.BOARD_ROWS - num_to_check;
		}

		int check = 0;
		counts.clearTempCounts();
		while (beginColumn <= maxColumnRight && beginRow <= maxRowsUp) {
			check = 0;
			int i = beginRow;
			int j = beginColumn;
			while (i < beginRow + num_to_check
					&& j < beginColumn + num_to_check) {
				if (board[i][j] == player.getID()) {
					check++;
					if (updateWinCombi)
						updateWinCombination(i, check, j);
				} else if (!isCountFlag) {
					beginRow = i++;
					beginColumn = j++;
					break;
				} else if (isCountFlag) {
					// in case of other player block
					if (board[i][j] == otherPlayer.getID()) {
						counts.clearTempCounts();
						if (i < row && j < column) {
							beginColumn = j++;
							beginRow = i++;
							break;
						} else if (i > row && j > column) {
							beginRow = maxRowsUp + 1;
							beginColumn = maxColumnRight + 1;
							break;
						}
					}
				}

				i++;
				j++;
			}

			if (isConnected(num_to_check, check)) {
				return true;
			} else {
				if (isCountFlag && j == beginColumn + num_to_check
						&& i == beginRow + num_to_check) {
					counts.incrementsCounts(check);
				}
			}
			beginColumn++;
			beginRow++;
		}
		counts.updateCounts();
		return isConnected(num_to_check, check);
	}

	private static boolean checkRightDiagonal(int row, int column,
			Player player, Player otherPlayer, byte num_to_check,
			boolean isCountFlag) {

		int beginRow;
		int beginColumn;
		if (column + num_to_check - 1 < Configurations.BOARD_COLUMNS
				&& row - num_to_check + 1 > 0) {
			beginRow = row - num_to_check + 1;
			beginColumn = column + num_to_check - 1;
		}

		else {
			// above diagonal
			if (column + row > Configurations.BOARD_COLUMNS - 1) {
				beginColumn = Configurations.BOARD_COLUMNS - 1;
				beginRow = column + row - beginColumn;
			}
			// below diagonal
			else if (column + row < Configurations.BOARD_COLUMNS - 1) {
				beginRow = 0;
				beginColumn = column + row;
			}
			// on diagonal
			else {
				beginColumn = Configurations.BOARD_COLUMNS - 1;
				beginRow = 0;
			}
		}

		int maxColumnLeft = column;
		int maxRowsUp = row;

		if (column < num_to_check) {
			maxColumnLeft = num_to_check - 1;
		}
		if (row > Configurations.BOARD_ROWS - num_to_check) {
			maxRowsUp = Configurations.BOARD_ROWS - num_to_check;
		}

		counts.clearTempCounts();
		int check = 0;
		while (beginColumn >= maxColumnLeft && beginRow <= maxRowsUp) {
			check = 0;
			int i = beginRow;
			int j = beginColumn;
			while (i < beginRow + num_to_check
					&& j > beginColumn - num_to_check) {
				if (board[i][j] == player.getID()) {
					check++;
					if (updateWinCombi)
						updateWinCombination(i, check, j);
				} else if (!isCountFlag) {
					beginRow = i++;
					beginColumn = j--;
					break;
				} else if (isCountFlag) {
					// in case of other player block
					if (board[i][j] == otherPlayer.getID()) {
						counts.clearTempCounts();
						if (i < row && j > column) {
							beginColumn = j--;
							beginRow = i++;
							break;
						} else if (i > row && j < column) {
							beginRow = maxRowsUp + 1;
							beginColumn = maxColumnLeft - 1;
							break;
						}
					}
				}

				i++;
				j--;
			}

			if (isConnected(num_to_check, check)) {
				return true;
			} else {
				if (isCountFlag && j == beginColumn - num_to_check
						&& i == beginRow + num_to_check) {
					counts.incrementsCounts(check);
				}
			}
			beginColumn--;
			beginRow++;
		}

		counts.updateCounts();
		return isConnected(num_to_check, check);
	}

	private static boolean isConnected(byte num_to_check, int check) {
		if (check == num_to_check) {
			return true;
		} else {
			return false;
		}
	}

	private static Player switchPlayer(Player player) {
		if (player == Player.PlayerOne)
			player = Player.PlayerTwo;
		else
			player = Player.PlayerOne;
		return player;
	}

	public static byte getBestMove(byte[][] board) {
		Board.board = board;
		if (checkNextMove(Player.PlayerTwo)) {
			return moveMade.column;
		} else if (checkNextMove(Player.PlayerOne)) {
			return moveMade.column;
		} else {
			if (Connect4.difficulty == Difficulty.EASY) {
				return getRandomMove();
			} else if (Connect4.difficulty == Difficulty.HARD
					|| Connect4.difficulty == Difficulty.MEDIUM) {
				alpha_beta(Player.PlayerTwo, Integer.MIN_VALUE,
						Integer.MAX_VALUE, (byte) -1, (byte) -1, (byte) 1);
			}
		}
		return alphaBestMove;
	}

	private static byte getRandomMove() {
		ArrayList<Byte> columns = new ArrayList<Byte>();
		for (byte column = 0; column < Configurations.BOARD_COLUMNS; column++) {
			if (board[Configurations.BOARD_ROWS - 1][column] == 0) {
				columns.add(column);
			}
		}
		return columns.get(MathUtils.random(columns.size() - 1));
	}

	private static boolean checkNextMove(Player player) {
		boolean flag = false;
		byte[] allMoves = getMoves();
		for (byte move : allMoves) {
			if (move != -1) {
				makeMove(move, player);
				if (checkForWin(moveMade.row, moveMade.column, board, player,
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

	private static void undoMove(int row, int column) {
		board[row][column] = 0;
	}

	private static byte alphaBestMove = -1;
	private static byte[][] board;
	private static Counts counts = new Counts();

	public static class Move {
		public byte row = -1, column = -1;
	}

	public static Move[] winningCombinations = new Move[Configurations.NUM_TO_CHECK];
	private static Move moveMade = new Move();
	public static boolean updateWinCombi;
}
