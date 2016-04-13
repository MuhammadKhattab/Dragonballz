package dragonball.model.character.fighter;

import java.util.*;
import dragonball.model.attack.*;
import dragonball.model.character.PlayableCharacter;

public abstract class PlayableFighter extends Fighter implements PlayableCharacter {
	public static final int INITIAL_LEVEL = 1;
	public static final int INITIAL_XP = 0;
	public static final int INITIAL_TARGET_XP = 10;
	public static final int INITIAL_ABILITY_POINTS = 0;
	private int xp;
	private int targetXp;
	private int abilityPoints;

	public PlayableFighter(String name, int level, int xp, int targetXp, int maxHealthPoints, int blastDamage,
			int physicalDamage, int abilityPoints, int maxKi, int maxStamina, ArrayList<SuperAttack> superAttacks,
			ArrayList<UltimateAttack> ultimateAttacks) {
		super(name, level, maxHealthPoints, blastDamage, physicalDamage, maxKi, maxStamina, superAttacks,
				ultimateAttacks);
		this.xp = xp;
		this.targetXp = targetXp;
		this.abilityPoints = abilityPoints;
		
	}

	public PlayableFighter(String name, int maxHealthPoints, int blastDamage, int physicalDamage, int maxKi,
			int maxStamina, ArrayList<SuperAttack> superAttacks, ArrayList<UltimateAttack> ultimateAttacks) {
		this(name, INITIAL_LEVEL, INITIAL_XP, INITIAL_TARGET_XP, maxHealthPoints, blastDamage, physicalDamage,
				INITIAL_ABILITY_POINTS, maxKi, maxStamina, superAttacks, ultimateAttacks);
	}

	public PlayableFighter(String name, int maxHealthPoints, int blastDamage, int physicalDamage, int maxKi,
			int maxStamina) {
		this(name, maxHealthPoints, blastDamage, physicalDamage, maxKi, maxStamina, new ArrayList<SuperAttack>(),
				new ArrayList<UltimateAttack>());
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		int target = getTargetXp(), pts = getAbilityPoints(), lvl = getLevel();
		// leveling up
		while (xp >= target) {
			xp -= target;
			target += 20;
			pts += 2;
			lvl++;
		}
		this.xp = xp;
		setAbilityPoints(pts);
		setTargetXp(target);
		setLevel(lvl);
	}

	public int getTargetXp() {
		return targetXp;
	}

	public void setTargetXp(int targetXp) {
		this.targetXp = targetXp;
	}

	public int getAbilityPoints() {
		return abilityPoints;
	}

	public void setAbilityPoints(int abilityPoints) {
		this.abilityPoints = abilityPoints;
	}

}
