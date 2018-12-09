package com.inoxapps.connect_4.utils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.inoxapps.connect_4.Connect4;

public abstract class Game implements ApplicationListener, InputProcessor {

	Screen screen;
	public SpriteBatch batch;

	public void setScreen(Screen helpScreen) {
		screen.pause();
		screen.dispose();
		screen = helpScreen;		
		if(Connect4.actionResolver!=null)
			Connect4.actionResolver.setCurrentScreen(screen);
	}

	public abstract Screen getStartScreen();

	public Screen getScreen() {
		return screen;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		screen = getStartScreen();
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void resume() {
		screen.resume();
	}

	@Override
	public void render() {
		screen.update(Gdx.graphics.getDeltaTime());
		screen.present(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		screen.pause();
	}

	@Override
	public void dispose() {
		screen.dispose();
	}
}
