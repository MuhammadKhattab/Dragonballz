package dragonball.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

import dragonball.model.attack.Attack;
import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.SuperSaiyan;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleEvent;
import dragonball.model.battle.BattleEventType;
import dragonball.model.battle.BattleListener;
import dragonball.model.battle.BattleOpponent;
import dragonball.model.cell.Cell;
import dragonball.model.cell.CellListener;
import dragonball.model.cell.Collectible;
import dragonball.model.cell.CollectibleCell;
import dragonball.model.cell.EmptyCell;
import dragonball.model.cell.FoeCell;
import dragonball.model.character.fighter.Earthling;
import dragonball.model.character.fighter.Frieza;
import dragonball.model.character.fighter.Majin;
import dragonball.model.character.fighter.Namekian;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.dragon.Dragon;
import dragonball.model.dragon.DragonWish;
import dragonball.model.dragon.DragonWishType;
import dragonball.model.game.Game;
import dragonball.model.game.GameListener;
import dragonball.model.game.GameState;
import dragonball.model.player.Player;
import dragonball.model.player.PlayerListener;
import dragonball.model.world.World;
import dragonball.model.world.WorldListener;

public class M2PrivateTest {
	private boolean hasCalled = false;
	private BattleEventType type;
	private Battle b;
	private boolean firstChange = true;
	private boolean onAttackerCalled1 = false;
	private boolean onAttackerCalled2 = false;
	private boolean onDefenderCalled1 = false;
	private boolean onDefenderCalled2 = false;

	// ************************ Listeners tests *********//
	@Test(timeout = 1000)
	public void testWorldClassOnFoeEncounteredMethodListeners()
			throws Exception {
		hasCalled = false;
		Game s = new Game() {
			public void onFoeEncountered(NonPlayableFighter foe) {
				hasCalled = true;
			}
		};
		World w = s.getWorld();

		try {
			w.onFoeEncountered(new NonPlayableFighter("foe", 1, 1, 1, 1, 1, 1,
					true, null, null));
		} catch (NullPointerException e) {
			fail("The method onFoeEncountered in World class should handle when listener is null");
		}
		invokeSetter(w, WorldListener.class, s);
		assertTrue(
				"World class should notify the Game class when a foe cell is encountered",
				hasCalled);
	}

	@Test(timeout = 1000)
	public void testWorldClassOnCollectibleFoundMethodListeners()
			throws Exception {
		hasCalled = false;
		Game s = new Game() {
			public void onCollectibleFound(Collectible collectible) {
				hasCalled = true;
			}
		};
		World w = s.getWorld();
		try {
			w.onCollectibleFound(Collectible.SENZU_BEAN);
		} catch (NullPointerException e) {
			fail("The method onCollectibleFound in World class should handle when listener is null");
		}
		invokeSetter(w, WorldListener.class, s);
		assertTrue(
				"World class should notify the Game class when a Collectible cell is encountered",
				hasCalled);
	}

	@Test(timeout = 1000)
	public void testCollectibleCellClassOnStepMethodListeners()
			throws Exception {
		hasCalled = false;
		World w = new World() {
			public void onCollectibleFound(Collectible collectible) {
				hasCalled = true;
			}
		};
		CollectibleCell c = new CollectibleCell(Collectible.SENZU_BEAN);
		Field[] fields = Cell.class.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(CellListener.class)) {
				i = j;
				break;
			}
		}

		if (i != -1) {
			Field f = Cell.class.getDeclaredFields()[i];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = Cell.class
					.getMethod(methodName, CellListener.class);
			try {
				setter.invoke(c, w);
			} catch (Exception e) {
				fail("The class: " + c.getClass()
						+ " should have a setter method for the listener");
			}
		}

		c.onStep();

		assertTrue(
				"CollectibleCell class should notify the World class when a Collectible cell is encountered onStep",
				hasCalled);
	}

	@Test(timeout = 1000)
	public void testFoeCellClassOnStepMethodListeners() throws Exception {
		hasCalled = false;
		World w = new World() {
			public void onFoeEncountered(NonPlayableFighter foe) {
				hasCalled = true;
			}
		};
		FoeCell c = new FoeCell((new NonPlayableFighter("foe", 1, 1, 1, 1, 1,
				1, true, null, null)));

		Field[] fields = Cell.class.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(CellListener.class)) {
				i = j;
				break;
			}
		}

		if (i != -1) {
			Field f = Cell.class.getDeclaredFields()[i];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = Cell.class
					.getMethod(methodName, CellListener.class);
			try {
				setter.invoke(c, w);
			} catch (Exception e) {
				fail("The class: " + c.getClass()
						+ " should have a setter method for the listener");
			}
		}
		c.onStep();

		assertTrue(
				"FoeCell class should notify the World class when a Foe cell is encountered onStep",
				hasCalled);
	}

	@Test(timeout = 1000)
	public void testAllCellsHaveAssignedListeners() throws Exception {
		Game g = new Game();
		World w = g.getWorld();
		Field[] fields = Cell.class.getDeclaredFields();

		int index = -1;
		for (int k = 0; k < fields.length; k++) {
			if (fields[k].getType().equals(CellListener.class)) {
				index = k;
				break;
			}
		}
		if (index != -1) {
			Field f = Cell.class.getDeclaredFields()[index];
			f.setAccessible(true);

			for (int i = 0; i < w.getMap().length; i++) {
				for (int j = 0; j < w.getMap().length; j++) {
					assertNotNull("The cells listener of the map shouldn't be null", f.get(w.getMap()[i][j]));
				}
			}
		}

	}

	@Test(timeout = 1000)
	public void testBattleClassAttackMethodListeners() throws Exception {
		hasCalled = false;

		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null));

		b.attack(new SuperAttack("Kamehameha", 400));

		invokeSetter(b, BattleListener.class, g);
		b.attack(new SuperAttack("Kamehameha", 400));
		assertTrue("Battle class should notify the Game class on attack",
				hasCalled);

		assertEquals(
				"The Battle Event type notified from Battle to the Game class on attack is incorrect",
				BattleEventType.ATTACK, type);
	}

	@Test(timeout = 1000)
	public void testBattleClassBlockMethodListeners() throws Exception {
		hasCalled = false;
		firstChange = true;
		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null));

		invokeSetter(b, BattleListener.class, g);
		b.block();

		assertTrue("Battle class should notify the Game class on block",
				hasCalled);
		assertEquals(
				"The Battle Event type notified from Battle to the Game class on block is incorrect",
				BattleEventType.BLOCK, type);
	}

	@Test(timeout = 1000)
	public void testBattleClassUseMethodListeners() throws Exception {
		hasCalled = false;
		firstChange = true;
		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null));
		invokeSetter(b, BattleListener.class, g);
		Player p = new Player("");
		p.setSenzuBeans(2);

		Namekian n = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6,
				600, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(n);
		p.setFighters(fighters);
		p.setActiveFighter(n);
		b.use(p, Collectible.SENZU_BEAN);

		assertTrue("Battle class should notify the Game class on use",
				hasCalled);
		assertEquals(
				"The Battle Event type notified from Battle to the Game class on Use is incorrect",
				BattleEventType.USE, type);
	}

	@Test(timeout = 1000)
	public void testBattleClassEndTurnMethodListeners_OnWinnning() throws Exception {
		hasCalled = false;
		firstChange = true;
		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		NonPlayableFighter foe = new NonPlayableFighter("foe", 1, 1, 1, 1, 1,
				1, true, null, null);
		Battle b = new Battle(new Saiyan("saiyan"), foe);

		invokeSetter(b, BattleListener.class, g);
		Field f = Battle.class.getDeclaredField("foe");
		f.setAccessible(true);

		foe.setHealthPoints(0);
		f.set(b, foe);
		b.endTurn();

		assertTrue("Battle class should notify the Game class on endTurn",
				hasCalled);
		assertEquals(
				"The Battle Event type notified from Battle to the Game class on endTurn, when there is a winner, is incorrect",
				BattleEventType.ENDED, type);
	}

	@Test(timeout = 1000)
	public void testBattleClassEndTurnMethodListeners_NewTurn() throws Exception {
		hasCalled = false;

		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		NonPlayableFighter foe = new NonPlayableFighter("foe", 1, 1, 1, 1, 1,
				1, true, null, null);
		Battle b = new Battle(new Saiyan("saiyan"), foe);

		invokeSetter(b, BattleListener.class, g);
		Field f = Battle.class.getDeclaredField("foe");
		f.setAccessible(true);
		hasCalled = false;
		firstChange = true;
		foe.setHealthPoints(230);
		f.set(b, foe);
		b.endTurn();
		assertEquals(
				"The Battle Event type notified from Battle to the Game class on endTurn, when there is a winner, is incorrect",
				BattleEventType.NEW_TURN, type);
	}

	@Test(timeout = 1000)
	public void testBattleClassStartMethodListeners() throws Exception {
		hasCalled = false;
		firstChange = true;
		Game g = new Game() {
			public void onBattleEvent(BattleEvent e) {
				hasCalled = true;
				if (firstChange) {
					type = e.getType();
					firstChange = false;
				}
			}
		};

		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null));

		invokeSetter(b, BattleListener.class, g);

		b.start();

		assertTrue("Battle class should notify the Game class on start",
				hasCalled);
		assertEquals(
				"The Battle Event type notified from Battle to the Game class on start is incorrect",
				BattleEventType.STARTED, type);
	}

	@Test(timeout = 1000)
	public void testListenersVariablesAccessibility()
			throws Exception, NoSuchFieldException, SecurityException {
		int index = classContainsFieldAtIndex(Player.class,
				PlayerListener.class);
		if (index != -1) {
			Field f = Player.class.getDeclaredFields()[index];
			assertEquals(
					"\"listener\" instance variable in class Player should not be accessed outside that class",
					2, f.getModifiers());
		}

		index = classContainsFieldAtIndex(Cell.class, CellListener.class);
		if (index != -1) {
			Field f = Cell.class.getDeclaredFields()[index];
			assertEquals(
					"\"listener\" instance variable in class Cell should not be accessed outside that class",
					2, f.getModifiers());
		}

		index = classContainsFieldAtIndex(Battle.class, BattleListener.class);
		if (index != -1) {
			Field f = Battle.class.getDeclaredFields()[index];
			assertEquals(
					"\"listener\" instance variable in class Battle should not be accessed outside that class",
					2, f.getModifiers());
		}

		index = classContainsFieldAtIndex(World.class, WorldListener.class);
		if (index != -1) {
			Field f = World.class.getDeclaredFields()[index];
			assertEquals(
					"\"listener\" instance variable in class World should not be accessed outside that class",
					2, f.getModifiers());
		}
		index = classContainsFieldAtIndex(Game.class, GameListener.class);
		if (index != -1) {
			Field f = Game.class.getDeclaredFields()[index];
			assertEquals(
					"\"listener\" instance variable in class Game should not be accessed outside that class",
					2, f.getModifiers());
		}
	}

	// **************************** Functionality methods
	// ********************************//

	@Test(timeout = 1000)
	public void testUltimateAssignAttackMethod() throws Exception {
		Earthling fighter1 = new Earthling("fighter1");
		ArrayList<UltimateAttack> ultimateAttacks1 = new ArrayList<UltimateAttack>();
		UltimateAttack u1 = new UltimateAttack("Ultimate1", 50);
		ultimateAttacks1.add(u1);
		fighter1.setUltimateAttacks(ultimateAttacks1);

		ArrayList<UltimateAttack> ultimateAttacks2 = new ArrayList<UltimateAttack>();
		UltimateAttack u3 = new UltimateAttack("Ultimate3", 50);
		UltimateAttack u4 = new UltimateAttack("Ultimate4", 50);
		ultimateAttacks2.add(u3);
		ultimateAttacks2.add(u4);
		Earthling fighter2 = new Earthling("fighter2");
		fighter2.setUltimateAttacks(ultimateAttacks2);

		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		UltimateAttack ultimateAttack = new UltimateAttack("Ultimate", 50);
		player.assignAttack(fighter1, ultimateAttack, u1);
		assertTrue(
				"assignAttack method in class Player should add the new Ultimate Attack to the list of UltimateAttacks of the input fighter",
				player.getFighters().get(0).getUltimateAttacks()
						.contains(ultimateAttack));
		assertFalse(
				"assignAttack method in class Player should remove the old Ultimate attack from the list of UltimateAttacks of the input fighter",
				player.getFighters().get(0).getUltimateAttacks().contains(u1));

		player.assignAttack(fighter1, ultimateAttack, null);
		assertTrue(
				"assignAttack method in class Player should add the new Ultimate Attack to the list of ultimateAttacks of the input fighter",
				player.getFighters().get(0).getUltimateAttacks()
						.contains(ultimateAttack));

		player.assignAttack(fighter2, ultimateAttack, null);
		assertFalse(
				"assignAttack method in class Player should not add the new Ultimate Attack if the list of ultimate attacks of the input fighter is full",
				player.getFighters().get(1).getUltimateAttacks()
						.contains(ultimateAttack));

	}

	@Test(timeout = 1000)
	public void testUpgradeFighterMethod_BlastDamage() throws Exception {
		Earthling fighter1 = new Earthling("fighter1", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		Earthling fighter2 = new Earthling("fighter2", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();

		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		player.upgradeFighter(fighter1, 'B');
		assertEquals(
				"upgradeFighter method in class Player should increase the blastDamage of the corresponding fighter "
						+ "with 50 points when the char input is B",
				100,
				player.getFighters().get(0).getBlastDamage());

	}

	@Test(timeout = 1000)
	public void testUpgradeFighterMethod_PhysicalDamage() throws Exception {

		Earthling fighter1 = new Earthling("fighter1", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		Earthling fighter2 = new Earthling("fighter2", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();

		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		player.upgradeFighter(fighter1, 'P');
		assertEquals(
				"upgradeFighter method in class Player should increase the physicalDamage of the corresponding fighter "
						+ "with 50 points when the char input is P",
				90, player
						.getFighters().get(0).getPhysicalDamage());
	}

	@Test(timeout = 1000)
	public void testUpgradeFighterMethod_MaxKi() throws Exception {
		Earthling fighter1 = new Earthling("fighter1", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		Earthling fighter2 = new Earthling("fighter2", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();

		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		player.upgradeFighter(fighter2, 'K');
		assertEquals(
				"upgradeFighter method in class Player should increase the maxKi of the corresponding fighter "
						+ "with 1 ki bar when the char input is K",
				5, player
						.getFighters().get(1).getMaxKi());

	}

	@Test(timeout = 1000)
	public void testUpgradeFighterMethod_MaxStamina() throws Exception {
		Earthling fighter1 = new Earthling("fighter1", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		Earthling fighter2 = new Earthling("fighter2", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();

		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		player.upgradeFighter(fighter2, 'S');
		assertEquals(
				"upgradeFighter method in class Player should increase the maxStamina of the corresponding fighter "
						+ "with 1 stamina bar when the char input is S",
				6,
				player.getFighters().get(1).getMaxStamina());

		fighter2.setAbilityPoints(0);
		player.upgradeFighter(fighter2, 'S');
		assertEquals(
				"upgradeFighter method in class Player should not apply any upgrading if the ability points is zero ",
				6, player.getFighters().get(1).getMaxStamina());
	}

	@Test(timeout = 1000)
	public void testUpgradeFighterMethod_HealthPoints() throws Exception {

		Earthling fighter1 = new Earthling("fighter1", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		Earthling fighter2 = new Earthling("fighter2", 1, 1, 10, 1250, 50, 40,
				10, 4, 5, null, null);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();

		fighters.add(fighter1);
		fighters.add(fighter2);

		Player player = new Player("player");
		player.setFighters(fighters);

		player.upgradeFighter(fighter1, 'H');
		assertEquals(
				"upgradeFighter method in class Player should increase the maxHealthpoints of the corresponding fighter "
						+ "with 50 points when the char input is H",
				1300,
				player.getFighters().get(0).getMaxHealthPoints());
		assertEquals(
				"upgradeFighter method in class Player should not affect any attributes other than the maxHealthpoints when the char input is H",
				50, player.getFighters().get(0).getBlastDamage());
		assertEquals(
				"upgradeFighter method in class Player should not affect any attributes other than the maxHealthpoints when the char input is H",
				40, player.getFighters().get(0).getPhysicalDamage());
		assertEquals(
				"upgradeFighter method in class Player should not affect any attributes other than the maxHealthpoints when the char input is H",
				4, player.getFighters().get(0).getMaxKi());
		assertEquals(
				"upgradeFighter method in class Player should not affect any attributes other than the maxHealthpoints when the char input is H",
				5, player.getFighters().get(0).getMaxStamina());
	}

	@Test(timeout = 1000)
	public void testSetHealthPointsMethodBoundaries() throws Exception {
		PlayableFighter fighter = new Earthling("Goku", 1, 1, 1, 1000, 1, 1, 1,
				1, 1, null, null);
		fighter.setHealthPoints(500);
		assertEquals(
				"The healthPoints should be correctly set according to the input of method setHealthPoints",
				500, fighter.getHealthPoints());

		fighter.setHealthPoints(2000);
		assertEquals(
				"The healthPoints cannot be set to a number that exceeds maxHealthPoints",
				fighter.getMaxHealthPoints(), fighter.getHealthPoints());

		fighter.setHealthPoints(-1);
		assertEquals(
				"The healthPoints cannot be set to a number that is less than 0",
				0, fighter.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testSetStaminaMethodBoundaries() throws Exception {
		PlayableFighter fighter = new Earthling("Goku", 1, 1, 1, 1, 1, 1, 1, 1,
				5, null, null);
		fighter.setStamina(3);
		assertEquals(
				"The stamina should be correctly set according to the input of method setStamina",
				3, fighter.getStamina());

		fighter.setStamina(10);
		assertEquals(
				"The stamina cannot be set to a number that exceeds maxStamina",
				fighter.getMaxStamina(), fighter.getStamina());

		fighter.setStamina(-1);
		assertEquals(
				"The stamina cannot be set to a number that is less than 0", 0,
				fighter.getStamina());

	}

	@Test(timeout = 1000)
	public void testgetMaxFighterLevelMethod() throws Exception {
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(new Earthling("fighter1", 1, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));
		fighters.add(new Earthling("fighter2", 3, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));
		fighters.add(new Earthling("fighter3", 2, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));
		fighters.add(new Earthling("fighter4", 6, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));
		fighters.add(new Earthling("fighter5", 7, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));
		fighters.add(new Earthling("fighter6", 5, 1, 1, 1, 1, 1, 1, 1, 1, null,
				null));

		Player player = new Player("player");
		player.setFighters(fighters);
		player.setActiveFighter(fighters.get(0)); //new line
		assertEquals(
				"getMaxFighterLevel method in class Player should return the maximum level from the arraylist of fighters",
				7, player.getMaxFighterLevel());
	}

	@Test(timeout = 1000)
	public void testgetActiveFighterMethod() throws Exception {
		Player player = new Player("player");
		player.createFighter('E', "fighter1");
		player.createFighter('E', "fighter2");

		assertEquals(
				"getActiveFighter method in class Player should initially "
						+ "return the first fighter in the list of fighters",
				player.getFighters().get(0), player.getActiveFighter());

		player.setActiveFighter(player.getFighters().get(1));
		assertEquals(
				"getActiveFighter method in class Player should return the active fighter if there is an active one",
				player.getFighters().get(1), player.getActiveFighter());
	}

	@Test(timeout = 1000)
	public void testOnAttackerTurn_OnDefenderTurnMethods_Earthling() throws Exception {
		Earthling earthling = new Earthling("Earthling");
		earthling.setStamina(0);
		earthling.setKi(0);

		earthling.onAttackerTurn();
		assertEquals(
				"OnAttackerTurn should add one stamina for an earthling fighter",
				1, earthling.getStamina());
		assertEquals(
				"OnAttackerTurn should add one ki bar for an earthling fighter",
				1, earthling.getKi());

		earthling.onDefenderTurn();
		assertEquals(
				"OnDefenderTurn should add one stamina for an earthling fighter",
				2, earthling.getStamina());

	}

	@Test(timeout = 1000)
	public void testOnAttackerTurn_OnDefenderTurnMethods_Namekian() throws Exception {
		Namekian namekian = new Namekian("Namekian"); // 0 50 1350 3 5
		namekian.setStamina(0);
		namekian.setHealthPoints(1000);

		namekian.onAttackerTurn();
		assertEquals(
				"OnAttackerTurn should add one stamina for a namekian fighter",
				1, namekian.getStamina());
		assertEquals("OnAttackerTurn should add 50 hp for a namekian fighter",
				1050, namekian.getHealthPoints());

		namekian.onDefenderTurn();
		assertEquals(
				"OnDefenderTurn should add one stamina for a namekian fighter",
				2, namekian.getStamina());
		assertEquals("OnDefenderTurn should add 50 hp for a namekian fighter",
				1100, namekian.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testOnAttackerTurn_OnDefenderTurnMethods_Frieza() throws Exception {
		Frieza frieza = new Frieza("Frieza");
		frieza.setStamina(0);
		frieza.setHealthPoints(1100);

		frieza.onAttackerTurn();
		assertEquals(
				"OnAttackerTurn should add one stamina for a frieza fighter",
				1, frieza.getStamina());
		assertEquals(
				"OnAttackerTurn should not affect any attributes other than the stamina for a frieza fighter",
				0, frieza.getKi());
		assertEquals(
				"OnAttackerTurn should not affect any attributes other than the stamina for a frieza fighter",
				1100, frieza.getHealthPoints());

		frieza.onDefenderTurn();
		assertEquals(
				"OnDefenderTurn should add one stamina for a frieza fighter",
				2, frieza.getStamina());
		assertEquals(
				"OnDefenderTurn should not affect any attributes other than the stamina for a frieza fighter",
				0, frieza.getKi());
		assertEquals(
				"OnDefenderTurn should not affect any attributes other than the stamina for a frieza fighter",
				1100, frieza.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testOnAttackerTurn_OnDefenderTurnMethods_NonPlayable() throws Exception {
		NonPlayableFighter foe = new NonPlayableFighter("foe", 1, 1000, 1, 1,
				10, 10, false, null, null);
		foe.setStamina(0);

		foe.onAttackerTurn();
		assertEquals("OnAttackerTurn should add one stamina for any Non Playable fighter", 1, foe.getStamina());

		foe.onDefenderTurn();
		assertEquals("onDefenderTurn should add one stamina for any Non Playable fighter", 2, foe.getStamina());

	}

	@Test(timeout = 1000)
	public void testResetPlayerPosition() throws Exception {
		Game g = new Game();
		g.getWorld().resetPlayerPosition();
		assertEquals(
				"resetPlayerPosition method in class World should reset the player's row to 9",
				9, g.getWorld().getPlayerRow());
		assertEquals(
				"resetPlayerPosition method in class World should reset the player's column to 9",
				9, g.getWorld().getPlayerColumn());

	}

	@Test(timeout = 1000)
	public void testMoveUp() throws Exception {
		Game g;

		g = new Game();
		World w = g.getWorld();
		clearWorld(w.getMap());
		Field f;
		f = w.getClass().getDeclaredField("playerRow");
		f.setAccessible(true);
		f.set(w, 1);

		f = w.getClass().getDeclaredField("playerColumn");
		f.setAccessible(true);
		f.set(w, 8);

		int oldPlayerRow = g.getWorld().getPlayerRow();
		g.getWorld().moveUp();
		int newPlayerRow = g.getWorld().getPlayerRow();
		assertEquals(
				"moveUp method in class World should move the player one cell up if possible",
				oldPlayerRow - 1, newPlayerRow);

	}

	@Test(timeout = 1000)
	public void testMoveUpOutOfBoundaries() throws Exception {
		Game g;

		g = new Game();
		World w = g.getWorld();
		clearWorld(w.getMap());
		Field f;
		f = w.getClass().getDeclaredField("playerRow");
		f.setAccessible(true);
		f.set(w, 0);

		f = w.getClass().getDeclaredField("playerColumn");
		f.setAccessible(true);
		f.set(w, 8);

		g.getWorld().moveUp();
		int playerRow = g.getWorld().getPlayerRow();
		System.out.println("after: " + playerRow);
		assertEquals(
				"moveUp method in class World should handle out of boundaries",
				playerRow, 0);

	}

	@Test(timeout = 1000)
	public void testMoveRight() throws Exception {
		Game g = new Game();
		World w = g.getWorld();
		clearWorld(w.getMap());
		Field f;
		f = w.getClass().getDeclaredField("playerRow");
		f.setAccessible(true);
		f.set(w, 9);

		f = w.getClass().getDeclaredField("playerColumn");
		f.setAccessible(true);
		f.set(w, 1);

		int oldPlayerColumn = g.getWorld().getPlayerColumn();
		g.getWorld().moveRight();
		int newPlayerColumn = g.getWorld().getPlayerColumn();
		assertEquals(
				"moveRight method in class World should move the player one cell to the right if possible",
				oldPlayerColumn + 1, newPlayerColumn);

	}

	@Test(timeout = 1000)
	public void testMoveRightOutOfBoundaries() throws Exception {
		Game g = new Game();
		World w = g.getWorld();
		clearWorld(w.getMap());
		Field f;
		f = w.getClass().getDeclaredField("playerRow");
		f.setAccessible(true);
		f.set(w, 9);

		f = w.getClass().getDeclaredField("playerColumn");
		f.setAccessible(true);
		f.set(w, 9);

		g.getWorld().moveRight();
		int playerColumn = g.getWorld().getPlayerColumn();
		assertEquals(
				"moveRight method in class World should handle out of boundaries",
				9, playerColumn);
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_ChangesXP() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);

		Player p = g.getPlayer();
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 1000);
		ArrayList<SuperAttack> fightersSuperAttacks = new ArrayList<SuperAttack>();
		fightersSuperAttacks.add(k);
		ArrayList<UltimateAttack> fighterUltimateAttacks = new ArrayList<UltimateAttack>();
		fighterUltimateAttacks.add(f);

		Namekian n = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6,
				600, fightersSuperAttacks, fighterUltimateAttacks);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(n);
		p.setFighters(fighters);

		p.setActiveFighter(n);
		int oldXP = p.getActiveFighter().getXp();

		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		// after winning we should test:
		// 1. the xp points are set to foe's level *5
		assertEquals("The Xp points of the player is not changed correctly after defeating a weak foe",
				(foe.getLevel() * 5) + oldXP, p.getActiveFighter().getXp());

	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_AddsNonRedundantSuperAttacks() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		assertFalse(
				"On winning, the winner shouldn't gain a super attack that he already has",
				containDuplicatesSuperAttacks(p.getSuperAttacks()));

	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_AddsNonRedundantUltimateAttacks() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		assertFalse(
				"On winning, the winner shouldn't gain an ultimate attack that he already has",
				containDuplicatesUltimateAttacks(p.getUltimateAttacks()));
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_AddsSuperAttacks() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		ArrayList<SuperAttack> newPlayerSuperAttacks = new ArrayList<>();
		for (SuperAttack foSuperAttack : p.getSuperAttacks())
			newPlayerSuperAttacks.add(foSuperAttack);

		for (SuperAttack foSuperAttack : foeSuperAttacks)
			if (!newPlayerSuperAttacks.contains(foSuperAttack))
				newPlayerSuperAttacks.add(foSuperAttack);

		assertTrue(
				"On winning, the fighter should gain the foe's super attacks",
				superAttacksListsAreEqual(p.getSuperAttacks(),
						newPlayerSuperAttacks));
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_AddsUltimateAttacks() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		ArrayList<UltimateAttack> newPlayerUltimateAttacks = new ArrayList<>();
		for (UltimateAttack ulAttack : p.getUltimateAttacks())
			newPlayerUltimateAttacks.add(ulAttack);

		for (UltimateAttack ulSuperAttack : foeUltimateAttacks)
			if (!newPlayerUltimateAttacks.contains(ulSuperAttack))
				newPlayerUltimateAttacks.add(ulSuperAttack);
		assertTrue(
				"On winning, the fighter should gain the foe's ultimate attacks",
				ultimateAttacksListsAreEqual(p.getUltimateAttacks(),
						newPlayerUltimateAttacks));

	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_AddsAbilityPoints() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();
		int oldAbilityPoints = p.getActiveFighter().getAbilityPoints();
		int oldLevel = p.getActiveFighter().getLevel();

		// gain 2 ability points, in case the gained xp causes the fighter to
		// level up
		if (p.getActiveFighter().getLevel() - oldLevel == 1)
			assertEquals(
					"The fighter should gain 2 ability points, in case the gained xp causes the fighter to level up",
					oldAbilityPoints + 1, p.getActiveFighter().getAbilityPoints());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnWinScenarioFighterDefeatsWeakFoe_RemovesFoeFromCell() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		// weakFoe cell is turned to an empty cell
		assertEquals(
				"On winning a weak foe, the old foe cell should be turned to an empty cell",
				EmptyCell.class, g.getWorld().getMap()[8][9].getClass());
	}

	@SuppressWarnings("unused")
	public void testOnWinScenarioFighterDefeatsWeakFoe_ChangesGameState() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 900, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);
		testOnWinScenarioFighterDefeatsWeakFoeHelper(g, foe);
		Player p = g.getPlayer();

		assertEquals(
				"Game State should be changed to WORLD when a battle ends",
				GameState.WORLD, g.getState());
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFoeDefeatsPlayer_ResetPlayerPosition() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 3000, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);

		testOnWinScenarioFoeDefeatsPlayerHelper(g, foe);

		// test that player position is reset
		boolean playerPositionReset = g.getWorld().getPlayerColumn() == 9
				&& g.getWorld().getPlayerRow() == 9;
		assertTrue(
				"on winning a fighter, player position should be reset to column 9 and row 9",
				playerPositionReset);
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFoeDefeatsPlayer_FoeRemainsInCell() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 3000, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);

		testOnWinScenarioFoeDefeatsPlayerHelper(g, foe);
		// test that the map is unchanged
		assertEquals(
				"On winning a fighter, the foe should remain in it's position in the map",
				FoeCell.class, (g.getWorld().getMap()[8][9]).getClass());
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFoeDefeatsPlayer_WorldNOTChanged() throws Exception {

		Game g = new Game();
		World oldWorld = g.getWorld();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 3000, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);

		testOnWinScenarioFoeDefeatsPlayerHelper(g, foe);

		assertFalse("On winning a fighter, the world shouldn't be changed",
				TwoMapsDifferent(oldWorld.getMap(), g.getWorld().getMap()));
	}

	@Test(timeout = 1000)
	public void testOnWinScenarioFoeDefeatsPlayer_ChangesGameState() throws Exception {

		Game g = new Game();
		ArrayList<SuperAttack> foeSuperAttacks = new ArrayList<SuperAttack>();
		foeSuperAttacks.add(new SuperAttack("Kamehameha", 250));
		foeSuperAttacks.add(new MaximumCharge());

		ArrayList<UltimateAttack> foeUltimateAttacks = new ArrayList<UltimateAttack>();
		foeUltimateAttacks.add(new UltimateAttack("Super Kamehameha", 1000));
		foeUltimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));

		NonPlayableFighter foe = new NonPlayableFighter("Goku", 10, 3000, 350,
				400, 5, 6, false, foeSuperAttacks, foeUltimateAttacks);

		testOnWinScenarioFoeDefeatsPlayerHelper(g, foe);

		assertEquals(
				"Game State should be changed to WORLD when a battle ends",
				GameState.WORLD, g.getState());

	}

	@Test(timeout = 1000)
	public void testOnCollectibleFoundUpdatesVariablesCorrectly_SenzuBean()
			throws Exception {
		Game g = new Game();
		Player p = g.getPlayer();
		try {
			int oldSenzuBeans = p.getSenzuBeans();
			g.onCollectibleFound(Collectible.SENZU_BEAN);
			assertEquals(
					"The method onCellCollectible should update the senzu beans count correctly",
					oldSenzuBeans + 1, p.getSenzuBeans());
		} catch (NullPointerException e) {
			fail("Player should be intialized in the Game class constructor");
		}

	}

	@Test(timeout = 1000)
	public void testOnCollectibleFoundUpdatesVariablesCorrectly_DragonBall()
			throws Exception {
		Game g = new Game();
		Player p = g.getPlayer();

		try {
			int oldDragonBallsCount = p.getDragonBalls();
			g.onCollectibleFound(Collectible.DRAGON_BALL);
			assertEquals(
					"The method onCellCollectible should update the dragon balls count correctly",
					oldDragonBallsCount + 1, p.getDragonBalls());
		} catch (NullPointerException e) {
			fail("Player should be intialized in the Game class");
		}
	}

	@Test(timeout = 1000)
	public void testGameStateChangesToWorldOnWishGranted() throws Exception {
		Game g = new Game();
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		SuperAttack s1 = new SuperAttack("kamehameha", 250);
		supers.add(s1);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		UltimateAttack u = new UltimateAttack("final flash", 450);
		ultimates.add(u);

		g.getPlayer().setActiveFighter(new Earthling("Earthlig"));

		Dragon d = new Dragon("Porunga", supers, ultimates, 3, 5);
		g.onWishChosen(new DragonWish(d, DragonWishType.ABILITY_POINTS, 3));

		assertEquals(
				"Game State should be changed to WORLD onWishChosen method in Game class",
				GameState.WORLD, g.getState());

	}

	@Test(timeout = 1000)
	public void testDragonBallsSubtractedOnWishGranted() throws Exception {
		Game g = new Game();
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		SuperAttack s1 = new SuperAttack("kamehameha", 250);
		supers.add(s1);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		UltimateAttack u = new UltimateAttack("final flash", 450);
		ultimates.add(u);

		g.getPlayer().setActiveFighter(new Earthling("Earthlig"));

		Dragon d = new Dragon("Porunga", supers, ultimates, 3, 5);
		g.onWishChosen(new DragonWish(d, DragonWishType.ABILITY_POINTS, 3));
		assertEquals("The dragon balls should be reset to zero after colelcting seven dragon balls and getting a wish",
				0, g.getPlayer().getDragonBalls());
	}

	@Test(timeout = 1000)
	public void testGameStateChangesToDragonOnCollectingSevenDragonBalls()
			throws Exception {
		Game g = new Game();
		World w = g.getWorld();
		w.getMap()[8][9] = new CollectibleCell(Collectible.DRAGON_BALL);
		Field[] fields = Cell.class.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(CellListener.class)) {
				i = j;
				break;
			}
		}

		if (i != -1) {
			Field f = Cell.class.getDeclaredFields()[i];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = Cell.class
					.getMethod(methodName, CellListener.class);
			try {
				setter.invoke(w.getMap()[8][9], w);
			} catch (Exception e) {
				fail("The class: " + w.getMap()[8][9].getClass()
						+ " should have a setter method for the listener");
			}
		}
		g.getPlayer().setDragonBalls(6);
		w.moveUp();

		assertEquals(
				"Game State should be changed to DRAGON onDragonCalled method in Game class",
				GameState.DRAGON, g.getState());

	}

	@Test(timeout = 1000)
	public void testGetWishesHaveSenzuBeans() throws Exception {

		DragonWish[] wishes = testGetWishesHelper();
		for (int i = 0; i < wishes.length; i++) {
			if (wishes[i].getType() == DragonWishType.SENZU_BEANS)
				assertEquals(
						"method getWishes should return an array where one of the wishes have the correct senzubeans of the dragon",
						10, wishes[i].getSenzuBeans());

		}
	}

	@Test(timeout = 1000)
	public void testGetWishesHaveAbilityPoints() throws Exception {

		DragonWish[] wishes = testGetWishesHelper();
		for (int i = 0; i < wishes.length; i++) {
			if (wishes[i].getType() == DragonWishType.ABILITY_POINTS)
				assertEquals(
						"method getWishes should return an array where one of the wishes have the correct ability points of the dragon",
						5, wishes[i].getAbilityPoints());
		}
	}

	@Test(timeout = 1000)
	public void testGetWishesHaveSuperAttack() throws Exception {

		DragonWish[] wishes = testGetWishesHelper();
		ArrayList<SuperAttack> superAttacks = new ArrayList<>();
		superAttacks.add(new SuperAttack("Big Bang Kamehameha", 350));
		superAttacks.add(new SuperAttack("Emperor's Death Beam", 350));
		superAttacks.add(new SuperAttack("Demon Wave", 300));
		superAttacks.add(new SuperAttack("Guilty Flash", 300));
		for (int i = 0; i < wishes.length; i++) {
			if (wishes[i].getType() == DragonWishType.SUPER_ATTACK) {

				boolean exists = false;
				for (SuperAttack s : superAttacks)
					if (s.getName()
							.equals(wishes[i].getSuperAttack().getName()))
						exists = true;
				assertTrue(
						"method getWishes should return an array where one of the wishes have a super attack from the dragon super attacks",
						exists);

			}
		}
	}

	@Test(timeout = 1000)
	public void testGetWishesHaveUltimateAttack() throws Exception {

		DragonWish[] wishes = testGetWishesHelper();

		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<>();
		ultimateAttacks
				.add(new UltimateAttack("Super Big Bang Kamehameha", 350));
		ultimateAttacks.add(new UltimateAttack("Final Shine Attack", 350));
		ultimateAttacks.add(new UltimateAttack("Final Galick Gun", 300));
		ultimateAttacks.add(new UltimateAttack("Explosive Demon Wave", 300));
		for (int i = 0; i < wishes.length; i++) {
			if (wishes[i].getType() == DragonWishType.ULTIMATE_ATTACK) {
				boolean exists = false;
				for (UltimateAttack s : ultimateAttacks)
					if (s.getName().equals(
							wishes[i].getUltimateAttack().getName()))
						exists = true;
				assertTrue(
						"method getWishes should return an array where one of the wishes have an ultimate attack from the dragon ultimate attacks",
						exists);
			}

		}
	}

	@Test(timeout = 1000)
	public void testGetWishesChooseRandomSuperAttack() throws Exception {

		DragonWish[] wishes;

		// test the chosen super and ultimate attacks are random
		ArrayList<SuperAttack> randomSuperAttacks = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			wishes = testGetWishesHelper();
			for (DragonWish w : wishes) {
				if (w.getType() == DragonWishType.SUPER_ATTACK)
					randomSuperAttacks.add(w.getSuperAttack());
			}
		}
		boolean random = false;
		for (int i = 0; i < randomSuperAttacks.size() - 1; i++)
			if (!randomSuperAttacks.get(i).getName()
					.equals(randomSuperAttacks.get(i + 1).getName()))
				random = true;
		assertTrue(
				"The method getWishes in Dragon class should choose a random super attack from the dragon,if any",
				random);
	}

	@Test(timeout = 1000)
	public void testGetWishesChooseRandomUltimateAttack() throws Exception {

		DragonWish[] wishes;

		ArrayList<UltimateAttack> randomUltimateAttacks = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			wishes = testGetWishesHelper();
			for (DragonWish w : wishes) {
				if (w.getType() == DragonWishType.ULTIMATE_ATTACK)
					randomUltimateAttacks.add(w.getUltimateAttack());
			}
		}
		boolean random = false;
		for (int i = 0; i < randomUltimateAttacks.size() - 1; i++)
			if (!randomUltimateAttacks.get(i).getName()
					.equals(randomUltimateAttacks.get(i + 1).getName()))
				random = true;
		assertTrue(
				"The method getWishes in Dragon class should choose a random ultimate attack from the dragon, if any",
				random);

	}

	@Test(timeout = 1000)
	public void testDirectAttackWithSuperAttack() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 400, 5, 6, true, null, null);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		Frieza e = new Frieza("frieza");
		e.setSuperAttacks(superAttacks);
		strong.setSuperAttacks(superAttacks);
		Battle b = new Battle(e, strong);
		b.start();
		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());
		b.attack(e.getSuperAttacks().get(0));
		assertEquals(
				"When me attacks the foe that is not blocking with Super attack , the health points of the foe should be reduced according to the game rules",
				2550, strong.getHealthPoints());

		b.attack(strong.getSuperAttacks().get(0));
		assertEquals(
				"When foe attacks me  while not blocking with with Super attack , the health points of the foe should be reduced according to the game rules",
				50, e.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testSuperAttackConsumesKiCorrectly() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 400, 5, 6, true, null, null);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		Frieza e = new Frieza("frieza");
		e.setSuperAttacks(superAttacks);
		strong.setSuperAttacks(superAttacks);
		Game g = new Game();
		Battle b = new Battle(e, strong);
		invokeSetter(b, BattleListener.class, g);
		//b.setListener(g);
		b.start();
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		int before = e.getKi();
		b.attack(e.getSuperAttacks().get(0));
		assertEquals(
				"When me attacks foe with Super attack , me's ki should be reduced by 1",
				before - 1, e.getKi());
		before = strong.getKi();
		b.attack(strong.getSuperAttacks().get(0));
		assertEquals(
				"When foe attacks me with Super attack , foe's Ki should be reduced by 1",
				before - 1, strong.getKi());
	}

	@Test(timeout = 1000)
	public void testDirectAttackWithUltimateAttack() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 100, 5, 6, true, null, null);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));
		Majin e = new Majin("majin");
		e.setUltimateAttacks(ultimateAttacks);
		ArrayList<UltimateAttack> ultimateAttacks2 = new ArrayList<UltimateAttack>();
		ultimateAttacks2.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks2);
		Battle b = new Battle(e, strong);
		b.start();
		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(e.getUltimateAttacks().get(0));
		assertEquals(
				"When me attacks the foe that is not blocking with Ultimate attack , the health points of the foe should be reduced according to game rules",
				2150, strong.getHealthPoints());

		b.attack(strong.getUltimateAttacks().get(0));

		assertEquals(
				"When foe attacks me  while not blocking with Ultimate attack , the health points of me should be reduced according to the game rules",
				250, e.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testBlockVariables() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 400, 5, 6, true, null, null);
		Namekian e = new Namekian("namekian");
		Battle b = new Battle(e, strong);
		b.start();
		b.block();

		assertTrue(
				"if me blocks in a battle , the meBlocking variable should change to true",
				b.isMeBlocking());
		assertFalse(
				"if me blocks in a battle , this should not affect weather foe is blocking or not",
				b.isFoeBlocking());

		b.block();
		assertTrue(
				"if foe blocks in a battle , the foeBlocking variable should change to true",
				b.isFoeBlocking());
		assertFalse(
				"if foe blocks in a battle , this should not affect weather me is blocking or not",
				b.isMeBlocking());
	}

	@Test(timeout = 1000)
	public void testBlockConsumesStaminaCorrectly() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 300, 5, 6, true, null, null);
		Frieza e = new Frieza("King Cold");
		Battle b = new Battle(e, strong);
		b.start();
		b.block();
		int before = e.getHealthPoints();
		b.attack(new PhysicalAttack());
		assertEquals(
				"if foe attacks me while me was blocking ,me's Stamina should be reduced according to the game rules",
				1, e.getStamina());
		assertEquals(
				"if foe attacks me while me was blocking and me's stamina didn't reach zero , me's healthpoints should'nt get reduced ",
				before, e.getHealthPoints());
		b.attack(new PhysicalAttack());
		b.block();
		before = strong.getHealthPoints();
		b.attack(new PhysicalAttack());
		assertEquals(
				"if me attacks foe while foe was blocking ,foe's Stamina should be reduced according to the game rules",
				5, strong.getStamina());
		assertEquals(
				"if me attacks foe while foe was blocking and foe's stamina didn't reach zero , foe's healthpoints should'nt get reduced ",
				before, strong.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testBlockingUltimateAttack() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 100, 5, 4, true, null, null);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Spirit Bomb", 500));
		Frieza e = new Frieza("Cooler");
		e.setUltimateAttacks(ultimateAttacks);
		ArrayList<UltimateAttack> ultimateAttacks2 = new ArrayList<UltimateAttack>();
		ultimateAttacks2.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks2);
		Battle b = new Battle(e, strong);
		b.start();
		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());

		b.attack(new PhysicalAttack());
		b.block();
		b.attack(strong.getUltimateAttacks().get(0));
		assertEquals(
				"When foe attacks me with Ultimate attack while me is blocking and me's stamina reached zero , the health points of me should be reduced according to the game rules",
				250, e.getHealthPoints());
		assertEquals(
				"if foe attacks me while me was blocking ,me's Stamina should be reduced according to the game rules",
				1, e.getStamina());
		b.attack(new PhysicalAttack());
		b.block();
		b.attack(e.getUltimateAttacks().get(0));

		assertEquals(
				"When me attacks foe with Ultimate attack while foe is blocking, the health points of foe should be reduced according to the game rules",
				2325, strong.getHealthPoints());
		assertEquals(
				"if me attacks foe while foe was blocking ,foe's Stamina should be reduced according to the game rules",
				1, strong.getStamina());
	}

	@Test(timeout = 1000)
	public void testBlockEndsAfterOneTurn() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		Battle b = new Battle(e, strong);
		b.start();
		b.block();
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		int sbefore = e.getStamina();
		int hbefore = e.getHealthPoints();
		b.attack(new PhysicalAttack());
		assertEquals(
				"blocking only take effect for one turn therefore , stamina should not be reduced after that turn",
				sbefore, e.getStamina());
		assertEquals(
				"blocking only take effect for one turn therefore , any attack should reduce from the health points directly",
				hbefore - 150, e.getHealthPoints());
		b.attack(new PhysicalAttack());
		b.block();
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		sbefore = strong.getStamina();
		hbefore = strong.getHealthPoints();
		b.attack(new PhysicalAttack());
		assertEquals(
				"blocking takes effect for only one turn therefore , stamina should not be reduced after that turn",
				sbefore, strong.getStamina());
		assertEquals(
				"blocking takes effect for only one turn therefore , any attack should reduce from the health points directly",
				hbefore - 125, strong.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testBlockVariablesAfterMoreThanOneTurn() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				350, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		Battle b = new Battle(e, strong);
		b.start();
		b.block();
		b.attack(new PhysicalAttack());
		assertFalse(
				"blocking takes effect for only one turn . After it ends , the blocking variable should be reset to false",
				b.isMeBlocking());
		b.attack(new PhysicalAttack());
		b.block();
		b.attack(new PhysicalAttack());
		assertFalse(
				"blocking takes effect for only one turn . After it ends , the blocking variable should be reset to false",
				b.isFoeBlocking());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testGetAppliedDamage_PhysicalAttack() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				100, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		PhysicalAttack a = new PhysicalAttack();
		assertEquals(
				"The output of getAppliedDamage method of a physical attack is incorrect ",
				125, a.getAppliedDamage(e));
	}

	@Test(timeout = 1000)
	public void testGetAppliedDamage_SuperAttack() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				100, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);

		SuperAttack s = new SuperAttack("Kamehameha", 250);
		assertEquals(
				"The output of getAppliedDamage method of a super attack is incorrect ",
				350, s.getAppliedDamage(strong));
	}

	@Test(timeout = 1000)
	public void testGetAppliedDamage_UltimateAttack() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				100, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);

		UltimateAttack u = new UltimateAttack("Super Kamehameha", 450);
		assertEquals(
				"The output of getAppliedDamage method of an ultimate attack is incorrect ",
				550, u.getAppliedDamage(strong));
	}

	@Test(timeout = 1000)
	public void testGetAppliedDamage_MaximumCharge() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				100, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		assertEquals(
				"The output of getAppliedDamage method of MaximumCharge is incorrect ",
				0, new MaximumCharge().getAppliedDamage(strong));
	}

	@Test(timeout = 1000)
	public void testGetAppliedDamage_SuperSaiyan() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				100, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		assertEquals(
				"The output of getAppliedDamage method of SuperSaiyan is incorrect ",
				0, new SuperSaiyan().getAppliedDamage(strong));
	}

	@SuppressWarnings("unused")

	@Test(timeout = 1000)
	public void testOnUseMethodOnSuperAttack_NonBlocking() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		strong.setSuperAttacks(superAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(1);
		strong.getSuperAttacks().get(0).onUse(strong, e, false);
		assertEquals(
				"the functionality of onUse method of a super attack is incorrect in case of defender not blocking",
				650, e.getHealthPoints());

	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnSuperAttack_Blocking() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		strong.setSuperAttacks(superAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(1);
		strong.getSuperAttacks().get(0).onUse(strong, e, true);

		assertEquals(
				"the functionality of onUse method of a super attack is incorrect in case of defender is blocking",
				1050, e.getHealthPoints());

		assertEquals(
				"When calling onUse method on a super attack , it should reduce one Ki from the one who used it",
				0, e.getKi());
		int healthBefore = e.getHealthPoints();
		assertEquals(
				"if you call onUse method on a super Attack while its performer does not have at leasr 1 ki bar , the attack should not be performed",
				healthBefore, e.getHealthPoints());
	}

	@SuppressWarnings({ "unused" })
	@Test(timeout = 1000)
	public void testOnUseMethodOnSuperAttack_KiConsumption() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		strong.setSuperAttacks(superAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(1);
		strong.getSuperAttacks().get(0).onUse(strong, e, true);
		assertEquals(
				"When calling onUse method on a super attack , it should reduce one Ki from the one who used it",
				0, strong.getKi());
		int healthBefore = e.getHealthPoints();
		assertEquals(
				"if you call onUse method on a super Attack while its performer does not have at leasr 1 ki bar , the attack should not be performed",
				healthBefore, e.getHealthPoints());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnSuperAttack_MinimumKi() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<SuperAttack> superAttacks = new ArrayList<SuperAttack>();
		superAttacks.add(new SuperAttack("Kamehameha", 250));
		strong.setSuperAttacks(superAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(1);
		strong.getSuperAttacks().get(0).onUse(strong, e, true);
		int healthBefore = e.getHealthPoints();
		assertEquals(
				"if you call onUse method on a super Attack while its performer does not have at leasr 1 ki bar , the attack should not be performed",
				healthBefore, e.getHealthPoints());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnUltimateAttack_notBlocking() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(3);
		strong.getUltimateAttacks().get(0).onUse(strong, e, false);
		assertEquals(
				"the functionality of onUse method of a ultimate attack is incorrect in case of defender not blocking",
				450, e.getHealthPoints());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnUltimateAttack_Blocking() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(3);
		int healthBefore = e.getHealthPoints();
		strong.getUltimateAttacks().get(0).onUse(strong, e, true);
		assertEquals(
				"the functionality of onUse method of a ultimate attack is incorrect in case of defender is blocking",
				healthBefore - 250, e.getHealthPoints());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnUltimateAttack_KiConsumption() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(3);
		strong.getUltimateAttacks().get(0).onUse(strong, e, true);
		assertEquals(
				"When calling onUse method on a ultimate attack , it should reduce three Ki from the one who used it",
				0, strong.getKi());
	}

	@SuppressWarnings("unused")
	@Test(timeout = 1000)
	public void testOnUseMethodOnUltimateAttack_MinimumKi() throws Exception {
		Player p = new Player("7amada");
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		p.getFighters().add(e);
		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<UltimateAttack>();
		ultimateAttacks.add(new UltimateAttack("Super Kamehameha", 450));
		strong.setUltimateAttacks(ultimateAttacks);
		Battle b = new Battle(e, strong);
		strong.setKi(3);
		int healthBefore = strong.getHealthPoints();
		strong.getUltimateAttacks().get(0).onUse(strong, e, true);
		healthBefore = e.getHealthPoints();
		assertEquals(
				"if you call onUse method on an ultimate Attack while its performer does not have at least 3 ki bars , the attack should not be performed",
				healthBefore, e.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testOnUseMaximumCharge_Ki() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		MaximumCharge m = new MaximumCharge();
		e.getSuperAttacks().add(m);
		m.onUse(e, strong, false);
		assertEquals(
				"When calling onUse method on a Maximum Charge , it adds 3 Ki bars to the performer",
				3, e.getKi());
	}

	@Test(timeout = 1000)
	public void testOnUseMaximumCharge_Damage() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Frieza e = new Frieza("frieza");
		MaximumCharge m = new MaximumCharge();
		e.getSuperAttacks().add(m);

		int hpprev = strong.getHealthPoints();

		m.onUse(e, strong, false);

		assertEquals(
				"When calling onUse method on a Maximum Charge , it wont inflect damage on the attacked target",
				hpprev, strong.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testOnUseSuperSaiyan_Ki() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Saiyan e = new Saiyan("Vegeta");
		SuperSaiyan s = new SuperSaiyan();
		e.getUltimateAttacks().add(s);
		e.setKi(3);
		s.onUse(e, strong, false);
		assertEquals(
				"When onUse is called on SuperSaiyan , it wont consume any Ki from the attacker",
				3, e.getKi());
	}

	@Test(timeout = 1000)
	public void testOnUseSuperSaiyan_Transformed() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Saiyan e = new Saiyan("Vegeta");
		SuperSaiyan s = new SuperSaiyan();
		e.getUltimateAttacks().add(s);
		e.setKi(3);
		s.onUse(e, strong, false);
		assertTrue(
				"When onUse is called on SuperSaiyan, the attacker becomes transformed",
				e.isTransformed());
	}

	@Test(timeout = 1000)
	public void testOnUseSuperSaiyan_Damage() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 3000,
				200, 100, 5, 6, true, null, null);
		Saiyan e = new Saiyan("Vegeta");
		SuperSaiyan s = new SuperSaiyan();
		e.getUltimateAttacks().add(s);
		e.setKi(3);

		int hpprev = strong.getHealthPoints();

		s.onUse(e, strong, false);

		assertEquals("When calling onUse method on SuperSaiyan , it wont apply any damage on the attacked target",
				hpprev, strong.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testSuperSaiyan() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 5000,
				350, 100, 5, 6, true, null, null);
		Saiyan e = new Saiyan("Vegeta");
		e.getSuperAttacks().add(new SuperAttack("Galick Gun", 250));
		e.getUltimateAttacks().add(new SuperSaiyan());
		e.getUltimateAttacks().add(new UltimateAttack("Final Flash", 450));
		e.setMaxHealthPoints(5000);
		Battle b = new Battle(e, strong);
		b.start();
		b.attack(e.getUltimateAttacks().get(0));
		assertFalse("Super Saiyan requires 3 Ki bars to be activated",
				e.isTransformed());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		System.out.println(e.getKi());
		b.attack(e.getUltimateAttacks().get(0));
		assertTrue("Super Saiyan can be activated if the Saiyan has 3 Ki bars",
				e.isTransformed());
		assertEquals("Super Saiyan does not consume Ki", 2, e.getKi());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan , he loses one Ki bar each turn",
				2, e.getKi());
		b.attack(new PhysicalAttack());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan , he loses one Ki bar each turn",
				1, e.getKi());
		int healthBefore = strong.getHealthPoints();
		b.attack(e.getUltimateAttacks().get(1));
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan , his ultimate attacks does not require nor consumes Ki",
				0, e.getKi());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan , he loses one Ki bar each turn",
				0, e.getKi());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan, he can use his ultimate attacks even if he does not have 3 Ki bars",
				healthBefore - 750, strong.getHealthPoints());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan, all the damage he inflicts is increased by 25%",
				healthBefore - 750, strong.getHealthPoints());
		assertFalse(
				"When a Saiyan is transformed and his Ki reaches zero , the transformation ends",
				e.isTransformed());
		assertEquals(
				"When a Saiyan is transformed and his Ki reaches zero , the transformation ends and his stamina becomes zero",
				0, e.getStamina());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(e.getUltimateAttacks().get(0));
		b.attack(new PhysicalAttack());
		healthBefore = strong.getHealthPoints();
		b.attack(e.getSuperAttacks().get(0));
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan , his super attacks does not require nor consumes Ki",
				0, e.getKi());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan, he can use his super attacks even if he does not have 1 Ki bars",
				healthBefore - 500, strong.getHealthPoints());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan, all the damage he inflicts is increased by 25%",
				healthBefore - 500, strong.getHealthPoints());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(new PhysicalAttack());
		b.attack(e.getUltimateAttacks().get(0));
		b.attack(new PhysicalAttack());
		healthBefore = strong.getHealthPoints();
		b.attack(new PhysicalAttack());
		assertEquals(
				"When a Saiyan transforms to a Super Saiyan, all the damage he inflicts is increased by 25%",
				healthBefore - 187, strong.getHealthPoints());
	}

	@Test(timeout = 1000)
	public void testSwitchTurn() throws Exception {
		NonPlayableFighter strong = new NonPlayableFighter("Goku", 10, 5000,
				350, 100, 5, 6, true, null, null);
		Saiyan e = new Saiyan("Vegeta");

		Battle b = new Battle(e, strong);
		Method m = Battle.class.getDeclaredMethod("switchTurn");
		m.setAccessible(true);
		m.invoke(b);
		assertEquals(
				"Wrong functionality in switchTurn method in Battle class ",
				strong, b.getAttacker());
		assertEquals(
				"Wrong functionality in switchTurn method in Battle class ", e,
				b.getDefender());
	}

	@Test(timeout = 1000)
	public void testBlockMethodEndsTurn() throws Exception {
		hasCalled = false;
		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null)) {
			public void endTurn() {
				hasCalled = true;
			}
		};
		b.block();
		assertTrue("Block method in Battle class should end the turn",
				hasCalled);
	}

	@Test(timeout = 1000)
	public void testUseMethodEndsTurn() throws Exception {
		hasCalled = false;
		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null)) {
			public void endTurn() {
				hasCalled = true;
			}
		};
		PlayableFighter activeFighter = new Earthling("Goku", 1, 1, 1, 1, 1, 1,
				1, 1, 1, null, null);
		Player player = new Player("player", new ArrayList<PlayableFighter>(),
				new ArrayList<SuperAttack>(), new ArrayList<UltimateAttack>(),
				1, 1, activeFighter, 1);
		b.use(player, Collectible.SENZU_BEAN);
		assertTrue("Use method in Battle class should end the turn", hasCalled);
	}

	@Test(timeout = 1000)
	public void testPlayMethodEndsTurn() throws Exception {
		hasCalled = false;
		Battle b = new Battle(new Saiyan("saiyan"), new NonPlayableFighter(
				"foe", 1, 1, 1, 1, 1, 1, true, null, null)) {
			public void endTurn() {
				hasCalled = true;
			}
		};
		b.play();
		assertTrue("Play method in Battle class should end the turn", hasCalled);
	}

	@Test(timeout = 1000)
	public void testEndTurnMethodCallsOnAttacker_OnDefender() throws Exception {
		hasCalled = false;
		Saiyan s = new Saiyan("saiyan") {
			public void onAttackerTurn() {
				onAttackerCalled1 = true;
			}

			public void onDefenderTurn() {
				onDefenderCalled1 = true;
			}
		};

		NonPlayableFighter foe = new NonPlayableFighter("foe", 1, 1, 1, 1, 1,
				1, true, null, null) {
			public void onAttackerTurn() {
				onAttackerCalled2 = true;
			}

			public void onDefenderTurn() {
				onDefenderCalled2 = true;
			}
		};
		Battle b = new Battle(s, foe);

		Method m = Battle.class.getDeclaredMethod("endTurn");
		m.setAccessible(true);
		m.invoke(b);
		boolean success = (onAttackerCalled1 && onDefenderCalled2)
				|| (onAttackerCalled2 && onDefenderCalled1);
		assertTrue(
				"EndTurn method in Battle class should perform the race specific actions correctly",
				success);
	}

	@Test(timeout = 1000)
	public void testOnUseCallsGetAppliedDamage() throws Exception {
		hasCalled = false;
		Attack c = new Attack("", 40) {

			@Override
			public int getAppliedDamage(BattleOpponent attacker) {
				hasCalled = true;
				return 0;
			}
		};
		Saiyan s = new Saiyan("saiyan");

		NonPlayableFighter foe = new NonPlayableFighter("foe", 1, 1, 1, 1, 1,
				1, true, null, null);

		c.onUse(s, foe, false);
		assertTrue(
				"OnUse method in Attack class should get the applied damage",
				hasCalled);
	}

	// **************************** Helper methods
	// ********************************//
	@SuppressWarnings("rawtypes")
	private static void invokeSetter(Object mainObject, Class variableClass,
			Object variableObject) throws NoSuchMethodException,
					SecurityException {

		int index = classContainsFieldAtIndex(mainObject.getClass(),
				variableClass);
		if (index != -1) {
			Field f = mainObject.getClass().getDeclaredFields()[index];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = mainObject.getClass().getMethod(methodName,
					variableClass);
			try {
				setter.invoke(mainObject, variableObject);
			} catch (Exception e) {
				fail("The class: " + mainObject.getClass()
						+ " should have a setter method for the listener");
			}
		}
	}

	// returns the index of the variable in a certain class if exists, and -1
	// otherwise
	@SuppressWarnings("rawtypes")
	private static int classContainsFieldAtIndex(Class c, Class contained) {
		Field[] fields = c.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(contained)) {
				i = j;
				break;
			}
		}
		return i;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private static boolean classContainsField(Class c, Class contained) {
		Field[] fields = c.getDeclaredFields();
		boolean contains = false;
		for (Field f : fields) {
			if (f.getType().equals(contained)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public static void clearWorld(Cell[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new EmptyCell();
			}
		}
	}

	private static boolean containDuplicatesSuperAttacks(
			ArrayList<SuperAttack> s1) {
		boolean dup = false;
		for (int i = 0; i < s1.size(); i++)
			for (int j = i + 1; j < s1.size(); j++)
				if (s1.get(i).getName().equals(s1.get(j).getName())) {
					dup = true;
					break;
				}
		return dup;
	}

	private static boolean containDuplicatesUltimateAttacks(
			ArrayList<UltimateAttack> s1) {
		boolean dup = false;
		for (int i = 0; i < s1.size(); i++)
			for (int j = i + 1; j < s1.size(); j++)
				if (s1.get(i).getName().equals(s1.get(j).getName())) {
					dup = true;
					break;
				}
		return dup;
	}

	private static boolean superAttacksListsAreEqual(ArrayList<SuperAttack> s1,
			ArrayList<SuperAttack> s2) {
		boolean equals = true;
		for (int i = 0; i < s1.size(); i++)
			if (!(s1.get(i).getName().equals(s2.get(i).getName()))) {
				equals = false;
				break;
			}
		return equals;
	}

	private static boolean ultimateAttacksListsAreEqual(
			ArrayList<UltimateAttack> s1, ArrayList<UltimateAttack> s2) {
		boolean equals = true;
		for (int i = 0; i < s1.size(); i++)
			if (!s1.get(i).getName().equals(s2.get(i).getName())) {
				equals = false;
				break;
			}
		return equals;
	}

	public static boolean TwoMapsDifferent(Cell[][] map1, Cell[][] map2) {
		boolean different = false;
		for (int i = 0; i < map1.length; i++) {
			for (int j = 0; j < map1[i].length; j++) {

				if (!map1[i][j].getClass().equals(map2[i][j].getClass())) {
					different = true;
					break;
				} else {

					if (map1[i][j].getClass().equals(FoeCell.class)
							&& !((FoeCell) map1[i][j])
									.getFoe()
									.getName()
									.equals(((FoeCell) map2[i][j]).getFoe()
											.getName())) {

						different = true;
						break;
					}

					else if (map1[i][j].getClass()
							.equals(CollectibleCell.class)
							&& ((CollectibleCell) map1[i][j]).getCollectible() != ((CollectibleCell) map2[i][j])
									.getCollectible()) {

						different = true;
						break;
					}
				}
			}

		}

		return different;

	}

	public static DragonWish[] testGetWishesHelper() {
		ArrayList<SuperAttack> superAttacks = new ArrayList<>();
		superAttacks.add(new SuperAttack("Big Bang Kamehameha", 350));
		superAttacks.add(new SuperAttack("Emperor's Death Beam", 350));
		superAttacks.add(new SuperAttack("Demon Wave", 300));
		superAttacks.add(new SuperAttack("Guilty Flash", 300));

		ArrayList<UltimateAttack> ultimateAttacks = new ArrayList<>();
		ultimateAttacks
				.add(new UltimateAttack("Super Big Bang Kamehameha", 350));
		ultimateAttacks.add(new UltimateAttack("Final Shine Attack", 350));
		ultimateAttacks.add(new UltimateAttack("Final Galick Gun", 300));
		ultimateAttacks.add(new UltimateAttack("Explosive Demon Wave", 300));

		Dragon d = new Dragon("Shenron", superAttacks, ultimateAttacks, 10, 5);
		DragonWish[] wishes = d.getWishes();

		return wishes;
	}

	private void testOnWinScenarioFighterDefeatsWeakFoeHelper(Game g, NonPlayableFighter foe) throws Exception {
		World w = g.getWorld();

		w.getMap()[8][9] = new FoeCell(foe);

		Field[] fields = Cell.class.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(CellListener.class)) {
				i = j;
				break;
			}
		}

		if (i != -1) {
			Field f = Cell.class.getDeclaredFields()[i];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = Cell.class
					.getMethod(methodName, CellListener.class);
			try {
				setter.invoke(w.getMap()[8][9], w);
			} catch (Exception e) {
				fail("The class: " + w.getMap()[8][9].getClass()
						+ " should have a setter method for the listener");
			}
		}

		Player p = g.getPlayer();

		if (p.getActiveFighter() == null) {

			SuperAttack k = new SuperAttack("Kamehameha", 400);
			UltimateAttack f = new UltimateAttack("Final Flash", 1000);
			ArrayList<SuperAttack> fightersSuperAttacks = new ArrayList<SuperAttack>();
			fightersSuperAttacks.add(k);
			ArrayList<UltimateAttack> fighterUltimateAttacks = new ArrayList<UltimateAttack>();
			fighterUltimateAttacks.add(f);

			Namekian n = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6,
					600, fightersSuperAttacks, fighterUltimateAttacks);
			ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
			fighters.add(n);
			p.setFighters(fighters);

			p.setActiveFighter(n);

		}

		GameController controller = new GameController() {
			public void onBattleEvent(BattleEvent battleEvent) {
				b = (Battle) battleEvent.getSource();
			}
		};
		invokeSetter(g, GameListener.class, controller);

		w.moveUp();

		if (b == null)
			fail("When meeting a Foe, a new battle should be created, and STARTED event type should be sent to the Battle Listener");

		p.getActiveFighter().setKi(6);

		b.start();
		b.attack(p.getActiveFighter().getUltimateAttacks().get(0));
		int foeHealth = foe.getHealthPoints();
		System.out.println(foeHealth);
	}

	@SuppressWarnings("unused")
	private void testOnWinScenarioFoeDefeatsPlayerHelper(Game g, NonPlayableFighter foe) throws Exception {
		World w = g.getWorld();
		w.getMap()[8][9] = new FoeCell(foe);

		Field[] fields = Cell.class.getDeclaredFields();
		int i = -1;
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getType().equals(CellListener.class)) {
				i = j;
				break;
			}
		}

		if (i != -1) {
			Field f = Cell.class.getDeclaredFields()[i];
			String n = f.getName().toUpperCase();
			String methodName = "set" + n.charAt(0) + f.getName().substring(1);
			System.out.println(methodName);
			Method setter = Cell.class
					.getMethod(methodName, CellListener.class);
			try {
				setter.invoke(w.getMap()[8][9], w);
			} catch (Exception e) {
				fail("The class: " + w.getMap()[8][9].getClass()
						+ " should have a setter method for the listener");
			}
		}

		Player p = g.getPlayer();
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> fightersSuperAttacks = new ArrayList<SuperAttack>();
		fightersSuperAttacks.add(k);
		ArrayList<UltimateAttack> fighterUltimateAttacks = new ArrayList<UltimateAttack>();
		fighterUltimateAttacks.add(f);

		Namekian n = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6,
				600, fightersSuperAttacks, fighterUltimateAttacks);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(n);
		p.setFighters(fighters);
		p.setActiveFighter(n);

		GameController controller = new GameController() {
			public void onBattleEvent(BattleEvent battleEvent) {
				b = (Battle) battleEvent.getSource();
			}
		};
		invokeSetter(g, GameListener.class, controller);

		w.moveUp();
		if (b == null)
			fail("When meeting a Foe, a new battle should be created, and STARTED event type should be sent to the Battle Listener");

		b.start();
		b.attack((new PhysicalAttack()));
		int meHealth = n.getHealthPoints();
		foe.setKi(7);
		b.attack(foe.getUltimateAttacks().get(0));
		meHealth = n.getHealthPoints();
	}
}
