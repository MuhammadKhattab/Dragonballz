package dragonball.model.player;

import java.io.Serializable;
import java.util.*;
import dragonball.model.attack.*;
import dragonball.model.character.fighter.*;
import dragonball.model.dragon.DragonWish;
import dragonball.model.exceptions.DuplicateAttackException;
import dragonball.model.exceptions.MaximumAttacksLearnedException;
import dragonball.model.exceptions.NotASaiyanException;
import dragonball.model.exceptions.NotEnoughAbilityPointsException;

@SuppressWarnings("serial")
public class Player implements Serializable {
	private String name;
	private ArrayList<PlayableFighter> fighters;
	private ArrayList<SuperAttack> superAttacks;
	private ArrayList<UltimateAttack> ultimateAttacks;
	private PlayableFighter activeFighter;
	private int senzuBeans;
	private int dragonBalls;
	private int exploredMaps;
	private PlayerListener listener;
	private static final int MAX_SUPER_ATTACKS_NUMBER = 4, MAX_ULTIMATE_ATTACKS_NUMBER = 2;

	public Player(String name) {
		this(name, new ArrayList<PlayableFighter>(), new ArrayList<SuperAttack>(), new ArrayList<UltimateAttack>(), 0,
				0, null, 0);
	}

	public Player(String name, ArrayList<PlayableFighter> fighters, ArrayList<SuperAttack> superAttacks,
			ArrayList<UltimateAttack> ultimateAttacks, int senzuBeans, int dragonBalls, PlayableFighter activeFighter,
			int exploredMaps) {
		this.name = name;
		this.fighters = fighters;
		this.superAttacks = superAttacks;
		this.ultimateAttacks = ultimateAttacks;
		this.senzuBeans = senzuBeans;
		this.dragonBalls = dragonBalls;
		this.activeFighter = activeFighter;
		this.exploredMaps = exploredMaps;
	}

	public int getMaxFighterLevel() {
		int max = 1;
		for (PlayableFighter x : fighters)
			max = Math.max(max, x.getLevel());
		return max;
	}

	public void setFighters(ArrayList<PlayableFighter> fighters) {
		this.fighters = fighters;
	}

	public void setSuperAttacks(ArrayList<SuperAttack> superAttacks) {
		this.superAttacks = superAttacks;
	}

	public void setUltimateAttacks(ArrayList<UltimateAttack> ultimateAttacks) {
		this.ultimateAttacks = ultimateAttacks;
	}

	public void setActiveFighter(PlayableFighter activeFighter) {
		this.activeFighter = activeFighter;
	}

	public ArrayList<PlayableFighter> getFighters() {
		return fighters;
	}

	public ArrayList<SuperAttack> getSuperAttacks() {
		return superAttacks;
	}

	public ArrayList<UltimateAttack> getUltimateAttacks() {
		return ultimateAttacks;
	}

	public PlayableFighter getActiveFighter() {
		return activeFighter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSenzuBeans() {
		return senzuBeans;
	}

	public void setSenzuBeans(int senzuBeans) {
		this.senzuBeans = senzuBeans;
	}

	public int getDragonBalls() {
		return dragonBalls;
	}

	public void setDragonBalls(int dragonBalls) {
		this.dragonBalls = dragonBalls;
	}

	public int getExploredMaps() {
		return exploredMaps;
	}

	public void setExploredMaps(int exploredMaps) {
		this.exploredMaps = exploredMaps;
	}

	public PlayerListener getListener() {
		return listener;
	}

	public void setListener(PlayerListener listener) {
		this.listener = listener;
	}

	public void callDragon() {
		dragonBalls -= 7;
		notifyOnDragonCalled();
	}

	public void notifyOnDragonCalled() {
		if (listener != null) {
			listener.onDragonCalled();
		}
	}

	public void notifyOnWishChosen(DragonWish wish) {
		if (listener != null) {
			listener.onWishChosen(wish);
		}
	}

	public void chooseWish(DragonWish wish) {
		switch (wish.getType()) {
		case SENZU_BEANS:
			senzuBeans += wish.getSenzuBeans();
			break;
		case ABILITY_POINTS:
			activeFighter.setAbilityPoints(activeFighter.getAbilityPoints() + wish.getAbilityPoints());
			break;
		case SUPER_ATTACK:
			superAttacks.add(wish.getSuperAttack());
			break;
		case ULTIMATE_ATTACK:
			ultimateAttacks.add(wish.getUltimateAttack());
			break;
		default:
			break;
		}

		notifyOnWishChosen(wish);
	}

	public void createFighter(char race, String name) throws Exception {
		for(PlayableFighter x: fighters)
			if(x.getName().equals(name))
				throw new Exception("This name already exists!");
		PlayableFighter neu = null;
		switch (race) {
		case 'E':
			neu = new Earthling(name);
			break;
		case 'S':
			neu = new Saiyan(name);
			break;
		case 'N':
			neu = new Namekian(name);
			break;
		case 'F':
			neu = new Frieza(name);
			break;
		case 'M':
			neu = new Majin(name);
		}
		if (neu != null) {
			if (fighters.size() == 0)
				activeFighter = neu;
			fighters.add(neu);
		}
	}

	public void upgradeFighter(PlayableFighter fighter, char fighterAttribute) throws NotEnoughAbilityPointsException {
		if (!fighters.contains(fighter))
			return;
		int pts = fighter.getAbilityPoints();
		if (pts > 0) {
			fighter.setAbilityPoints(pts - 1);
			switch (fighterAttribute) {
			case 'H':
				fighter.setMaxHealthPoints(fighter.getMaxHealthPoints() + 50);
				break;
			case 'P':
				fighter.setPhysicalDamage(fighter.getPhysicalDamage() + 50);
				break;
			case 'B':
				fighter.setBlastDamage(fighter.getBlastDamage() + 50);
				break;
			case 'K':
				fighter.setMaxKi(fighter.getMaxKi() + 1);
				break;
			case 'S':
				fighter.setMaxStamina(fighter.getMaxStamina() + 1);
				break;
			default:
				return;
			}
		} else {
			throw new NotEnoughAbilityPointsException();
		}
	}

	public void assignAttack(PlayableFighter fighter, SuperAttack newAttack, SuperAttack oldAttack)
			throws DuplicateAttackException, MaximumAttacksLearnedException {
		if (fighter.getSuperAttacks().contains(newAttack))
			throw new DuplicateAttackException(newAttack);
		else {
			ArrayList<SuperAttack> x = fighter.getSuperAttacks();
			if (oldAttack == null) {
				if (x.size() < MAX_SUPER_ATTACKS_NUMBER)
					x.add(newAttack);
				else
					throw new MaximumAttacksLearnedException();
			} else {
				int ind = x.indexOf(oldAttack);
				x.remove(ind);
				x.add(ind, newAttack);
			}
		}
	}

	public void assignAttack(PlayableFighter fighter, UltimateAttack newAttack, UltimateAttack oldAttack)
			throws DuplicateAttackException, NotASaiyanException, MaximumAttacksLearnedException {

		if (fighter.getUltimateAttacks().contains(newAttack))
			throw new DuplicateAttackException(newAttack);
		else {
			if (newAttack instanceof SuperSaiyan && !(fighter instanceof Saiyan))
				throw new NotASaiyanException();
			else {
				ArrayList<UltimateAttack> x = fighter.getUltimateAttacks();
				if (oldAttack == null) {
					if (x.size() < MAX_ULTIMATE_ATTACKS_NUMBER)
						x.add(newAttack);
					else
						throw new MaximumAttacksLearnedException();
				} else {
					int ind = x.indexOf(oldAttack);
					x.remove(ind);
					x.add(ind, newAttack);
				}
			}
		}
	}

}
