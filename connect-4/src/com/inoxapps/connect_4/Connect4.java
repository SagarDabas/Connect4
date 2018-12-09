package com.inoxapps.connect_4;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.inoxapps.connect_4.logic.Difficulty;
import com.inoxapps.connect_4.logic.GameState;
import com.inoxapps.connect_4.screens.LoadingScreen;
import com.inoxapps.connect_4.utils.Game;
import com.inoxapps.connect_4.utils.GenericSpriteAccessor;
import com.inoxapps.connect_4.utils.Screen;

public class Connect4 extends Game {

	public static GameState gameState = GameState.MAIN_MENU;
	public static Difficulty difficulty;
	public static ActionResolver actionResolver;

	public Connect4(ActionResolver actionResolver) {
		Connect4.actionResolver = actionResolver;
	}

	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Configurations.GUI_CAM.position.set(Configurations.CAM_WIDTH / 2,
				Configurations.CAM_HEIGHT / 2, 0);
		Tween.registerAccessor(Sprite.class,
				new GenericSpriteAccessor<Sprite>());
		super.create();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			getScreen().backKeyPressed();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this, new SpriteBatch());
	}
}
