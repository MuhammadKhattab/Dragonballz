package dragonball.model.attack;

import java.io.Serializable;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;

@SuppressWarnings("serial")
public abstract class Attack implements Serializable {
	private String name;
	private int damage;

	public Attack(String name, int damage) {
		this.name = name;
		this.damage = damage;
	}

	public String getName() {
		return name;
	}

	public int getDamage() {
		return damage;
	}

	public abstract int getAppliedDamage(BattleOpponent attacker);

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking)
			throws NotEnoughKiException {
		int damage = getAppliedDamage(attacker);
		if (attacker instanceof Saiyan && ((Saiyan) attacker).isTransformed())
			damage += (int) (.25 * damage);
		Fighter y = (Fighter) defender;
		int stam = y.getStamina();
		if (defenderBlocking)
			while (damage > 0 && stam > 0) {
				damage -= 100;
				stam--;
			}
		if (damage < 0)
			damage = 0;
		y.setStamina(stam);
		y.setHealthPoints(y.getHealthPoints() - damage);
	}

}
