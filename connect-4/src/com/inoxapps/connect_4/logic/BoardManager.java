package com.inoxapps.connect_4.logic;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.inoxapps.connect_4.Assets;
import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.gameObjects.Player;
import com.inoxapps.connect_4.screens.GameScreen;
import com.inoxapps.connect_4.utils.GenericSpriteAccessor;
import com.inoxapps.connect_4.utils.OverlapTester;

public class BoardManager {

	public static String p1Score = "00";
	public static String p2Score = "00";
	// private String playerOneKey;
	// private String playerTwoKey;
	private boolean translateFlag = false;
	private byte[][] board;
	private ArrayList<FilledPosition> filledPositions;
	private Player player = Player.PlayerOne;
	private TweenManager tweenManager;
	private Vector3 touchPoint;
	private GameScreen gameScreen;
	private float translateTargetY;
	private Rectangle boardBounds;
	private boolean firstMove = true;

	public BoardManager(GameScreen gameScreen) {
		Assets.diskSprite.setPosition(1000, 1000);
		this.touchPoint = new Vector3();
		this.gameScreen = gameScreen;
		this.filledPositions = new ArrayList<FilledPosition>();
		// this.playerOneKey = Player.PlayerOne.name() + "_"
		// + Connect4.difficulty.name();
		// this.playerTwoKey = Player.PlayerTwo.name() + "_"
		// + Connect4.difficulty.name();
		this.tweenManager = new TweenManager();
		// this.p1Score = (Assets.preferences.getInteger(playerOneKey) < 10 ?
		// "0" : "") + Assets.preferences.getInteger(playerOneKey);
		// this.p2Score = (Assets.preferences.getInteger(playerTwoKey) < 10 ?
		// "0" : "") + Assets.preferences.getInteger(playerTwoKey);
		this.board = new byte[Configurations.BOARD_ROWS][Configurations.BOARD_COLUMNS];
		this.boardBounds = Assets.boardBGOne.getBoundingRectangle();
	}

	public void update(float deltaTime) {

		tweenManager.update(deltaTime);

		if (!translateFlag) {
			if (player == Player.PlayerOne) {
				humanMove();
			} else if (player == Player.PlayerTwo) {
				if (Connect4.difficulty == Difficulty.HUMAN)
					humanMove();
				else
					aiMove();
			}
		}
	}

	private void aiMove() {

		if (firstMove) {
			firstMove = false;
			updateBoard((byte) MathUtils.random(2, 4));
		} else {
			if (Connect4.difficulty == Difficulty.HARDEST)
				updateBoard(Board2.guessMove(board, Player.PlayerTwo));
			else
				updateBoard(Board.getBestMove(board));
		}
	}

	private void humanMove() {
		if (Gdx.input.justTouched()) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(boardBounds, touchPoint.x,
					touchPoint.y)) {
				if (Connect4.difficulty == Difficulty.HARDEST)
					Board2.guessMove(board, Player.PlayerOne);
				updateBoard((int) ((touchPoint.x - Configurations.BOARD_LEFT_MARGIN) / Configurations.TILE_WIDTH));
			}
		}
	}

	private void updateBoard(int column) {
		float y = 0;
		float x = (column) * Configurations.TILE_WIDTH
				+ Configurations.BOARD_LEFT_MARGIN;
		for (int row = 0; row < Configurations.BOARD_ROWS; row++) {
			if (board[row][column] == 0) {
				board[row][column] = player.getID();
				translate(x, y + Configurations.BOARD_BOTTOM_MARGIN, row,
						column);
				return;
			}
			y += Configurations.TILE_LENGTH;
		}
	}

	// translating it x,y position and checking for the winner.
	private void translate(final float x, final float y, final int row,
			final int column) {
		count = 1;
		translateFlag = true;
		translateTargetY = y + 15;
		Assets.diskSprite.setPosition(Configurations.TILE_WIDTH * column
				+ Configurations.BOARD_LEFT_MARGIN, Configurations.TILE_LENGTH
				* Configurations.BOARD_ROWS
				+ Configurations.BOARD_BOTTOM_MARGIN);

		float sec = 1 - (y - Configurations.BOARD_BOTTOM_MARGIN)
				/ (Configurations.TILE_LENGTH * Configurations.BOARD_ROWS);

		Tween.to(Assets.diskSprite, GenericSpriteAccessor.POS_XY, sec)
				.target(x, y).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						filledPositions.add(new FilledPosition(x, y, player));
						Board.updateWinCombi = true;
						if (Board.checkForWin(row, column, board, player,
								Configurations.NUM_TO_CHECK, false)) {
							declareWinner();
						} else if (isDraw()) {
							Connect4.gameState = GameState.DRAW;
						}
						Board.updateWinCombi = false;
						player = switchPlayers(player);
						translateFlag = false;
					}
				}).ease(TweenEquations.easeOutBounce).start(tweenManager);
	}

	private boolean isDraw() {
		boolean flag = true;
		for (int i = 0; i < Configurations.BOARD_COLUMNS; i++) {
			if (board[Configurations.BOARD_ROWS - 1][i] == 0)
				return false;
		}
		return flag;
	}

	public void render(SpriteBatch batcher, float delta) {
		Assets.boardBGTwo.draw(batcher);
		setBallSprite(player.getID());
		playHitSound();
		if (Connect4.gameState == GameState.RUNNING)
			Assets.diskSprite.draw(batcher);
		drawPlacedSprites(batcher);
		Assets.boardBGOne.draw(batcher);

	}

	private void drawPlacedSprites(SpriteBatch batcher) {
		for (FilledPosition position : filledPositions) {
			setBallSprite(position.player.getID());
			batcher.draw(Assets.diskSprite, position.x, position.y);
		}
	}

	private int count = 1;

	private void playHitSound() {
		if (translateFlag) {
			if (count <= 2 && Assets.diskSprite.getY() <= translateTargetY
					&& !Assets.ballDropSound.isPlaying()) {
				Assets.ballDropSound.play();
				count++;
			}
		}
	}

	private void setBallSprite(int playerID) {
		if (playerID == Player.PlayerOne.getID()) {
			Assets.diskSprite.setRegion(Player.PlayerOne.getPlayerSprite());
		} else if (playerID == Player.PlayerTwo.getID()) {
			Assets.diskSprite.setRegion(Player.PlayerTwo.getPlayerSprite());
		}
	}

	private void declareWinner() {
		player.setWinner(true);
		switchPlayers(player).setWinner(false);
		saveScore();
		Connect4.gameState = GameState.OVER;
		gameScreen.animateWinnerSprites();
	}

	private void saveScore() {
		int score1 = Integer.parseInt(p1Score);
		int score2 = Integer.parseInt(p2Score);
		;
		if (player == Player.PlayerOne)
			score1++;
		// Assets.preferences.putInteger(playerOneKey,
		// Assets.preferences.getInteger(playerOneKey) + 1);
		else
			score2++;
		// Assets.preferences.putInteger(playerTwoKey,
		// Assets.preferences.getInteger(playerTwoKey) + 1);

		// Assets.preferences.flush();

		// int playerOneScore = Assets.preferences.getInteger(playerOneKey);
		// int playerTwoScore = Assets.preferences.getInteger(playerTwoKey);
		// p1Score = (playerOneScore < 10 ? "0" : "") + playerOneScore;
		// p2Score = (playerTwoScore < 10 ? "0" : "") + playerTwoScore;
		p1Score = (score1 < 10 ? "0" : "") + score1;
		p2Score = (score2 < 10 ? "0" : "") + score2;
	}

	private Player switchPlayers(Player player) {
		if (player == Player.PlayerOne) {
			return Player.PlayerTwo;
		} else {
			return Player.PlayerOne;
		}
	}

	public static void clearScore() {
		BoardManager.p1Score = BoardManager.p2Score = "00";
	}

}
