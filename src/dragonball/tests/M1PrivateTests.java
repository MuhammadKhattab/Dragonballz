package dragonball.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.Test;

import dragonball.model.attack.Attack;
import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.SuperSaiyan;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleOpponent;
import dragonball.model.cell.Cell;
import dragonball.model.cell.Collectible;
import dragonball.model.cell.CollectibleCell;
import dragonball.model.cell.EmptyCell;
import dragonball.model.cell.FoeCell;
import dragonball.model.character.Character;
import dragonball.model.character.fighter.Earthling;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Frieza;
import dragonball.model.character.fighter.Majin;
import dragonball.model.character.fighter.Namekian;
import dragonball.model.character.fighter.NonPlayableFighter;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.dragon.Dragon;
import dragonball.model.game.Game;
import dragonball.model.player.Player;
import dragonball.model.world.World;

public class M1PrivateTests {
	@Test(timeout = 1000)
	public void testPlayerClassSecondConstructor() throws SecurityException {

		Class aClass = Player.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] { String.class, ArrayList.class,
					ArrayList.class, ArrayList.class, int.class, int.class, PlayableFighter.class, int.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing constructor with 8 parametesr in Player class.", thrown);
	}

	@Test(timeout = 1000)
	public void testPlayerFirstConstructorInitialization() {
		boolean thrown = false;
		try {
			Constructor c = Player.class.getDeclaredConstructor(String.class);
			Player p = (Player) c.newInstance("7amada");
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"name\" correctly",
					"7amada", p.getName());
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"senzuBeans\" correctly",
					0, p.getSenzuBeans());
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"dragonBalls\" correctly",
					0, p.getDragonBalls());

			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"activeFighter\" correctly",
					null, p.getActiveFighter());
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"exploredMaps\" correctly",
					0, p.getExploredMaps());

			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"fighters\" correctly",
					new ArrayList<PlayableFighter>(), p.getFighters());
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"superAttacks\" correctly",
					new ArrayList<SuperAttack>(), p.getSuperAttacks());
			assertEquals(
					"The first constructor of the player class should initialize the instance variable \"ultimateAttacks\" correctly",
					new ArrayList<UltimateAttack>(), p.getUltimateAttacks());
		} catch (Exception e) {
			thrown = true;
		}
		assertFalse(" There should be a constructor in class player with one input of type String ", thrown);
	}

	@Test(timeout = 1000)
	public void testPlayerSecondConstructorInitialization() {
		Saiyan g = new Saiyan("Goku");
		Saiyan v = new Saiyan("Vegeta");
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<PlayableFighter> fighters = new ArrayList<PlayableFighter>();
		fighters.add(g);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);
		boolean thrown = false;
		try {
			Constructor c = Player.class.getDeclaredConstructor(String.class, ArrayList.class, ArrayList.class,
					ArrayList.class, int.class, int.class, PlayableFighter.class, int.class);
			Player p = (Player) c.newInstance("7amada", fighters, supers, ultimates, 3, 2, v, 4);

			assertEquals(
					"The second constructor of the player class should initialize the instance variable \"name\" correctly",
					"7amada", p.getName());
			assertEquals(
					"The second constructor of the player class should initialize the instance variable \"senzuBeans\" correctly",
					3, p.getSenzuBeans());
			assertEquals(
					"The second constructor of the player class should initialize the instance variable \"dragonBalls\" correctly",
					2, p.getDragonBalls());

			assertEquals(
					"The second constructor of the player class should initialize the instance variable \"activeFighter\" correctly",
					v, p.getActiveFighter());
			assertEquals(
					"The second constructor of the player class should initialize the instance variable \"exploredMaps\" correctly",
					4, p.getExploredMaps());

			boolean equalFighters = p.getFighters() != null
					&& fighters.get(0).getName().equals(p.getFighters().get(0).getName());
			assertTrue(
					"The second constructor of the player class should initialize the instance variable \"fighters\" correctly",
					equalFighters);

			boolean equalSuperAttacks = p.getSuperAttacks() != null
					&& supers.get(0).getName().equals(p.getSuperAttacks().get(0).getName());
			assertTrue(
					"The second constructor of the player class should initialize the instance variable \"superAttacks\" correctly",
					equalSuperAttacks);

			boolean equalUltimateAttacks = p.getUltimateAttacks() != null
					&& ultimates.get(0).getName().equals(p.getUltimateAttacks().get(0).getName());
			assertTrue(
					"The second constructor of the player class should initialize the instance variable \"ultimateAttacks\" correctly",
					equalUltimateAttacks);

		} catch (Exception e) {
			thrown = true;
		}
		assertFalse(
				" There should be a constructor in class player with the following input parameters: String, ArrayList, ArrayList, ArrayList, int, int, PlayableFighter, int",
				thrown);

	}

	@Test(timeout = 1000)
	public void testFighterCannotBeInstaniated() {
		assertTrue("You should not be able to create new instances from Fighter class.",
				Modifier.isAbstract(Fighter.class.getModifiers()));
	}

	@Test(timeout = 1000)
	public void testFighterClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = Fighter.class.getDeclaredField("level");
		assertEquals("\"level\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("healthPoints");
		assertEquals("\"healthPoints\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("blastDamage");
		assertEquals("\"blastDamage\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("physicalDamage");
		assertEquals("\"physicalDamage\" instance variable in class Fighter should not be accessed outside that class",
				2, f.getModifiers());
		f = Fighter.class.getDeclaredField("ki");
		assertEquals("\"ki\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("stamina");
		assertEquals("\"stamina\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("maxHealthPoints");
		assertEquals("\"maxHealthPoints\" instance variable in class Fighter should not be accessed outside that class",
				2, f.getModifiers());
		f = Fighter.class.getDeclaredField("maxKi");
		assertEquals("\"maxKi\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("maxStamina");
		assertEquals("\"maxStamina\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("superAttacks");
		assertEquals("\"superAttacks\" instance variable in class Fighter should not be accessed outside that class", 2,
				f.getModifiers());
		f = Fighter.class.getDeclaredField("ultimateAttacks");
		assertEquals("\"ultimateAttacks\" instance variable in class Fighter should not be accessed outside that class",
				2, f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testFighterClassWRITEVariables() {
		Method[] methods = Fighter.class.getDeclaredMethods();
		assertTrue("The \"level\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setLevel"));
		assertTrue("The \"healthPoints\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setHealthPoints"));
		assertTrue("The \"blastDamage\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setBlastDamage"));
		assertTrue("The \"physicalDamage\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setPhysicalDamage"));
		assertTrue("The \"ki\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setKi"));
		assertTrue("The \"stamina\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setStamina"));
		assertTrue("The \"maxHealthPoints\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setMaxHealthPoints"));
		assertTrue("The \"maxKi\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setMaxKi"));
		assertTrue("The \"maxStamina\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setMaxStamina"));
		assertTrue("The \"superAttacks\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setSuperAttacks"));
		assertTrue("The \"ultimateAttacks\" instance variable in class Fighter is a WRITE variable.",
				containsMethodName(methods, "setUltimateAttacks"));

		Method m = null;
		boolean found = true;
		try {
			m = Fighter.class.getDeclaredMethod("setLevel", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setLevel\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setLevel\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));
		try {
			m = Fighter.class.getDeclaredMethod("setHealthPoints", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setHealthPoints\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setHealthPoints\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setBlastDamage", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setBlastDamage\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setBlastDamage\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setPhysicalDamage", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setPhysicalDamage\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setPhysicalDamage\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setKi", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setKi\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setKi\" method in Fighter class.", m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setStamina", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setStamina\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setStamina\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setMaxHealthPoints", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setMaxHealthPoints\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setMaxHealthPoints\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setMaxKi", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setMaxKi\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setMaxKi\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setMaxStamina", int.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setMaxStamina\" method that takes one int parameter", found);
		assertTrue("incorrect return type for \"setMaxStamina\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setSuperAttacks", ArrayList.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setSuperAttacks\" method that takes one ArrayList parameter", found);
		assertTrue("incorrect return type for \"setSuperAttacks\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

		try {
			m = Fighter.class.getDeclaredMethod("setUltimateAttacks", ArrayList.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Fighter class should have \"setUltimateAttacks\" method that takes one ArrayList parameter", found);
		assertTrue("incorrect return type for \"setUltimateAttacks\" method in Fighter class.",
				m.getReturnType().equals(Void.TYPE));

	}

	@Test(timeout = 1000)
	public void testFighterClassInheritance() {
		assertEquals("Fighter class should inherit from Character class.", Character.class,
				Fighter.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testFighterClassInterface() {
		Class[] interfaces = Fighter.class.getInterfaces();
		boolean inherits = false;
		for (Class i : interfaces) {
			if (i.toString().equals(BattleOpponent.class.toString()))
				inherits = true;

		}
		assertTrue("Fighter class should implement BattleOpponent interface.", inherits);

	}

	@Test(timeout = 1000)
	public void testPlayableFighterCannotBeInstaniated() {
		assertTrue("You should not be able to create new instances from PlayableFighter class.",
				Modifier.isAbstract(PlayableFighter.class.getModifiers()));

	}

	@Test(timeout = 1000)
	public void testPlayableFighterClassInstanceVariables() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		Field f;
		try {
			f = PlayableFighter.class.getDeclaredField("level");
			found = true;
		} catch (NoSuchFieldException e) {

		}

		assertFalse("the \"abilityPoints\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("healthPoints");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"healthPoints\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("blastDamage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"blastDamage\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("physicalDamage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"physicalDamage\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("ki");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"ki\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("stamina");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"stamina\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("maxStamina");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxStamina\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("maxHealthPoints");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxHealthPoints\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("maxKi");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxKi\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("superAttacks");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"superAttacks\" instance variable should be inherited from class Fighter", found);

		try {
			f = PlayableFighter.class.getDeclaredField("ultimateAttacks");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"ultimateAttacks\" instance variable should be inherited from class Fighter", found);

		Field ff = PlayableFighter.class.getDeclaredField("xp");
		assertNotEquals("there should be \"xp\" instance variable in class Playable Fighter", null, ff);
		ff = PlayableFighter.class.getDeclaredField("targetXp");
		assertNotEquals("there should be \"targetXp\" instance variable in class Playable Fighter", null, ff);
		ff = PlayableFighter.class.getDeclaredField("abilityPoints");
		assertNotEquals("there should be \"abilityPoints\" instance variable in class Playable Fighter", null, ff);

	}

	@Test(timeout = 1000)
	public void testPlayableFighterClassSecondConstructor() throws SecurityException {
		Class aClass = PlayableFighter.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] { String.class, int.class, int.class, int.class,
					int.class, int.class, ArrayList.class, ArrayList.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing a constructor with 8 parameters in PlayableFighter class.", thrown);
	}

	@Test(timeout = 1000)
	public void testPlayableFighterClassInheritance() {
		assertEquals("Wrong inheritance in PlayableFighter class .", Fighter.class,
				PlayableFighter.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testFriezaClassInheritance() {
		assertEquals("Frieza class should inherit from PlayableFighter .", PlayableFighter.class,
				Frieza.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testMajinClassInheritance() {
		assertEquals("Majin class should inherit from PlayableFighter .", PlayableFighter.class,
				Majin.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testSaiyanClassFirstConstructor() {
		Class aClass = Saiyan.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] { String.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing a constructor with String parameter in Saiyan class.", thrown);
	}

	@Test(timeout = 1000)
	public void testSaiyanClassSecondConstructor() {
		Class aClass = Saiyan.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass
					.getConstructor(new Class[] { String.class, int.class, int.class, int.class, int.class, int.class,
							int.class, int.class, int.class, int.class, ArrayList.class, ArrayList.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing a constructor with 12 parameters in Saiyan class.", thrown);
	}

	@Test(timeout = 1000)
	public void testNamekianClassFirstConstructor() {
		Class aClass = Namekian.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] { String.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing a constructor with String parameter in Namekian class.", thrown);
	}

	@Test(timeout = 1000)
	public void testNamekianClassSecondConstructor() {
		Class aClass = Namekian.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass
					.getConstructor(new Class[] { String.class, int.class, int.class, int.class, int.class, int.class,
							int.class, int.class, int.class, int.class, ArrayList.class, ArrayList.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing a constructor with 12 parameters in Namekian class.", thrown);
	}

	@Test(timeout = 1000)
	public void testFriezaClassSecondConstructorInitialization() {
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);
		Frieza e = new Frieza("frieza", 2, 100, 200, 300, 400, 500, 5, 6, 600, supers, ultimates);
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"name\" correctly",
				"frieza", e.getName());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"level\" correctly",
				2, e.getLevel());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"xp\" correctly",
				100, e.getXp());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"targetXp\" correctly",
				200, e.getTargetXp());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"maxHealthPoints\" correctly",
				300, e.getMaxHealthPoints());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"blastDamage\" correctly",
				400, e.getBlastDamage());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"physicalDamage\" correctly",
				500, e.getPhysicalDamage());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"abilityPoints\" correctly",
				5, e.getAbilityPoints());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"ki\" correctly",
				0, e.getKi());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"maxKi\" correctly",
				6, e.getMaxKi());
		assertEquals(
				"The second constructor of the Frieza class should initialize the instance variable \"maxStamina\" correctly",
				600, e.getMaxStamina());

		assertTrue(
				"The second constructor of the Frieza class should initialize the instance variable \"superAttacks\" correctly",
				e.getSuperAttacks() != null && supers.get(0).getName().equals(e.getSuperAttacks().get(0).getName()));
		assertTrue(
				"The second constructor of the Frieza class should initialize the instance variable \"ultimateAttacks\" correctly",
				e.getUltimateAttacks() != null
						&& ultimates.get(0).getName().equals(e.getUltimateAttacks().get(0).getName()));

	}

	@Test(timeout = 1000)
	public void testFriezaClassInstanceVariable() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		try {
			Field f = Frieza.class.getDeclaredField("transformed");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("Only Saiyans have \"transformed\" instance variable", found);

	}

	@Test(timeout = 1000)
	public void testMajinFirstConstructorInitialization() {
		Majin e = new Majin("majin");
		assertEquals("The constructor of the Majun class should initialize the instance variable \"name\" correctly",
				"majin", e.getName());

		assertEquals("All majins should start with \"1500\" healthpoints", 1500, e.getMaxHealthPoints());

		assertEquals("All majins can get up to \"3\" Ki bars", 3, e.getMaxKi());
		assertEquals("All majins can get up to \"6\" stamina bars", 6, e.getMaxStamina());
		assertEquals("All majins should start with level \"1\" ", 1, e.getLevel());
		assertEquals("All majins should start with \"0\" xp", 0, e.getXp());
		assertEquals("All majins should start with \"10\" targetXp:", 10, e.getTargetXp());
		assertEquals("All majins should start with \"0\" abilityPoints:", 0, e.getAbilityPoints());
		assertEquals("All Majins should start with 50 \"blast damage\"", 50, e.getBlastDamage());
		assertEquals("All Majins should start with 50 \"physicalDamage\" ", 50, e.getPhysicalDamage());
		assertEquals(
				"The first constructor of the Majin class should initialize the instance variable \"ki\" correctly", 0,
				e.getKi());
	}

	@Test(timeout = 1000)
	public void testEarthlingClassSecondConstructorInitialization() {
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);
		Earthling e = new Earthling("earthling", 2, 100, 200, 300, 400, 500, 5, 6, 600, supers, ultimates);
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"name\" correctly",
				"earthling", e.getName());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"level\" correctly",
				2, e.getLevel());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"xp\" correctly",
				100, e.getXp());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"targetXp\" correctly",
				200, e.getTargetXp());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"maxHealthPoints\" correctly",
				300, e.getMaxHealthPoints());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"blastDamage\" correctly",
				400, e.getBlastDamage());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"physicalDamage\" correctly",
				500, e.getPhysicalDamage());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"abilityPoints\" correctly",
				5, e.getAbilityPoints());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"ki\" correctly",
				0, e.getKi());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"maxKi\" correctly",
				6, e.getMaxKi());
		assertEquals(
				"The second constructor of the Earthling class should initialize the instance variable \"maxStamina\" correctly",
				600, e.getMaxStamina());
		assertTrue(
				"The second constructor of the Earthling class should initialize the instance variable \"superAttacks\" correctly",
				e.getSuperAttacks() != null && supers.get(0).getName().equals(e.getSuperAttacks().get(0).getName()));
		assertTrue(
				"The second constructor of the Earthling class should initialize the instance variable \"ultimateAttacks\" correctly",
				e.getUltimateAttacks() != null
						&& ultimates.get(0).getName().equals(e.getUltimateAttacks().get(0).getName()));
	}

	@Test(timeout = 1000)
	public void testNamekianClassInstanceVariable() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		try {
			Field f = Namekian.class.getDeclaredField("transformed");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("Only Saiyans have \"transformed\" instance variable", found);

	}

	@Test(timeout = 1000)
	public void testNamekianClassSecondConstructorInitialization() {
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);
		Namekian e = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6, 600, supers, ultimates);
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"name\" correctly",
				"namekian", e.getName());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"level\" correctly",
				2, e.getLevel());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"xp\" correctly",
				100, e.getXp());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"targetXp\" correctly",
				200, e.getTargetXp());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"maxHealthPoints\" correctly",
				300, e.getMaxHealthPoints());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"blastDamage\" correctly",
				400, e.getBlastDamage());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"physicalDamage\" correctly",
				500, e.getPhysicalDamage());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"abilityPoints\" correctly",
				5, e.getAbilityPoints());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"ki\" correctly",
				0, e.getKi());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"maxKi\" correctly",
				6, e.getMaxKi());
		assertEquals(
				"The second constructor of the Namekian class should initialize the instance variable \"maxStamina\" correctly",
				600, e.getMaxStamina());
		assertTrue(
				"The second constructor of the Namekian class should initialize the instance variable \"superAttacks\" correctly",
				e.getSuperAttacks() != null && supers.get(0).getName().equals(e.getSuperAttacks().get(0).getName()));
		assertTrue(
				"The second constructor of the Namekian class should initialize the instance variable \"ultimateAttacks\" correctly",
				e.getUltimateAttacks() != null
						&& ultimates.get(0).getName().equals(e.getUltimateAttacks().get(0).getName()));
	}

	@Test(timeout = 1000)
	public void testSaiyanClassInstanceVariable() throws NoSuchFieldException, SecurityException {

		Field f = Saiyan.class.getDeclaredField("transformed");
		assertNotEquals(" Saiyan class should  have \"transformed\" instance variable", null, f);
	}

	@Test(timeout = 1000)
	public void testSaiyanClassREADVariables() {
		Method[] methods = Saiyan.class.getDeclaredMethods();
		assertTrue("The \"transformed\" instance variable in class Saiyan is a READ variable.",
				containsMethodName(methods, "isTransformed"));

	}

	@Test(timeout = 1000)
	public void testSaiyanClassWRITEVariables() {
		Method[] methods = Saiyan.class.getDeclaredMethods();
		assertTrue("The \"transformed\" instance variable in class Saiyan is a WRITE variable.",
				containsMethodName(methods, "setTransformed"));

		Method m = null;
		boolean found = true;
		try {
			m = Saiyan.class.getDeclaredMethod("setTransformed", boolean.class);
		} catch (Exception e) {
			found = false;
		}
		assertTrue("Saiyan class should have \"setTransformed\" method that takes one boolean parameter", found);
		assertTrue("incorrect return type for \"setTransformed\" method in Saiyan class.",
				m.getReturnType().equals(Void.TYPE));
	}

	@Test(timeout = 1000)
	public void testSaiyanClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = Saiyan.class.getDeclaredField("transformed");
		assertEquals("\"transformed\" instance variable in class Saiyan should not be accessed outside that class", 2,
				f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testNonPlayableFighterClassInstanceVariables() throws NoSuchFieldException, SecurityException {

		Field f;
		boolean found = false;
		try {
			f = NonPlayableFighter.class.getDeclaredField("strong");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertTrue("there should be \"strong\" instance variable in class NonPlayableFighter", found);
		found = false;
		try {
			f = NonPlayableFighter.class.getDeclaredField("level");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"level\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("healthPoints");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"healthPoints\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("blastDamage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"blastDamage\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("physicalDamage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"physicalDamage\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("ki");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"ki\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("stamina");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"stamina\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("maxStamina");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxStamina\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("maxHealthPoints");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxHealthPoints\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("maxKi");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"maxKi\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("superAttacks");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"superAttacks\" instance variable should be inherited from class Fighter", found);

		try {
			f = NonPlayableFighter.class.getDeclaredField("ultimateAttacks");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"ultimateAttacks\" instance variable should be inherited from class Fighter", found);

	}

	@Test(timeout = 1000)
	public void testNonPlayableFighterClassConstructorInitialization() {
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);
		NonPlayableFighter e = new NonPlayableFighter("boss", 4, 2000, 100, 300, 7, 7, true, supers, ultimates);
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"name\" correctly",
				"boss", e.getName());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"level\" correctly",
				4, e.getLevel());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"maxHealthPoints\" correctly",
				2000, e.getMaxHealthPoints());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"blastDamage\" correctly",
				100, e.getBlastDamage());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"physicalDamage\" correctly",
				300, e.getPhysicalDamage());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"maxKi\" correctly",
				7, e.getMaxKi());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"ki\" correctly",
				0, e.getKi());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"maxStamina\" correctly",
				7, e.getMaxStamina());
		assertEquals(
				"The  constructor of the NonPlayableFighter class should initialize the instance variable \"strong\" correctly",
				true, e.isStrong());
		assertTrue(
				"The  constructor of theNonPlayableFighter class should initialize the instance variable \"superAttacks\" correctly",
				e.getSuperAttacks() != null && supers.get(0).getName().equals(e.getSuperAttacks().get(0).getName()));
		assertTrue(
				"The second constructor of the theNonPlayableFighter class should initialize the instance variable \"ultimateAttacks\" correctly",
				e.getUltimateAttacks() != null
						&& ultimates.get(0).getName().equals(e.getUltimateAttacks().get(0).getName()));
	}

	@Test(timeout = 1000)
	public void testWorldClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = World.class.getDeclaredField("map");
		assertEquals("\"map\" instance variable in class World should not be accessed outside that class", 2,
				f.getModifiers());

		f = World.class.getDeclaredField("playerColumn");
		assertEquals("\"playerColumn\" instance variable in class World should not be accessed outside that class", 2,
				f.getModifiers());

		f = World.class.getDeclaredField("playerRow");
		assertEquals("\"playerRow\" instance variable in class World should not be accessed outside that class", 2,
				f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testEmptyCellClassInheritance() {
		assertEquals("Wrong inheritance in EmptyCell class .", Cell.class, EmptyCell.class.getSuperclass());

	}

	@Test(timeout = 1000)
	public void testEmptyCellClassToStringMethod() throws NoSuchMethodException, SecurityException {
		Method[] methods = EmptyCell.class.getDeclaredMethods();
		assertTrue("Empty Cell class should have \"toString\" method", containsMethodName(methods, "toString"));
		Method m = EmptyCell.class.getDeclaredMethod("toString");
		assertTrue("incorrect return type for \"toString\" method in Empty Cell class.",
				m.getReturnType().equals(String.class));

		boolean thrown = false;
		try {
			Constructor c = EmptyCell.class.getDeclaredConstructor();
			EmptyCell cell = (EmptyCell) c.newInstance();
			String cellOutput = cell.toString();
			assertEquals("Empty Cell should be represented as an empty square bracket with a space inside", "[ ]",
					cellOutput);

		} catch (Exception e) {
			thrown = true;
		}
		assertFalse(" There should be a constructor in class EmptyCell with no input parameters", thrown);

	}

	@Test(timeout = 1000)
	public void testFoeCellClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = FoeCell.class.getDeclaredField("foe");
		assertEquals("\"foe\" instance variable in class FoeCell should not be accessed outside that class", 2,
				f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testFoeCellClassREADVariables() {
		Method[] methods = FoeCell.class.getDeclaredMethods();
		assertTrue("The \"foe\" instance variable in class FoeCell is a READ variable.",
				containsMethodName(methods, "getFoe"));

	}

	@Test(timeout = 1000)
	public void testFoeCellClassWRITEVariables() {
		Method[] methods = FoeCell.class.getDeclaredMethods();
		assertFalse("The \"foe\" instance variable in class FoeCell is a READ ONLY variable.",
				containsMethodName(methods, "setFoe"));
	}

	@Test(timeout = 1000)
	public void testCollectibleCellClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = CollectibleCell.class.getDeclaredField("collectible");
		assertEquals(
				"\"collectible\" instance variable in class CollectibleCell should not be accessed outside that class",
				2, f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testCollectibleCellClassREADVariables() {
		Method[] methods = CollectibleCell.class.getDeclaredMethods();
		assertTrue("The \"collectible\" instance variable in class CollectibleCell is a READ variable.",
				containsMethodName(methods, "getCollectible"));

	}

	@Test(timeout = 1000)
	public void testCollectibleCellClassWRITEVariables() {
		Method[] methods = CollectibleCell.class.getDeclaredMethods();
		assertFalse("The \"collectible\" instance variable in class CollectibleCell is a READ ONLY variable.",
				containsMethodName(methods, "setCollectible"));
	}

	@Test(timeout = 1000)
	public void testBattleClassInstanceVariables() throws SecurityException {
		Field f;
		boolean thrown = false;
		try {
			f = Battle.class.getDeclaredField("me");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"me\" instance variable in class Battle", thrown);

		thrown = false;
		try {
			f = Battle.class.getDeclaredField("foe");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"foe\" instance variable in class Battle", thrown);

		thrown = false;
		try {
			f = Battle.class.getDeclaredField("attacker");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"attacker\" instance variable in class Battle", thrown);
	}

	@Test(timeout = 1000)
	public void testBattleConstructorInitialization() {
		SuperAttack k = new SuperAttack("Kamehameha", 400);
		UltimateAttack f = new UltimateAttack("Final Flash", 700);
		ArrayList<SuperAttack> supers = new ArrayList<SuperAttack>();
		supers.add(k);
		ArrayList<UltimateAttack> ultimates = new ArrayList<UltimateAttack>();
		ultimates.add(f);

		Namekian n = new Namekian("namekian", 2, 100, 200, 300, 400, 500, 5, 6, 4, supers, ultimates);

		n.setHealthPoints(10);
		n.setStamina(5);
		NonPlayableFighter e = new NonPlayableFighter("boss", 4, 2000, 100, 300, 7, 7, true, supers, ultimates);

		e.setHealthPoints(10);
		e.setStamina(5);

		boolean thrown = false;
		try {
			Constructor c = Battle.class.getDeclaredConstructor(BattleOpponent.class, BattleOpponent.class);
			Battle b = (Battle) c.newInstance(n, e);
			assertEquals("the constructor of the Battle class should initialize the instance variable \"me\" correctly",
					n, b.getMe());
			assertEquals("the constructor of the World class should initialize the instance variable \"foe\" correctly",
					e, b.getFoe());

			assertEquals("The fighter character in the \"Battle\" class should start with full health", 300,
					n.getHealthPoints());

		} catch (Exception exception) {
			thrown = true;
		}
		assertFalse(
				" There should be a constructor in class Battle with the following input parameters: BattleOpponent, BattleOpponent",
				thrown);
	}

	@Test(timeout = 1000)
	public void testAttackClassWRITEVariables() {
		Method[] methods = Attack.class.getDeclaredMethods();
		assertFalse("The \"damage\" instance variable in class Attack is a not WRITE variable.",
				containsMethodName(methods, "setDamage"));
		assertFalse("The \"name\" instance variable in class Attack is a not WRITE variable.",
				containsMethodName(methods, "setName"));

	}

	@Test(timeout = 1000)
	public void testAttackClassConstructor() throws SecurityException {
		Class aClass = Attack.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] { String.class, int.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing constructor in Attack class.", thrown);
	}

	@Test(timeout = 1000)
	public void testPhysicalAttackClassInheritance() {
		assertEquals("PhysicalAttack class should inherit from Attack.", Attack.class,
				PhysicalAttack.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testAttackSubclassesConstructorInitialization() {
		UltimateAttack u = new UltimateAttack("final flash", 450);
		assertEquals(
				"the constructor of the UltimateAttack class should initialize the instance variable \"name\" correctly",
				"final flash", u.getName());
		assertEquals(
				"the constructor of the UltimateAttack class should initialize the instance variable \"damage\" correctly",
				450, u.getDamage());

		boolean thrown = false;
		try {
			Constructor c = SuperSaiyan.class.getDeclaredConstructor();
			SuperSaiyan ss = (SuperSaiyan) c.newInstance();
			assertTrue(
					"the constructor of the SuperSaiyan class should initialize the instance variable \"name\" correctly",
					ss.getName().replaceAll("\\s+", "").equalsIgnoreCase("supersaiyan"));

			assertEquals("SuperSaiyan does not inflict damage", 0, ss.getDamage());

		} catch (Exception exception) {
			thrown = true;
		}
		assertFalse(" There should be a constructor in class SuperSaiyan which takes no input parameters", thrown);

	}

	@Test(timeout = 1000)
	public void testPhysicalAttackClassInstanceVariables() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		try {
			Field f = PhysicalAttack.class.getDeclaredField("damage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"damage\" instance variable should be inherited from class Attack", found);

		try {
			Field f = PhysicalAttack.class.getDeclaredField("name");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"name\" instance variable should be inherited from class Attack", found);

	}

	@Test(timeout = 1000)
	public void testSuperAttackClassInheritance() {
		assertEquals("SuperAttack class should inherit from Attack.", Attack.class, SuperAttack.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testUltimateAttackClassInstanceVariables() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		try {
			Field f = UltimateAttack.class.getDeclaredField("damage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"damage\" instance variable should be inherited from class Attack", found);

		try {
			Field f = UltimateAttack.class.getDeclaredField("name");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"name\" instance variable should be inherited from class Attack", found);

	}

	@Test(timeout = 1000)
	public void testMaximumChargeClassInheritance() {
		assertEquals("MaximumCharge class should inherit from SuperAttack.", SuperAttack.class,
				MaximumCharge.class.getSuperclass());
	}

	@Test(timeout = 1000)
	public void testMaximumChargeClassInstanceVariables() throws NoSuchFieldException, SecurityException {
		boolean found = false;
		try {
			Field f = MaximumCharge.class.getDeclaredField("damage");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"damage\" instance variable should be inherited from class Attack", found);

		try {
			Field f = MaximumCharge.class.getDeclaredField("name");
			found = true;
		} catch (NoSuchFieldException e) {
		}
		assertFalse("the \"name\" instance variable should be inherited from class Attack", found);

	}

	@Test(timeout = 1000)
	public void testDragonClassInstanceVariables() throws SecurityException {
		Field f;
		boolean thrown = false;
		try {
			f = Dragon.class.getDeclaredField("name");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"name\" instance variable in class Dragon", thrown);

		try {
			f = Dragon.class.getDeclaredField("superAttacks");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"superAttacks\" instance variable in class Dragon", thrown);

		try {
			f = Dragon.class.getDeclaredField("ultimateAttacks");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"ultimateAttacks\" instance variable in class Dragon", thrown);

		try {
			f = Dragon.class.getDeclaredField("senzuBeans");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"senzuBeans\" instance variable in class Dragon", thrown);

		try {
			f = Dragon.class.getDeclaredField("abilityPoints");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"abilityPoints\" instance variable in class Dragon", thrown);
	}

	@Test(timeout = 1000)
	public void testDragonClassVariablesAccessibility() throws NoSuchFieldException, SecurityException {
		Field f = Dragon.class.getDeclaredField("name");
		assertEquals("\"name\" instance variable in class Dragon should not be accessed outside that class", 2,
				f.getModifiers());
		f = Dragon.class.getDeclaredField("superAttacks");
		assertEquals("\"superAttacks\" instance variable in class Dragon should not be accessed outside that class", 2,
				f.getModifiers());
		f = Dragon.class.getDeclaredField("ultimateAttacks");
		assertEquals("\"ultimateAttacks\" instance variable in class Dragon should not be accessed outside that class",
				2, f.getModifiers());
		f = Dragon.class.getDeclaredField("senzuBeans");
		assertEquals("\"senzuBeans\" instance variable in class Dragon should not be accessed outside that class", 2,
				f.getModifiers());
		f = Dragon.class.getDeclaredField("abilityPoints");
		assertEquals("\"abilityPoints\" instance variable in class Dragon should not be accessed outside that class", 2,
				f.getModifiers());
	}

	@Test(timeout = 1000)
	public void testDragonClassREADVariables() {
		Method[] methods = Dragon.class.getDeclaredMethods();
		assertTrue("The \"name\" instance variable in class Dragon is a READ variable.",
				containsMethodName(methods, "getName"));
		assertTrue("The \"superAttacks\" instance variable in class Dragon is a READ variable.",
				containsMethodName(methods, "getSuperAttacks"));
		assertTrue("The \"ultimateAttacks\" instance variable in class Dragon is a READ variable.",
				containsMethodName(methods, "getUltimateAttacks"));
		assertTrue("The \"senzuBeans\" instance variable in class Dragon is a READ variable.",
				containsMethodName(methods, "getSenzuBeans"));
		assertTrue("The \"abilityPoints\" instance variable in class Dragon is a READ variable.",
				containsMethodName(methods, "getAbilityPoints"));

	}

	@Test(timeout = 1000)
	public void testDragonClassConstructor() throws SecurityException {

		Class aClass = Dragon.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(
					new Class[] { String.class, ArrayList.class, ArrayList.class, int.class, int.class });
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing constructor in Dragon class.", thrown);
	}

	@Test(timeout = 1000)
	public void testGameClassInstanceVariables() throws SecurityException {
		Field f;
		boolean thrown = false;
		try {
			f = Game.class.getDeclaredField("player");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"player\" instance variable in class Game", thrown);
		try {
			f = Game.class.getDeclaredField("world");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"world\" instance variable in class Game", thrown);
		try {
			f = Game.class.getDeclaredField("weakFoes");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"weakFoes\" instance variable in class Game", thrown);
		try {
			f = Game.class.getDeclaredField("strongFoes");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"strongFoes\" instance variable in class Game", thrown);
		try {
			f = Game.class.getDeclaredField("attacks");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"attacks\" instance variable in class Game", thrown);
		try {
			f = Game.class.getDeclaredField("dragons");
		} catch (NoSuchFieldException e) {
			thrown = true;
		}
		assertFalse("there should be \"dragons\" instance variable in class Game", thrown);
	}

	@Test(timeout = 1000)
	public void testGameClassREADVariables() {
		Method[] methods = Game.class.getDeclaredMethods();
		assertTrue("The \"player\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getPlayer"));
		assertTrue("The \"world\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getWorld"));

		assertTrue("The \"foes\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getWeakFoes"));
		assertTrue("The \"player\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getStrongFoes"));
		assertTrue("The \"attacks\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getAttacks"));
		assertTrue("The \"dragons\" instance variable in class Game is a READ variable.",
				containsMethodName(methods, "getDragons"));

	}

	@Test(timeout = 1000)
	public void testGameClassWRITEVariables() {
		Method[] methods = Game.class.getDeclaredMethods();
		assertFalse("The \"player\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setPlayer"));
		assertFalse("The \"world\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setWorld"));
		assertFalse("The \"foes\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setWeakFoes"));
		assertFalse("The \"foes\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setStrongFoes"));
		assertFalse("The \"attacks\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setAttacks"));
		assertFalse("The \"dragons\" instance variable in class Player is a READ ONLY variable.",
				containsMethodName(methods, "setDragons"));
	}

	@Test(timeout = 1000)
	public void testGameClassConstructor() throws SecurityException {

		Class aClass = Game.class;
		boolean thrown = false;
		try {
			Constructor constructor = aClass.getConstructor(new Class[] {});
		} catch (NoSuchMethodException e) {
			thrown = true;
		}
		assertFalse("Missing constructor in Game class.", thrown);
	}

	@Test(timeout = 1000)
	public void testMapDragonBallsCount() throws Exception, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		World w = new World();
		Game game = new Game();
		w.generateMap(game.getWeakFoes(), game.getStrongFoes());
		Field f = World.class.getDeclaredField("map");
		f.setAccessible(true);
		Cell[][] map = (Cell[][]) f.get(w);

		int dragonBalls = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].getClass().equals(CollectibleCell.class))
					try {
						if (((CollectibleCell) map[i][j]).getCollectible().equals(Collectible.DRAGON_BALL))
							dragonBalls++;
					} catch (NullPointerException e) {
					}
			}
		}
		assertTrue("The map should contain only one Dragon Ball", dragonBalls == 1);
	}

	@Test(timeout = 1000)
	public void testMapDragonBallsPositionsRandomlyGenerated() throws Exception, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		World w = new World();
		Game game = new Game();

		ArrayList<Location> locations = new ArrayList<Location>();
		for (int m = 0; m < 100; m++) {
			w.generateMap(game.getWeakFoes(), game.getStrongFoes());
			Field f = World.class.getDeclaredField("map");
			f.setAccessible(true);
			Cell[][] map = (Cell[][]) f.get(w);

			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j].getClass().equals(CollectibleCell.class))
						try {
							if (((CollectibleCell) map[i][j]).getCollectible().equals(Collectible.DRAGON_BALL))
								locations.add(new Location(i, j));
						} catch (NullPointerException e) {
						}
				}
			}
			w = new World();
		}
		boolean random = uniqueLocations(locations);
		assertTrue("Location of the dragon ball should be randomly generated in the map", random);

	}

	@Test(timeout = 1000)
	public void testMapWeakFoesCount() throws Exception, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		World w = new World();
		Game game = new Game();
		w.generateMap(game.getWeakFoes(), game.getStrongFoes());
		Field f = World.class.getDeclaredField("map");
		f.setAccessible(true);
		Cell[][] map = (Cell[][]) f.get(w);

		int weakFoes = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].getClass().equals(FoeCell.class))
					try {
						if (((FoeCell) map[i][j]).getFoe().isStrong() == false)
							weakFoes++;
					} catch (NullPointerException e) {

					}
			}
		}
		assertTrue("The map should contain 15 weak foes", weakFoes == 15);
	}

	@Test(timeout = 1000)
	public void testMapWeakFoesLocationsRandomlyGenerated() throws Exception, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		World w = new World();
		Game game = new Game();
		ArrayList<ArrayList<Location>> locations = new ArrayList<ArrayList<Location>>();
		for (int m = 0; m < 30; m++) {
			w.generateMap(game.getWeakFoes(), game.getStrongFoes());
			Field f = World.class.getDeclaredField("map");
			f.setAccessible(true);
			Cell[][] map = (Cell[][]) f.get(w);
			ArrayList<Location> mapLocations = new ArrayList<Location>();
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j].getClass().equals(FoeCell.class))
						try {
							if (((FoeCell) map[i][j]).getFoe().isStrong() == false)
								mapLocations.add(new Location(i, j));
						} catch (NullPointerException e) {

						}

				}
			}
			locations.add(mapLocations);
			w = new World();
		}

		boolean random = true;
		for (int i = 0; i < locations.size(); i++) {
			ArrayList<Location> l = locations.get(i);
			for (int j = i + 1; j < locations.size(); j++) {
				boolean equal = false;
				ArrayList<Location> m = locations.get(j);
				if (l.size() == m.size()) {
					equal = true;
					for (int x = 0; x < l.size(); x++) {
						if (!l.get(x).equals(m.get(x)))
							equal = false;
					}
				}
				if (equal) {
					random = false;
					break;
				}
			}
		}

		assertTrue("The locations of the weak foes should be randomly generated in the map", random);

	}

	@Test(timeout = 1000)
	public void testMapWeakFoesRandomlyGenerated() throws Exception, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		World w = new World();
		Game game = new Game();
		ArrayList<NonPlayableFighter> gameWeakFoes = game.getWeakFoes();
		ArrayList<ArrayList<NonPlayableFighter>> allWeakFoes = new ArrayList<ArrayList<NonPlayableFighter>>();
		for (int m = 0; m < 30; m++) {
			w.generateMap(game.getWeakFoes(), game.getStrongFoes());
			Field f = World.class.getDeclaredField("map");
			f.setAccessible(true);
			Cell[][] map = (Cell[][]) f.get(w);
			ArrayList<NonPlayableFighter> foes = new ArrayList<NonPlayableFighter>();
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j].getClass().equals(FoeCell.class))
						try {
							if (((FoeCell) map[i][j]).getFoe().isStrong() == false)
								foes.add(((FoeCell) map[i][j]).getFoe());
						} catch (NullPointerException e) {

						}

				}
			}
			allWeakFoes.add(foes);
			w = new World();
		}

		boolean random = true;
		for (int i = 0; i < allWeakFoes.size(); i++) {
			ArrayList<NonPlayableFighter> l = allWeakFoes.get(i);
			for (int j = i + 1; j < allWeakFoes.size(); j++) {
				boolean equal = false;
				ArrayList<NonPlayableFighter> m = allWeakFoes.get(j);
				if (l.size() == m.size()) {
					equal = true;
					for (int x = 0; x < l.size(); x++) {
						if (!l.get(x).getName().equals(m.get(x).getName()))
							equal = false;
					}
				}
				if (equal) {
					random = false;
					break;
				}
			}
		}

		assertTrue("The locations of the weak foes should be randomly generated in the map", random);
		ArrayList<NonPlayableFighter> gameWeakFoesAfterTest = game.getWeakFoes();
		boolean changed = false;
		if (gameWeakFoes.size() != gameWeakFoesAfterTest.size())
			changed = true;
		else {
			for (int i = 0; i < gameWeakFoes.size(); i++)
				if (!gameWeakFoes.get(i).getName().equals(gameWeakFoesAfterTest.get(i).getName())) {
					changed = true;
					break;
				}
		}

		assertFalse("The weak foes arrayList shouldn't be changed after generating the map", changed);
	}

	public static String toString(Cell[][] map) {
		String s = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				s += map[i][j].toString() + "\t";
			}
			s += '\n';
		}
		return s;
	}

	@Test(timeout = 1000)
	public void testLoadAttacksMethod() throws Exception, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, FileNotFoundException {

		PrintWriter attacksWriter = new PrintWriter("AttacksTest.csv");
		attacksWriter.println("MC,Maximum Charge,0");
		attacksWriter.println("SS,Super Saiyan,0");
		attacksWriter.println("SA,Kamehameha,250");
		attacksWriter.println("UA,Vanishing Ball,500");
		attacksWriter.close();
		Game g = new Game();
		g.getAttacks().clear();
		Method method = Game.class.getDeclaredMethod("loadAttacks", new Class[] { String.class });
		method.setAccessible(true);
		method.invoke(g, "AttacksTest.csv");
		ArrayList<Attack> testAttacks = g.getAttacks();

		assertEquals("Attacks ArrayList doesn't contain the same number of attacks given in the CSV file", 4,
				testAttacks.size());

		assertTrue("Attack type is not loaded correctly from the CSV file of Attacks",
				testAttacks.get(0) instanceof MaximumCharge);
		assertEquals("Attack name is not loaded correctly from the CSV file of Attacks", "Maximum Charge",
				testAttacks.get(0).getName());
		assertEquals("Attack damage is not loaded correctly from the CSV file of Attacks", 0,
				testAttacks.get(0).getDamage());

		assertTrue("Attack type is not loaded correctly from the CSV file of Attacks",
				testAttacks.get(1) instanceof SuperSaiyan);
		assertEquals("Attack name is not loaded correctly from the CSV file of Attacks", "Super Saiyan",
				testAttacks.get(1).getName());
		assertEquals("Attack damage is not loaded correctly from the CSV file of Attacks", 0,
				testAttacks.get(1).getDamage());

		assertTrue("Attack type is not loaded correctly from the CSV file of Attacks",
				testAttacks.get(2) instanceof SuperAttack);
		assertEquals("Attack name is not loaded correctly from the CSV file of Attacks", "Kamehameha",
				testAttacks.get(2).getName());
		assertEquals("Attack damage is not loaded correctly from the CSV file of Attacks", 250,
				testAttacks.get(2).getDamage());

		assertTrue("Attack type is not loaded correctly from the CSV file of Attacks",
				testAttacks.get(3) instanceof UltimateAttack);
		assertEquals("Attack name is not loaded correctly from the CSV file of Attacks", "Vanishing Ball",
				testAttacks.get(3).getName());
		assertEquals("Attack damage is not loaded correctly from the CSV file of Attacks", 500,
				testAttacks.get(3).getDamage());

		try {

			File file = new File("AttacksTest.csv");
			file.delete();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Test(timeout = 1000)
	public void testLoadDragonsMethod() throws Exception, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, FileNotFoundException {

		PrintWriter dragonsWriter = new PrintWriter("DragonsTest.csv");
		dragonsWriter.println("Dragon1,5,7");
		dragonsWriter.println("Demon Wave,Nova Star");
		dragonsWriter.println("Weekend,Red Magma,Final Flash");
		dragonsWriter.println("Dragon2,10,20");
		dragonsWriter.println("Majin Kamehameha,Perfect Special Beam Cannon,Ice Ray");
		dragonsWriter.println("Spirit Sword,Genocide Blast");
		dragonsWriter.close();
		Game g = new Game();
		g.getDragons().clear();
		Method method = Game.class.getDeclaredMethod("loadDragons", new Class[] { String.class });
		method.setAccessible(true);
		method.invoke(g, "DragonsTest.csv");
		ArrayList<Dragon> testDragons = g.getDragons();

		assertEquals("Dragons ArrayList doesn't contain the same number of dragons given in the CSV file", 2,
				testDragons.size());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", "Dragon1",
				testDragons.get(0).getName());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", 5,
				testDragons.get(0).getSenzuBeans());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", 7,
				testDragons.get(0).getAbilityPoints());

		assertEquals("Super Attacks of Dragon doesn't contain the same number of super attacks given in the CSV file",
				2, testDragons.get(0).getSuperAttacks().size());
		assertEquals("Super Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Demon Wave",
				testDragons.get(0).getSuperAttacks().get(0).getName());
		assertEquals("Super Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Nova Star",
				testDragons.get(0).getSuperAttacks().get(1).getName());

		assertEquals(
				"Ultimate Attacks of Dragon doesn't contain the same number of ultimate attacks given in the CSV file",
				3, testDragons.get(0).getUltimateAttacks().size());
		assertEquals("Ultimate Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Weekend",
				testDragons.get(0).getUltimateAttacks().get(0).getName());
		assertEquals("Ultimate Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Red Magma",
				testDragons.get(0).getUltimateAttacks().get(1).getName());
		assertEquals("Ultimate Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Final Flash",
				testDragons.get(0).getUltimateAttacks().get(2).getName());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", "Dragon2",
				testDragons.get(1).getName());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", 10,
				testDragons.get(1).getSenzuBeans());

		assertEquals("Dragon name is not loaded correctly from the CSV file of Dragons", 20,
				testDragons.get(1).getAbilityPoints());

		assertEquals("Super Attacks of Dragon doesn't contain the same number of super attacks given in the CSV file",
				3, testDragons.get(1).getSuperAttacks().size());
		assertEquals("Super Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Majin Kamehameha",
				testDragons.get(1).getSuperAttacks().get(0).getName());
		assertEquals("Super Attack of Dragon is not loaded correctly from the CSV file of Dragons",
				"Perfect Special Beam Cannon", testDragons.get(1).getSuperAttacks().get(1).getName());
		assertEquals("Super Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Ice Ray",
				testDragons.get(1).getSuperAttacks().get(2).getName());

		assertEquals(
				"Ultimate Attacks of Dragon doesn't contain the same number of ultimate attacks given in the CSV file",
				2, testDragons.get(1).getUltimateAttacks().size());
		assertEquals("Ultimate Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Spirit Sword",
				testDragons.get(1).getUltimateAttacks().get(0).getName());
		assertEquals("Ultimate Attack of Dragon is not loaded correctly from the CSV file of Dragons", "Genocide Blast",
				testDragons.get(1).getUltimateAttacks().get(1).getName());
		try {

			File file = new File("DragonsTest.csv");
			file.delete();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@Test(timeout = 1000)
	public void testLoadedAttacks() throws Exception {
		ArrayList<Attack> attacks = new ArrayList<Attack>();

		attacks.add(new SuperAttack("Kamehameha", 250));
		attacks.add(new SuperAttack("Big Bang Attack", 250));
		attacks.add(new SuperAttack("Death Beam", 300));
		attacks.add(new SuperAttack("Masenko", 200));
		attacks.add(new SuperAttack("Destructo Disc", 200));
		attacks.add(new SuperAttack("Ki Blast Thrust", 150));
		attacks.add(new SuperAttack("Dodon Ray", 200));
		attacks.add(new SuperAttack("Tri-Beam", 250));
		attacks.add(new SuperAttack("Evil Explosion", 200));
		attacks.add(new SuperAttack("Double Sunday", 150));
		attacks.add(new SuperAttack("Shining Friday", 150));
		attacks.add(new SuperAttack("Bomber DX", 200));
		attacks.add(new SuperAttack("Galick Gun", 200));
		attacks.add(new SuperAttack("Saibabeam", 100));
		attacks.add(new SuperAttack("Beam Rifle", 100));
		attacks.add(new SuperAttack("Energy Shot", 150));
		attacks.add(new SuperAttack("Big Bang Kamehameha", 350));
		attacks.add(new SuperAttack("Emperor's Death Beam", 350));
		attacks.add(new SuperAttack("Demon Wave", 300));
		attacks.add(new SuperAttack("Guilty Flash", 300));
		attacks.add(new SuperAttack("Neo Dodon Ray", 300));
		attacks.add(new SuperAttack("Nova Star", 350));
		attacks.add(new SuperAttack("Ice Ray", 350));
		attacks.add(new SuperAttack("Kamehameha Cannon", 350));
		attacks.add(new SuperAttack("Headshot", 275));
		attacks.add(new SuperAttack("God Of Destruction's Wrath", 325));
		attacks.add(new SuperAttack("Prelude To Destruction", 350));
		attacks.add(new SuperAttack("Strike Of Revelation", 350));
		attacks.add(new SuperAttack("Blue Impulse", 200));
		attacks.add(new SuperAttack("Crusher Ball", 225));
		attacks.add(new SuperAttack("Death Cannon", 200));
		attacks.add(new SuperAttack("Death Wave", 200));
		attacks.add(new SuperAttack("Recoome Boom", 150));
		attacks.add(new SuperAttack("Space Mach", 200));
		attacks.add(new SuperAttack("Ginyu Storm", 200));
		attacks.add(new SuperAttack("Dynamite Pressure", 200));
		attacks.add(new SuperAttack("Sadistic 18", 250));
		attacks.add(new SuperAttack("Endgame", 200));
		attacks.add(new SuperAttack("Power Blitz", 250));
		attacks.add(new SuperAttack("Energy Shield", 200));
		attacks.add(new SuperAttack("Perfect Death Beam", 325));
		attacks.add(new SuperAttack("Perfect Special Beam Cannon", 300));
		attacks.add(new SuperAttack("Perfect Galick Gun", 300));
		attacks.add(new SuperAttack("Vanishing Beam", 275));
		attacks.add(new SuperAttack("Majin Kamehameha", 250));
		attacks.add(new SuperAttack("Splitting Hheadache", 200));
		attacks.add(new SuperAttack("Galactic Donut", 250));
		attacks.add(new SuperAttack("Innocence Breath", 225));
		attacks.add(new SuperAttack("Innocence Bullet", 250));
		attacks.add(new SuperAttack("Spirit Cannon", 275));
		attacks.add(new SuperAttack("Shine Shot", 200));

		attacks.add(new UltimateAttack("Super Kamehameha", 450));
		attacks.add(new UltimateAttack("Spirit Bomb", 500));
		attacks.add(new UltimateAttack("Final Flash", 450));
		attacks.add(new UltimateAttack("Vanishing Ball", 500));
		attacks.add(new UltimateAttack("Special Beam Cannon", 400));
		attacks.add(new UltimateAttack("Instant Kamehameha", 400));
		attacks.add(new UltimateAttack("Explosive Assault", 450));
		attacks.add(new UltimateAttack("Scatter-Kamehameha", 400));
		attacks.add(new UltimateAttack("Spirit Ball", 400));
		attacks.add(new UltimateAttack("Neo Tri-Beam", 400));
		attacks.add(new UltimateAttack("Weekend", 450));
		attacks.add(new UltimateAttack("Vacation Delete", 400));
		attacks.add(new UltimateAttack("Break Cannon", 475));
		attacks.add(new UltimateAttack("Giant Storm", 475));
		attacks.add(new UltimateAttack("Super Dodon Wave", 425));
		attacks.add(new UltimateAttack("Thunder Shock Surprise", 450));
		attacks.add(new UltimateAttack("Evil Containment Wave", 400));
		attacks.add(new UltimateAttack("Super Kamehameha Cannon", 500));
		attacks.add(new UltimateAttack("Angry Explosion", 500));
		attacks.add(new UltimateAttack("Sphere Of Destruction", 550));
		attacks.add(new UltimateAttack("Symphonic Destruction", 575));
		attacks.add(new UltimateAttack("Blue Hurricane", 425));
		attacks.add(new UltimateAttack("Red Magma", 425));
		attacks.add(new UltimateAttack("Death Ball", 500));
		attacks.add(new UltimateAttack("Recoome Eraser Gun", 450));
		attacks.add(new UltimateAttack("Ginyu Force Rampage", 425));
		attacks.add(new UltimateAttack("Super Electric Strike", 450));
		attacks.add(new UltimateAttack("Perfect Kamehameha", 475));
		attacks.add(new UltimateAttack("Perfect Spirit Bomb", 525));
		attacks.add(new UltimateAttack("Father-Son Kamehameha", 450));
		attacks.add(new UltimateAttack("Super Ghost Kamikaze", 450));
		attacks.add(new UltimateAttack("Genocide Blast", 450));
		attacks.add(new UltimateAttack("Spirit Sword", 525));
		attacks.add(new UltimateAttack("Final Kamehameha", 500));
		attacks.add(new UltimateAttack("Super Big Bang Kamehameha", 525));
		attacks.add(new UltimateAttack("Final Shine Attack", 500));
		attacks.add(new UltimateAttack("Final Galick Gun", 500));
		attacks.add(new UltimateAttack("Explosive Demon Wave", 450));

		attacks.add(new MaximumCharge());
		attacks.add(new SuperSaiyan());

		Game game = new Game();
		ArrayList<Attack> testAttacks = game.getAttacks();
		assertEquals("The loaded attacks ArrayList doesn't contain the same number of attacks given in the CSV file",
				attacks.size(), testAttacks.size());
		for (int i = 0; i < attacks.size(); i++) {
			assertEquals(
					"The attack " + attacks.get(i).getName()
							+ " should be created with the same type as its corresponding input in the CSV file",
					attacks.get(i).getClass(), testAttacks.get(i).getClass());
			assertEquals("A loaded attack should have the same name as its corresponding input in the CSV file",
					attacks.get(i).getName(), testAttacks.get(i).getName());
			assertEquals(
					"The attack " + attacks.get(i).getName()
							+ " should have the same damage as its corresponding input in the CSV file",
					attacks.get(i).getDamage(), testAttacks.get(i).getDamage());
		}
	}

	public static boolean containsMethodName(Method[] methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals(name))
				return true;
		}
		return false;
	}

	public static boolean TwoMapsEqual(Cell[][] map1, Cell[][] map2) {
		int different = 0;
		for (int i = 0; i < map1.length; i++) {
			for (int j = 0; j < map1[i].length; j++) {

				if (!map1[i][j].getClass().equals(map2[i][j].getClass()))
					different++;
				else {

					if (map1[i][j].getClass().equals(FoeCell.class) && !((FoeCell) map1[i][j]).getFoe().getName()
							.equals(((FoeCell) map2[i][j]).getFoe().getName()))
						different++;

					else if (map1[i][j].getClass().equals(CollectibleCell.class) && ((CollectibleCell) map1[i][j])
							.getCollectible() != ((CollectibleCell) map2[i][j]).getCollectible())
						different++;
				}
			}

		}
		if (different >= 100)
			return false;

		return true;

	}

	public static boolean uniqueLocations(ArrayList<Location> locations) {
		for (int i = 0; i < locations.size(); i++) {
			Location m = locations.get(i);
			int identical = 0;
			for (int j = i + 1; j < locations.size(); j++) {
				if (m.equals(locations.get(j)))
					identical++;
			}
			if (identical >= locations.size() / 2)
				return false;
		}
		return true;
	}
}
