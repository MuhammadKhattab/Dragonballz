package dragonball.model.game;

import java.util.*;

import javax.swing.JOptionPane;

import dragonball.model.attack.*;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleEvent;
import dragonball.model.battle.BattleEventType;
import dragonball.model.battle.BattleListener;
import dragonball.model.cell.Collectible;
import dragonball.model.cell.EmptyCell;
import dragonball.model.character.fighter.*;
import dragonball.model.dragon.*;
import dragonball.model.exceptions.MissingFieldException;
import dragonball.model.exceptions.UnknownAttackTypeException;
import dragonball.model.player.*;
import dragonball.model.world.*;
import java.io.*;

@SuppressWarnings("serial")
public class Game implements Serializable, PlayerListener, WorldListener, BattleListener {
	private Player player;
	private World world;
	private ArrayList<NonPlayableFighter> weakFoes;
	private ArrayList<NonPlayableFighter> strongFoes;
	private ArrayList<Attack> attacks;
	private ArrayList<Dragon> dragons;
	private GameState state;
	private GameListener listener;
	private String lastSavedFile;
	private String gainedSkills;

	public Game() throws UnknownAttackTypeException, MissingFieldException {
		world = new World();
		weakFoes = new ArrayList<NonPlayableFighter>();
		strongFoes = new ArrayList<NonPlayableFighter>();
		attacks = new ArrayList<Attack>();
		dragons = new ArrayList<Dragon>();
		state = GameState.WORLD;
		player = new Player("Random");
		player.setListener(this);
		world.setListener(this);

		lastSavedFile = "";

		try {
			loadAttacks("data/Database-Attacks.csv");
		} catch (dragonball.model.exceptions.InvalidFormatException e) {
			attacks.clear();
			loadAttacks("data/Database-Attacks-aux.csv");
		}

		try {
			loadDragons("data/Database-Dragons.csv");
		} catch (MissingFieldException e) {
			dragons.clear();
			loadDragons("data/Database-Dragons-aux.csv");
		}

		foeRange();
		world.generateMap(weakFoes, strongFoes);
	}

	private ArrayList<String[]> loadCSV(String filePath) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		String line;
		String[] lineA;
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				lineA = line.split(",");
				list.add(lineA);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void loadAttacks(String filePath) throws UnknownAttackTypeException, MissingFieldException {
		ArrayList<String[]> atts = loadCSV(filePath);
		String[] a;
		for (int i = 0; i < atts.size(); i++) {
			a = atts.get(i);
			Attack x = null;

			if (a.length < 3) {
				throw new MissingFieldException(
						"File: " + filePath + ", Line: " + (i + 1) + ", Expected: 3, Missing: " + (3 - a.length),
						filePath, i + 1, 3 - a.length);
			}

			if (a[0].equals("SA")) {
				x = new SuperAttack(a[1], Integer.parseInt(a[2]));
			} else {
				if (a[0].equals("UA")) {
					x = new UltimateAttack(a[1], Integer.parseInt(a[2]));
				} else {
					if (a[0].equals("SS")) {
						x = new SuperSaiyan();
					} else {
						if (a[0].equals("MC"))
							x = new MaximumCharge();
						else
							throw new UnknownAttackTypeException("File: " + filePath + ", Line: " + (i + 1)
									+ ", Attack Type: " + a[0] + " is undefined.", filePath, i + 1, a[0]);
					}
				}
			}
			if (x != null)
				attacks.add(x);
		}
	}

	public void loadFoes(String filePath) throws MissingFieldException {
		strongFoes.clear();
		weakFoes.clear();

		ArrayList<String[]> foos = loadCSV(filePath);
		String[] a, b, c;
		ArrayList<SuperAttack> superA = null;
		SuperAttack su = null;
		ArrayList<UltimateAttack> ultimateA = null;
		UltimateAttack ul = null;
		for (int i = 0; i < foos.size(); i += 3) {

			// first line: info
			a = foos.get(i);

			// if number of fields is less than expected.
			if (a.length < 8)
				throw new MissingFieldException(
						"File: " + filePath + ", Line: " + (i + 1) + ", Expected: 8, Missing: " + (8 - a.length),
						filePath, i + 1, 8 - a.length);

			// second line: super attacks
			b = foos.get(i + 1);
			superA = new ArrayList<SuperAttack>();
			// get arrayList of super attacks
			for (int j = 0; j < b.length; j++) {
				// search for the damages
				for (int k = 0; k < attacks.size(); k++) {
					if (b[j].equals(attacks.get(k).getName())) {
						su = new SuperAttack(b[j], attacks.get(k).getDamage());
						break;
					}
				}
				if (su != null)
					superA.add(su);
				su = null;
			}
			// third line: ultimate attacks
			c = foos.get(i + 2);
			// get arrayList of ultimate attacks
			ultimateA = new ArrayList<UltimateAttack>();
			for (int j = 0; j < c.length; j++) {
				// search for the damage
				for (int k = 0; k < attacks.size(); k++) {
					if (c[j].equals(attacks.get(k).getName())) {
						if (c[j].equals("Super Saiyan"))
							ul = new SuperSaiyan();
						else
							ul = new UltimateAttack(c[j], attacks.get(k).getDamage());
						break;
					}
				}
				if (ul != null)
					ultimateA.add(ul);
				ul = null;
			}
			boolean str;
			str = a[7].equals("TRUE") ? true : false;
			NonPlayableFighter z = new NonPlayableFighter(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2]),
					Integer.parseInt(a[3]), Integer.parseInt(a[4]), Integer.parseInt(a[5]), Integer.parseInt(a[6]), str,
					superA, ultimateA);
			if (str)
				strongFoes.add(z);
			else
				weakFoes.add(z);
		}
	}

	public void loadDragons(String filePath) throws MissingFieldException {
		ArrayList<String[]> dragonz = loadCSV(filePath);
		String[] a, b, c;
		ArrayList<SuperAttack> superA;
		SuperAttack su = null;
		ArrayList<UltimateAttack> ultimateA;
		UltimateAttack ul = null;
		// file
		for (int i = 0; i < dragonz.size(); i += 3) {
			// first line info
			a = dragonz.get(i);
			if (a.length < 3)
				throw new MissingFieldException("Dragonz", filePath, i + 1, 3 - a.length);
			// second line super attacks
			b = dragonz.get(i + 1);
			superA = new ArrayList<SuperAttack>();
			// get arrayList of super attacks
			for (int j = 0; j < b.length; j++) {
				// search for the damage
				for (int k = 0; k < attacks.size(); k++) {
					if (b[j].equals(attacks.get(k).getName())) {
						if (b[j].equals("Maximum Charge"))
							su = new MaximumCharge();
						else
							su = new SuperAttack(b[j], attacks.get(k).getDamage());
						break;
					}
				}
				if (su != null)
					superA.add(su);
				su = null;
			}
			// third line ultimate attacks
			c = dragonz.get(i + 2);
			// get arrayList of ultimate attacks
			ultimateA = new ArrayList<UltimateAttack>();
			for (int j = 0; j < c.length; j++) {
				// search for the damage
				for (int k = 0; k < attacks.size(); k++) {
					if (c[j].equals(attacks.get(k).getName())) {
						if (c[j].equals("Super Saiyan"))
							ul = new SuperSaiyan();
						else
							ul = new UltimateAttack(c[j], attacks.get(k).getDamage());
						break;
					}
				}
				if (ul != null)
					ultimateA.add(ul);
				ul = null;
			}
			Dragon z = new Dragon(a[0], superA, ultimateA, Integer.parseInt(a[1]), Integer.parseInt(a[2]));
			dragons.add(z);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public ArrayList<NonPlayableFighter> getWeakFoes() {
		return weakFoes;
	}

	public ArrayList<NonPlayableFighter> getStrongFoes() {
		return strongFoes;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public ArrayList<Dragon> getDragons() {
		return dragons;
	}

	public GameState getState() {
		return state;
	}

	@Override
	public void onDragonCalled() {
		state = GameState.DRAGON;

		Dragon dragon = dragons.get(new Random().nextInt(dragons.size()));

		notifyOnDragonCalled(dragon);
	}

	@Override
	public void onWishChosen(DragonWish wish) {
		state = GameState.WORLD;
	}

	@Override
	public void onFoeEncountered(NonPlayableFighter foe) {
		state = GameState.BATTLE;
		Battle x = new Battle(player.getActiveFighter(), foe);
		x.setListener(this);
		x.start();
	}

	@Override
	public void onCollectibleFound(Collectible collectible) {
		if (collectible.equals(Collectible.SENZU_BEAN))
			player.setSenzuBeans(player.getSenzuBeans() + 1);
		else {
			player.setDragonBalls(player.getDragonBalls() + 1);
			if (player.getDragonBalls() >= 7)
				player.callDragon();
		}
		notifyOnCollectibleFound(collectible);
	}

	public void foeRange() throws MissingFieldException {
		int N = 1, maxlvl = 1;
		if (player != null)
			maxlvl = player.getMaxFighterLevel();
		N = (maxlvl - 1) / 10 + 1;
		try {
			loadFoes("data/Database-Foes-Range" + N + ".csv");
		} catch (IOException e) {
			weakFoes.clear();
			strongFoes.clear();
			loadFoes("data/Database-Foes-aux.csv");
		}

	}

	@Override
	public void onBattleEvent(BattleEvent e) {
		if (e.getType().equals(BattleEventType.ENDED)) {
			state = GameState.WORLD;
			Battle b = (Battle) e.getSource();
			NonPlayableFighter fo = (NonPlayableFighter) b.getFoe();
			PlayableFighter meh = (PlayableFighter) b.getMe();
			if (e.getWinner() == meh) {
				int oldXP = meh.getXp();
				meh.setXp(oldXP + fo.getLevel() * 5);

				gainedSkills = "Learned Super Attacks:\n";
				ArrayList<SuperAttack> fozsu = fo.getSuperAttacks();

				for (SuperAttack x : fozsu)
					if (!(player.getSuperAttacks().contains(x))) {
						player.getSuperAttacks().add(x);
						gainedSkills += String.format(" + %s\n", x.getName());
					}

				gainedSkills += "Learned Ultimate Attacks:\n";
				ArrayList<UltimateAttack> fozult = fo.getUltimateAttacks();

				for (UltimateAttack x : fozult)
					if (!(player.getUltimateAttacks().contains(x))) {
						player.getUltimateAttacks().add(x);
						gainedSkills += String.format(" + %s\n", x.getName());
					}

				if (fo.isStrong()) {
					player.setExploredMaps(player.getExploredMaps() + 1);
					try {
						foeRange();
					} catch (MissingFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					world.generateMap(weakFoes, strongFoes);
				} else {
					world.getMap()[world.getPlayerRow()][world.getPlayerColumn()] = new EmptyCell();
					world.getMap()[world.getPlayerRow()][world.getPlayerColumn()].setListener(world);
				}

				state = GameState.WORLD;
			} else {
				try {
					load(lastSavedFile);
				} catch (FileNotFoundException exp) {
					world = new World();
					world.generateMap(weakFoes, strongFoes);
					world.setListener(this);
					state = GameState.WORLD;
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
		notifyOnBattleEvent(e);
	}

	public GameListener getListener() {
		return listener;
	}

	public void setListener(GameListener listener) {
		this.listener = listener;
	}

	public void save(String fileName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(this);
		out.close();
		fileOut.close();
		lastSavedFile = fileName;
	}

	public void load(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(new File(fileName));
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Game x = (Game) in.readObject();
		in.close();
		fileIn.close();
		player = x.player;
		player.setListener(this);
		world = x.world;
		world.setListener(this);
		weakFoes = x.weakFoes;
		strongFoes = x.strongFoes;
		attacks = x.attacks;
		dragons = x.dragons;
		state = x.state;
		listener = x.listener;
		lastSavedFile = fileName;
		x = null;
	}

	public void notifyOnCollectibleFound(Collectible collectible) {
		if (listener != null) {
			listener.onCollectibleFound(collectible);
		}
	}

	public void notifyOnBattleEvent(BattleEvent e) {
		if (listener != null) {
			listener.onBattleEvent(e);
		}
	}

	public void notifyOnDragonCalled(Dragon dragon) {
		if (listener != null) {
			listener.onDragonCalled(dragon);
		}
	}

	public String getGainedSkills() {
		return gainedSkills;
	}

	public String getLastSavedFile() {
		return lastSavedFile;
	}
}
