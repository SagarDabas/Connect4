package com.inoxapps.connect_4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.inoxapps.connect_4.Assets;
import com.inoxapps.connect_4.Configurations;
import com.inoxapps.connect_4.Connect4;
import com.inoxapps.connect_4.utils.Screen;

public class LoadingScreen extends Screen {

	private AssetManager manager;
	private SpriteBatch batcher;
	private Connect4 game;
	private TextureAtlas loadingAtlas;
	private Sprite splashScreeen;
	private int count;
	private Sprite dotOne;

	public LoadingScreen(Connect4 game, SpriteBatch batcher) {
		super(game);
		this.batcher = batcher;
		this.game = game;
		loadingAtlas = new TextureAtlas("data/images/pack/loading");
		splashScreeen = loadingAtlas.createSprite(("splashscreen"));
		dotOne = loadingAtlas.createSprite(("dotfirst"));
		startLoading();
		
	}

	private void startLoading() {
		manager = new AssetManager();
		manager.load("data/images/pack/connect4graphics", TextureAtlas.class);
		manager.load("data/scoreFont.fnt", BitmapFont.class);
		manager.load("data/sounds/button.ogg", Music.class);
		manager.load("data/sounds/balldrop.ogg", Music.class);
		manager.load("data/sounds/win.ogg", Music.class);
		manager.load("data/sounds/loose.ogg", Music.class);
//		manager.load("data/sounds/button_slide.ogg", Music.class);
//		manager.load("data/sounds/gameplayMusic.ogg", Music.class);
//		manager.load("data/sounds/menuMusic.ogg", Music.class);		
	}

	@Override
	public void present(float delta) {
		update(delta);
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Configurations.GUI_CAM.update();
		batcher.setProjectionMatrix(Configurations.GUI_CAM.combined);
		batcher.enableBlending();
		batcher.begin();
		splashScreeen.draw(batcher);
		
		count++;
		if(count/30 == 3){
			count=0;
		}
		for(int i=0 ; i<=count/30 ;i++){
			batcher.draw(dotOne,300 + 20*i,40);
		}
		batcher.end();

	}

	@Override
	public void update(float deltaTime) {
		if (manager.update()) {
			Assets.load(manager);
			game.setScreen(new MenuScreen(game, batcher));
		}
//		else if (delay < 60 && oneFlag) {
//			oneFlag = false;
//			twoFlag = true;
//			threeFlag = false;
//			dotSprite = dotOne;
//		} else if (delay > 60 && delay < 120 && twoFlag) {
//			oneFlag = false;
//			twoFlag = false;
//			threeFlag = true;
//			dotSprite = dotTwo;
//		} else if (delay > 120 && delay < 180 && threeFlag) {
//			oneFlag = true;
//			twoFlag = false;
//			threeFlag = false;
//			dotSprite = dotThree;
//		} else if (delay > 180)
//			delay = 0;
//		delay++;
	}

	@Override
	public void backKeyPressed() {
		// do nothing
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}