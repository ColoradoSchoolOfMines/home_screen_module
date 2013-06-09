package edu.mines.acmX.exhibit.modules.home_screen.backdrops.gameoflife;

import java.util.Random;

import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;

public class GridBackdrop extends Backdrop {
	
	public static final int SIZE = 20;
	//public static final int FRAME_DELAY = 50;
	public static final int RAND_TILE_ALIVE = 20;
	
	//private int frameTick = 0;
	boolean tiles[][];
	
	private static final float SECONDS_TO_UPDATE = 1;
	private int lastUpdateTime;
	
	private int size_w;
	private int size_h;
	
	public GridBackdrop(HomeScreen p) {
		super(p);
		
		size_w = (int) (Math.floor(p.screenWidth / SIZE));
		size_h = (int) (Math.floor(p.screenHeight / SIZE));
		
		tiles = new boolean[size_w][size_h];
		setup();
		getNumNeighbors(25, 25);
	}
	
	public boolean isAlive(int x, int y) {
		return tiles[x][y];
	}
	
	public boolean tileExists(int x, int y) {
		return ((x >= 0) && (x < size_w))
				&& ((y >= 0) && (y < size_h));
	}
	
	public void setTile(int x, int y, boolean val) {
		tiles[x][y] = val;
	}
	
	public void setup() {
		Random r = new Random();
		r.setSeed(parent.millis());
		for (int i = 0; i < size_w; ++i) {
			for (int j = 0; j < size_h; ++j) {
				tiles[i][j] = false;
				
				if (r.nextInt(100) < RAND_TILE_ALIVE) {
					setTile(i, j, true);
				}
				
			}
		}
		lastUpdateTime = parent.millis();
	}

	public void nextGeneration() {
		boolean nextGeneration[][] = new boolean[size_w][size_h];
		for (int i = 0; i < size_w; ++i) {
			for (int j = 0; j < size_h; ++j) {
				
				int numNeighbors = getNumNeighbors(i, j);
				nextGeneration[i][j] = tiles[i][j];
				
				if (numNeighbors > 3 || numNeighbors < 2) {
					nextGeneration[i][j] = false;
				}
				if (numNeighbors == 3) {
					nextGeneration[i][j] = true;
				}
//				
//				if (!isAlive) {
//					if (numNeighbors == 3) nextGeneration[i][j] = true;
//				} else {
//					if (!(numNeighbors == 3 || numNeighbors == 2)) nextGeneration[i][j] = false;
//				}
			}
		}
		for (int i = 0; i < size_w; ++i) {
			for (int j = 0; j < size_h; ++j) {
				if (nextGeneration[i][j] != tiles[i][j]) {
					tiles[i][j] = nextGeneration[i][j];
				}
			}
		}
		
	}
	
	public int getNumNeighbors(int x, int y) {
		int offsets[][] = { {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0} };
		int ret = 0;
		for (int i = 0; i < offsets.length; ++i) {
			int tx = x + offsets[i][0];
			int ty = y + offsets[i][1];
			if (tileExists(tx, ty)) {
				if (isAlive(tx, ty)) ++ret;
			}
		}
		return ret;
	}

	@Override
	public void update() {
		if (parent.millis() - lastUpdateTime > SECONDS_TO_UPDATE * 1000) {
			nextGeneration();
			lastUpdateTime = parent.millis();
		}
		/*++frameTick;
		if (frameTick == FRAME_DELAY) {
			frameTick = 0;
			nextGeneration();
		}*/
		
	}

	@Override
	public void draw() {
		parent.background(81, 81, 81);
		parent.stroke(66, 66, 66);
		for (int i = 0; i < size_w; ++i) {
			parent.line(i * SIZE, 0, i * SIZE, parent.height);
		}
		for (int i = 0; i < size_h; ++i) {
			parent.line(0, i * SIZE, parent.width, i * SIZE);
		}
		
		for (int i = 0; i < size_w; ++i) {
			for (int j = 0; j < size_h; ++j) {
				if (isAlive(i, j)) {
					parent.fill(0, 255, 0);
				} else {
					parent.fill(81, 81, 81);
				}
				parent.rect(i * SIZE, j * SIZE, SIZE, SIZE);
			}
		}
	}

}
