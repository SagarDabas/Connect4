package com.inoxapps.connect_4.logic;

import com.inoxapps.connect_4.gameObjects.Player;


public class FilledPosition {
	public float x, y;
	public Player player;

	public FilledPosition(float x, float y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}
}