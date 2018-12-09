package com.inoxapps.connect_4.logic;

public enum Difficulty {
	// EASY : RANDOM MOVES
	// MEDIUM : ONLY DEFENSIVE
	// HARD : STRATEGIC
	HUMAN((byte) 0), EASY((byte) 3), MEDIUM((byte) 3), HARD((byte) 3), HARDEST((byte) 4);

	private byte level;

	Difficulty(byte level) {
		this.level = level;
	}

	public byte getDepth() {
		return level;
	}
}
