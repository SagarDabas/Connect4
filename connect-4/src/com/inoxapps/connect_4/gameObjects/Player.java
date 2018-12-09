package com.inoxapps.connect_4.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.inoxapps.connect_4.Assets;

public enum Player {
	PlayerOne((byte) 1, Assets.disk1Region), PlayerTwo((byte) 2,
			Assets.disk2Region);
	private byte id;
	private TextureRegion playerSprite;
	private boolean isWinner = false;

	Player(byte id, TextureRegion playerSprite) {
		this.id = id;
		this.playerSprite = playerSprite;
	}

	public byte getID() {
		return id;
	}

	public TextureRegion getPlayerSprite() {
		return playerSprite;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
}
