package com.inoxapps.connect_4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Preferences preferences;
	public static BitmapFont fontScore;

	public static TextureRegion disk1Region;
	public static TextureRegion disk2Region;

	public static Sprite onePlayerButton;
	public static Sprite twoPlayerButton;
	public static Sprite easyButton;
	public static Sprite normalButton;
	public static Sprite hardButton;
	public static Sprite extremeButton;
	public static Sprite exitButton;
	public static Sprite background;
	public static Sprite boardBGOne;
	public static Sprite boardBGTwo;
	public static Sprite scoreBox;
	public static Sprite scoreBox1;
	public static Sprite score1Name;
	public static Sprite score2Name;
	public static Sprite scoreAIName;
	public static Sprite menuButton;
	public static Sprite restartButton;
	public static Sprite resumeButton;
	public static Sprite pauseButton;
	public static Sprite soundOnButton;
	public static Sprite soundOffButton;
	public static Sprite winnerOne;
	public static Sprite winnerTwo;
	public static Sprite winnerAI;
	public static Sprite diskSprite;
	public static Sprite addSprite;
	public static Sprite bgAnimated;
	public static Sprite trophy;
	public static Sprite blueWin;
	public static Sprite yellowWin;
	public static Sprite itsADraw;

	public static Music ballDropSound;
//	public static Music gamePlayMusic;
//	public static Music menuMusic;
	public static Music winSound;
	public static Music looseSound;
	public static Music buttonSound;
//	public static Music buttonSlideSound;

	public static void load(AssetManager manager) {
		TextureAtlas connect4Atlas = manager.get(
				"data/images/pack/connect4graphics", TextureAtlas.class);
//		gamePlayMusic = manager.get("data/sounds/gameplayMusic.ogg", Music.class);
//		menuMusic = manager.get("data/sounds/menuMusic.ogg", Music.class);
		looseSound = manager.get("data/sounds/loose.ogg", Music.class);
		ballDropSound = manager.get("data/sounds/balldrop.ogg", Music.class);
		winSound = manager.get("data/sounds/win.ogg", Music.class);
//		buttonSlideSound = manager.get("data/sounds/button_slide.ogg", Music.class);
		buttonSound = manager.get("data/sounds/button.ogg", Music.class);
		fontScore = manager.get("data/scoreFont.fnt", BitmapFont.class);

		disk1Region = connect4Atlas.findRegion("player1");
		disk2Region = connect4Atlas.findRegion("player2");
		diskSprite = new Sprite(disk1Region);

		background = connect4Atlas.createSprite("background");
		easyButton = connect4Atlas.createSprite("easybutton");
		normalButton = connect4Atlas.createSprite("normalbutton");
		hardButton = connect4Atlas.createSprite("hardbutton");
		exitButton = connect4Atlas.createSprite("quitbutton");
		extremeButton = connect4Atlas.createSprite("extremebutton");
		onePlayerButton = connect4Atlas.createSprite("oneplayerbutton");
		twoPlayerButton = connect4Atlas.createSprite("twoplayerbutton");
		soundOnButton = connect4Atlas.createSprite("musiconbutton");
		soundOffButton = connect4Atlas.createSprite("musicoffbutton");
		boardBGOne = connect4Atlas.createSprite("grid");
		boardBGTwo = connect4Atlas.createSprite("gridshade");
		scoreBox = connect4Atlas.createSprite("scorebox");
		scoreBox1 = connect4Atlas.createSprite("scorebox1");
		menuButton = connect4Atlas.createSprite("mainmenubutton");
		restartButton = connect4Atlas.createSprite("restartbutton");
		resumeButton = connect4Atlas.createSprite("resumebutton");
		pauseButton = connect4Atlas.createSprite("pausebuttonon");
		bgAnimated = connect4Atlas.createSprite("bgAnimate");
		bgAnimated.setX(240 - bgAnimated.getWidth() / 2f);
		winnerOne = connect4Atlas.createSprite("player1winsfont");
		winnerTwo = connect4Atlas.createSprite("player2winsfont");
		winnerAI = connect4Atlas.createSprite("computerwinsfont");
		score1Name = connect4Atlas.createSprite("p1");
		score2Name = connect4Atlas.createSprite("p2");
		scoreAIName = connect4Atlas.createSprite("computer");
		trophy = connect4Atlas.createSprite("trophy");
		addSprite = connect4Atlas.createSprite("add");
		blueWin = connect4Atlas.createSprite("glowballblue");
		yellowWin = connect4Atlas.createSprite("glowballyellow");
		itsADraw = connect4Atlas.createSprite("itsadrawfont");

		preferences = Gdx.app.getPreferences("Scores");
		Assets.fontScore.setColor(Color.toFloatBits(255, 252, 0, 255));
		Assets.setVolume();
	}

	public static void setVolume() {
		if (!Assets.preferences.getBoolean("sound")) {
			
			ballDropSound.setVolume(0);
//			gamePlayMusic.setVolume(0);
//			menuMusic.setVolume(0);
			winSound.setVolume(0);
			looseSound.setVolume(0);
			buttonSound.setVolume(0);
//			buttonSlideSound.setVolume(0);
		} else {
			ballDropSound.setVolume(1);
//			gamePlayMusic.setVolume(1);
//			menuMusic.setVolume(1);
			winSound.setVolume(1);
			looseSound.setVolume(1);
			buttonSound.setVolume(1);
//			buttonSlideSound.setVolume(1);
		}
	}

}
