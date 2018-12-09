package com.inoxapps.connect_4;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface Configurations {
    
	public static final byte TILE_WIDTH = 60;
	public static final byte TILE_LENGTH = 60;
//	public static final byte BALL_WIDTH = 60;
//	public static final byte BALL_LENGTH = 60;
	public static final byte BOARD_LEFT_MARGIN = 30;
	public static final int BOARD_BOTTOM_MARGIN = 225;
	public static final byte BOARD_ROWS = 6;
	public static final byte BOARD_COLUMNS = 7;
	public static final byte NUM_TO_CHECK = 4;
	public static final int WEIGHT_3 = 100;
	public static final int WEIGHT_2 = 10;
	public static final int WEIGHT_1 = 1;
	public static final int MAX_SCORE = 10000;
	public static final int CAM_WIDTH = 480;
	public static final int CAM_HEIGHT = 800;
	public static final OrthographicCamera GUI_CAM = new OrthographicCamera(Configurations.CAM_WIDTH,
			Configurations.CAM_HEIGHT);
	
}