package dragonball.model.world;

import dragonball.model.cell.*;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.exceptions.MapIndexOutOfBoundsException;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class World implements Serializable, CellListener {
	public static final int MAP_SIZE = 10;
	public static final int NUM_WEAK_FOES = 15;
	public static final int NUM_MIN_SENZU_BEANS = 3;
	public static final int NUM_MAX_SENZU_BEANS = 5;
	public static final int NUM_DRAGON_BALLS = 1;
	private Cell[][] map;
	private int playerColumn;
	private int playerRow;
	private WorldListener listener;

	public World() {
		map = new Cell[MAP_SIZE][MAP_SIZE];
	}

	public void generateMap(ArrayList<NonPlayableFighter> weakFoes, ArrayList<NonPlayableFighter> strongFoes) {
		for (int i = 0; i < MAP_SIZE; i++)
			for (int j = 0; j < MAP_SIZE; j++) {
				map[i][j] = new EmptyCell();
				map[i][j].setListener(this);
			}
		playerRow = playerColumn = MAP_SIZE - 1;
		Random r = new Random();
		Random col = new Random();
		Random row = new Random();
		// random boss
		NonPlayableFighter boss = strongFoes.get(r.nextInt(strongFoes.size()));
		map[0][0] = new FoeCell(boss);
		map[0][0].setListener(this);
		NonPlayableFighter foe;
		int x, y;
		for (int i = 0; i < NUM_WEAK_FOES; i++) {
			// random foe
			foe = weakFoes.get(r.nextInt(weakFoes.size()));
			// find an empty random cell
			while (true) {
				x = col.nextInt(MAP_SIZE);
				y = row.nextInt(MAP_SIZE);
				if (map[x][y].toString().equals("[ ]") && x != 9 && y != 9) {
					map[x][y] = new FoeCell(foe);
					map[x][y].setListener(this);
					break;
				}
			}
		}
		// number between 3-5
		int noBeans = r.nextInt(NUM_MAX_SENZU_BEANS - NUM_MIN_SENZU_BEANS) + NUM_MIN_SENZU_BEANS;
		CollectibleCell senzuBean = new CollectibleCell(Collectible.SENZU_BEAN);
		for (int i = 0; i < noBeans; i++) {
			// find an empty random cell
			while (true) {
				x = col.nextInt(MAP_SIZE);
				y = row.nextInt(MAP_SIZE);
				if (map[x][y].toString().equals("[ ]") && x != 9 && y != 9) {
					map[x][y] = senzuBean;
					map[x][y].setListener(this);
					break;
				}
			}
		}
		CollectibleCell dragonBall = new CollectibleCell(Collectible.DRAGON_BALL);
		while (true) {
			x = col.nextInt(MAP_SIZE);
			y = row.nextInt(MAP_SIZE);
			if (map[x][y].toString().equals("[ ]") && x != 9 && y != 9) {
				map[x][y] = dragonBall;
				map[x][y].setListener(this);
				break;
			}
		}
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++)
				if (i == playerRow && j == playerColumn)
					s += "[x]";
				else
					s += map[i][j];
			s += "\n";
		}
		return s;
	}

	public Cell[][] getMap() {
		return map;
	}

	public int getPlayerColumn() {
		return playerColumn;
	}

	public int getPlayerRow() {
		return playerRow;
	}

	@Override
	public void onFoeEncountered(NonPlayableFighter foe) {
		if (listener != null)
			listener.onFoeEncountered(foe);

	}

	@Override
	public void onCollectibleFound(Collectible collectible) {
		if (listener != null)
			listener.onCollectibleFound(collectible);
		map[playerRow][playerColumn] = new EmptyCell();
		map[playerRow][playerColumn].setListener(this);
	}

	public WorldListener getListener() {
		return listener;
	}

	public void setListener(WorldListener worldListener) {
		this.listener = worldListener;
	}

	public void resetPlayerPosition() {
		playerRow = playerColumn = MAP_SIZE - 1;
	}

	public void moveUp() {
		if (playerRow > 0) {
			playerRow--;
			map[playerRow][playerColumn].onStep();
		} else
			throw new MapIndexOutOfBoundsException(playerRow, playerColumn);
	}

	public void moveDown() {
		if (playerRow < 9) {
			playerRow++;
			map[playerRow][playerColumn].onStep();
		} else
			throw new MapIndexOutOfBoundsException(playerRow, playerColumn);
	}

	public void moveRight() {
		if (playerColumn < 9) {
			playerColumn++;
			map[playerRow][playerColumn].onStep();
		} else
			throw new MapIndexOutOfBoundsException(playerRow, playerColumn);
	}

	public void moveLeft() {
		if (playerColumn > 0) {
			playerColumn--;
			map[playerRow][playerColumn].onStep();
		} else
			throw new MapIndexOutOfBoundsException(playerRow, playerColumn);
	}

}
