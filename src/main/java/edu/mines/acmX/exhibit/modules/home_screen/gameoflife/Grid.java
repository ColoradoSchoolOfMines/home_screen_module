package edu.mines.acmX.exhibit.modules.home_screen.gameoflife;

public class Grid {
	public static final int SIZE = 100;
	
	boolean tiles[][];
	
	public Grid() {
		tiles = new boolean[SIZE][SIZE];
	}
	
	public boolean isAlive(int x, int y) {
		return tiles[x][y];
	}
	
	public boolean tileExists(int x, int y) {
		return ((x >= 0) || (x < SIZE)
				|| (y >= 0) || (y < SIZE));
	}
	
	public void setTile(int x, int y, boolean val) {
		tiles[x][y] = val;
	}
	
	public void setup() {
		for (int i = 0; i < SIZE; ++i) {
			for (int j = 0; j < SIZE; ++j) {
				tiles[i][j] = false;
			}
		}
	}
	
	public void nextGeneration() {
		boolean nextGeneration[][] = new boolean[SIZE][SIZE];
		for (int i = 0; i < SIZE; ++i) {
			for (int j = 0; j < SIZE; ++j) {
				boolean isAlive = isAlive(i, j);
				int numNeighbors = getNumNeighbors(i, j);
				
				if (!isAlive) {
					if (numNeighbors < 2 || numNeighbors > 3) nextGeneration[i][j] = false;
					else nextGeneration[i][j] = true;
				} else {
					if (numNeighbors == 3) nextGeneration[i][j] = true;
					else nextGeneration[i][j] = false;
				}
			}
		}
		for (int i = 0; i < SIZE; ++i) {
			for (int j = 0; j < SIZE; ++j) {
				if (nextGeneration[i][j] != tiles[i][j]) {
					tiles[i][j] = nextGeneration[i][j];
				}
			}
		}
		
	}
	
	public int getNumNeighbors(int x, int y) {
		int OFFSETS[][] = { {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0} };
		int ret = 0;
		for (int i = 0; i < 8; ++i) {
			int tx = i + OFFSETS[i][0];
			int ty = y + OFFSETS[i][1];
			if (tileExists(tx, ty)) {
				if (isAlive(tx, ty)) ++ret;
			}
		}
		return ret;
	}

}
