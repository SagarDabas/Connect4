package com.inoxapps.connect_4.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.inoxapps.connect_4.Assets;
import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.logic.BoardManager;
import com.inoxapps.connect_4.logic.Difficulty;
import com.inoxapps.connect_4.logic.GameState;
import com.inoxapps.connect_4.utils.GenericSpriteAccessor;
import com.inoxapps.connect_4.utils.OverlapTester;
import com.inoxapps.connect_4.utils.Screen;

public class MenuScreen extends Screen {

	private Rectangle onePlayerBounds;
	private Rectangle twoPlayerBounds;
	private Rectangle exitBounds;
	private Rectangle easyBounds;
	private Rectangle normalBounds;
	private Rectangle hardBounds;
	private Rectangle extremeBounds;
	
	private TweenManager tweenManager;
	private boolean translateFlag;
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private Connect4 game;

	public MenuScreen(Connect4 game, SpriteBatch batcher) {
		super(game);
		BoardManager.clearScore();
		this.game = game;
		this.batcher = batcher;
		this.touchPoint = new Vector3();
		this.tweenManager = new TweenManager();
		hardBounds = Assets.hardButton.getBoundingRectangle();
		normalBounds = Assets.normalButton.getBoundingRectangle();
		easyBounds = Assets.easyButton.getBoundingRectangle();
		extremeBounds = Assets.extremeButton.getBoundingRectangle();
		onePlayerBounds = Assets.onePlayerButton.getBoundingRectangle();
		twoPlayerBounds = Assets.twoPlayerButton.getBoundingRectangle();
		exitBounds = Assets.exitButton.getBoundingRectangle();
//		Assets.menuMusic.play();
//		Assets.menuMusic.setLooping(true);
		Connect4.gameState = GameState.MAIN_MENU;
	}

	public void update(float delta) {
		tweenManager.update(delta);

		switch (Connect4.gameState) {
		case MAIN_MENU:
			updateRunning();
			break;
		case MENU_POP_UP:
			updatePopUp();
			break;
		default:
			break;
		}
	}

	private void updateRunning() {
		if (Gdx.input.justTouched()) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(onePlayerBounds, touchPoint.x,
					touchPoint.y)) {
				Assets.buttonSound.play();
				translate(Assets.easyButton);
				translate(Assets.normalButton);
				translate(Assets.hardButton);
				translate(Assets.extremeButton);
				Connect4.gameState = GameState.MENU_POP_UP;
				return;
			}
			if (OverlapTester.pointInRectangle(twoPlayerBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				Connect4.difficulty = Difficulty.HUMAN;
				Connect4.gameState = GameState.RUNNING;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(exitBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				if(Connect4.actionResolver!=null)
					Connect4.actionResolver.gameFinish();
				return;
			}
		}
	}

	private void updatePopUp() {
		if (Gdx.input.justTouched() && !translateFlag) {
			Configurations.GUI_CAM.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(easyBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				Connect4.difficulty = Difficulty.EASY;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(normalBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				Connect4.difficulty = Difficulty.MEDIUM;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
			if (OverlapTester.pointInRectangle(hardBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				Connect4.difficulty = Difficulty.HARD;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
			
			if (OverlapTester.pointInRectangle(extremeBounds, touchPoint.x,
					touchPoint.y)) {
//				Assets.menuMusic.stop();
				Assets.buttonSound.play();
				Connect4.difficulty = Difficulty.HARDEST;
				game.setScreen(new GameScreen(game, batcher));
				return;
			}
		}
	}

	public void present(float delta) {

		// what are these statements
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Configurations.GUI_CAM.update();
		batcher.setProjectionMatrix(Configurations.GUI_CAM.combined);
		batcher.enableBlending();
		batcher.begin();
		switch (Connect4.gameState) {
		case MAIN_MENU:
			drawRunning();
			break;
		case MENU_POP_UP:
			drawPopUp();
			break;
		default:
			break;
		}
//		Assets.addSprite.draw(batcher);
		batcher.end();
	}

	private void drawRunning() {
		Assets.background.draw(batcher);
		Assets.onePlayerButton.draw(batcher);
		Assets.twoPlayerButton.draw(batcher);
		Assets.exitButton.draw(batcher);
	}

	private void drawPopUp() {
		Assets.background.draw(batcher, 0.4f);
		Assets.easyButton.draw(batcher);
		Assets.normalButton.draw(batcher);
		Assets.hardButton.draw(batcher);
		Assets.extremeButton.draw(batcher);
	}

	private void translate(Sprite sprite) {
		translateFlag = true;

		sprite.setPosition(-Configurations.CAM_WIDTH, 0);

		Tween.to(sprite, GenericSpriteAccessor.POS_XY, 1).target(0, 0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						translateFlag = false;
					}
				}).ease(Elastic.OUT).start(tweenManager);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void backKeyPressed() {
		if (Connect4.gameState == GameState.MENU_POP_UP)
			Connect4.gameState = GameState.MAIN_MENU;
		else
		{
			if(Connect4.actionResolver!=null)
				Connect4.actionResolver.gameFinish();
		}
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

}
