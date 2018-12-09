package com.inoxapps.connect_4.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.inoxapps.connect_4.ActionResolver.PopupCallback;
import com.inoxapps.connect_4.Assets;
import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.gameObjects.Player;
import com.inoxapps.connect_4.logic.Board;
import com.inoxapps.connect_4.logic.BoardManager;
import com.inoxapps.connect_4.logic.Difficulty;
import com.inoxapps.connect_4.logic.GameState;
import com.inoxapps.connect_4.utils.GenericSpriteAccessor;
import com.inoxapps.connect_4.utils.OverlapTester;
import com.inoxapps.connect_4.utils.Screen;

public class GameScreen extends Screen {

	private Connect4 game;
	private SpriteBatch batcher;
	private BoardManager boardManager;
	private Vector3 touchPoint;
	private Rectangle pauseBounds;
	private Rectangle soundBounds;
	private Rectangle resumeBounds;
	private Rectangle menuBounds;
	private Rectangle restartBounds;
	private Rectangle scoreBox;
	private Rectangle scoreBox1;
	private TweenManager tweenManager;
	private Rectangle aiNameBounds;
	private Rectangle _2NameBounds;
	private Rectangle _1NameBounds;
	private boolean animateFlag;
	private float time;
	private float delay = 1.5f;

	public GameScreen(Connect4 game, SpriteBatch batcher) {
		super(game);
		this.game = game;
		this.batcher = batcher;
		this.touchPoint = new Vector3();
		this.tweenManager = new TweenManager();
		this.boardManager = new BoardManager(this);
		aiNameBounds = Assets.scoreAIName.getBoundingRectangle();
		_2NameBounds = Assets.score2Name.getBoundingRectangle();
		_1NameBounds = Assets.score1Name.getBoundingRectangle();
		pauseBounds = adjustSize(Assets.pauseButton.getBoundingRectangle());
		soundBounds = adjustSize(Assets.soundOnButton.getBoundingRectangle());
		resumeBounds = Assets.resumeButton.getBoundingRectangle();
		menuBounds = Assets.menuButton.getBoundingRectangle();
		restartBounds = Assets.restartButton.getBoundingRectangle();
		scoreBox = Assets.scoreBox.getBoundingRectangle();
		scoreBox1 = Assets.scoreBox1.getBoundingRectangle();
//		Assets.gamePlayMusic.play();
//		Assets.gamePlayMusic.setLooping(true);
		Connect4.gameState = GameState.RUNNING;
	}

	private Rectangle adjustSize(Rectangle bounds) {
		bounds.width += 100;
		bounds.height += 100;
		bounds.x -= 50;
		bounds.y -= 50;
		// very bad approach, never modify the input and send it as output.
		// instead create a temp, copy and modify then send it.
		return bounds;
	}

	public void update(float delta) {

		tweenManager.update(delta);

		switch (Connect4.gameState) {
		case READY:
			updateReady();
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		case PAUSED:
			updatePaused();
			break;
		case DRAW:
			updateItsADraw();
			break;
		case OVER:
			// do nothing
			break;
		}
	}

	private void updateItsADraw() {
		if (Gdx.input.justTouched() && !animateFlag) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			// MenuButton' bounds are now resumeButton's bounds.
			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.gamePlayMusic.stop();
//				Assets.buttonSound.play();
				Connect4.gameState = GameState.MAIN_MENU;
				game.setScreen(new MenuScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(restartBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.buttonSound.play();
				Connect4.gameState = GameState.RUNNING;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
		}
	}

	private void updateReady() {
		if (Gdx.input.justTouched()) {
			Connect4.gameState = GameState.RUNNING;
		}
	}

	private void updateRunning(float delta) {
		if (Gdx.input.justTouched()) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.buttonSound.play();
				translate(Assets.menuButton);
				translate(Assets.resumeButton);
				translate(Assets.restartButton);
				Connect4.gameState = GameState.PAUSED;
				return;
			}
			if (OverlapTester.pointInRectangle(soundBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.preferences.putBoolean("sound",
						!Assets.preferences.getBoolean("sound", true));
				Assets.preferences.flush();
				Assets.buttonSound.play();
				Assets.setVolume();
				return;
			}
		}
		// must be called after the above block
		boardManager.update(delta);
	}

	private void updatePaused() {
		if (Gdx.input.justTouched() && !animateFlag) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(menuBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.gamePlayMusic.stop();
//				Assets.buttonSound.play();
				game.setScreen(new MenuScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(restartBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.buttonSound.play();
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.buttonSound.play();
				Connect4.gameState = GameState.RUNNING;
				return;
			}
		}
	}

	// private void updateGameOver(float delta) {
	// time += delta;
	// if (time >= delay)
	// Assets.bgAnimated.rotate(-1.2f);
	// // if (Gdx.input.justTouched() && !animateFlag) {
	// // Connect4.gameState = GameState.RUNNING;
	// // game.setScreen(new GameScreen(game, batcher));
	// // return;
	// // }
	// }

	@Override
	public void present(float delta) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(0, 0, 0, 1);
		Configurations.GUI_CAM.update();
		batcher.setProjectionMatrix(Configurations.GUI_CAM.combined);
		batcher.enableBlending();
		batcher.begin();

		switch (Connect4.gameState) {
		case READY:
			drawReady();
			break;
		case RUNNING:
			drawRunning(delta);
			break;
		case PAUSED:
			drawPaused();
			break;
		case DRAW:
			drawItsADraw();
			break;
		case OVER:
			drawGameOver(delta);
			break;
		}
		// Assets.addSprite.draw(batcher);
		batcher.end();
	}

	private void drawItsADraw() {
		Assets.background.draw(batcher, .4f);
		batcher.draw(Assets.itsADraw, menuBounds.x - 15, menuBounds.y
				+ ((menuBounds.y - resumeBounds.y) / 3));
		batcher.draw(Assets.menuButton, menuBounds.x, menuBounds.y
				- (menuBounds.y - resumeBounds.y));
		Assets.restartButton.draw(batcher);
	}

	private void drawReady() {
	}

	private void drawRunning(float delta) {
		Assets.background.draw(batcher);
		boardManager.render(batcher, delta);
		Assets.soundOnButton.draw(batcher);
		if (!Assets.preferences.getBoolean("sound"))
			Assets.soundOffButton.draw(batcher);
		Assets.pauseButton.draw(batcher);
		Assets.scoreBox.draw(batcher);
		Assets.scoreBox1.draw(batcher);
		Assets.fontScore.draw(batcher, BoardManager.p1Score, scoreBox.x
				+ scoreBox.width / 6f, scoreBox.y + scoreBox.height / 1.5f);
		Assets.fontScore.draw(batcher, BoardManager.p2Score, scoreBox1.x
				+ scoreBox1.width / 6f, scoreBox1.y + scoreBox1.height / 1.5f);

		Assets.score1Name.draw(batcher);
		batcher.draw(Assets.disk1Region, _1NameBounds.x - 25,
				_1NameBounds.y + 5, 20, 20);
		if (Connect4.difficulty != Difficulty.HUMAN) {
			Assets.scoreAIName.draw(batcher);
			batcher.draw(Assets.disk2Region, aiNameBounds.x
					+ aiNameBounds.width + 5, aiNameBounds.y + 5, 20, 20);
		} else {
			Assets.score2Name.draw(batcher);
			batcher.draw(Assets.disk2Region, _2NameBounds.x
					+ _2NameBounds.width + 5, _2NameBounds.y + 5, 20, 20);
		}

	}

	private void drawPaused() {
		Assets.background.draw(batcher, .4f);
		Assets.menuButton.draw(batcher);
		Assets.restartButton.draw(batcher);
		Assets.resumeButton.draw(batcher);
	}

	private void drawGameOver(float delta) {
		time += delta;
		if (time < delay) {
			Assets.background.draw(batcher);
			drawWinCombi(delta);
		} else if (time >= delay)
			drawWinner();
	}

	private void drawWinCombi(float delta) {
		drawRunning(delta);
		for (int i = 0; i < Configurations.NUM_TO_CHECK; i++) {
			if (Player.PlayerOne.isWinner()) {
				batcher.draw(Assets.blueWin,
						Board.winningCombinations[i].column
								* Configurations.TILE_WIDTH
								+ Configurations.BOARD_LEFT_MARGIN - 5,
						Board.winningCombinations[i].row
								* Configurations.TILE_LENGTH
								+ Configurations.BOARD_BOTTOM_MARGIN - 5);
			} else if (Player.PlayerTwo.isWinner()) {
				batcher.draw(Assets.yellowWin,
						Board.winningCombinations[i].column
								* Configurations.TILE_WIDTH
								+ Configurations.BOARD_LEFT_MARGIN - 5,
						Board.winningCombinations[i].row
								* Configurations.TILE_LENGTH
								+ Configurations.BOARD_BOTTOM_MARGIN - 5);
			}
		}
	}

	private void drawWinner() {
		Assets.bgAnimated.draw(batcher);
		Assets.trophy.draw(batcher);
		if (Player.PlayerOne.isWinner()) {
			Assets.winnerOne.draw(batcher);
		} else if (Player.PlayerTwo.isWinner()) {
			if (Connect4.difficulty == Difficulty.HUMAN) {
				Assets.winnerTwo.draw(batcher);
			} else {
				Assets.winnerAI.draw(batcher);
			}
		}
	}

	private void translate(Sprite sprite) {
		animateFlag = true;
		sprite.setPosition(-Configurations.CAM_WIDTH, 0);
		Tween.to(sprite, GenericSpriteAccessor.POS_XY, 1).target(0, 0)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						animateFlag = false;
					}
				}).ease(Elastic.OUT).start(tweenManager);
	}

	public void animateWinnerSprites() {
		doRotate(Assets.bgAnimated);
		animateWinnerSprite(Assets.bgAnimated);
		animateWinnerSprite(Assets.trophy);
		if (Player.PlayerOne.isWinner()) {
			Assets.winSound.play();
			animateWinnerSprite(Assets.winnerOne);
		} else if (Player.PlayerTwo.isWinner()) {
			if (Connect4.difficulty == Difficulty.HUMAN) {
				Assets.winSound.play();
				animateWinnerSprite(Assets.winnerTwo);
			} else {
				Assets.looseSound.play();
				animateWinnerSprite(Assets.winnerAI);
			}
		}

		if (Connect4.actionResolver != null)
			Connect4.actionResolver.callApplovinAdd();
	}

	private void animateWinnerSprite(Sprite sprite) {
		animateFlag = true;
		doOpacity(sprite);
		doScale(sprite);
	}

	private void doOpacity(Sprite sprite) {
		Color color = sprite.getColor();
		sprite.setColor(color.r, color.g, color.b, 0);
		Tween.to(sprite, GenericSpriteAccessor.OPACITY, 0.2f).target(1)
				.ease(Linear.INOUT).delay(delay).start(tweenManager);
	}

	private void doRotate(Sprite sprite) {
		sprite.setRotation(0);
		Tween.to(sprite, GenericSpriteAccessor.ROTATION, 5).target(360)
				.ease(Linear.INOUT).delay(delay)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						animateFlag = false;
						Connect4.gameState = GameState.RUNNING;
						game.setScreen(new GameScreen(game, batcher));
					}
				}).start(tweenManager);
	}

	private void doScale(Sprite sprite) {
		sprite.setScale(0, 0);
		Tween.to(sprite, GenericSpriteAccessor.SCALE_XY, 0.2f)
				.target(1.2f, 1.2f).ease(Linear.INOUT).delay(delay)
				.start(tweenManager);
	}

	@Override
	public void backKeyPressed() {
		if (Connect4.gameState == GameState.PAUSED)
			Connect4.gameState = GameState.RUNNING;
		else if (Connect4.gameState == GameState.RUNNING) {
			Connect4.actionResolver.showPopup("Exit Game",
					"Are you sure you want to exit ?", "YES", "NO", true,
					new PopupCallback() {
						@Override
						public void onClicked(boolean isYesClicked) {
							if (isYesClicked) {
//								Assets.gamePlayMusic.stop();
								game.setScreen(new MenuScreen(game, batcher));
							}
						}
					});
		}
	}
	

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
